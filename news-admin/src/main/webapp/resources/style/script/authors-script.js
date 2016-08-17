var authorName;
var authorSelectedId;

function showHiddenButtons (authorId){
    if(authorName!=null){
        hideButtons(authorSelectedId);
    }
    authorName = document.getElementById(authorId).value;
    authorSelectedId = authorId;
    $("#updateBut".concat(authorId)).css("display", "inline");
    $("#cancelBut".concat(authorId)).css("display", "inline");
    $("#expireBut".concat(authorId)).css("display", "inline");

    if(document.getElementById("exp".concat(authorId)).value){
        $("#expireBut".concat(authorId)).attr("disabled", true);
    }

    $("#editBut".concat(authorId)).css("display", "none");
    $("#".concat(authorId)).prop('readonly', false);
}

function hideButtons (authorId){
    $("#".concat(authorId)).val(authorName);
    $("#updateBut".concat(authorId)).css("display", "none");
    $("#cancelBut".concat(authorId)).css("display", "none");
    $("#expireBut".concat(authorId)).css("display", "none");
    $("#editBut".concat(authorId)).css("display", "inline");

    $("#".concat(authorId)).prop('readonly', true);
}

function expireAuthor (authorId){

    var now = new Date();
    var day = ("0" + now.getDate()).slice(-2);
    var month = ("0" + (now.getMonth() + 1)).slice(-2);
    var today = now.getFullYear()+"-"+(month)+"-"+(day)+" "+"00:00";
    document.getElementById("exp".concat(authorId)).value=today;
    $( "#form".concat(authorId) ).submit();
}

function updateAuthor (authorId) {
    $( "#form".concat(authorId) ).submit();
}


