<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<div class="full-news-container">
    <c:set value="${message}" var="message"/>
    <div class="date-cell">
        <span style="float: left;"><spring:message code="label.creation.date"/><fmt:formatDate value="${message.news.newsCreationDate}"
                                                                 pattern="MM/dd/yyyy HH:mm" /> </span>
        <span style="float: right;"><spring:message code="label.modification.date"/><fmt:formatDate value="${message.news.newsModificationDate}"
                                                                 pattern="MM/dd/yyyy HH:mm" /></span>
    </div>
    <div class="view-title">
        <span style="position: relative; width: auto">${message.news.title}</span>
    </div>
    <div class="view-full">
        <span style="position: relative; width: auto">${message.news.fullText}</span>
    </div>
    <div class="tags">
        <spring:message code="label.tags"/>
        <c:forEach items="${message.tags}" var="tags">
            ${tags.tagName}
        </c:forEach>
    </div>
</div>
<c:set var="searchCriteria" value="${sessionScope.get('searchCriteria')}"/>
<c:set var="previous" value="${searchCriteria.getPrevId()}" scope="page"/>
<c:set var="next" value="${searchCriteria.getNextId()}" scope="page"/>
<div class="next-prev-container">
    <c:if test="${previous!=null}">
        <div class="prev"><a href="<spring:url value="/newsMessage?id=${previous}"/>"><spring:message code="label.previous"/></a></div>
    </c:if>
    <c:if test="${next!=null}">
        <div class="next"><a href="<spring:url value="/newsMessage?id=${next}"/>"><spring:message code="label.next"/></a></div>
    </c:if>
</div>
<div class="comments-container">
    <div class="comment-field">
        <form action="<spring:url value="/postComment"/>" method="POST">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <input type="hidden" name="newsId" value="${message.news.newsId}" required>
            <label for="input-author"><spring:message code="label.name"/></label> <input id="input-author" name="author" type="text" min="3" max="20"/>
            <br>
            <label for="input-comment"><spring:message code="label.comment"/></label>
            <br>
            <textarea id="input-comment" name="comment"
                      cols="40" rows="3" maxlength="100" required></textarea>
            <input type="submit" name="submit-btn" value="<spring:message code="label.post.comment"/>" style="float: left">
        </form>
    </div>
    <div class="comments">
        <spring:message code="label.comments"/>

        <c:forEach items="${message.comments}" var="comments">
            <hr/>
            <div class="comment">
                <div class="comment-body">
                    <div class="info">
                        <p style="float: left">${comments.commentAuthor}</p>
                        <p style="position: relative; width: auto; float: right"><fmt:formatDate value="${comments.commentCreationDate}"
                                                pattern="MM/dd/yyyy HH:mm" /></p>
                    </div>
                    <br>
                    <span style="position: relative; width: auto">${comments.commentText}</span>
                </div>
                <div class="delete-block">
                    <a href="<spring:url value="/deleteComment?commentId=${comments.commentId}&newsId=${message.news.newsId}"/>">
                        <img src="<spring:url value="/resources/style/image/delete_ena.png"/>"></a>
                </div>
            </div>

            <hr/>
        </c:forEach>

    </div>
</div>