<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

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
                                    
					<td><fmt:message key="label.cruduser.id" /></td>
					
                                        <c:if test="${param['insert'] == 'true'}">
                                                <td><input type="text" name="id" value="" size=15
                                                           maxlength=20></td>
                                        <tr>
                                                <td><fmt:message key="label.cruduser.password" /></td>
                                                <td><input type="password" name="password" value="" size=15
                                                           maxlength=20></td>
                                        </tr>        
                                                    <input type="hidden" name="insert" value="true" />  

                                               
                                        </c:if> 
                                        <c:if test="${param['insert']=='false'}">
                                                <td> <input type="text" name="id" value="${param['id']}" size=15
                                                            maxlength=20 readonly="readonly"></td>
                                                <input type="hidden" name="insert" value="false" />
                                                
                                        </c:if>
                                       
                                      
				</tr>
				<tr>
					<td><fmt:message key="label.cruduser.name" /></td>
					<td><input type="text" name="name"
						value="${param['name']}" size=45 maxlength=20></td>
				</tr>
                                <tr>
                                    <td><fmt:message key="label.cruduser.roles" /></td>
					
                                    <td>   <table>
                                            <c:forEach var="role" items="${roles}">
                                                    <tr>
                                                        
                                                   <!--  <c:forEach var="roleName" items="${role.role}">
                                                            <c:out value="${role.role}"></c:out> 
                                                      </c:forEach> !-->
                                                      <td><c:out value="${listUserRole}"/></td>
                                                      <td><c:out value="${ fn:contains(listUserRole,'manager')}"/></td>
                                                      
                                                       <c:choose>
                                                        <c:when test="${ fn:contains(listUserRole, role.role)}">
                                                            
                                                             <td><input type="checkbox"  checked="true" name="roleName"  value="${role.role}" >  "${role.role}"</td>
                                                        </c:when>    
                                                        <c:otherwise>
                                                          
                                                              <td><input type="checkbox" checked="false" name="roleName"  value="${role.role}" >  "${role.role}"</td>
                                                        </c:otherwise>
                                                    </c:choose>

                                                    
                                                      
                                                    
                                                        
                                                    </tr>
                                            </c:forEach>
                                        </table>
                                    </td>
                                </tr>
				
			</table>
		</center>
		<input type="submit" value="Submit"> <input type="reset"
			value="Reset">
	</form>

</body>
</html>