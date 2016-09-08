/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sg.edu.nus.iss.phoenix.core.dao;

import sg.edu.nus.iss.phoenix.authenticate.dao.RoleDao;
import sg.edu.nus.iss.phoenix.authenticate.dao.UserDao;

import sg.edu.nus.iss.phoenix.presenterproducer.dao.PresenterDAO;
import sg.edu.nus.iss.phoenix.presenterproducer.dao.ProducerDAO;
import sg.edu.nus.iss.phoenix.radioprogram.dao.ProgramDAO;
import sg.edu.nus.iss.phoenix.scheduledProgram.dao.ScheduleDAO;

/**
 *
 * @author projects
 */
public interface DAOFactory {

	ProgramDAO getProgramDAO();

	RoleDao getRoleDAO();

	UserDao getUserDAO();
        
	ScheduleDAO getScheduleDAO();
        
        ProducerDAO getProducerDAO();
        PresenterDAO getPresenterDAO();
	
}
