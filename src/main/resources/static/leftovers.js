$(document).ready( function () {
    var table = $('#leftoversTable').DataTable({
        "sAjaxSource": "/api/product/list/leftovers",
        "sAjaxDataProp": "",
        "order": [[ 0, "asc" ]],
        "aoColumns": [
            { "mData": "id"},
            { "mData": "name" },
            { "mData": "brand" },
            { "mData": "price" },
            { "mData": "quantity" },

       ]
    })
});