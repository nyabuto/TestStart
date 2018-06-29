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
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import static org.apache.poi.ss.usermodel.CellType.NUMERIC;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
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
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
            session = request.getSession();
            dbConn conn = new dbConn();
      
            JSONObject finalobj = new JSONObject();
            JSONObject objacc4link = new JSONObject();
            JSONObject objteststart = new JSONObject();
            JSONObject objartcurr = new JSONObject();

            
            JSONArray jarray = new JSONArray();
       
            
        String applicationPath = request.getServletContext().getRealPath("");
         String uploadFilePath = applicationPath + File.separator + UPLOAD_DIR;
         session=request.getSession();
          File fileSaveDir = new File(uploadFilePath);
        if (!fileSaveDir.exists()) {
            fileSaveDir.mkdirs();
        }
        
        for (Part part : request.getParts()) {
            
            if(!getFileName(part).equals("")){
           fileName = getFileName(part);
            part.write(uploadFilePath + File.separator + fileName);
            
            full_path=fileSaveDir.getAbsolutePath()+"\\"+fileName;
            System.out.println("fullpath : "+full_path);
           // read the contents of the workbook and sheets here
           
           FileInputStream fileInputStream = new FileInputStream(full_path);
            
//****************************WORKBOOK INFORMATION****************************************
        //Call to read all sheets.
              
       
       //call sheet for accounting for linkages--validation
       
       
       if(fileName.endsWith(".xlsx")){
       XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
//       XSSFSheet worksheetAccValid = workbook.getSheet("Acc for Linkage -Validation");
       XSSFSheet  worksheetAcc4Link = workbook.getSheet("1b. Accounting for Linkage");
       XSSFSheet worksheetTestStart = workbook.getSheet("2b.Test & Start -Cohort Summary");
       XSSFSheet worksheetARTLoss = workbook.getSheet("3b. ART Current Net Loss-Var ");

       
//       fOR xlsx fILES
//        Account4LinkageValidationXLSX(worksheetAccValid,conn);
      objacc4link = Accounting4LinkageSummary(worksheetAcc4Link,conn);
      objteststart = TestStartSummary(worksheetTestStart,conn);
      objartcurr = ARTCurrentLoss(worksheetARTLoss,conn);
      
      System.out.println("artcurr: "+objartcurr);
       }
       else if(fileName.endsWith(".xls")){
       HSSFWorkbook workbook = new HSSFWorkbook(fileInputStream);
//       HSSFSheet worksheetAccValid = workbook.getSheet("Acc for Linkage -Validation");
       HSSFSheet  worksheetAcc4Link = workbook.getSheet("1b. Accounting for Linkage");
       HSSFSheet worksheetTestStart = workbook.getSheet("2b.Test & Start -Cohort Summary");
       HSSFSheet worksheetARTLoss = workbook.getSheet("3b. ART Current Net Loss-Var ");
       
//        Account4LinkageValidationXLS(worksheetAccValid,conn);
      objacc4link = Accounting4LinkageSummary(worksheetAcc4Link,conn);
      objteststart = TestStartSummary(worksheetTestStart,conn);
      objartcurr = ARTCurrentLoss(worksheetARTLoss,conn);  
      
      System.out.println("artcurr: "+objartcurr);
       }
       else{
           System.out.println("unsupported file type");
       }
        
        
        //end of reading all sheets
 
        }
   
        }
       jarray.add(objacc4link);
       jarray.add(objteststart);
       jarray.add(objartcurr);
        
        
        System.out.println("errors : "+jarray);
        session.setAttribute("upload_errors", jarray);
      
      
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

    public int Account4LinkageValidation(XSSFSheet worksheet, dbConn conn) throws SQLException{
        String id="",date="",county="",sub_county="",facility="",mfl_code="",positive="",enrolled_care="",started_art="",perc_enrolled_care="",perc_started_art="",variance_enroll_care="",yearmonth="";
        
      int num=0;
        Iterator rowIterator = worksheet.iterator();

        int i=1,y=0;
        while(rowIterator.hasNext()){
             XSSFRow rowi = worksheet.getRow(i);
                if(rowi==null){
                 break;
                }
// **************************************Month***********************

if (HSSFDateUtil.isCellDateFormatted(rowi.getCell(0))) {
    date = dateformat.format(rowi.getCell(0).getDateCellValue());
            
    System.out.println(i+"-----date for month is : "+date);
}
else{
  break;
}

//*******************************End of month************************

// **************************************County***********************
            XSSFCell cell1 = rowi.getCell((short) 1);
            switch (cell1.getCellType()) {
                   case 0:
                       //numeric
                       county =""+(int)cell1.getNumericCellValue();
                       break;
                   case 1:
                       //string
                       county =cell1.getStringCellValue();
                       break;
                   default:
                       //other
                       county = cell1.getRawValue();
                       break;
               }
//*******************************End of county************************
               
// **************************************Sub county***********************
            XSSFCell cell2 = rowi.getCell((short) 2);
            switch (cell2.getCellType()) {
                   case 0:
                       //numeric
                       sub_county =""+(int)cell2.getNumericCellValue();
                       break;
                   case 1:
                       //string
                       sub_county =cell2.getStringCellValue();
                       break;
                   default:
                       //other
                       sub_county = cell2.getRawValue();
                       break;
               }
//*******************************End of sub-county************************

// **************************************Facility Name***********************
            XSSFCell cell3 = rowi.getCell((short) 3);
            switch (cell3.getCellType()) {
                   case 0:
                       //numeric
                       facility =""+(int)cell3.getNumericCellValue();
                       break;
                   case 1:
                       //string
                       facility =cell3.getStringCellValue();
                       break;
                   default:
                       //other
                       facility = cell3.getRawValue();
                       break;
               }
//*******************************End of facility name************************;
               
// **************************************mflcode***********************
            XSSFCell cell4 = rowi.getCell((short) 4);
            switch (cell4.getCellType()) {
                   case 0:
                       //numeric
                       mfl_code =""+(int)cell4.getNumericCellValue();
                       break;
                   case 1:
                       //string
                       mfl_code =cell4.getStringCellValue();
                       break;
                   default:
                       //other
                       mfl_code = cell4.getRawValue();
                       break;
               }
//*******************************End of mflcode************************

// **************************************positive***********************
            XSSFCell cell5 = rowi.getCell((short) 5);
            switch (cell5.getCellType()) {
                   case 0:
                       //numeric
                       positive =""+(int)cell5.getNumericCellValue();
                       break;
                   case 1:
                       //string
                       positive =cell5.getStringCellValue();
                       break;
                   default:
                       //other
                       positive = cell5.getRawValue();
                       break;
               }
//*******************************End of tested positive************************
               
// **************************************enrolled to care***********************
            XSSFCell cell6 = rowi.getCell((short) 6);
            switch (cell6.getCellType()) {
                   case 0:
                       //numeric
                       enrolled_care =""+(int)cell6.getNumericCellValue();
                       break;
                   case 1:
                       //string
                       enrolled_care =cell6.getStringCellValue();
                       break;
                   default:
                       //other
                       enrolled_care = cell6.getRawValue();
                       break;
               }
//*******************************End of enrolled to care************************

               
// **************************************started on ART***********************
            XSSFCell cell7 = rowi.getCell((short) 7);
            switch (cell7.getCellType()) {
                   case 0:
                       //numeric
                       started_art =""+(int)cell7.getNumericCellValue();
                       break;
                   case 1:
                       //string
                       started_art =cell7.getStringCellValue();
                       break;
                   default:
                       //other
                       started_art = cell7.getRawValue();
                       break;
               }
//*******************************End of STARTED art************************
               
// **************************************percentage enrolled to care***********************
            XSSFCell cell8 = rowi.getCell((short) 8);
            switch (cell8.getCellType()) {
                   case 0:
                       //numeric
                       perc_enrolled_care =""+(int)cell8.getNumericCellValue();
                       break;
                   case 1:
                       //string
                       perc_enrolled_care =cell8.getStringCellValue();
                       break;
                   default:
                       //other
                       perc_enrolled_care = cell8.getRawValue();
                       break;
               }
//*******************************End of perc enrolled to care************************

               
// **************************************percentage started on ART***********************
            XSSFCell cell9 = rowi.getCell((short) 9);
            switch (cell9.getCellType()) {
                   case 0:
                       //numeric
                       perc_started_art =""+(int)cell9.getNumericCellValue();
                       break;
                   case 1:
                       //string
                       perc_started_art =cell9.getStringCellValue();
                       break;
                   default:
                       //other
                       perc_started_art = cell9.getRawValue();
                       break;
               }
//*******************************End of PERC STARTED ON art************************
               
// **************************************Variance in enrollment***********************
            XSSFCell cell10 = rowi.getCell((short) 10);
            switch (cell10.getCellType()) {
                   case 0:
                       //numeric
                       variance_enroll_care =""+(int)cell10.getNumericCellValue();
                       break;
                   case 1:
                       //string
                       variance_enroll_care =cell10.getStringCellValue();
                       break;
                   default:
                       //other
                       variance_enroll_care = cell10.getRawValue();
                       break;
               }
//*******************************End of variance in enrollment************************

//              get yearmonth here
            yearmonth =  GetYearMonth(date);
            id = mfl_code+"_"+yearmonth;

      //       END OF READING VALUES

            //GET CORRECT COUNTY, SUBCOUNTY DATA FOR THIS FACILITY
            JSONObject obj_facil = GetFacilityDetails(conn, mfl_code);
            
            if(obj_facil.containsKey("county")){
              county = obj_facil.get("county").toString();
              sub_county = obj_facil.get("sub_county").toString();
              facility = obj_facil.get("facility").toString();

               String query = "REPLACE INTO accounting_linkage_validation SET id=?,month=?,county=?,sub_county=?,facility=?,mfl_code=?,positive=?,enrolled_care=?,started_art=?,perc_enrolled_care=?,perc_started_art=?,variance_enroll_care=?,yearmonth=?";
                conn.pst = conn.conn.prepareStatement(query);
                conn.pst.setString(1, id);
                conn.pst.setString(2, date);
                conn.pst.setString(3, county);
                conn.pst.setString(4, sub_county);
                conn.pst.setString(5, facility);
                conn.pst.setString(6, mfl_code);
                conn.pst.setString(7, positive);
                conn.pst.setString(8, enrolled_care);
                conn.pst.setString(9, started_art);
                conn.pst.setString(10, perc_enrolled_care);
                conn.pst.setString(11, perc_started_art);
                conn.pst.setString(12, variance_enroll_care);
                conn.pst.setString(13, yearmonth);
                conn.pst.executeUpdate();
            


            num += conn.pst.executeUpdate();
              if(num>0){
                 
              }  
              else{
                
              }
            }
            else{
               
            }
            
//***************************************************************************
        i++;
        }
        
      
      return num;
    }
 
    public JSONObject Accounting4LinkageSummary (XSSFSheet worksheet, dbConn conn) throws SQLException{
        String all_error_details = "<thead class=\"thead-dark\"><tr><th>Row Number</th><th>County</th><th>Sub-County</th><th>Health Facility</th><th>MFL Code</th><th>Date confirmed HIV positive</th><th>Gender</th><th>Current Age</th><th>Documented in linkage register</th><th>Patient CCC Number</th><th>Enrolment Date</th><th>Referred</th><th>Enrolled to other site</th><th>Enrolled from other site</th><th>ART start date</th><th>Started ART in this facility</th><th>Started in other facility</th><th>Patient Status</th><th>If declined, indicate reason</th><th>If dead, indicate reported  cause of death</th></tr></thead><tbody>";
        String id="",period_details="",year="",month="",county="",sub_county="",facility="",mfl_code="",date_confirmed_hiv_pos="",gender="",current_age="",documented_linkage_register="",ccc_no="",enrollment_date="",referred="",enrolled_to_other_site="",enrolled_from_other_site="",art_start_date="",started_art_in_this_facility="",started_art_in_other_facility="",patient_status="",declined_reason="",reported_cause_of_death="",yearmonth="";
        JSONObject obj_det = new JSONObject();
         String error_details="",period_error = "Error While loading <b>1b. Accounting for Linkage</b> Sheet: <br>";
        boolean has_error=false;
        
        Iterator rowIterator = worksheet.iterator();

         XSSFRow row = worksheet.getRow(2);
// **************************************Month***********************
            XSSFCell cellmn = row.getCell((short) 4);
            switch (cellmn.getCellType()) {
                   case 0:
                       //numeric
                       month =""+(int)cellmn.getNumericCellValue();
                       break;
                   case 1:
                       //string
                       month =cellmn.getStringCellValue();
                       break;
                   
               }
            if(month.equals("") || cellmn==null){
               has_error=true;  
               period_error+="Missing Month <br>";
            }
// **************************************************************************           
// **************************************Year***********************
            XSSFCell cellyr = row.getCell((short) 12);
            switch (cellyr.getCellType()) {
                   case 0:
                       //numeric
                       year =""+(int)cellyr.getNumericCellValue();
                       break;
                   case 1:
                       //string
                       year =cellyr.getStringCellValue();
                       break;
               }
            if(year.equals("") || cellyr==null){
               has_error=true;  
               period_error+="Missing Year <br>";
            }
// **************************************************************************           
            System.out.println("perid details : "+period_details);
            
            JSONObject period_data = CleanPeriod(year,month,conn);
            
            yearmonth = period_data.get("yearmonth").toString();
            year = period_data.get("year").toString();
            month = period_data.get("month").toString();
            
                    
            
            System.out.println("perid details : yearmonth : "+yearmonth+" year : "+year+" month :: "+month);
//*******************************YearMonth************************

        if(!has_error){
        int i=5,y=0,skipped_records=0,added_records=0;
        while(rowIterator.hasNext()){
            error_details = "";
            has_error=false;
        
            id=period_details=county=sub_county=facility=mfl_code=date_confirmed_hiv_pos=gender=current_age=documented_linkage_register=ccc_no=enrollment_date=referred=enrolled_to_other_site=enrolled_from_other_site=art_start_date=started_art_in_this_facility=started_art_in_other_facility=patient_status=declined_reason=reported_cause_of_death="";

             XSSFRow rowi = worksheet.getRow(i);
                if(rowi==null){
                 break;
                }

// **************************************County***********************
            XSSFCell cell0 = rowi.getCell((short) 0);
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
            XSSFCell cell1 = rowi.getCell((short) 1);
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
            XSSFCell cell2 = rowi.getCell((short) 2);
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
            XSSFCell cell3 = rowi.getCell((short) 3);
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
            }
            error_details+="<td>"+ccc_no+"</td>";
//*******************************End of mflcode************************

// **************************************date confirmed HIV pos***********************
        if(rowi.getCell(4)!=null){
        if(rowi.getCell(4).getCellType()==0){
          date_confirmed_hiv_pos =""+(int)rowi.getCell(4).getNumericCellValue();  
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
//*******************************End of date confirmed HIV pos************************
               
// **************************************gender***********************
            XSSFCell cell5 = rowi.getCell((short) 5);
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
            XSSFCell cell6 = rowi.getCell((short) 6);
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
            XSSFCell cell7 = rowi.getCell((short) 7);
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
            XSSFCell cell8 = rowi.getCell((short) 8);
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
            if(ccc_no.equals("")){
               has_error=true;  
            }
            error_details+="<td>"+ccc_no+"</td>";
//*******************************End of patient ccc number************************


// **************************************enrollment date***********************
    if(rowi.getCell(9)!=null){
  if(rowi.getCell(9).getCellType()==0){
  enrollment_date =""+(int)rowi.getCell(9).getNumericCellValue();  
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
//*******************************End of enrollment date************************


// **************************************referred***********************
            XSSFCell cell10 = rowi.getCell((short) 10);
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
            XSSFCell cell11 = rowi.getCell((short) 11);
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
            XSSFCell cell12 = rowi.getCell((short) 12);
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
  art_start_date =""+(int)rowi.getCell(13).getNumericCellValue();  
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
//*******************************End of art start date************************


// **************************************started art in this facility***********************
            XSSFCell cell14 = rowi.getCell((short) 14);
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
            XSSFCell cell15 = rowi.getCell((short) 15);
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
            XSSFCell cell16 = rowi.getCell((short) 16);
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
            XSSFCell cell17 = rowi.getCell((short) 17);
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
            XSSFCell cell18 = rowi.getCell((short) 18);
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

            id = mfl_code+"_"+yearmonth+"_"+ccc_no;

      //       END OF READING VALUES
    if(has_error){
      all_error_details+="<tr><td>"+i+"</td>"+error_details+"</tr>"; 
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
            
                System.out.println("query : "+conn.pst);
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
    }
          else { //has errors while loading excel
          obj_det.put("period_error", period_error);       
            }
             obj_det.put("sheetname", "1b. Accounting for Linkage");
     
        
      
      return obj_det;
    }
    public JSONObject TestStartSummary (XSSFSheet worksheet, dbConn conn) throws SQLException{
        String all_error_details="<thead class=\"thead-dark\"><tr><th>Row Number</th><th>County</th><th>Subcounty</th><th>Health Facility</th><th>MFL Code</th><th>Patient CCC Number</th><th>Gender</th><th>Current Age</th><th>Date confirmed HIV Positive</th><th>Enrollment date</th><th>ART start date</th><th>Baseline WHO Stage</th><th>Baseline CD4 Count or Percent</th><th>Initial VL Result</th><th>Initial Vl Date</th><th>Repeat VL  result</th><th>Repeat VL date</th><th>VL result at 12 months</th><th>12 month Vl Date</th><th>Last visit date</th><th>Patient Outcome</th></tr></thead><tbody>";
      String id="",year="",month="",county="",sub_county="",facility="",mfl_code="",ccc_no="",gender="",current_age="",date_confirmed_hiv_pos="",enrollment_date="",art_start_date="",baseline_who_stage="",baseline_cd4_cell_count_perc="",initial_vl="",initial_vl_date="",repeat_vl_value="",repeat_vl_date="",vl_12_months_value="",vl_12_months_date="",last_visit_date="",patient_outcome="",yearmonth="";
       JSONObject obj_det = new JSONObject();
        String error_details="",period_error = "Error While loading <b>2b.Test & Start -Cohort Summary</b> Sheet: <br>";
        boolean has_error=false;
        
        Iterator rowIterator = worksheet.iterator();

         XSSFRow row = worksheet.getRow(2);
// **************************************Month***********************
            XSSFCell cellmn = row.getCell((short) 4);
            if(cellmn!=null){
            switch (cellmn.getCellType()) {
                   case 0:
                       //numeric
                       month =""+(int)cellmn.getNumericCellValue();
                       break;
                   case 1:
                       //string
                       month =cellmn.getStringCellValue();
                       break;
                   
               }
            }
            if(month.equals("") || cellmn==null){
               has_error=true;  
               period_error+="Missing Month <br>";
            }
// **************************************************************************           
// **************************************Year***********************
            XSSFCell cellyr = row.getCell((short) 11);
            if(cellyr!=null){
            switch (cellyr.getCellType()) {
                   case 0:
                       //numeric
                       year =""+(int)cellyr.getNumericCellValue();
                       break;
                   case 1:
                       //string
                       year =cellyr.getStringCellValue();
                       break;
               }
        }
            if(year.equals("") || cellyr==null){
               has_error=true;  
               period_error+="Missing Year <br>";
            }
// ************************************************************************** 
            
            JSONObject period_data = CleanPeriod(year,month,conn);
            
            yearmonth = period_data.get("yearmonth").toString();
            year = period_data.get("year").toString();
            month = period_data.get("month").toString();
            
                    
            
            System.out.println("yearmonth : "+yearmonth+" year : "+year+" month :: "+month);
//*******************************YearMonth************************

        
         if(!has_error){
        int i=5,y=0,skipped_records=0,added_records=0;
        while(rowIterator.hasNext()){
            error_details = "";
            has_error=false;
        
            id=county=sub_county=facility=mfl_code=ccc_no=gender=current_age=date_confirmed_hiv_pos=enrollment_date=art_start_date=baseline_who_stage=baseline_cd4_cell_count_perc=initial_vl=initial_vl_date=repeat_vl_value=repeat_vl_date=vl_12_months_value=vl_12_months_date=last_visit_date=patient_outcome="";
      
             XSSFRow rowi = worksheet.getRow(i);
                if(rowi==null){
                 break;
                }

// **************************************County***********************
            XSSFCell cell0 = rowi.getCell((short) 0);
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
            XSSFCell cell1 = rowi.getCell((short) 1);
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
            XSSFCell cell2 = rowi.getCell((short) 2);
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
            XSSFCell cell3 = rowi.getCell((short) 3);
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
            }
            error_details+="<td>"+mfl_code+"</td>";
//*******************************End of mflcode************************

// **************************************CCC Number***********************
        
            XSSFCell cell4 = rowi.getCell((short) 4);
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
            }
            error_details+="<td>"+ccc_no+"</td>";
//*******************************End of ccc number************************
               
// **************************************gender***********************
            XSSFCell cell5 = rowi.getCell((short) 5);
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
            XSSFCell cell6 = rowi.getCell((short) 6);
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
          date_confirmed_hiv_pos =""+(int)rowi.getCell(7).getNumericCellValue();  
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
//*******************************End of DATE CONFIRMED hiv pos************************
               
// **************************************enrollment date***********************
if(rowi.getCell(8)!=null){
          if(rowi.getCell(8).getCellType()==0){
          enrollment_date =""+(int)rowi.getCell(8).getNumericCellValue();  
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
//*******************************End of enrollment date************************


// **************************************ART Start date***********************
if(rowi.getCell(9)!=null){
  if(rowi.getCell(9).getCellType()==0){
  art_start_date =""+(int)rowi.getCell(9).getNumericCellValue();  
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
//*******************************End of art start date************************

// ************************************** baseline WHO stage ***********************
            XSSFCell cell10 = rowi.getCell((short) 10);
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
            XSSFCell cell11 = rowi.getCell((short) 11);
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
            XSSFCell cell12 = rowi.getCell((short) 12);
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
  initial_vl_date =""+(int)rowi.getCell(13).getNumericCellValue();  
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
//*******************************End of art start date************************


// **************************************repeat vl results***********************
            XSSFCell cell14 = rowi.getCell((short) 14);
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
      repeat_vl_date =""+(int)rowi.getCell(15).getNumericCellValue();  
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
//*******************************End of repeat vl date************************

// **************************************vl results at 12 months***********************
            XSSFCell cell16 = rowi.getCell((short) 16);
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
           vl_12_months_date =""+(int)rowi.getCell(17).getNumericCellValue();  
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
//*******************************End of 12 months vl date************************


// **************************************last vl date***********************
    if(rowi.getCell(18)!=null){
                 if(rowi.getCell(18).getCellType()==0){
            last_visit_date =""+(int)rowi.getCell(18).getNumericCellValue();  
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
//*******************************End of last vl date************************

// **************************************patient outcomes***********************
            XSSFCell cell19 = rowi.getCell((short) 19);
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
      all_error_details+="<tr><td>"+i+"</td>"+error_details+"</tr>"; 
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
            
                System.out.println("query test n start______ : "+conn.pst);
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
    }
          else { //has errors while loading excel
          obj_det.put("period_error", period_error);       
            }
         
     obj_det.put("sheetname", "2b.Test & Start -Cohort Summary");
     
     return obj_det;
    }
    public JSONObject ARTCurrentLoss (XSSFSheet worksheet, dbConn conn) throws SQLException{
    String all_error_details = "<thead class=\"thead-dark\"><tr><th>Row Number</th><th>County</th><th>Sub County</th><th>Health Facility</th><th>MFL Code</th><th>Patient CCC Number</th><th>Gender</th><th>Current Age</th><th>Date confirmed HIV Positive</th><th>Enrollment date</th><th>ART start date</th><th>Baseline WHO Stage</th><th>Baseline CD4 Count or Percent</th><th>Initial VL Result</th><th>Initial Vl Date</th><th>Repeat VL  result</th><th>Repeat VL date</th><th>VL result at 12 months</th><th>12 month Vl Date</th><th>Last visit date</th><th>Expected return date (TCA)</th><th>Patient Status</th><th>Date patient resumed Tx</th></tr></thead><tbody>";
      String id="",year="",month="",county="",sub_county="",facility="",mfl_code="",ccc_no="",gender="",current_age="",date_confirmed_hiv_pos="",enrollment_date="",art_start_date="",baseline_who_stage="",baseline_cd4_cell_count_perc="",initial_vl="",initial_vl_date="",repeat_vl_value="",repeat_vl_date="",vl_12_months_value="",vl_12_months_date="",last_visit_date="",expected_return_date="",patient_status="",date_resumed_tx="",yearmonth="";
           JSONObject obj_det = new JSONObject();
        String error_details="",period_error = "Error While loading <b>3b. ART Current Net Loss-Var</b> Sheet: <br>";
        boolean has_error=false;
        Iterator rowIterator = worksheet.iterator();

         XSSFRow row = worksheet.getRow(2);
// **************************************Month***********************
            XSSFCell cellmn = row.getCell((short) 4);
            if(cellmn!=null){
            switch (cellmn.getCellType()) {
                   case 0:
                       //numeric
                       month =""+(int)cellmn.getNumericCellValue();
                       break;
                   case 1:
                       //string
                       month =cellmn.getStringCellValue();
                       break;
                   
               }
            }
            if(month.equals("") || cellmn==null){
               has_error=true;  
               period_error+="Missing Month <br>";
            }
            
// **************************************************************************           
// **************************************Year***********************
            XSSFCell cellyr = row.getCell((short) 11);
            if(cellyr!=null){
            switch (cellyr.getCellType()) {
                   case 0:
                       //numeric
                       year =""+(int)cellyr.getNumericCellValue();
                       break;
                   case 1:
                       //string
                       year =cellyr.getStringCellValue();
                       break;
               }
        }
        if(year.equals("") || cellyr==null){
               has_error=true;  
               period_error+="Missing Year <br>";
            }
// ************************************************************************** 
           
            JSONObject period_data = CleanPeriod(year,month,conn);
            
            yearmonth = period_data.get("yearmonth").toString();
            year = period_data.get("year").toString();
            month = period_data.get("month").toString();
            
                    
            
            System.out.println("yearmonth : "+yearmonth+" year : "+year+" month :: "+month);
//*******************************YearMonth************************
    if(!has_error){
        int i=5,y=0,skipped_records=0,added_records=0;
        while(rowIterator.hasNext()){
            error_details = "";
            has_error=false;
        
            id=county=sub_county=facility=mfl_code=ccc_no=gender=current_age=date_confirmed_hiv_pos=enrollment_date=art_start_date=baseline_who_stage=baseline_cd4_cell_count_perc=initial_vl=initial_vl_date=repeat_vl_value=repeat_vl_date=vl_12_months_value=vl_12_months_date=last_visit_date=expected_return_date=patient_status=date_resumed_tx="";

             XSSFRow rowi = worksheet.getRow(i);
                if(rowi==null){
                 break;
                }

// **************************************County***********************
            XSSFCell cell0 = rowi.getCell((short) 0);
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
            XSSFCell cell1 = rowi.getCell((short) 1);
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
            XSSFCell cell2 = rowi.getCell((short) 2);
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
            XSSFCell cell3 = rowi.getCell((short) 3);
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
            }
            error_details+="<td>"+mfl_code+"</td>";
//*******************************End of mflcode************************

// **************************************CCC Number***********************
        
            XSSFCell cell4 = rowi.getCell((short) 4);
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
            }
            error_details+="<td>"+ccc_no+"</td>";
//*******************************End of ccc number************************
               
// **************************************gender***********************
            XSSFCell cell5 = rowi.getCell((short) 5);
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
            XSSFCell cell6 = rowi.getCell((short) 6);
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
          date_confirmed_hiv_pos =""+(int)rowi.getCell(7).getNumericCellValue();  
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
//*******************************End of DATE CONFIRMED hiv pos************************
               
// **************************************enrollment date***********************
if(rowi.getCell(8)!=null){
          if(rowi.getCell(8).getCellType()==0){
          enrollment_date =""+(int)rowi.getCell(8).getNumericCellValue();  
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
//*******************************End of enrollment date************************


// **************************************ART Start date***********************
if(rowi.getCell(9)!=null){
  if(rowi.getCell(9).getCellType()==0){
  art_start_date =""+(int)rowi.getCell(9).getNumericCellValue();  
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
//*******************************End of art start date************************

// ************************************** baseline WHO stage ***********************
            XSSFCell cell10 = rowi.getCell((short) 10);
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
            XSSFCell cell11 = rowi.getCell((short) 11);
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
            XSSFCell cell12 = rowi.getCell((short) 12);
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
  initial_vl_date =""+(int)rowi.getCell(13).getNumericCellValue();  
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
//*******************************End of art start date************************


// **************************************repeat vl results***********************
            XSSFCell cell14 = rowi.getCell((short) 14);
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
      repeat_vl_date =""+(int)rowi.getCell(15).getNumericCellValue();  
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
//*******************************End of repeat vl date************************

// **************************************vl results at 12 months***********************
            XSSFCell cell16 = rowi.getCell((short) 16);
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
           vl_12_months_date =""+(int)rowi.getCell(17).getNumericCellValue();  
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
//*******************************End of 12 months vl date************************


// **************************************last vl date***********************
    if(rowi.getCell(18)!=null){
                 if(rowi.getCell(18).getCellType()==0){
            last_visit_date =""+(int)rowi.getCell(18).getNumericCellValue();  
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
//*******************************End of last vl date************************

// **************************************expected return date***********************
              if(rowi.getCell(19)!=null){
                 if(rowi.getCell(19).getCellType()==0){
            expected_return_date =""+(int)rowi.getCell(19).getNumericCellValue();  
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
//*******************************End of expected return date************************

// **************************************patient status***********************
            XSSFCell cell20 = rowi.getCell((short) 20);
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
            date_resumed_tx =""+(int)rowi.getCell(21).getNumericCellValue();  
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
//*******************************End of date patient resument tx************************

//     generate id

            id = mfl_code+"_"+yearmonth+"_"+ccc_no;

      //       END OF READING VALUES
if(has_error){
  all_error_details+="<tr><td>"+i+"</td>"+error_details+"</tr>"; 
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
            
                System.out.println("query ART current net loss______ : "+conn.pst);
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
    }
          else { //has errors while loading excel
          obj_det.put("period_error", period_error);       
            }

     obj_det.put("sheetname", "3b. ART Current Net Loss-Var");
        
      return obj_det;
    }
    
    public JSONObject Accounting4LinkageSummary (HSSFSheet worksheet, dbConn conn) throws SQLException{
        String all_error_details = "<thead class=\"thead-dark\"><tr><th>Row Number</th><th>County</th><th>Sub-County</th><th>Health Facility</th><th>MFL Code</th><th>Date confirmed HIV positive</th><th>Gender</th><th>Current Age</th><th>Documented in linkage register</th><th>Patient CCC Number</th><th>Enrolment Date</th><th>Referred</th><th>Enrolled to other site</th><th>Enrolled from other site</th><th>ART start date</th><th>Started ART in this facility</th><th>Started in other facility</th><th>Patient Status</th><th>If declined, indicate reason</th><th>If dead, indicate reported  cause of death</th></tr></thead><tbody>";
        String id="",period_details="",year="",month="",county="",sub_county="",facility="",mfl_code="",date_confirmed_hiv_pos="",gender="",current_age="",documented_linkage_register="",ccc_no="",enrollment_date="",referred="",enrolled_to_other_site="",enrolled_from_other_site="",art_start_date="",started_art_in_this_facility="",started_art_in_other_facility="",patient_status="",declined_reason="",reported_cause_of_death="",yearmonth="";
        JSONObject obj_det = new JSONObject();
         String error_details="",period_error = "Error While loading <b>1b. Accounting for Linkage</b> Sheet: <br>";
        boolean has_error=false;
        
        Iterator rowIterator = worksheet.iterator();

         HSSFRow row = worksheet.getRow(2);
// **************************************Month***********************
            HSSFCell cellmn = row.getCell((short) 4);
            switch (cellmn.getCellType()) {
                   case 0:
                       //numeric
                       month =""+(int)cellmn.getNumericCellValue();
                       break;
                   case 1:
                       //string
                       month =cellmn.getStringCellValue();
                       break;
                   
               }
            if(month.equals("") || cellmn==null){
               has_error=true;  
               period_error+="Missing Month <br>";
            }
// **************************************************************************           
// **************************************Year***********************
            HSSFCell cellyr = row.getCell((short) 12);
            switch (cellyr.getCellType()) {
                   case 0:
                       //numeric
                       year =""+(int)cellyr.getNumericCellValue();
                       break;
                   case 1:
                       //string
                       year =cellyr.getStringCellValue();
                       break;
               }
            if(year.equals("") || cellyr==null){
               has_error=true;  
               period_error+="Missing Year <br>";
            }
// **************************************************************************           
            System.out.println("perid details : "+period_details);
            
            JSONObject period_data = CleanPeriod(year,month,conn);
            
            yearmonth = period_data.get("yearmonth").toString();
            year = period_data.get("year").toString();
            month = period_data.get("month").toString();
            
                    
            
            System.out.println("perid details : yearmonth : "+yearmonth+" year : "+year+" month :: "+month);
//*******************************YearMonth************************

        if(!has_error){
        int i=5,y=0,skipped_records=0,added_records=0;
        while(rowIterator.hasNext()){
            error_details = "";
            has_error=false;
        
            id=period_details=county=sub_county=facility=mfl_code=date_confirmed_hiv_pos=gender=current_age=documented_linkage_register=ccc_no=enrollment_date=referred=enrolled_to_other_site=enrolled_from_other_site=art_start_date=started_art_in_this_facility=started_art_in_other_facility=patient_status=declined_reason=reported_cause_of_death="";

             HSSFRow rowi = worksheet.getRow(i);
                if(rowi==null){
                 break;
                }

// **************************************County***********************
            HSSFCell cell0 = rowi.getCell((short) 0);
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
            HSSFCell cell1 = rowi.getCell((short) 1);
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
            HSSFCell cell2 = rowi.getCell((short) 2);
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
            HSSFCell cell3 = rowi.getCell((short) 3);
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
            }
            error_details+="<td>"+ccc_no+"</td>";
//*******************************End of mflcode************************

// **************************************date confirmed HIV pos***********************
        if(rowi.getCell(4)!=null){
        if(rowi.getCell(4).getCellType()==0){
          date_confirmed_hiv_pos =""+(int)rowi.getCell(4).getNumericCellValue();  
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
//*******************************End of date confirmed HIV pos************************
               
// **************************************gender***********************
            HSSFCell cell5 = rowi.getCell((short) 5);
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
            HSSFCell cell6 = rowi.getCell((short) 6);
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
            HSSFCell cell7 = rowi.getCell((short) 7);
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
            HSSFCell cell8 = rowi.getCell((short) 8);
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
            if(ccc_no.equals("")){
               has_error=true;  
            }
            error_details+="<td>"+ccc_no+"</td>";
//*******************************End of patient ccc number************************


// **************************************enrollment date***********************
    if(rowi.getCell(9)!=null){
  if(rowi.getCell(9).getCellType()==0){
  enrollment_date =""+(int)rowi.getCell(9).getNumericCellValue();  
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
//*******************************End of enrollment date************************


// **************************************referred***********************
            HSSFCell cell10 = rowi.getCell((short) 10);
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
            HSSFCell cell11 = rowi.getCell((short) 11);
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
            HSSFCell cell12 = rowi.getCell((short) 12);
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
  art_start_date =""+(int)rowi.getCell(13).getNumericCellValue();  
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
//*******************************End of art start date************************


// **************************************started art in this facility***********************
            HSSFCell cell14 = rowi.getCell((short) 14);
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
            HSSFCell cell15 = rowi.getCell((short) 15);
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
            HSSFCell cell16 = rowi.getCell((short) 16);
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
            HSSFCell cell17 = rowi.getCell((short) 17);
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
            HSSFCell cell18 = rowi.getCell((short) 18);
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

            id = mfl_code+"_"+yearmonth+"_"+ccc_no;

      //       END OF READING VALUES
    if(has_error){
      all_error_details+="<tr><td>"+i+"</td>"+error_details+"</tr>"; 
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
            
                System.out.println("query : "+conn.pst);
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
    }
          else { //has errors while loading excel
          obj_det.put("period_error", period_error);       
            }

     obj_det.put("sheetname", "1b. Accounting for Linkage");
     
        
      
      return obj_det;
    }
    public JSONObject TestStartSummary (HSSFSheet worksheet, dbConn conn) throws SQLException{
        String all_error_details="<thead class=\"thead-dark\"><tr><th>Row Number</th><th>County</th><th>Subcounty</th><th>Health Facility</th><th>MFL Code</th><th>Patient CCC Number</th><th>Gender</th><th>Current Age</th><th>Date confirmed HIV Positive</th><th>Enrollment date</th><th>ART start date</th><th>Baseline WHO Stage</th><th>Baseline CD4 Count or Percent</th><th>Initial VL Result</th><th>Initial Vl Date</th><th>Repeat VL  result</th><th>Repeat VL date</th><th>VL result at 12 months</th><th>12 month Vl Date</th><th>Last visit date</th><th>Patient Outcome</th></tr></thead><tbody>";
      String id="",year="",month="",county="",sub_county="",facility="",mfl_code="",ccc_no="",gender="",current_age="",date_confirmed_hiv_pos="",enrollment_date="",art_start_date="",baseline_who_stage="",baseline_cd4_cell_count_perc="",initial_vl="",initial_vl_date="",repeat_vl_value="",repeat_vl_date="",vl_12_months_value="",vl_12_months_date="",last_visit_date="",patient_outcome="",yearmonth="";
       JSONObject obj_det = new JSONObject();
        String error_details="",period_error = "Error While loading <b>2b.Test & Start -Cohort Summary</b> Sheet: <br>";
        boolean has_error=false;
        
        Iterator rowIterator = worksheet.iterator();

         HSSFRow row = worksheet.getRow(2);
// **************************************Month***********************
            HSSFCell cellmn = row.getCell((short) 4);
            if(cellmn!=null){
            switch (cellmn.getCellType()) {
                   case 0:
                       //numeric
                       month =""+(int)cellmn.getNumericCellValue();
                       break;
                   case 1:
                       //string
                       month =cellmn.getStringCellValue();
                       break;
                   
               }
            }
            if(month.equals("") || cellmn==null){
               has_error=true;  
               period_error+="Missing Month <br>";
            }
// **************************************************************************           
// **************************************Year***********************
            HSSFCell cellyr = row.getCell((short) 11);
            if(cellyr!=null){
            switch (cellyr.getCellType()) {
                   case 0:
                       //numeric
                       year =""+(int)cellyr.getNumericCellValue();
                       break;
                   case 1:
                       //string
                       year =cellyr.getStringCellValue();
                       break;
               }
        }
            if(year.equals("") || cellyr==null){
               has_error=true;  
               period_error+="Missing Year <br>";
            }
// ************************************************************************** 
            
            JSONObject period_data = CleanPeriod(year,month,conn);
            
            yearmonth = period_data.get("yearmonth").toString();
            year = period_data.get("year").toString();
            month = period_data.get("month").toString();
            
                    
            
            System.out.println("yearmonth : "+yearmonth+" year : "+year+" month :: "+month);
//*******************************YearMonth************************

        
         if(!has_error){
        int i=5,y=0,skipped_records=0,added_records=0;
        while(rowIterator.hasNext()){
            error_details = "";
            has_error=false;
        
            id=county=sub_county=facility=mfl_code=ccc_no=gender=current_age=date_confirmed_hiv_pos=enrollment_date=art_start_date=baseline_who_stage=baseline_cd4_cell_count_perc=initial_vl=initial_vl_date=repeat_vl_value=repeat_vl_date=vl_12_months_value=vl_12_months_date=last_visit_date=patient_outcome="";
      
             HSSFRow rowi = worksheet.getRow(i);
                if(rowi==null){
                 break;
                }

// **************************************County***********************
            HSSFCell cell0 = rowi.getCell((short) 0);
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
            HSSFCell cell1 = rowi.getCell((short) 1);
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
            HSSFCell cell2 = rowi.getCell((short) 2);
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
            HSSFCell cell3 = rowi.getCell((short) 3);
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
            }
            error_details+="<td>"+mfl_code+"</td>";
//*******************************End of mflcode************************

// **************************************CCC Number***********************
        
            HSSFCell cell4 = rowi.getCell((short) 4);
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
            }
            error_details+="<td>"+ccc_no+"</td>";
//*******************************End of ccc number************************
               
// **************************************gender***********************
            HSSFCell cell5 = rowi.getCell((short) 5);
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
            HSSFCell cell6 = rowi.getCell((short) 6);
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
          date_confirmed_hiv_pos =""+(int)rowi.getCell(7).getNumericCellValue();  
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
//*******************************End of DATE CONFIRMED hiv pos************************
               
// **************************************enrollment date***********************
if(rowi.getCell(8)!=null){
          if(rowi.getCell(8).getCellType()==0){
          enrollment_date =""+(int)rowi.getCell(8).getNumericCellValue();  
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
//*******************************End of enrollment date************************


// **************************************ART Start date***********************
if(rowi.getCell(9)!=null){
  if(rowi.getCell(9).getCellType()==0){
  art_start_date =""+(int)rowi.getCell(9).getNumericCellValue();  
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
//*******************************End of art start date************************

// ************************************** baseline WHO stage ***********************
            HSSFCell cell10 = rowi.getCell((short) 10);
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
            HSSFCell cell11 = rowi.getCell((short) 11);
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
            HSSFCell cell12 = rowi.getCell((short) 12);
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
  initial_vl_date =""+(int)rowi.getCell(13).getNumericCellValue();  
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
//*******************************End of art start date************************


// **************************************repeat vl results***********************
            HSSFCell cell14 = rowi.getCell((short) 14);
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
      repeat_vl_date =""+(int)rowi.getCell(15).getNumericCellValue();  
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
//*******************************End of repeat vl date************************

// **************************************vl results at 12 months***********************
            HSSFCell cell16 = rowi.getCell((short) 16);
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
           vl_12_months_date =""+(int)rowi.getCell(17).getNumericCellValue();  
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
//*******************************End of 12 months vl date************************


// **************************************last vl date***********************
    if(rowi.getCell(18)!=null){
                 if(rowi.getCell(18).getCellType()==0){
            last_visit_date =""+(int)rowi.getCell(18).getNumericCellValue();  
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
//*******************************End of last vl date************************

// **************************************patient outcomes***********************
            HSSFCell cell19 = rowi.getCell((short) 19);
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
      all_error_details+="<tr><td>"+i+"</td>"+error_details+"</tr>"; 
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
            
                System.out.println("query test n start______ : "+conn.pst);
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
    }
          else { //has errors while loading excel
          obj_det.put("period_error", period_error);       
            }
         
     obj_det.put("sheetname", "2b.Test & Start -Cohort Summary");
     return obj_det;
    }
    public JSONObject ARTCurrentLoss (HSSFSheet worksheet, dbConn conn) throws SQLException{
    String all_error_details = "<thead class=\"thead-dark\"><tr><th>Row Number</th><th>County</th><th>Sub County</th><th>Health Facility</th><th>MFL Code</th><th>Patient CCC Number</th><th>Gender</th><th>Current Age</th><th>Date confirmed HIV Positive</th><th>Enrollment date</th><th>ART start date</th><th>Baseline WHO Stage</th><th>Baseline CD4 Count or Percent</th><th>Initial VL Result</th><th>Initial Vl Date</th><th>Repeat VL  result</th><th>Repeat VL date</th><th>VL result at 12 months</th><th>12 month Vl Date</th><th>Last visit date</th><th>Expected return date (TCA)</th><th>Patient Status</th><th>Date patient resumed Tx</th></tr></thead><tbody>";
      String id="",year="",month="",county="",sub_county="",facility="",mfl_code="",ccc_no="",gender="",current_age="",date_confirmed_hiv_pos="",enrollment_date="",art_start_date="",baseline_who_stage="",baseline_cd4_cell_count_perc="",initial_vl="",initial_vl_date="",repeat_vl_value="",repeat_vl_date="",vl_12_months_value="",vl_12_months_date="",last_visit_date="",expected_return_date="",patient_status="",date_resumed_tx="",yearmonth="";
           JSONObject obj_det = new JSONObject();
        String error_details="",period_error = "Error While loading <b>3b. ART Current Net Loss-Var</b> Sheet: <br>";
        boolean has_error=false;
        Iterator rowIterator = worksheet.iterator();

         HSSFRow row = worksheet.getRow(2);
// **************************************Month***********************
            HSSFCell cellmn = row.getCell((short) 4);
            if(cellmn!=null){
            switch (cellmn.getCellType()) {
                   case 0:
                       //numeric
                       month =""+(int)cellmn.getNumericCellValue();
                       break;
                   case 1:
                       //string
                       month =cellmn.getStringCellValue();
                       break;
                   
               }
            }
            if(month.equals("") || cellmn==null){
               has_error=true;  
               period_error+="Missing Month <br>";
            }
            
// **************************************************************************           
// **************************************Year***********************
            HSSFCell cellyr = row.getCell((short) 11);
            if(cellyr!=null){
            switch (cellyr.getCellType()) {
                   case 0:
                       //numeric
                       year =""+(int)cellyr.getNumericCellValue();
                       break;
                   case 1:
                       //string
                       year =cellyr.getStringCellValue();
                       break;
               }
        }
        if(year.equals("") || cellyr==null){
               has_error=true;  
               period_error+="Missing Year <br>";
            }
// ************************************************************************** 
           
            JSONObject period_data = CleanPeriod(year,month,conn);
            
            yearmonth = period_data.get("yearmonth").toString();
            year = period_data.get("year").toString();
            month = period_data.get("month").toString();
            
                    
            
            System.out.println("yearmonth : "+yearmonth+" year : "+year+" month :: "+month);
//*******************************YearMonth************************
    if(!has_error){
        int i=5,y=0,skipped_records=0,added_records=0;
        while(rowIterator.hasNext()){
            error_details = "";
            has_error=false;
        
            id=county=sub_county=facility=mfl_code=ccc_no=gender=current_age=date_confirmed_hiv_pos=enrollment_date=art_start_date=baseline_who_stage=baseline_cd4_cell_count_perc=initial_vl=initial_vl_date=repeat_vl_value=repeat_vl_date=vl_12_months_value=vl_12_months_date=last_visit_date=expected_return_date=patient_status=date_resumed_tx="";

             HSSFRow rowi = worksheet.getRow(i);
                if(rowi==null){
                 break;
                }

// **************************************County***********************
            HSSFCell cell0 = rowi.getCell((short) 0);
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
            HSSFCell cell1 = rowi.getCell((short) 1);
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
            HSSFCell cell2 = rowi.getCell((short) 2);
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
            HSSFCell cell3 = rowi.getCell((short) 3);
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
            }
            error_details+="<td>"+mfl_code+"</td>";
//*******************************End of mflcode************************

// **************************************CCC Number***********************
        
            HSSFCell cell4 = rowi.getCell((short) 4);
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
            }
            error_details+="<td>"+ccc_no+"</td>";
//*******************************End of ccc number************************
               
// **************************************gender***********************
            HSSFCell cell5 = rowi.getCell((short) 5);
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
            HSSFCell cell6 = rowi.getCell((short) 6);
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
          date_confirmed_hiv_pos =""+(int)rowi.getCell(7).getNumericCellValue();  
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
//*******************************End of DATE CONFIRMED hiv pos************************
               
// **************************************enrollment date***********************
if(rowi.getCell(8)!=null){
          if(rowi.getCell(8).getCellType()==0){
          enrollment_date =""+(int)rowi.getCell(8).getNumericCellValue();  
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
//*******************************End of enrollment date************************


// **************************************ART Start date***********************
if(rowi.getCell(9)!=null){
  if(rowi.getCell(9).getCellType()==0){
  art_start_date =""+(int)rowi.getCell(9).getNumericCellValue();  
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
//*******************************End of art start date************************

// ************************************** baseline WHO stage ***********************
            HSSFCell cell10 = rowi.getCell((short) 10);
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
            HSSFCell cell11 = rowi.getCell((short) 11);
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
            HSSFCell cell12 = rowi.getCell((short) 12);
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
  initial_vl_date =""+(int)rowi.getCell(13).getNumericCellValue();  
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
//*******************************End of art start date************************


// **************************************repeat vl results***********************
            HSSFCell cell14 = rowi.getCell((short) 14);
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
      repeat_vl_date =""+(int)rowi.getCell(15).getNumericCellValue();  
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
//*******************************End of repeat vl date************************

// **************************************vl results at 12 months***********************
            HSSFCell cell16 = rowi.getCell((short) 16);
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
           vl_12_months_date =""+(int)rowi.getCell(17).getNumericCellValue();  
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
//*******************************End of 12 months vl date************************


// **************************************last vl date***********************
    if(rowi.getCell(18)!=null){
                 if(rowi.getCell(18).getCellType()==0){
            last_visit_date =""+(int)rowi.getCell(18).getNumericCellValue();  
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
//*******************************End of last vl date************************

// **************************************expected return date***********************
              if(rowi.getCell(19)!=null){
                 if(rowi.getCell(19).getCellType()==0){
            expected_return_date =""+(int)rowi.getCell(19).getNumericCellValue();  
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
//*******************************End of expected return date************************

// **************************************patient status***********************
            HSSFCell cell20 = rowi.getCell((short) 20);
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
            date_resumed_tx =""+(int)rowi.getCell(21).getNumericCellValue();  
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
//*******************************End of date patient resument tx************************

//     generate id

            id = mfl_code+"_"+yearmonth+"_"+ccc_no;

      //       END OF READING VALUES
if(has_error){
  all_error_details+="<tr><td>"+i+"</td>"+error_details+"</tr>"; 
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
            
                System.out.println("query ART current net loss______ : "+conn.pst);
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
    }
          else { //has errors while loading excel
          obj_det.put("period_error", period_error);       
            }

     
     obj_det.put("sheetname", "3b. ART Current Net Loss-Var");
        
      
      return obj_det;
    }
    
 
 
    
}