<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
<link rel="stylesheet" type="text/css" href="login style.css" />
<title>Login result</title>
</head>

<body>
<h1>Welcome to SQL Demo</h1>
<br>
<br>

<c:set var="loginStatus" value="${requestScope.loginStatus}"/>

Login result: <br>
<c:out value="${loginStatus}"></c:out>

<br>
<br>
<a href="login.jsp">Back to login page</a>

</body>
</html>