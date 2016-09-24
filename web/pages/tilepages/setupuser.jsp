<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<fmt:setBundle basename="ApplicationResources" />

<title><fmt:message key="title.cruduser" /></title>
</head>
<body>
	<form action="${pageContext.request.contextPath}/nocturne/enteruser" method=post>
		<center>
			<table cellpadding=4 cellspacing=2 border=0>
                            <tr>
					
				</tr>
				<tr>
                                    <c:set var="userid" value ="" />
                                    <c:set var="userName" value ="" />
                                    
                                    
					<td><fmt:message key="label.cruduser.id" /></td>
					<td><c:if test="${param['insert'] == 'true'}">
							<input type="text" name="id" value="" size=15
								maxlength=20>
                                                    <input type="hidden" name="ins" value="true" />  
                                            </c:if> 
                                            <c:if test="${param['insert']=='false'}">
							<input type="text" name="id" value="${param['id']}" size=15
								maxlength=20 readonly="readonly">
                                                       
                                                       <input type="hidden" name="ins" value="false" />
                                            </c:if>
                                        </td>
                                      <%--  <td>  <c:set var="rolesString" value ="" />
                                                <c:forEach var="cruduser" items="${cruduser.roles}" varStatus="status">
                                                    <c:set var="rolesString" value="${status.first ? '' : rolesString} ${cruduser.accessPrivilege}" />
                                                </c:forEach>
                                        </td>  --%>
				</tr>
				<tr>
					<td><fmt:message key="label.cruduser.name" /></td>
					<td><input type="text" name="name"
						value="${param['name']}" size=45 maxlength=20></td>
				</tr>
				<tr>
					<td><fmt:message key="label.cruduser.roles" /></td>
					<td><input type="text" name="roles"
						value="${roles}" size=45 maxlength=20></td>
                                        
                                       <c:forEach var="role" items="${roles}">
                                            <tr>
                                                <c:forEach var="roleName" items="${role.role}">
                                                    <td> <c:out value="${role.role}"></c:out> </td>
                                                </c:forEach>
                                                <td> <input type="checkbox" name="completed" value="?????"> </td>
                                            </tr>
                                </c:forEach>
				
			</table>
		</center>
		<input type="submit" value="Submit"> <input type="reset"
			value="Reset">
	</form>

</body>
</html>