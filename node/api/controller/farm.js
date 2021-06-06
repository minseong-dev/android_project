const httpStatus = require('http-status-codes')
const db = require('../middleware/db')

class farm {

    async info(req, res) {

        let date_sent = 0;

        try{

            const zone_name = req.body.zone_name;
            const zone_contract_date = req.body.zone_contract_date;
            const zone_user_id = req.body.zone_user_id;
            const zone_farm_id = req.body.zone_farm_id;
            let zone_id = 10000000 * Math.random();

            await db('insert into zone(id, contract_date, user_id, farm_id, zone_name) values(?, ?, ?, ?, ?)', [zone_id, zone_contract_date, zone_user_id, zone_farm_id, zone_name])

            res.status(httpStatus.OK).send('구역등록완료')
        }
        
        catch (error) {
            console.error(error)
            res.status(httpStatus.INTERNAL_SERVER_ERROR).send([])
        }

    }

    async info_update(req, res) {

        try{

            const zone_id = req.body.zone_id;
            const zone_name = req.body.zone_name;
            const zone_contract_date = req.body.zone_contract_date;
            const zone_user_id = req.body.zone_user_id;

            if (zone_name.length > 1) {
                await db('update zone set zone_name=? where id=?', [zone_name, zone_id])
            }

            if (zone_contract_date.length > 1) {
                await db('update zone set contract_date=? where id=?', [zone_contract_date, zone_id])
            }

            if (zone_user_id.length > 1) {
                await db('update zone set user_id=? where id=?', [zone_user_id, zone_id])
            }

            res.status(httpStatus.OK).send('구역수정완료')

        }
        
        catch (error) {
            console.error(error)
            res.status(httpStatus.INTERNAL_SERVER_ERROR).send([])
        }

    }

    async info_delete(req, res) {

        try{

            const zone_id = req.body.zone_id;

            await db('delete from zone where id=?', [zone_id])

            res.status(httpStatus.OK).send('구역삭제완료')
        }
        
        catch (error) {
            console.error(error)
            res.status(httpStatus.INTERNAL_SERVER_ERROR).send([])
        }

    }

    async status(req, res) {

        try{
            const zone_id = req.query.zone_id;

            let zoneInfo = await db('select * from zone where id=?', [zone_id])
            const mainInfo = {
                zoneInfo : zoneInfo
            }

            res.status(httpStatus.OK).send(mainInfo)
        }
        
        catch (error) {
            console.error(error)
            res.status(httpStatus.INTERNAL_SERVER_ERROR).send([])
        }

    }

    async status_update(req, res) {

        try{

            const zone_id = req.body.zone_id;
            const zone_crop_name = req.body.zone_crop_name;

            await db('update zone set crop_name=? where id=?', [zone_crop_name, zone_id])

            res.status(httpStatus.OK).send('재배식물명수정완료')
        }
        
        catch (error) {
            console.error(error)
            res.status(httpStatus.INTERNAL_SERVER_ERROR).send([])
        }

    }

    async chat(req, res) {

        try{

            let chat_id = 1000000 * Math.random();
            const zone_id = req.body.zone_id;
            const date_sent = req.body.date_sent;
            const type_sent = req.body.type_sent;
            const chat_type = req.body.chat_type;
            const chat_text = req.body.chat_text;
            const chat_img = req.body.chat_img;

            if (chat_type == 'text') {
                await db('insert into chat(id, date_sent, chat_text, type_sent, zone_id, chat_type) values(?, ?, ?, ?, ?, ?)', 
                [chat_id, date_sent, chat_text, type_sent, zone_id, chat_type])
            } 
            else if (chat_type == 'img') {
                await db('insert into chat(id, date_sent, chat_img, type_sent, zone_id, chat_type) values(?, ?, ?, ?, ?, ?)', 
                [chat_id, date_sent, chat_img, type_sent, zone_id, chat_type])
            }
        
            res.status(httpStatus.OK).send('채팅등록완료')

        }
        
        catch (error) {
            console.error(error)
            res.status(httpStatus.INTERNAL_SERVER_ERROR).send([])
        }
    }

    async chatting(req, res) {

        try{

            const zone_id = req.body.zone_id;

            let chatInfo = await db('select chat_text, chat_img, type_sent from chat where zone_id=?', [zone_id])
            const mainInfo = {
                chatInfo : chatInfo
            }
            
            res.status(httpStatus.OK).send(mainInfo)

        }
        
        catch (error) {
            console.error(error)
            res.status(httpStatus.INTERNAL_SERVER_ERROR).send([])
        }
    }
}

module.exports = farm