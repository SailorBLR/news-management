<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<head>
    <script src="<c:url value="/resources/style/script/menu-script.js" />"></script>
</head>
<script>
    var page = "${springViewName}";
</script>


<ul>
    <li id="all"><a href="<spring:url value="/all"/>"><spring:message code="href.allnews"/></a></li>
    <li id="addNews"><a href="<spring:url value="/addNews?id=0"/>"><spring:message code="href.addnews"/></a></li>
    <li id="allAuthors"><a href="<spring:url value="/allAuthors"/>"><spring:message code="href.author"/></a></li>
    <li id="allTags"><a href="<spring:url value="/allTags"/>"><spring:message code="href.tags"/></a></li>
</ul>

