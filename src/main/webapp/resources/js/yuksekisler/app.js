/**
 * Created by JetBrains WebStorm.
 * User: capacman
 * Date: 9/9/11
 * Time: 7:47 PM
 * To change this template use File | Settings | File Templates.
 */
dojo.provide("yuksekisler.app");

yuksekisler.app = {
    parseOnLoad: (function(config) {
        // set dojo.config.parseOnLoad to false
        // and store the original value in our own parseOnLoad property
        var parseOnLoad = config.parseOnLoad;
        config.parseOnLoad = false;
        return parseOnLoad;
    })(dojo.config),
    init: function() {

        // phase 2: load further dependencies
        dojo.require("yuksekisler.module");

        // register callback for when dependencies have loaded
        dojo.ready(dojo.hitch(this, "startup"));
    },
    startup:function() {
        //var aop = dojox.lang.aspect;
        //aop.advise(dojo, ["xhrGet","xhrPost"], {
        //    before:function() {
        //        if (!arguments['0'].content) {
        //            arguments['0'].content = {};
        //        }
        //        arguments['0'].content.ajaxRequest = true;
        //    }
        //});
        //dojo.subscribe(this.events.loginsuccsess, this, "handleLogin");
        var oldxhr = dojo.xhr;
        dojo.xhr = function() {
            return oldxhr.apply(dojo, arguments).addErrback(function(error) {
                dojo.publish("/global-ajax-error", arguments);
            });
        };
        dojo.subscribe("/global-ajax-error", this, "onGlobalError");
        dojo.subscribe(this.events.equipmentsselected, this, "showEquipments");
        dojo.subscribe(this.events.newequipment, this, "newEquipment");
        dojo.subscribe(this.events.equipmentselected, this, 'equipmentSelected');
        dojo.subscribe(this.events.definitionsselected, this, 'definitionSelected');
        dojo.subscribe(this.events.employeesselected, this, 'showEmployees');
        dojo.subscribe(this.events.worksselected, this, 'showWorks');
        dojo.subscribe(this.events.workselected, this, 'showWork');
        dojo.subscribe(this.events.newwork, this, 'showWork');

        dojo.subscribe("/dojo/hashchange", this, this.mapHistory);
        //check for user info if access denied then show login view
        dojo.parser.parse();
        this.loadingDialog = new dijit.DialogUnderlay({'class': 'loading'});

        this.hidePreloader();
        this.handleLogin();
    },
    initUi:function() {
        var toolBar = new yuksekisler.Toolbar({
            id:'navigation'
        });
        dijit.byId("header").set("content", toolBar);
        this.gridMenu = new dijit.Menu({style:{'display':'none'}});
        this.gridMenu.addChild(new dijit.MenuItem({
            label: "Delete",
            onClick:dojo.hitch(this, 'contextMenuClicked')
        }));

        this.lightbox = new dojox.image.LightboxDialog({
            id:'imageLightBox'
        }).startup();
        this.mapHistory(dojo.hash());
    },
    prepareData:function(data) {
        this.userInfo = data;
        this.equipmentStore = new dojo.store.JsonRest({target:dojo.config.applicationBase + '/equipment/',idProperty:'id'});
        this.categoryStore = new dojo.store.JsonRest({target:dojo.config.applicationBase + '/category/',idProperty:'id'});
        this.brandStore = new dojo.store.JsonRest({target:dojo.config.applicationBase + '/brand/',idProperty:'id'});
        this.employeeStore = new dojo.store.JsonRest({target:dojo.config.applicationBase + '/employee/',idProperty:'id'});
        this.titleStore = new dojo.store.JsonRest({target:dojo.config.applicationBase + '/employee/title/',idProperty:'id'});
        this.certificateStore = new dojo.store.JsonRest({target:dojo.config.applicationBase + '/employee/certificate/',idProperty:'id'});
        this.workStore = new dojo.store.JsonRest({target:dojo.config.applicationBase + '/work/',idProperty:'id'});
        this.initUi();
    },
    showEquipments:function() {
        this.clearContent();
        var equipmentListView = new yuksekisler.EquipmentListView({
            id:'equipmentListView',
            dataStore:this.equipmentStore
        });
        this.setContent(equipmentListView);
    },
    newEquipment:function() {
        var newEquipmentView = new yuksekisler.EquipmentFormView({
            id:'newEquipmentView',
            categoryStore:this.categoryStore,
            brandStore:this.brandStore,
            equipmentStore:this.equipmentStore
        });
        var newEquipmentDialog = new dijit.Dialog({
            parseOnLoad:false,
            style:'width:400px',
            onHide:function() {
                try {
                    this.destroyRecursive();
                } catch(err) {

                }
                dojo.hash('equipments');
            }
        });

        newEquipmentView.onSubmit = dojo.hitch(newEquipmentDialog, 'hide');
        newEquipmentDialog.set('content', newEquipmentView);
        newEquipmentDialog.show();
        this.loadingDialog.hide();
        //this.setContent(newEquipmentView);
    },
    getContent:function() {
        return dijit.byId('content');
    },
    clearContent:function() {
        if (this.getContent().get('content')) {
            try {
                this.getContent().destroyDescendants();
            } catch(err) {
                console.log(err);
            }
        }
    },
    setContent:function(newContent) {
        this.clearContent();
        this.getContent().set('content', newContent);
        this.loadingDialog.hide();
    },
    events:{
        //loginsuccsess:"loginsuccsess",
        categorycreated:"categorycreated",
        brandcreated:"brandcreated",
        equipmentcreated:"equipmentcreated",
        newequipment:"newequipment",
        equipmentchanged:"equipmentchanged",
        equipmentsselected:"equipmentsselected",
        equipmentselected:"equipmentselected",
        definitionsselected:"definitionsselected",
        employeesselected:"employeesselected",
        worksselected:'worksselected',
        newwork:'newwork',
        workselected:'workselected'

    },
    getHashEvent:function(hashValue) {
        if (!this.hashEvents) {
            this.hashEvents = {
                'equipments':this.events.equipmentsselected,
                'newequipment':this.events.newequipment,
                'equipment':this.events.equipmentselected,
                'definitions':this.events.definitionsselected,
                'employees':this.events.employeesselected,
                'works':this.events.worksselected,
                'newwork':this.events.newwork,
                'work':this.events.workselected
            };
        }
        return this.hashEvents[hashValue];
    },
    mapHistory:function(hashValue) {
        //hashValue might require some preprocessing
        this.loadingDialog.show();
        if (!hashValue)
            dojo.hash('equipments');
        else {
            var segments = hashValue.split('/');
            var hashEvent = this.getHashEvent(segments[0]);
            if (!hashEvent) {
                dojo.hash('equipments');
            } else {
                if (segments.length > 1)
                    dojo.publish(hashEvent, segments.slice(1));
                else
                    dojo.publish(hashEvent);
            }
        }
    },
    handleLogin:function() {
        dojo.xhrGet({
            url: dojo.config.applicationBase + "/user/current",
            handleAs: "json",
            load:function(data) {
                yuksekisler.app.prepareData(data);
            }
        });
    },
    equipmentSelected:function(equipmentId) {
        var equipment = this.equipmentStore.get(equipmentId);
        var equipmentView = new yuksekisler.EquipmentView({
            id:'equipmentView',
            categoryStore:this.categoryStore,
            brandStore:this.brandStore,
            equipmentStore:this.equipmentStore,
            equipment:equipment
        });
        this.setContent(equipmentView);
    },
    hidePreloader:function() {
        dojo.fadeOut({
            node:"preloader",
            duration:900,
            onEnd: function() {
                dojo.style("preloader", "display", "none");
            }
        }).play();
    },
    prepareImageStore:function(value, imagesAttr) {
        var storeData = [];
        for (var x in value[imagesAttr]) {
            storeData[x] = {
                "thumb":(dojo.config.applicationBase + '/file/image/' + value[imagesAttr][x].id) + '/thumbnail',
                "large":(dojo.config.applicationBase + '/file/image/' + value[imagesAttr][x].id),
                "title":value[imagesAttr][x].title,
                "link":value[imagesAttr][x].id
            };
        }
        var memoryStore = dojo.data.ObjectStore({objectStore: new dojo.store.Memory({data: storeData})});
        return memoryStore;
    },
    definitionSelected:function() {
        var definitions = new yuksekisler.Definitions({
            categoryStore:this.categoryStore,
            brandStore:this.brandStore,
            certificateStore:this.certificateStore,
            titleStore:this.titleStore
        });
        this.setContent(definitions);
    },
    showEmployees:function() {
        var employeesWidget = new yuksekisler.EmployeeListWidget({
            employeeStore:this.employeeStore
        });
        dojo.addClass(employeesWidget.domNode, 'employeeListStandAlone');
        this.setContent(employeesWidget);
    },
    showWorks:function() {
        var worksView = new yuksekisler.WorkListView({
            workStore:this.workStore
        });
        this.setContent(worksView);
    },
    showWork:function(workID) {
        if (workID) {
            var workDeferred = this.workStore.get(workID);
            var workView = new yuksekisler.WorkDefinitionView({
                workDeferred:workDeferred,
                employeeStore:this.employeeStore,
                equipmentStore:this.equipmentStore
            });
        } else {
            var workView = new yuksekisler.WorkDefinitionView({
                employeeStore:this.employeeStore,
                equipmentStore:this.equipmentStore
            });
        }
        this.setContent(workView);
    },
    onRowContextMenu:function(e) {
        yuksekisler.app.gridMenu.bindDomNode(e.grid.domNode);
        yuksekisler.app.contextMenuClicked = function(d) {
            e.grid.store.deleteItem(e.grid.getItem(e.rowIndex));
            e.grid.store.save({
                onError:function(err) {
                    if (err.status == 403)
                        alert("Item " + e.grid.getItem(e.rowIndex) + " in active use");
                }
            });
        }
    },
    contextMenuClicked:function() {
    },
    onGlobalError:function(err) {
        if (err.responseText.lastIndexOf('<html>', 7) < 0)
            var responseObject = dojo.fromJson(err.responseText);
        var errorDef = dojo.mixin(responseObject ? responseObject : {}, {status:err.status});
        switch (errorDef.status) {
            case 401:
                window.location = dojo.config.applicationBase + '/login.html';
                break;
            default:
                alert(errorDef);
        }
    }
};