cordova.define('cordova/plugin_list', function(require, exports, module) {
module.exports = [
  {
    "id": "com.mcierzniak.cordova.plugin.inappbrowserxwalk.inAppBrowserXwalk",
    "file": "plugins/com.mcierzniak.cordova.plugin.inappbrowserxwalk/www/inappbrowserxwalk.js",
    "pluginId": "com.mcierzniak.cordova.plugin.inappbrowserxwalk",
    "clobbers": [
      "inAppBrowserXwalk"
    ]
  }
];
module.exports.metadata = 
// TOP OF METADATA
{
  "cordova-plugin-whitelist": "1.3.3",
  "cordova-plugin-crosswalk-webview": "2.4.0",
  "com.mcierzniak.cordova.plugin.inappbrowserxwalk": "0.4.0",
  "cordova-android-support-gradle-release": "1.3.0"
};
// BOTTOM OF METADATA
});