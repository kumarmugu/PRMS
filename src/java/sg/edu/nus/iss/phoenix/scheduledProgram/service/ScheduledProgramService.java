/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sg.edu.nus.iss.phoenix.scheduledProgram.service;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import sg.edu.nus.iss.phoenix.authenticate.dao.UserDao;
import sg.edu.nus.iss.phoenix.authenticate.entity.User;
import sg.edu.nus.iss.phoenix.core.dao.DAOFactoryImpl;
import sg.edu.nus.iss.phoenix.core.exceptions.NotFoundException;
import sg.edu.nus.iss.phoenix.core.exceptions.ScheduledProgramNotDeletableException;
import sg.edu.nus.iss.phoenix.presenterproducer.dao.PresenterDAO;
import sg.edu.nus.iss.phoenix.presenterproducer.dao.ProducerDAO;
import sg.edu.nus.iss.phoenix.presenterproducer.entity.Presenter;
import sg.edu.nus.iss.phoenix.presenterproducer.entity.Producer;
import sg.edu.nus.iss.phoenix.radioprogram.dao.ProgramDAO;
import sg.edu.nus.iss.phoenix.scheduledProgram.dao.ScheduleDAO;
import sg.edu.nus.iss.phoenix.scheduledProgram.entity.AnnualSchedule;
import sg.edu.nus.iss.phoenix.scheduledProgram.entity.ProgramSlot;
import sg.edu.nus.iss.phoenix.scheduledProgram.entity.WeeklySchedule;
import sg.edu.nus.iss.phoenix.util.DateUtil;
import sg.edu.nus.iss.phoenix.util.ValidationResult;

/**
 *
 * @author Mugunthan, Zehua, Mi Zaw, Thiri
 */
public class ScheduledProgramService {
    // Public DAO for Mockito to access. 
    DAOFactoryImpl factory;
    ScheduleDAO spDao;
    ProgramDAO rpDao;
    PresenterDAO preDao;
    ProducerDAO proDao;
    UserDao userDAO;

    /**
     *
     */
    public ScheduledProgramService() {
        super();
        factory = new DAOFactoryImpl();
        spDao = factory.getScheduleDAO();
        rpDao = factory.getProgramDAO();
        preDao = factory.getPresenterDAO();
        proDao = factory.getProducerDAO();
        userDAO = factory.getUserDAO();
    }

    /**
     * Process delete program slot
     *  throws NotFoundException when there is not such program slot in DB
     *  or SQLException when there is SQL connection error
     *  or ScheduledProgramNotDeletableException when the program slot can not be deleted due to business rule(s)
     */
    public void processDelete(ProgramSlot programSlot) throws NotFoundException, SQLException, ScheduledProgramNotDeletableException{
        if (IsProgramSlotDeletable(programSlot)) {
            spDao.delete(programSlot);
            spDao.complete();
        }
        else
        {
            Logger.getLogger(ScheduledProgramService.class.getName()).log(Level.SEVERE, null, "Not Allow to delete the scheduled program in past !");
            throw new ScheduledProgramNotDeletableException("Not Allow to delete the scheduled program in past !");
         
        }
    }

