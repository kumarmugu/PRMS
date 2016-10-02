/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sg.edu.nus.iss.phoenix.presenterproducer.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import sg.edu.nus.iss.phoenix.core.dao.DAOFactoryImpl;
import sg.edu.nus.iss.phoenix.core.exceptions.NotFoundException;
import sg.edu.nus.iss.phoenix.presenterproducer.dao.PresenterDAO;
import sg.edu.nus.iss.phoenix.presenterproducer.dao.ProducerDAO;
import sg.edu.nus.iss.phoenix.presenterproducer.entity.Presenter;
import sg.edu.nus.iss.phoenix.presenterproducer.entity.Producer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 *
 * @author THIRILWIN
 */
public class ReviewSelectPresenterProducerServiceTest {
    
    private DAOFactoryImpl factory;
    private PresenterDAO presenterDAO;
    private ProducerDAO producerDAO;
    
    ReviewSelectPresenterProducerService reviewSelectPresenterProducerService;
    
    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }
    
     /**
     * This method will execute before test case execute. This method mock the
     * PresenterDAO,ProducerDAO, DAOFactoryImpl. Also it mocks methods of PresenterDAO,ProducerDAO with
     * some outputs.
     *
     * @throws SQLException
     */
    @Before
    public void setUp() throws SQLException, NotFoundException {
        
        presenterDAO=mock(PresenterDAO.class);
        producerDAO=mock(ProducerDAO.class);
        factory = mock(DAOFactoryImpl.class);
        reviewSelectPresenterProducerService=new ReviewSelectPresenterProducerService();
        reviewSelectPresenterProducerService.factory = factory;
        reviewSelectPresenterProducerService.presenterdao=presenterDAO;
        reviewSelectPresenterProducerService.prodao=producerDAO;
        
        Presenter presenter1=new Presenter("Swarnalatha","Swarnalatha","Presenter");
        presenter1.setName("Swarnalatha");
        Presenter presenter2 = new Presenter("BoonKui","BoonKui","Presenter");
        presenter2.setName("Boon Kui");
        Presenter presenter3 = new Presenter("ISS_Thiri_Presenter","ISS_Thiri_Presenter","Presenter");
        presenter3.setName("ISS_Thiri_Presenter");
        Presenter presenter4 = new Presenter("ISS_MiSite_Presenter","ISS_MiSite_Presenter","Presenter");
        presenter4.setName("ISS_MiSite_Presenter");
        Presenter presenter5 = new Presenter("Eric","Eric","Presenter");
        presenter5.setName("Eric");
        Presenter presenter6 = new Presenter("Alvin","Alvin","Presenter");
        presenter6.setName("Alvin");
        
        Producer producer1 = new Producer("ISS_MeeZaw_Producer","ISS_MeeZaw_Producer");
        producer1.setName("ISS_MeeZaw_Producer");
        Producer producer2 = new Producer("ISS_Mugu_Producer","ISS_Mugu_Producer");
        producer2.setName("ISS_MeeZaw_Producer");
        Producer producer3 = new Producer("ISS_Zehua_Producer","ISS_Zehua_Producer");
        producer3.setName("ISS_MeeZaw_Producer");
        Producer producer4 = new Producer("David","David");
        producer4.setName("David");
        Producer producer5 = new Producer("Alice","Alice");
        producer5.setName("Alice");
        
        when(presenterDAO.getObject("Swarnalatha")).thenReturn(presenter1);
        when(presenterDAO.getObject("BoonKui")).thenReturn(presenter2);
        when(presenterDAO.getObject("ISS_Thiri_Presenter")).thenReturn(presenter3);
        when(presenterDAO.getObject("ISS_MiSite_Presenter")).thenReturn(presenter4);
        when(presenterDAO.getObject("Eric")).thenReturn(presenter5);
        when(presenterDAO.getObject("Alvin")).thenReturn(presenter6);
        
        List<Presenter> presenterList=new ArrayList<Presenter>();
        presenterList.add(presenter1);
        presenterList.add(presenter2);
        presenterList.add(presenter3);
        presenterList.add(presenter4);
        presenterList.add(presenter5);
        presenterList.add(presenter6);
        
        List<Producer> producerList=new ArrayList<Producer>();
        producerList.add(producer5);
        producerList.add(producer4);
        producerList.add(producer3);
        producerList.add(producer2);
        producerList.add(producer1);
        
        List<Producer> producerSearchList=new ArrayList<Producer>();
        producerSearchList.add(producer1);
        producerSearchList.add(producer2);
        producerSearchList.add(producer3);
        
        when(presenterDAO.loadAll()).thenReturn(presenterList);
        when(producerDAO.loadAll()).thenReturn(producerList);
        when(producerDAO.findProducer("ISS")).thenReturn(producerSearchList);
        Mockito.doThrow(new SQLException()).when(presenterDAO).findPresenter("Alice");
        
    }

    @After
    public void tearDown() {
    }
    
     /**
     * This is the testreviewSelectPresenter test method for testreviewSelectPresenter method in testreviewSelectPresenterProducerService
     * It will return all the presenters
     * @throws SQLException
     */
    
    @Test
    public void testreviewSelectPresenter() throws SQLException{
         List<Presenter> presenter= reviewSelectPresenterProducerService.reviewSelectPresenter();
         
    }


    /**
     * This is the testreviewSelectProducer test method for testreviewSelectProducer method in testreviewSelectPresenterProducerService
     * It will return all the producer
     * @throws SQLException
     */
    
    @Test
    public void testreviewSelectProducer() throws SQLException{
         List<Producer> producer= reviewSelectPresenterProducerService.reviewSelectProducer();
         
    }

    /**
     * This is the testreviewSelectProducer test method for testreviewSelectProducer method in testreviewSelectPresenterProducerService
     * It will return the List<Producer> based on the test producer name
     * @throws SQLException
     */
    
    @Test
    public void testreviewSelectProducerwithParameters() throws SQLException{
         List<Producer> producer= reviewSelectPresenterProducerService.reviewSelectProducer("ISS");
         
    }
    
    @Test
    public void testreviewSelectPresenterwithNulls() throws SQLException {
        List<Presenter> presenter = reviewSelectPresenterProducerService.reviewSelectPresenter("Alice");
        assertNull(presenter);
    }
}

