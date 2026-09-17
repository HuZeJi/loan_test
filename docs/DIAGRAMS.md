# Diagrama de arquitectura
```mermaid
architecture-beta
    group api(cloud)[API]
    group webapp(cloud)[Web app]

    service web(server)[Frontend] in webapp

    service db(database)[Database] in api
    service server(server)[Backend] in api

    db:L -- R:server
    web:R -- L:server
```
# Diagrama ER

```mermaid
erDiagram

    CLIENT ||--o{ LOAN: "applies for"
    LOAN_TERM ||--|{ LOAN : contains
    LOAN ||--o{ LOAN_PAYMENT: contains

    USER ||--o{ LOAN : "resolves"
    USER ||--o{ LOAN_PAYMENT : "registers"

    USER {
        string id PK
        string username 
        string password_hash
        string role
        string email 
        boolean is_active
    }

    CLIENT {
        string id PK
        string name
        string last_name
        date birthday
        string address
        string email
        string phone_number
    }

    LOAN {
        string id PK
        float amount
        float pending_amount
        string application_status
        string payment_status
        string resolution_notes
        date resolution_date
        %%  FK
        string client_id FK
        string loan_term_id FK
        string user_id FK
    }

    LOAN_TERM {
        string id PK
        string description
        int days
    }

    LOAN_PAYMENT {
        string id PK
        date payment_date
        string payment_method
        float amount
        %%  FK
        string loan_id FK
        string user_id FK
    }



```
---
# Diagrama de secuencia
## Gestion de clientes
### Agregar clientes

```mermaid
sequenceDiagram
    actor us as Usuario
    participant fe as Web App
    participant be as Backend
    participant db as Base de datos

    us->>fe: Agregar cliente
    fe->>be: POST /client <br>{...datos de cliente}<br> Authorization: Bearer ...
    alt  token invalido
        be-->>fe: 401 Acceso no autorizado
        fe--xus: Modal: El usuario no tiene acceso a los datos
    end
    be->>db: INSERT INTO client (...)<br>VALUES (...)
    alt Cliente ya existe
        db-->>be: Duplicate entry
        be-->>fe: 409 Ya existe el cliente
        fe--xus: Modal: El usuario ya existe
    else existe algun error durante la insercion
        be-->>fe: 503 Servicio no disponible
        fe--xus: Modal: El servicio se encuentra inactivo<br>trate nuevamente mas tarde
    end
    be-->>fe: 201 Cliente creado exitosamente
    fe--xus: Modal: Cliente creado exitosamente
```
### Listar clientes

```mermaid
sequenceDiagram
    actor us as Usuario
    participant fe as Web App
    participant be as Backend
    participant db as Base de datos

    us->>fe: Listar clientes
    fe->>be: GET /client <br> Authorization: Bearer ...
    alt  token invalido
        be-->>fe: 401 Acceso no autorizado
        fe--xus: Modal: El usuario no tiene acceso a los datos
    end

    be->>db: SELECT ... FROM CLIENT (With pagination)
    alt existe algun error durante la carga de datos
        be-->>fe: 503 Servicio no disponible
        fe--xus: Modal: El servicio se encuentra inactivo<br>trate nuevamente mas tarde
    end
    db-->>be: Datos de cliente
    be-->>fe: 200 { ...datos de cliente }
    fe--xus: Modal: Datos de cliente
```
### Editar clientes
```mermaid
sequenceDiagram
    actor us as Usuario
    participant fe as Web App
    participant be as Backend
    participant db as Base de datos

    us->>fe: Actualizar cliente
    fe->>be: PUT /client/{client_id} <br>{...datos de cliente}<br> Authorization: Bearer ...
    alt  token invalido
        be-->>fe: 401 Acceso no autorizado
        fe--xus: Modal: El usuario no tiene acceso a los datos
    end
    be->>db: UPDATE client SET ... <br> WHERE ...
    alt Cliente no existe
        db-->>be: No record found
        be-->>fe: 404 Cliente no encontrado
        fe--xus: Modal: Cliente no encontrado
    else existe algun error durante la actualizacion
        be-->>fe: 503 Servicio no disponible
        fe--xus: Modal: El servicio se encuentra inactivo<br>trate nuevamente mas tarde
    end
    be-->>fe: 201 Cliente actualizado exitosamente
    fe--xus: Modal: Cliente actualizado exitosamente
```
### Eliminar clientes
```mermaid
sequenceDiagram
    actor us as Usuario
    participant fe as Web App
    participant be as Backend
    participant db as Base de datos

    us->>fe: Eliminar cliente
    fe->>be: DELETE /client/{client_id} <br> Authorization: Bearer ...
    alt  token invalido
        be-->>fe: 401 Acceso no autorizado
        fe--xus: Modal: El usuario no tiene acceso a los datos
    end
    be->>db: UPDATE client SET status=innactive <br> WHERE ...
    alt existe algun error durante la actualizacion
        be-->>fe: 503 Servicio no disponible
        fe--xus: Modal: El servicio se encuentra inactivo<br>trate nuevamente mas tarde
    end
    be->>db: UPDATE loan SET status=innactive <br> WHERE ...
    be-->>fe: 201 Cliente eliminado exitosamente
    fe--xus: Modal: Cliente actualizado exitosamente
```

