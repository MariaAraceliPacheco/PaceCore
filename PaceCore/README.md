# PaceCore

## 1. Objetivo Principal
Aplicación web diseñada para **registrar entrenamientos deportivos** de forma manual, **gestionar intervalos** y mostrar **estadísticas y estimaciones de rendimiento**, con funcionalidades de **perfil de usuario** y **red social ligera**.

---

## 2. Descripción Detallada
PaceCore permite:  

- **Registrar entrenamientos manualmente** con fecha, distancia y tiempo total.  
- **Añadir intervalos** a los entrenamientos (tipo de actividad, duración, ritmo).  
- **Visualizar estadísticas** de entrenamientos y estimaciones de tiempos en 5k, 10k, media maratón y maratón.  
- **Ver perfiles de otros usuarios** y sus estadísticas.  
- **Editar y eliminar** entrenamientos propios.  
- **Gestión de usuarios** mediante un panel de administración (solo administradores).  
- **Autenticación segura** con JWT.  
- **Intercambio de datos** mediante API REST en formato JSON.  

---

## 3. Usuarios y Roles

### 3.1 Administrador
**Descripción:** Usuario con **control total** sobre la aplicación y la gestión de usuarios.  
**Acceso:** Panel de administración `/admin` (solo para administradores).  
**Protección:** RoleGuard  

**Tareas principales:**  
- ✅ Crear, editar y eliminar usuarios  
- ✅ Ver todos los perfiles de usuario  
- ✅ Acceder al panel de administración  

### 3.2 Usuario Registrado / Normal
**Descripción:** Usuario que puede **registrar entrenamientos**, **editar y eliminar los suyos**, y ver perfiles y estadísticas de otros usuarios.  
**Acceso:** Panel de usuario `/perfil`, `/entrenos`, `/usuarios`.  
**Protección:** AuthGuard (requiere login)  

**Tareas principales:**  
- ✅ Crear entrenamientos  
- ✅ Editar entrenamientos propios  
- ✅ Eliminar entrenamientos propios  
- ✅ Añadir intervalos a entrenamientos  
- ✅ Ver estadísticas personales (km totales, tiempo total, ritmo medio, intervalos más rápidos)  
- ✅ Ver perfiles y estadísticas de otros usuarios  
- ✅ Acceder a su perfil y actualizar información  

### 3.3 Permisos

| Permiso            | ADMIN | USUARIO NORMAL |
|-------------------|:-----:|:--------------:|
| entrenos:leer      | ✅    | ✅             |
| entrenos:crear     | ✅    | ✅             |
| entrenos:actualizar| ✅    | ✅             |
| entrenos:eliminar  | ✅    | ✅             |
| usuarios:leer      | ✅    | ✅             |
| usuarios:actualizar| ✅    | ✅             |
| usuarios:crear     | ✅    | ❌             |
| usuarios:eliminar  | ✅    | ❌             |

---

## 4. Seguridad

### 4.1 Autenticación
- Login con **email y contraseña**  
- Validación de credenciales en backend  
- ✅ Generación de **JWT** con id, email y rol  
- ✅ Guardado del token en **localStorage**  
- ✅ Envío del token en headers `Authorization`  
- ❌ Error en login: “Usuario o contraseña incorrectos”  

### 4.2 Guards de Seguridad
- **AuthGuard:** Verifica si el usuario está logueado. Protege rutas `/perfil`, `/entrenos`, `/usuarios`.  
- **RoleGuard:** Verifica si el usuario es **administrador** para acceder al panel `/admin`. Bloquea acceso si no es admin.  

---

## 5. Funcionalidades Principales

### 5.1 Autenticación
- ✅ Login / Logout  
- ✅ Registro de usuarios (solo administrador)  
- ✅ JWT tokens  
- ✅ Passwords con **bcrypt**  

### 5.2 Gestión de Usuarios (Administrador)
- ✅ Crear, editar y eliminar usuarios  
- ✅ Ver perfiles de todos los usuarios  

### 5.3 Gestión de Entrenamientos
- ✅ CRUD completo: crear, leer, actualizar, eliminar  
- ✅ Añadir intervalos  
- ✅ Editar y eliminar entrenos propios  

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
- **Framework:** Angular 16+  
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
