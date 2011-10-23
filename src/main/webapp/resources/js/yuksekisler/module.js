/**
 * Created by JetBrains WebStorm.
 * User: capacman
 * Date: 9/9/11
 * Time: 7:36 PM
 * To change this template use File | Settings | File Templates.
 */
dojo.provide("yuksekisler.module");

// dependencies for the application UI and data layers
dojo.require("dojo.hash");
dojo.require("dojo.DeferredList");
dojo.require("dojo.store.JsonRest");
dojo.require("dojo.store.Memory");
dojo.require("dojo.data.ObjectStore");
dojo.require("dojo.date.locale");
dojo.require("dojo.fx");
dojo.require('dojo.dnd.Source');
//dojo.require("dojox.lang.aspect");
dojo.require("dijit.Dialog");
dojo.require("dijit.DialogUnderlay");
dojo.require("dijit.form.Form");
dojo.require("dijit.form.ValidationTextBox");
dojo.require("dijit.form.Select");
dojo.require('dijit.form.DateTextBox');
dojo.require("dijit.form.Button");
dojo.require("dijit.form.SimpleTextarea");
dojo.require("dijit.layout.BorderContainer");
dojo.require("dijit.layout.ContentPane");
dojo.require("dijit.MenuBar");
dojo.require("dijit.MenuBarItem");
dojo.require("dijit.Menu");
dojo.require("dijit.MenuItem");
dojo.require("dijit.PopupMenuBarItem");
dojo.require('dijit.TitlePane');
dojo.require("dijit.Editor");
// Require a few extra plugins
dojo.require("dijit._editor.plugins.TextColor");
dojo.require("dijit._editor.plugins.LinkDialog");
dojo.require("dijit._editor.plugins.FullScreen");


dojo.require("dojox.grid.DataGrid");
dojo.require("dojox.layout.TableContainer");
dojo.require('dojox.form.Uploader');
dojo.require("dojox.form.uploader.plugins.HTML5");
dojo.require("dojox.form.uploader.FileList");
dojo.require('dojox.image.ThumbnailPicker');
dojo.require('dojox.image.Lightbox');
dojo.require('dojox.widget.TitleGroup');
dojo.require('dojox.uuid.generateRandomUuid');
dojo.require('dojox.form.CheckedMultiSelect');

dojo.require('yuksekisler._ProperDestroyMixin');
dojo.require("yuksekisler.Login");
dojo.require("yuksekisler.Toolbar");
dojo.require("yuksekisler.EquipmentListView");
dojo.require("yuksekisler.EquipmentFormView");
dojo.require("yuksekisler.EquipmentView");
dojo.require('yuksekisler.InspectionReportWidget');
dojo.require('yuksekisler.InspectionReportFormWidget');
dojo.require('yuksekisler.Definitions');
dojo.require('yuksekisler.EmployeeWidget');
dojo.require('yuksekisler.EmployeeListWidget');
dojo.require('yuksekisler.UploaderSuccessWidget');
dojo.require('yuksekisler.WorkDefinitionView');
dojo.require('yuksekisler.WorkListView');
dojo.require('yuksekisler.EquipmentWidget');

