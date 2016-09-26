<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:choose>
    <c:when test="${source == 'schedule'}">
<!--        <div style="padding:10px;height:15px;padding-bottom: 30px;padding-left: 170px;">
            <input name="searchText" id="searchText${type}" type="text" value="${searchText}" style="height:30px;" />
            &nbsp;<input type="button" onclick="searchProducerPresenter('${type}')" value="Search" />
        </div>-->
        <table class="table-fill">
            <tbody class="table-hover">   
            <c:forEach var="crudrp" items="${searchrplist}" varStatus="loop">
                <tr id="program-tr-${loop.index + 1}" onclick="programClicked('program-td-${loop.index +1 }');">
                    <td id="program-td-${loop.index +1 }" class="text-left">${crudrp.name}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </c:when>
    <c:otherwise>
        <tiles:insertDefinition name="searchrpPage"/>
    </c:otherwise>
</c:choose>
