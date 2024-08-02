## Configuración del entorno

Para configurar las variables de entorno necesarias:

1. Copia el archivo `.env.example` y renómbralo a `.env`
2. Edita `.env` y añade tus valores reales para cada variable
3. No compartas tu archivo `.env`, está en .gitignore por razones de seguridad

Levantar aplicacion en docker
1. Compilación: .\mvnw.cmd clean package -DskipTests
2. Validar imagen docker creada: docker images
3. Levantar docker desde Dockerfile: docker run -p 8080:8080 afa6fd21178c