'use strict';

var jwt = require('jsonwebtoken')
var User = require('../models/user');
const path = require("path");

exports.postLogin = function (req, res) {
    var username = req.body.name
    var passwordHash = req.body.pHash
    if (username != undefined && passwordHash != undefined) {
        User.find({
            name: username,
            pHash: passwordHash
        }).exec(function (err, userObj) {
            // Si la peticion tiene algun error...
            if (err) {
                res.status(500).send({
                    error: 'Request error'
                })
            } else if (!userObj || userObj === [] || userObj.length === 0) { // Si no encontramos el objeto
                res.status(404).send({
                    error: 'Not found'
                })
            } else { // Creamos el token
                var tokenData = {
                    username: username
                    // Podemos incluir fecha u otros datos relevantes
                }
                var token = jwt.sign(tokenData, 'PasswordCOMOV', {
                    expiresIn: 3600 // Duracion del token 1h
                 })
                userObj.password = undefined;
                return res.status(200).json(token);
            }
        })
    } else {
        res.status(500).send({
            error: 'Request error'
        })
    }

}