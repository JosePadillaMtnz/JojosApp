# Fast GUIDE

## Puesta en marcha
- Se requiere NodeJS https://nodejs.org/en/ v12.14.0 LTS
- npm install
    - Esto instalará todas las dependencias incluidas en package.json
- npm install [paquete] --save 
    - instala el paquete y lo añade como dependencia a package.json
- Crear un archivo con nombre bbdd_conf que contenga la url de conexión a nuestra base de datos MongoDB.
    - https://cloud.mongodb.com para crear una base de datos en la nube.
    - Otra opción es instalarla en local.
- node index.js
    - Lanza el servidor y lo deja escuchando en http://localhost:3800/api

- Addon Firefox/Chrome RESTED para poder probar la API fácilmente

## Problemas comunes
### Error de timeout con cloud mongo.
En la interfaz cloud de mongo, debes añadir 0.0.0.0/0 a la lista de WhiteList para poder conectar o, en su defecto, la IP desde la que desees conectar a la bbdd.

### Usuario no encontrado al hacer login
1. Asegúrate que en el archivo bbdd_conf tienes correctamente especificada tu base de datos.
2. Las colecciones base de la API deben estar definidos en mongocloud (creados) exactamente con los siguientes nombres:
    - messages
    - users
En plural y en minúscula.
3. Debes tener un usuario registrado en tu base de datos (de forma manual).
4. Los usuarios siguen el esquema que puedes encontrar en models/user.js
    {"name":"Usuario","email":"usuario@test.com","pHash":"12345","image":""}

### No sé cómo hacer login.
1. Descarga una herramienta como RESTED o POSTMAN que te permita hacer peticiones HTTP.
2. POST http://localhost:3800/api/login
3. Headers tienes que añadir: Content-type: application/json
4. En el body de la petición: {"name":"Usuario","pHash":"12345"}

## Añadir nuevos modelos

Para añadir modelos hay que realizar el siguiente proceso:
1. Añadir un nuevo archivo con el nombre del modelo en models/. Este archivo contiene la definición del modelo, tipos de datos etc.
2. Añadir un nuevo archivo con el mismo nombre del modelo en controllers/. Este archivo contiene los métodos o comportamiento esperados.
3. Añadir un nuevo archivo con el mismo nombre del modelo en routes/. Aquí se definen las rutas expuestas en el modelo.
    a. Aquí se indica si deseamos que el acceso al método sea autenticado o no mediante el uso del middleware Auth.js.
4. Finalmente en app.js, se añade el modelo nuevo y se indica su uso mediante app.use().


## Realizar pruebas

Actualmente existen 3 posibles rutas:
- /api/login
    - Permite iniciar sesión dado un nombre de usuario y una contraseña.
    - Devuelve un token JWT que hay que incluir en las sucesivas peticiones como cabecera "Authorization".
- /api/user
    - Permite recuperar un usuario dado un ID.
    - Permite añadir nuevos usuarios.
- /api/message
    - Permite recuperar todos los mensajes existentes ordenados por fecha.
    - Permite añadir un nuevo mensaje.

### Login
- POST http://localhost:3800/api/login
- Headers:
    - Content-Type: application/json
- Request body:
    - {"name": "STRING", "pHash": "STRING"}
- Response:
    - {"token": "STRING"}

### Get User/:id
- GET http://localhost:3800/api/user/[ID]
- Headers:
    - Authorization: [Token]
- Response:
    - { "userObj": {"_id": "STRING", "name": "STRING", "email": "STRING", "image": "STRING" }}

### Post User
- POST http://localhost:3800/api/user/
- Headers:
    - Authorization: [Token]
    - Content-Type: application/json
- Request body:
    - {"name": "STRING", "email": "STRING, "pHash": "STRING"}
    - image es opcional.

### Get Message/
- GET http://localhost:3800/api/message/
- Headers:
    - Authorization: [Token]
- Response:
    - "MessageObj" : [array(message)]

### Post Message
- POST http://localhost:3800/api/message/
- Headers:
    - Authorization: [Token]
    - Content-Type: application/json
- Request body:
    - {"title": "STRING","body": STRING", "uid": "STRING"}
    - Aquí no se está controlando si vienen todos los datos.