    /**
     * Create annual schedule, with all weekly schedule in the year
     * @param as - the annual schedule
     */
    public void processCreateAnnualSchedule(AnnualSchedule as) {

        try {
            ArrayList<WeeklySchedule> wsList = new ArrayList<WeeklySchedule>();
            WeeklySchedule ws;

            for (int i = 1; i < 54; i++) {
                ws = new WeeklySchedule();
                if (i == 1) {
                    ws.setStartDate(DateUtil.getFirstDayOfYear(as.getYear()));
                } else {
                    ws.setStartDate(DateUtil.getStartDateOfWeek(as.getYear(), i));
                }
                ws.setWeekNo(i);
                ws.setYear(as.getYear());
                wsList.add(ws);
            }

            spDao.processCreateAnnualSchedule(as, wsList);
            spDao.complete();

        } catch (SQLException ex) {
            Logger.getLogger(ScheduledProgramService.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Create Program slot
     * @param programSlot - the new program slot to be created in DB
     * @throws Exception - when validation fails or sql connection error
     */
    public void processCreate(ProgramSlot programSlot)throws Exception  {
        
           ProgramSlot existingProgramSlot = getProgramSlot(programSlot.getID());
        if (existingProgramSlot != null
                && existingProgramSlot.getID() != programSlot.getID()) {
            throw new Exception("A Program already exists in the new timeslot.");
        }

        ValidationResult<Boolean> validation = validateData(programSlot, null);
        if (!validation.result) {
            throw new Exception("Invalid Program Slot. " + validation.reasons.toString());
        }

        try {
            
            spDao.create(programSlot);
            spDao.complete();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private boolean IsProgramSlotDeletable(ProgramSlot programSlot) {
         Date currentTime = new Date();
         System.out.println("Program end time " + programSlot.getEndTime().getTime() + " - CurrentTime " +  currentTime.getTime()+
                 programSlot);
         if( programSlot.getEndTime().getTime() > currentTime.getTime()){
             System.out.println("Program end time is less than currentTime");
             return true;
         }
         return false;
         
    }
    /**
     * Copy Program slot, similar to Create program slot 
     * @param newProgramSlot - the new program slot to be created in DB
     * @throws Exception - when validation fails or sql connection error
     */
    public void processCopy(ProgramSlot newProgramSlot) throws Exception {
        processCreate(newProgramSlot); // reuse. same logic applies
    }

    /**
     * Modify Program slot
     * @param modifyingProgramSlot - the old program slot is modifying 
     * @param newProgramSlot - the new program slot to be created in DB
     * @throws Exception - when validation fails or sql connection error
     */
    public void processModify(ProgramSlot modifyingProgramSlot, ProgramSlot newProgramSlot) throws Exception {
        ProgramSlot existingProgramSlot = getProgramSlot(newProgramSlot.getID());
        if (existingProgramSlot != null
                && existingProgramSlot.getID() != newProgramSlot.getID()) {
            throw new Exception("A Program already exists in the new timeslot.");
        }

        ValidationResult<Boolean> validation = validateData(newProgramSlot, modifyingProgramSlot);
        if (!validation.result) {
            throw new Exception("Invalid Program Slot. " + validation.reasons.toString());
        }

        try {
            if (modifyingProgramSlot.getID() == newProgramSlot.getID()) {
                spDao.update(newProgramSlot);
                spDao.complete();
            } else {
                spDao.setManualCommitRequired(true);
                spDao.delete(modifyingProgramSlot);
                /*
                try {
                    spDao.getScheduleForWeek(newProgramSlot.getYear(),newProgramSlot.getWeek());
                } catch (NotFoundException e) {
                    WeeklySchedule ws = new WeeklySchedule(newProgramSlot.getYear(),newProgramSlot.getWeek());
                    spDao.create(ws);
                }     
                 */
                spDao.create(newProgramSlot);
                spDao.complete();
            }

        } catch (NotFoundException | SQLException e) {
            // TODO Auto-generated catch block
            throw e;
        }
    }

    /**
     * Validate Program slot data from two aspects
     *  1. entity rules - mainly focus on new program slot 
     *  2. business rules
     * @param newProgramSlot - new program slot
     * @param modifyingProgramSlot - the old program slot
     * @return Validation result as boolean.
     * @throws Exception upon failure
     */
    private ValidationResult<Boolean> validateData(ProgramSlot newProgramSlot,ProgramSlot modifyingProgramSlot) throws Exception {
        ValidationResult<Boolean> validation = validateProgramSlotDetail(newProgramSlot);
        if (!validation.result) {
            return validation;
        }

        boolean isOverlapping = isProgramSlotOverlapping(newProgramSlot, modifyingProgramSlot);
        if (isOverlapping) {
            validation = new ValidationResult(false);
            validation.reasons.add("New time slot is overlapping with existing program slot(s). ");
            return validation;
        }     
        return new ValidationResult(true);
    } 
    
    /**
     * Get program slot with specific ID
     * @param id - program slot ID == program slot start date time
     * @return program slot found or null when not found;
     */
    public ProgramSlot getProgramSlot(long id) {
        try {
            return spDao.getProgramSlot(new Date(id));
        } catch (NotFoundException | SQLException ex) {
            return null;
        }
    }

    /**
     * Construct program slot with parameter map
     * @param params - program slot parameter map
     * @param user - the user who creating this 
     * @return - program slot object
     * @throws Exception - when parameters are insufficient or incorrect
     */
    public ProgramSlot constructProgramSlot(Map<String, String[]> params, User user) throws Exception {

        String programName = params.get("program")[0];//req.getParameter("program");
        String presenter = params.get("presenterId")[0]; //req.getParameter("presenterId");
        String producer = params.get("producerId")[0];//req.getParameter("producerId");

        Date startDate = DateUtil.getDate(params.get("date")[0], "yyyy-MM-dd");
        Date startTime = DateUtil.getTime(params.get("startTime")[0], "HH:mm");
        Date endTime = DateUtil.getTime(params.get("endTime")[0], "HH:mm");

        Date startDateTime = DateUtil.AddDateTime(startDate, startTime);
        Date endDateTime = DateUtil.AddDateTime(startDate, endTime);
        //Date duration = new Date(endDateTime.getTime() - startDateTime.getTime());

        //User user = (User) req.getSession().getAttribute("user");
        if (user == null) {
            throw new Exception("User Session not found.");
        }
        String updateBy = user.getId();//req.getParameter("updateBy");
        Date updateOn = new Date();

        ProgramSlot ps = new ProgramSlot(startDateTime, endDateTime, programName);
        //ps.setDuration(duration);
        ps.setProducerId(producer);
        ps.setPresenterId(presenter);
        ps.setupdatedBy(updateBy);
        ps.setProgramName(programName);
        ps.setupdatedOn(new Date(updateOn.getTime()));

        String reason = "";
        try {
            reason = "Fail to load weekly schedule.";
            WeeklySchedule ws = spDao.getScheduleForWeek(ps.getYear(), ps.getWeek());
            ps.setweekStartDate(ws.getStartDate());
            
            reason="Program Name is Empty";
            
            if(ps.getProgramName() == null || ps.getProgramName().equals("")){
                 throw new NotFoundException(reason);
            }
            reason = "Presenter Name is Empty.";
            ps.setPresenterName(getUser(ps.getPresenterId()).getName());
            reason = "Producer Name is Empty.";
            ps.setProducerName(getUser(ps.getProducerId()).getName());
        } catch (NotFoundException | SQLException ex) {
            Logger.getLogger(ScheduledProgramService.class.getName()).log(Level.SEVERE, null, ex);
            throw new Exception(reason);
        }
        return ps;
    }

    /**
     * Validate Program slot detail
     *  - check radio program name
     *  - check presenter name
     *  - check producer name
     *  - check updater name
     *  - check updating time vs creation time
     * @param ps - the program slot to be validated
     * @return validation result
     */
    public ValidationResult<Boolean> validateProgramSlotDetail(ProgramSlot ps) {
        ValidationResult<Boolean> valdiation = ps.valdiate();
        if (valdiation.result == false) {
            return valdiation;
        }
        String reason = "";
        try {
            reason = "Radio Program Not Found.";
            rpDao.getObject(ps.getProgramName());

            reason = "Presenter Not Found.";
            Presenter presenter = getPresenter(ps.getPresenterId());

            reason = "Producer Not Found.";
            Producer producer = getProducer(ps.getProducerId());

            reason = "UpdatedBy User Not Found.";
            userDAO.getObject(ps.getupdatedBy());
            reason = "Can not update past scheduled program.";
            boolean isUpdatingTimeValid = ps.getupdatedOn().getTime() < ps.getStartTime().getTime();
            if (isUpdatingTimeValid == false) {
                valdiation = new ValidationResult(false);
                valdiation.reasons.add(reason);
                return valdiation;
            }
            valdiation = new ValidationResult(true);
        } catch (NotFoundException | SQLException ex) {
            valdiation = new ValidationResult(false);
            valdiation.reasons.add(reason);
            if (ex.getMessage()!= null && !ex.getMessage().isEmpty())
                valdiation.reasons.add(ex.getMessage());
            Logger.getLogger(ScheduledProgramService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return valdiation;
    }

    /**
     * Checking if the new program slot will be overlapping with existing program slots
     * @param newPs - the new program slot 
     * @param oldPs - the old program slot, to be excluded from checking
     * @return true when there is overlapping, false for not overlapping. 
     */
    public boolean isProgramSlotOverlapping(ProgramSlot newPs, ProgramSlot oldPs) {
        if (newPs == null) {
            return false;
        }
        WeeklySchedule ws;

        try {
            ws = spDao.getScheduleForWeek(newPs.getYear(), newPs.getWeek());
            ws = spDao.loadAllScheduleForWeek(ws);
            String day = newPs.getDay();
            for (ProgramSlot eps : ws.getProgramSlots()) {
                if ((oldPs == null || eps.getID() != oldPs.getID())
                        && day.equals(eps.getDay())) {
                    boolean isNotOverlapping = newPs.getEndTime().getTime() <= eps.getStartTime().getTime()
                            || newPs.getStartTime().getTime() >= eps.getEndTime().getTime();
                    if (!isNotOverlapping) {
                        return true;
                    }
                }
            }
        } catch (NotFoundException | SQLException ex) {
            Logger.getLogger(ScheduledProgramService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    private User getUser(String theID) throws NotFoundException {
        try {
            return userDAO.getObject(theID);
        } catch (SQLException ex) {
            throw new NotFoundException(ex.getMessage());
        }
    }

    private Presenter getPresenter(String theID) throws NotFoundException {
        User user = null;
        try {
            user = getUser(theID);
            if (user == null) {
                throw new NotFoundException("Presenter ID " + theID + " not found.");
            }

            return preDao.findPresenter(user.getName()).get(0);
        } catch (Exception ex) {
            throw new NotFoundException("Presenter " + user.getName() + " not found.");
        }
    }

    private Producer getProducer(String theID) throws NotFoundException {
        User user = null;
        try {
            user = getUser(theID);
            if (user == null) {
                throw new NotFoundException("Producer ID " + theID + " not found.");
            }
            return proDao.findProducer(user.getName()).get(0);
        } catch (Exception ex) {
            throw new NotFoundException("Producer " + user.getName() + " not found.");
        }
    }

}
