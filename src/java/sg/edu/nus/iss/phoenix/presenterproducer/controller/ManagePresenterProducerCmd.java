/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sg.edu.nus.iss.phoenix.presenterproducer.controller;

import at.nocturne.api.Action;
import at.nocturne.api.Perform;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import sg.edu.nus.iss.phoenix.presenterproducer.delegate.ReviewSelectPresenterProducerDelegate;
import sg.edu.nus.iss.phoenix.presenterproducer.entity.Presenter;
import sg.edu.nus.iss.phoenix.presenterproducer.entity.Producer;


/**
 *
 * @author THIRILWIN
 */
@Action("managepp")
public class ManagePresenterProducerCmd  implements Perform{
     @Override
    public String perform(String path, HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        // LOADALLPRESENTER , SEARCHPRESENTER , LOADALLPRODUCER , SEARCHPRODUCER
        
        List<Presenter> data1 = null;
        req.setAttribute("rps", data1);
        ReviewSelectPresenterProducerDelegate del=new ReviewSelectPresenterProducerDelegate();
        
        String actionType=req.getParameter("submittype");
        
       if(actionType !=null )
        { 
        if(actionType.equals("loadall"))
        {
            String type=req.getParameter("type");
            if(type!=null){
             switch (type) {
                 case "presenter":
                     {
                         List<Presenter> data=del.reviewSelectPresenter();
                         req.setAttribute("type", type);
                         req.setAttribute("rps", data);
                         break;
                     }
                 case "producer":
                     {
                         List<Producer> data=del.reviewSelectProducer();
                         req.setAttribute("type", type);
                         req.setAttribute("rps", data);
                         break;
                     }
                 default:
                     break;
             }
            }  
       }
        else if(actionType.equals("search"))
        {
            String inputValue=req.getParameter("inputname");
            String type=req.getParameter("searchtype");
            if(type!=null){
             switch (type) {
                 case "presenter":
                     {
                         System.out.println(inputValue);
                         List<Presenter> data=del.reviewSelectPresenter(inputValue);
                         req.setAttribute("type", type);
                         req.setAttribute("searchText", inputValue);
                         req.setAttribute("rps", data);
                         break;
                     }
                 case "producer":
                     {
                         List<Producer> data=del.reviewSelectProducer(inputValue);                         
                         req.setAttribute("type", type);
                         req.setAttribute("searchText", inputValue);
                         req.setAttribute("rps", data);
                         break;
                     }
                 default:
                     break;
             }
            }  
        }
        else{
            
        }
        }
       //if (value.equals("SEARCHPRESENTER"))
       // {
        //    List<Presenter> data=del.reviewSelectPresenter("presenter1");
        //     req.setAttribute("rps", data);
        //}
        //else if(value.equals("SEARCHPRODUCER")){
            
       // }
       // List<Producer> data=del.reviewSelectProducer("pro");
       // req.setAttribute("rps", data);
        return "/pages/reviewselectpp.jsp";
    }
    
}
