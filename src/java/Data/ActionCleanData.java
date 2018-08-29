/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Data;

import Db.dbConn;
import java.sql.SQLException;

/**
 *
 * @author GNyabuto
 */
public class ActionCleanData {
 String lastknownstatus="";
String[] month_labels = {"oct_17","nov_17","dec_17","jan_18","feb_18","mar_18","apr_18","may_18","jun_18","jul_18"};
String id,query;
int months_counter,recordscounter,added;
dbConn conn = new dbConn();
    
 public int clean_data() throws SQLException{
      String getRecords = "SELECT id,ccc_no,is_ti,oct_17,nov_17, dec_17,jan_18,feb_18, mar_18,apr_18,may_18,jun_18, jul_18, month_due_vl \n" +
"        from tx_curr LEFT JOIN subpartnera ON tx_curr.mflcode=subpartnera.CentreSanteID  \n" +
"        LEFT JOIN district ON district.DistrictID=subpartnera.DistrictID  \n" +
"        LEFT JOIN county on district.CountyID=county.CountyID \n" +
"        \n" +
"        where \n" +
"        \n" +
"        (tx_curr.nov_17='' ||  \n" +
"        tx_curr.dec_17='' || tx_curr.jan_18='' ||  \n" +
"        tx_curr.feb_18='' || tx_curr.mar_18='' || \n" +
"        tx_curr.apr_18='' || tx_curr.may_18='' ||  \n" +
"        tx_curr.jun_18='' || tx_curr.jul_18='')\n" +
"            \n" +
"        GROUP BY tx_curr.id";
            
            conn.rs = conn.st.executeQuery(getRecords);
            while(conn.rs.next()){
                added=0;
                
            id = conn.rs.getString("id");
             query = "UPDATE tx_curr SET ";
             months_counter=0;
             
             for(String label:month_labels){
              if(months_counter==0) {
                  lastknownstatus = conn.rs.getString(label);
              }
              else{
                if(conn.rs.getString(label).equals("")){
                  query+=label+"='"+lastknownstatus+"',"; 
                  added++;
                }
                else{
                  lastknownstatus = conn.rs.getString(label);
                }
              }
              months_counter++;   
             }
             
             
             query = removeLast(query, 1);
             
             query+=" WHERE id='"+id+"'";
             
             if(added>0){
             recordscounter++;
                System.out.println(recordscounter+". "+query);
                conn.st1.executeUpdate(query);
             }
            }
           
            clean_data_new_TI();
    return recordscounter;        
 } 
 
 public int clean_data_new_TI() throws SQLException{
      String getRecords = "SELECT id,ccc_no,is_ti,oct_17,nov_17, dec_17,jan_18,feb_18, mar_18,apr_18,may_18,jun_18, jul_18 \n" +
"        from tx_curr LEFT JOIN subpartnera ON tx_curr.mflcode=subpartnera.CentreSanteID  \n" +
"        LEFT JOIN district ON district.DistrictID=subpartnera.DistrictID  \n" +
"        LEFT JOIN county on district.CountyID=county.CountyID \n" +
"        \n" +
"        where \n" +
"        \n" +
"        (tx_curr.nov_17='' ||  \n" +
"        tx_curr.dec_17='' || tx_curr.jan_18='' ||  \n" +
"        tx_curr.feb_18='' || tx_curr.mar_18='' || \n" +
"        tx_curr.apr_18='' || tx_curr.may_18='' ||  \n" +
"        tx_curr.jun_18='' || tx_curr.jul_18='')\n" +
"            \n" +
"        GROUP BY tx_curr.id";
            
            conn.rs = conn.st.executeQuery(getRecords);
            while(conn.rs.next()){
                added=0;
                
            id = conn.rs.getString("id");
             query = "UPDATE tx_curr SET ";
             months_counter=0;
             
             for(String label:month_labels){
              
                if(conn.rs.getString(label).equals("")){
                  query+=label+"='N/A',"; 
                  added++;
                }
                
              }
              months_counter++;   
             
             query = removeLast(query, 1);
             
             query+=" WHERE id='"+id+"'";
             
             if(added>0){
             recordscounter++;
                System.out.println(recordscounter+". "+query);
                conn.st1.executeUpdate(query);
             }
            }
            
    return recordscounter;        
 } 
 
 
  public String removeLast(String str, int num) {
    if (str != null && str.length() > 0) {
        str = str.substring(0, str.length() - num);
    }
    return str;
    }
}
