🧪 DOCUMENTACIÓN TÉCNICA – PRUEBAS BACKEND
🚀 Estado Actualizado
🧭 1) Ramas de trabajo

Durante la implementación del sistema de testing del backend se trabajó en dos ramas diferenciadas para aislar cambios y garantizar estabilidad.

🔹 test/backend

Rama dedicada a la construcción inicial del sistema de pruebas:

✔ Tests unitarios de servicios

✔ Tests de controller con @WebMvcTest

✔ Primeras pruebas de integración

✔ Integración inicial de JaCoCo

Permitió construir la base de testing sin afectar la estabilidad de dev.

🔹 fix/tests-backend-h2

Rama de estabilización y mejora del entorno de testing.

Objetivos principales:

🔧 Eliminar dependencia de PostgreSQL en tests

🧠 Integrar H2 en memoria

⚙ Configurar correctamente el perfil test

🛠 Resolver fallos de @SpringBootTest

📈 Aumentar cobertura al máximo posible

✅ Garantizar cumplimiento de JaCoCo ≥ 75%

Una vez validado que el comando:

./mvnw clean verify


ejecutaba correctamente y la cobertura era satisfactoria, los cambios fueron integrados en dev.

🚨 2) Problema inicial detectado

Al ejecutar:

./mvnw test


El test @SpringBootTest intentaba:

Levantar el contexto completo.

Conectarse a PostgreSQL.

Leer variables de entorno (DB_HOST, DB_NAME, etc.).

❌ Problema

El entorno fallaba cuando:

No existía PostgreSQL configurado.

No estaban definidas variables de entorno.

Se ejecutaba en equipos externos o CI sin base de datos real.

🔎 Error típico
Cannot load driver class: org.h2.Driver


o errores de conexión a PostgreSQL.

🧩 3) Solución aplicada – Entorno reproducible

Se implementa un entorno totalmente desacoplado de PostgreSQL.

🗄 Base de datos

H2 en memoria exclusiva para tests.

Configuración aislada mediante perfil test.

📂 Perfil de test

Archivo:

src/test/resources/application-test.properties


Configuración clave:

spring.datasource.url=jdbc:h2:mem:ecoaula_test
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.test.database.replace=ANY

📦 Dependencia añadida en pom.xml
<dependency>
  <groupId>com.h2database</groupId>
  <artifactId>h2</artifactId>
  <scope>test</scope>
</dependency>

🎯 Resultado

Los tests se ejecutan en cualquier máquina sin dependencia externa.

📊 4) Integración de JaCoCo

Se integra JaCoCo con:

prepare-agent

report

check (umbral mínimo 75%)

🔧 Comandos relevantes
./mvnw clean test
./mvnw clean test jacoco:report
./mvnw clean verify

📈 Reporte generado en
target/site/jacoco/index.html

🧪 5) Pruebas unitarias – Capa Service
🛠 Tecnologías

JUnit 5

Mockito

@ExtendWith(MockitoExtension.class)

📚 Clases cubiertas

UserServiceImpl

ContainerServiceImpl

WasteServiceImpl

EmailServiceImpl (si aplica)

🧠 Estrategia

Casos positivos

Casos negativos

Validación de excepciones

Verificación de interacciones con repositorios

Verificación de envíos de email

Cobertura de ramas (branch coverage)

🌐 6) Pruebas de Controller – Slice MVC
🛠 Tecnología

@WebMvcTest

MockMvc

@MockBean de servicios

🔍 Características

Sin levantar contexto completo

Sin base de datos real

Sin repositorios reales

📌 Endpoints cubiertos

Users

Containers

Wastes

Casos cubiertos:

200 OK

201 CREATED

204 NO_CONTENT

400 BAD_REQUEST

404 NOT_FOUND

🧠 7) Pruebas de integración – Contexto completo

Clase:

EcoaulaApplicationTests


Configuración:

@SpringBootTest

@ActiveProfiles("test")

Verifica:

✔ Levantamiento real del contexto

✔ Configuración correcta de H2

✔ Integración completa sin mocks

🔁 8) Pruebas End-to-End Backend

Clases tipo:

UserControllerIT

ContainerControllerIT

WasteControllerIT

⚙ Estrategia

@SpringBootTest

@AutoConfigureMockMvc

Perfil test

Base de datos H2 real en memoria

🔄 Flujos reales

POST → GET

PUT → GET

DELETE → verificación posterior

Sin mocks.
Con repositorios reales.

📈 9) Cobertura actual

Validación:

./mvnw clean verify

🎯 Resultado

Instrucciones: 100%

Branches: 100%

Métodos: 100%

Clases: 100%

Total: 100% cobertura real del backend.

Esto garantiza que cualquier modificación futura que rompa comportamiento existente provocará fallo inmediato en los tests.