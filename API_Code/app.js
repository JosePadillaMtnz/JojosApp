// Utilizar funcionalidades del Ecmascript 6
'use strict'

// Cargamos los módulos de express y body-parser
var express = require('express');
var bodyParser = require('body-parser');
var methodOverride = require("method-override");
var helmet = require('helmet');

// Llamamos a express para poder crear el servidor
var app = express();

// Importamos las rutas
var user_routes = require('./routes/user');
var login_routes = require('./routes/login');
var message_routes = require('./routes/message');

//cargar middlewares
//un metodo que se ejecuta antes que llegue a un controlador
//Configuramos bodyParser para que convierta el body de nuestras peticiones a JSON
app.use(bodyParser.urlencoded({
    extended: false
}));
app.use(bodyParser.json());
app.use(methodOverride());
app.use(helmet());

// Cargamos las rutas
app.use('/api', user_routes);
app.use('/api', login_routes);
app.use('/api', message_routes);


// exportamos este módulo para poder usar la variable app fuera de este archivo
module.exports = app;