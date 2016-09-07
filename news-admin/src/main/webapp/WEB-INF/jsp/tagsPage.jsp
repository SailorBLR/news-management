<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>

<head>
    <spring:url value="/resources/style/script/tags-script.js" var="mainJs"/>
    <script src="${mainJs}"></script>
</head>
<sf:form modelAttribute="tag">
    <sf:errors path="tagName" cssClass="error"/>
</sf:form>

<div class="authors-container">
    <h1><spring:message code="label.tags"/></h1>
    <c:forEach items="${tags}" var="tagInfo">
        <div class="author-field">
            <div style="display: block; ">
                <div style="display: inline; width: 50%; float: left">
                    <sf:form id="form${tagInfo.tagId}" action="/updateTag" modelAttribute="tag" method="POST">
                        <sf:hidden path="tagId" name="tagId" value="${tagInfo.tagId}"/>
                        <p><spring:message code="label.tag"/> <sf:input path="tagName" id="${tagInfo.tagId}" type="text" name="tagName"
                                          value="${tagInfo.tagName}" readonly="true"/></p>
                    </sf:form>
                </div>
                <div style="display: inline; width: 50%; float: right">
                    <button id="${"editBut".concat(tagInfo.tagId)}"
                            class="edit-button" onclick="showHiddenButtons(${tagInfo.tagId})"><spring:message code="label.edit"/>
                    </button>
                    <button id="${"updateBut".concat(tagInfo.tagId)}"
                            class="edit-button" style="display: none"
                            onclick="updateTag(${tagInfo.tagId})"><spring:message code="label.update"/>
                    </button>
                    <button id="${"deleteBut".concat(tagInfo.tagId)}"
                            class="delete-button" style="display: none"
                            onclick="deleteTag(${tagInfo.tagId})"><spring:message code="label.delete"/>
                    </button>
                    <button id="${"cancelBut".concat(tagInfo.tagId)}"
                            class="edit-button" style="display: none"
                            onclick="hideButtons(${tagInfo.tagId})"><spring:message code="label.cancel"/>
                    </button>
                </div>
            </div>
        </div>
    </c:forEach>

</div>
<div>
    <sf:form modelAttribute="tag" action="/addTag" method="POST">
        <p><spring:message code="label.add.tag"/> <sf:input path="tagName"/>
            <sf:button type="submit" value="Create">
                <spring:message code="label.add"/>
        </sf:button></p>
    </sf:form>
</div>