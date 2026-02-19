# Documentacion tecnica de pruebas backend

## Estado actual

El backend tiene pruebas unitarias, de controlador, de integracion y
verificacion de cobertura automatica.

## 1. Ramas de trabajo

Durante la implementacion del sistema de testing se usaron dos ramas para
aislar cambios y mantener estabilidad.

### `test/backend`

Rama de construccion inicial del sistema de pruebas.

- Tests unitarios de servicios.
- Tests de controller con `@WebMvcTest`.
- Primeras pruebas de integracion.
- Integracion inicial de JaCoCo.

### `fix/tests-backend-h2`

Rama de estabilizacion del entorno de pruebas.

Objetivos principales:

- Eliminar dependencia de PostgreSQL en tests.
- Integrar H2 en memoria.
- Configurar correctamente el perfil `test`.
- Resolver fallos de `@SpringBootTest`.
- Aumentar cobertura.
- Garantizar JaCoCo >= 75%.

Una vez validado el flujo completo, los cambios se integraron en `dev`.

```bash
./mvnw clean verify
```

## 2. Problema inicial detectado

Al ejecutar:

```bash
./mvnw test
```

el test `@SpringBootTest` intentaba:

- Levantar el contexto completo.
- Conectarse a PostgreSQL.
- Leer variables de entorno (`DB_HOST`, `DB_NAME`, etc.).

El entorno fallaba cuando:

- No existia PostgreSQL configurado.
- No estaban definidas variables de entorno.
- Se ejecutaba en CI sin base de datos real.

Error tipico:

```text
Cannot load driver class: org.h2.Driver
```

## 3. Solucion aplicada: entorno reproducible

Se desacoplo el entorno de tests de PostgreSQL usando H2 en memoria y
perfil dedicado.

Archivo de configuracion:

```text
src/test/resources/application-test.properties
```

Configuracion clave:

```properties
spring.datasource.url=jdbc:h2:mem:ecoaula_test
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.test.database.replace=ANY
```

Dependencia en `pom.xml`:

```xml
<dependency>
  <groupId>com.h2database</groupId>
  <artifactId>h2</artifactId>
  <scope>test</scope>
</dependency>
```

Resultado:

- Los tests corren en cualquier maquina.
- No dependen de servicios externos.

## 4. Integracion de JaCoCo

JaCoCo se integra con los objetivos:

- `prepare-agent`
- `report`
- `check` (umbral minimo 75%)

Comandos relevantes:

```bash
./mvnw clean test
./mvnw clean test jacoco:report
./mvnw clean verify
```

Reporte generado en:

```text
target/site/jacoco/index.html
```

## 5. Pruebas unitarias (service)

Tecnologias:

- JUnit 5
- Mockito
- `@ExtendWith(MockitoExtension.class)`

Clases cubiertas:

- `UserServiceImpl`
- `ContainerServiceImpl`
- `WasteServiceImpl`
- `EmailServiceImpl`

Estrategia:

- Casos positivos y negativos.
- Validacion de excepciones.
- Verificacion de interacciones con repositorios.
- Verificacion de envios de email.
- Cobertura de ramas.

## 6. Pruebas de controller (slice MVC)

Tecnologia:

- `@WebMvcTest`
- `MockMvc`
- Servicios simulados con `@MockBean`.

Caracteristicas:

- Sin levantar contexto completo.
- Sin base de datos real.
- Sin repositorios reales.

Endpoints cubiertos:

- `Users`
- `Containers`
- `Wastes`

Codigos verificados:

- `200 OK`
- `201 CREATED`
- `204 NO_CONTENT`
- `400 BAD_REQUEST`
- `404 NOT_FOUND`

## 7. Pruebas de integracion (contexto completo)

Clase principal:

- `EcoaulaApplicationTests`

Configuracion:

- `@SpringBootTest`
- `@ActiveProfiles("test")`

Validaciones:

- Levantamiento real del contexto.
- Configuracion correcta de H2.
- Integracion completa sin mocks.

## 8. Pruebas End-to-End backend

Clases tipo:

- `UserControllerIT`
- `ContainerControllerIT`
- `WasteControllerIT`

Estrategia:

- `@SpringBootTest`
- `@AutoConfigureMockMvc`
- Perfil `test`
- H2 en memoria

Flujos reales:

- `POST -> GET`
- `PUT -> GET`
- `DELETE -> verificacion posterior`

Ejecucion sin mocks y con repositorios reales.

## 9. Cobertura actual

Validacion:

```bash
./mvnw clean verify
```

Resultado actual:

- Instrucciones: `100%`
- Branches: `100%`
- Metodos: `100%`
- Clases: `100%`
- Cobertura total: `100%`

Esto permite detectar regresiones de comportamiento de forma inmediata.
