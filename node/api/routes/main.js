var express = require('express');
var router = express.Router();

const mainController = require('../controller/main')
const main = new mainController()

/* GET home page. */
router.get('/', main.main);

module.exports = router;
