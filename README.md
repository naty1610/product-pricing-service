# product-pricing-service

## Descripción

Esta aplicación fue diseñada para buscar la información del precio disponible para cierto producto de acuerdo a su marca. 

## Documentación
[RFC - Servicio Precio](https://docs.google.com/document/d/1A1qR_oI0oDdVYw5G-lcpZb-ywS6Q2A2qa67c_LxFpvU/edit?tab=t.0#heading=h.jyp2yssllsgg)

## Stack Tecnológico
### Lenguaje
- Java 21

### Framework y Librerías
- **Spring Web Flux**: Manejo de peticiones HTTP de manera reactiva
- **Reactor**: Librería de programacón reactiva para manejar flujos de datos asincrónicos

### Base de Datos
- MongoDB
- Docker and Docker compose
- Reactive Mongo Template

### Herramientas
- JUnit 
- Mockito
- TestContainers para MongoDB

## Arquitectura
- **Arquitectura Limpia**: Se aplica arquitectura hexagonal =>
  - **Dominio**: lógica de negocio, modelo `Price`
  - **Infraestructura**: adaptadores para MongoDB
  - **Aplicación**: lógica de aplicación y transformaciones

El uso de `ReactiveMongoTemplate` permite realizar consultas personalizadas con reactividad.

## 🚀 Cómo ejecutar
- **Aplicación local**
```bash
docker-compose up -d
./mvnw spring-boot:run
```

- **Pruebas**
```bash
./mvnw test
```