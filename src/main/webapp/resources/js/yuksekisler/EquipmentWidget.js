/**
 * Created by JetBrains WebStorm.
 * User: capacman
 * Date: 10/23/11
 * Time: 7:52 PM
 * To change this template use File | Settings | File Templates.
 */
dojo.provide('yuksekisler.EquipmentWidget');

dojo.declare('yuksekisler.EquipmentWidget', [dijit._Widget], {
    equipment:null,
    buildRendering: function() {
        // create the DOM for this widget
        this.domNode = dojo.create("div", {
            "class":'simpleWidgetContainer equipmentWidget'
        });
        this.equipmentImage = dojo.create('img', {"class":'simpleWidgetImage',align:'right'}, this.domNode);
        this.equipmentProductName = dojo.create('div', {"class":'simpleWidgetDefinition'}, this.domNode);
        this.equipmentProductCode = dojo.create('div', {"class":'simpleWidgetDefinition'}, this.domNode);

        this.inherited(arguments);
    },
    postCreate:function() {

        dojo.attr(this.equipmentProductName, {
            innerHTML:'<b>' + this.equipment.productName + '</b>'
        });

        dojo.attr(this.equipmentProductCode, {
            innerHTML:'ProductCode ' + this.equipment.productCode
        });

        dojo.attr(this.equipmentImage, {
            src:this.equipment.images.length > 0 ? dojo.config.applicationBase + '/file/image/' + this.equipment.images[0].id + '/thumbnail' : dojo.config.applicationBase + '/resources/images/no-image.jpg'
        });

        this.inherited(arguments);
    }
});
