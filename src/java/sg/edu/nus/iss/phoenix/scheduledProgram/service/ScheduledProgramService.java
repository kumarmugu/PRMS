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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import sg.edu.nus.iss.phoenix.authenticate.dao.UserDao;
import sg.edu.nus.iss.phoenix.authenticate.entity.User;
import sg.edu.nus.iss.phoenix.core.dao.DAOFactoryImpl;
import sg.edu.nus.iss.phoenix.core.exceptions.NotFoundException;
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
 * @author Mugunthan
 */
public class ScheduledProgramService {

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
     *
     *
     */
    public void processDelete(ProgramSlot programSlot) throws NotFoundException, SQLException {
        if (IsProgramSlotDeletable(programSlot)) {
            spDao.delete(programSlot);
            spDao.complete();
        }
    }

    // public abstract Boolean PorcessCreateAnnualSchedule(AnnualSchedule as) throws SQLException;
    public void processCreateAnnualSchedule(AnnualSchedule as) {

        try {
            ArrayList<WeeklySchedule> wsList = new ArrayList<WeeklySchedule>();
            WeeklySchedule ws;

            for (int i = 1; i < 54; i++) {
                ws = new WeeklySchedule();
                if (i == 1) {
                    ws.setStartDate(DateUtil.getFirstDayOfYear(as.getYear()));
                } else {
                    ws.setStartDate(DateUtil.getStartDateOfWeek(String.valueOf(as.getYear()), String.valueOf(i)));
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

    public void PorcessCreate(ProgramSlot srp) {
        try {
            spDao.create(srp);
            spDao.complete();
        } catch (SQLException ex) {
            // TODO Auto-generated catch block
             Logger.getLogger(ScheduledProgramService.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private boolean IsProgramSlotDeletable(ProgramSlot programSlot) {
        return true;
    }

    public void PorcessCopy(ProgramSlot newProgramSlot) throws Exception {
        ProgramSlot existingProgramSlot = getProgramSlot(newProgramSlot.getID());
        if (existingProgramSlot != null
                && existingProgramSlot.getID() != newProgramSlot.getID()) {
            throw new Exception("A Program already exists in the new timeslot.");
        }

        ValidationResult<Boolean> validation = validateProgramSlotDetail(newProgramSlot);
        if (!validation.result) {
            throw new Exception("Invalid Program Slot. " + validation.reasons.toString());
        }

        boolean isOverlapping = isProgramSlotOverlapping(newProgramSlot, null);
        if (isOverlapping) {
            throw new Exception("New time slot is overlapping with existing program slot(s). ");
        }

        try {
            /*
                spDao.setManualCommitRequired(true);
                try {
                    spDao.getScheduleForWeek(newProgramSlot.getYear(),newProgramSlot.getWeek());
                } catch (NotFoundException e) {
                    WeeklySchedule ws = new WeeklySchedule(newProgramSlot.getYear(),newProgramSlot.getWeek());
                    spDao.create(ws);
                }
                spDao.create(newProgramSlot);
                spDao.setManualCommitRequired(false);  */
            spDao.create(newProgramSlot);
            spDao.complete();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void processModify(ProgramSlot modifyingProgramSlot, ProgramSlot newProgramSlot) throws Exception {
        ProgramSlot existingProgramSlot = getProgramSlot(newProgramSlot.getID());
        if (existingProgramSlot != null
                && existingProgramSlot.getID() != newProgramSlot.getID()) {
            throw new Exception("A Program already exists in the new timeslot.");
        }

        ValidationResult<Boolean> validation = validateProgramSlotDetail(newProgramSlot);
        if (!validation.result) {
            throw new Exception("Invalid Program Slot. " + validation.reasons.toString());
        }

        boolean isOverlapping = isProgramSlotOverlapping(newProgramSlot, modifyingProgramSlot);
        if (isOverlapping) {
            throw new Exception("New time slot is overlapping with existing program slot(s). ");
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
            e.printStackTrace();
        }
    }

    public ProgramSlot getProgramSlot(long id) {
        try {
            return spDao.getProgramSlot(new Date(id));
        } catch (NotFoundException | SQLException ex) {
            return null;
        }
    }

    public ProgramSlot constructProgramSlot(HttpServletRequest req) throws Exception {

        String programName = req.getParameter("program");
        String presenter = req.getParameter("presenterId");
        String producer = req.getParameter("producerId");

        Date startDate = DateUtil.getDate(req.getParameter("date"), "yyyy-MM-dd");
        Date startTime = DateUtil.getTime(req.getParameter("startTime"), "HH:mm");
        Date endTime = DateUtil.getTime(req.getParameter("endTime"), "HH:mm");

        Date startDateTime = DateUtil.AddDateTime(startDate, startTime);
        Date endDateTime = DateUtil.AddDateTime(startDate, endTime);
        //Date duration = new Date(endDateTime.getTime() - startDateTime.getTime());

        User user = (User) req.getSession().getAttribute("user");
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
        ps.setupdatedOn(new Date(updateOn.getTime()));

        String reason = "";
        try {
            reason = "Fail to load weekly schedule.";
            WeeklySchedule ws = spDao.getScheduleForWeek(ps.getYear(), ps.getWeek());
            ps.setweekStartDate(ws.getStartDate());

            reason = "Fail to load presenter detail.";
            ps.setPresenterName(getUser(ps.getPresenterId()).getName());
            reason = "Fail to load producer detail.";
            ps.setProducerName(getUser(ps.getProducerId()).getName());
        } catch (NotFoundException | SQLException ex) {
            Logger.getLogger(ScheduledProgramService.class.getName()).log(Level.SEVERE, null, ex);
            throw new Exception(reason);
        }
        return ps;
    }

    public ValidationResult validateProgramSlotDetail(ProgramSlot ps) {
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
