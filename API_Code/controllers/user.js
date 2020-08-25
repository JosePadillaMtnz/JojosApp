'use strict'

// Cargamos los modelos para usarlos posteriormente
var User = require('../models/user');

// Obtener un usuario dado un ID
exports.getUser = function (req, res) {
    User.findById(req.params.id).exec(function (err, userObj) {
        // Si la peticion tiene algun error...
        if (err) {
            console.log(err);
            res.status(500).send({
                error: 'Request error'
            })
        } else if (!userObj) { // Si no encontramos el objeto
            res.status(404).send({
                error: 'Not found'
            })
        } else { // Podemos modificar el objeto antes de devolverlo, por ejemplo, quitando elementos.
            userObj.password = undefined;
            return res.status(200).send({
                userObj
            });
        }
    });
}

// Obtener un usuario dado un nombre
exports.getUserByName = function (req, res) {
    User.find({name:req.params.name}).exec(function (err, userObj) {
        // Si la peticion tiene algun error...
        if (err) {
            console.log(err);
            res.status(500).send({
                error: 'Request error'
            })
        } else if (!userObj) { // Si no encontramos el objeto
            res.status(404).send({
                error: 'Not found'
            })
        } else { // Podemos modificar el objeto antes de devolverlo, por ejemplo, quitando elementos.
            userObj[0].pHash = undefined;
            return res.status(200).send({
                userObj
            });
        }
    });
}

// Introducir un nuevo usuario
exports.postUser = function (req, res) {
    var uName = req.body.name;
    var uMail = req.body.email;
    var uHash = req.body.pHash;
    if (!(uName && uMail && uHash) || uName.length === 0 || uMail.length === 0 || uHash.length === 0) {
        return res.status(400).send({
            error: 'Missing fields'
        })
    } else {
        User.find({$or : [{name : uName}, {email : uMail}]}).exec(function (err, userObj) {
            if (err || userObj.length == 0) {
                User.create(req.body, function(err, newUser) {
                    if(err) {
                        return res.status(500).send({
                            body: req.body,
                            error: 'Server error'
                        });
                    } else {
                        res.status(201).json(newUser)
                    }
                })
            }
            else {
                return res.status(409).send({
                    error: 'Username or email already used'
                })
            }
        }) 
    } 
}

exports.modifyUser = function(req, res) {
    var uName = req.body.name;
    var field = req.body.field;
    var value = req.body.value;  
    if (!(uName && field && value) || uName.length === 0 || field.length === 0 || value.length === 0) {
        return res.status(400).send({
            error: 'Missing fields'
        })
    } else {
        if (field === "name" || field === "email") {
            User.find({$or : [{name : value}, {email : value}]}).exec(function (err, userObj) {
                if (!(err || userObj.length == 0)) {
                    return res.status(409).send({
                        error: 'Username or email already used'
                    })
                }
            })
        }
        var query = {};
        query[field] = value;
        User.where({name : uName}).updateOne(query).exec(function (err, userObj) {
            if (err) {
                return res.status(500).send({
                    body: req.body,
                    error: 'Server error'
                });
            } else {
                res.status(201).json(userObj)
            }
        })
    }
}

exports.deleteUser = function(req, res) {
    var uName = req.params.name;
    if (!uName || uName.length === 0) {
        return res.status(400).send({
            error: 'Missing fields'
        })
    } else {
        User.deleteOne({name : uName}).exec(function (err, userObj) {
            if (err) {
                return res.status(500).send({
                    body: req.body,
                    error: 'Server error'
                });
            } else {
                res.status(201).json(userObj)
            }
        })
    }
}
