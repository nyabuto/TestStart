/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Reports;

import Db.dbConn;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSetMetaData;
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
public class load_curr_totals extends HttpServlet {
HttpSession session;
int has_data;
String highv;
String county,sub_county,facility,where;
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
          dbConn conn = new dbConn();
          session = request.getSession();
            JSONObject obj = new JSONObject();
            JSONArray jarray = new JSONArray();
            
            
        highv = request.getParameter("highv");
        county = request.getParameter("county");
        sub_county = request.getParameter("sub_county");
        facility = request.getParameter("facility");
        
       
        where = " WHERE high_volume="+highv+" ";
        if(facility==null || facility.equals("")){
            where=" WHERE high_volume="+highv+" ";
            if(sub_county==null || sub_county.equals("")){
             where=" WHERE high_volume="+highv+" ";
                if(county==null || county.equals("")){
                where=" WHERE high_volume="+highv+" ";    
                }
                else{
                   String[] county_data = request.getParameter("county").split("_"); 
                   where = " WHERE high_volume="+highv+" AND (";
                    has_data=0;
                    for(String ct:county_data){
                     if(ct!=null && !ct.equals("")){
                      where+="county.CountyID='"+ct+"' OR ";
                      has_data++;
                     }  
                    }

                    if(has_data>0){
                    where = removeLast(where, 3);
                    where+=")";
                    }
                    else{
                     where = " WHERE high_volume="+highv+" ";   
                    }
                //where = " WHERE county.CountyID='"+county+"' ";
                   
                }
                
            }
            else{
            String[] sub_county_data = request.getParameter("sub_county").split("_");;
               where = " WHERE high_volume="+highv+" AND (";
           has_data=0;
           for(String sct:sub_county_data){
            if(sct!=null && !sct.equals("")){
             where+="district.DistrictID='"+sct+"' OR ";
             has_data++;
            }  
           }
           
           if(has_data>0){
           where = removeLast(where, 3);
           where+=")";
           }
           else{
            where = " WHERE  high_volume="+highv+" ";   
           } 
                System.out.println(has_data+" where : "+where);    
              // where = " WHERE district.DistrictID='"+sub_county+"' ";    
            }
        }
        else{
         String[] facility_data = request.getParameter("facility").split("_");;   
            where = " WHERE  high_volume="+highv+" AND (";
           has_data=0;
           for(String fac:facility_data){
            if(fac!=null && !fac.equals("")){
             where+="subpartnera.SubpartnerID='"+fac+"' OR ";
             has_data++;
            }  
           }
           
           if(has_data>0){
           where = removeLast(where, 3);
           where+=")";
           }
           else{
            where = " WHERE high_volume="+highv+" ";   
           } 
            
            //where = " WHERE subpartnera.SubpartnerID='"+facility+"' ";   
        }
        
            

  if(where.contains("high_volume=1")){
      where = where.replace("high_volume=1", "all_highvolume=1");
  }
  else if(where.contains("high_volume=0")){
      where = where.replace("high_volume=0", "all_highvolume IS NULL");
  }
  else if(where.contains("high_volume=2")){
      where = where.replace("high_volume=2", " 1=1 ");
  }    
     
