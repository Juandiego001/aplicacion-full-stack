const express = require('express')
const mysql = require('mysql')
const app = express()
const port = 3001

app.use(express.json())
app.use(express.urlencoded({extended: false}))

// MySQL connection
const connection = mysql.createConnection({
    host: 'localhost',
    user: 'root',
    password: 'root',
    database: 'app_db'
});

// For testing connection
connection.connect();

// Routes
app.route('/')

    .delete((req, res) => {
        let cedula = req.body.cedula;

        let query_eliminar = "DELETE FROM `USUARIO` WHERE `cedula` = ?";

        connection.query(query_eliminar, [cedula], (err, results, fields) => {
            if (err) {
                res.json({
                    'code': 300,
                    'message': 'Failed on delete user'
                })
            } else {
                res.json({
                    'code': 200,
                    'message': 'User deleted with success'
                })
            }
        })
    })

    .put((req, res) => {
        let cedulaOriginal = req.body.cedulaOriginal;
        let nuevaCedula = req.body.nuevaCedula;
        let nuevaContrasena = req.body.nuevaContrasena;
        let nuevoNombre = req.body.nuevoNombre;
        let nuevoApellido = req.body.nuevoApellido;
        let nuevoTelefono = req.body.nuevoTelefono;

        let query_actualizar = "UPDATE `USUARIO` SET `cedula` = ?, `contrasena` = ?, `nombre` = ?, " +
            "`apellido` = ?, `telefono` = ? WHERE cedula = ?";

        connection.query(query_actualizar, [nuevaCedula, nuevaContrasena, nuevoNombre, nuevoApellido, 
            nuevoTelefono, cedulaOriginal], (err, results, fields) => {
                if (err) {
                    res.json({
                        'code': 300,
                        'message': 'Failed on update'
                    })
                } else {
                    res.json({
                        'code': 200,
                        'message': 'Successful update'
                    })
                }
            })
    })

    .post((req, res) => {
      let cedula = req.body.cedula;
      let contrasena = req.body.contrasena;
      let nombre = req.body.nombre;
      let apellido = req.body.apellido;
      let telefono = req.body.telefono;

      let query_registrarse = "INSERT INTO `USUARIO` VALUES(?, ?, ?, ?, ?)"

      connection.query(query_registrarse, [cedula, contrasena, nombre, apellido, telefono], 
        (err, results, fields) => {
            if (err) {
                res.json({
                    'code': 300,
                    'message': 'There was an error while trying to sing up'
                })
            } else {
                res.json({
                    'code': 200,
                    'message': 'Successful sing up'
                })
            }
        });
    })

    .get((req, res) => {
        let cedula = req.query.cedula;
        let contrasena = req.query.contrasena;
        
        let query_iniciar = "SELECT * FROM `USUARIO` WHERE `cedula` = ? AND `contrasena` = ?";

        connection.query(query_iniciar, [cedula, contrasena], (err, results, fields) => {
            if (err) {
                console.log("There was an error");
                console.log(err);
                res.json({
                        'code': 500,
                        'message': "There was an server error."
                });
            } else {
                
                // RowDataPacket {
                //     cedula: 123,
                //     contrasena: 'juan123',
                //     nombre: 'Juan',       
                //     apellido: 'C.',       
                //     telefono: '3101234446'
                //   }

                if (results.length > 0) {
                    res.json({
                        'code': 200,
                        'message': 'Get values with success.',
                        'data': results[0]
                    });
                } else {
                    res.json({
                        'code': 300,
                        'message': 'There are no users in the database with that values.',
                    });
                }
            }
        })
    })

// Starting to listen
app.listen(port, () => {
    console.log('Server listen on port 3001...')
})