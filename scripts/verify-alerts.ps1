param(
    [string]$BaseUrl = "http://localhost:8080",
    [int]$ScanMaxId = 50,
    [switch]$SkipOpenMailhog
)

Set-StrictMode -Version Latest
$ErrorActionPreference = "Stop"
$script:MailProvider = $null
$script:MailWebPort = 8025

function Write-Step {
    param([string]$Message)
    Write-Host "==> $Message" -ForegroundColor Cyan
}

function Test-TcpPort {
    param(
        [string]$HostName,
        [int]$Port
    )

    try {
        $client = New-Object System.Net.Sockets.TcpClient
        $async = $client.BeginConnect($HostName, $Port, $null, $null)
        $ok = $async.AsyncWaitHandle.WaitOne(1000, $false)
        if (-not $ok) {
            $client.Close()
            return $false
        }
        $client.EndConnect($async)
        $client.Close()
        return $true
    } catch {
        return $false
    }
}

function Invoke-Api {
    param(
        [string]$Method,
        [string]$Uri,
        [object]$Body
    )

    $params = @{
        Method      = $Method
        Uri         = $Uri
        TimeoutSec  = 20
        ErrorAction = "Stop"
    }

    if ($PSBoundParameters.ContainsKey("Body")) {
        $params["ContentType"] = "application/json"
        $params["Body"] = ($Body | ConvertTo-Json -Depth 10)
    }

    try {
        return Invoke-RestMethod @params
    } catch {
        throw "Fallo en API [$Method] $Uri`n$($_.Exception.Message)"
    }
}

function Ensure-MailHog {
    if (Test-TcpPort -HostName "127.0.0.1" -Port 1025) {
        $detected = Get-RunningMailProvider
        if ($null -ne $detected) {
            $script:MailProvider = $detected.Provider
            $script:MailWebPort = [int]$detected.WebPort
            Write-Step "Buzon detectado: $($detected.Provider) en puerto web $($detected.WebPort)"
            return
        }
    }

    $dockerStarted = $false
    $docker = Get-Command docker -ErrorAction SilentlyContinue
    if (($null -ne $docker) -and (Test-DockerEngineAvailable)) {
        $containerName = "ecoaula-mailhog"
        $existing = docker ps -a --format "{{.Names}}" 2>$null | Where-Object { $_ -eq $containerName }

        if ($existing) {
            Write-Step "Intentando arrancar MailHog (Docker)..."
            docker start $containerName 2>$null | Out-Null
            $dockerStarted = ($LASTEXITCODE -eq 0)
        } else {
            Write-Step "Intentando crear MailHog (Docker)..."
            docker run -d --name $containerName -p 1025:1025 -p 8025:8025 mailhog/mailhog 2>$null | Out-Null
            $dockerStarted = ($LASTEXITCODE -eq 0)
        }
    }

    if ($dockerStarted) {
        Start-Sleep -Seconds 2
    }

    if (-not (Test-TcpPort -HostName "127.0.0.1" -Port 1025)) {
        $mailDevWebPort = 8025
        if (Test-TcpPort -HostName "127.0.0.1" -Port 8025) {
            $mailDevWebPort = 1080
        }

        if (Start-MailDev -WebPort $mailDevWebPort) {
            Write-Step "MailDev iniciado (sin Docker)."
        }
    }

    if (-not (Test-TcpPort -HostName "127.0.0.1" -Port 1025)) {
        throw "No se pudo levantar un buzon SMTP local (ni MailHog ni MailDev)."
    }

    $detected = Get-RunningMailProvider
    if ($null -eq $detected) {
        throw "SMTP activo en 1025, pero no detecto API web de buzon local."
    }

    $script:MailProvider = $detected.Provider
    $script:MailWebPort = [int]$detected.WebPort
    Write-Step "Buzon detectado: $($detected.Provider) en puerto web $($detected.WebPort)"
}

function Test-DockerEngineAvailable {
    $dockerPipes = @(
        "\\.\pipe\dockerDesktopLinuxEngine",
        "\\.\pipe\docker_engine"
    )

    foreach ($pipe in $dockerPipes) {
        if (Test-Path $pipe) {
            return $true
        }
    }

    return $false
}

function Start-MailDev {
    param([int]$WebPort)

    $node = Get-Command node -ErrorAction SilentlyContinue
    if ($null -eq $node) {
        return $false
    }

    Write-Step "Intentando iniciar MailDev (sin Docker) en puerto web $WebPort..."
    try {
        Start-Process -FilePath "cmd.exe" `
            -ArgumentList "/c npx --yes maildev --smtp 1025 --web $WebPort --ip 127.0.0.1 --silent" `
            -WindowStyle Hidden | Out-Null
    } catch {
        return $false
    }

    Start-Sleep -Seconds 5
    return ((Test-TcpPort -HostName "127.0.0.1" -Port 1025) -and
            (Test-TcpPort -HostName "127.0.0.1" -Port $WebPort))
}

function Get-MailProvider {
    param([int]$WebPort)

    try {
        $mailhog = Invoke-RestMethod -Method Get -Uri "http://localhost:$WebPort/api/v2/messages" -TimeoutSec 5
        if ($null -ne $mailhog) {
            return "mailhog"
        }
    } catch {
        # Intentamos la API alternativa de MailDev.
    }

    try {
        $maildev = Invoke-RestMethod -Method Get -Uri "http://localhost:$WebPort/email" -TimeoutSec 5
        if ($null -ne $maildev) {
            return "maildev"
        }
    } catch {
        return $null
    }
}

