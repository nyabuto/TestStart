/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TestStart;

import Db.dbConn;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import static org.apache.poi.ss.usermodel.CellType.NUMERIC;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
/**
 *
 * @author GNyabuto
 */
@MultipartConfig
public class UploadExcels extends HttpServlet {
  String full_path="";
  String fileName="";
  File file_source;
  HttpSession session;
  private static final long serialVersionUID = 205242440643911308L;
  private static final String UPLOAD_DIR = "uploads";
SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
int row_skipped,rows_added,skipped_info;
String yearmonth="",year="",month="";
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
            session = request.getSession();
            dbConn conn = new dbConn();
      
       Sheet  worksheetAcc4Link = null;
       Sheet worksheetTestStart = null;
       Sheet worksheetARTLoss = null;
       
            JSONObject finalobj = new JSONObject();
            JSONObject objacc4link = new JSONObject();
            JSONObject objteststart = new JSONObject();
            JSONObject objartcurr = new JSONObject();

            
            JSONArray finalarray = new JSONArray();
            JSONArray jarray = new JSONArray();
       
            
        String applicationPath = request.getServletContext().getRealPath("");
         String uploadFilePath = applicationPath + File.separator + UPLOAD_DIR;
         session=request.getSession();
          File fileSaveDir = new File(uploadFilePath);
        if (!fileSaveDir.exists()) {
            fileSaveDir.mkdirs();
        }
        
        for (Part part : request.getParts()) {
            JSONObject obj_files = new JSONObject();
            if(!getFileName(part).equals("")){
           fileName = getFileName(part);
            part.write(uploadFilePath + File.separator + fileName);
            
            full_path=fileSaveDir.getAbsolutePath()+"\\"+fileName;
            System.out.println("fullpath : "+full_path);
           // read the contents of the workbook and sheets here
           
           FileInputStream fileInputStream = new FileInputStream(full_path);
            
           obj_files.put("file_name", fileName);
           
//****************************WORKBOOK INFORMATION****************************************
        //Call to read all sheets.
              
       
       //call sheet for accounting for linkages--validation
       
       Workbook workbook = null;
       if(fileName.endsWith(".xlsx")){
       workbook = new XSSFWorkbook(fileInputStream);
       }
       else if(fileName.endsWith(".xls")){
       workbook = new HSSFWorkbook(fileInputStream);
       }
       else{
        break;   
       }
//       HSSFSheet worksheetAccValid = workbook.getSheet("Acc for Linkage -Validation");
        if(workbook.getSheet("1b. Accounting for Linkage")!=null){
       worksheetAcc4Link = workbook.getSheet("1b. Accounting for Linkage");
       objacc4link = Accounting4LinkageSummary(worksheetAcc4Link,conn);
       jarray.add(objacc4link);
        }
        
        if(workbook.getSheet("2b.Test & Start -Cohort Summary")!=null){
       worksheetTestStart = workbook.getSheet("2b.Test & Start -Cohort Summary");
       objteststart = TestStartSummary(worksheetTestStart,conn);
       jarray.add(objteststart);
        }
        
        if(workbook.getSheet("3b. ART Current Net Loss-Var ")!=null){
       worksheetARTLoss = workbook.getSheet("3b. ART Current Net Loss-Var ");
       objartcurr = ARTCurrentLoss(worksheetARTLoss,conn);  
       jarray.add(objartcurr); 
        }
//        Account4LinkageValidationXLS(worksheetAccValid,conn);

        //end of reading all sheets
 
        }
   
       obj_files.put("upload_info", jarray);
       
       finalarray.add(obj_files);
        }

        
        System.out.println("errors : "+finalarray);
        session.setAttribute("upload_errors", finalarray);
      
      
      response.sendRedirect("ExcelUpload.jsp");
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
          Logger.getLogger(UploadExcels.class.getName()).log(Level.SEVERE, null, ex);
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
          Logger.getLogger(UploadExcels.class.getName()).log(Level.SEVERE, null, ex);
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

    private String getFileName(Part part) {
            String file_name="";
        String contentDisp = part.getHeader("content-disposition");
//        System.out.println("content-disposition header= "+contentDisp);
        String[] tokens = contentDisp.split(";");
      
        for (String token : tokens) {
            if (token.trim().startsWith("filename")) {
                file_name = token.substring(token.indexOf("=") + 2, token.length()-1);
              break;  
            }
            
        }
//         System.out.println("content-disposition final : "+file_name);
        return file_name;
    }
    public String removeLast(String str, int num) {
    if (str != null && str.length() > 0) {
        str = str.substring(0, str.length() - num);
    }
    return str;
    }

    public JSONObject GetFacilityDetails(dbConn conn, String mflcode) throws SQLException{
        JSONObject obj = new JSONObject();
        String ct="",sub_ct="",facil="";
         String getFacilDetails="SELECT County, DistrictNom,SubPartnerNom " +
            "FROM  subpartnera join district on subpartnera.DistrictID=district.DistrictID " +
            "join county on county.CountyID=district.CountyID " +
            "where subpartnera.CentreSanteId=?";
            conn.pst2 = conn.conn.prepareStatement(getFacilDetails);
            conn.pst2.setString(1, mflcode);
            conn.rs2 = conn.pst2.executeQuery();
            if(conn.rs2.next()){
              ct = conn.rs2.getString(1);
              sub_ct = conn.rs2.getString(2);
              facil = conn.rs2.getString(3);
              
              obj.put("county", ct);
              obj.put("sub_county", sub_ct);
              obj.put("facility", facil);
             }
        
        return obj;
    }
    public String GetYearMonth(String  date){
        String yearmonth="";
        String[] date_array = date.split("-");
        yearmonth = date_array[0]+""+date_array[1];
        
        return yearmonth;
    }
    public JSONObject CleanPeriod(String year,String month, dbConn conn) throws SQLException{
    JSONObject period_data = new JSONObject();
    String yearmonth;
        System.out.println("year : "+year+" month: "+year);  
        
            String getmonth = "SELECT code FROM months WHERE shortname LIKE '%"+month+"%' OR fullname LIKE '%"+month+"%' ";
            System.out.println("yearmonth query: "+getmonth);
     conn.rs = conn.st.executeQuery(getmonth);
     if(conn.rs.next()){
         month = conn.rs.getString(1);
     }
     else{
         month="00";
     }
     yearmonth = year+""+month;
     
     
     period_data.put("yearmonth", yearmonth);
     period_data.put("year", year);
     period_data.put("month", month);
     
            return period_data;
    }

