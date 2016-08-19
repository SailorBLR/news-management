<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>

<head>
    <spring:url value="/resources/style/script/authors-script.js" var="mainJs"/>
    <script src="${mainJs}"></script>
</head>

<div class="authors-container">
    <sf:form modelAttribute="author">
        <sf:errors path="authorName" cssClass="error"/>
    </sf:form>
    <h1><spring:message code="label.authors"/></h1>

    <c:forEach items="${authors}" var="authors">
        <div class="author-field">
            <div style="display: block; ">
                <div style="display: inline; width: 50%; float: left">
                    <sf:form id="form${authors.authorId}" modelAttribute="author" action="/updateAuthor" method="POST">
                        <sf:hidden  name="authorId" path="authorId" value="${authors.authorId}"/>
                        <sf:hidden name="expired" id="exp${authors.authorId}" path="expired" value="${authors.expired}" />

                        <p><spring:message code="label.author"/> <sf:input id="${authors.authorId}" name="authorName"
                                             path="authorName" readonly="true" value="${authors.authorName}"/></p>
                    </sf:form>
                </div>
                <div style="display: inline; width: 50%; float: right">
                    <button id="${"editBut".concat(authors.authorId)}"
                            class="edit-button" onclick="showHiddenButtons(${authors.authorId})"><spring:message code="label.edit"/>
                    </button>
                    <button id="${"updateBut".concat(authors.authorId)}"
                            class="edit-button" style="display: none"
                            onclick="updateAuthor(${authors.authorId})"><spring:message code="label.update"/>
                    </button>
                    <button id="${"expireBut".concat(authors.authorId)}"
                            class="edit-button" style="display: none"
                            onclick="expireAuthor(${authors.authorId})"><spring:message code="label.expire"/>
                    </button>
                    <button id="${"cancelBut".concat(authors.authorId)}"
                            class="edit-button" style="display: none"
                            onclick="hideButtons(${authors.authorId})"><spring:message code="label.cancel"/>
                    </button>
                </div>
            </div>
        </div>
    </c:forEach>

</div>
<div>
    <sf:form id="addform" action="/addAuthor" modelAttribute="author" method="POST">

        <p><spring:message code="label.add.author"/> <sf:input name="authorName" path="authorName"/>
            <button onclick="addAuthor()"><spring:message code="label.create"/></button></p>
    </sf:form>


</div>