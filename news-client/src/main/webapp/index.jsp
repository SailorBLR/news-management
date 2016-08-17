<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<c:set var="locale" value="en_US" scope="session"/>
<c:set var="role" value="user" scope="session"/>
<c:set var="login" value="Guest" scope="session"/>



<head><title>Index</title></head>
<body>


<jsp:forward page="/controller?command=allNews&nextPage=1"/>

</body>
</html>