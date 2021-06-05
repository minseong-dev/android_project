const httpStatus = require('http-status-codes')
const db = require('../middleware/db')

class main {

    async main(req, res) {

        try {

            const id = 'minn'
            const password = '1234'

            let minn = await db('select * from users where id = ? and password = ?', [id, password])
            let userInfo = await db('select * from users')

            const mainInfo = {
                userInfo : userInfo,
                minn : minn
            }

            res.status(httpStatus.OK).send(mainInfo)

            
        } catch (error) {
            console.error(error)
            res.status(httpStatus.INTERNAL_SERVER_ERROR).send([])
        }
        
    }

}

module.exports = main