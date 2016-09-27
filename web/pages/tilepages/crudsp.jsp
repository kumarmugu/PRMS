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
            var selectedScheduledProgram = null;
            
            var days = ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'];
            <c:forEach items="${events}" var="item">
            scheduledPrograms.push({
                'id': ${item.getID()},
                'title': '${item.programName}',
                'producerID': 0,
                'presenterID': 0,                
                'year' : ${item.getYear()},
                'week' : ${item.getWeek()},
                'day' : '${item.getDay()}',
                'start': new Date(${item.startTime.getTime()}),
                'end': new Date(${item.endTime.getTime()})                
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
                $('#copyButton').hide();
                $('#modifyButton').hide();
                $('#deleteButton').hide();
//                $('#createButton').hide();
                $("#createScreen").hide();
                
                $("#modifyScreen").hide();
                $("#copyScreen").hide();
                //$("#details").hide();
                
                $("#deleteScreen").hide();
                $("#programDialog").hide();
                $("#producerDialog").hide();
                $("#presenterDialog").hide();
                
                $("#copyButton").click(function () {
                    $("#createScreen").hide();
                    $("#modifyScreen").hide();
                    $("#copyScreen").show();
                    $("#deleteScreen").hide();
                    
                    loadScheduledProgram("copy");
                    $('#copyScreen #body').html( $("#details") );
                    $("#details").show();
                    
                    location.hash = "#copyScreen";
                });
                $("#modifyButton").click(function () {
                    $("#createScreen").hide();
                    $("#modifyScreen").show();
                    $("#copyScreen").hide();
                    $("#deleteScreen").hide();
                    
                    loadScheduledProgram("modify");
                    $('#modifyScreen #body').html( $("#details") );
                    $("#details").show();
                    
                    
                    location.hash = "#modifyScreen";
                });
                $("#deleteButton").click(function () {
                    $("#createScreen").hide();
                    $("#modifyScreen").hide();
                    $("#copyScreen").hide();
                    $("#deleteScreen").show();
                    location.hash = "#deleteScreen";
                });
                $("#programBrowse").click(function () {
                    $('#presenterDialog').dialog('close');
                    $('#producerDialog').dialog('close');
                    $.get( "managerp?submittype=loadall&source=schedule", function( data ) {
                        //alert(data);
                        $("#programDialog").html(data);
                    });
                    $("#programDialog").dialog({
                        width: 600,
                        height: 600
                    });
                });
                $("#presenterBrowse").click(function () {
                    $('#programDialog').dialog('close');
                    $('#producerDialog').dialog('close');
                    $.get( "managepp?submittype=loadall&type=presenter", function( data ) {
                        $("#presenterDialog").html(data);
                    });
                    $("#presenterDialog").dialog({
                        width: 600,
                        height: 600
                    });
                });
                $("#producerBrowse").click(function () {
                    $('#programDialog').dialog('close');
                    $('#presenterDialog').dialog('close');
                    $.get( "managepp?submittype=loadall&type=producer", function( data ) {
                        $("#producerDialog").html(data);
                    });
                    $("#producerDialog").dialog({
                        width: 600,
                        height: 600
                    });
                });
                $('[id^="program-tr-"]').click(function () {
                    var programId = $(this).attr('id').replace("tr", "td");
                    $('#programDialog').dialog('close');
                    $("#programCreate").val($("#" + programId).html());
                });
                if (${isAnnualScheduleExist}) {
                    //    pointer-events: none;
                    var $calendar = $('#calendar').weekCalendar({
                        date: new Date(${startDate.getTime()}),
                        businessHours : false,
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
                                //var startMinutes = (calEvent.start.getMinutes() == 0) ? (calEvent.start.getMinutes() + '0') : calEvent.start.getMinutes();
                                //var endMinutes = (calEvent.end.getMinutes() == 0) ? (calEvent.end.getMinutes() + '0') : calEvent.end.getMinutes();
                               // $('#startTimeCreate').val(calEvent.start.getHours().toString().padStart(2,"0") + ":" + startMinutes);
                                //$('#endTimeCreate').val(calEvent.end.getHours().toString().padStart(2,"0") + ":" + endMinutes);
                                $('#startTimeCreate').val(getFormatedTimeStringHHMM(calEvent.start));
                                $('#endTimeCreate').val(getFormatedTimeStringHHMM(calEvent.end));
                                $('#yearCreate').val($('#year').val());
                                $('#weekCreate').val($('#week').val());
                                $('#dayCreate').val(days[calEvent.start.getDay()]);
                                $('#dateCreate').val($.datepicker.formatDate('dd/mm/yy', calEvent.end) );
                                //$('#dateCreate').val(calEvent.end.getDate().toString().padStart(2,"0") + "/" + calEvent.end.getMonth().toString().padStart(2,"0") + "/" + calEvent.end.getFullYear());
                                $('#copyButton').hide();
                                $('#modifyButton').hide();
                                $('#deleteButton').hide();
//                                $('#createButton').show();
                                $("#createScreen").show();
                                $("#modifyScreen").hide();
                                $("#copyScreen").hide();
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
                            selectedScheduledProgram = calEvent;
                            $('.wc-cal-event').css('backgroundColor', '#68a1e5');
                            $event.css('backgroundColor', '#F09E73');
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
                            $("#copyScreen").hide();
                            $("#deleteScreen").hide();
                        }
                    });
                } else {
                        var jscurrentYear=new Date().getFullYear() ;
                        var userInputYear= parseInt($('#year').val()) ;
                        var DiffYear= userInputYear-jscurrentYear;

                        var createscheduleflag=document.getElementById("txtcreateAnnualSchedule").value;

                        if( $('#year').val()<jscurrentYear)
                        {
                             alert ("No Annual Schedule found for selected Year!!!");
                        }
                        else if (( userInputYear > jscurrentYear ||userInputYear == jscurrentYear) )
                        {  
                            if(DiffYear<4 && createscheduleflag =="0" )
                            { 

                              var r = confirm("Annual schedule not exist. Do you want to create Annual Schedule for the Year : ("+ userInputYear +" ) Current Year :"+ jscurrentYear);
                              if(r==true)
                              {
                                document.getElementById("txtcreateAnnualSchedule").value = "1";
                                 alert(document.getElementById("txtcreateAnnualSchedule").value); 
                                document.getElementById("searchScheduleform").submit();                          
                              }
   
    }
                         else
                             alert ("No Annual Schedule found for Year("+ userInputYear +" !!) New annual schdeule allow to create only for upcoming 3 Years!!!"); 
                      }
                }
            });
            
         function presenterProducerClicked(id, type, hdid){
           
            $('#' + type + 'Dialog').dialog('close');
            $('#' + type + 'Create').val($("#" + id).html());
            //alert($("#" + hdid).val());
            $('#' + type + 'Createid').val($("#" + hdid).val());
             
         }
         function programClicked(id){
            $('#programDialog').dialog('close');
            $('#programCreate').val($("#" + id).html());
            
            //alert($("#" + id).html());
         }
        function searchProducerPresenter(type){
            var searchtext = $('#searchText'+ type).val();
            $.get('managepp?submittype=search&searchtype=' + type + '&inputname='+ searchtext, function( data ) {
                $('#' + type + 'Dialog').html(data);
            });
        }
        
        function loadScheduledProgram(mode) {
                    
           
            
            $("#program").attr('readonly', true);
            $("#presenter").attr('readonly', true);
            $("#producer").attr('readonly', true);

            if (mode === "create") {
                $("#program").val("");
                $("#presenter").val("");
                $("#producer").val("");
            
                $("#year").attr('readonly', true);
                $("#week").attr('readonly', true);
                $("#startTime").attr('readonly', true);
                $("#endTime").attr('readonly', true);
            }
            else if(mode === "modify") {
                $("#scheduledProgramId").val(selectedScheduledProgram.id);                
                $("#program").val(selectedScheduledProgram.title);
                $("#presenter").val("");
                $("#producer").val("");
                $("#details #year").val(selectedScheduledProgram.year);
                $("#details #week").val(selectedScheduledProgram.week);
                $("#day").val(selectedScheduledProgram.day);
                $("#startTime").val( getFormatedTimeStringHHMM(selectedScheduledProgram.start));
                $("#endTime").val(getFormatedTimeStringHHMM(selectedScheduledProgram.end));

                $("#details #year").attr('readonly', true);
                $("#details #week").attr('readonly', false);
                $("#day").attr('readonly', false);
                $("#startTime").attr('readonly', false);
                $("#endTime").attr('readonly', false);
            }
            else if( mode === "copy") {
                $("#scheduledProgramId").val(0);                
                $("#program").val(selectedScheduledProgram.title);
                $("#presenter").val("");
                $("#producer").val("");
                $("#details #year").val(selectedScheduledProgram.year);
                $("#details #week").val(selectedScheduledProgram.week + 1);
                $("#day").val(selectedScheduledProgram.day);
                $("#startTime").val( getFormatedTimeStringHHMM(selectedScheduledProgram.start));
                $("#endTime").val(getFormatedTimeStringHHMM(selectedScheduledProgram.end));

                $("#details #year").attr('readonly', false);
                $("#details #week").attr('readonly', false);
                $("#day").attr('readonly', false);
                $("#startTime").attr('readonly', false);
                $("#endTime").attr('readonly', false);
            }
            
            return $("#details");            
        }
        
        function getFormatedTimeStringHHMM(time) {
            return ("0" + time.getHours()).slice(-2) + ":" + ("0" + time.getMinutes()).slice(-2);
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
        </div>
        <br/>

        <input type="hidden" name="sYear" id="sYear"/>
        <input type="hidden" name="sWeek" id="sWeek"/>
        <input type="hidden" name="sDay" id="sDay"/>
        <input type="hidden" name="sStartTime" id="sStartTime"/>
        <input type="hidden" name="sEndTime" id="sEndTime"/>
        

                
        <div id="createScreen" class="form-style-2" style="diplay:none;">
        <div class="form-style-2-heading">Create New Schedule</div>
        
            <form action="${pageContext.request.contextPath}/nocturne/addsp" method="post">
                <label for="programCreate"><span>Program Name: </span><input type="text" class="input-field" id="programCreate"  name="programCreate" value="charity" disabled/>&nbsp;<input type="button" id="programBrowse" value="..." /></label>
                <label for="presenterCreate"><span>Presenter: </span><input type="text" class="input-field" id="presenterCreate" name="presenterCreate" disabled/>&nbsp;<input type="button" id="presenterBrowse" value="..." /></label>
                <label for="producerCreate"><span>Producer: </span><input type="text" class="input-field" id="producerCreate" name="producerCreate" disabled/>&nbsp;<input type="button" id="producerBrowse" value="..." /></label>
                <label for="yearCreate"><span>Year: </span><input class="tel-number-field" type="text" id="yearCreate"  name="yearCreate" maxlength="4"  readonly/></label>
                <label for="weekCreate"><span>Week: </span><input class="tel-number-field" type="text" id="weekCreate"  name="weekCreate" maxlength="2" readonly/></label>
                <label for="dayCreate"><span>Day: </span><input class="input-field" type="text" id="dayCreate"  name="dayCreate" readonly/></label><br/>
                <label for="dateCreate"><span>Date </span><input class="input-field" type="text" id="dateCreate"  name="dateCreate" readonly/></label><br/>
                <label for="startTimeCreate"><span>Start Time: </span><input class="tel-number-field" type="text" id="startTimeCreate"  name="startTimeCreate" readonly/></label>
                <label for="endTimeCreate"><span>End Time: </span><input class="tel-number-field" type="text" id="endTimeCreate"  name="endTimeCreate" readonly/></label
                
               
                <input type="hidden" name="presenterCreateid" id="presenterCreateid" value=""/>
                <input type="hidden" name="producerCreateid"  id="producerCreateid" value=""/>

                <label><span>&nbsp;</span><input type="submit" value="Create Schedule" /></label>
            </form>
        </div>
        
        
        <div id="modifyScreen"  class="form-style-2" style="diplay:none;">
            <div class="form-style-2-heading">Modify Schedule</div>
            <form action="${pageContext.request.contextPath}/nocturne/modifysp" method="post">
                <div id="body">
                    
                </div>
                <label><span>&nbsp;</span><input type="submit" value="Modify Schedule" /></label>
            </form>
        </div>
        <div id="copyScreen"  class="form-style-2" style="diplay:none;">
            <div class="form-style-2-heading">Copy Schedule</div>
            <form action="${pageContext.request.contextPath}/nocturne/copysp" method="post">
                <div id="body">
                    
                </div>
                <label><span>&nbsp;</span><input type="submit" value="Copy Schedule" /></label>
            </form>
        </div>
        
        
        <div id="deleteScreen"  class="form-style-2" style="diplay:none;">
            <div class="form-style-2-heading">Delete Schedule</div>
            <form action="${pageContext.request.contextPath}/nocturne/deletesp" method="post">
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
                
        <div id="details" style="diplay:none;" hidden>
            <input class="input-field" type="hidden" id="scheduledProgramId"  name="scheduledProgramId" hidden/>
            
            <label for="program"><span>Program Name: </span><input type="text" class="input-field" id="program"  name="program" readonly />&nbsp;<input type="button" id="programBrowse" value="..." /></label>
            <label for="presenter"><span>Presenter: </span><input type="text" class="input-field" id="presenter" name="presenter" readonly/>&nbsp;<input type="button" id="presenterBrowse" value="..." /></label>
            <label for="producer"><span>Producer: </span><input type="text" class="input-field" id="producer" name="producer" readonly/>&nbsp;<input type="button" id="producerBrowse" value="..." /></label>
            <label for="year"><span>Year: </span><input class="tel-number-field" type="text" id="year"  name="year" maxlength="4"  readonly/></label>
            <label for="week"><span>Week: </span><input class="tel-number-field" type="text" id="week"  name="week" maxlength="2" readonly/></label>
            <label for="day"><span>Day: </span><input class="tel-number-field" type="text" id="day"  name="day" maxlength="10" readonly/></label>
            <label for="startTime"><span>Start Time: </span><input class="input-field" type="text" id="startTime"  name="startTime" readonly/></label>
            <label for="endTime"><span>End Time: </span><input class="input-field" type="text" id="endTime"  name="endTime" readonly/></label>
        </div>
    </body>
</html>
