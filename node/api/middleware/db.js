const mysql = require('mysql2/promise')

const pool = mysql.createPool({
    host : '127.0.0.1',
    user : 'root',
    password : 'gPwjd1025',
    database : 'rental_farm',
})

async function query(sql, args) {
    return new Promise(async (resolve, reject) => {
        // Arrow Function
        try {
            // Check to connection of database
            const conn = await pool.getConnection()

            const [rows, fields] = await pool.query(sql, args)

            conn.release()

            resolve(rows)
        } catch(err) {
            console.error(err)

            resolve([])
        }
    })
}

module.exports = query