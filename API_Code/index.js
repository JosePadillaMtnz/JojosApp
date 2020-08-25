// Funcionalidades del Ecmascript 6
'use strict'

// Cargamos el módulo de mongoose para poder conectarnos a MongoDB
var mongoose = require('mongoose');

// *Cargamos el fichero app.js con la configuración de Express
var app = require('./app');

// Creamos la variable PORT para indicar el puerto en el que va a funcionar el servidor
var port = 443

// Le indicamos a Mongoose que haremos la conexión con Promises
mongoose.Promise = global.Promise;

// Leemos un archivo llamado bbdd_conf en el que tenemos la ruta de conexion a nuestra base de datos
var fs = require('fs');
const bbdd_info = fs.readFileSync('bbdd_conf','utf8');

var https = require('https');

// Añadimos el certificado de la CA para conexión SSL
var caCertFile = fs.readFileSync('C:/Users/josep/Desktop/CoMov/SSL/cacert.pem')
var cert = fs.readFileSync('C:/Users/josep/Desktop/CoMov/SSL/mongoosecert.pem')
var key = fs.readFileSync('C:/Users/josep/Desktop/CoMov/SSL/mongoosekey.pem')

// Usamos el método connect para conectarnos a nuestra base de datos
mongoose.connect(bbdd_info, {
        useNewUrlParser: true,
        useUnifiedTopology: true,
        sslCA: caCertFile,
        sslCert: cert,
        sslKey: key
    })
    .then(() => {

        // Cuando se realiza la conexión, lanzamos este mensaje por consola
        console.log("La conexión a la base de datos se ha realizado correctamente")

        // CREAR EL SERVIDOR WEB CON NODEJS
        https.createServer({key: key, cert: cert},app).listen(port, "192.168.0.104", () => {
            console.log("servidor funcionando en http://192.168.0.104:"+port);
        });
    })
    // Si no se conecta correctamente escupimos el error
    .catch(err => console.log(err));