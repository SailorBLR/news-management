<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<html>
<head>

</head>
<body>
<div><tiles:insertAttribute name="error"/></div>
<div class="portal-name">
    <a href="<c:url value="/all"/>">
        <h3 id="title"><spring:message code="label.news-portal"/></h3></a>
</div>
<div>${message}</div>
</body>
</html>