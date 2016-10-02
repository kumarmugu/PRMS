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
            var scheduledPrograms = [];
            var selectedScheduledProgram = {
                'id': (${default.getID()}) ? ${default.getID()} : 0,
                'title': '${default.programName}',
                'programName': '${default.programName}',
                'producerID': '${default.producerId}',
                'presenterID': '${default.presenterId}',
                'producerName': '${default.producerName}',
                'presenterName': '${default.presenterName}',
                'year': ${default.getYear()},
                'week': ${default.getWeek()},
                'day': '${default.getDay()}',
                'start': new Date(${default.startTime.getTime()}),
                'end': new Date(${default.endTime.getTime()})
            };

            var mode = '${mode}';
            var msg = '${msg}';

            var days = ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'];
            <c:forEach items="${events}" var="item">
            scheduledPrograms.push({
                'id': ${item.getID()},
                'title': '${item.programName}',
                'programName': '${item.programName}',
                'producerID': '${item.producerId}',
                'presenterID': '${item.presenterId}',
                'producerName': '${item.producerName}',
                'presenterName': '${item.presenterName}',
                'year': ${item.getYear()},
                'week': ${item.getWeek()},
                'day': '${item.getDay()}',
                'start': new Date(${item.startTime.getTime()}),
                'end': new Date(${item.endTime.getTime()}),
                'updatedBy': 'you'
            });
            </c:forEach>

            console.log(scheduledPrograms);

            var eventData = {
                options: {
                    allowEventDelete: false,
                    readonly: true
                },
                events: scheduledPrograms
            };

            $(document).ready(function () {
                hideAllButtons();
                hideAllScreens();
                hideAllDialogs();

                $("#copyButton").click(function () {
                    clearMsg();
                    loadScreen("copy")
                });

                $("#modifyButton").click(function () {
                    clearMsg();
                    loadScreen("modify");
                });
                $("#deleteButton").click(function () {
                    clearMsg();
                    loadScreen("delete");
                });

                $('[id^="program-tr-"]').click(function () {
                    var programId = $(this).attr('id').replace("tr", "td");
                    $('#programDialog').dialog('close');
                    $("#program").val($("#" + programId).html());
                });

                if (${isAnnualScheduleExist}) {
                    //    pointer-events: none;
                    var $calendar = $('#calendar').weekCalendar({
                        date: new Date(${startDate.getTime()}),
                        dateFormat: 'M d, Y',
                        alwaysDisplayTimeMinutes: true,
                        use24Hour: false,
                        daysToShow: 7,
                        businessHours: false,
                        data: eventData,
                        timeslotsPerHour: 2,
                        scrollToHourMillis: 0,
                        allowCalEventOverlap: false, // Enable conflicting events
                        overlapEventsSeparate: false, // Separate conflicting events
                        totalEventsWidthPercentInOneColumn : 48,
                        height: function ($calendar) {
                            return $(window).height() / 2 - $('h1').outerHeight(true);
                        },
                        eventRender: function (calEvent, $event) {
                            
                            $('#create').css('display', 'inline');
                            //console.log(calEvent);
                            if (!calEvent.id) {
                                console.log(calEvent.start);
                                clearMsg();
                                var today = new Date();
                                hideAllButtons();
                                hideAllScreens();
                                if (calEvent.start >= today) {
                                    createScheduleProgram(calEvent);
                                    loadScreen("create");
                                }
                            }                            
                        },
                        beforeEventNew: function ($event, ui) {
                            $event.stopPropagation();
                            return false;
                        },
                        eventClick: function (calEvent, $event) {
                            selectedScheduledProgram = calEvent;
                            $('.wc-cal-event').css('backgroundColor', '#68a1e5');
                            $event.css('backgroundColor', '#F09E73');
                            $event.find('.time').css({'backgroundColor': '#999', 'border': '1px solid #888'});
                            $('#sStartTime').val(calEvent.start.getTime());
                            $('#sEndTime').val(calEvent.end.getTime());
                            $('#sYear').val($('#year').val());
                            $('#sWeek').val($('#week').val());
                            //$('#sDay').val(calEvent.start.get);
                            clearMsg();
                            hideAllButtons();
                            hideAllScreens();
                            showAllButtons(calEvent.start);
                            //calEvent.title = "[" + selectedScheduledProgram.programName + "] by " + selectedScheduledProgram.presenterName + " and " + selectedScheduledProgram.producerName;
                        },
                    });
                } else {
                    var jscurrentYear = new Date().getFullYear();
                    var userInputYear = parseInt($('#year').val());
                    var DiffYear = userInputYear - jscurrentYear;

                    var createscheduleflag = document.getElementById("txtcreateAnnualSchedule").value;

                    if ($('#year').val() < jscurrentYear)
                    {
                        alert("Annual Schedule Not Found for selected Year: ("+ userInputYear + ")  \n\n"+
                              "Reason: New annual schdeule Not allow to create for past Year!!!");
                    } else if ((userInputYear > jscurrentYear || userInputYear == jscurrentYear))
                    {
                        if (DiffYear < 6 && createscheduleflag == "0")
                        {
                            var r = confirm("Annual Schedule Not Found for selected Year: ("+ userInputYear + ")  \n\n"+
                                    "Do you want to create Annual Schedule for the Year :(" + userInputYear + " ) ?" );
                            if (r == true)
                            {
                                document.getElementById("txtcreateAnnualSchedule").value = "1";                                
                                document.getElementById("searchScheduleform").submit();
                            }

                        } else
                            alert("Annual Schedule Not Found for selected Year: (" + userInputYear + " !!) \n\n"+
                                  "Reason: New annual schdeule allow to create only for upcoming 5 Years!!!");
                    }
                }

                //Re-initialised page
                if (mode !== "") {
                    loadScreen(mode);
                } else if (msg !== "") {
                    displayMsg();
                }
            }
            );

            function popUpProgramDialog() {
                $.get("managerp?submittype=loadall&source=schedule", function (data) {
                    //alert(data);
                    loadDialog("program", data);
                });
            }

            function popUpPresenterDialog() {
                $.get("managepp?submittype=loadall&type=presenter", function (data) {
                    loadDialog("presenter", data);
                });
            }

            function popUpProducerDialog() {
                $.get("managepp?submittype=loadall&type=producer", function (data) {
                    loadDialog("producer", data);
                });
            }

            function presenterProducerClicked(id, type, hdid) {
                $('#' + type + 'Dialog').dialog('close');
                $('#' + type).val($("#" + id).html());
                $('#' + type + "Id").val(document.getElementById(hdid).value);
                //$('#' + type + 'Createid').val(document.getElementById(hdid).value);

            }
            function programClicked(id) {
                $('#programDialog').dialog('close');
                $('#program').val($("#" + id).html());
            }

            function searchProducerPresenter(type) {
                var searchtext = $('#searchText' + type).val();
                $.get('managepp?submittype=search&searchtype=' + type + '&inputname=' + searchtext, function (data) {
                    $('#' + type + 'Dialog').html(data);
                });
            }

            function loadDialog(name, data) {
                hideAllDialogs();
                $("#" + name + "Dialog").html(data);
                $("#" + name + "Dialog").dialog({
                    width: 600,
                    height: 600
                });
            }
            function hideAllDialogs() {
                $('#programDialog').dialog('close');
                $('#producerDialog').dialog('close');
                $('#presenterDialog').dialog('close');
            }

            function loadScreen(name) {
                hideAllScreens();
                $("#" + name + "Screen").show();
                loadScheduledProgram(name);
                $('#' + name + 'Screen #body').html($("#details"));
                $("#details").show();

                location.hash = '#' + name + 'Screen';
            }

            function hideAllScreens() {
                $("#detailScreen").hide();
                $("#createScreen").hide();
                $("#modifyScreen").hide();
                $("#copyScreen").hide();
                $("#deleteScreen").hide();
            }

            function showAllButtons(date) {
                $('#copyButton').show();                
                $('#createButton').hide();
                var today = new Date();
                if (date <= today) {
                    $('#modifyButton').hide();
                    $('#deleteButton').hide();
                }   
                else {
                    $('#modifyButton').show();
                    $('#deleteButton').show();
                }
                loadScreen("detail");
            }
            function hideAllButtons() {
                $('#copyButton').hide();
                $('#modifyButton').hide();
                $('#deleteButton').hide();
                $('#createButton').hide();
            }
            
            function createScheduleProgram(calEvent) {
                selectedScheduledProgram.title = "";
                selectedScheduledProgram.programName = "";
                selectedScheduledProgram.presenterName = "";
                selectedScheduledProgram.producerName = "";
                selectedScheduledProgram.presenterID = "";
                selectedScheduledProgram.producerID = "";
                selectedScheduledProgram.day = calEvent.start.getDay() + 1; // google calender starts on monday, java calendar starts on sunday
                var date = new Date(calEvent.start.getTime());                
                var onejan = new Date(selectedScheduledProgram.start.getFullYear(), 0, 1);
                var week = Math.ceil((((date - onejan) / 86400000) + onejan.getDay() + 1) / 7);
                
                selectedScheduledProgram.start = date;
                selectedScheduledProgram.end = new Date(calEvent.end.getTime());
                selectedScheduledProgram.year = date.getFullYear();
                selectedScheduledProgram.week = week;
                selectedScheduledProgram.day = days[date.getDay()];
            }

            function loadScheduledProgram(mode) {
                $("#scheduledProgramId").val(selectedScheduledProgram.id);
                $("#program").val(selectedScheduledProgram.programName);
                $("#presenter").val(selectedScheduledProgram.presenterName);
                $("#producer").val(selectedScheduledProgram.producerName);
                $("#presenterId").val(selectedScheduledProgram.presenterID);
                $("#producerId").val(selectedScheduledProgram.producerID);
                $("#details #year").val(selectedScheduledProgram.year);
                $("#details #week").val(selectedScheduledProgram.week);
                $("#details #day").val(selectedScheduledProgram.day);
                $("#details #date").attr('value', $.datepicker.formatDate('yy-mm-dd', selectedScheduledProgram.start));
                //$("#details #date").val($.datepicker.formatDate('dd/mm/yyyy', selectedScheduledProgram.start));
                $("#details #startTime").val(getFormatedTimeStringHHMM(selectedScheduledProgram.start));
                $("#details #endTime").val(getFormatedTimeStringHHMM(selectedScheduledProgram.end));
                $("#updatedBy").val(selectedScheduledProgram.updatedBy);

                $("#details #programBrowse").attr('hidden', false);
                $("#details #presenterBrowse").attr('hidden', false);
                $("#details #producerBrowse").attr('hidden', false);
                $("#program").attr('readonly', true);
                $("#presenter").attr('readonly', true);
                $("#producer").attr('readonly', true);
                $("#details #year").attr('readonly', true);
                $("#details #week").attr('readonly', true);
                $("#details #day").attr('readonly', true);
                $("#details #date").attr('readonly', true);                    

                var firstDateOfYear = new Date(selectedScheduledProgram.year, 0, 1);
                $("#details #date").attr('min', $.datepicker.formatDate('yy-mm-dd', firstDateOfYear));
                var lastDateOfYear = new Date(selectedScheduledProgram.year, 11, 31);
                $("#details #date").attr('max', $.datepicker.formatDate('yy-mm-dd', lastDateOfYear));

                if (msg !== "") {
                    $("#details #msg").html(msg);
                }
                if (mode === "create") {
                    $("#program").val("");
                    $("#presenter").val("");
                    $("#producer").val("");

                    $("#details #startTime").attr('readonly', true);
                    $("#details #endTime").attr('readonly', true);
                } else if (mode === "modify") {
                    $("#details #date").attr('readonly', false);
                    $("#details #startTime").attr('readonly', false);
                    $("#details #endTime").attr('readonly', false);
                } else if (mode === "copy") {
                    $("#scheduledProgramId").val(0);
                    if (msg === "")
                    {
                        var nextWeek = new Date(selectedScheduledProgram.start);
                        nextWeek.setDate(nextWeek.getDate() + 7);

                        $("#details #date").attr('value', $.datepicker.formatDate('yy-mm-dd', nextWeek));
                        dateChange();
                    }
                    $("#details #date").attr('readonly', false);
                    $("#details #startTime").attr('readonly', false);
                    $("#details #endTime").attr('readonly', false);
                } else if (mode === "delete" || mode === "detail")
                {
                    $("#details #programBrowse").attr('hidden', true);
                    $("#details #presenterBrowse").attr('hidden', true);
                    $("#details #producerBrowse").attr('hidden', true);
                    $("#details #week").attr('readonly', true);
                    $("#details #day").attr('readonly', true);
                    $("#details #startTime").attr('readonly', true);
                    $("#details #endTime").attr('readonly', true);
                    $("#details #year").attr('readonly', true);
                    $("#details #date").attr('readonly', true);
                }
                return $("#details");
            }

            function displayMsg() {
                if (mode !== "")
                    $('#msg').html(msg);
                else
                    $('#msgSuccess').html(msg);
            }
            function clearMsg() {
                msg = "";
                $('#msgSuccess').html("");
                $('#msg').html("");
            }
            function getFormatedTimeStringHHMM(time) {
                return ("0" + time.getHours()).slice(-2) + ":" + ("0" + time.getMinutes()).slice(-2);
            }

            function dateChange() {
                var date = new Date($("#details #date").attr('value'));
                if (date === null)
                    return;
                $("#details #year").attr('value', date.getFullYear());
                var onejan = new Date(date.getFullYear(), 0, 1);
                var week = Math.ceil((((date - onejan) / 86400000) + onejan.getDay() + 1) / 7);
                $("#details #week").attr('value', week);
                $("#details #day").attr('value', days[date.getDay()]);
            }

        </script>

    </head>
    <body>
        <div id="programDialog" title="Radio program List">            
        </div>
        <div id="presenterDialog" title="Presenter List">            
        </div>
        <div id="producerDialog" title="Producer List">
        </div>
        <h1><fmt:message key="label.crudsp"/></h1>
        <form action="${pageContext.request.contextPath}/nocturne/managesp" method="get"  id="searchScheduleform" name="searchScheduleform">
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
                <input  type="hidden" name="txtcreateAnnualSchedule" id="txtcreateAnnualSchedule" value="0" >               
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
            <br/>
            <span id="msgSuccess" name="msgSuccess" style="color:green"></span>
            
        </div>
        <br/>

        <input type="hidden" name="sYear" id="sYear"/>
        <input type="hidden" name="sWeek" id="sWeek"/>
        <input type="hidden" name="sDay" id="sDay"/>
        <input type="hidden" name="sStartTime" id="sStartTime"/>
        <input type="hidden" name="sEndTime" id="sEndTime"/>

        <div id="detailScreen"  class="form-style-2" style="diplay:none;">
             <div class="form-style-2-heading">View Schedule</div>

            <form action="${pageContext.request.contextPath}/nocturne/nosp" method="post">
                <div id="body"> </div>
            </form>
        </div>

        <div id="createScreen" class="form-style-2" style="diplay:none;">
            <div class="form-style-2-heading">Create New Schedule</div>

            <form action="${pageContext.request.contextPath}/nocturne/addsp" method="post">
                <div id="body"> </div>              

                <label><span>&nbsp;</span><input type="submit" value="Create Schedule" /></label>
            </form>
        </div>


        <div id="modifyScreen"  class="form-style-2" style="diplay:none;">
            <div class="form-style-2-heading">Modify Schedule</div>
            <form action="${pageContext.request.contextPath}/nocturne/modifysp" method="post">
                <div id="body"> </div>
                <label><span>&nbsp;</span><input type="submit" value="Modify Schedule" /></label>
            </form>
        </div>
        <div id="copyScreen"  class="form-style-2" style="diplay:none;">
            <div class="form-style-2-heading">Copy Schedule</div>
            <form action="${pageContext.request.contextPath}/nocturne/copysp" method="post">
                <div id="body"> </div>
                <label><span>&nbsp;</span><input type="submit" value="Copy Schedule" /></label>
            </form>
        </div>


        <div id="deleteScreen"  class="form-style-2" style="diplay:none;">
            <div class="form-style-2-heading">Delete Schedule</div>
            <form action="${pageContext.request.contextPath}/nocturne/deletesp" method="post">
                <div id="body"> </div>
                <label><span>&nbsp;</span><input type="submit" value="Delete Schedule" /></label>
            </form>
        </div>

        <div id="details" style="diplay:none;" hidden>
            <input class="input-field" type="hidden" id="scheduledProgramId"  name="scheduledProgramId" hidden/>
            <input type="hidden" name="presenterId" id="presenterId" value=""/>
            <input type="hidden" name="producerId" id="producerId" value=""/>
            <input type="hidden" name="updateBy" id="updateBy" value=""/>

            <label for="program"><span>Program Name: </span><input type="text" class="input-field" id="program"  name="program" readonly />&nbsp;<input type="button" id="programBrowse" value="..." onClick="popUpProgramDialog()"/></label>
            <label for="presenter"><span>Presenter: </span><input type="text" class="input-field" id="presenter" name="presenter" readonly/>&nbsp;<input type="button" id="presenterBrowse" value="..." onClick="popUpPresenterDialog()"/></label>
            <label for="producer"><span>Producer: </span><input type="text" class="input-field" id="producer" name="producer" readonly/>&nbsp;<input type="button" id="producerBrowse" value="..." onClick="popUpProducerDialog()" /></label>

            <label for="year"><span>Year: </span><input class="input-field" type="text" id="year"  name="year" maxlength="4"  readonly/></label>
            <label for="week"><span>Week: </span><input class="input-field" type="text" id="week"  name="week" maxlength="2" readonly/></label>
            <label for="day"><span>Day: </span><input class="input-field" type="text" id="day"  name="day" maxlength="10" readonly/></label>
            <label for="date"><span>Date: </span><input class="input-field" type="date" id="date"  name="date" readonly onChange="dateChange()"/></label>
            <label for="startTime"><span>Start Time: </span><input class="input-field" type="time" id="startTime"  name="startTime" step="1800" readonly/></label>
            <label for="endTime"><span>End Time: </span><input class="input-field" type="time" id="endTime"  name="endTime" step="1800" readonly/></label>

            <br/><span id="msg" name="msg" style="color:red"></span> <br/><br/>
        </div>
    </body>
</html>
