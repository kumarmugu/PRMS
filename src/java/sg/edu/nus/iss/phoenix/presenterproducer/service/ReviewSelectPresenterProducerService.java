/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sg.edu.nus.iss.phoenix.presenterproducer.service;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import sg.edu.nus.iss.phoenix.core.dao.DAOFactoryImpl;
import sg.edu.nus.iss.phoenix.presenterproducer.dao.PresenterDAO;
import sg.edu.nus.iss.phoenix.presenterproducer.dao.ProducerDAO;
import sg.edu.nus.iss.phoenix.presenterproducer.entity.Presenter;
import sg.edu.nus.iss.phoenix.presenterproducer.entity.Producer;

/**
 *
 * @author THIRILWIN
 */
public class ReviewSelectPresenterProducerService {
      
            DAOFactoryImpl factory;
            PresenterDAO presenterdao;
            ProducerDAO prodao;
	
	public ReviewSelectPresenterProducerService() {
		super();
		// TODO Auto-generated constructor stub
		factory = new DAOFactoryImpl();
		presenterdao = factory.getPresenterDAO();
                prodao= factory.getProducerDAO();
	}

        public List<Presenter> reviewSelectPresenter(){
            List<Presenter> data=null;
            try {
                data = presenterdao.loadAll();
            } catch (SQLException ex) {
                Logger.getLogger(ReviewSelectPresenterProducerService.class.getName()).log(Level.SEVERE, null, ex);
            }
            return data;
        }
        
        public List<Producer> reviewSelectProducer(){
            List<Producer> data=null;
            try {
                data = prodao.loadAll();
            } catch (SQLException ex) {
                Logger.getLogger(ReviewSelectPresenterProducerService.class.getName()).log(Level.SEVERE, null, ex);
            }
            return data;
        }
        
        public List<Presenter> reviewSelectPresenter(String presenterName){
            List<Presenter> data=null;
            try {
                data = presenterdao.findPresenter(presenterName);
            } catch (SQLException ex) {
                Logger.getLogger(ReviewSelectPresenterProducerService.class.getName()).log(Level.SEVERE, null, ex);
            }
            return data;
        }
        
        public List<Producer> reviewSelectProducer(String producerName){
            List<Producer> data=null;
            try {
                data = prodao.findProducer(producerName);
            } catch (SQLException ex) {
                Logger.getLogger(ReviewSelectPresenterProducerService.class.getName()).log(Level.SEVERE, null, ex);
            }
            return data;
        }
}
