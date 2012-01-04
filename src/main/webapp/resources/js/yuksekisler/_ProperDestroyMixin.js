/**
 * Created by JetBrains WebStorm.
 * User: ci-lab
 * Date: 10/3/11
 * Time: 11:15 AM
 * To change this template use File | Settings | File Templates.
 */

define(["dojo/_base/declare", "dojo"], function (declare, dojo) {
    return declare(null, {
        addInner:function (widget) {
            if (!this._innerWidgets)
                this._innerWidgets = [];
            this._innerWidgets.push(widget);
        },
        destroy:function () {
            dojo.forEach(this._innerWidgets, function (widget) {
                if (widget && widget.destroyRecursive) {
                    widget.destroyRecursive();
                } else if (widget && widget.destroy) {
                    widget.destroy()
                }
            });
            this.inherited(arguments);
        }
    });
});
