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
import org.json.simple.JSONObject;

/**
 *
 * @author GNyabuto
 */
public class register extends HttpServlet {
HttpSession session;
String fullname,email,phone,pass1,pass2,password,level,status,gender;
String message;
int code;
MessageDigest m;
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException, NoSuchAlgorithmException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
           session = request.getSession();
           dbConn conn = new dbConn();
           
           fullname = request.getParameter("fullname");
           email = request.getParameter("email");
           phone = request.getParameter("phone");
           pass1 = request.getParameter("pass1");
           pass2 = request.getParameter("pass2");
           level = request.getParameter("level");
           gender = request.getParameter("gender");
           status = "1";
           
           code=0;
           message = "";
           
           if(pass1.equals(pass2)){
            //encrypt the password
           //checker 
           String checker = "SELECT id FROM user WHERE phone=? OR email=?";
           conn.pst = conn.conn.prepareStatement(checker);
           conn.pst.setString(1, phone);
           conn.pst.setString(2, email);
           
           conn.rs = conn.pst.executeQuery();
           if(conn.rs.next()){
            code = 0;
               message = "Email or phone number has already been registered to another user.";   
           }
           else{
            m = MessageDigest.getInstance("MD5");
            m.update(pass1.getBytes(), 0, pass1.length());
            password = new BigInteger(1, m.digest()).toString(16);
            
               String adder = "INSERT INTO user(fullname,email,phone,password,level,status,gender) VALUES(?,?,?,?,?,?,?)";
               conn.pst = conn.conn.prepareStatement(adder);
               conn.pst.setString(1, fullname);
               conn.pst.setString(2, email);
               conn.pst.setString(3, phone);
               conn.pst.setString(4, password);
               conn.pst.setString(5, level);
               conn.pst.setString(6, status);
               conn.pst.setString(7, gender);
               
               int num = conn.pst.executeUpdate();
               if(num==1){
               code = 1;
               message = "User added successfully.";
               }
               else{
                 code = 0;
               message = "Operation failed";  
               }
           }
           }
           else{
               code = 0;
               message = "Passwords do not match";
           }
           
            JSONObject obj = new JSONObject();
            obj.put("code", code);
            obj.put("message", message);
            
            JSONObject data = new JSONObject();
            data.put("data", obj);
            out.println(data);
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
        Logger.getLogger(register.class.getName()).log(Level.SEVERE, null, ex);
    } catch (NoSuchAlgorithmException ex) {
        Logger.getLogger(register.class.getName()).log(Level.SEVERE, null, ex);
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
        Logger.getLogger(register.class.getName()).log(Level.SEVERE, null, ex);
    } catch (NoSuchAlgorithmException ex) {
        Logger.getLogger(register.class.getName()).log(Level.SEVERE, null, ex);
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