function Get-RunningMailProvider {
    $candidatePorts = @(8025, 1080)
    foreach ($port in $candidatePorts) {
        if (-not (Test-TcpPort -HostName "127.0.0.1" -Port $port)) {
            continue
        }

        $provider = Get-MailProvider -WebPort $port
        if ($null -ne $provider) {
            return @{
                Provider = $provider
                WebPort  = $port
            }
        }
    }

    return $null
}

function Test-Backend {
    Write-Step "Comprobando backend en $BaseUrl ..."
    try {
        # 200 o 204 ambos son validos para esta comprobacion.
        Invoke-WebRequest -UseBasicParsing -Uri "$BaseUrl/api/v1/containers/summary" -Method GET -TimeoutSec 10 | Out-Null
    } catch {
        if ($_.Exception.Response -and
            [int]$_.Exception.Response.StatusCode -eq 204) {
            return
        }
        throw "No responde el backend en $BaseUrl. Arranca la app y reintenta."
    }
}

function Find-ContainerId {
    param([int]$MaxId)

    foreach ($id in 1..$MaxId) {
        try {
            $status = Invoke-Api -Method "GET" -Uri "$BaseUrl/api/v1/containers/$id/status"
            if ($null -ne $status -and $null -ne $status.id) {
                return [int]$status.id
            }
        } catch {
            continue
        }
    }
    return $null
}

Write-Step "Verificacion automatica de alertas por correo"
Ensure-MailHog
Test-Backend

$timestamp = Get-Date -Format "yyyyMMddHHmmss"
$email = "demo.alerta.$timestamp@test.local"

Write-Step "Creando usuario receptor: $email"
$user = Invoke-Api -Method "POST" -Uri "$BaseUrl/api/v1/users" -Body @{
    name     = "Demo Alertas"
    email    = $email
    password = "1234"
}

$containerId = Find-ContainerId -MaxId $ScanMaxId
if ($null -eq $containerId) {
    Write-Step "No hay contenedores visibles. Creo uno con un residuo PLASTIC..."
    $null = Invoke-Api -Method "POST" -Uri "$BaseUrl/api/v1/wastes" -Body @{
        name        = "Botella Demo"
        description = "Prueba alertas"
        heavy       = 1
        category    = "PLASTIC"
    }
    $containerId = Find-ContainerId -MaxId $ScanMaxId
}

if ($null -eq $containerId) {
    throw "No pude localizar ningun contenedor para disparar alertas."
}

Write-Step "Usando contenedor ID: $containerId"

# Forzamos transiciones de estado para asegurar notificacion:
# EMPTY -> LIMIT -> FULL.
Write-Step "Forzando estado EMPTY (10%)..."
$null = Invoke-Api -Method "PUT" -Uri "$BaseUrl/api/v1/containers/$containerId/fill" -Body @{
    percentage = 10
}

Write-Step "Forzando estado LIMIT (70%)..."
$null = Invoke-Api -Method "PUT" -Uri "$BaseUrl/api/v1/containers/$containerId/fill" -Body @{
    percentage = 70
}

Write-Step "Forzando estado FULL (95%)..."
$null = Invoke-Api -Method "PUT" -Uri "$BaseUrl/api/v1/containers/$containerId/fill" -Body @{
    percentage = 95
}

Start-Sleep -Seconds 2

Write-Step "Consultando emails en el buzon ($script:MailProvider)..."

$messagesForUser = @()

if ($script:MailProvider -eq "mailhog") {
    $mailhog = Invoke-RestMethod -Method Get -Uri "http://localhost:$script:MailWebPort/api/v2/messages" -TimeoutSec 10
    $allMessages = @($mailhog.items)

    $messagesForUser = $allMessages | Where-Object {
        $toHeader = $_.Content.Headers.To
        if ($toHeader -is [array]) {
            ($toHeader -join " ") -match [regex]::Escape($email)
        } else {
            "$toHeader" -match [regex]::Escape($email)
        }
    }
}

if ($script:MailProvider -eq "maildev") {
    $maildev = Invoke-RestMethod -Method Get -Uri "http://localhost:$script:MailWebPort/email" -TimeoutSec 10
    $allMessages = @($maildev)

    $messagesForUser = $allMessages | Where-Object {
        $mailJson = $_ | ConvertTo-Json -Depth 20 -Compress
        $mailJson -match [regex]::Escape($email)
    }
}

$count = @($messagesForUser).Count

Write-Host ""
Write-Host "RESULTADO" -ForegroundColor Green
Write-Host "Usuario demo: $email"
Write-Host "Contenedor usado: $containerId"
Write-Host "Mensajes recibidos para ese usuario: $count"

if ($count -eq 0) {
    Write-Host "No se encontraron correos para el usuario demo." -ForegroundColor Yellow
    Write-Host "Revisa logs del backend y que este usando SMTP localhost:1025."
    exit 1
}

Write-Host ""
Write-Host "Asuntos encontrados:" -ForegroundColor Green
$messagesForUser | ForEach-Object {
    $subject = ""

    if ($script:MailProvider -eq "mailhog") {
        $subject = $_.Content.Headers.Subject
        if ($subject -is [array]) {
            $subject = $subject -join " "
        }
    } elseif ($script:MailProvider -eq "maildev") {
        $subject = $_.subject
    }

    if ([string]::IsNullOrWhiteSpace("$subject")) {
        $subject = "(sin asunto)"
    }

    Write-Host "- $subject"
}

if (-not $SkipOpenMailhog) {
    Start-Process "http://localhost:$script:MailWebPort"
}

Write-Host ""
Write-Host "OK: las alertas estan llegando por email." -ForegroundColor Green
