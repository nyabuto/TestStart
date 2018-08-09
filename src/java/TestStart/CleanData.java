/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TestStart;

import Db.dbConn;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author GNyabuto
 */
public class CleanData extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException,SQLException {
        dbConn conn = new dbConn();
        UploadTXCURR upload = new UploadTXCURR();
       
        String getdata = "SELECT id, month_due_vl FROM tx_curr";
        conn.rs = conn.st.executeQuery(getdata);
        while(conn.rs.next()){
            String id = conn.rs.getString(1);
            String month=conn.rs.getString(2);
            if(upload.isNumeric(month)){
            String new_month = upload.numbertomonth(month);
            
            String updatemonth = "UPDATE tx_curr SET month_due_vl=? WHERE id=?";
            conn.pst = conn.conn.prepareStatement(updatemonth);
            conn.pst.setString(1, new_month);
            conn.pst.setString(2, id);
            conn.pst.executeUpdate();
            }
            else{
               // System.out.println("month: "+month+" is non numeric");
            }
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
            Logger.getLogger(CleanData.class.getName()).log(Level.SEVERE, null, ex);
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
        }  catch (SQLException ex) {
            Logger.getLogger(CleanData.class.getName()).log(Level.SEVERE, null, ex);
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
