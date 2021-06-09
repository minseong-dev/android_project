const { request } = require('express');
const httpStatus = require('http-status-codes')
const db = require('../middleware/db')

class user {

    async signup(req, res) {

        try{
            
            const user_id = req.body.user_id;
            const user_password = req.body.user_password;
            const user_name = req.body.user_name;
            const user_phone_number = req.body.user_phone_number;
            const user_type = req.body.user_type;

            await db('insert into users(id, password, name, phone_number, type) values(?, ?, ?, ?, ?)', [user_id, user_password, user_name, user_phone_number, user_type])

            res.status(httpStatus.OK).send('회원가입완료')
        }

        catch (error) {
            console.error(error)
            res.status(httpStatus.INTERNAL_SERVER_ERROR).send([])
        }

    }

    async login(req, res) {
        
        try{
            
            const user_id = req.body.user_id;
            const user_password = req.body.user_password;
            let mainInfo = {};
            console.log(user_id)

            let typeStr = await db('select type from users where id=? and password=?', [user_id, user_password])
            console.log(typeStr);
            let user_type = typeStr[0].type;

            if (user_type == '관리자') {
                let farmInfo = await db('select * from farm where user_id=?', [user_id])
                let farm_id = farmInfo[0].id;
                let zoneInfo = await db('select * from zone where farm_id=?', [farm_id])
                mainInfo = {
                    farmInfo : farmInfo,
                    zoneInfo : zoneInfo,
                    userType : user_type
                }
            }
            
            else if (user_type == '사용자') {
                let zoneInfo = await db('select * from zone where user_id=?', [user_id])
                mainInfo = {
                    zoneInfo : zoneInfo,
                    userType : user_type 
                }
            }
            
            res.status(httpStatus.OK).send(mainInfo)
        }

        catch (error) {
            console.error(error)
            res.status(httpStatus.INTERNAL_SERVER_ERROR).send([])
        }

    }
}

module.exports = user