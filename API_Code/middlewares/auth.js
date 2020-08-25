// Este middleware se encarga de verificar que las peticiones realizadas al API vengan autenticadas mediante JWT
'user strict'

const jwt = require('jsonwebtoken');

exports.ensureAuth = function (req, res, next) {
    if (!req.headers.authorization) {
        return res.status(403).send({
            message: 'Missing authorization header'
        });
    } else {
        var token = req.headers.authorization.replace(/['"]+/g, '');

        jwt.verify(token, 'PasswordCOMOV', function(err) {
            if (err) {
                res.status(401).send({
                    message: 'Token not valid'
                })
            } else {
                next();
            }
        })
        
    }
}