  else{
      where="";
  }
                        String getdata="SELECT  \n" +
            "          COUNT(DISTINCT(CASE WHEN subpartnera.active=1 && subpartnera.ART=1 THEN CentreSanteId END)) AS all_sites, \n" +
            "          COUNT(DISTINCT(CASE WHEN subpartnera.active=1 && subpartnera.ART=1 THEN tx_curr.mflcode END)) AS reported_sites, \n" +
            "          COUNT(id) AS files_reviewed, \n" +
            "          COUNT(CASE WHEN tx_curr.jul_18='Active' THEN 1 END) AS active, \n" +
            "          COUNT( \n" +
            "          CASE WHEN  \n" +
            "          tx_curr.month_due_vl='Nov-17' || tx_curr.month_due_vl='Nov-2017' ||  \n" +
            "          tx_curr.month_due_vl='Dec-17' || tx_curr.month_due_vl='Dec-2017' ||  \n" +
            "          tx_curr.month_due_vl='Jan-18' || tx_curr.month_due_vl='Jan-2018' ||  \n" +
            "          tx_curr.month_due_vl='Feb-18' || tx_curr.month_due_vl='Feb-2018' ||  \n" +
            "          tx_curr.month_due_vl='Mar-18' || tx_curr.month_due_vl='Mar-2018' ||  \n" +
            "          tx_curr.month_due_vl='Apr-18' || tx_curr.month_due_vl='Apr-2018' ||  \n" +
            "          tx_curr.month_due_vl='May-18' || tx_curr.month_due_vl='May-2018' ||  \n" +
            "          tx_curr.month_due_vl='Jun-18' || tx_curr.month_due_vl='Jun-2018' ||  \n" +
            "          tx_curr.month_due_vl='Jul-18' || tx_curr.month_due_vl='Jul-2018' ||  \n" +
            "          tx_curr.month_due_vl='Aug-18' || tx_curr.month_due_vl='Aug-2018' ||  \n" +
            "          tx_curr.month_due_vl='Sep-18' || tx_curr.month_due_vl='Sep-2018' ||  \n" +
            "          tx_curr.month_due_vl='Oct-18' || tx_curr.month_due_vl='Oct-2018' \n" +
            "          THEN 1 END) AS due_vl, \n" +
            "          COUNT(CASE WHEN tx_curr.jul_18='Defaulters' || tx_curr.jul_18='Defaulter' THEN 1 END) AS defaulters, \n" +
            "          COUNT(CASE WHEN tx_curr.jul_18='LTFU' THEN 1 END) AS ltfu, \n" +
            "          COUNT(CASE WHEN tx_curr.jul_18='TO' || tx_curr.jul_18='T.O' THEN 1 END) AS transferred_out, \n" +
            "          COUNT( \n" +
            "          CASE WHEN  \n" +
            "          tx_curr.oct_17='Dead' || tx_curr.nov_17='Dead' ||  \n" +
            "          tx_curr.dec_17='Dead' || tx_curr.jan_18='Dead' ||  \n" +
            "          tx_curr.feb_18='Dead' || tx_curr.mar_18='Dead' ||  \n" +
            "          tx_curr.apr_18='Dead' || tx_curr.may_18='Dead' ||  \n" +
            "          tx_curr.jun_18='Dead' || tx_curr.jul_18='Dead' \n" +
            "          THEN 1 END) AS dead, \n" +
            "        COUNT( \n" +
            "        CASE WHEN  \n" +
            "        ((( \n" +
            "        tx_curr.jun_18='Stopped' || tx_curr.jun_18='Stopped ' )  \n" +
            "        AND tx_curr.jul_18='') ||  \n" +
            "        tx_curr.jul_18='Stopped' || tx_curr.jul_18='Stopped ') \n" +
            "        THEN 1 END) AS stopped,  \n" +
            "        COUNT(CASE WHEN (tx_curr.jul_18='Pending verification') THEN 1 END) AS pending_v,\n" +
            "        COUNT( \n" +
            "          CASE WHEN tx_curr.jul_18='N/A' \n" +
            "          THEN 1 END) AS no_status\n" +
            "          FROM subpartnera  \n" +
            "          LEFT JOIN tx_curr ON tx_curr.mflcode=subpartnera.CentreSanteId \n" +
            "          LEFT JOIN district ON district.DistrictID=subpartnera.DistrictID  \n" +
            "          LEFT JOIN county on district.CountyID=county.CountyID\n" +
            "           "+where;

            conn.rs = conn.st.executeQuery(getdata);
        ResultSetMetaData  metaData = conn.rs.getMetaData();
        int col_count = metaData.getColumnCount(); //number of column
            if(conn.rs.next()){
                for(int i=1;i<=col_count;i++){
             jarray.add(conn.rs.getInt(i));
                }
            }
            obj.put("data", jarray);
            System.out.println("obj : "+obj);   
            
            out.println(obj);
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
        Logger.getLogger(load_curr_totals.class.getName()).log(Level.SEVERE, null, ex);
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
        Logger.getLogger(load_curr_totals.class.getName()).log(Level.SEVERE, null, ex);
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

    
     
    public boolean isNumeric(String s) {  
        return s != null && s.matches("[-+]?\\d*\\.?\\d+");  
    }
    
    public boolean isEvenlyDivisable(int a, int b) {
    return a % b == 0;
    }
    
    public String removeLast(String str, int num) {
    if (str != null && str.length() > 0) {
        str = str.substring(0, str.length() - num);
    }
    return str;
    }
}
