/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sg.edu.nus.iss.phoenix.util;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Rong
 */
public class ValidationResult {
    public final boolean result;
    public List<String> reasons;
    
    public ValidationResult(boolean result) {
        this.result = result;
        this.reasons = new LinkedList<String>();
    }
}
