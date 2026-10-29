# Guía para desplegar en AWS Academy (pendiente de ejecutar)

Basado en las clases GTICS: creación de cuenta AWS Academy, EC2, RDS, despliegue de Spring Boot en VM, fundamentos y despliegue con Docker.

1. Entrar a **AWS Academy Learner Lab** e iniciar la consola de AWS.
2. Crear una instancia **EC2 Amazon Linux 2023**, asignar un par de claves y abrir SSH (22) solo desde tu IP; abrir 8080 para las pruebas, preferiblemente solo desde tu IP.
3. Crear una BD **Amazon RDS MySQL** en la **misma VPC** que EC2, sin acceso público, y configurar el grupo de seguridad de RDS para aceptar **3306 solo desde el security group de EC2** (nunca 0.0.0.0/0 para una BD privada).
4. Mediante MySQL Workbench `Standard TCP/IP over SSH` conectarse a RDS y ejecutar `database/doctor_manager.sql`.
5. En tu PC compilar: `mvn clean package -DskipTests`; subir el `.jar` y el `Dockerfile` a la VM, o configurar Docker remoto por SSH en IntelliJ como en clase.
6. Instalar Docker en Amazon Linux 2023:
   ```sh
   sudo dnf install -y docker
   sudo systemctl enable --now docker
   sudo usermod -aG docker ec2-user
   ```
   Volver a entrar en SSH para aplicar el grupo `docker`.
7. Generar la imagen dentro de la carpeta que contiene el `.jar` (usando el Dockerfile del proyecto):
   ```sh
   docker build -t doctormanager:v1 .
   ```
8. Ejecutarla indicando variables sin guardar contraseñas en el repositorio:
   ```sh
   docker run -d --name doctormanager -p 8080:8080 \
     -e DB_URL='jdbc:mysql://ENDPOINT-RDS:3306/doctor_manager?serverTimezone=America/Lima' \
     -e DB_USER='TU_USUARIO' -e DB_PASSWORD='TU_PASSWORD' doctormanager:v1
   ```
9. Revisar `docker logs doctormanager`; abrir `http://IP_PUBLICA_EC2:8080`.
10. Presentar una evidencia real de pantalla al profesor o JP. **Este ZIP NO prueba ningún despliegue real.**

**Nota:** AWS Academy puede tener restricciones de presupuesto, duración y servicios. Detener y borrar los recursos cuando termines para evitar consumo de créditos.
