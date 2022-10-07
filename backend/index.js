const express = require('express')
const mysql = require('mysql')
const app = express()
const port = 3001

// MySQL connection
const connection = mysql.createConnection({
    host: 'localhost',
    user: 'root',
    password: 'root',
    database: 'app_db'
})

// For testing connection
connection.connect();

// Routes
app.route('/')

    .post((req, res) => {
        console.log(req);
    })

    .get((req, res) => {
        
        console.log(req);

        let query_usuarios = "SELECT * FROM USUARIO";
        connection.query(query_usuarios, (err, results, fields) => {

            if (err) {
                console.log("There was an error");
                console.log(err);
                res.json({
                        'code': 500,
                        'message': "There was an server error."
                });
            } else {
                let data = [];

                for (let i = 0; i < results.length; i++) {
                    data.push(results[i]);
                }
    
                res.json({
                        'code': 200,
                        'message': 'Values got it with success',
                        'data': data
                });
            }
        })
    })

    

// Starting to listen
app.listen(port, () => {
    console.log('Server listen on port 3001...')
})