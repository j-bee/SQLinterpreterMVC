<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<html>
<body>
<link rel="stylesheet" type="text/css" href="style.css" />
<%@ include file="header.html" %>


<!-- Dla ka¿dego rzêdu wynikowego("row"), te rzêdy pochodz¹ z wyniku zapytania sql bêd¹cego zmienn¹ "rs", maj¹c¹ pole "rows", "items" to tak jak "in"-->
<c:set var="resultList" value="${requestScope.resultList}"/>
<c:set var="columns" value="${requestScope.columns}"/>
<c:set var="sqlMessage" value="${requestScope.sqlMessage}"/>
<c:set var="errorMsg" value="${requestScope.errorMsg}"/>

<c:choose>
<c:when test="${errorMsg!=null}">
<c:out value="${errorMsg}"></c:out>
</c:when>

<c:when test="${sqlMessage!=null}">
${sqlMessage}
</c:when>

<c:when test="${resultList!=null}">

You queried for: 
<c:forEach var="col" items="${columns}">
<i><c:out value="${col}"></c:out>  </i>
</c:forEach>
<br><br>
<strong>Results: </strong>
<br><br><table>

<!--drukowanie nazw kolumn--> 
<tr>
<c:forEach var="col" items="${columns}">
<!--<td><strong>${col}</strong></td>-->

<th><c:out value="${col}"></c:out></th>
</c:forEach>
</tr>

<c:forEach var="book" items="${resultList}">

<tr>
<c:forEach var="col" items="${columns}">

<td><c:out value="${book[col]}"></c:out></td>

</c:forEach>
</c:forEach>
</tr>

</table>
</c:when>
</c:choose>
 
<br><br>
</body>
</html>