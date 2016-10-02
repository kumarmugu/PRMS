<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
<link href="<c:url value='/css/main.css'/>" rel="stylesheet" type="text/css"/>
<fmt:setBundle basename="ApplicationResources" />
<title> <fmt:message key="title.cruduser"/> </title>
</head>
<body>
        <h6>${deleteErrMsg}</h6>
        <h1><fmt:message key="label.cruduser"/></h1>
        <c:url var="url" scope="page" value="/nocturne/createmodifyuser">
        		<c:param name="id" value=""/>
                <c:param name="name" value=""/>
                <c:param name="roles" value=""/>
                <c:param name="insert" value="true"/>
        </c:url>
        <a href="${url}"><fmt:message key="label.cruduser.add"/></a>
        <form action="${pageContext.request.contextPath}/nocturne/finduser"
		method=post>
		<center>
			<table>
				
				<tr>
					<td><fmt:message key="label.cruduser.findbyid" /></td>
					<td><input type="text" name="userid" size=45 maxlength=45></td>
                                        <td colspan="2" align="center"><input type="submit" value="Submit"> <input
						type="reset" value="Reset"></td>
                                </tr>
			</table>
		</center>

	</form>
        <br/><br/>
        <table class="borderAll">
            <tr>
                <th><fmt:message key="label.cruduser.id"/></th>
                <th><fmt:message key="label.cruduser.name"/></th>
                <th><fmt:message key="label.cruduser.roles"/></th>
                <th><fmt:message key="label.cruduser.edit"/> 
                <th><fmt:message key="label.cruduser.delete"/></th>
            </tr>
            
            <c:forEach var="cruduser" items="${users}" varStatus="status">
                <c:set var="rolesString" value ="" />
                <tr class="${status.index%2==0?'even':'odd'}">
                    <td class="nowrap">${cruduser.id}</td>
                    <td class="nowrap">${cruduser.name}</td>
                    
                    
                    <c:forEach var="role" items="${cruduser.roles}" varStatus="status">
                        
                    <c:choose>
                        <c:when test="${rolesString==''}">
                            <c:set var="rolesString" value="${status.first ? '' :   rolesString}${role.accessPrivilege}" />
                        </c:when>    
                        <c:otherwise>
                           <c:set var="rolesString" value="${status.first ? '' :   rolesString}:${role.accessPrivilege}" />
                        </c:otherwise>
                    </c:choose>
                        
                    </c:forEach>
                    
                    <td class="nowrap">${rolesString}</td>
                    <%-- <td class="nowrap">${cruduser.roles}</td> --%>
                    <td class="nowrap">
                        <c:url var="updurl" scope="page" value="/nocturne/createmodifyuser">
                            <c:param name="id" value="${cruduser.id}"/>
                            <c:param name="name" value="${cruduser.name}"/>
                          
                            <c:param name="roles" value="${rolesString}"/>
                             <c:param name="insert" value="false"/>
                        </c:url>
                        <a href="${updurl}"><fmt:message key="label.cruduser.edit"/></a>
                        &nbsp;&nbsp;&nbsp;
                       
                    </td>
                    <td>
                         <c:url var="delurl" scope="page" value="/nocturne/deleteuser">
                            <c:param name="id" value="${cruduser.id}"/>
                        </c:url>
                        <a href="${delurl}"><fmt:message key="label.cruduser.delete"/></a>
                        
                    </td>
                </tr>
            </c:forEach>
        </table>
</body>
</html>