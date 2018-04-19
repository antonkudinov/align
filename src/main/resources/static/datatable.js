$(document).ready( function () {
    var table = $('#productsTable').DataTable({
        "sAjaxSource": "/product/list",
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