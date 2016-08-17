
function acceptTags (){
    alert("HELLO");
    var options = [];

    $(".fs-option g0 selected[data-value]").each(function()
    {
        options.push($(this).val());
        // Add  to your list
    });

    alert(options);
}