<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<h1><spring:message code="error.access.denied"/></h1>

<c:choose>
    <c:when test="${empty username}">
        <h2><spring:message code="error.no.permission"/></h2>
    </c:when>
    <c:otherwise>
        <h2><spring:message code="label.username"/> ${username} <br/>
            <spring:message code="error.no.permission"/></h2>
    </c:otherwise>
</c:choose>
