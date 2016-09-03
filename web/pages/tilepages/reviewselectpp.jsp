<%-- 
    Document   : reviewselectpp
    Created on : 01-Sep-2016, 05:08:10
    Author     : THIRILWIN
--%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
<link href="<c:url value='/css/main.css'/>" rel="stylesheet" type="text/css"/>
<fmt:setBundle basename="ApplicationResources" />
<title> <fmt:message key="title.reviewselectpp"/> </title>
</head>
<body>
     <form action="${pageContext.request.contextPath}/nocturne/managepp"
		method=post>
		<center>
			<table class="framed">
                            <tr>
                                <td>Select the type:</td>
                                <td> 
                                   <select name="type">
                                    <option value="presenter">Presenter</option>
                                    <option value="producer">Producer</option>
                                  </select></td>
                            </tr>
                            <tr>
                                <input type="hidden" name="submittype" value="loadall" />
				<td colspan="2" align="center"><input type="submit" value="LOAD ALL"> </td>
				</tr>
                                <Tr>
                                    <td>&nbsp;</td>
                                </Tr>
	</form>	
        <form action="${pageContext.request.contextPath}/nocturne/managepp"
		method=post>
				<tr>
                                    <td>&nbsp;</td>
                                    <td>&nbsp;</td>
				</tr>
				<tr>
					<td>Key in presenter/producer</td>
					<td><input type="text" name="inputname" size=45 maxlength=45></td>
				</tr>
                                 <tr>
                                <td>Select the type:</td>
                                <td> 
                                   <select name="searchtype">
                                    <option value="presenter">Presenter</option>
                                    <option value="producer">Producer</option>
                                  </select></td>
                                </tr>
				<tr>
                                    <input type="hidden" name="submittype" value="search" />
				    <td colspan="2" align="center"><input type="submit" value="Search"> </td>
				</tr>
			</table>
		</center>
	</form>
      <c:if test="${! empty  rps}">
        <table class="borderAll">
            <tr>
                <th><fmt:message key="label.reviewselectpp.id"/></th>
                <th><fmt:message key="label.reviewselectpp.Name"/></th>
            </tr>
            <c:forEach var="crudrp" items="${rps}" varStatus="status">
                <tr class="${status.index%2==0?'even':'odd'}">
                    <td class="nowrap">${crudrp.id}</td>
                    <td class="nowrap">${crudrp.name}</td>
                </tr>
            </c:forEach>
        </table>
        </c:if>
</body>
</html>