## Solicitud de prestamos
### Solicitar prestamo
```mermaid
sequenceDiagram
    actor us as Usuario
    participant fe as Web App
    participant be as Backend
    participant db as Base de datos

    us->>fe:Solicitar prestamo
    fe->>be:POST /loan <br>{ ...datos del prestamo}<br> Authorization: Bearer ...
    alt  token invalido
        be-->>fe: 401 Acceso no autorizado
        fe--xus: Modal: El usuario no tiene acceso a los datos
    else monto y plazo no definidos
        be-->>fe: 400 Datos de prestamos [monto|plazo] <br>no definidos
        fe--xus: Modal: Los datos [monto|plazo]<br> son obligatorios
    end
    be->>db: INSERT INTO loan (...)<br>VALUES (...)
    alt existe algun error durante la actualizacion
        be-->>fe: 503 Servicio no disponible
        fe--xus: Modal: El servicio se encuentra inactivo<br>trate nuevamente mas tarde
    end
    be-->>fe: 200 Prestamo creado exitosamente
    fe--xus: Modal: Prestamo creado exitosamente
```
### Listar prestamos por cliente
```mermaid
sequenceDiagram
    actor us as Usuario
    participant fe as Web App
    participant be as Backend
    participant db as Base de datos

    us->>fe:Listar prestamos
    fe->>be:GET /client/{client_id}/loan<br> Authorization: Bearer ...
    alt  token invalido
        be-->>fe: 401 Acceso no autorizado
        fe--xus: Modal: El usuario no tiene acceso a los datos
    end
    be->>db: SELECT ... FROM loan WHERE ... (With pagination)
    alt existe algun error durante la carga de datos
        be-->>fe: 503 Servicio no disponible
        fe--xus: Modal: El servicio se encuentra inactivo<br>trate nuevamente mas tarde
    end
    db-->>be: Lisatdo de prestamos
    be-->>fe: 200 { ... }
    fe--xus: Listado de prestamos
```
### Aprobar/rechazar prestamo
```mermaid
sequenceDiagram
    actor us as Usuario
    participant fe as Web App
    participant be as Backend
    participant db as Base de datos

    us->>fe:Aprobar/rechazar prestamo
    fe->>be:PATCH /loan/{loan_id}/{approve|reject}<br> Authorization: Bearer ...
    alt  token invalido
        be-->>fe: 401 Acceso no autorizado
        fe--xus: Modal: El usuario no tiene acceso a los datos
    end
    be->>db: UPDATE loan SET status=[approved|rejected] <br> WHERE ...
    alt existe algun error durante la actualizacion
        be-->>fe: 503 Servicio no disponible
        fe--xus: Modal: El servicio se encuentra inactivo<br>trate nuevamente mas tarde
    end
    be-->>fe: 201 Prestamo [aprobado|rechazado] exitosamente
    fe--xus: Modal: Prestamo [aprobado|rechazado] exitosamente
```

## Gestion de prestamos aprobados y pagos
### Ver prestamos aprobados
```mermaid
sequenceDiagram
    actor us as Usuario
    participant fe as Web App
    participant be as Backend
    participant db as Base de datos

    us->>fe:Listar prestamos aprobados
    fe->>be:GET /client/{client_id}/loan/approbed<br> Authorization: Bearer ...
    alt  token invalido
        be-->>fe: 401 Acceso no autorizado
        fe--xus: Modal: El usuario no tiene acceso a los datos
    end
    be->>db: SELECT ... <br>FROM loan<br> WHERE ... AND status = "APPROVED"<br> (With pagination)
    alt existe algun error durante la carga de datos
        be-->>fe: 503 Servicio no disponible
        fe--xus: Modal: El servicio se encuentra inactivo<br>trate nuevamente mas tarde
    end
    db-->>be: Lisatdo de prestamos
    be-->>fe: 200 { ... }
    fe--xus: Listado de prestamos
```
### Registrar pagos realizados
```mermaid
sequenceDiagram
    actor us as Usuario
    participant fe as Web App
    participant be as Backend
    participant db as Base de datos

    us->>fe:Registrar pago realizado
    fe->>be:POST /loan/{loan_id}/loan-payment <br>{ ...datos del pago prestamo}<br> Authorization: Bearer ...
    alt  token invalido
        be-->>fe: 401 Acceso no autorizado
        fe--xus: Modal: El usuario no tiene acceso a los datos
    end
    be->>db: SELECT ... FROM loan WHERE ... (Limit 1)
    alt  el prestamo no existe
        be-->>fe: 404 Datos no encontrados
        fe--xus: Modal: El prestamo asociado no existe
    else el prestamo ya esta solventado
        be-->>fe: 200 El prestamo ya esta solventado
        fe--xus: Modal: El prestamo ya esta solventado
    else el pago es mayor al monto pendiente del prestamo
        be-->>fe: 200 el pago es mayor al monto pendiente del prestamo
        fe--xus: Modal: el pago es mayor al monto pendiente del prestamo
    end
    be->>be: Calcular nuevo saldo pendiente
    be->>db: INSERT INTO loan_payment (...)<br>VALUES (...)
    be->>db: INSERT INTO loan (...)<br>VALUES (...)
    alt existe algun error durante la actualizacion
        be-->>fe: 503 Servicio no disponible
        fe--xus: Modal: El servicio se encuentra inactivo<br>trate nuevamente mas tarde
    end
    be-->>fe: 200 Pago de prestamo registrado exitosamente
    fe--xus: Modal: Pago de prestamo registrado exitosamente
```
### Calcular saldos pendientes
```
    Este calculo ya se encuentre en los flujos
```



