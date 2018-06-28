/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Db;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
public class login extends HttpServlet {
HttpSession session;
String id,password,fullname,email,phone,pass,nextPage,message,timestamp,gender;
MessageDigest m;
int status,level,code;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, NoSuchAlgorithmException, SQLException {
        
           session = request.getSession();
           dbConn conn = new dbConn();
           
           email = request.getParameter("email");
           pass = request.getParameter("password");
           
           //encrypt the password
             m = MessageDigest.getInstance("MD5");
            m.update(pass.getBytes(), 0, pass.length());
            password = new BigInteger(1, m.digest()).toString(16);
       //end of password encryption
       
       
       String login = "SELECT id,fullname,email,phone,level,status,timestamp,gender FROM user WHERE email=? and password=?";
       conn.pst = conn.conn.prepareStatement(login);
       conn.pst.setString(1, email);
       conn.pst.setString(2, password);
       
       conn.rs = conn.pst.executeQuery();
       if(conn.rs.next()){
        id = conn.rs.getString(1);
        fullname = conn.rs.getString(2);
        email = conn.rs.getString(3);
        phone = conn.rs.getString(4);
        level = conn.rs.getInt(5);
        status = conn.rs.getInt(6);
        timestamp = conn.rs.getString(7);
        gender = conn.rs.getString(8);
        
        
        session.setAttribute("id", id);
        session.setAttribute("fullname", fullname);
        session.setAttribute("email", email);
        session.setAttribute("phone", phone);
        session.setAttribute("level", level);
        session.setAttribute("status", status);
        session.setAttribute("timestamp", timestamp);
        session.setAttribute("gender", gender);
        if(level == 1){
         nextPage =   "Dashboard.jsp"; 
        }
        else{
        nextPage =  "Dashboard.jsp";    
        }
        
       }
       else{
           code = 0;
           message = "login failed. Wrong email and password combination.";
           nextPage = "index.jsp";
           session.setAttribute("message", message);
       }
        
        response.sendRedirect(nextPage);
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
    } catch (NoSuchAlgorithmException ex) {
        Logger.getLogger(login.class.getName()).log(Level.SEVERE, null, ex);
    } catch (SQLException ex) {
        Logger.getLogger(login.class.getName()).log(Level.SEVERE, null, ex);
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
    } catch (NoSuchAlgorithmException ex) {
        Logger.getLogger(login.class.getName()).log(Level.SEVERE, null, ex);
    } catch (SQLException ex) {
        Logger.getLogger(login.class.getName()).log(Level.SEVERE, null, ex);
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
