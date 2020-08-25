'use strict'

// Cargamos el módulo de express para poder crear rutas
var express = require('express');

// Cargamos el controlador
var UserController = require('../controllers/user');

// Llamamos al router
var api = express.Router();

// Llamamos al middleware para proteger la ruta
var mdl_auth = require('../middlewares/auth');

// Creamos una ruta para los métodos que tenemos en nuestros controladores
// Esta ruta recibe un parametro ID y es necesario que vaya autenticada
api.get('/user/:id', mdl_auth.ensureAuth, UserController.getUser);
api.get('/userbyname/:name', mdl_auth.ensureAuth, UserController.getUserByName);
api.post('/user', UserController.postUser);
api.post('/modifyuser', mdl_auth.ensureAuth, UserController.modifyUser);
// api.put('/user/:id', mdl_auth.ensureAuth, UserController.putUser);
api.delete('/user/:name', mdl_auth.ensureAuth, UserController.deleteUser);

// Exportamos la configuración
module.exports = api;