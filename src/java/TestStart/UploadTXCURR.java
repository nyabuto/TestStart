/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TestStart;

import Db.OSValidator;
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
import org.apache.poi.ss.usermodel.Cell;
import static org.apache.poi.ss.usermodel.CellType.NUMERIC;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


/**
 *
 * @author GNyabuto
 */
@MultipartConfig
public class UploadTXCURR extends HttpServlet {

HttpSession session;
  private static final long serialVersionUID = 205242440643911308L;
  private static final String UPLOAD_DIR = "uploads";
  int row_skipped,rows_added,skipped_info;
  String full_path="";
  String fileName="";
  File file_source;
  SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
  String output=""; 
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
       dbConn conn = new dbConn();
       session = request.getSession();
             
       Sheet  worksheet = null;
        output=""; 
        String applicationPath = request.getServletContext().getRealPath("");
         String uploadFilePath = applicationPath + File.separator + UPLOAD_DIR;
         session=request.getSession();
          File fileSaveDir = new File(uploadFilePath);
        if (!fileSaveDir.exists()) {
            fileSaveDir.mkdirs();
        }
        System.out.println("started");
        for (Part part : request.getParts()) {
            if(!getFileName(part).equals("")){
           fileName = getFileName(part);
            part.write(uploadFilePath + File.separator + fileName);
            
            if(OSValidator.isWindows()){
            full_path=fileSaveDir.getAbsolutePath()+"\\"+fileName;
            }
            else if(OSValidator.isUnix()){
             full_path=fileSaveDir.getAbsolutePath()+"/"+fileName;   
            }
            System.out.println("fullpath : "+full_path);
           // read the contents of the workbook and sheets here
           
           FileInputStream fileInputStream = new FileInputStream(full_path);
            
//           obj_files.put("file_name", fileName);
           
//****************************WORKBOOK INFORMATION****************************************
        //Call to read all sheets.
              
       
       //call sheet for accounting for linkages--validation
       
       Workbook workbook = null;
       if(fileName.endsWith(".xlsx")){
       workbook = new XSSFWorkbook(fileInputStream);
       output+="<br><b><u>File Name:"+fileName+"</u></b><br>";
       }
       else{
           System.out.println("Wrong file format");
           output+="<br><b><u>Wrong file format for file "+fileName+". Your file must end with .xlsx </u></b><br>";
       }
        if(workbook.getSheet("tx_data")!=null){
            System.out.println("read data for "+fileName);
            
        worksheet = workbook.getSheet("tx_data");
        readSheet(worksheet,conn);
            }
        //end of reading all sheets
 
        }
    }
        session.setAttribute("output", output);

        System.out.println(output);
      response.sendRedirect("TXCURRUpload.jsp");
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
        Logger.getLogger(UploadTXCURR.class.getName()).log(Level.SEVERE, null, ex);
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
        Logger.getLogger(UploadTXCURR.class.getName()).log(Level.SEVERE, null, ex);
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
   
        
    public boolean facilityExist(dbConn conn, String mflcode) throws SQLException{
boolean exist = false;
         String getFacilDetails="SELECT SubPartnerNom FROM  subpartnera where CentreSanteId=? AND ART=?";
            conn.pst2 = conn.conn.prepareStatement(getFacilDetails);
            conn.pst2.setString(1, mflcode);
            conn.pst2.setInt(2, 1);
            conn.rs2 = conn.pst2.executeQuery();
            if(conn.rs2.next()){
              exist=true;  
             }
        return exist;
    }
        
        
        
