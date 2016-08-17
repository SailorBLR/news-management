$( document ).ready(function() {

        var now = new Date();
        var day = ("0" + now.getDate()).slice(-2);
        var month = ("0" + (now.getMonth() + 1)).slice(-2);
        var today =(month)+"/"+(day)+"/"+ now.getFullYear();
        document.getElementById("modificationDate").value=today;
});

function submitNewsForm(){

    var now = new Date();
    var day = ("0" + now.getDate()).slice(-2);
    var month = ("0" + (now.getMonth() + 1)).slice(-2);
    var today = now.getFullYear()+"-"+(month)+"-"+(day)+" "+now.getHours()+":"+now.getMinutes();
    document.getElementById("modificationDate").value=today;
    $("#form").submit();
}

function deleteNewsMessage (newsId){
    var button = location.href = '/deleteMessage?id='.concat(newsId);
    return button;
}

