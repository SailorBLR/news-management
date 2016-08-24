<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<div class="full-news-container">
    <c:set value="${message}" var="message"/>
    <div class="date-cell">
        <c:set value="${message.news.newsCreationDate}" var="dateString"/>
        <fmt:parseDate value="${dateString}" var="dateObject"
                       pattern="yyyy-MM-dd HH:mm"/>
        <span style="float: left;">Creation date:<fmt:formatDate value="${dateObject}"
                                                                 pattern="MM/dd/yyyy HH:mm"/></span>
        <span style="float: right;">Modification date: ${message.news.newsModificationDate}</span>
    </div>
    <div class="view-title">
        <span style="position: relative; width: auto">${message.news.title}</span>
    </div>
    <div class="view-full">
        <span style="position: relative; width: auto">${message.news.fullText}</span>
    </div>
    <div class="tags">
        Tags:
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
        <div class="prev"><a href="<c:url value="/controller?command=newsMessage&id=${previous}"/>">Previous</a></div>
    </c:if>
    <c:if test="${next!=null}">
        <div class="next"><a href="<c:url value="/controller?command=newsMessage&id=${next}"/>">Next</a></div>
    </c:if>
</div>
<div class="comments-container">
    <div class="comment-field">
        <form action="<c:url value="/controller"/>" method="GET">
            <input type="hidden" name="command" value="postComment">
            <input type="hidden" name="newsId" value="${message.news.newsId}" required>
            <label for="input-author">Name</label> <input id="input-author" name="author" type="text" min="3" max="20"/>
            <br>
            <label for="input-comment">Comment:</label>
            <br>
            <textarea id="input-comment" name="comment"
                      cols="40" rows="3" maxlength="100" required></textarea>
            <input type="submit" name="submit-btn" value="Send" style="float: left">
        </form>
    </div>
    <div class="comments">
        Comments:

        <c:forEach items="${message.comments}" var="comments">
            <hr/>
            <div class="comment">
                <div class="info">
                    <h3 style="float: left">${comments.commentAuthor}</h3>

                    <h3 style="position: relative; width: auto; float: right">${comments.commentCreationDate}</h3>
                </div>
                <br>
                <span style="position: relative; width: auto">${comments.commentText}</span>
            </div>
            <hr/>
        </c:forEach>

    </div>
</div>