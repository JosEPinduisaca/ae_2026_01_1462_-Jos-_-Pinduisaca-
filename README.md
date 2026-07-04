# exam2 — Microservicio de Estacionamiento (parking)

Microservicio en capas (Kotlin + Spring Boot 4) que controla espacios de
estacionamiento y tickets de entrada/salida, protegido con AWS Cognito.

## Cómo usar este código

1. Crea un proyecto en https://start.spring.io con:
   - Gradle - Kotlin, Java 21, Spring Boot 4.x
   - Group: com.pucetec, Artifact: exam2
   - Dependencias: Spring Web, Spring Data JPA, PostgreSQL Driver, OAuth2 Resource Server
2. Reemplaza su `src/` y `build.gradle.kts` por los de este paquete.
3. Levanta la base con `docker compose up -d` (usa el puerto 5433 en el host
   para no chocar con otros proyectos que ya usen 5432).
4. Corre la app y prueba con la colección de Postman en `postman/`.
5. Corre los tests de `services` con **Run with Coverage**.
6. Llena `evidence/` con las capturas pedidas por el examen.
