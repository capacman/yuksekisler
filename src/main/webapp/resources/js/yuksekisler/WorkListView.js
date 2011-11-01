/**
 * Created by JetBrains WebStorm.
 * User: capacman
 * Date: 10/23/11
 * Time: 5:07 PM
 * To change this template use File | Settings | File Templates.
 */
dojo.provide('yuksekisler.WorkListView');

dojo.declare('yuksekisler.WorkListView', [dijit._Widget,dijit._Templated,yuksekisler._ProperDestroyMixin], {
    templateString:dojo["cache"]('yuksekisler.templates', 'worklist_view_template.html', {sanitize: true}),
    widgetsInTemplate:true,
    workStore:null,
    postCreate:function() {
        this.grid = new dojox.grid.DataGrid({
            query:{},
            selectionMode:'single',
            id:'workListGrid',
            rowCount:10,
            store:new dojo.data.ObjectStore({objectStore: this.workStore,labelProperty:'name'}),
            structure:
                [
                    {name:'Name',field:'name',width:'auto'},
                    {name:'Customer',field:'customer',width:'auto'},
                    {name:'Start Date',field:'startDate',formatter:this.formatDate,width:'auto'},
                    {name:'End Date',field:'endDate',formatter:this.formatDate,width:'auto'}
                ],
            onRowContextMenu:dojo.hitch(this, yuksekisler.app.onRowContextMenu)
        });
        this.gridContainer.set('content', this.grid);
        this.grid.startup();
        dojo.connect(this.searchBox, 'onKeyUp', this, this.textBoxChanged);
        dojo.connect(this.grid, 'onRowClick', this, this.onRowClick);
        this.newWork = new dijit.form.Button({
            label:'New Work',
            onClick:function() {
                dojo.hash('newwork');
            }
        }).placeAt(this.subContainer);
        this.addInner(this.newWork);
        this.newWork.startup();
        this.inherited(arguments);
        dojo.addClass(this.domNode, 'yuksekisler-widget');
        this.inherited(arguments);
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
    textBoxChanged:function(e) {
        if (this.timerId) {
            clearTimeout(this.timerId);
        }
        this.timerId = setTimeout(dojo.hitch(this, 'updateGrid'), 400);
    },
    onRowClick:function(e) {
        var item = this.grid.getItem(e.rowIndex);
        dojo.hash('work/' + item.id);
    },
    updateGrid:function() {
        if (this.searchBox.get('value') == '')
            this.grid.setQuery({});
        else
            this.grid.setQuery({'searchString':this.searchBox.get('value')});
    }
});