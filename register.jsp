<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
<title>Login page</title>
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
var usr = document.forms["registerForm"]["username"].value;
if(usr.length<1) {
return false;}

return true;
}

function validateEmail() {
var mail = document.forms["registerForm"]["email"].value;
var atIndex = mail.indexOf("@");
var dotIndex = mail.lastIndexOf(".");
if(atIndex < 1 || dotIndex < atIndex + 2 || atIndex < 0 || dotIndex < 0) {
return false;}

return true;
}

function validatePassword() {
var pswd = document.forms["registerForm"]["password"].value;
if(pswd.length<6) {
return false;}

var smallLet = /[a-z]/;
var capitalLet = /[A-Z]/;
var num = /[0-9]/;
var specialChars = /[&\-._!\"'#$\*\+,\/:;<=>\?@\[\]\\`\^{}|~()]/;

if(smallLet.test(pswd) && capitalLet.test(pswd) && num.test(pswd)
&& specialChars.test(pswd)) {
return true;
}

else {
return false;
}

}

function validateData() {
if (validatePassword() && validateEmail() && validateUsername()) {
alert("Data correct.");
return true;}

else {
alert("Invalid data.");
return false;}
}
</script>
</head>

<c:if test="${sessionScope.username!=null}">
<c:redirect url="jdbcMVC.jsp"/>
</c:if>

<body>
<link rel="stylesheet" type="text/css" href="login style.css" />
<h1>Welcome to SQL Demo</h1>
Register below:
<br>
<br>

<form name="registerForm" onsubmit="return validateData()"
method="post" action="reg.do">

<table>
<tr>
<td>Email: </td>
<td><input type="text" name="email"></td>
</tr>
<tr>
<td id="userCaption">Username: </td>
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

Note: your password must be at least 6 characters long and must have one small letter, one capital letter, one digit, and one special character.

<br>
<br>
Already a user? <a href="login.jsp">Log in here.</a>

</body>
</html>