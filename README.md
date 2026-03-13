# PaceCore

## 1. Objetivo Principal
Aplicación web diseñada para **registrar entrenamientos deportivos** de forma manual, **gestionar intervalos** y mostrar **estadísticas y estimaciones de rendimiento**, con funcionalidades de **perfil de usuario**.

---

## 2. Descripción Detallada
PaceCore permite:  

- **Registrar entrenamientos manualmente** con fecha, distancia y tiempo total.  
- **Añadir intervalos** a los entrenamientos (tipo de actividad, duración, ritmo).  
- **Visualizar estadísticas** de entrenamientos y estimaciones de tiempos en 5k, 10k, media maratón y maratón.  
- **Ver perfiles de otros usuarios** y sus estadísticas.  
- **Editar y eliminar** entrenamientos propios.  
- **Gestión de usuarios** vía API (CRUD + estadísticas).  
- **Autenticación** con JWT.  
- **Intercambio de datos** mediante API REST en formato JSON.  

---

## 3. Usuarios y Roles

Actualmente **no hay distinción real de permisos por rol** entre “usuario normal” y “admin”.

- En backend existe el campo `rol` en `Usuario`.
- En el registro (`/auth/register`) el rol se asigna por defecto a **`USUARIO`**.
- No hay comprobaciones tipo `@PreAuthorize/hasRole(...)` ni un **RoleGuard** en el frontend.
- Existe algún endpoint con ruta “admin” (por ejemplo `/entrenos/admin/recalcularZonas`), pero **no está protegido por rol** a nivel de aplicación.

---

## 4. Seguridad

### 4.1 Autenticación
- Login con **email y contraseña** (backend valida credenciales).  
- Generación de **JWT** en backend.  
- El frontend guarda el token en **`localStorage`** y lo usa para recuperar el perfil con `/auth/me?token=...`.  
- Las contraseñas se almacenan hasheadas con **BCrypt**.

### 4.2 Protección de rutas (estado actual)
- **Frontend**: hay `AuthGuard` para bloquear `/dashboard` si no hay sesión cargada.  
- **Backend**: existe un filtro JWT (`FiltroWeb`) que puede devolver **401** si la request no incluye un token válido (según la whitelist del filtro).  
- **Roles**: no hay autorización por rol implementada (RBAC) más allá del campo `rol` en la entidad.

---

## 5. Funcionalidades Principales

### 5.1 Autenticación
- ✅ Login / Logout  
- ✅ Registro de usuarios  
- ✅ JWT tokens  
- ✅ Passwords con **bcrypt**  

### 5.2 Gestión de Usuarios
- ✅ CRUD de usuarios vía API (`/usuarios`)  
- ✅ Estadísticas de usuario (`/usuarios/estadisticas/...`)  

### 5.3 Gestión de Entrenamientos
- ✅ CRUD completo: crear, leer, actualizar, eliminar  
- ✅ Añadir intervalos  

### 5.4 Estadísticas y Estimaciones
- ✅ Ritmo medio por entreno  
- ✅ Ritmo medio global  
- ✅ Km totales y tiempo acumulado  
- ✅ Intervalo más rápido por entreno  
- ✅ Estimaciones: 5k, 10k, media maratón, maratón  

### 5.5 Perfiles de Usuario
- ✅ Ver y actualizar perfil propio  
- ✅ Ver perfiles y estadísticas de otros usuarios  

---

## 6. Tecnología Empleada

**Frontend**  
- **Framework:** Angular 18  
- **Lenguaje:** TypeScript  
- **Componentes:** Standalone  
- **Estilos:** CSS + Tailwind (opcional)  
- **Enrutamiento:** Router con lazy loading  
- **HTTP:** HttpClient + Interceptors  
- **Almacenamiento:** localStorage (JWT)  

**Backend**  
- **Framework:** Spring Boot  
- **Lenguaje:** Java  
- **Persistencia:** JPA / Hibernate  
- **Base de datos:** MySQL  
- **Autenticación:** JWT  
- **Password:** bcrypt  
- **CORS:** habilitado para desarrollo  

---

## 7. Base de datos con Docker (recomendado)

El backend está preparado para MySQL. Un `docker-compose.yml` mínimo podría ser:

```yaml
services:
  mysql:
    image: mysql:8.4
    container_name: pacecore-mysql
    environment:
      MYSQL_DATABASE: pacecore
      MYSQL_ROOT_PASSWORD: admin
    ports:
      - "3306:3306"
    volumes:
      - pacecore_mysql_data:/var/lib/mysql

volumes:
  pacecore_mysql_data:
```

Y la URL típica en `application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/pacecore?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=admin
```