# Instrucciones: 
## Instrucciones: 
Realizar el siguiente examen práctico utilizando como lenguaje de programación **Java** como lenguaje de aplicación, utilizar *Transact-SQL** como lenguaje de Base de datos. Al finalizar la práctica (Aplicación), **Script de Base de Datos** y **Diagrama Entidad/Relación**, subir el código y documentación en Github o Gitlab y enviar el repositorio por correo. Recuerda documentar adecuadamente tu código y proporcionar las instrucciones para ejecutar la aplicación localmente.

## Entrega:
-  Al finalizar la práctica, sube el código y la documentación a un repositorio en Github o Gitlab.
- Incluye el script de la base de datos y el diagrama entidad-relación en la documentación.

## Tecnologías Sugeridas:
- VueJS, Angular, React para frontend.
- Spring Boot para el desarrollo de la aplicación.
- JPA (Java Persistence API) para la capa de persistencia.
- Docker y Docker Compose para la gestión de contenedores.

## Valoración:
Se valorará positivamente la facilidad de despliegue de la aplicación. Se espera que la aplicación sea fácil de desplegar utilizando Docker Compose, lo que garantizará un proceso de despliegue rápido y eficiente en cualquier entorno. Además, se apreciará la inclusión de instrucciones claras para ejecutar la aplicación localmente.

### Examen: 
Elaborar un Sistema Web amigable para gestionar préstamos bancarios.
### Gestión de Clientes:
- Agregar un nuevo cliente: Nombre, Apellido, Número de identificación, Fecha de nacimiento, Dirección, Correo electrónico, Teléfono.
- Ver la lista de todos los clientes registrados, incluyendo la información de contacto.
- Editar la información de un cliente existente.
- Eliminar un cliente del sistema y todas sus solicitudes de préstamos asociadas.
### Solicitud de Préstamos:
- Permitir a un cliente solicitar un préstamo bancario. Debe incluir el monto solicitado, el plazo deseado y otros detalles relevantes.
- Ver la lista de solicitudes de préstamos pendientes para cada cliente y su estado actual (aprobado, rechazado, en proceso).
- Aprobar o rechazar una solicitud de préstamo y registrar los detalles de la aprobación/rechazo.

### Gestión de Préstamos Aprobados y Pagos:
- Ver la lista de préstamos aprobados para cada cliente, incluyendo los detalles del préstamo y el estado de pago.
- Registrar los pagos en efectivo realizados por los clientes para los préstamos aprobados. 
- Calcular y mostrar el saldo pendiente para cada préstamo aprobado
