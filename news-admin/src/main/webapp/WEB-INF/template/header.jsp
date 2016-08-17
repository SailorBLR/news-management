<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<div class="portal-name"><a href="<c:url value="/all"/>"><h3 id="title"><spring:message code="label.news-portal"/></h3>
</a></div>

<div class="languages">
    <c:set var="address" value="${springViewName}"/>
    <c:if test="${id!=null}">
        <c:set var="itemId" value="&id=${id}"/>
    </c:if>
    <a style="color: white"
       href="/${address}?lang=en_US${itemId}">
        <spring:message code="label.english"/> </a> |
    <a style="color: white" href="/${address}?lang=ru_RU${itemId}"> <spring:message code="label.russian"/> </a>
    <br/>
</div>
<div class="logout-field">
    <sec:authorize access="isAuthenticated()">
        <spring:message code="label.hello"/> <sec:authentication property='principal.username'/>
        <form action="/login">
            <input type="hidden" name="logout" value="logout">
            <input type="submit" value=" <spring:message code="label.logout"/>">
        </form>
    </sec:authorize>

</div>