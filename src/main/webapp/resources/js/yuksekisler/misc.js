/**
 * Created by JetBrains WebStorm.
 * User: mint
 * Date: 1/3/12
 * Time: 3:51 PM
 * To change this template use File | Settings | File Templates.
 */
define(["dojo", "dojo/data/ObjectStore", "dojo/store/Memory", "dojo/store/Cache",
    "dojo/store/JsonRest", "dojox/image/Lightbox", "dijit/DialogUnderlay", "dijit/Menu", "dijit/MenuItem","dijit/Dialog"],
    function (dojo, ObjectStore, Memory, Cache, JsonRest, Lightbox, DialogUnderlay, Menu, MenuItem,Dialog) {
        var loadingDialog = new DialogUnderlay({'class':'loading'});
        var gridMenu = new Menu({style:{'display':'none'}});
        var contextMenuClicked = function () {

        }
        gridMenu.addChild(new MenuItem({
            label:"Delete",
            onClick:dojo.hitch(this, function (d) {
                contextMenuClicked(d);
            })
        }));
        var dialog = new Dialog({
            style:"width:300px"
        });
        var lightbox = new dojox.image.LightboxDialog({
            id:'imageLightBox'
        }).startup();
        return {
            onGlobalError:function (err) {
                if (err.responseText && err.responseText.lastIndexOf('<html>', 7) < 0)
                    var responseObject = dojo.fromJson(err.responseText);
                var errorDef = dojo.mixin(responseObject ? responseObject : {}, {status:err.status});
                switch (errorDef.status) {
                    case 401:
                        window.location = dojo.config.applicationBase + '/login.html';
                        break;
                }
            },
            turnToPromise:function (val) {
                var deferred = new dojo.Deferred();
                dojo.when(val, function (result) {
                    deferred.resolve(result);
                }, function (err) {
                    deferred.reject(result);
                });
                return deferred;
            },
            contextMenuClicked:function () {
            },
            loadingDialog:loadingDialog,
            onRowContextMenu:function (e) {
                gridMenu.bindDomNode(e.grid.domNode);
                contextMenuClicked = function (d) {
                    var item = e.grid.getItem(e.rowIndex);
                    e.grid.store.deleteItem(item);
                    e.grid.store.save({
                        onError:function (err) {
                            if (err.status == 403) {
                                var responseObject = dojo.fromJson(err.responseText);
                                dojo.publish("globalMessageTopic", [
                                    {
                                        message:responseObject.errorTextCode,
                                        type:'warning'
                                    }
                                ]);
                            }
                        },
                        onComplete:function () {
                            dojo.publish("globalMessageTopic", [
                                {
                                    message:e.grid.store.getLabel(item) + ' deleted!'
                                }
                            ]);
                        }
                    });
                }
            },
            prepareImageStore:function (value, imagesAttr) {
                var storeData = [];
                for (var x in value[imagesAttr]) {
                    storeData[x] = {
                        "thumb":(dojo.config.applicationBase + '/file/image/' + value[imagesAttr][x].id) + '/thumbnail',
                        "large":(dojo.config.applicationBase + '/file/image/' + value[imagesAttr][x].id),
                        "title":value[imagesAttr][x].title,
                        "link":value[imagesAttr][x].id
                    };
                }
                var memoryStore = new ObjectStore({objectStore:new Memory({data:storeData})});
                return memoryStore;
            },
            prepareData:function (data) {
                this.userInfo = data;
                this.equipmentStore = new Cache(new JsonRest({target:dojo.config.applicationBase + '/equipment/', idProperty:'id'}), new Memory({}));
                this.categoryStore = new Cache(new JsonRest({target:dojo.config.applicationBase + '/category/', idProperty:'id'}), new Memory({}));
                this.brandStore = new Cache(new JsonRest({target:dojo.config.applicationBase + '/brand/', idProperty:'id'}), new Memory({}));
                this.employeeStore = new Cache(new JsonRest({target:dojo.config.applicationBase + '/employee/', idProperty:'id'}), new Memory({}));
                this.titleStore = new Cache(new JsonRest({target:dojo.config.applicationBase + '/employee/title/', idProperty:'id'}), new Memory({}));
                this.certificateStore = new Cache(new JsonRest({target:dojo.config.applicationBase + '/employee/certificate/', idProperty:'id'}), new Memory({}));
                this.workStore = new Cache(new JsonRest({target:dojo.config.applicationBase + '/work/', idProperty:'id'}), new Memory({}));
            },
            showDialog:function(title,content){
                dialog.set('content',content);
                dialog.set('title',title);
                dialog.show();
            }
        }
    });