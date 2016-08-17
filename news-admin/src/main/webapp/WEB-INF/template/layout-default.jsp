<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <spring:url value="/resources/style/css/style.css" var="styleCss"/>
    <link href="${styleCss}" rel="stylesheet"/>
    <title><tiles:insertAttribute name="title"/></title>
</head>

<html>
<body>
<div id="container">
    <div class="header">
        <header id="header">
            <tiles:insertAttribute name="header"/>
        </header>
    </div>
    <div class="page-wrap">

        <div><tiles:insertAttribute name="body"/></div>

    </div>
    <footer class="site-footer">
        <tiles:insertAttribute name="footer"/>
    </footer>
</div>
</body>
</html>