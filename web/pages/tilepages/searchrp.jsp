<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<html>
<head>
<link href="<c:url value='/css/main.css'/>" rel="stylesheet" type="text/css"/>
<fmt:setBundle basename="ApplicationResources" />
<title><fmt:message key="title.searchrp" /></title>
</head>
<body>
    
     <form action="${pageContext.request.contextPath}/nocturne/searchrp"
		method=post>

	<table class="framed">
				<tr>
					<th width="45%"><fmt:message key="caption.name" /></th>
					<th width="55%"><fmt:message key="caption.desc" /></th>
				</tr>
				<tr>
					<td><fmt:message key="fieldLabel.name" /></td>
					<td><input type="text" name="rpnametxt" id="rpnametxt" size=45 maxlength=45></td>
				</tr>
				<tr>
					<td><fmt:message key="fieldLabel.description" /></td>
					<td><input type="text" name="rpdescriptiontxt" id="rpdescriptiontxt" size=45 maxlength=45></td>
				</tr>
				<tr>
					<td colspan="2" align="center">
                                            <input type="button" value="Submit" onClick="searchProgram()" value="Search"> 
                                            <input type="button" value="Reset">
                                        </td>
				</tr>
			</table>
		</center>
            
    
        </div>
                                
       </form>                                 
	<c:if test="${! empty  searchrplist}">
		<table class="borderAll">
			<tr>
				<th><fmt:message key="label.radioprogram.name" /></th>
				<th><fmt:message key="label.radioprogram.description" /></th>
				<th><fmt:message key="label.radioprogram.duration" /></th>
			</tr>
			<c:forEach var="rprogram" items="${searchrplist}" varStatus="loop">
				<tr class="${loop.index%2==0?'even':'odd'}" id="tr-rpid-${loop.index + 1}" onclick="pradioProgramClicked('td-rpname-${loop.index + 1 }');">
					<td class="nowrap" id="td-rpname-${loop.index + 1}">${rprogram.name}</td>
					<td class="nowrap" id="td-rpdesc-${loop.index + 1}">${rprogram.description}</td>
					<td class="nowrap" id="td-rpduration-${loop.index + 1}">${rprogram.typicalDuration}</td>
                                        
				</tr>
			</c:forEach>
		</table>
                        
                        
	</c:if>
    
</body>
</html>