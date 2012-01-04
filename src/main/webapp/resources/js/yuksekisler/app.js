/**
 * Created by JetBrains WebStorm.
 * User: capacman
 * Date: 9/9/11
 * Time: 7:47 PM
 * To change this template use File | Settings | File Templates.
 */

define(["dojo", "yuksekisler/Toolbar", "dijit/Dialog", "dojox/fx/scroll",
    "yuksekisler/Definitions", "yuksekisler/Employee", "yuksekisler/Work", "dijit/_base/manager",
    "yuksekisler/Equipment", "yuksekisler/misc", "dojo/hash" , "dojox/widget/Toaster"],
    function (dojo, Toolbar, Dialog, scroll, Definitions, Employee, Work, dijit, Equipment, misc, hash) {
        return {
            parseOnLoad:(function (config) {
                // set dojo.config.parseOnLoad to false
                // and store the original value in our own parseOnLoad property
                var parseOnLoad = config.parseOnLoad;
                config.parseOnLoad = false;
                return parseOnLoad;
            })(dojo.config),
            init:function () {

                // register callback for when dependencies have loaded
                dojo.ready(dojo.hitch(this, "startup"));
            },
            startup:function () {
                var oldxhr = dojo.xhr;
                dojo.xhr = function () {
                    return oldxhr.apply(dojo, arguments).addErrback(function (error) {
                        dojo.publish("/global-ajax-error", arguments);
                    });
                };
                dojo.subscribe("/global-ajax-error", this, misc.onGlobalError);
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
                //dojo.parser.parse();
                //this.loadingDialog = new DialogUnderlay({'class':'loading'});

                this.hidePreloader();
                this.handleLogin();
            },
            initUi:function () {
                var toolBar = new Toolbar({
                    id:'navigation'
                });
                dijit.byId("header").set("content", toolBar);
                var contentPosition = dojo.position(dojo.query('.content')[0]);
                var leftValue = contentPosition.x + contentPosition.w / 2;
                dojo.place(dijit.byId("toaster").domNode, dojo.byId('toasterWrapper'));
                dojo.style('toasterWrapper', {
                    'position':'absolute',
                    top:contentPosition.y + 'px',
                    left:contentPosition.x + 'px',
                    width:contentPosition.w + 'px'
                });
                this.mapHistory(hash());
            },
            showEquipments:function () {
                this.clearContent();
                var equipmentListView = new Equipment.EquipmentListView({
                    id:'equipmentListView',
                    dataStore:misc.equipmentStore
                });
                this.setContent(equipmentListView);
            },
            newEquipment:function () {
                var newEquipmentView = new Equipment.EquipmentFormView({
                    id:'newEquipmentView',
                    categoryStore:misc.categoryStore,
                    brandStore:misc.brandStore,
                    equipmentStore:misc.equipmentStore
                });
                var newEquipmentDialog = new Dialog({
                    parseOnLoad:false,
                    style:'width:400px',
                    onHide:function () {
                        try {
                            this.destroyRecursive();
                        } catch (err) {

                        }
                        hash('equipments');
                    }
                });

                newEquipmentView.onSubmit = dojo.hitch(newEquipmentDialog, 'hide');
                newEquipmentDialog.set('content', newEquipmentView);
                newEquipmentDialog.show();
                misc.loadingDialog.hide();
                //this.setContent(newEquipmentView);
            },
            getContent:function () {
                return dijit.byId('content');
            },
            clearContent:function () {
                if (this.getContent().get('content')) {
                    try {
                        this.getContent().destroyDescendants();
                    } catch (err) {
                        console.log(err);
                    }
                }
            },
            setContent:function (newContent) {
                this.clearContent();
                this.getContent().set('content', newContent);
                misc.loadingDialog.hide();
                //dojo.window.scrollIntoView(dojo.body(), {y:-20,x:0});
                scroll({node:dojo.body(), win:window, duration:400}).play();
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
            getHashEvent:function (hashValue) {
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
            mapHistory:function (hashValue) {
                //hashValue might require some preprocessing
                misc.loadingDialog.show();
                if (!hashValue)
                    hash('equipments');
                else {
                    var segments = hashValue.split('/');
                    var hashEvent = this.getHashEvent(segments[0]);
                    if (!hashEvent) {
                        hash('equipments');
                    } else {
                        if (segments.length > 1)
                            dojo.publish(hashEvent, segments.slice(1));
                        else
                            dojo.publish(hashEvent);
                    }
                }
            },
            handleLogin:function () {
                dojo.xhrGet({
                    url:dojo.config.applicationBase + "/user/current",
                    handleAs:"json",
                    load:dojo.hitch(this, function (data) {
                        misc.prepareData(data);
                        this.initUi();
                    })
                });
            },
            equipmentSelected:function (equipmentId) {
                var equipment = misc.equipmentStore.get(equipmentId);
                var equipmentView = new Equipment.EquipmentView({
                    id:'equipmentView',
                    categoryStore:misc.categoryStore,
                    brandStore:misc.brandStore,
                    equipmentStore:misc.equipmentStore,
                    equipment:equipment
                });
                this.setContent(equipmentView);
            },
            hidePreloader:function () {
                dojo.fadeOut({
                    node:"preloader",
                    duration:900,
                    onEnd:function () {
                        dojo.style("preloader", "display", "none");
                    }
                }).play();
            },
            definitionSelected:function () {
                var definitions = new Definitions({
                    categoryStore:misc.categoryStore,
                    brandStore:misc.brandStore,
                    certificateStore:misc.certificateStore,
                    titleStore:misc.titleStore
                });
                this.setContent(definitions);
            },
            showEmployees:function () {
                var employeesWidget = new Employee.EmployeeListWidget({
                    employeeStore:misc.employeeStore
                });
                dojo.addClass(employeesWidget.domNode, 'employeeListStandAlone');
                this.setContent(employeesWidget);
            },
            showWorks:function () {
                var worksView = new Work.WorkListView({
                    workStore:misc.workStore
                });
                this.setContent(worksView);
            },
            showWork:function (workID) {
                if (workID) {
                    var workDeferred = misc.workStore.get(workID);
                    var workView = new Work.WorkDefinitionView({
                        workDeferred:workDeferred,
                        employeeStore:misc.employeeStore,
                        equipmentStore:misc.equipmentStore,
                        categoryStore:misc.categoryStore
                    });
                } else {
                    var workView = new Work.WorkDefinitionView({
                        employeeStore:misc.employeeStore,
                        equipmentStore:misc.equipmentStore,
                        categoryStore:misc.categoryStore
                    });
                }
                workView.startup();
                this.setContent(workView);
            }
        };
    });
