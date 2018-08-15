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
public class load_sub_counties extends HttpServlet {
HttpSession session;
String output="";
String county_where = "";
int has_data=0;
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
           dbConn conn = new dbConn();
           String[] county_data = request.getParameter("county").split("_");
           
           county_where = "(";
           has_data=0;
           for(String ct:county_data){
            if(ct!=null && !ct.equals("")){
             county_where+="CountyID='"+ct+"' OR ";
             has_data++;
            }  
           }
           
           if(has_data>0){
           county_where = removeLast(county_where, 3);
           county_where+=")";
           }
           else{
            county_where = "1=2";   
           }
           
           
           
           output="<option value=\"\">Choose Subcounty</option>";
            
           String getSubCounties = "SELECT DistrictID,DistrictNom FROM district WHERE "+county_where+" ORDER BY DistrictNom";
            System.out.println("query:"+getSubCounties);
           conn.rs = conn.st.executeQuery(getSubCounties);
           while(conn.rs.next()){
               output+="<option value=\""+conn.rs.getString(1)+"\">"+conn.rs.getString(2)+"</option>";
           }
            System.out.println(output);
            
conn.rs.close();
conn.st.close(); 
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
        Logger.getLogger(load_sub_counties.class.getName()).log(Level.SEVERE, null, ex);
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
        Logger.getLogger(load_sub_counties.class.getName()).log(Level.SEVERE, null, ex);
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

     
public String removeLast(String str, int num) {
    if (str != null && str.length() > 0) {
        str = str.substring(0, str.length() - num);
    }
    return str;
    }
}
