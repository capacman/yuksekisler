/**
 * Created by JetBrains WebStorm.
 * User: capacman
 * Date: 9/15/11
 * Time: 2:37 PM
 * To change this template use File | Settings | File Templates.
 */

dojo.provide('yuksekisler.EquipmentListView');

dojo.require('dijit.layout.BorderContainer');
dojo.require('dijit._Templated');

dojo.declare('yuksekisler.EquipmentListView', [dijit.layout.BorderContainer,dijit._Templated], {
    templateString:dojo.cache('yuksekisler.EquipmentListView', dojo.moduleUrl('yuksekisler', '../../templates/equipment_list_view_template.html')),
    widgetsInTemplate:true,
    dataStore:null,
    gutters:false,
    postCreate:function() {
        this.grid = new dojox.grid.DataGrid({
            query:{},
            region:'center',
            selectionMode:'single',
            id:'equipmentListGrid',
            store:dojo.data.ObjectStore({objectStore: this.dataStore}),
            structure:[
                {name:'Product Name',field:'productName',width:'auto'},
                {name:'Product Code',field:'productCode',width:'auto'},
                {name:'Image',get:this.getImagePath,width:'auto',formatter:this.formatImage},
                {name:'Brand',get:this.getBrandName,width:'auto'},
                {name:'Category',get:this.getCategoryName,width:'auto'},
                {name:'Stock Entrance',field:'stockEntrance',formatter:this.formatDate,width:'auto'}
            ]
        });
        this.addChild(this.grid);
        this.grid.startup();
        dojo.connect(this.searchBox, 'onKeyUp', this, this.textBoxChanged);
        dojo.connect(this.grid, 'onRowClick', this, this.onRowClick);
        this.newEquipment = new dijit.form.Button({
            label:'New Equipment',
            region:'bottom',
            onClick:function() {
                dojo.hash('newequipment');
            }
        });
        this.subContainer.domNode.appendChild(this.newEquipment.domNode);
        this.newEquipment.startup();
        this.inherited(arguments);
    },
    getBrandName:function(colIndex, item) {
        if (item)
            return item.brand.name;
    },
    getImagePath:function(colIndex, item) {
        if (item) {
            return item.images[0];
        }
    },
    getCategoryName:function(colIndex, item) {
        if (item)
            return item.category.name;
    },
    formatDate:function(value) {
        return dojo.date.locale.format(new Date(value), {
            datePattern:'dd/MM/yyyy',
            formatLength:'short',
            selector:'date'
        });
    },
    formatImage:function(value) {
        if (value)
            return "<img width='48px' height='48px' src='" + (dojo.config.applicationBase + '/equipment/image/' + value.id) + "'/>";
        else
            return "<img width='48px' height='48px' src='/yuksekisler/resources/images/no-image.jpg'/>";
    },
    textBoxChanged:function(e) {
        if (this.timerId) {
            clearTimeout(this.timerId);
        }
        this.timerId = setTimeout(dojo.hitch(this, 'updateGrid'), 400);
    },
    onRowClick:function(e) {
        var item = this.grid.getItem(e.rowIndex);
        dojo.hash('equipment/' + item.id);
    },
    updateGrid:function() {
        if (this.searchBox.get('value') == '')
            this.grid.setQuery({});
        else
            this.grid.setQuery({'searchString':this.searchBox.get('value')});
    }
});
