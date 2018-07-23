/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TestStart;

import java.sql.Timestamp;
import java.util.Calendar;

/**
 *
 * @author GNyabuto
 */
public class Manager {
Calendar cal = Calendar.getInstance();
int year=cal.get(Calendar.YEAR);
int month=cal.get(Calendar.MONTH)+1;
int date=cal.get(Calendar.DATE);

    public String getdatekey(){
     String datekey="";   
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    datekey = (""+timestamp).replace(" ", "").replace("-", "").replace(".", "").replace(":", "");
     
     return datekey;
    }
    
    public String today(){
        if(month>=10){
     return year+"-"+month+"-"+date;
        }
        else{
       return year+"-0"+month+"-"+date;     
        }
    }
    
  public String removeLastChars(String str, int num) {
    return str.substring(0, str.length() - num);
}
}
