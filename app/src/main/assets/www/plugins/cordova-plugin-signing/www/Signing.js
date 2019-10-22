cordova.define("cordova-plugin-signing.Signing", function(require, exports, module) {
var exec = require('cordova/exec');

//exports.sign = function (arg0, success, error) {
//    exec(success, error, 'Signing', 'coolMethod', [arg0]);
exports.sign =  function(str,callback,error){
if(str=='sign'){
    exec(callback, error, 'Signing', 'signaction');
}

};

});
