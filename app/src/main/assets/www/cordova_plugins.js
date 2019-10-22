cordova.define('cordova/plugin_list', function(require, exports, module) {
  module.exports = [
    {
      "id": "cordova-plugin-signing.Signing",
      "file": "plugins/cordova-plugin-signing/www/Signing.js",
      "pluginId": "cordova-plugin-signing",
      "clobbers": [
        "cordova.plugins.Signing"
      ]
    }
  ];
  module.exports.metadata = {
    "cordova-plugin-whitelist": "1.3.4",
    "cordova-plugin-signing": "1.0.0"
  };
});