    public JSONObject Accounting4LinkageSummary (Sheet worksheet, dbConn conn) throws SQLException{
        String all_error_details = "<thead class=\"thead-dark\"><tr><th>Row Number</th><th>County</th><th>Sub-County</th><th>Health Facility</th><th>MFL Code</th><th>Date confirmed HIV positive</th><th>Gender</th><th>Current Age</th><th>Documented in linkage register</th><th>Patient CCC Number</th><th>Enrolment Date</th><th>Referred</th><th>Enrolled to other site</th><th>Enrolled from other site</th><th>ART start date</th><th>Started ART in this facility</th><th>Started in other facility</th><th>Patient Status</th><th>If declined, indicate reason</th><th>If dead, indicate reported  cause of death</th><th>Reason for Failing to Upload</th></tr></thead><tbody>";
        String id="",period_details="",county="",sub_county="",facility="",mfl_code="",date_confirmed_hiv_pos="",gender="",current_age="",documented_linkage_register="",ccc_no="",enrollment_date="",referred="",enrolled_to_other_site="",enrolled_from_other_site="",art_start_date="",started_art_in_this_facility="",started_art_in_other_facility="",patient_status="",declined_reason="",reported_cause_of_death="",reason="";
        JSONObject obj_det = new JSONObject();
         String error_details="",period_error = "Error While loading <b>1b. Accounting for Linkage</b> Sheet: <br>";
        boolean has_error=false;
        
        Iterator rowIterator = worksheet.iterator();

//*******************************YearMonth************************

        int i=5,y=0,skipped_records=0,added_records=0;
        while(rowIterator.hasNext()){
            error_details = reason = "";
            has_error=false;
        
            yearmonth=year=month=id=period_details=county=sub_county=facility=mfl_code=date_confirmed_hiv_pos=gender=current_age=documented_linkage_register=ccc_no=enrollment_date=referred=enrolled_to_other_site=enrolled_from_other_site=art_start_date=started_art_in_this_facility=started_art_in_other_facility=patient_status=declined_reason=reported_cause_of_death="";

             Row rowi = worksheet.getRow(i);
                if(rowi==null){
                 break;
                }

// **************************************County***********************
            Cell cell0 = rowi.getCell((short) 0);
              if(cell0!=null){
            switch (cell0.getCellType()) {
                   case 0:
                       //numeric
                       county =""+(int)cell0.getNumericCellValue();
                       break;
                   case 1:
                       //string
                       county =cell0.getStringCellValue();
                       break;
                  
               }
        }
            error_details+="<td>"+county+"</td>";
//*******************************End of county************************
               
// **************************************Sub county***********************
            Cell cell1 = rowi.getCell((short) 1);
              if(cell1!=null){
            switch (cell1.getCellType()) {
                   case 0:
                       //numeric
                       sub_county =""+(int)cell1.getNumericCellValue();
                       break;
                   case 1:
                       //string
                       sub_county =cell1.getStringCellValue();
                       break;
                   
               }
        }
            error_details+="<td>"+sub_county+"</td>";
//*******************************End of sub-county************************

// **************************************Facility Name***********************
            Cell cell2 = rowi.getCell((short) 2);
              if(cell2!=null){
            switch (cell2.getCellType()) {
                   case 0:
                       //numeric
                       facility =""+(int)cell2.getNumericCellValue();
                       break;
                   case 1:
                       //string
                       facility =cell2.getStringCellValue();
                       break;
                   
               }
        }
            error_details+="<td>"+facility+"</td>";
//*******************************End of facility name************************;
               
// **************************************mflcode***********************
            Cell cell3 = rowi.getCell((short) 3);
              if(cell3!=null){
            switch (cell3.getCellType()) {
                   case 0:
                       //numeric
                       mfl_code =""+(int)cell3.getNumericCellValue();
                       break;
                   case 1:
                       //string
                       mfl_code =cell3.getStringCellValue();
                       break;
               }
            }
             else{
                has_error=true;
            }
            if(mfl_code.equals("")){
               has_error=true;
               reason+="Missing MFL Code <br>";
            }
            error_details+="<td>"+mfl_code+"</td>";
//*******************************End of mflcode************************

// **************************************date confirmed HIV pos***********************
        if(rowi.getCell(4)!=null){
        if(rowi.getCell(4).getCellType()==0){
          date_confirmed_hiv_pos =""+dateformat.format(rowi.getCell(4).getDateCellValue());
        }
        else{
           if (rowi.getCell(4).getCellTypeEnum().equals(NUMERIC)) {
            date_confirmed_hiv_pos = dateformat.format(rowi.getCell(4).getDateCellValue());
        }
        else{
         date_confirmed_hiv_pos = rowi.getCell(4).getStringCellValue();
        }
    }
        }
        
            error_details+="<td>"+date_confirmed_hiv_pos+"</td>";
            
            if(!date_confirmed_hiv_pos.equals("")){
            date_confirmed_hiv_pos = format_date(date_confirmed_hiv_pos);
            get_year_month(date_confirmed_hiv_pos); //get year month
        }
//*******************************End of date confirmed HIV pos************************
               
// **************************************gender***********************
            Cell cell5 = rowi.getCell((short) 5);
              if(cell5!=null){
            switch (cell5.getCellType()) {
                   case 0:
                       //numeric
                       gender =""+(int)cell5.getNumericCellValue();
                       break;
                   case 1:
                       //string
                       gender =cell5.getStringCellValue();
                       break;
               }
              }
            error_details+="<td>"+gender+"</td>";
//*******************************End of gender************************

               
// **************************************current age***********************
            Cell cell6 = rowi.getCell((short) 6);
              if(cell6!=null){
            switch (cell6.getCellType()) {
                   case 0:
                       //numeric
                       current_age =""+(int)cell6.getNumericCellValue();
                       break;
                   case 1:
                       //string
                       current_age =cell6.getStringCellValue();
                       break;
                   
               }
              }
            error_details+="<td>"+current_age+"</td>";
//*******************************End of current age************************
             
// **************************************documented in linked register***********************
            Cell cell7 = rowi.getCell((short) 7);
            if(cell7!=null){
            switch (cell7.getCellType()) {
                   case 0:
                       //numeric
                       documented_linkage_register =""+(int)cell7.getNumericCellValue();
                       break;
                   case 1:
                       //string
                       documented_linkage_register =cell7.getStringCellValue();
                       break;
                  
               }
            }
            error_details+="<td>"+documented_linkage_register+"</td>";
//*******************************End of documented in linked register************************
               
// **************************************patient ccc number***********************
            Cell cell8 = rowi.getCell((short) 8);
            if(cell8!=null){
            switch (cell8.getCellType()) {
                   case 0:
                       //numeric
                       ccc_no =""+(int)cell8.getNumericCellValue();
                       break;
                   case 1:
                       //string
                       ccc_no =cell8.getStringCellValue();
                       break;
                  
               }
            }
             else{
                has_error=true;
            }
//            if(ccc_no.equals("")){
//               has_error=true;  
//               reason+="Missing CCC Number <br>";
//            }
            error_details+="<td>"+ccc_no+"</td>";
//*******************************End of patient ccc number************************


// **************************************enrollment date***********************
    if(rowi.getCell(9)!=null){
  if(rowi.getCell(9).getCellType()==0){
  enrollment_date = ""+dateformat.format(rowi.getCell(9).getDateCellValue());
    }
    else{
           if (rowi.getCell(9).getCellTypeEnum().equals(NUMERIC)) {
            enrollment_date = dateformat.format(rowi.getCell(9).getDateCellValue());
        }
        else{
         enrollment_date = rowi.getCell(9).getStringCellValue();
        }
}
        }
            error_details+="<td>"+enrollment_date+"</td>";
                
            if(!enrollment_date.equals("")){
            enrollment_date = format_date(enrollment_date);
        }
//*******************************End of enrollment date************************


// **************************************referred***********************
            Cell cell10 = rowi.getCell((short) 10);
            if(cell10!=null){
            switch (cell10.getCellType()) {
                   case 0:
                       //numeric
                       referred =""+(int)cell10.getNumericCellValue();
                       break;
                   case 1:
                       //string
                       referred =cell10.getStringCellValue();
                       break;
                   
               } }
            error_details+="<td>"+referred+"</td>";
//*******************************End of referred************************


// **************************************linked to other sites***********************
            Cell cell11 = rowi.getCell((short) 11);
            if(cell11!=null){
            switch (cell11.getCellType()) {
                   case 0:
                       //numeric
                       enrolled_to_other_site =""+(int)cell11.getNumericCellValue();
                       break;
                   case 1:
                       //string
                       enrolled_to_other_site =cell11.getStringCellValue();
                       break;
                   
               } }
            error_details+="<td>"+enrolled_to_other_site+"</td>";
//*******************************End of linked to other site************************


// **************************************linked from other sites***********************
            Cell cell12 = rowi.getCell((short) 12);
            if(cell12!=null){
            switch (cell12.getCellType()) {
                   case 0:
                       //numeric
                       enrolled_from_other_site =""+(int)cell12.getNumericCellValue();
                       break;
                   case 1:
                       //string
                       enrolled_from_other_site =cell12.getStringCellValue();
                       break;
                  
               }}
            error_details+="<td>"+enrolled_from_other_site+"</td>";
//*******************************End of linked from other sites************************

// **************************************art start date***********************
    if(rowi.getCell(13)!=null){
     if(rowi.getCell(13).getCellType()==0){
  art_start_date =""+dateformat.format(rowi.getCell(13).getDateCellValue());
    }
    else{
           if (rowi.getCell(13).getCellTypeEnum().equals(NUMERIC)) {
            art_start_date = dateformat.format(rowi.getCell(13).getDateCellValue());
        }
        else{
         art_start_date = rowi.getCell(13).getStringCellValue();
        }
}
    }
            error_details+="<td>"+art_start_date+"</td>";   
                
            if(!art_start_date.equals("")){
            art_start_date = format_date(art_start_date);
        }
//*******************************End of art start date************************


// **************************************started art in this facility***********************
            Cell cell14 = rowi.getCell((short) 14);
        if(cell14!=null){
            switch (cell14.getCellType()) {
                   case 0:
                       //numeric
                       started_art_in_this_facility =""+(int)cell14.getNumericCellValue();
                       break;
                   case 1:
                       //string
                       started_art_in_this_facility =cell14.getStringCellValue();
                       break;
                   
               }}
            error_details+="<td>"+started_art_in_this_facility+"</td>";   
//*******************************End of started art in this facility************************


// **************************************started art in other facility***********************
            Cell cell15 = rowi.getCell((short) 15);
            if(cell15!=null){
            switch (cell15.getCellType()) {
                   case 0:
                       //numeric
                       started_art_in_other_facility =""+(int)cell15.getNumericCellValue();
                       break;
                   case 1:
                       //string
                       started_art_in_other_facility =cell15.getStringCellValue();
                       break;
                   
               }}
            error_details+="<td>"+started_art_in_other_facility+"</td>"; 
//*******************************End of started art in other facility************************


// **************************************patient status***********************
            Cell cell16 = rowi.getCell((short) 16);
            if(cell16!=null){
            switch (cell16.getCellType()) {
                   case 0:
                       //numeric
                       patient_status =""+(int)cell16.getNumericCellValue();
                       break;
                   case 1:
                       //string
                       patient_status =cell16.getStringCellValue();
                       break;
                   
               }}
            error_details+="<td>"+patient_status+"</td>"; 
//*******************************End of patient status************************


// **************************************if declined indicate reason***********************
            Cell cell17 = rowi.getCell((short) 17);
            if(cell17!=null){
            switch (cell17.getCellType()) {
                   case 0:
                       //numeric
                       declined_reason =""+(int)cell17.getNumericCellValue();
                       break;
                   case 1:
                       //string
                       declined_reason =cell17.getStringCellValue();
                       break;
                   
               }}
            error_details+="<td>"+declined_reason+"</td>"; 
//*******************************End of if declined indicate reason************************


// **************************************reported cause of death***********************
            Cell cell18 = rowi.getCell((short) 18);
            if(cell18!=null){
            switch (cell18.getCellType()) {
                   case 0:
                       //numeric
                       reported_cause_of_death =""+(int)cell18.getNumericCellValue();
                       break;
                   case 1:
                       //string
                       reported_cause_of_death =cell18.getStringCellValue();
                       break;
                   
               }
            }
            error_details+="<td>"+reported_cause_of_death+"</td>"; 
//*******************************End of reported cause of death************************

//     generate id
        if(!ccc_no.equals("")){
            id = mfl_code+"_"+date_confirmed_hiv_pos.replace("-", "")+"_"+ccc_no;
        }
        else{
         id = mfl_code+"_"+date_confirmed_hiv_pos.replace("-", "")+"_"+gender;   
        }

      //       END OF READING VALUES
    if(has_error){
      all_error_details+="<tr><td>"+i+"</td>"+error_details+"<td>"+reason+"</td></tr>"; 
      skipped_records++;
    }
    else{
        added_records++;
            //GET CORRECT COUNTY, SUBCOUNTY DATA FOR THIS FACILITY
            JSONObject obj_facil = GetFacilityDetails(conn, mfl_code);
            
            if(obj_facil.containsKey("county")){
              county = obj_facil.get("county").toString();
              sub_county = obj_facil.get("sub_county").toString();
              facility = obj_facil.get("facility").toString();

               String query = "REPLACE INTO accounting_for_linkage SET id=?,year=?,month=?,county=?,sub_county=?,facility=?,mfl_code=?,date_confirmed_hiv_pos=?,gender=?,current_age=?,documented_linkage_register=?,ccc_no=?,enrollment_date=?,referred=?,enrolled_to_other_site=?,enrolled_from_other_site=?,art_start_date=?,started_art_in_this_facility=?,started_art_in_other_facility=?,patient_status=?,declined_reason=?,reported_cause_of_death=?,yearmonth=?";
                conn.pst = conn.conn.prepareStatement(query);
                conn.pst.setString(1, id);
                conn.pst.setString(2, year);
                conn.pst.setString(3, month);
                conn.pst.setString(4, county);
                conn.pst.setString(5, sub_county);
                conn.pst.setString(6, facility);
                conn.pst.setString(7, mfl_code);
                conn.pst.setString(8, date_confirmed_hiv_pos);
                conn.pst.setString(9, gender);
                conn.pst.setString(10, current_age);
                conn.pst.setString(11, documented_linkage_register);
                conn.pst.setString(12, ccc_no);
                conn.pst.setString(13, enrollment_date);
                conn.pst.setString(14, referred);
                conn.pst.setString(15, enrolled_to_other_site);
                conn.pst.setString(16, enrolled_from_other_site);
                conn.pst.setString(17, art_start_date);
                conn.pst.setString(18, started_art_in_this_facility);
                conn.pst.setString(19, started_art_in_other_facility);
                conn.pst.setString(20, patient_status);
                conn.pst.setString(21, declined_reason);
                conn.pst.setString(22, reported_cause_of_death);
                conn.pst.setString(23, yearmonth);
                conn.pst.executeUpdate();
            
//                System.out.println("query : "+conn.pst);
            conn.pst.executeUpdate();
           }
            else{
               
            }
        }
//***************************************************************************
        i++;
        }
               
     obj_det.put("added", added_records);
     obj_det.put("skipped", skipped_records);
     obj_det.put("skipped_details", all_error_details+"</tbody>");
     obj_det.put("sheetname", "1b. Accounting for Linkage");
     
        
      
      return obj_det;
    }
    public JSONObject TestStartSummary (Sheet worksheet, dbConn conn) throws SQLException{
        String all_error_details="<thead class=\"thead-dark\"><tr><th>Row Number</th><th>County</th><th>Subcounty</th><th>Health Facility</th><th>MFL Code</th><th>Patient CCC Number</th><th>Gender</th><th>Current Age</th><th>Date confirmed HIV Positive</th><th>Enrollment date</th><th>ART start date</th><th>Baseline WHO Stage</th><th>Baseline CD4 Count or Percent</th><th>Initial VL Result</th><th>Initial Vl Date</th><th>Repeat VL  result</th><th>Repeat VL date</th><th>VL result at 12 months</th><th>12 month Vl Date</th><th>Last visit date</th><th>Patient Outcome</th><th>Reason for Failing to Upload</th></tr></thead><tbody>";
      String id="",county="",sub_county="",facility="",mfl_code="",ccc_no="",gender="",current_age="",date_confirmed_hiv_pos="",enrollment_date="",art_start_date="",baseline_who_stage="",baseline_cd4_cell_count_perc="",initial_vl="",initial_vl_date="",repeat_vl_value="",repeat_vl_date="",vl_12_months_value="",vl_12_months_date="",last_visit_date="",patient_outcome="",reason="";
       JSONObject obj_det = new JSONObject();
        String error_details="",period_error = "Error While loading <b>2b.Test & Start -Cohort Summary</b> Sheet: <br>";
        boolean has_error=false;
        
        Iterator rowIterator = worksheet.iterator();

        
        int i=5,y=0,skipped_records=0,added_records=0;
        while(rowIterator.hasNext()){
            error_details = reason = "";
            has_error=false;
        
            yearmonth=year=month=id=county=sub_county=facility=mfl_code=ccc_no=gender=current_age=date_confirmed_hiv_pos=enrollment_date=art_start_date=baseline_who_stage=baseline_cd4_cell_count_perc=initial_vl=initial_vl_date=repeat_vl_value=repeat_vl_date=vl_12_months_value=vl_12_months_date=last_visit_date=patient_outcome="";
      
             Row rowi = worksheet.getRow(i);
                if(rowi==null){
                 break;
                }

// **************************************County***********************
            Cell cell0 = rowi.getCell((short) 0);
            if(cell0!=null){
            switch (cell0.getCellType()) {
                   case 0:
                       //numeric
                       county =""+(int)cell0.getNumericCellValue();
                       break;
                   case 1:
                       //string
                       county =cell0.getStringCellValue();
                       break;
                  
               }
            }
            
            error_details+="<td>"+county+"</td>";
//*******************************End of county************************
               
// **************************************Sub county***********************
            Cell cell1 = rowi.getCell((short) 1);
            if(cell1!=null){
            switch (cell1.getCellType()) {
                   case 0:
                       //numeric
                       sub_county =""+(int)cell1.getNumericCellValue();
                       break;
                   case 1:
                       //string
                       sub_county =cell1.getStringCellValue();
                       break;
                   
               }
            }
             
            error_details+="<td>"+sub_county+"</td>";
//*******************************End of sub-county************************

// **************************************Facility Name***********************
            Cell cell2 = rowi.getCell((short) 2);
            if(cell2!=null){
            switch (cell2.getCellType()) {
                   case 0:
                       //numeric
                       facility =""+(int)cell2.getNumericCellValue();
                       break;
                   case 1:
                       //string
                       facility =cell2.getStringCellValue();
                       break;
                   
               }
            }
            
            error_details+="<td>"+facility+"</td>";
//*******************************End of facility name************************;
               
// **************************************mflcode***********************
            Cell cell3 = rowi.getCell((short) 3);
            if(cell3!=null){
            switch (cell3.getCellType()) {
                   case 0:
                       //numeric
                       mfl_code =""+(int)cell3.getNumericCellValue();
                       break;
                   case 1:
                       //string
                       mfl_code =cell3.getStringCellValue();
                       break;
               }
            }
             else{
                has_error=true;
            }
            if(mfl_code.equals("")){
               has_error=true; 
               reason+="Missing MFL Code <br>";
            }
            error_details+="<td>"+mfl_code+"</td>";
//*******************************End of mflcode************************

// **************************************CCC Number***********************
        
            Cell cell4 = rowi.getCell((short) 4);
            if(cell4!=null){
            switch (cell4.getCellType()) {
                   case 0:
                       //numeric
                       ccc_no =""+(int)cell4.getNumericCellValue();
                       break;
                   case 1:
                       //string
                       ccc_no =cell4.getStringCellValue();
                       break;
               }
            }
             else{
                has_error=true;
            }
            if(ccc_no.equals("")){
               has_error=true;  
               reason+="Missing MFL Code <br>";
            }
            error_details+="<td>"+ccc_no+"</td>";
//*******************************End of ccc number************************
               
// **************************************gender***********************
            Cell cell5 = rowi.getCell((short) 5);
            if(cell5!=null){
            switch (cell5.getCellType()) {
                   case 0:
                       //numeric
                       gender =""+(int)cell5.getNumericCellValue();
                       break;
                   case 1:
                       //string
                       gender =cell5.getStringCellValue();
                       break;
               }
            }
            
            error_details+="<td>"+gender+"</td>";
//*******************************End of gender************************

               
// **************************************current age***********************
            Cell cell6 = rowi.getCell((short) 6);
            if(cell6!=null){
            switch (cell6.getCellType()) {
                   case 0:
                       //numeric
                       current_age =""+(int)cell6.getNumericCellValue();
                       break;
                   case 1:
                       //string
                       current_age =cell6.getStringCellValue();
                       break;
                   
               }
            }
             error_details+="<td>"+current_age+"</td>";
//*******************************End of current age************************
             
// **************************************date confirmed HIV+***********************
           if(rowi.getCell(7)!=null){
if(rowi.getCell(7).getCellType()==0){
          date_confirmed_hiv_pos =""+dateformat.format(rowi.getCell(7).getDateCellValue());
        }
        else{
           if (rowi.getCell(7).getCellTypeEnum().equals(NUMERIC)) {
            date_confirmed_hiv_pos = dateformat.format(rowi.getCell(7).getDateCellValue());
        }
        else{
         date_confirmed_hiv_pos = rowi.getCell(7).getStringCellValue();
        }
    }
           }
            error_details+="<td>"+date_confirmed_hiv_pos+"</td>";
             if(!date_confirmed_hiv_pos.equals("")){
            date_confirmed_hiv_pos = format_date(date_confirmed_hiv_pos);
            get_year_month(date_confirmed_hiv_pos); //get year month
        }
//*******************************End of DATE CONFIRMED hiv pos************************
               
// **************************************enrollment date***********************
if(rowi.getCell(8)!=null){
          if(rowi.getCell(8).getCellType()==0){
          enrollment_date =""+dateformat.format(rowi.getCell(8).getDateCellValue());
        }
        else{
           if (rowi.getCell(8).getCellTypeEnum().equals(NUMERIC)) {
            enrollment_date = dateformat.format(rowi.getCell(8).getDateCellValue());
        }
        else{
         enrollment_date = rowi.getCell(8).getStringCellValue();
        }
    }
 }
 error_details+="<td>"+enrollment_date+"</td>";
  if(!enrollment_date.equals("")){
            enrollment_date = format_date(enrollment_date);
 }
//*******************************End of enrollment date************************


// **************************************ART Start date***********************
if(rowi.getCell(9)!=null){
  if(rowi.getCell(9).getCellType()==0){
  art_start_date =""+dateformat.format(rowi.getCell(9).getDateCellValue()); 
    }
    else{
           if (rowi.getCell(9).getCellTypeEnum().equals(NUMERIC)) {
            art_start_date = dateformat.format(rowi.getCell(9).getDateCellValue());
        }
        else{
         art_start_date = rowi.getCell(9).getStringCellValue();
        }
    }
}
 error_details+="<td>"+art_start_date+"</td>";
  if(!art_start_date.equals("")){
            art_start_date = format_date(art_start_date);
 }
//*******************************End of art start date************************

// ************************************** baseline WHO stage ***********************
            Cell cell10 = rowi.getCell((short) 10);
            if(cell10!=null){
            switch (cell10.getCellType()) {
                   case 0:
                       //numeric
                       baseline_who_stage =""+(int)cell10.getNumericCellValue();
                       break;
                   case 1:
                       //string
                       baseline_who_stage =cell10.getStringCellValue();
                       break;
                   
               }
            }
             error_details+="<td>"+baseline_who_stage+"</td>";
//*******************************End of baseline WHO stage************************


// **************************************baseline cd4 cell count percentage***********************
System.out.println("at pos : "+i+" mflcode : "+mfl_code);
            Cell cell11 = rowi.getCell((short) 11);
            if(cell11!=null){
            switch (cell11.getCellType()) {
                   case 0:
                       //numeric
                       baseline_cd4_cell_count_perc =""+(int)cell11.getNumericCellValue();
                       break;
                   case 1:
                       //string
                       baseline_cd4_cell_count_perc =cell11.getStringCellValue();
                       break;
                   
               }
            }
             error_details+="<td>"+baseline_cd4_cell_count_perc+"</td>";
//*******************************End of baseline cd4 cell count percentage************************


// **************************************initial vl***********************
            Cell cell12 = rowi.getCell((short) 12);
            if(cell12!=null){
            switch (cell12.getCellType()) {
                   case 0:
                       //numeric
                       initial_vl =""+(int)cell12.getNumericCellValue();
                       break;
                   case 1:
                       //string
                       initial_vl =cell12.getStringCellValue();
                       break;
                  
               }
            }
             error_details+="<td>"+initial_vl+"</td>";
//*******************************End of initial vl************************


// **************************************art start date***********************
if(rowi.getCell(13)!=null){
     if(rowi.getCell(13).getCellType()==0){
  initial_vl_date =""+dateformat.format(rowi.getCell(13).getDateCellValue()); 
    }
    else{
           if (rowi.getCell(13).getCellTypeEnum().equals(NUMERIC)) {
            initial_vl_date = dateformat.format(rowi.getCell(13).getDateCellValue());
        }
        else{
         initial_vl_date = rowi.getCell(13).getStringCellValue();
        }
    }
}      
 error_details+="<td>"+initial_vl_date+"</td>";
 
  if(!initial_vl_date.equals("")){
      initial_vl_date = format_date(initial_vl_date);
 }
 
//*******************************End of art start date************************


// **************************************repeat vl results***********************
            Cell cell14 = rowi.getCell((short) 14);
            if(cell14!=null){
            switch (cell14.getCellType()) {
                   case 0:
                       //numeric
                       repeat_vl_value =""+(int)cell14.getNumericCellValue();
                       break;
                   case 1:
                       //string
                       repeat_vl_value =cell14.getStringCellValue();
                       break;
                   
               }
            }
             error_details+="<td>"+repeat_vl_value+"</td>";
//*******************************End of repeat vl results************************


// **************************************repeat vl date***********************
        if(rowi.getCell(15)!=null) {  
          if(rowi.getCell(15).getCellType()==0){
      repeat_vl_date =""+dateformat.format(rowi.getCell(15).getDateCellValue());  
        }
        else{
               if (rowi.getCell(15).getCellTypeEnum().equals(NUMERIC)) {
                repeat_vl_date = dateformat.format(rowi.getCell(15).getDateCellValue());
            }
            else{
             repeat_vl_date = rowi.getCell(15).getStringCellValue();
            }
    }
        }
         error_details+="<td>"+repeat_vl_date+"</td>";
         
          if(!repeat_vl_date.equals("")){
            repeat_vl_date = format_date(repeat_vl_date);
 }
//*******************************End of repeat vl date************************

// **************************************vl results at 12 months***********************
            Cell cell16 = rowi.getCell((short) 16);
            if(cell16!=null){
            switch (cell16.getCellType()) {
                   case 0:
                       //numeric
                       vl_12_months_value =""+(int)cell16.getNumericCellValue();
                       break;
                   case 1:
                       //string
                       vl_12_months_value =cell16.getStringCellValue();
                       break;
                   
               }
            }
             error_details+="<td>"+vl_12_months_value+"</td>";
//*******************************End of vl results at 12 months************************


// **************************************12 months vl date***********************
        if(rowi.getCell(17)!=null){
    if(rowi.getCell(17).getCellType()==0){
           vl_12_months_date =""+dateformat.format(rowi.getCell(17).getDateCellValue());
             }
             else{
                if (rowi.getCell(17).getCellTypeEnum().equals(NUMERIC)) {
                 vl_12_months_date = dateformat.format(rowi.getCell(17).getDateCellValue());
             }
             else{
              vl_12_months_date = rowi.getCell(17).getStringCellValue();
             }
     }
        }
         error_details+="<td>"+vl_12_months_date+"</td>";
         
          if(!vl_12_months_date.equals("")){
            vl_12_months_date = format_date(vl_12_months_date);
 }
//*******************************End of 12 months vl date************************


// **************************************last vl date***********************
    if(rowi.getCell(18)!=null){
                 if(rowi.getCell(18).getCellType()==0){
            last_visit_date =""+dateformat.format(rowi.getCell(18).getDateCellValue());
              }
              else{
                     if (rowi.getCell(18).getCellTypeEnum().equals(NUMERIC)) {
                      last_visit_date = dateformat.format(rowi.getCell(18).getDateCellValue());
                  }
                  else{
                   last_visit_date = rowi.getCell(18).getStringCellValue();
                  }
          }
    }
     error_details+="<td>"+last_visit_date+"</td>";
     
      if(!last_visit_date.equals("")){
            last_visit_date = format_date(last_visit_date);
 }
//*******************************End of last vl date************************

// **************************************patient outcomes***********************
            Cell cell19 = rowi.getCell((short) 19);
            if(cell19!=null){
            switch (cell19.getCellType()) {
                   case 0:
                       //numeric
                       patient_outcome =""+(int)cell19.getNumericCellValue();
                       break;
                   case 1:
                       //string
                       patient_outcome =cell19.getStringCellValue();
                       break;
                   
               }
            }
             error_details+="<td>"+patient_outcome+"</td>";
//*******************************End of patient outcomes************************


//     generate id

            id = mfl_code+"_"+yearmonth+"_"+ccc_no;

      //       END OF READING VALUES
    if(has_error){
      all_error_details+="<tr><td>"+i+"</td>"+error_details+"<td>"+reason+"</td></tr>"; 
      skipped_records++;
    }
    else{
        added_records++;
            //GET CORRECT COUNTY, SUBCOUNTY DATA FOR THIS FACILITY
            JSONObject obj_facil = GetFacilityDetails(conn, mfl_code);
            
            if(obj_facil.containsKey("county")){
              county = obj_facil.get("county").toString();
              sub_county = obj_facil.get("sub_county").toString();
              facility = obj_facil.get("facility").toString();

                String query = "REPLACE INTO test_start_summary SET id=?,year=?,month=?,county=?,sub_county=?,facility=?,mfl_code=?,ccc_no=?,gender=?,current_age=?,date_confirmed_hiv_pos=?,enrollment_date=?,art_start_date=?,baseline_who_stage=?,baseline_cd4_cell_count_perc=?,initial_vl=?,initial_vl_date=?,repeat_vl_value=?,repeat_vl_date=?,vl_12_months_value=?,vl_12_months_date=?,last_visit_date=?,patient_outcome=?,yearmonth=?";
                conn.pst = conn.conn.prepareStatement(query);
                conn.pst.setString(1, id);
                conn.pst.setString(2, year);
                conn.pst.setString(3, month);
                conn.pst.setString(4, county);
                conn.pst.setString(5, sub_county);
                conn.pst.setString(6, facility);
                conn.pst.setString(7, mfl_code);
                conn.pst.setString(8, ccc_no);
                conn.pst.setString(9, gender);
                conn.pst.setString(10, current_age);
                conn.pst.setString(11, date_confirmed_hiv_pos);
                conn.pst.setString(12, enrollment_date);
                conn.pst.setString(13, art_start_date);
                conn.pst.setString(14, baseline_who_stage);
                conn.pst.setString(15, baseline_cd4_cell_count_perc);
                conn.pst.setString(16, initial_vl);
                conn.pst.setString(17, initial_vl_date);
                conn.pst.setString(18, repeat_vl_value);
                conn.pst.setString(19, repeat_vl_date);
                conn.pst.setString(20, vl_12_months_value);
                conn.pst.setString(21, vl_12_months_date);
                conn.pst.setString(22, last_visit_date);
                conn.pst.setString(23, patient_outcome);
                conn.pst.setString(24, yearmonth);
                conn.pst.executeUpdate();
            
//                System.out.println("query test n start______ : "+conn.pst);
           conn.pst.executeUpdate();
           }
            else{
               
            }
        }
//***************************************************************************
        i++;
        }
               
     obj_det.put("added", added_records);
     obj_det.put("skipped", skipped_records);
     obj_det.put("skipped_details", all_error_details+"</tbody>");
     obj_det.put("sheetname", "2b.Test & Start -Cohort Summary");
     
     return obj_det;
    }
    public JSONObject ARTCurrentLoss (Sheet worksheet, dbConn conn) throws SQLException{
    String all_error_details = "<thead class=\"thead-dark\"><tr><th>Row Number</th><th>County</th><th>Sub County</th><th>Health Facility</th><th>MFL Code</th><th>Patient CCC Number</th><th>Gender</th><th>Current Age</th><th>Date confirmed HIV Positive</th><th>Enrollment date</th><th>ART start date</th><th>Baseline WHO Stage</th><th>Baseline CD4 Count or Percent</th><th>Initial VL Result</th><th>Initial Vl Date</th><th>Repeat VL  result</th><th>Repeat VL date</th><th>VL result at 12 months</th><th>12 month Vl Date</th><th>Last visit date</th><th>Expected return date (TCA)</th><th>Patient Status</th><th>Date patient resumed Tx</th><th>Reason for Failing to Upload</th></tr></thead><tbody>";
      String id="",county="",sub_county="",facility="",mfl_code="",ccc_no="",gender="",current_age="",date_confirmed_hiv_pos="",enrollment_date="",art_start_date="",baseline_who_stage="",baseline_cd4_cell_count_perc="",initial_vl="",initial_vl_date="",repeat_vl_value="",repeat_vl_date="",vl_12_months_value="",vl_12_months_date="",last_visit_date="",expected_return_date="",patient_status="",date_resumed_tx="",reason="";
           JSONObject obj_det = new JSONObject();
        String error_details="",period_error = "Error While loading <b>3b. ART Current Net Loss-Var</b> Sheet: <br>";
        boolean has_error=false;
        Iterator rowIterator = worksheet.iterator();

        int i=5,y=0,skipped_records=0,added_records=0;
        while(rowIterator.hasNext()){
            error_details = reason = "";
            has_error=false;
        
            yearmonth=year=month=id=county=sub_county=facility=mfl_code=ccc_no=gender=current_age=date_confirmed_hiv_pos=enrollment_date=art_start_date=baseline_who_stage=baseline_cd4_cell_count_perc=initial_vl=initial_vl_date=repeat_vl_value=repeat_vl_date=vl_12_months_value=vl_12_months_date=last_visit_date=expected_return_date=patient_status=date_resumed_tx="";

             Row rowi = worksheet.getRow(i);
                if(rowi==null){
                 break;
                }

// **************************************County***********************
            Cell cell0 = rowi.getCell((short) 0);
            if(cell0!=null){
            switch (cell0.getCellType()) {
                   case 0:
                       //numeric
                       county =""+(int)cell0.getNumericCellValue();
                       break;
                   case 1:
                       //string
                       county =cell0.getStringCellValue();
                       break;
                  
               }
            }
            error_details+="<td>"+county+"</td>";
//*******************************End of county************************
               
// **************************************Sub county***********************
            Cell cell1 = rowi.getCell((short) 1);
            if(cell1!=null){
            switch (cell1.getCellType()) {
                   case 0:
                       //numeric
                       sub_county =""+(int)cell1.getNumericCellValue();
                       break;
                   case 1:
                       //string
                       sub_county =cell1.getStringCellValue();
                       break;
                   
               }
            }
            error_details+="<td>"+sub_county+"</td>";
//*******************************End of sub-county************************

// **************************************Facility Name***********************
            Cell cell2 = rowi.getCell((short) 2);
            if(cell2!=null){
            switch (cell2.getCellType()) {
                   case 0:
                       //numeric
                       facility =""+(int)cell2.getNumericCellValue();
                       break;
                   case 1:
                       //string
                       facility =cell2.getStringCellValue();
                       break;
                   
               }
            }
            error_details+="<td>"+facility+"</td>";
//*******************************End of facility name************************;
               
// **************************************mflcode***********************
            Cell cell3 = rowi.getCell((short) 3);
            if(cell3!=null){
            switch (cell3.getCellType()) {
                   case 0:
                       //numeric
                       mfl_code =""+(int)cell3.getNumericCellValue();
                       break;
                   case 1:
                       //string
                       mfl_code =cell3.getStringCellValue();
                       break;
               }
            }
            else{
                has_error=true;
            }
            if(mfl_code.equals("")){
               has_error=true;
               reason+="Missing MFL Code <br>";
            }
            error_details+="<td>"+mfl_code+"</td>";
//*******************************End of mflcode************************

// **************************************CCC Number***********************
        
            Cell cell4 = rowi.getCell((short) 4);
            if(cell4!=null){
            switch (cell4.getCellType()) {
                   case 0:
                       //numeric
                       ccc_no =""+(int)cell4.getNumericCellValue();
                       break;
                   case 1:
                       //string
                       ccc_no =cell4.getStringCellValue();
                       break;
               }
            }
            else{
                has_error=true;
            }
            if(ccc_no.equals("")){
               has_error=true; 
               reason+="Missing MFL Code <br>";
            }
            error_details+="<td>"+ccc_no+"</td>";
//*******************************End of ccc number************************
               
// **************************************gender***********************
            Cell cell5 = rowi.getCell((short) 5);
            if(cell5!=null){
            switch (cell5.getCellType()) {
                   case 0:
                       //numeric
                       gender =""+(int)cell5.getNumericCellValue();
                       break;
                   case 1:
                       //string
                       gender =cell5.getStringCellValue();
                       break;
               }
            }
            error_details+="<td>"+gender+"</td>";
//*******************************End of gender************************

               
// **************************************current age***********************
            Cell cell6 = rowi.getCell((short) 6);
            if(cell6!=null){
            switch (cell6.getCellType()) {
                   case 0:
                       //numeric
                       current_age =""+(int)cell6.getNumericCellValue();
                       break;
                   case 1:
                       //string
                       current_age =cell6.getStringCellValue();
                       break;
                   
               }
            }
            error_details+="<td>"+current_age+"</td>";
//*******************************End of current age************************
             
// **************************************date confirmed HIV+***********************
           if(rowi.getCell(7)!=null){
if(rowi.getCell(7).getCellType()==0){
          date_confirmed_hiv_pos =""+dateformat.format(rowi.getCell(7).getDateCellValue());
        }
        else{
           if (rowi.getCell(7).getCellTypeEnum().equals(NUMERIC)) {
            date_confirmed_hiv_pos = dateformat.format(rowi.getCell(7).getDateCellValue());
        }
        else{
         date_confirmed_hiv_pos = rowi.getCell(7).getStringCellValue();
        }
    }
           }
           error_details+="<td>"+date_confirmed_hiv_pos+"</td>";
            if(!date_confirmed_hiv_pos.equals("")){
            date_confirmed_hiv_pos = format_date(date_confirmed_hiv_pos);
                get_year_month(date_confirmed_hiv_pos);
 }
//*******************************End of DATE CONFIRMED hiv pos************************
               
// **************************************enrollment date***********************
if(rowi.getCell(8)!=null){
          if(rowi.getCell(8).getCellType()==0){
          enrollment_date =""+dateformat.format(rowi.getCell(8).getDateCellValue());
        }
        else{
           if (rowi.getCell(8).getCellTypeEnum().equals(NUMERIC)) {
            enrollment_date = dateformat.format(rowi.getCell(8).getDateCellValue());
        }
        else{
         enrollment_date = rowi.getCell(8).getStringCellValue();
        }
    }
 }
error_details+="<td>"+enrollment_date+"</td>";
 if(!enrollment_date.equals("")){
            enrollment_date = format_date(enrollment_date);
 }
//*******************************End of enrollment date************************


// **************************************ART Start date***********************
if(rowi.getCell(9)!=null){
  if(rowi.getCell(9).getCellType()==0){
  art_start_date =""+dateformat.format(rowi.getCell(9).getDateCellValue());
  }
    else{
           if (rowi.getCell(9).getCellTypeEnum().equals(NUMERIC)) {
            art_start_date = dateformat.format(rowi.getCell(9).getDateCellValue());
        }
        else{
         art_start_date = rowi.getCell(9).getStringCellValue();
        }
    }
}
error_details+="<td>"+art_start_date+"</td>";

 if(!art_start_date.equals("")){
            art_start_date = format_date(art_start_date);
 }
//*******************************End of art start date************************

// ************************************** baseline WHO stage ***********************
            Cell cell10 = rowi.getCell((short) 10);
            if(cell10!=null){
            switch (cell10.getCellType()) {
                   case 0:
                       //numeric
                       baseline_who_stage =""+(int)cell10.getNumericCellValue();
                       break;
                   case 1:
                       //string
                       baseline_who_stage =cell10.getStringCellValue();
                       break;
                   
               }
            }
            error_details+="<td>"+baseline_who_stage+"</td>";
//*******************************End of baseline WHO stage************************


// **************************************baseline cd4 cell count percentage***********************
System.out.println("at pos : "+i+" mflcode : "+mfl_code);
            Cell cell11 = rowi.getCell((short) 11);
            if(cell11!=null){
            switch (cell11.getCellType()) {
                   case 0:
                       //numeric
                       baseline_cd4_cell_count_perc =""+(int)cell11.getNumericCellValue();
                       break;
                   case 1:
                       //string
                       baseline_cd4_cell_count_perc =cell11.getStringCellValue();
                       break;
                   
               }
            }
            error_details+="<td>"+baseline_cd4_cell_count_perc+"</td>";
//*******************************End of baseline cd4 cell count percentage************************


// **************************************initial vl***********************
            Cell cell12 = rowi.getCell((short) 12);
            if(cell12!=null){
            switch (cell12.getCellType()) {
                   case 0:
                       //numeric
                       initial_vl =""+(int)cell12.getNumericCellValue();
                       break;
                   case 1:
                       //string
                       initial_vl =cell12.getStringCellValue();
                       break;
                  
               }
            }
            error_details+="<td>"+initial_vl+"</td>";
//*******************************End of initial vl************************


// **************************************art start date***********************
if(rowi.getCell(13)!=null){
     if(rowi.getCell(13).getCellType()==0){
  initial_vl_date =""+dateformat.format(rowi.getCell(13).getDateCellValue()); 
    }
    else{
           if (rowi.getCell(13).getCellTypeEnum().equals(NUMERIC)) {
            initial_vl_date = dateformat.format(rowi.getCell(13).getDateCellValue());
        }
        else{
         initial_vl_date = rowi.getCell(13).getStringCellValue();
        }
    }
}    
error_details+="<td>"+initial_vl_date+"</td>";

 if(!initial_vl_date.equals("")){
            initial_vl_date = format_date(initial_vl_date);
 }
//*******************************End of art start date************************


// **************************************repeat vl results***********************
            Cell cell14 = rowi.getCell((short) 14);
            if(cell14!=null){
            switch (cell14.getCellType()) {
                   case 0:
                       //numeric
                       repeat_vl_value =""+(int)cell14.getNumericCellValue();
                       break;
                   case 1:
                       //string
                       repeat_vl_value =cell14.getStringCellValue();
                       break;
                   
               }
            }
            error_details+="<td>"+repeat_vl_value+"</td>";
//*******************************End of repeat vl results************************


// **************************************repeat vl date***********************
        if(rowi.getCell(15)!=null) {  
          if(rowi.getCell(15).getCellType()==0){
      repeat_vl_date =""+dateformat.format(rowi.getCell(15).getDateCellValue());
        }
        else{
               if (rowi.getCell(15).getCellTypeEnum().equals(NUMERIC)) {
                repeat_vl_date = dateformat.format(rowi.getCell(15).getDateCellValue());
            }
            else{
             repeat_vl_date = rowi.getCell(15).getStringCellValue();
            }
    }
        }
        error_details+="<td>"+repeat_vl_date+"</td>";
        
         if(!repeat_vl_date.equals("")){
            repeat_vl_date = format_date(repeat_vl_date);
 }
//*******************************End of repeat vl date************************

// **************************************vl results at 12 months***********************
            Cell cell16 = rowi.getCell((short) 16);
            if(cell16!=null){
            switch (cell16.getCellType()) {
                   case 0:
                       //numeric
                       vl_12_months_value =""+(int)cell16.getNumericCellValue();
                       break;
                   case 1:
                       //string
                       vl_12_months_value =cell16.getStringCellValue();
                       break;
                   
               }
            }
            error_details+="<td>"+vl_12_months_value+"</td>";
//*******************************End of vl results at 12 months************************


// **************************************12 months vl date***********************
        if(rowi.getCell(17)!=null){
    if(rowi.getCell(17).getCellType()==0){
           vl_12_months_date =""+dateformat.format(rowi.getCell(17).getDateCellValue());
             }
             else{
                if (rowi.getCell(17).getCellTypeEnum().equals(NUMERIC)) {
                 vl_12_months_date = dateformat.format(rowi.getCell(17).getDateCellValue());
             }
             else{
              vl_12_months_date = rowi.getCell(17).getStringCellValue();
             }
     }
        }
        error_details+="<td>"+vl_12_months_date+"</td>";
        
         if(!vl_12_months_date.equals("")){
            vl_12_months_date = format_date(vl_12_months_date);
 }
//*******************************End of 12 months vl date************************


// **************************************last vl date***********************
    if(rowi.getCell(18)!=null){
                 if(rowi.getCell(18).getCellType()==0){
            last_visit_date =""+dateformat.format(rowi.getCell(18).getDateCellValue());
              }
              else{
                     if (rowi.getCell(18).getCellTypeEnum().equals(NUMERIC)) {
                      last_visit_date = dateformat.format(rowi.getCell(18).getDateCellValue());
                  }
                  else{
                   last_visit_date = rowi.getCell(18).getStringCellValue();
                  }
          }
    }
    error_details+="<td>"+last_visit_date+"</td>";
    
     if(!last_visit_date.equals("")){
            last_visit_date = format_date(last_visit_date);
 }
//*******************************End of last vl date************************

// **************************************expected return date***********************
              if(rowi.getCell(19)!=null){
                 if(rowi.getCell(19).getCellType()==0){
            expected_return_date =""+dateformat.format(rowi.getCell(19).getDateCellValue());
              }
              else{
                     if (rowi.getCell(19).getCellTypeEnum().equals(NUMERIC)) {
                      expected_return_date = dateformat.format(rowi.getCell(19).getDateCellValue());
                  }
                  else{
                   expected_return_date = rowi.getCell(19).getStringCellValue();
                  }
          }
    }
 error_details+="<td>"+expected_return_date+"</td>";  
 
  if(!expected_return_date.equals("")){
            expected_return_date = format_date(expected_return_date);
 }
  
//*******************************End of expected return date************************

// **************************************patient status***********************
            Cell cell20 = rowi.getCell((short) 20);
            if(cell20!=null){
            switch (cell20.getCellType()) {
                   case 0:
                       //numeric
                       patient_status =""+(int)cell20.getNumericCellValue();
                       break;
                   case 1:
                       //string
                       patient_status =cell20.getStringCellValue();
                       break;
                   
               }
            }
            error_details+="<td>"+patient_status+"</td>";
//*******************************End of patient status************************

// **************************************date patient resument tx***********************
              if(rowi.getCell(21)!=null){
                 if(rowi.getCell(21).getCellType()==0){
            date_resumed_tx =""+dateformat.format(rowi.getCell(21).getDateCellValue());
              }
              else{
                     if (rowi.getCell(21).getCellTypeEnum().equals(NUMERIC)) {
                      date_resumed_tx = dateformat.format(rowi.getCell(21).getDateCellValue());
                  }
                  else{
                   date_resumed_tx = rowi.getCell(21).getStringCellValue();
                  }
          }
    }
              error_details+="<td>"+date_resumed_tx+"</td>";
              
               if(!date_resumed_tx.equals("")){
            date_resumed_tx = format_date(date_resumed_tx);
 }
               
//*******************************End of date patient resument tx************************

//     generate id

            id = mfl_code+"_"+yearmonth+"_"+ccc_no;

      //       END OF READING VALUES
if(has_error){
  all_error_details+="<tr><td>"+i+"</td>"+error_details+"<td>"+reason+"</td></tr>"; 
  skipped_records++;
}
else{
    added_records++;
            //GET CORRECT COUNTY, SUBCOUNTY DATA FOR THIS FACILITY
            JSONObject obj_facil = GetFacilityDetails(conn, mfl_code);
            
            if(obj_facil.containsKey("county")){
              county = obj_facil.get("county").toString();
              sub_county = obj_facil.get("sub_county").toString();
              facility = obj_facil.get("facility").toString();

                String query = "REPLACE INTO art_current_net_loss SET id=?,year=?,month=?,county=?,sub_county=?,facility=?,mfl_code=?,ccc_no=?,gender=?,current_age=?,date_confirmed_hiv_pos=?,enrollment_date=?,art_start_date=?,baseline_who_stage=?,baseline_cd4_cell_count_perc=?,initial_vl=?,initial_vl_date=?,repeat_vl_value=?,repeat_vl_date=?,vl_12_months_value=?,vl_12_months_date=?,last_visit_date=?,yearmonth=?,expected_return_date=?,patient_status=?,date_resumed_tx=?";
                conn.pst = conn.conn.prepareStatement(query);
                conn.pst.setString(1, id);
                conn.pst.setString(2, year);
                conn.pst.setString(3, month);
                conn.pst.setString(4, county);
                conn.pst.setString(5, sub_county);
                conn.pst.setString(6, facility);
                conn.pst.setString(7, mfl_code);
                conn.pst.setString(8, ccc_no);
                conn.pst.setString(9, gender);
                conn.pst.setString(10, current_age);
                conn.pst.setString(11, date_confirmed_hiv_pos);
                conn.pst.setString(12, enrollment_date);
                conn.pst.setString(13, art_start_date);
                conn.pst.setString(14, baseline_who_stage);
                conn.pst.setString(15, baseline_cd4_cell_count_perc);
                conn.pst.setString(16, initial_vl);
                conn.pst.setString(17, initial_vl_date);
                conn.pst.setString(18, repeat_vl_value);
                conn.pst.setString(19, repeat_vl_date);
                conn.pst.setString(20, vl_12_months_value);
                conn.pst.setString(21, vl_12_months_date);
                conn.pst.setString(22, last_visit_date);
                conn.pst.setString(23, yearmonth);
                conn.pst.setString(24, expected_return_date);
                conn.pst.setString(25, patient_status);
                conn.pst.setString(26, date_resumed_tx);
                conn.pst.executeUpdate();
            
//                System.out.println("query ART current net loss______ : "+conn.pst);
                conn.pst.executeUpdate();
              
            }
            else{
               
            }
        }
//***************************************************************************
        i++;
        }
               
     obj_det.put("added", added_records);
     obj_det.put("skipped", skipped_records);
     obj_det.put("skipped_details", all_error_details+"</tbody>");
     obj_det.put("sheetname", "3b. ART Current Net Loss-Var");
        
      return obj_det;
    }
  
