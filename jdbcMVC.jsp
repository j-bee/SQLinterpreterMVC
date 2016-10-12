<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
<link rel="stylesheet" type="text/css" href="style.css" />
<title>SQL Demo</title>
<script src="http://code.jquery.com/jquery-latest.min.js"></script>
<script>
$(document).ready(function(){
	$(document).on("click", "#button1", function(){
		$("#div1").toggle(getDateFromServlet());
	});
});

function getDateFromServlet() {

var button = document.getElementById("button1");

if($("#div1").is(":hidden")) {
$.get("dateservlet", function(responseText) {
		$("#div1").text(responseText);
		button.firstChild.data = "Hide date";	
	});
  }
else button.firstChild.data = "Show date";  
  
}
<c:set var="username" value="${sessionScope.username}"/>	
</script>
</head>
<body>


<h1 align="center">SQL Demo</h1>
<div id="displayUserName" style="float: left;">Hello, </div>
<c:out value="${username}"></c:out>

<!--
<div id="div1" style="float: right;"></div>
<%@ include file="logoutButton.html" %>
-->



<button id="button1" style="float: right;">Show date</button>
<div id="div1" style="float: right; display: none;"></div>

<form method="POST"
action="sqlmvc">
<br><br>
Enter an SQL statement:<p>
<input type="text" name="sqlquery" size="100">
<br>
<br>
<input type="submit" value="SUBMIT">

</form>
<br>
<br>
<div id="div1"><%@ include file="logoutButton.html" %></div>

<br>
<br>
Active users: ${applicationScope['userCounter']}

</body></html>