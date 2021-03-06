/**
 * Created by JetBrains WebStorm.
 * User: capacman
 * Date: 10/12/11
 * Time: 2:19 AM
 * To change this template use File | Settings | File Templates.
 */

define(["dojo/_base/declare", "dijit/_Widget", "dijit/_Templated", "dojo", "dojo/data/ObjectStore", "dojox/grid/DataGrid", "yuksekisler/misc"], function (declare, _Widget, _Templated, dojo, ObjectStore, DataGrid, misc) {
    return declare([_Widget, _Templated], {
        templateString:dojo["cache"]('yuksekisler.templates', 'definitions_template.html', {sanitize:true}),
        widgetsInTemplate:true,
        categoryStore:null,
        brandStore:null,
        titleStore:null,
        certificateStore:null,
        postCreate:function () {
            this.categoryGrid = new DataGrid({
                query:{},
                selectionMode:'single',
                id:'categoryGrid',
                rowCount:10,
                store:new ObjectStore({objectStore:this.categoryStore, labelProperty:'name'}),
                autoHeight:5,
                structure:[
                    {name:'Name', field:'name', width:'auto'},
                    {name:'Description', field:'description', width:'auto'}
                ],
                onRowContextMenu:dojo.hitch(this, misc.onRowContextMenu)
            });
            this.categoryGridContainer.set('content', this.categoryGrid);
            this.categoryGrid.startup();
            dojo.connect(this.categoryName, "onKeyUp", this, this.categoryKeyUp);
            this.categoryName.validator = dojo.hitch(this, this.validateCategoryName);

            this.brandGrid = new DataGrid({
                query:{},
                selectionMode:'single',
                id:'brandGrid',
                rowCount:10,
                store:new ObjectStore({objectStore:this.brandStore, labelProperty:'name'}),
                autoHeight:5,
                structure:[
                    {name:'Name', field:'name', width:'auto'},
                    {name:'Description', field:'description', width:'auto'}
                ],
                onRowContextMenu:dojo.hitch(this, misc.onRowContextMenu)
            });
            this.brandGridContainer.set('content', this.brandGrid);
            this.brandGrid.startup();
            dojo.connect(this.brandName, "onKeyUp", this, this.brandKeyUp);
            this.brandName.validator = dojo.hitch(this, this.validateBrandName);

            this.titleGrid = new DataGrid({
                query:{},
                selectionMode:'single',
                id:'titleGrid',
                rowCount:10,
                store:new ObjectStore({objectStore:this.titleStore, labelProperty:'name'}),
                autoHeight:5,
                structure:[
                    {name:'Name', field:'name', width:'auto'},
                    {name:'Description', field:'description', width:'auto'}
                ],
                onRowContextMenu:dojo.hitch(this, misc.onRowContextMenu)
            });
            this.titleGridContainer.set('content', this.titleGrid);
            this.titleGrid.startup();
            dojo.connect(this.titleName, "onKeyUp", this, this.titleKeyUp);
            this.titleName.validator = dojo.hitch(this, this.validateTitleName);

            this.certificateGrid = new dojox.grid.DataGrid({
                query:{},
                selectionMode:'single',
                id:'certificateGrid',
                rowCount:10,
                store:new dojo.data.ObjectStore({objectStore:this.certificateStore, labelProperty:'name'}),
                autoHeight:5,
                structure:[
                    {name:'Name', field:'name', width:'auto'},
                    {name:'Description', field:'description', width:'auto'}
                ],
                onRowContextMenu:dojo.hitch(this, misc.onRowContextMenu)
            });
            this.certificateGridContainer.set('content', this.certificateGrid);
            this.certificateGrid.startup();
            dojo.connect(this.certificateName, "onKeyUp", this, this.certificateKeyUp);
            this.certificateName.validator = dojo.hitch(this, this.validateCertificateName);
            this.inherited(arguments);
        },
        onBrand:function () {
            if (this.brandForm.validate()) {
                dojo.when(this.brandStore.put(this.brandForm.get('value')), dojo.hitch(this, function (id) {
                    dojo.when(this.brandStore.get(id), function (data) {
                        dojo.publish("globalMessageTopic", [
                            {
                                message:'Brand ' + data.name + ' saved'
                            }
                        ]);
                    });
                    this.brandForm.reset();
                    this.brandGrid._refresh();
                }));
            }
        },
        onCategory:function () {
            if (this.categoryForm.validate()) {
                dojo.when(this.categoryStore.put(this.categoryForm.get('value')), dojo.hitch(this, function (id) {
                    dojo.when(this.categoryStore.get(id), function (data) {
                        dojo.publish("globalMessageTopic", [
                            {
                                message:'Category ' + data.name + ' saved'
                            }
                        ]);
                    });
                    this.categoryForm.reset();
                    this.categoryGrid._refresh();
                }));
            }
        },
        onTitle:function () {
            if (this.titleForm.validate()) {
                dojo.when(this.titleStore.put(this.titleForm.get('value')), dojo.hitch(this, function (id) {
                    dojo.when(this.titleStore.get(id), function (data) {
                        dojo.publish("globalMessageTopic", [
                            {
                                message:'Title ' + data.name + ' saved'
                            }
                        ]);
                    });
                    this.titleForm.reset();
                    this.titleGrid._refresh();
                }));
            }
        },
        onCertificate:function () {
            if (this.certificateForm.validate()) {
                dojo.when(this.certificateStore.put(this.certificateForm.get('value')), dojo.hitch(this, function (id) {
                    dojo.when(this.certificateStore.get(id), function (data) {
                        dojo.publish("globalMessageTopic", [
                            {
                                message:'Certificate type ' + data.name + ' saved'
                            }
                        ]);
                    });
                    this.certificateForm.reset();
                    this.certificateGrid._refresh();
                }));
            }
        },
        validateCategoryName:function (value, constraints) {
            return this.ajaxValidate(value, constraints, this.categoryName, this.categoryStore)
        },
        validateBrandName:function (value, constraints) {
            return this.ajaxValidate(value, constraints, this.brandName, this.brandStore)
        },
        validateTitleName:function (value, constraints) {
            return this.ajaxValidate(value, constraints, this.titleName, this.titleStore)
        },
        validateCertificateName:function (value, constraints) {
            return this.ajaxValidate(value, constraints, this.certificateName, this.certificateStore)
        },
        ajaxValidate:function (value, constraints, ctr, store) {
            if (constraints.duplicateName && constraints.ajaxValidaton) {
                constraints.ajaxValidaton = false;
                return false;
            } else if (ctr._isEmpty(ctr.get('value')))
                return false
            if (!ctr.focused && constraints.duplicateName)
                return false;
            return true;
        },
        validateWithTimer:function (ctr, store, value) {
            if (ctr._isEmpty(value)) {
                ctr.constraints = {};
                return;
            }
            if (this.timerId) {
                clearTimeout(this.timerId);
            }

            this.timerId = setTimeout(function () {
                dojo.when(store.query({name:value}), function (results) {
                    var duplicateName = dojo.some(results, function (arrayValue) {
                        console.log(arrayValue);
                        if (arrayValue.name == value)
                            return true;
                        return false;
                    });
                    ctr.constraints = {'duplicateName':duplicateName, ajaxValidaton:true};
                    ctr.validate(false);
                });
            }, 400);
        },
        categoryKeyUp:function (e) {
            this.validateWithTimer(this.categoryName, this.categoryStore, this.categoryName.get('value'));
        },
        brandKeyUp:function (e) {
            this.validateWithTimer(this.brandName, this.brandStore, this.brandName.get('value'));
        },
        titleKeyUp:function (e) {
            this.validateWithTimer(this.titleName, this.titleStore, this.titleName.get('value'));
        },
        certificateKeyUp:function (e) {
            this.validateWithTimer(this.certificateName, this.certificateStore, this.certificateName.get('value'));
        }
    });
});