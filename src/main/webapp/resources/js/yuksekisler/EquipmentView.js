/**
 * Created by JetBrains WebStorm.
 * User: capacman
 * Date: 9/21/11
 * Time: 4:40 PM
 * To change this template use File | Settings | File Templates.
 */
dojo.provide('yuksekisler.EquipmentView');

dojo.require('dijit.layout.BorderContainer');
dojo.require('dijit._Templated');

dojo.declare('yuksekisler.EquipmentView', [dijit.layout.BorderContainer,dijit._Templated], {
    templateString:dojo.cache('yuksekisler.EquipmentView', dojo.moduleUrl('yuksekisler', '../../templates/equipment_view_template.html')),
    widgetsInTemplate:true,
    categoryStore:null,
    brandStore:null,
    equipmentStore:null,
    equipment:null,
    gutters:false,
    design:'headline',
    postCreate:function() {
        this.inherited(arguments);

        var equipmentChain = this.equipment.then(dojo.hitch(this, this.prepareImageContent));

        this.editEquipment = new yuksekisler.EquipmentFormView({
            categoryStore:this.categoryStore,
            brandStore:this.brandStore,
            equipmentStore:this.equipmentStore,
            equipment:equipmentChain
        });
        this.editEquipment.startup();
        var container = dojo.create('div', {class:'equipmentFormContainer gradient'});
        container.appendChild(this.editEquipment.domNode);
        this.editEquipmentContent.set('content', container);


    },
    prepareImageContent:function(value) {
        if (value.images.length > 0) {
            /*var imageGallery = new dojox.image.Gallery({
             imageWidth:'500',
             imageHeight:'300'
             });*/
            var imageGallery = new dojox.image.ThumbnailPicker({
                id:'imagePicker',
                size:530,
                isClickable:true,
                isScrollable:false
            });
            this.lightbox = new dojox.image.LightboxDialog().startup();
            imageGallery.startup();
            this.imageContent.set('content', imageGallery);
            dojo.subscribe(imageGallery.getClickTopicName(), this, this.lightboxShow);

            var storeData = [];
            for (var x in value.images) {
                storeData[x] = {
                    "thumb":(dojo.config.applicationBase + '/equipment/image/' + value.images[x].id) + '/thumbnail',
                    "large":(dojo.config.applicationBase + '/equipment/image/' + value.images[x].id),
                    "title":value.images[x].title,
                    "link":value.images[x].id
                };
            }
            var memoryStore = dojo.data.ObjectStore({objectStore: new dojo.store.Memory({data: storeData})});
            imageGallery.setDataStore(memoryStore, { count:20 }, {
                imageThumbAttr: "thumb",
                imageLargeAttr: "large"
            });
        } else {
            var img = dojo.create('img', {class:'no-image',src:'/yuksekisler/resources/images/no-image.jpg'});
            this.imageContent.set('content', img);
        }
        return value;
    },
    lightboxShow:function (packet) {

        // you can just "show" this image
        this.lightbox.show({
            href: packet.largeUrl,
            title: packet.title
        });

    }
});