    public String readSheet (Sheet worksheet, dbConn conn) throws SQLException{
        
        String mflcode=""; //12
        String id,serialNo,review_date,ccc_no,is_ti,oct_17,nov_17,dec_17,jan_18,feb_18,mar_18,apr_18,may_18,jun_18,jul_18,month_due_vl;
String header="";
         String all_error_details="", error_details="",reason="";
        boolean has_error=false;
        
        Iterator rowIterator = worksheet.iterator();

//*******************************Header************************
    Row rowHeader = worksheet.getRow(0);

    
     Cell cellHeader = rowHeader.getCell((short) 3);
  if(cellHeader.getCellType() == Cell.CELL_TYPE_FORMULA) {
        switch(cellHeader.getCachedFormulaResultType()) {
            case Cell.CELL_TYPE_NUMERIC:
                header =""+(int) cellHeader.getNumericCellValue();
                break;
            case Cell.CELL_TYPE_STRING:
              header   =  cellHeader.getStringCellValue();
                break;
        }
     }
  else{
                  switch (cellHeader.getCellType()) {
                   case 0:
                       //numeric
                       header =""+(int)cellHeader.getNumericCellValue();
                       break;
                   case 1:
                       //string
                       header =cellHeader.getStringCellValue();
                       break;
                }
   
  }
  //end of header row
  
        System.out.println("header : "+header);
 if(header.contains("v1.0")) {
//*******************************MFL Code************************
    Row rowExcel = worksheet.getRow(3);

    
     Cell cellmfl = rowExcel.getCell((short) 12);
  if(cellmfl.getCellType() == Cell.CELL_TYPE_FORMULA) {
//        System.out.println("Formula is " + cellmfl.getCellFormula());
        switch(cellmfl.getCachedFormulaResultType()) {
            case Cell.CELL_TYPE_NUMERIC:
                mflcode =""+(int) cellmfl.getNumericCellValue();
                break;
            case Cell.CELL_TYPE_STRING:
              mflcode   =  cellmfl.getStringCellValue();
                break;
        }
     }
  System.out.println("mfl code :"+mflcode);
    if(facilityExist(conn, mflcode)){ //check if facility exist...
        int i=7,y=0,skipped_records=0,added_records=0;
        while(rowIterator.hasNext()){
            id=serialNo=review_date=ccc_no=is_ti=oct_17=nov_17=dec_17=jan_18=feb_18=mar_18=apr_18=may_18=jun_18=jul_18=month_due_vl="";
            error_details = "";
            has_error=false;
        
             Row rowi = worksheet.getRow(i);
                if(rowi==null){
                 break;
                }

// **************************************SerialNo***********************
    Cell cell0 = rowi.getCell((short) 0);
  if(cell0.getCellType() == Cell.CELL_TYPE_FORMULA) {
//        System.out.println("Formula is " + cell0.getCellFormula());
        switch(cell0.getCachedFormulaResultType()) {
            case Cell.CELL_TYPE_NUMERIC:
                serialNo =""+(int) cell0.getNumericCellValue();
                break;
            case Cell.CELL_TYPE_STRING:
              serialNo   =  cell0.getStringCellValue();
                break;
        }
     }
  else{
            switch (cell0.getCellType()) {
                   case 0:
                       //numeric
                       serialNo =""+(int)cell0.getNumericCellValue();
                       break;
                   case 1:
                       //string
                       serialNo =cell0.getStringCellValue();
                       break;
                }
          
  }
            error_details+="<td>"+serialNo+"</td>";
//*******************************End of serial no************************
               
// **************************************Review Date***********************
            
    if(rowi.getCell(1)!=null){
    if(rowi.getCell(1).getCellType()==0){
     review_date =""+dateformat.format(rowi.getCell(1).getDateCellValue());
       }
    else{
    if (rowi.getCell(1).getCellTypeEnum().equals(NUMERIC)) {
            review_date = dateformat.format(rowi.getCell(1).getDateCellValue());
        }
        else{
         review_date = rowi.getCell(1).getStringCellValue();
        }
      }
    }
      if(review_date.equals("")){
                  has_error=true;
                  reason = "Missing Review Date<br>";
                 break;
      }
      
      
  error_details+="<td>"+review_date+"</td>";   
      
//*******************************End of review date************************

// **************************************Patient CCC No***********************
            Cell cell2 = rowi.getCell((short) 2);
              if(cell2!=null){
            switch (cell2.getCellType()) {
                   case 0:
                       //numeric
                       ccc_no =""+(int)cell2.getNumericCellValue();
                       break;
                   case 1:
                       //string
                       ccc_no =cell2.getStringCellValue();
                       break;
                   
               }
        }
              
              if(ccc_no.equals("")){
                  has_error=true;
                  reason = "Missing CCC Number<br>";
                  break;
              }
              
            error_details+="<td>"+ccc_no+"</td>";
//*******************************End of patient CCC Number************************;
               
// **************************************IS Patient TI***********************
            Cell cell3 = rowi.getCell((short) 3);
              if(cell3!=null){
            switch (cell3.getCellType()) {
                   case 0:
                       //numeric
                       is_ti =""+(int)cell3.getNumericCellValue();
                       break;
                   case 1:
                       //string
                       is_ti =cell3.getStringCellValue();
                       break;
               }
            }
        
              if(is_ti.equals("")){
                  has_error=true;
                  reason = "Missing is Transfer in(TI)<br>";
              }
              
            error_details+="<td>"+is_ti+"</td>";
//*******************************End of is patient TI************************

// **************************************Oct 2017***********************
          Cell cell4 = rowi.getCell((short) 4);
              if(cell4!=null){
            switch (cell4.getCellType()) {
                   case 0:
                       //numeric
                       oct_17 =""+(int)cell4.getNumericCellValue();
                       break;
                   case 1:
                       //string
                       oct_17 =cell4.getStringCellValue();
                       break;
               }
              }
            if(oct_17.equals("")){
//                  has_error=true;
//                  reason = "Missing Status for Oct 2017<br>";
              }  
              
            error_details+="<td>"+oct_17+"</td>";
//*******************************End of oct 2017************************
               
// **************************************Nov 2017***********************
            Cell cell5 = rowi.getCell((short) 5);
              if(cell5!=null){
            switch (cell5.getCellType()) {
                   case 0:
                       //numeric
                       nov_17 =""+(int)cell5.getNumericCellValue();
                       break;
                   case 1:
                       //string
                       nov_17 =cell5.getStringCellValue();
                       break;
               }
              }
              if(nov_17.equals("")){
//                  has_error=true;
//                  reason = "Missing Status for Nov 2017<br>";
              } 
            error_details+="<td>"+nov_17+"</td>";
//*******************************End of Nov 2017************************

               
// **************************************Dec 2017***********************
            Cell cell6 = rowi.getCell((short) 6);
              if(cell6!=null){
            switch (cell6.getCellType()) {
                   case 0:
                       //numeric
                       dec_17 =""+(int)cell6.getNumericCellValue();
                       break;
                   case 1:
                       //string
                       dec_17 =cell6.getStringCellValue();
                       break;
                   
               }
              }
              
              if(dec_17.equals("")){
//                  has_error=true;
//                  reason = "Missing Status for Dec 2017<br>";
              } 
              
            error_details+="<td>"+dec_17+"</td>";
//*******************************End of dec 2017************************
             
// **************************************Jan 2018***********************
            Cell cell7 = rowi.getCell((short) 7);
            if(cell7!=null){
            switch (cell7.getCellType()) {
                   case 0:
                       //numeric
                       jan_18 =""+(int)cell7.getNumericCellValue();
                       break;
                   case 1:
                       //string
                       jan_18 =cell7.getStringCellValue();
                       break;
                  
               }
            }
            
            if(jan_18.equals("")){
//                  has_error=true;
//                  reason = "Missing Status for Jan 2018<br>";
              } 
            
            error_details+="<td>"+jan_18+"</td>";
//*******************************End of jan 2018************************
               
// **************************************Feb 2018***********************
            Cell cell8 = rowi.getCell((short) 8);
            if(cell8!=null){
            switch (cell8.getCellType()) {
                   case 0:
                       //numeric
                       feb_18=""+(int)cell8.getNumericCellValue();
                       break;
                   case 1:
                       //string
                       feb_18 =cell8.getStringCellValue();
                       break;
                  
               }
            }
            
            
            if(feb_18.equals("")){
//                  has_error=true;
//                  reason = "Missing Status for Feb 2018<br>";
              } 
            
            error_details+="<td>"+feb_18+"</td>";
//*******************************End of Feb 2018************************


// **************************************Mar 2018***********************
   Cell cell9 = rowi.getCell((short) 9);
            if(cell9!=null){
            switch (cell9.getCellType()) {
                   case 0:
                       //numeric
                       mar_18 =""+(int)cell9.getNumericCellValue();
                       break;
                   case 1:
                       //string
                       mar_18 =cell9.getStringCellValue();
                       break;
                   
               } 
            
            }
            
            
            if(mar_18.equals("")){
//                  has_error=true;
//                  reason = "Missing Status for Mar 2018<br>";
              } 
            
            error_details+="<td>"+mar_18+"</td>";
//*******************************End of Mar 2018************************


// **************************************Apr 2018***********************
            Cell cell10 = rowi.getCell((short) 10);
            if(cell10!=null){
            switch (cell10.getCellType()) {
                   case 0:
                       //numeric
                       apr_18 =""+(int)cell10.getNumericCellValue();
                       break;
                   case 1:
                       //string
                       apr_18 =cell10.getStringCellValue();
                       break;
                   
               } 
            }
            
            if(apr_18.equals("")){
//                  has_error=true;
//                  reason = "Missing Status for Apr 2018<br>";
              } 
            error_details+="<td>"+apr_18+"</td>";
//*******************************End of Apr 2018************************


// **************************************May 2018***********************
            Cell cell11 = rowi.getCell((short) 11);
            if(cell11!=null){
            switch (cell11.getCellType()) {
                   case 0:
                       //numeric
                       may_18 =""+(int)cell11.getNumericCellValue();
                       break;
                   case 1:
                       //string
                       may_18 =cell11.getStringCellValue();
                       break;
                   
               } 
            }
            
            
            if(may_18.equals("")){
//                  has_error=true;
//                  reason = "Missing Status for May 2018<br>";
              } 
            
            error_details+="<td>"+may_18+"</td>";
//*******************************End of may 2018************************


// **************************************Jun 2018***********************
            Cell cell12 = rowi.getCell((short) 12);
            if(cell12!=null){
            switch (cell12.getCellType()) {
                   case 0:
                       //numeric
                       jun_18 =""+(int)cell12.getNumericCellValue();
                       break;
                   case 1:
                       //string
                       jun_18 =cell12.getStringCellValue();
                       break;
                  
               }
            }
            
            if(jun_18.equals("")){
//                  has_error=true;
//                  reason = "Missing Status for Jun 2018<br>";
              } 
            
            error_details+="<td>"+jun_18+"</td>";
//*******************************End of june 2018************************

// **************************************Jul 2018***********************
         Cell cell13 = rowi.getCell((short) 13);
        if(cell13!=null){
            switch (cell13.getCellType()) {
                   case 0:
                       //numeric
                       jul_18 =""+(int)cell13.getNumericCellValue();
                       break;
                   case 1:
                       //string
                       jul_18 =cell13.getStringCellValue();
                       break;
                   
               }}
        
            if(jul_18.equals("")){
//                  has_error=true;
//                  reason = "Missing Status for Jul 2018<br>";
              } 
            error_details+="<td>"+jul_18+"</td>";
//*******************************End of jul 2018************************


// **************************************Month Due VL***********************
            Cell cell14 = rowi.getCell((short) 14);
        if(cell14!=null){
            switch (cell14.getCellType()) {
                   case 0:
                       //numeric
                       month_due_vl =""+(int)cell14.getNumericCellValue();
                       break;
                   case 1:
                       //string
                       month_due_vl =cell14.getStringCellValue();
                       break;
                   
               }
        }
            error_details+="<td>"+month_due_vl+"</td>";   
//*******************************End of MONTH due VL************************

//     generate id
        if(!ccc_no.equals("") && !mflcode.equals("")){
            id = mflcode+"_"+ccc_no;
        }
        
        
        //check on dead cases
        if(oct_17.equals("Dead")){
            nov_17 = dec_17 = jan_18 = feb_18 = mar_18 = apr_18 = may_18 = jun_18 = jul_18 = "";
        }
        else if(nov_17.equals("Dead")){
         dec_17 = jan_18 = feb_18 = mar_18 = apr_18 = may_18 = jun_18 = jul_18 = "";
        }
        else if(dec_17.equals("Dead")){
         jan_18 = feb_18 = mar_18 = apr_18 = may_18 = jun_18 = jul_18 = "";
        }
        else if(jan_18.equals("Dead")){
         feb_18 = mar_18 = apr_18 = may_18 = jun_18 = jul_18 = "";
        }
        else if(feb_18.equals("Dead")){
         mar_18 = apr_18 = may_18 = jun_18 = jul_18 = "";
        }
        else if(mar_18.equals("Dead")){
         apr_18 = may_18 = jun_18 = jul_18 = "";
        }
        else if(apr_18.equals("Dead")){
         may_18 = jun_18 = jul_18 = "";
        }
        else if(may_18.equals("Dead")){
         jun_18 = jul_18 = "";
        }
        else if(jun_18.equals("Dead")){
         jul_18 = "";
        }
       

      //       END OF READING VALUES
    if(has_error){
      all_error_details+="<tr>"+error_details+"<td>"+reason+"</td></tr>"; 
      skipped_records++;
    }
    else{
               added_records++;
               String query = "REPLACE INTO tx_curr SET id=?,mflcode=?,serialNo=?,review_date=?,ccc_no=?,is_ti=?,oct_17=?,nov_17=?,dec_17=?,jan_18=?,feb_18=?,mar_18=?,apr_18=?,may_18=?,jun_18=?,jul_18=?,month_due_vl=?";
                conn.pst = conn.conn.prepareStatement(query);
                conn.pst.setString(1, id);
                conn.pst.setString(2, mflcode);
                conn.pst.setString(3, serialNo);
                conn.pst.setString(4, review_date);
                conn.pst.setString(5, ccc_no);
                conn.pst.setString(6, is_ti);
                conn.pst.setString(7, oct_17);
                conn.pst.setString(8, nov_17);
                conn.pst.setString(9, dec_17);
                conn.pst.setString(10, jan_18);
                conn.pst.setString(11, feb_18);
                conn.pst.setString(12, mar_18);
                conn.pst.setString(13, apr_18);
                conn.pst.setString(14, may_18);
                conn.pst.setString(15, jun_18);
                conn.pst.setString(16, jul_18);
                conn.pst.setString(17, month_due_vl);
                conn.pst.executeUpdate();
            
                conn.pst.executeUpdate();
            
            }
    
    i++;
        }
        
        if(skipped_records>0){
                  output+="<b><u>"+added_records+"</u> Records added/updated successfully.</b><br>"
                          + "<font color=\"red\"><b>Record not Added</b></font></br>"
                          + "<table  class=\"table\" style=\"font-size:12px;\">"
                          + "<thead class=\"thead-dark\">"
                          + "<tr>"
                          + "<th>Serial Number</th>"
                          + "<th>Review Date</th>"
                          + "<th>Patient CCC Number</th>"
                          + "<th>Is Patient T.I?</th>"
                          + "<th>Oct-2017</th>"
                          + "<th>Nov-2017</th>"
                          + "<th>Dec-2017</th>"
                          + "<th>Jan-2018</th>"
                          + "<th>Feb-2018</th>"
                          + "<th>Mar-2018</th>"
                          + "<th>Apr-2018</th>"
                          + "<th>May-2018</th>"
                          + "<th>Jun-2018</th>"
                          + "<th>Jul-2018</th>"
                          + "<th>Month Patient Due for VL</th>"
                          + "<th>Reason</th>"
                          + "</tr>"
                          + "</thead><tbody>"
                          + ""
                          + ""+all_error_details+"</tbody></table>";
        }
        else{
         output+="<b><font color=\"blue\">"+added_records+"</u> Records added/updated successfully.</b></font><br>";    
        }
        }
         else {
//             missing ART Facility;  
if(mflcode.equals("")){
 output+="<font color=\"red\"><b>Error. No health facility/MFL Code selected. Kindly try selecting health facility from the drop down by selecting County> Sub County> Health facility and the MFL Code will autopopulate.</b></font><br>";    
}
else{
output+="<font color=\"red\"><b>This is not a HSDSA ART Supported site. Provided MFL Code is :"+mflcode+". Kindly check your MFL Code and try uploading again.</b></font><br>"; 
}
    }
 }
 else{
  output+="<font color=\"red\"><b>Wrong Excel Version. Kindly use Excel version v1.0 shown on your header as <u>Tx_ Curr Daily Data Verification Sheet v1.0</u></b></font><br>";        
 }
     return all_error_details;
    }
}
