/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sg.edu.nus.iss.phoenix.radioprogram.controller;

import at.nocturne.api.Action;
import at.nocturne.api.Perform;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import sg.edu.nus.iss.phoenix.radioprogram.delegate.ProgramDelegate;
import sg.edu.nus.iss.phoenix.radioprogram.delegate.ReviewSelectProgramDelegate;
import sg.edu.nus.iss.phoenix.radioprogram.entity.RPSearchObject;
import sg.edu.nus.iss.phoenix.radioprogram.entity.RadioProgram;

/**
 *
 * @author boonkui
 */
@Action("managerp")
public class ManageRadioProgramCmd implements Perform {

    @Override
    public String perform(String path, HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {

        String actionType = req.getParameter("submittype");

        System.out.println("************ Calling from scheduled Program");

        if (actionType == null) {
            ReviewSelectProgramDelegate del = new ReviewSelectProgramDelegate();
            List<RadioProgram> data = del.reviewSelectRadioProgram();
            req.setAttribute("rps", data);

            return "/pages/crudrp.jsp";
        } else {
            getRPs(req, actionType);
            return "/pages/searchrp.jsp";
        }
    }

    private void getRPs(HttpServletRequest req, String actionType) {

        if (actionType != null) {

            ProgramDelegate pdel = new ProgramDelegate();

            if (actionType.equals("loadall")) {
                {

                    List<RadioProgram> data = pdel.findAllRP();
                    req.setAttribute("source",req.getParameter("source"));
                    req.setAttribute("searchrplist", data);

                }
            } else if (actionType.equals("search")) {
                String inputrpnameValue = req.getParameter("rpnametxt");
                String inputrp_desValue = req.getParameter("rpdescriptiontxt");

                RPSearchObject rpso = new RPSearchObject(inputrpnameValue, inputrp_desValue);

                System.out.println(rpso.getName());
                List<RadioProgram> data = pdel.searchPrograms(rpso);
                req.setAttribute("rpnametxt", inputrpnameValue);
                req.setAttribute("rpdescriptiontxt", inputrp_desValue);
                req.setAttribute("searchrplist", data);

            }

        }
    }
}
