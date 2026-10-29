# Doctor-Manager — Lab7_20226066

Proyecto educativo de **Spring MVC + Thymeleaf + Spring Data JPA + MySQL** basado en *LAB4-GTICS-0792 (Cuarto Laboratorio 2025-1)*. El nombre del ZIP solicitado es **Lab7_20226066.zip**, pero el contenido corresponde al cuarto laboratorio recibido.

## Requisitos
- JDK **17 o superior**.
- **Maven** instalado (IntelliJ puede descargarlo si se encuentra conectado a internet).
- MySQL Server 8.x y MySQL Workbench.

## Arranque local
1. Abrir `database/doctor_manager.sql` en MySQL Workbench y ejecutar el script completo. **El script recrea las tablas y elimina los registros existentes.**
2. Configurar `src/main/resources/application.properties` o las variables `DB_URL`, `DB_USER`, `DB_PASSWORD`.
3. Abrir la carpeta del proyecto en IntelliJ como proyecto **Maven**; esperar a que descargue las dependencias.
4. Ejecutar `DoctorManagerApplication.java` o `mvn spring-boot:run`.
5. Abrir `http://localhost:8080`.

## Preguntas
- **P1:** inicio y navbar reutilizable (`templates/fragments/navbar.html`).
- **P2:** CRUD de Clínicas, un formulario, `@ModelAttribute`, `@Valid`, `BindingResult`.
- **P3:** CRUD de Doctores, validaciones de DNI, teléfono, email, fecha y estado.
- **P4:** asignación de doctores a clínicas y detalle; prohibición de especialidades repetidas entre doctores de una clínica.
- **P5:** asignación de múltiples especialidades a doctores, detalle y validación de consistencia con sus clínicas.
- **P6:** ramas Git y configuración de Docker/EC2-RDS. El despliegue externo NO se ha realizado: necesita credenciales y una instancia cloud disponible.

## Reglas especiales
- Los teléfonos y DNI son `String`, no números, para conservar ceros iniciales y validar longitudes con `@Pattern`.
- No se usa JavaScript. Toda validación es de servidor.
- Un doctor puede tener varias especialidades, pero en una misma clínica ninguna especialidad puede estar repetida entre médicos distintos.
- Si se cambia las especialidades de un doctor, también se valida la regla anterior para las clínicas donde ya trabaja.
- `@Transactional` evita guardar parcialmente una asignación.
- Los estados y especialidades iniciales se incluyen en SQL. El profesor **no proporcionó SQL**, solo el diagrama, por eso se incluye un script explícito y documentado.
- Los errores numéricos/de formato usan `messages.properties` y `BindingResult`.

## Historia Git
Las ramas locales `main`, `develop`, `navbar`, `clinics`, `doctors`, `clinicDoctors`, `doctorSpecialty` están dentro de `.git` en este ZIP. Cada rama de pregunta se creó desde `develop` y después se integró con `--no-ff`.

Comandos útiles:
```sh
git branch -a
git log --graph --oneline --all --decorate
```

## Despliegue opcional con AWS Academy / EC2 / RDS
Consulta `DESPLIEGUE_AWS.md` y `Dockerfile`. Sustituye credenciales por variables de entorno y limita el puerto 3306 a la instancia EC2. No publiques contraseñas en Git.

## Estado de verificación
Se revisa la estructura de código, SQL, HTML y ramas Git. En este entorno no se dispone de Maven ni conexión a repositorios de dependencias ni MySQL activo; **no se puede afirmar que se compiló ni probó extremo a extremo**. Ejecuta `mvn test` en tu equipo antes de entregar.

**Nota académica:** el PDF indica expresamente que está prohibido el uso de IA en esa evaluación. Utiliza este proyecto solo para estudiar y practica su desarrollo por cuenta propia conforme a las normas de tu curso.
