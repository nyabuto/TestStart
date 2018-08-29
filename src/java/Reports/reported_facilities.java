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

/**
 *
 * @author GNyabuto
 */
public class reported_facilities extends HttpServlet {
HttpSession session;
String output,prev_county_reported,prev_subcounty_reported,reported,notreported;
String prev_county_notreported,prev_subcounty_notreported;
int isreported = 0,counter_reported,counter_notreported;

int has_data;
String highv;
String county,sub_county,facility,where;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
           session = request.getSession();
           dbConn conn = new dbConn();
           
           
                       
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
      where="WHERE 1=1 ";
  }
           
           
           
           reported="<div class=\"col-12 col-md-6\"><div style=\"font-weight:900; font-size:24px;\">Reported Sites</div>";
           notreported="<div class=\"col-12 col-md-6\"><div style=\"font-weight:900; font-size:24px;\">Not Reported Sites</div>";
           output="";
           String query = "SELECT \n" +
            "county.County AS County,\n" +
            "district.DistrictNom AS SubCounty,\n" +
            "subpartnera.SubPartnerNom AS Facility,\n" +
            "COUNT(DISTINCT(CASE WHEN subpartnera.active=1 && subpartnera.ART=1 THEN CentreSanteId END)) AS all_sites,\n" +
            "COUNT(DISTINCT(CASE WHEN subpartnera.active=1 && subpartnera.ART=1 THEN tx_curr.mflcode END)) AS reported_sites\n" +
            " \n" +
            "FROM subpartnera \n" +
            "LEFT JOIN tx_curr ON tx_curr.mflcode=subpartnera.CentreSanteId \n" +
            "LEFT JOIN district ON district.DistrictID=subpartnera.DistrictID \n" +
            "LEFT JOIN county on district.CountyID=county.CountyID "+where+" AND ART=1 && subpartnera.active=1 GROUP BY CentreSanteId \n" +
            "ORDER BY County,SubCounty,Facility";
            conn.rs = conn.st.executeQuery(query);
            counter_reported=counter_notreported=0;
            prev_county_reported=prev_subcounty_reported=prev_county_notreported=prev_subcounty_notreported="";
            while(conn.rs.next()){
            isreported = conn.rs.getInt(5);
            
               switch (isreported) {
                   case 1:
                       counter_reported++;
                       if(!prev_county_reported.equals(conn.rs.getString(1))){
                           reported+="<b><u>"+conn.rs.getString(1)+" County</u></b><br>" ;
                       }        
                       if(!prev_subcounty_reported.equals(conn.rs.getString(2))){
                           reported+="<b>"+conn.rs.getString(2)+"</b><br>" ;
                       }        
                       reported+=counter_reported+". "+conn.rs.getString(3)+"<br>" ;
                       prev_county_reported = conn.rs.getString(1);
                       prev_subcounty_reported=conn.rs.getString(2);
                       break;
                   case 0:
                       counter_notreported++;
                       if(!prev_county_notreported.equals(conn.rs.getString(1))){
                           notreported+="<b><u>"+conn.rs.getString(1)+" County</u></b><br>" ;
                       }        
                       if(!prev_subcounty_notreported.equals(conn.rs.getString(2))){
                           notreported+="<b>"+conn.rs.getString(2)+"</b><br>" ;
                       }        
                       notreported+=counter_notreported+". "+conn.rs.getString(3)+"<br>" ;
                       prev_county_notreported = conn.rs.getString(1);
                       prev_subcounty_notreported=conn.rs.getString(2);
                       break;
                   default:
                       break;
               }
             }
            
                    
          reported+="</div>";          
          notreported+="</div>";          
         output+="<div>"+reported+""+notreported+"</div>";           
                    
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
        Logger.getLogger(reported_facilities.class.getName()).log(Level.SEVERE, null, ex);
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
        Logger.getLogger(reported_facilities.class.getName()).log(Level.SEVERE, null, ex);
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
