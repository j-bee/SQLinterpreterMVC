<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
<title>Registration result</title>
</head>

<body>
<link rel="stylesheet" type="text/css" href="login style.css" />
<h1>Welcome to SQL Demo</h1>
<br>
<br>

<c:set var="registerStatus" value="${requestScope.registerStatus}"/>

<c:choose>
<c:when test="${registerStatus=='OK'}">Registration successful!
<br><br>
<a href="login.jsp">Login page</a>
</c:when>
<c:otherwise> Registration error: <br>
<c:out value="${registerStatus}"></c:out>
<br><br>
<a href="register.jsp">Back to registration page</a>
</c:otherwise>
</c:choose>

</body>
</html>