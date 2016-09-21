$(document).ready(function () {
    var firstPage = "<a href='/search?nextPage=1'>1</a>";
    var lastPage = "<a href='/search?nextPage=";
    var prevPage = "<a href='/search?nextPage=";
    var curPage = "<a style='font-size: large' href='/search?nextPage=";
    if (pages != 1) {
        $("#paging")
            .append(firstPage).append("...");
        $("#paging")
            .append(prevPage.concat(pages - 1).concat("'><<</a>").concat(" "));
    }

    $("#paging")
        .append(curPage.concat(pages).concat("'>").concat(pages).concat("</a>").concat(" "));


    var pagesStart = pages + 1;
    var pagesEnd = pages + 5;
    if ((total-pagesStart)<5){
        pagesEnd = total;
    }

    for (i = pagesStart; i <= pagesEnd; i++) {
        var hre = "<a href='";
        var val = "/search?nextPage=";
        var endHre = "'>";
        var reference = hre.concat(val);
        reference = reference.concat(i).concat(endHre).concat(i);
        reference = reference.concat("</a>");
        $("#paging")
            .append(reference);
        $("#paging")
            .append(" ");
    }
    if (pages != total) {
        $("#paging")
            .append(prevPage.concat(pages + 1).concat("'>>></a>").concat(" "));
        $("#paging")
            .append("...").append(lastPage.concat(total).concat("'>").concat(total).concat("</a>"));

    }
});


