'use strict'

// Cargamos el módulo de express para poder crear rutas
var express = require('express');

// Cargamos el controlador
var LoginController = require('../controllers/login');

// Llamamos al router
var api = express.Router();

// Creamos una ruta para los métodos que tenemos en nuestros controladores
api.post('/login', LoginController.postLogin);

// Exportamos la configuración
module.exports = api;