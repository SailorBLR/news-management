<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>

<head>
    <script src="<c:url value="/resources/style/source/jquery-3.1.0.js" />"></script>
    <script src="<c:url value="/resources/style/script/fSelect.js" />"></script>
    <script src="<c:url value="/resources/style/script/news-edit-script.js" />"></script>
    <link href="<c:url value="/resources/style/css/fSelect.css" />" rel="stylesheet">
</head>
<script>
    (function ($) {
        $(function () {
            $('.demo').fSelect();
        });
    })(jQuery);
</script>
<div class="news-edit-container">

    <sf:form method="POST" action="/addNewMessage" modelAttribute="newsDto" cssClass="news-form" id="form">
        <sf:hidden  path="news.newsId"/>
        <div class="news-info-block">
            <p><spring:message code="label.title"/></p>

            <p>
                <sf:errors path="news.title" cssClass="error"/>
            </p>
            <sf:textarea maxlength="30" path="news.title" required="true" cssClass="title-input"></sf:textarea>
        </div>

        <div class="news-info-block">
            <p><spring:message code="label.date"/></p>
            <sf:input type="datetime" id="modificationDate" path="news.newsCreationDate" required="true" readonly="true"/>
        </div>

        <div class="news-info-block">
            <p><spring:message code="label.short"/></p>

            <p>
                <sf:errors path="news.shortText" cssClass="error"/>
            </p>
            <sf:textarea maxlength="100" path="news.shortText" required="true" cssClass="title-input"></sf:textarea>
        </div>
        <div class="news-info-block">
            <p><spring:message code="label.full"/></p>

            <p>
                <sf:errors path="news.fullText" cssClass="error"/>
            </p>
            <sf:textarea maxlength="2000" path="news.fullText" required="true" cssClass="full-input"></sf:textarea>
        </div>

        <div class="news-info-block">

            <sf:errors path="author.authorId" cssClass="error"/>
            <span><spring:message code="label.author"/></span>
            <sf:select class="demo" path="author.authorId" required="true">
                <sf:option value="0"><spring:message code="label.select.author"/></sf:option>
                <c:forEach items="${authorList}" var="authors">
                    <sf:option value="${authors.authorId}">${authors.authorName}</sf:option>
                </c:forEach>
            </sf:select>

            <span> <spring:message code="label.tags"/></span> <select multiple class="demo" name="tags">
            <c:forEach items="${tagList}" var="tag">
            <c:choose>
            <c:when test="${newsDto.tags.contains(tag)}">
            <option selected value="${tag.tagId}">${tag.tagName}
                </c:when>
                <c:otherwise>
            <option value="${tag.tagId}">${tag.tagName}
                </c:otherwise>
                </c:choose>
                </c:forEach>
        </select>

            <p>
                <sf:errors path="tags" cssClass="error"/>
            </p>

        </div>

    </sf:form>

    <div class="news-info-block">
        <button type="button" class="sbm-btn" onclick="submitNewsForm()">
            <span><spring:message code="label.post"/></span>
        </button>
        <c:if test="${newsDto.news.newsId!=null}">
            <button type="button" class="del-btn" onclick="deleteNewsMessage(${newsDto.news.newsId})">
                <span><spring:message code="label.delete"/></span>
            </button>
        </c:if>
    </div>


</div>