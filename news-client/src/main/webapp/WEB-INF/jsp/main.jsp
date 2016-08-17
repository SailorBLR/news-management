<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" type="text/css" href="<c:url value="/resources/style/css/reset.css" />">
    <link href="<c:url value="/resources/style/css/style.css" />" rel="stylesheet">
    <script src="<c:url value="/resources/style/source/jquery-3.1.0.js" />"></script>
    <title>${title}</title>
</head>

<html>
<body>
<div id="container">
    <div class="header">
    <header id="header">
        <c:import url="includes/header.jsp"/>
    </header>
    </div>
    <div class="page-wrap">
        <c:import url="${include}"/>
    </div>
    <footer class="site-footer">
        <c:import url="includes/footer.jsp"/>
    </footer>
</div>
</body>
</html>
