# TFG

## Requisitos

Asegúrate de tener instalados los siguientes software:

- [Java 19](https://www.oracle.com/java/technologies/javase/jdk19-archive-downloads.html)
- [Maven](https://maven.apache.org/install.html)
- [Node.js y npm](https://nodejs.org/)
- [Angular 17](https://angular.dev)
- [Angular CLI](https://angular.io/cli)
- [Postgres y PostGIS](https://www.postgresql.org/download/)

## Configuración general

1. Clona el repositorio del proyecto:
   ```bash
   git clone https://gitlab.lbd.org.es/tfgs/miguel.rodriguez6.tfg/tfg.git

## Configuración de la Base de Datos

1. Crea una base de datos con el nombre tfg_test.

   ```bash
   CREATE DATABASE tfg_test;

2. Ejecuta el script ConfigureDB.sql en postgres.

   ```bash
   \i backend/src/sql/ConfigureDB.sql

## Ejecución del Backend (Spring Boot)

1. Una vez clonado el repositorio, cambia de directorio actual a .../backend:

   ```bash
   cd tfg/backend
   
2. Una vez completados los anteriores pasos, inicia el backend de la aplicación:

   ```bash
   mvn spring-boot:run

## Ejecución del Frontend (Angular)

1. Una vez clonado el repositorio, cambia de directorio actual a .../frontend:

   ```bash
   cd tfg/frontend

2. Una vez completados los anteriores pasos, inicia el frontend de la aplicación:

   ```bash
   ng serve