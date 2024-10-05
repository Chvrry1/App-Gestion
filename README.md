
# App Gestión - Proyecto

Este repositorio contiene las APIs y la aplicación móvil del proyecto "App Gestión". A continuación, se detallan los pasos para instalar las dependencias necesarias, configurar el entorno de desarrollo y gestionar el código usando Git.

## Requisitos

- **XAMPP** para la ejecución de las APIs.
- **Android Studio** (versión Koala o superior) para la aplicación móvil.

## Instalación de XAMPP

1. Descarga e instala **XAMPP** desde su [sitio oficial](https://www.apachefriends.org/index.html).
2. Una vez instalado, dirígete a la carpeta donde se encuentra instalado. Por lo general, se ubica en:
   ```
   C:\xampp\htdocs
   ```
3. Dentro de la carpeta `htdocs`, copia el contenido del repositorio relacionado a las APIs.

## Configuración de las APIs

1. Navega a la carpeta `htdocs` dentro de la instalación de XAMPP:
   ```
   C:\xampp\htdocs
   ```
2. Copia la carpeta `Apis/app_gestion` en la ruta mencionada.
3. Asegúrate de que **XAMPP** esté ejecutando **Apache** y **MySQL** para que las APIs funcionen correctamente.
4. Accede a las APIs a través de la URL correspondiente (por ejemplo: `http://localhost/app_gestion/`).

## Configuración de la Aplicación Móvil

1. Abre **Android Studio** (versión Koala o superior).
2. Clona el repositorio de la aplicación en Android Studio o descárgalo directamente en tu equipo.
3. Ubica la carpeta `AppGestion` dentro del proyecto.
4. Carga la carpeta `AppGestion` en Android Studio.
5. Configura las dependencias y asegúrate de que el proyecto compile sin errores.

## Cómo descargar y gestionar el repositorio

### Clonación del repositorio

Para clonar el repositorio en tu máquina local, ejecuta el siguiente comando en tu terminal:

```bash
git clone https://github.com/Chvrry1/App-Gestion.git
```

### Realizar cambios y hacer commit

1. Realiza los cambios necesarios en los archivos.
2. Una vez que hayas terminado, añade los archivos modificados a Git:

```bash
git add .
```

3. Haz un commit de los cambios:

```bash
git commit -m "Descripción de los cambios"
```

4. Sube los cambios a GitHub:

```bash
git push origin main
```

### Realizar un pull request

Si deseas enviar tus cambios a la rama principal del proyecto, sigue estos pasos:

1. Crea una nueva rama para tus cambios:

```bash
git checkout -b nombre_rama
```

2. Sube la nueva rama:

```bash
git push origin nombre_rama
```

3. Ve a GitHub, abre tu repositorio y selecciona la rama que acabas de crear.
4. Haz clic en **New Pull Request**.
5. Añade una descripción y envía la solicitud de pull request.

### Mantén tu repositorio actualizado

Para sincronizar tu repositorio local con los últimos cambios del repositorio remoto:

```bash
git pull origin main
```

## Contacto

Para preguntas o soporte, por favor contáctame a través de mi correo electrónico: [jhosepcv01@gmail.com](mailto:jhosepcv01@gmail.com).
