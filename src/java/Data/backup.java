/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Data;

import Db.OSValidator;
import TestStart.Manager;
import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.util.IOUtils;

/**
 *
 * @author GNyabuto
 */
public class backup extends HttpServlet {
String filename = "";
String database,port,host,user,password,path;
String command;
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, InterruptedException {
        Process p ;
        Manager manager = new Manager();
        
        
        command = "";
        database = request.getParameter("database");
        port = "3308";
        host = "localhost";
        user = "root";
        password = "admin";
        
        database = database.replace("_db", "");
        
        if(OSValidator.isWindows()){
        path = "C:\\Program Files\\MySQL\\MySQL Server 5.7\\bin\\";
        }
        else if(OSValidator.isUnix()){
            path="";
            user = "root";
            password = "P@ss4M&E!Fhiimpact!";
            path = "/var/lib/mysql/";
        }
       Runtime rt = Runtime.getRuntime();
//       Process p = rt.exec("C:\\Program Files\\MySQL\\MySQL Server 5.7\\bin\\mysqldump --user=root --password=admin --host=localhost excels  --host=localhost --port=3308 -uroot -padmin excels");
       if(OSValidator.isWindows()){
            command = path+"mysqldump -u"+user+" -p"+password+" --host="+host+" --port="+port+" "+database;
       }
       else if(OSValidator.isUnix()){
            command = path+"mysqldump -u"+user+" -p'"+password+"' "+database;    
       }
        System.out.println("command : "+command);
       p = rt.exec(command); 
        System.out.println(" process p : "+p);
       InputStream is=p.getInputStream();     
        
         // output
        

//        in.write(outByteStream);
//        byte [] outArray = outByteStream.toByteArray();
        byte[] outArray = IOUtils.toByteArray(is);
        response.setContentType("application/sql");
        response.setContentLength(outArray.length);
        response.setHeader("Expires:", "0"); // eliminates browser caching
        response.setHeader("Content-Disposition", "attachment; filename=TX_CURR_RRI_"+manager.getdatekey()+".sql");
        OutputStream outStream = response.getOutputStream();
        outStream.write(outArray);
        outStream.flush();   
      
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
    } catch (InterruptedException ex) {
        Logger.getLogger(backup.class.getName()).log(Level.SEVERE, null, ex);
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
    } catch (InterruptedException ex) {
        Logger.getLogger(backup.class.getName()).log(Level.SEVERE, null, ex);
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
