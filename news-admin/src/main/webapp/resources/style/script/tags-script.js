var authorName;

function showHiddenButtons (tagId){

    authorName = document.getElementById(tagId).value;
    $("#updateBut".concat(tagId)).css("display", "inline");
    $("#cancelBut".concat(tagId)).css("display", "inline");
    $("#deleteBut".concat(tagId)).css("display", "inline");

    $("#editBut".concat(tagId)).css("display", "none");
    $("#".concat(tagId)).prop('readonly', false);
}

function hideButtons (tagId){
    $("#".concat(tagId)).val(authorName);
    $("#updateBut".concat(tagId)).css("display", "none");
    $("#cancelBut".concat(tagId)).css("display", "none");
    $("#deleteBut".concat(tagId)).css("display", "none");
    $("#editBut".concat(tagId)).css("display", "inline");

    $("#".concat(tagId)).prop('readonly', true);
}

function deleteTag (tagId){
    /*$("#form".concat(tagId)).attr('action', '/deleteTag');
    $( "#form".concat(tagId) ).submit();*/
    var button = location.href = '/deleteTag?tagId='.concat(tagId);
    return button;
}

function updateTag (tagId) {
    $( "#form".concat(tagId) ).submit();
}


