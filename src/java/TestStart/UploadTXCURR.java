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
import java.util.ArrayList;
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
  String health_facility,output=""; 
    ArrayList uploaded_data = new ArrayList<>();
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
       dbConn conn = new dbConn();
       session = request.getSession();
             
       Sheet  worksheet = null;
        health_facility=output=""; 
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
        
public String getFacility(dbConn conn, String mflcode) throws SQLException{
    String facil = "";
         String getFacilDetails="SELECT SubPartnerNom FROM  subpartnera where CentreSanteId=? AND ART=?";
            conn.pst2 = conn.conn.prepareStatement(getFacilDetails);
            conn.pst2.setString(1, mflcode);
            conn.pst2.setInt(2, 1);
            conn.rs2 = conn.pst2.executeQuery();
            if(conn.rs2.next()){
              facil=conn.rs2.getString(1);  
             }
        return facil;
    }    
        
    public String readSheet (Sheet worksheet, dbConn conn) throws SQLException{
        uploaded_data.clear();
        int empty_counter=0,is_empty=0,no_data=0;
        int updated=0;
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
                cellmfl.setCellType(Cell.CELL_TYPE_STRING);
                mflcode   =  cellmfl.getStringCellValue();
                break;
            case Cell.CELL_TYPE_STRING:
              mflcode   =  cellmfl.getStringCellValue();
                break;
        }
     }
  System.out.println("mfl code :"+mflcode);
    if(facilityExist(conn, mflcode)){ //check if facility exist...
        int i=7,y=0,skipped_records=0,duplicate=0,added_records=updated=0;
        String exi_ccc_no="";
        int is_duplicate;
        health_facility = getFacility(conn,mflcode);
        
        output+="<b><u>Facility Name : "+health_facility.replace("'", "")+"</u> MFL Code : "+mflcode+"</b><br>";
        
        while(rowIterator.hasNext()){
            id=serialNo=review_date=ccc_no=is_ti=oct_17=nov_17=dec_17=jan_18=feb_18=mar_18=apr_18=may_18=jun_18=jul_18=month_due_vl="";
            error_details = reason = "";
            has_error=false;
            is_empty=no_data=is_duplicate=0;
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
         Cell cellreview_date = rowi.getCell((short) 1);
        cellreview_date.setCellType(Cell.CELL_TYPE_STRING);
    if(rowi.getCell(1).getCellType()==0){
     review_date =""+dateformat.format(rowi.getCell(1).getDateCellValue());
       }
    else{
    if (rowi.getCell(1).getCellTypeEnum().equals(NUMERIC)) {
            review_date = dateformat.format(rowi.getCell(1).getDateCellValue());
        }
    else{
         review_date = cellreview_date.getStringCellValue();
    }
        }
      }
    
      if(review_date.equals("")){
//                  has_error=true;
//                  reason += "Missing Review Date<br>";
//                 break;
            is_empty++;
      }
      
      
  error_details+="<td>"+review_date+"</td>";   
      
//*******************************End of review date************************

// **************************************Patient CCC No***********************
            Cell cell2 = rowi.getCell((short) 2);
              if(cell2!=null){
            switch (cell2.getCellType()) {
                   case 0:
                       //numeric
                       cell2.setCellType(Cell.CELL_TYPE_STRING);
                       ccc_no = cell2.getStringCellValue();
                       break;
                   case 1:
                       //string
                       ccc_no =cell2.getStringCellValue();
                       break;
                   
               }
        }
              
              if(ccc_no.equals("")){
//                  has_error=true;
//                  reason += "Missing CCC Number<br>";
//                  break;
            is_empty++;
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
            if(!statusOK(oct_17)){
                  has_error=true;
                  reason += "Wrong Status for Oct 2017<br>";
              }  
            if(oct_17.equals("")){
                  no_data++;
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
              if(!statusOK(nov_17)){
                  has_error=true;
                  reason += "Wrong Status for Nov 2017<br>";
              } 
              if(nov_17.equals("")){
                  no_data++;
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
              
             if(!statusOK(dec_17)){
                  has_error=true;
                  reason += "Wrong Status for Dec 2017<br>";
              } 
              if(dec_17.equals("")){
                  no_data++;
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
            
            if(!statusOK(jan_18)){
                  has_error=true;
                  reason += "Wrong Status for Jan 2018<br>";
              } 
            
            if(jan_18.equals("")){
                  no_data++;
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
            
            
            if(!statusOK(feb_18)){
                  has_error=true;
                  reason += "Wrong Status for Feb 2018<br>";
              } 
            
            if(feb_18.equals("")){
                  no_data++;
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
            
            
            if(!statusOK(mar_18)){
                  has_error=true;
                  reason += "Wrong Status for Mar 2018<br>";
              } 
            if(mar_18.equals("")){
                  no_data++;
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
            
            if(!statusOK(apr_18)){
                  has_error=true;
                  reason += "Wrong Status for Apr 2018<br>";
              } 
            
            if(apr_18.equals("")){
                  no_data++;
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
            
            
            if(!statusOK(may_18)){
                  has_error=true;
                  reason += "Wrong Status for May 2018<br>";
              } 
            if(may_18.equals("")){
                  no_data++;
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
            
            if(!statusOK(jun_18)){
                  has_error=true;
                  reason += "Wrong Status for Jun 2018<br>";
              } 
            if(jun_18.equals("")){
                  no_data++;
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
        
            if(!statusOK(jul_18)){
                  has_error=true;
                  reason += "Wrong Status for Jul 2018<br>";
              } 
            if(jul_18.equals("")){
                  no_data++;
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
        if(!isNumeric(month_due_vl)){
        if(!monthvlOK(month_due_vl)){
                  has_error=true;
                   reason += "Wrong Month due for VL<br>";
              } 
        }
            error_details+="<td>"+month_due_vl+"</td>";   
//*******************************End of MONTH due VL************************

//     generate id
        if(!ccc_no.equals("") && !mflcode.equals("")){
            id = mflcode+"_"+ccc_no;
        }
         System.out.println("id is :"+id);
            
        //check on dead cases
        if(oct_17.equalsIgnoreCase("Dead")){
            nov_17 = dec_17 = jan_18 = feb_18 = mar_18 = apr_18 = may_18 = jun_18 = jul_18 = "";
        }
        else if(nov_17.equalsIgnoreCase("Dead")){
         dec_17 = jan_18 = feb_18 = mar_18 = apr_18 = may_18 = jun_18 = jul_18 = "";
        }
        else if(dec_17.equalsIgnoreCase("Dead")){
         jan_18 = feb_18 = mar_18 = apr_18 = may_18 = jun_18 = jul_18 = "";
        }
        else if(jan_18.equalsIgnoreCase("Dead")){
         feb_18 = mar_18 = apr_18 = may_18 = jun_18 = jul_18 = "";
        }
        else if(feb_18.equalsIgnoreCase("Dead")){
         mar_18 = apr_18 = may_18 = jun_18 = jul_18 = "";
        }
        else if(mar_18.equalsIgnoreCase("Dead")){
         apr_18 = may_18 = jun_18 = jul_18 = "";
        }
        else if(apr_18.equalsIgnoreCase("Dead")){
         may_18 = jun_18 = jul_18 = "";
        }
        else if(may_18.equalsIgnoreCase("Dead")){
         jun_18 = jul_18 = "";
        }
        else if(jun_18.equalsIgnoreCase("Dead")){
         jul_18 = "";
        }

       if(is_ti.equals("") && no_data<10){
                  has_error=true;
                  reason += "Missing is Transfer in(TI)<br>";
            System.out.println("no data : "+no_data+" is ti");
              }

      //       END OF READING VALUES
    if(has_error){
      all_error_details+="<tr>"+error_details+"<td>"+reason+"</td></tr>"; 
      skipped_records++;
    }
    else{
        
        if(no_data!=10 && is_empty==0){
          
            if(uploaded_data.contains(ccc_no)){
             duplicate++;
             is_duplicate=1;
            exi_ccc_no+=duplicate+". "+ccc_no+" <br>";   
            }
            else{
                uploaded_data.add(ccc_no);
            }
           
            
        
        if(isNumeric(month_due_vl)){
            month_due_vl =numbertomonth(month_due_vl);
        }
        
        String checker = "SELECT id FROM tx_curr WHERE id='"+id+"'";
//        System.out.println("checker:"+checker);
        conn.rs = conn.st.executeQuery(checker);
        if(conn.rs.next()){
            if(is_duplicate==0){
            updated++;
            String query = "UPDATE tx_curr SET mflcode=?,serialNo=?,review_date=?,ccc_no=?,is_ti=?,oct_17=?,nov_17=?,dec_17=?,jan_18=?,feb_18=?,mar_18=?,apr_18=?,may_18=?,jun_18=?,jul_18=?,month_due_vl=? WHERE id=?";
                conn.pst = conn.conn.prepareStatement(query);
                
                conn.pst.setString(1, mflcode);
                conn.pst.setString(2, serialNo);
                conn.pst.setString(3, review_date);
                conn.pst.setString(4, ccc_no);
                conn.pst.setString(5, is_ti);
                conn.pst.setString(6, oct_17);
                conn.pst.setString(7, nov_17);
                conn.pst.setString(8, dec_17);
                conn.pst.setString(9, jan_18);
                conn.pst.setString(10, feb_18);
                conn.pst.setString(11, mar_18);
                conn.pst.setString(12, apr_18);
                conn.pst.setString(13, may_18);
                conn.pst.setString(14, jun_18);
                conn.pst.setString(15, jul_18);
                conn.pst.setString(16, month_due_vl);
                conn.pst.setString(17, id);
                conn.pst.executeUpdate();
            
                int added = conn.pst.executeUpdate();
                System.out.println("updated======================"+added);
            }
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
            
                int added = conn.pst.executeUpdate();
        }
        }
        else{
        //no data
        reason+="No data for this column";
        }
        
        }
    if(is_empty>0){
        empty_counter++;
    }
    
    if(empty_counter>=10){
        break;
    }
    i++;
   }
        
        if(skipped_records>0){
                  output+="<b><font color=\"green\"><u>"+added_records+"</u> Records added successfully.</font><br> </b>";
                  if(updated>0){
                  output+="<b><font color=\"blue\">"+updated+" records updated successfully.</font></b><br>"; 
                  }     
                  if(duplicate>0){
                  output+="<b><font color=\"brown\">"+duplicate+" possible duplicates detected.</font><br>"; 
                  }     
                   output+="<b><font color=\"red\"><u>"+skipped_records+"</u> Record(s) were Rejected.</font><br> </b>";
                  
                  output+="<font color=\"red\"><b>Rejected Record(s)</b></font></br>"
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
                  
                   if(duplicate>0){
                  output+="<br><u>List of duplicated CCC Numbers:</u><br>"+exi_ccc_no+"</b><br>";
                  }
        }
        else{
         output+="<b><font color=\"green\">"+added_records+"</u> Records added successfully.<br></font></b>";
         if(updated>0){
         output+="<b><font color=\"blue\">"+updated+" records updated successfully.</b></font><br>";
         }
         if(duplicate>0){
         output+="<b><font color=\"brown\">"+duplicate+" possible duplicates detected.</font><font color=\"black\"><br> <u>List of duplicated CCC Numbers:</u> <br>"+exi_ccc_no+"</b></font><br>";
         }
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
    
    public String numbertomonth(String month_num){
        String month_vl = ""; 
   int month = Integer.parseInt(month_num);
        if(month>=43040 && month<=43069){
          month_vl = "Nov-17";  
        }
        else if(month>=43070 && month<=43100){
         month_vl = "Dec-17";      
        }
        else if(month>=43101 && month<=43131){
         month_vl = "Jan-18";      
        }
        else if(month>=43132 && month<=43159){
         month_vl = "Feb-18";      
        }
        else if(month>=43160 && month<=43190){
         month_vl = "Mar-18";      
        }
        else if(month>=43191 && month<=43220){
         month_vl = "Apr-18";      
        }
        else if(month>=43221 && month<=43251){
         month_vl = "May-18";      
        }
        else if(month>=43252 && month<=43281){
         month_vl = "Jun-18";      
        }
        else if(month>=43282 && month<=43312){
         month_vl = "Jul-18";      
        }
        else if(month>=43313 && month<=43343){
         month_vl = "Aug-18";      
        }
        else if(month>=43344 && month<=43373){
         month_vl = "Sep-18";      
        }
        else if(month>=43374 && month<=43404){
         month_vl = "Oct-18";      
        }
        
        System.out.println("Numeric month : "+month_num+" converted to "+month_vl);
        return month_vl;
    }
    
        
    public boolean isNumeric(String s) {  
        return s != null && s.matches("[-+]?\\d*\\.?\\d+");  
    }
    
    public boolean statusOK(String se){
      boolean exist=false;
      
      if(se.equalsIgnoreCase("Active") || se.equalsIgnoreCase("Defaulters") || se.equalsIgnoreCase("T.O") || se.equalsIgnoreCase("LTFU") || 
         se.equalsIgnoreCase("Dead") || se.trim().equalsIgnoreCase("Stopped") || se.equalsIgnoreCase("Pending verification") || 
         se.equalsIgnoreCase("Stopped ") || se.equalsIgnoreCase("")){
          return true;
      }
      
      return exist;
    }
    
    public boolean monthvlOK(String se){
      boolean exist=false;
      if(se.equalsIgnoreCase("Nov-17") || se.equalsIgnoreCase("Dec-17") || se.equalsIgnoreCase("Jan-18") || se.equalsIgnoreCase("Feb-18") || 
       se.equalsIgnoreCase("Mar-18") || se.trim().equalsIgnoreCase("Apr-18") || se.equalsIgnoreCase("May-18") || se.equalsIgnoreCase("Jun-18") ||
       se.equalsIgnoreCase("Jul-18") || se.trim().equalsIgnoreCase("Aug-18") || se.equalsIgnoreCase("Sep-18") || se.equalsIgnoreCase("Oct-18") ||
       se.equalsIgnoreCase("")){
          return true;
      }
      
      return exist;
    }
}
