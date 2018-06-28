/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Db;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
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
public class dbsetup extends HttpServlet {
HttpSession session;
String output;
String hostname,database,user,password;
String dbconnpath,dbsetup,status,nextpage;
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        response.setContentType("text/html;charset=UTF-8");
        try{
             PrintWriter out = response.getWriter();
            
        nextpage ="";
         hostname = request.getParameter("hostname");
         database = request.getParameter("database");
         user = request.getParameter("user");
         password = request.getParameter("password");
         
       String allpath = getServletContext().getRealPath("/dbase.txt");
        String mydrive = allpath.substring(0, 1);
        //dbconnpath=mydrive+":\\MNHC_SYSTEM_APHIA_PLUS\\"; 
      dbconnpath=mydrive+":\\HSDSA\\TestStart\\DO_NOT_DELETE\\_\\_\\."; 
       
      //create a directory
      
      // new File(dbconnpath).mkdir();
     new File(dbconnpath).mkdirs();
        
        
        

    dbsetup =dbconnpath+"\\dbconnection.txt";
        
    //dbsetup=ctx.getRealPath("/dbase.txt");
        
       
        
try {     
                    FileWriter fw = new FileWriter(dbsetup);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(hostname+"\n"+database+"\n"+user+"\n"+password.trim());
			bw.close();
                        
                        
                        
    
   } catch (IOException e) {
    
    
}
   
   
   
//   System.out.println("Number of lines:========="+getLineCount(dbsetup));
   
   getLineCount(dbsetup);
        response.setContentType("text/html;charset=UTF-8");
        String url2 = "jdbc:mysql://"+hostname+"/"+database+"";
//         String url = "jdbc:mysql://localhost:3306/javabase";
Connection connection = null;
status="failed";
try {
    nextpage="index.jsp";
    System.out.println("Connecting database...");
     Class.forName("com.mysql.jdbc.Driver").newInstance();
    connection = DriverManager.getConnection(url2, user, password);
    System.out.println("Database connected!");
} catch (SQLException e) {
    nextpage="index.jsp";
    System.out.println("status : "+status+" error "+e); 
}
           
        }
        catch (FileNotFoundException nf){
            
        }
        
        response.sendRedirect(nextpage);
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
    } catch (ClassNotFoundException ex) {
        Logger.getLogger(dbsetup.class.getName()).log(Level.SEVERE, null, ex);
    } catch (InstantiationException ex) {
        Logger.getLogger(dbsetup.class.getName()).log(Level.SEVERE, null, ex);
    } catch (IllegalAccessException ex) {
        Logger.getLogger(dbsetup.class.getName()).log(Level.SEVERE, null, ex);
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
    } catch (ClassNotFoundException ex) {
        Logger.getLogger(dbsetup.class.getName()).log(Level.SEVERE, null, ex);
    } catch (InstantiationException ex) {
        Logger.getLogger(dbsetup.class.getName()).log(Level.SEVERE, null, ex);
    } catch (IllegalAccessException ex) {
        Logger.getLogger(dbsetup.class.getName()).log(Level.SEVERE, null, ex);
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

            
public  void getLineCount (String filename) throws FileNotFoundException, IOException
    {
        
        //URL url3= getClass().getResource("/db.txt");
File file = new File(dbsetup);
        
 FileInputStream fstream = new FileInputStream(file);
  // Get the object of DataInputStream
  DataInputStream in = new DataInputStream(fstream);
  BufferedReader br = new BufferedReader(new InputStreamReader(in));
  String strLine;
  //Read File Line By Line
  while ((strLine = br.readLine()) != null)   {
  // Print the content on the console
  System.out.println ("=="+strLine);
  }
  //Close the input stream
  in.close();
           
    }
    

 public void createdb(){
        try {
            
          URL location = dbConn.class.getProtectionDomain().getCodeSource().getLocation();
          String  mydrive = location.getFile().substring(1, 2);
          
            String command=mydrive+":/wamp/bin/mysql/mysql5.1.36/bin mysql -u root -p"+password+" compliance  FILE.sql";
            Runtime.getRuntime().exec(command);
        } catch (IOException ex) {
            Logger.getLogger(dbsetup.class.getName()).log(Level.SEVERE, null, ex);
        }
 
 
 }
 
}
