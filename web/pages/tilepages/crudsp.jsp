<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
    <head>
        <link href="<c:url value='/css/main.css'/>" rel="stylesheet" type="text/css"/>
        <fmt:setBundle basename="ApplicationResources" />
        <title> <fmt:message key="title.crudsp"/> </title>
        <link rel='stylesheet' type='text/css' href="<c:url value='/css/jquery-ui-1.8.11.custom.css'/>" />
        <link rel='stylesheet' type='text/css' href="<c:url value='/css/jquery.weekcalendar.css'/>" />
        <link rel="stylesheet" type="text/css" href="<c:url value='/css/default.css'/>" />
        <link rel="stylesheet" type="text/css" href="<c:url value='/css/prms.css'/>" />


        <script type='text/javascript' src="<c:url value='/js/jquery-1.4.4.min.js'/>"></script>
        <script type='text/javascript' src="<c:url value='/js/jquery-ui-1.8.11.custom.min.js'/>"></script>

        <script type="text/javascript" src="<c:url value='/js/date.js'/>"></script>
        <script type='text/javascript' src="<c:url value='/js/jquery.weekcalendar.js'/>"></script>

        <script type='text/javascript'>
            var events = [];
            var days = ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'];
            <c:forEach items="${events}" var="item">
            events.push({
                'id': 1,
                'start': new Date(${item.startTime.getTime()}),
                'end': new Date(${item.endTime.getTime()}),
                'title': '${item.programName}'
            });
            </c:forEach>
            console.log(events);

            var eventData = {
                options: {
                    allowEventDelete: false,
                    readonly: true
                },
                events: events
            };

            $(document).ready(function () {
                $('#copyButton').hide();
                $('#modifyButton').hide();
                $('#deleteButton').hide();
//                $('#createButton').hide();
                $("#createScreen").hide();
                $("#modifyScreen").hide();
                $("#deleteScreen").hide();
                $("#programDialog").hide();
                $("#producerDialog").hide();
                $("#presenterDialog").hide();

                $("#copyButton").click(function () {
                    $("#createScreen").hide();
                    $("#modifyScreen").show();
                    $("#deleteScreen").hide();
                    location.hash = "#modifyScreen";
                });
                $("#modifyButton").click(function () {
                    $("#createScreen").hide();
                    $("#modifyScreen").show();
                    $("#deleteScreen").hide();
                    location.hash = "#modifyScreen";
                });
                $("#deleteButton").click(function () {
                    $("#createScreen").hide();
                    $("#modifyScreen").hide();
                    $("#deleteScreen").show();
                    location.hash = "#deleteScreen";
                });
                $("#programBrowse").click(function () {
                    $("#programDialog").dialog({
                        width: 500,
                        height: 400
                    });
                });
                $("#presenterBrowse").click(function () {
                    $("#presenterDialog").dialog({
                        width: 500,
                        height: 400
                    });
                });
                $("#producerBrowse").click(function () {
                    $("#producerDialog").dialog({
                        width: 500,
                        height: 400
                    });
                });
                $('[id^="program-tr-"]').click(function () {
                    var programId = $(this).attr('id').replace("tr", "td");
                    $('#programDialog').dialog('close');
                    $("#programCreate").val($("#" + programId).html());
                });
                $('[id^="presenter-tr-"]').click(function () {
                    var presenterId = $(this).attr('id').replace("tr", "td");
                    $('#presenterDialog').dialog('close');
                    $("#presenterCreate").val($("#" + presenterId).html());
                });
                $('[id^="producer-tr-"]').click(function () {
                    var producerId = $(this).attr('id').replace("tr", "td");
                    $('#producerDialog').dialog('close');
                    $("#producerCreate").val($("#" + producerId).html());
                });
                if (${isAnnualScheduleExist}) {
                    var $calendar = $('#calendar').weekCalendar({
                        date: new Date(${startDate.getTime()}),
                        data: eventData,
                        timeslotsPerHour: 2,
                        scrollToHourMillis: 0,
                        allowCalEventOverlap: false, // Enable conflicting events
                        overlapEventsSeparate: false, // Separate conflicting events
                        height: function ($calendar) {
                            return $(window).height() / 2 - $('h1').outerHeight(true);
                        },
                        eventRender: function (calEvent, $event) {
                            $('#create').css('display', 'inline');
                            //console.log(calEvent);
                            if (!calEvent.id) {
                                console.log(calEvent.start);
                                $('.wc-cal-event').css('backgroundColor', '#68a1e5');
                                var startMinutes = (calEvent.start.getMinutes() == 0) ? (calEvent.start.getMinutes() + '0') : calEvent.start.getMinutes();
                                var endMinutes = (calEvent.end.getMinutes() == 0) ? (calEvent.end.getMinutes() + '0') : calEvent.end.getMinutes();
                                $('#startTimeCreate').val(calEvent.start.getHours() + ":" + startMinutes);
                                $('#endTimeCreate').val(calEvent.end.getHours() + ":" + endMinutes);
                                $('#yearCreate').val($('#year').val());
                                $('#weekCreate').val($('#week').val());
                                $('#dayCreate').val(days[calEvent.start.getDay()]);
                                $('#dateCreate').val(calEvent.end.getDate() + "/" + calEvent.end.getMonth() + "/" + calEvent.end.getFullYear());
                                $('#copyButton').hide();
                                $('#modifyButton').hide();
                                $('#deleteButton').hide();
//                                $('#createButton').show();
                                $("#createScreen").show();
                                $("#modifyScreen").hide();
                                $("#deleteScreen").hide();
                                $("#programCreate").val('');
                                $("#presenterCreate").val('');
                                $("#producerCreate").val('');
                                location.hash = "#createScreen";
                            }
                        },
                        beforeEventNew: function ($event, ui) {
                            $event.stopPropagation();
                            return false;
                        },
                        eventClick: function (calEvent, $event) {
                            $('.wc-cal-event').css('backgroundColor', '#68a1e5');
                            $event.css('backgroundColor', '#aaa');
                            $event.find('.time').css({'backgroundColor': '#999', 'border': '1px solid #888'});
                            $('#sStartTime').val(calEvent.start.getTime());
                            $('#sEndTime').val(calEvent.end.getTime());
                            $('#sYear').val($('#year').val());
                            $('#sWeek').val($('#week').val());
                            //$('#sDay').val(calEvent.start.get);
                            $('#copyButton').show();
                            $('#modifyButton').show();
                            $('#deleteButton').show();
                            $('#createButton').hide();
                            $("#createScreen").hide();
                            $("#modifyScreen").hide();
                            $("#deleteScreen").hide();
                        }
                    });
                } else {
                    var r = confirm("Annual schedule not exist. Do you want to create?");
                }
            });
        </script>

    </head>
    <body>
        <div id="programDialog" title="Radio program List">
            <table class="table-fill">
                <tbody class="table-hover">
                    <% for(int i = 1; i <= 10; i++) { %>
                        <tr id="program-tr-<%=i%>">
                            <td id="program-td-<%=i%>" class="text-left">Program <%=i%></td>
                        </tr>
                    <% } %>
                </tbody>
            </table>
        </div>
        <div id="presenterDialog" title="Presenter List">
            <table class="table-fill">
                <tbody class="table-hover">
                    <% for(int i = 1; i <= 10; i++) { %>
                        <tr id="presenter-tr-<%=i%>">
                            <td id="presenter-td-<%=i%>" class="text-left">Presenter <%=i%></td>
                        </tr>
                    <% } %>
                </tbody>
            </table>
        </div>
        <div id="producerDialog" title="Producer List">
            <table class="table-fill">
                <tbody class="table-hover">
                    <% for(int i = 1; i <= 10; i++) { %>
                        <tr id="producer-tr-<%=i%>">
                            <td id="producer-td-<%=i%>" class="text-left">Producer <%=i%></td>
                        </tr>
                    <% } %>
                </tbody>
            </table>
        </div>
        <h1><fmt:message key="label.crudsp"/></h1>
        <form action="${pageContext.request.contextPath}/nocturne/managesp" method="get">
            <div class="form-style-2" style="margin: auto;">
                Year: <input type="text" class="tel-number-field" name="year" id="year" value="${(param['year']!=null) ? param['year'] : currentYear}" size=15
                             maxlength=20>
                <span style="width:50px;">&nbsp;</span>
                Week: <select name="week" class="tel-number-field" id="week" value="${param['week']}">
                    <c:forEach begin="1" end="53" varStatus="loop">
                        <option value="${loop.index}" ${(loop.index== param['week'] || (param['week'] ==null && loop.index== weekNo )) ? 'selected="selected"' : ''}>W${loop.index}</option>
                    </c:forEach>
                </select>
                <span style="width:50px;">&nbsp;</span>
                <input type="submit" value="Search">
            </div>
        </form>
        <div class="clearer"></div>
        <div id="calendar"></div>

        <div class="form-style-2" style="margin: auto;">            
            <input type="button" id="copyButton" value="Copy">  
            <span style="width:50px;">&nbsp;</span>         
            <input type="button" id="modifyButton" value="Modify">     
            <span style="width:50px;">&nbsp;</span>      
            <input type="button" id="deleteButton" value="Delete">      
            <span style="width:50px;">&nbsp;</span>     
            <input type="button" id="createButton" value="Create Schedule" style="display:none;">
        </div>
        <br/>

        <input type="hidden" name="sYear" id="sYear"/>
        <input type="hidden" name="sWeek" id="sWeek"/>
        <input type="hidden" name="sDay" id="sDay"/>
        <input type="hidden" name="sStartTime" id="sStartTime"/>
        <input type="hidden" name="sEndTime" id="sEndTime"/>
        <div id="createScreen" class="form-style-2" style="diplay:none;">
            <div class="form-style-2-heading">Create New Schedule</div>
            <form action="" method="post">
                <label for="programCreate"><span>Program Name: </span><input type="text" class="input-field" id="programCreate"  name="programCreate" disabled/>&nbsp;<input type="button" id="programBrowse" value="..." /></label>
                <label for="presenterCreate"><span>Presenter: </span><input type="text" class="input-field" id="presenterCreate" name="presenterCreate" disabled/>&nbsp;<input type="button" id="presenterBrowse" value="..." /></label>
                <label for="producerCreate"><span>Producer: </span><input type="text" class="input-field" id="producerCreate" name="producerCreate" disabled/>&nbsp;<input type="button" id="producerBrowse" value="..." /></label>
                <label for="yearCreate"><span>Year: </span><input class="tel-number-field" type="text" id="yearCreate"  name="yearCreate" maxlength="4"  readonly/></label>
                <label for="weekCreate"><span>Week: </span><input class="tel-number-field" type="text" id="weekCreate"  name="weekCreate" maxlength="2" readonly/></label>
                <label for="dayCreate"><span>Day: </span><input class="input-field" type="text" id="dayCreate"  name="dayCreate" readonly/></label><br/>
                <label for="dateCreate"><span>Date </span><input class="tel-number-field" type="text" id="dateCreate"  name="dayCreate" readonly/></label><br/>
                <label for="startTimeCreate"><span>Start Time: </span><input class="tel-number-field" type="text" id="startTimeCreate"  name="startTimeCreate" readonly/></label>
                <label for="endTimeCreate"><span>Week: </span><input class="tel-number-field" type="text" id="endTimeCreate"  name="endTimeCreate" readonly/></label


                <label><span>&nbsp;</span><input type="submit" value="Create Schedule" /></label>
            </form>
        </div>
        <div id="modifyScreen"  class="form-style-2" style="diplay:none;">
            <div class="form-style-2-heading">Modify/Copy Schedule</div>
            <form action="" method="post">
                <label for="programModify"><span>Program Name: </span><input type="text" class="input-field" id="programModify"  name="programModify" /></label>
                <label for="presenterModify"><span>Presenter: </span><input type="text" class="input-field" id="presenterModify" name="presenterModify" /></label>
                <label for="producerModify"><span>Producer: </span><input type="text" class="input-field" id="producerModify" name="producerv" /></label>
                <label for="yearModify"><span>Year: </span><input class="tel-number-field" type="text" id="yearModify"  name="yearModify" maxlength="4"  readonly/></label>
                <label for="weekModify"><span>Week: </span><input class="tel-number-field" type="text" id="weekModify"  name="weekModify" maxlength="2" readonly/></label>
                <label for="startTimeModify"><span>Start Time: </span><input class="input-field" type="text" id="startTimeModify"  name="startTimeModify" readonly/></label>
                <label for="endTimeModify"><span>End Time: </span><input class="input-field" type="text" id="endTimeModify"  name="endTimeModify" readonly/></label


                <label><span>&nbsp;</span><input type="submit" value="Modify/Copy Schedule" /></label>
            </form>
        </div>
        <div id="deleteScreen"  class="form-style-2" style="diplay:none;">
            <div class="form-style-2-heading">Delete Schedule</div>
            <form action="" method="post">
                <label for="program"><span>Program Name: </span><span>XXXXXXXXXXXXX</span></label>
                <label for="presenter"><span>Presenter: </span><span>XXXXXXXXXXXXX</span></label>
                <label for="producer"><span>Producer: </span><span>XXXXXXXXXXXXX</span></label>
                <label for="yearDelete"><span>Year: </span><span id="yearDelete"></span></label>
                <label for="weekDelete"><span>Week: </span><span id="weekDelete"></span></label>
                <label for="dayDelete"><span>Day: </span><span id="dayDelete"></span></label>
                <label for="startTimeDelete"><span>Start Time: </span><span id="startTimeDelete"></span></label>
                <label for="endTimeDelete"><span>End Time: </span><span id="endTimeDelete"></span></label>

                <label><span>&nbsp;</span><input type="submit" value="Delete Schedule" /></label>
            </form>
        </div>
    </body>
</html>