var express = require('express');
var router = express.Router();

const mainRoute = require('./main')
const userRoute = require('./user')
const farmRoute = require('./farm')

router.use('/', mainRoute)
router.use('/user', userRoute)
router.use('/farm', farmRoute)

module.exports = router;
