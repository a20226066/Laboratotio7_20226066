# Guía para entender el código de cada pregunta

**Este proyecto sirve para practicar la lógica de programación del curso GTICS.** Lee primero el PDF del laboratorio y reproduce el programa paso a paso.

## Pregunta 1: navbar y página de inicio
- `HomeController.java`: `@Controller` recibe la URL `/`; `return "index"` busca `templates/index.html`.
- `fragments/navbar.html`: `th:fragment="navbar(active)"` permite crear un navbar reutilizable.
- `th:replace` coloca el mismo navbar en todas las vistas sin copiar el código.
- Rama: `navbar` creada desde `develop`, luego merge de vuelta.

## Pregunta 2: CRUD de clínicas
- `entity/Clinic.java`: `@Entity`, `@Table`, `@Id`, `@Column` representan la tabla `clinic`.
- `repository/ClinicRepository.java`: extiende `JpaRepository`; hereda `findById`, `findAll`, `save`, `delete`.
- `controller/ClinicController.java`: rutas GET para listar/crear/editar y POST para guardar/eliminar.
- `form/ClinicForm.java`: `@NotBlank`, `@Size`, `@Pattern` verifican que el nombre tenga 5-100 caracteres, la dirección al menos 10 y el teléfono 9 dígitos.
- `templates/clinics/form.html`: usa `th:object`, `th:field` y `th:errors`. **Un único archivo sirve para crear y editar** gracias al ID oculto.
- Rama: `clinics`.

## Pregunta 3: CRUD de doctores
- `entity/Doctor.java` tiene una relación `@ManyToOne` con `DoctorState` porque muchos doctores pueden compartir estado.
- `form/DoctorForm.java` utiliza `@Email`, `@Pattern`, `@PastOrPresent`, `@NotNull` y `@NotBlank`.
- Los campos DNI y teléfono se declaran `String` porque se validan como *dígitos* y no se calculan aritméticamente.
- `messages.properties` personaliza errores al convertir parámetros del formulario a `Integer` o `LocalDate`.
- Rama: `doctors`.

## Pregunta 4: doctores asignados a una clínica
- La relación entre Clínica y Doctor es muchos-a-muchos (`@ManyToMany`), usando `clinic_doctor`.
- `ClinicForm.doctorIds` recibe los ID de los checkbox marcados.
- `ClinicService.validarDoctores` recorre las especialidades de los médicos seleccionados. Si dos comparten alguna, llama `BindingResult.reject(...)` y el formulario muestra el error global.
- `ClinicService.guardar` usa `@Transactional` para guardar la clínica y las filas de la relación como una operación.
- `clinics/detail.html` muestra los doctores de cada clínica.
- Rama: `clinicDoctors`.

## Pregunta 5: especialidades del doctor
- `Doctor.specialties` gestiona la tabla `doctor_specialty` como relación muchos-a-muchos.
- `DoctorForm.specialtyIds` recibe los checkbox de especialidades; `@NotEmpty` exige al menos una.
- `DoctorService` valida y guarda la asignación con `@Transactional`.
- Al cambiar las especialidades de un doctor ya asignado, también se revisan las clínicas donde trabaja para no crear conflictos de la P4.
- `doctors/detail.html` presenta las especialidades.
- Rama: `doctorSpecialty`.

## Pregunta 6: Git y nube
- Todas las ramas anteriores fueron creadas desde `develop` e integradas de regreso; el último merge de `develop` fue realizado hacia `main`.
- `Dockerfile` y `DESPLIEGUE_AWS.md` dejan preparado el proyecto para EC2 + RDS; el despliegue efectivo requiere credenciales, servicios AWS y la demostración ante el JP.

## Ejercicios de comprobación
1. Crea una clínica llamada `ABC` y explica por qué debe rechazarse.
2. Registra un doctor con DNI `1234ABC8` y muestra qué anotación lo bloquea.
3. Trata de registrar un doctor con fecha de mañana y señala la anotación aplicable.
4. Asigna a dos doctores la especialidad Cardiología; trata de añadirlos a la misma clínica. Debe mostrarse error.
5. Asigna doctores con especialidades distintas a una clínica: debe guardarse y verse en detalle.
6. Edita un doctor y asígnale una especialidad que ya tiene otro doctor de su clínica: debe rechazarse.
7. Ejecuta `mvn test` y revisa las pruebas de validación.
