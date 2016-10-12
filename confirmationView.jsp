<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
<link rel="stylesheet" type="text/css" href="login style.css" />
<title>Registration confirmed</title>
</head>

<body>
<h1>Welcome to SQL Demo</h1>
<br>
<br>
<c:set var="confResult" value="${requestScope.confResult}"/>

<c:choose>
<c:when test="${confResult=='OK'}">Congratulations! You are now registered.
<br><br>
<a href="login.jsp">Login page</a>
</c:when>
<c:otherwise> Registration error: <br>
<c:out value="${confResult}"></c:out>
<br><br>
<a href="register.jsp">Back to registration page</a>
</c:otherwise>
</c:choose>

</body>
</html>