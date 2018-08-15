/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Reports;

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
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author GNyabuto
 */
public class load_cur_art_data extends HttpServlet {
HttpSession session;
int monthlyachieved,monthlyrecounted;
int oct_17,nov_17,dec_17,jan_18,feb_18,mar_18,apr_18,may_18,jun_18,jul_18;
String where;
int has_data;
String county,sub_county,facility;
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            dbConn conn = new dbConn();
            JSONArray jarray = new JSONArray();
            JSONArray jarray1 = new JSONArray();
            JSONArray jarray2 = new JSONArray();
            
          has_data=0;  
        county = request.getParameter("county");
        sub_county = request.getParameter("sub_county");
        facility = request.getParameter("facility");
        
       
        where = "";
        if(facility==null || facility.equals("")){
            where="";
            if(sub_county==null || sub_county.equals("")){
             where="";
                if(county==null || county.equals("")){
                where="";    
                }
                else{
                   String[] county_data = request.getParameter("county").split("_"); 
                   where = " WHERE (";
                    has_data=0;
                    for(String ct:county_data){
                     if(ct!=null && !ct.equals("")){
                      where+="CountyID='"+ct+"' OR ";
                      has_data++;
                     }  
                    }

                    if(has_data>0){
                    where = removeLast(where, 3);
                    where+=")";
                    }
                    else{
                     where = "";   
                    }
                //where = " WHERE county.CountyID='"+county+"' ";
                   
                }
                
            }
            else{
            String[] sub_county_data = request.getParameter("sub_county").split("_");;
               where = " WHERE (";
           has_data=0;
           for(String sct:sub_county_data){
            if(sct!=null && !sct.equals("")){
             where+="SubCountyID='"+sct+"' OR ";
             has_data++;
            }  
           }
           
           if(has_data>0){
           where = removeLast(where, 3);
           where+=")";
           }
           else{
            where = " ";   
           } 
                System.out.println(has_data+" where : "+where);    
              // where = " WHERE district.DistrictID='"+sub_county+"' ";    
            }
        }
        else{
         String[] facility_data = request.getParameter("facility").split("_");;   
            where = " WHERE (";
           has_data=0;
           for(String fac:facility_data){
            if(fac!=null && !fac.equals("")){
             where+="SubpartnerID='"+fac+"' OR ";
             has_data++;
            }  
           }
           
           if(has_data>0){
           where = removeLast(where, 3);
           where+=")";
           }
           else{
            where = " ";   
           } 
            
            //where = " WHERE subpartnera.SubpartnerID='"+facility+"' ";   
        }
        
            
            
            
 oct_17=nov_17=dec_17=jan_18=feb_18=mar_18=apr_18=may_18=jun_18=jul_18=0;           
            
            
String getrecounted="SELECT SUM(`Active-201710`) AS 'oct_17',SUM(`Active-201711`) AS 'nov_17',SUM(`Active-201712`) AS 'dec_17'," +
"SUM(`Active-201801`) AS 'jan_18',SUM(`Active-201802`) AS 'feb_18',SUM(`Active-201803`) AS 'mar_18'," +
"SUM(`Active-201804`) AS 'apr_18',SUM(`Active-201805`) AS 'may_18',SUM(`Active-201806`) AS 'jun_18'," +
"SUM(`Active-201807`) AS 'jul_18' FROM rpt_facil_summary "+where+" ";
conn.rs = conn.st.executeQuery(getrecounted);
if(conn.rs.next()){
   oct_17 = conn.rs.getInt(1);
   nov_17 = conn.rs.getInt(2);
   dec_17 = conn.rs.getInt(3);
   jan_18 = conn.rs.getInt(4);
   feb_18 = conn.rs.getInt(5);
   mar_18 = conn.rs.getInt(6);
   apr_18 = conn.rs.getInt(7);
   may_18 = conn.rs.getInt(8);
   jun_18 = conn.rs.getInt(9);
   jul_18 = conn.rs.getInt(10);
    
    System.out.println("jan : "+jan_18);
    for(int i=1;i<=10;i++){
    jarray1.add(conn.rs.getInt(i));
    }
}
JSONObject obj2 = new JSONObject();
obj2.put("data", jarray1);
obj2.put("name", "Recounted");

//get achieved
String getreported="SELECT (SUM(oct_17)-"+oct_17+") AS 'oct_17',(SUM(nov_17)-"+nov_17+") AS 'nov_17',(SUM(dec_17)-"+dec_17+") AS 'dec_17'," +
"(SUM(jan_18)-"+jan_18+") AS 'oct_17',(SUM(feb_18)-"+feb_18+") AS 'feb_18',(SUM(mar_18)-"+mar_18+") AS 'mar_18'," +
"(SUM(apr_18)-"+apr_18+") AS 'apr_18',(SUM(may_18)-"+may_18+") AS 'may_18',(SUM(jun_18)-"+jun_18+") AS 'jun_18'," +
"(SUM(jul_18)-"+jul_18+") AS 'jul_18'FROM current_art "+where+" ";
conn.rs = conn.st.executeQuery(getreported);
            System.out.println("query: "+getreported);
if(conn.rs.next()){
    for(int i=1;i<=10;i++){
    jarray2.add(conn.rs.getInt(i));
    }
}
JSONObject obj1 = new JSONObject();
obj1.put("data", jarray2);
obj1.put("name", "Variance");


jarray.add(obj1);
jarray.add(obj2);



conn.rs.close();
conn.st.close();

            out.println(jarray);
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
        Logger.getLogger(load_cur_art_data.class.getName()).log(Level.SEVERE, null, ex);
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
        Logger.getLogger(load_cur_art_data.class.getName()).log(Level.SEVERE, null, ex);
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
