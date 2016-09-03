/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sg.edu.nus.iss.phoenix.presenterproducer.dao;

import java.sql.SQLException;
import java.util.List;
import sg.edu.nus.iss.phoenix.presenterproducer.entity.Presenter;

/**
 *
 * @author THIRILWIN
 */
public interface PresenterDAO {
    
    public abstract Presenter createValueObject();
    public abstract List<Presenter> loadAll() throws SQLException;
     public abstract List<Presenter> findPresenter(String presenterName) throws SQLException;
}
