var express = require('express');
var router = express.Router();

const userController = require('../controller/user')
const user = new userController()


/* 회원가입 */
router.post('/signup', user.signup);

/* 로그인 */ 
router.post('/login', user.login);

module.exports = router;