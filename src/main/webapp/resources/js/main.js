/**
 * Created by JetBrains WebStorm.
 * User: mint
 * Date: 1/26/12
 * Time: 7:06 PM
 * To change this template use File | Settings | File Templates.
 */
// Filename: main.js
define.amd.jQuery = true;
// Require.js allows us to configure shortcut alias
// There usage will become more apparent futher along in the tutorial.
require.config({
    paths: {
        jquery: 'libs/jquery-1.7.1.min',
        jquerybind: 'libs/jquery-bind',
        underscore: 'libs/underscore',
        Backbone: 'libs/backbone',
        Slick:'libs/Slick',
        fileupload:'libs/jquery.fileupload'
    }
});
require([

    // Load our app module and pass it to our definition function
    'app'
], function(App){
    // The "app" dependency is passed in as "App"
    // Again, the other dependencies passed in are not "AMD" therefore don't pass a parameter to this function
    App.initialize();

});

