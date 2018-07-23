/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Loaders;

import Db.dbConn;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author GNyabuto
 */
public class load_quarter extends HttpServlet {
    HttpSession session;
    String output,year,where;
    int prev_year;
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
           session = request.getSession();
            dbConn conn = new dbConn();
            
            year = request.getParameter("year");
            output = "<option value=\"\">Choose quarter</option>";
           prev_year = Integer.parseInt(year)-1;
           
           where = "(year="+prev_year+" AND (month=10 OR month=11 OR month=12)) OR (year="+year+" AND month<10)";
           
            String getquarters= "SELECT "+
                    "group_concat(ifnull(q1,'') separator '') AS q1," +
                    "group_concat(ifnull(q2,'') separator '') AS q2," +
                    "group_concat(ifnull(q3,'') separator '') AS q3," +
                    "group_concat(ifnull(q4,'') separator '') AS q4 "
                    + "FROM ("
                    + "SELECT "
                    + ""
                     + "CASE WHEN  year="+prev_year+" AND (month=10 OR month=11 or month=12) THEN 1 END AS q1," +
                    " CASE WHEN  year="+year+" AND (month=1 OR month=2 or month=3) THEN 2 END AS q2," +
                    " CASE WHEN  year="+year+" AND (month=4 OR month=5 or month=6) THEN 3 END AS q3," +
                    " CASE WHEN  year="+year+" AND (month=7 OR month=8 or month=9) THEN 4 END AS q4 "
                    + ""
                    + "FROM accounting_for_linkage WHERE "+where+"  " +
                   " UNION " +
                   " SELECT "
                    + ""
                    + "CASE WHEN  year="+prev_year+" AND (month=10 OR month=11 or month=12) THEN 1 END AS q1," +
                    " CASE WHEN  year="+year+" AND (month=1 OR month=2 or month=3) THEN 2 END AS q2," +
                    " CASE WHEN  year="+year+" AND (month=4 OR month=5 or month=6) THEN 3 END AS q3," +
                    " CASE WHEN  year="+year+" AND (month=7 OR month=8 or month=9) THEN 4 END AS q4 "
                    + ""
                    + "FROM art_current_net_loss WHERE "+where+" " +
                   " UNION " +
                   " SELECT "
                    + ""
                     + "CASE WHEN  year="+prev_year+" AND (month=10 OR month=11 or month=12) THEN 1 END AS q1," +
                    " CASE WHEN  year="+year+" AND (month=1 OR month=2 or month=3) THEN 2 END AS q2," +
                    " CASE WHEN  year="+year+" AND (month=4 OR month=5 or month=6) THEN 3 END AS q3," +
                    " CASE WHEN  year="+year+" AND (month=7 OR month=8 or month=9) THEN 4 END AS q4 "
                    + ""
                    + "FROM test_start_summary WHERE "+where+" ) AS year_data ";
            
            conn.rs = conn.st.executeQuery(getquarters);
            while(conn.rs.next()){
            if(!conn.rs.getString("q1").trim().equalsIgnoreCase("")){
                output+="<option value=\"1\">Oct - Dec '"+prev_year+"</option>";
            }    
            if(!conn.rs.getString("q2").trim().equalsIgnoreCase("")){
                output+="<option value=\"2\">Jan - Mar '"+year+"</option>";
            }    
            if(!conn.rs.getString("q3").trim().equalsIgnoreCase("")){
                output+="<option value=\"3\">Apr - Jun '"+year+"</option>";
            }    
            if(!conn.rs.getString("q4").trim().equalsIgnoreCase("")){
                output+="<option value=\"4\">Jul - Sep '"+year+"</option>";
            }    
            }
            
            System.out.println("query : "+getquarters);
            
            
            out.println(output);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(load_quarter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(load_quarter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
