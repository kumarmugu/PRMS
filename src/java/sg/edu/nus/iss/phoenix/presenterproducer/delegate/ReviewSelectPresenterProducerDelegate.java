/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sg.edu.nus.iss.phoenix.presenterproducer.delegate;

import java.util.List;
import sg.edu.nus.iss.phoenix.presenterproducer.entity.Presenter;
import sg.edu.nus.iss.phoenix.presenterproducer.entity.Producer;
import sg.edu.nus.iss.phoenix.presenterproducer.service.ReviewSelectPresenterProducerService;


/**
 *
 * @author THIRILWIN
 * @ This is the ReviewSelectPresenterProducer Delegate
 */
public class ReviewSelectPresenterProducerDelegate {
    private ReviewSelectPresenterProducerService service;
    
    public ReviewSelectPresenterProducerDelegate(){
        
        service=new ReviewSelectPresenterProducerService();
    }
    
    public List<Presenter> reviewSelectPresenter(){
        return service.reviewSelectPresenter();
    }
    
    public List<Producer> reviewSelectProducer(){
        return service.reviewSelectProducer();
    }
    
    public List<Presenter> reviewSelectPresenter(String PresenterName){
        return service.reviewSelectPresenter(PresenterName);
    }
    
    public List<Producer> reviewSelectProducer(String ProducerName){
    return service.reviewSelectProducer(ProducerName);
    }
    
    
}
