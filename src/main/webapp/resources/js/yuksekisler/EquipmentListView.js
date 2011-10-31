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

dojo.declare('yuksekisler.EquipmentListView', [dijit._Widget,dijit._Templated,yuksekisler._ProperDestroyMixin], {
    templateString:dojo["cache"]('yuksekisler.templates', 'equipment_list_view_template.html', {sanitize: true}),
    widgetsInTemplate:true,
    dataStore:null,
    gutters:false,
    postCreate:function() {
        this.grid = new dojox.grid.DataGrid({
            query:{},
            selectionMode:'single',
            id:'equipmentListGrid',
            rowCount:10,
            store:dojo.data.ObjectStore({objectStore: this.dataStore}),
            structure:
                [
                    {name:'Product Name',field:'productName',width:'auto'},
                    {name:'Product Code',field:'productCode',width:'auto'},
                    {name:'Image',get:this.getImagePath,width:'48px',height:'auto',formatter:this.formatImage},
                    {name:'Brand',get:this.getBrandName,width:'auto'},
                    {name:'Category',get:this.getCategoryName,width:'auto'},
                    {name:'Inspection Date',get:this.stockEntrance,formatter:this.formatDate,width:'auto'}
                ],
            onRowContextMenu:dojo.hitch(this, yuksekisler.app.onRowContextMenu)
        });
        this.gridContainer.set('content', this.grid);
        this.grid.startup();
        dojo.connect(this.searchBox, 'onKeyUp', this, this.textBoxChanged);
        dojo.connect(this.grid, 'onRowClick', this, this.onRowClick);
        this.newEquipment = new dijit.form.Button({
            label:'New Equipment',
            onClick:function() {
                dojo.hash('newequipment');
            }
        }).placeAt(this.subContainer);
        this.addInner(this.newEquipment);
        this.newEquipment.startup();
        this.inherited(arguments);
        dojo.addClass(this.domNode, 'yuksekisler-widget');
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
    stockEntrance:function(colIndex, item) {
        if (item && item.inspectionReports.length > 0)
            return item.inspectionReports[item.inspectionReports.length - 1].inspectionDate;
    },
    formatDate:function(value) {
        if (value)
            return dojo.date.locale.format(new Date(value), {
                datePattern:'dd/MM/yyyy',
                formatLength:'short',
                selector:'date'
            });
        return "";
    },
    formatImage:function(value) {
        if (value)
            return "<img width='48px' height='48px' src='" + (dojo.config.applicationBase + '/file/image/' + value.id) + "/thumbnail'/>";
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
