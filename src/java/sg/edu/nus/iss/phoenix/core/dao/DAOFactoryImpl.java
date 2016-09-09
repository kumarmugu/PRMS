package sg.edu.nus.iss.phoenix.core.dao;

import sg.edu.nus.iss.phoenix.authenticate.dao.RoleDao;
import sg.edu.nus.iss.phoenix.authenticate.dao.UserDao;
import sg.edu.nus.iss.phoenix.authenticate.dao.impl.RoleDaoImpl;
import sg.edu.nus.iss.phoenix.authenticate.dao.impl.UserDaoImpl;
import sg.edu.nus.iss.phoenix.presenterproducer.dao.PresenterDAO;
import sg.edu.nus.iss.phoenix.presenterproducer.dao.ProducerDAO;
import sg.edu.nus.iss.phoenix.radioprogram.dao.ProgramDAO;
import sg.edu.nus.iss.phoenix.radioprogram.dao.impl.ProgramDAOImpl;
import sg.edu.nus.iss.phonix.presenterproducer.dao.impl.PresenterDAOImpl;
import sg.edu.nus.iss.phonix.presenterproducer.dao.impl.ProducerDAOImpl;
import sg.edu.nus.iss.phoenix.scheduledProgram.dao.ScheduleDAO;
import sg.edu.nus.iss.phoenix.scheduledProgram.dao.impl.ScheduleDAOImpl;

public class DAOFactoryImpl implements DAOFactory {
	private UserDao userDAO = new UserDaoImpl();
	private RoleDao roleDAO = new RoleDaoImpl();
	private ProgramDAO rpdao = new ProgramDAOImpl();
        private PresenterDAO rpresenterdao=new PresenterDAOImpl();
        private ProducerDAO rpProducerdao=new ProducerDAOImpl();
        private ScheduleDAO spdao = new ScheduleDAOImpl();

	@Override
	public UserDao getUserDAO() {
		// TODO Auto-generated method stub
		return userDAO;
	}

	@Override
	public RoleDao getRoleDAO() {
		// TODO Auto-generated method stub
		return roleDAO;
	}

	@Override
	public ProgramDAO getProgramDAO() {
		// TODO Auto-generated method stub
		return rpdao;
	}

    public PresenterDAO getPresenterDAO() {
        return rpresenterdao;
    }

    @Override
    public ProducerDAO getProducerDAO() {
        return rpProducerdao;
    }
	
	@Override
	public ScheduleDAO getScheduleDAO() {
		// TODO Auto-generated method stub
		return spdao;
	}
}
