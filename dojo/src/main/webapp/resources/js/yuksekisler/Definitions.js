/**
 * Created by JetBrains WebStorm.
 * User: capacman
 * Date: 10/12/11
 * Time: 2:19 AM
 * To change this template use File | Settings | File Templates.
 */
dojo.provide('yuksekisler.Definitions');

dojo.declare('yuksekisler.Definitions', [dijit._Widget,dijit._Templated], {
    templateString:dojo.cache('yuksekisler.Definitions', '../../../templates/definitions_template.html'),
    widgetsInTemplate:true,
    categoryStore:null,
    brandStore:null,
    postCreate:function() {
        this.categoryGrid = new dojox.grid.DataGrid({
            query:{},
            selectionMode:'single',
            id:'categoryGrid',
            rowCount:10,
            store:new dojo.data.ObjectStore({objectStore: this.categoryStore}),
            autoHeight:5,
            structure:
                [
                    {name:'Name',field:'name',width:'auto'},
                    {name:'Description',field:'description',width:'auto'}
                ],
            onRowContextMenu:dojo.hitch(this, yuksekisler.app.onRowContextMenu)
        });
        this.categoryGridContainer.set('content', this.categoryGrid);
        this.categoryGrid.startup();

        this.brandGrid = new dojox.grid.DataGrid({
            query:{},
            selectionMode:'single',
            id:'brandGrid',
            rowCount:10,
            store:new dojo.data.ObjectStore({objectStore: this.brandStore}),
            autoHeight:5,
            structure:
                [
                    {name:'Name',field:'name',width:'auto'},
                    {name:'Description',field:'description',width:'auto'}
                ],
            onRowContextMenu:dojo.hitch(this, yuksekisler.app.onRowContextMenu)
        });
        this.brandGridContainer.set('content', this.brandGrid);
        this.brandGrid.startup();
        this.inherited(arguments);
    },
    onBrand:function() {
        this.brandStore.put(this.brandForm.get('value')).then(dojo.hitch(this, function() {
            this.brandForm.reset();
            this.brandGrid._refresh();
        }));
    },
    onCategory:function() {
        this.categoryStore.put(this.categoryForm.get('value')).then(dojo.hitch(this, function() {
            this.categoryForm.reset();
            this.categoryGrid._refresh();
        }));
    }
});