var express = require('express');
var router = express.Router();

const farmController = require('../controller/farm')
const farm = new farmController()


/* 구역 정보 입력 */
router.post('/', farm.info);

/* 구역 정보 수정 */
router.patch('/', farm.info_update);

/* 구역 정보 삭제 */
router.delete('/', farm.info_delete);

/* 상태 표시(값 전달) */
router.get('/zone', farm.status);

/* 상태 수정(재배식물명) */
router.patch('/zone', farm.status_update);

/* 채팅 입력*/
router.post('/zone/chat', farm.chat);

/* 채팅 최신화 */

module.exports = router;