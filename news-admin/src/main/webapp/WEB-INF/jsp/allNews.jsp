<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<script src="<c:url value="/resources/style/script/all-news-script.js" />"></script>
<script src="<c:url value="/resources/style/source/jquery-3.1.0.js" />"></script>
<script src="<c:url value="/resources/style/script/fSelect.js" />"></script>
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
    <form action="<spring:url value="/search"/> " method="get">
        <input type="hidden" id="tagList" name="tagsSet">
        <input type="hidden" name="nextPage" value="1">
        <label> <spring:message code="label.search"/> </label>
        <br>

        <select class="tags" multiple="multiple" name="tagses" id="ts">
            <c:forEach items="${tags}" var="tags">
                <option name="tag" value="${tags.tagId}">${tags.tagName}</option>
            </c:forEach>
        </select>

        <select class="authors" name="authorsname" id="au">
            <option value="0"><spring:message code="label.select.author"/></option>
            <c:forEach items="${authors}" var="authors">
                <option value="${authors.authorId}">${authors.authorName}</option>
            </c:forEach>
        </select>

        <div class="search-els"><input type="submit" value="<spring:message code="label.search"/>"/></div>
    </form>
</div>


<c:forEach items="${listNews}" var="list">
    <div class="news-container">
        <div class="news-header">
            <div class="news-ref"><a href="<spring:url value="/newsMessage?id=${list.news.newsId}"/>">
                    ${list.news.title}</a></div>
            <div class="news-date"><fmt:formatDate value='${list.news.newsModificationDate}' pattern='MM/dd/yyyy HH:mm'/></div>
        </div>
        <div class="news-short">
                ${list.news.shortText}
        </div>
        <div class="news-info">
            <div class="author"><spring:message code="label.author"/> ${list.author.authorName}</div>
            <div class="comments-quantity"><spring:message code="label.comments-quantity"/> ${fn:length(list.comments)})</div>
            <div class="tagses"><spring:message code="label.tag"/> <c:forEach items="${list.tags}" var="tags">${tags.tagName} </c:forEach></div>
            <div class="news-edit"><a href="<spring:url value="/addNews?id=${list.news.newsId}"/>"><spring:message code="label.edit"/></a></div>
        </div>

    </div>
</c:forEach>
<div class="pagination" id="paging">

</div>
