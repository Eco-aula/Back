# Documentacion de testing backend

## Estado real (verificado)

Ultima verificacion manual: **20-02-2026**

Comando ejecutado:

```bash
mvnw clean verify
```

Resultado:

- Build: `SUCCESS`
- Tests totales: `81`
- Fallos: `0`
- Errores: `0`
- Suites ejecutadas: `20`
- Gate JaCoCo activo: `INSTRUCTION >= 75%`

Este documento reemplaza versiones antiguas que indicaban cobertura `100%`.

## Objetivo de esta guia

- Explicar como se prueba el backend hoy.
- Mostrar cobertura real y trazable.
- Definir workflow local y CI.
- Priorizar mejoras de cobertura sin inflar metricas.

## Estrategia de pruebas

El proyecto usa una estrategia por capas:

- Unit tests de servicios y dominio.
- Controller tests con MockMvc (`@WebMvcTest`).
- Integracion con contexto Spring (`@SpringBootTest`).
- End-to-end backend (MockMvc + repos reales + perfil `test`).

## Stack de testing

- JUnit 5
- Mockito
- Spring Boot Test
- MockMvc
- H2 en memoria (perfil `test`)
- JaCoCo para cobertura y quality gate

## Inventario actual de suites

| Tipo | Clases principales | Estado |
| --- | --- | --- |
| Unit - service | `ContainerServiceImplTest`, `EmailServiceImplTest`, `UserServiceImplTest`, `WasteServiceImplTest` | Activo |
| Unit - dominio | `CategoryTest`, `ContainerTest`, `StateTest`, `UserTest`, `WasteTest` | Activo |
| Unit - DTO/config/exception | `CategoryVolumeDTOTest`, `ContainerStatusDTOTest`, `UpdateFillDTOTest`, `WasteStatusDTOTest`, `DotenvConfigTest`, `ContainerNotFoundExceptionTest` | Activo |
| Controller slice | `ContainerControllerWebMvcTest`, `UserControllerWebMvcTest`, `WasteControllerWebMvcTest` | Activo |
| Integracion contexto | `EcoaulaApplicationTests`, `UserControllerTest` | Activo |
| End-to-end backend | `UserControllerIT` | Activo |
| Repository (`@DataJpaTest`) | `ContainerRepositoryDataJpaTest` | Inactivo (archivo comentado) |

## Entorno reproducible de tests

Archivo clave:

- `src/test/resources/application-test.properties`

Puntos importantes:

- Base de datos H2 en memoria: `jdbc:h2:mem:ecoaula_test`
- `spring.jpa.hibernate.ddl-auto=create-drop`
- Perfil de pruebas desacoplado de PostgreSQL
- Sin dependencia de variables `DB_*` para correr tests

## Cobertura actual (JaCoCo)

Snapshot del run de **20-02-2026**:

| Metrica | Cobertura |
| --- | --- |
| Instrucciones | `87.04%` |
| Branches | `83.56%` |
| Lineas | `86.27%` |
| Complejidad | `86.36%` |
| Metodos | `89.66%` |
| Clases | `95.45%` |

Reporte local:

- `target/site/jacoco/index.html`

## Hotspots de cobertura baja

Segun `target/site/jacoco/jacoco.csv`:

| Clase | Cobertura instrucciones | Observacion |
| --- | --- | --- |
| `ContainerSummaryDTO` | `0.00%` | DTO sin test dedicado |
| `ContainerController` | `50.85%` | Faltan casos para endpoints PATCH y errores |
| `ContainerServiceImpl` | `58.97%` | Faltan ramas de negocio y errores |
| `ContainerReminderScheduler` | `61.76%` | Falta test directo del scheduler |

## Workflows de testing

### Workflow local

```mermaid
flowchart TD
    A[Escribir o ajustar test] --> B[Ejecutar mvnw test]
    B --> C{Todo verde?}
    C -- No --> A
    C -- Si --> D[Ejecutar mvnw clean verify]
    D --> E[Revisar JaCoCo HTML]
    E --> F{Gate >= 75%?}
    F -- No --> A
    F -- Si --> G[Preparar PR]
```

### Workflow CI

Archivo:

- `.github/workflows/backend-tests.yml`

```mermaid
flowchart LR
    PR[Push o Pull Request] --> CI[GitHub Actions]
    CI --> JDK[Setup Temurin 21 + cache Maven]
    JDK --> VERIFY[Run mvnw clean verify]
    VERIFY --> ART1[Artifact: surefire reports]
    VERIFY --> ART2[Artifact: JaCoCo HTML]
```

## Comandos operativos

Windows:

```bash
.\mvnw.cmd test
.\mvnw.cmd clean verify
```

Linux/macOS:

```bash
./mvnw test
./mvnw clean verify
```

Script util para validar alertas por correo:

```bash
powershell -ExecutionPolicy Bypass -File .\scripts\verify-alerts.ps1
```

## Riesgos tecnicos detectados

- Hay dos reglas de umbral para estado de contenedor (50/90 en entidad y 70/90 en servicio). Conviene unificar y cubrir con tests para evitar comportamiento inconsistente.
- La capa repository no tiene pruebas activas porque `ContainerRepositoryDataJpaTest` esta comentado.

## Plan de mejora propuesto (priorizado)

1. Reactivar `ContainerRepositoryDataJpaTest` y añadir casos de agregacion por categoria.
2. Subir cobertura de `ContainerController` con casos de error (`404`, `400`, estados invalidos).
3. Añadir tests unitarios directos de `ContainerReminderScheduler` con mocks de repo/email.
4. Añadir tests para `ContainerSummaryDTO` y ramas faltantes de `ContainerServiceImpl`.
5. Publicar badge dinamico de CI/cobertura cuando el repo este en GitHub.

## Criterio de calidad recomendado para PR

- Ningun test roto (`mvnw test`).
- `mvnw clean verify` en verde.
- No bajar de `75%` instrucciones (gate actual).
- Si se toca logica de negocio, incluir prueba nueva que cubra al menos una rama adicional.
