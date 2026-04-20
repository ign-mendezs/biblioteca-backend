# Biblioteca MVP - Arquitectura Hexagonal

Este proyecto es un Producto Mínimo Viable (MVP) para la gestión de una biblioteca, desarrollado como prueba técnica. Implementa un sistema de reservas y préstamos de libros con validaciones de tiempo y reglas de estado estrictas.

## Arquitectura y Decisiones de Diseño

Para este servicio se ha implementado una **Arquitectura Hexagonal (Ports & Adapters)**. Como ingeniero backend, la prioridad en este diseño ha sido garantizar que la lógica de negocio (el core) esté completamente aislada de los detalles de infraestructura (Frameworks, Bases de Datos, Interfaces Web).



### ¿Por qué Arquitectura Hexagonal?

1. **Agnosticismo Tecnológico:** El dominio de la aplicación (`domain`) está escrito en Java puro. No tiene dependencias de Spring Boot, JPA o validadores web. Si en el futuro se decide migrar de Spring Boot a Quarkus, o de JPA a JDBC, la lógica de negocio permanece intacta.
2. **Inversión de Dependencias (SOLID - DIP):** La capa de aplicación no depende del repositorio de base de datos directamente, sino de un "Puerto" (Interfaz). La capa de infraestructura "conecta" el adaptador (JPA) a ese puerto.
3. **Testabilidad:** Al tener las reglas de negocio encapsuladas en entidades puras, se pueden escribir pruebas unitarias exhaustivas y ultrarrápidas sin necesidad de levantar el contexto de Spring.

### Estructura del Microservicio

El código fuente está dividido en tres capas principales que fluyen de adentro hacia afuera:

```text
com.empresa.biblioteca_mvp
├── domain                # [CORE] Lógica de negocio y reglas puras.
│   ├── model               # Entidades de Dominio Rico (ej. Book con métodos reserve(), borrow()).
│   └── port                # Interfaces que definen los contratos de salida (ej. BookRepositoryPort).
│
├── application             # [ORQUESTACIÓN] Casos de Uso.
│   └── usecase             # Coordinan el flujo entre los puertos y las entidades del dominio.
│
└── infrastructure          # [ADAPTADORES] Tecnologías concretas (Spring, H2, REST).
    ├── adapter
    │   ├── in.web          # Controladores REST, DTOs y Manejo Global de Excepciones.
    │   └── out.db          # Entidades JPA (@Entity), Repositorios Spring Data y Mappers.
    ├── config              # Configuración de Beans (Inyección de Casos de Uso) y Seeders.
    └── security            # (Próximamente) Implementación de JWT.



```

### Decisiones de Diseño y Patrones (ADRs)

1. **Rich Domain Model vs Anemic Domain Model:**
   En lugar de tener servicios inflados con múltiples `if/else`, la lógica reside en la entidad `Book`. Las validaciones de tiempo (reservas de 1 hora, préstamos de 2 días) y las transiciones de estado (`AVAILABLE`, `RESERVED`, `BORROWED`) se gestionan internamente en la entidad.

2. **Manejo de Tiempos y Concurrencia:**
    * **Lazy Expiration:** Para evitar la complejidad de *Cron Jobs* o tareas en segundo plano para limpiar reservas expiradas, se implementó evaluación perezosa. Al consultar o intentar operar un libro, el sistema verifica si la reserva anterior expiró comparando el `endTime` con el reloj actual, liberándolo dinámicamente si es necesario.
    * **Optimistic Locking:** Implementado a nivel de base de datos (con la anotación `@Version` de JPA) para prevenir *Race Conditions* en caso de que dos usuarios intenten reservar el mismo libro en el mismo milisegundo.

3. **Inversión de Control Pura:**
   Los Casos de Uso (`BookManagementUseCase`, `BookOperationsUseCase`) no utilizan anotaciones de Spring (como `@Service`). Son clases Java puras. Se registran en el contenedor de IoC a través de una clase de configuración explícita (`UseCaseConfig`), manteniendo el núcleo agnóstico del framework.

## 🚀 Tecnologías

* **Lenguaje:** Java 17
* **Framework:** Spring Boot 3
* **Base de Datos:** H2 Database (En memoria) + Spring Data JPA
* **Librerías:** Lombok (reducción de boilerplate), Validation (restricciones de entrada).

## ⚙️ Cómo ejecutar en local

Para facilitar la revisión técnica, el proyecto utiliza una base de datos en memoria (H2) y cuenta con un **Database Seeder** que inyecta datos de prueba automáticamente al iniciar la aplicación.

1. Clona el repositorio.
2. Ejecuta la aplicación usando tu IDE o Maven:
   ```bash
   ./mvnw spring-boot:run
   ```
3. La API estará disponible en `http://localhost:8080`.

### Ejemplos de uso (Endpoints actuales)
*Nota: La seguridad JWT se implementará en la siguiente iteración. Actualmente los endpoints de usuario reciben el ID del usuario mediante el header `X-User-Id`.*

* **Obtener catálogo completo:** `GET /api/v1/management/books`
* **Reservar un libro:** `POST /api/v1/operations/books/{bookId}/reserve` (Requiere Header: `X-User-Id: <id-del-usuario>`)
* **Consola de Base de Datos H2:** `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:bibliotecadb`, User: `sa`, sin contraseña).
