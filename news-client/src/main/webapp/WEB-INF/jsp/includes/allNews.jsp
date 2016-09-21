<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<script src="<c:url value="/resources/style/script/fSelect.js" />"></script>
<script src="<c:url value="/resources/style/script/all-news-script.js" />"></script>
<link href="<c:url value="/resources/style/css/fSelect.css" />" rel="stylesheet">
<script>
    (function ($) {
        $(function () {
            $('.tags').fSelect();
        });
        $(function () {
            $('.authors').fSelect();
        });
    })(jQuery);

</script>
<script>
    var pages = ${nextPage};
    var total = ${pagesQuantity};
</script>


<div class="search-container">
    <form id="search-form" action="<c:url value="/controller"/>" method="get">
        <input type="hidden" name="command" value="allNews">
        <input type="hidden" id="tagList" name="tagsSet">
        <input type="hidden" name="nextPage" value="1">
        <input type="hidden" id="clear" name="clearMarker" value="false">
        <label> Search: </label>
        <br>
        <select class="tags" multiple="multiple" name="tagses" id="ts">
            <c:forEach items="${tags}" var="tags">
                <option name="tag" value="${tags.tagId}">${tags.tagName}</option>
            </c:forEach>
        </select>

        <select class="authors" name="authorsname" id="au">
            <option value="0">Select Author</option>
            <c:forEach items="${authors}" var="authors">
                <option value="${authors.authorId}">${authors.authorName}</option>
            </c:forEach>
        </select>
        <div class="search-els"><input type="submit" value="Search"/></div>
        <div class="refresh-button"><input type="button" value="Refresh" onclick="clearSearch()"></div>
    </form>
</div>


<c:forEach items="${newsList}" var="list">
    <div class="news-container">
        <div class="news-header">
            <div class="news-ref"><a href="<c:url value='/controller?command=newsMessage&id=${list.news.newsId}'/>">
                    ${list.news.title}</a></div>
            <c:set value="${list.news.newsCreationDate}" var="dateString" />
            <fmt:parseDate value="${dateString}" var="dateObject"
                           pattern="yyyy-MM-dd HH:mm" />

            <div class="news-date"><fmt:formatDate value="${dateObject}" pattern="MM/dd/yyyy"/></div>
        </div>
        <div class="news-short">
                ${list.news.shortText}
        </div>
        <div class="news-info">
            <div class="author">Author: ${list.author.authorName}</div>
            <div class="comments-quantity">Comments(${fn:length(list.comments)})</div>
            <div class="tagses">Tags: <c:forEach items="${list.tags}" var="tags">${tags.tagName} </c:forEach></div>
        </div>
    </div>
</c:forEach>
<div class="pagination" id="paging">

</div>
