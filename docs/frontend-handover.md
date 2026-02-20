# Frontend Handover (Backend)

Este documento sirve para pasar datos de integracion al repositorio Front.

## Datos que necesita Front

1. `BACKEND_PUBLIC_URL`
- URL base publica del backend (sin `/api/v1`).
- Ejemplo: `https://ecoaula-backend.onrender.com`

2. `Incluye /api/v1?`
- Respuesta: `no`

3. `Endpoint de prueba publico`
- `GET {BACKEND_PUBLIC_URL}/api/v1/health`

4. `CORS_ALLOWED_ORIGINS`
- Sale de `FRONTEND_URL` (admite lista separada por comas).
- Ejemplo:
  - `https://tu-org.github.io,https://tu-front.vercel.app`

5. `Endpoints que deben responder`
- `GET /api/v1/containers/summary`
- `POST /api/v1/users`
- `POST /api/v1/wastes`

## Variables minimas para prod

- `SPRING_PROFILES_ACTIVE=prod`
- `SPRING_DATASOURCE_URL`
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`
- `FRONTEND_URL`

Opcional:
- `APP_MAIL_ENABLED=false` si no hay SMTP aun
- `SMTP_HOST`, `SMTP_PORT`, `SMTP_USERNAME`, `SMTP_PASSWORD` (si correo activo)
