$(function(){
    $("#logoutButton").on("click", function(e) {
        window.location.href = "/private/home/logout";
    })

    $("#fileDownloadButton").on("click", function(e) {
        location.href = "/private/report/download";
    });

    $("#fileUploadButton").on("click", function (e) {
        location.href = "/private/settings/report/fileUpload/form";
    });

});