    public String format_date(String date){
        System.out.println("date is : "+date);
        String indate = date;
        String yr="",mn="",dat="";
        int split=0;
        
    if(date.equals("")){

    }        
    else if(date.contains("-")){
        String date_array[] = date.split("-");
        split=date_array.length;
        if(split==3){
        yr = date_array[0];
        mn = date_array[1];
        dat = date_array[2];
        }
        }
        else if(date.contains(".") || date.contains("/")){
        date = date.replace(".","/");
        String date_array[] = date.split("/");
        split=date_array.length;
        if(split==3){
        yr = date_array[2];
        mn = date_array[0];
        dat = date_array[1];
        }
        }
        
        else if(date.contains(" ")){
            String[] date_arr = date.split(" ");
            yr = date_arr[2];
            dat = date_arr[0];
            
            mn = getmonth(date_arr[1]);
            split=date_arr.length;
        }
        yr = yr.replace(" ", "").trim();
        mn = mn.replace(" ", "").trim();
        dat = dat.replace(" ", "").trim();
        
        if(split==3 && isNumeric(yr) && isNumeric(mn) && isNumeric(dat)){
         if(Integer.parseInt(mn)>12 && isNumeric(dat)){
             String dat2;
               dat2=mn;
               mn=dat;
               dat=dat2;
           }   
        if(Integer.parseInt(mn)<10){
            mn = "0"+Integer.parseInt(mn);
        }
        if(Integer.parseInt(dat)<10){
            dat = "0"+Integer.parseInt(dat);
        }
        date = yr+"-"+mn+"-"+dat;
           
        }
        else{
          date = indate;  
        }
        System.out.println("Indate : "+indate+" Formatted date : "+date);
        return date;
    }
    public void get_year_month(String date){
        String indate = date;
        String yr="",mn="",dat="";
        int split=0;
                
        if(date.contains("-")){
        String date_array[] = date.split("-");
        yr = date_array[0];
        mn = date_array[1];
        dat = date_array[2];
        split=1;
        }
        else if(date.contains(".") || date.contains("/")){
        date = date.replace(".","/");
        String date_array[] = date.split("/");
        
        yr = date_array[2];
        mn = date_array[0];
        dat = date_array[1];
        split=1;
        }
        
        else if(date.contains(" ")){
            String[] date_arr = date.split(" ");
            yr = date_arr[2];
            dat = date_arr[0];
            
            mn = getmonth(date_arr[1]);
        }
        
       mn = mn.replace(" ", "").trim();
       dat = dat.replace(" ", "").trim();
       
        if(split==1 && isNumeric(yr) && isNumeric(mn)){
           if(Integer.parseInt(mn)>12 && isNumeric(dat)){
               mn=dat;
           }
        if(Integer.parseInt(mn)<10){
            mn = "0"+Integer.parseInt(mn);
        }
            
        yearmonth = yr+""+mn;
        year = yr;
        month = mn;
    }
    
}
     public boolean isNumeric(String s) {  
        return s != null && s.matches("[-+]?\\d*\\.?\\d+");  
    }
     
     public String getmonth(String mn){
         String month="";
         if(mn.equalsIgnoreCase("Jan")){
          month="01";   
         }
         else if(mn.equalsIgnoreCase("Feb")){
          month="02";     
         }
         else if(mn.equalsIgnoreCase("Mar")){
          month="03";     
         }
         else if(mn.equalsIgnoreCase("Apr")){
         month="04";      
         }
         else if(mn.equalsIgnoreCase("May")){
         month="05";      
         }
         else if(mn.equalsIgnoreCase("Jun")){
         month="06";      
         }
         else if(mn.equalsIgnoreCase("Jul")){
         month="07";      
         }
         else if(mn.equalsIgnoreCase("Aug")){
          month="08";     
         }
         else if(mn.equalsIgnoreCase("Sep")){
          month="09";     
         }
         else if(mn.equalsIgnoreCase("Oct")){
          month="10";     
         }
         else if(mn.equalsIgnoreCase("Nov")){
          month="11";     
         }
         else if(mn.equalsIgnoreCase("Dec")){
          month="12";     
         }
         
         System.out.println("month "+month+" mn :"+mn);
         return month;
     }
}