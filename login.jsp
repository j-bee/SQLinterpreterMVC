<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
<title>Login page</title>
<link rel="stylesheet" type="text/css" href="login style.css" />
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
<script>
$(document).ready(function(){
$("input").focus(function() {
	$(this).css("border-color", "green");
	$(this).css("border-style", "solid");
});
$("input").blur(function(){
	$(this).css("border-color", "");
	$(this).css("border-style", "");
	});
});	

</script>


<script type="text/javascript">
function validateUsername() {
var usr = document.forms["loginForm"]["username"].value;
if(usr.length<1) {
return false;}

return true;
}

function validatePassword() {
var pswd = document.forms["loginForm"]["password"].value;
if(pswd.length<6) {
return false;}

return true;
}

function validateData() {
if (validatePassword() && validateUsername()) {
//alert("Data format correct!");
return true;}

else {
alert("Invalid data, password or username too short.");
return false;}
}
</script>
</head>

<c:if test="${sessionScope.username!=null}">
<c:redirect url="jdbcMVC.jsp"/>
</c:if>

<body>
<h1>Welcome to SQL Demo</h1>
Login below:
<br>
<br>

<form name="loginForm" onsubmit="return validateData()"
method="post" action="login.do">

<table>

<tr>
<td>Username: </td>
<td><input type="text" name="username"></td>
</tr>
<tr>
<td>Password: </td>
<td><input type="password" name="password"></td>
</tr>
<tr>
<td></td>
<td><input type="submit" name="Register"></input>
<input type="reset" name="Reset"></input></td>
</tr>

</table>
</form>

Not a user? <a href="register.jsp">Sign up here.</a>

</body>
</html>