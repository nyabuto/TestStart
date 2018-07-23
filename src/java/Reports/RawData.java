/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Reports;

import Db.dbConn;
import TestStart.Manager;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.FontFamily;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author GNyabuto
 */
public class RawData extends HttpServlet {
HttpSession session;
String query_account_linkage,query_test_start,query_art_net_loss;
String year,quarter,where;
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        session = request.getSession();
        dbConn conn = new dbConn();
        Manager manager = new Manager();
        
        XSSFWorkbook wb=new XSSFWorkbook();
    XSSFSheet shet1=wb.createSheet("Accounting for Linkage");
    XSSFSheet shet2=wb.createSheet("Test & Start -Cohort Summary");
    XSSFSheet shet3=wb.createSheet("ART Current Net Loss-Var ");
    
    XSSFFont font=wb.createFont();
    font.setFontHeightInPoints((short)18);
    font.setFontName("Cambria");
    font.setColor((short)0000);
        
        
    XSSFCellStyle styleHeader = wb.createCellStyle();
    styleHeader.setFillForegroundColor(HSSFColor.GREY_40_PERCENT.index);
    styleHeader.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    styleHeader.setBorderTop(BorderStyle.THIN);
    styleHeader.setBorderBottom(BorderStyle.THIN);
    styleHeader.setBorderLeft(BorderStyle.THIN);
    styleHeader.setBorderRight(BorderStyle.THIN);
    styleHeader.setAlignment(HorizontalAlignment.CENTER);
    
    XSSFFont fontHeader = wb.createFont();
    fontHeader.setColor(HSSFColor.BLACK.index);
    fontHeader.setBold(true);
    fontHeader.setFamily(FontFamily.MODERN);
    fontHeader.setFontName("Cambria");
    fontHeader.setFontHeight(15);
    styleHeader.setFont(fontHeader);
    styleHeader.setWrapText(true);

    XSSFCellStyle stborder = wb.createCellStyle();
    stborder.setBorderTop(BorderStyle.THIN);
    stborder.setBorderBottom(BorderStyle.THIN);
    stborder.setBorderLeft(BorderStyle.THIN);
    stborder.setBorderRight(BorderStyle.THIN);
    stborder.setAlignment(HorizontalAlignment.LEFT);
    
    XSSFFont font_cell=wb.createFont();
    font_cell.setColor(HSSFColor.BLACK.index);
    font_cell.setFamily(FontFamily.MODERN);
    font_cell.setFontName("Cambria");
    stborder.setFont(font_cell);
    stborder.setWrapText(true);
        
        
    ResultSetMetaData metaData;    
    int col_count; 
    XSSFRow rowheader;
    XSSFCell firstCell;
    XSSFCell lastCell;
    int row_num=0;  
    
    
    
    year = request.getParameter("year");
    quarter = request.getParameter("quarter");
    
    where = "";
    if(year.equals("")){
     where=" 1=1 ";   
    }
    else{
      
     if(quarter.equals("")){
       where += " (year="+(Integer.parseInt(year)-1)+" AND month>=10) OR (year="+year+" AND month<10)";     
     }   
     else{   
        if(quarter.equals("1") && !year.equalsIgnoreCase("")){
       where += "year="+(Integer.parseInt(year)-1)+" AND (";     
    }
    else{
       where += "year="+year+" AND (";      
    }
    
    
    String getmonths = "SELECT months FROM quarter WHERE id="+quarter;
    conn.rs = conn.st.executeQuery(getmonths);
    if(conn.rs.next()){
        String months[] = conn.rs.getString(1).split(",");
        for(String month:months){
            if(!month.equals("")){
             where+=" month="+month+" OR ";   
            }
        }
        
        // REMOVE THE LAST 3 CHARACTERS
       
        where = manager.removeLastChars(where, 3)+")";
        
    }
     }
    }
    
    
    
        query_account_linkage = "SELECT year AS Year,months.shortname AS Month,county AS County,sub_county AS 'Sub County',facility AS 'Health Facility',mfl_code AS 'MFL Code'," +
"date_confirmed_hiv_pos AS 'Date Confirmed HIV Positive',gender as Gender,current_age AS 'Current Age',documented_linkage_register AS 'Documented in Linkage Register'," +
"ccc_no AS 'Client CCC Number',enrollment_date AS 'Date of Enrollment',referred AS 'Referred'," +
"enrolled_to_other_site AS 'Enrolled to other site',enrolled_from_other_site AS 'Enrolled from other site'," +
"art_start_date AS 'ART Start Date', started_art_in_this_facility AS 'Started ART in this facility', started_art_in_other_facility AS 'Started in other facility (Name of the facility)'," +
"patient_status AS 'Patient Status', declined_reason AS 'If declined, indicate reason', reported_cause_of_death AS 'Reported cause of death'," +
"yearmonth AS 'YearMonth' FROM accounting_for_linkage LEFT JOIN months ON accounting_for_linkage.month=months.id WHERE "+where+" ";
        
        query_test_start="SELECT year AS Year,months.shortname AS Month,county AS County,sub_county AS 'Sub County',facility AS 'Health Facility',mfl_code AS 'MFL Code',ccc_no AS 'Client CCC Number',gender as Gender," +
"current_age AS 'Current Age',date_confirmed_hiv_pos AS 'Date Confirmed HIV Positive'," +
"enrollment_date AS 'Date of Enrollment',art_start_date AS 'ART Start Date',baseline_who_stage AS 'Baseline WHO Stage', baseline_cd4_cell_count_perc AS 'Baseline CD4 Count or Percent'," +
"initial_vl AS 'Initial VL Result ', initial_vl_date AS 'Initial Vl Date',repeat_vl_value AS 'Repeat VL  result',repeat_vl_date AS 'Repeat VL date',vl_12_months_value AS 'VL result at 12 months'," +
"vl_12_months_date AS '12-month Vl Date',last_visit_date AS 'Last visit date',patient_outcome AS 'Patient Outcome',yearmonth AS 'YearMonth' " +
"FROM test_start_summary LEFT JOIN months ON test_start_summary.month=months.id  WHERE "+where+" ";
        
        query_art_net_loss="SELECT year AS Year,months.shortname AS Month,county AS County,sub_county AS 'Sub County',facility AS 'Health Facility',mfl_code AS 'MFL Code',ccc_no AS 'Client CCC Number',gender as Gender," +
"current_age AS 'Current Age',date_confirmed_hiv_pos AS 'Date Confirmed HIV Positive'," +
"enrollment_date AS 'Date of Enrollment',art_start_date AS 'ART Start Date',baseline_who_stage AS 'Baseline WHO Stage', baseline_cd4_cell_count_perc AS 'Baseline CD4 Count or Percent'," +
"initial_vl AS 'Initial VL Result ', initial_vl_date AS 'Initial Vl Date',repeat_vl_value AS 'Repeat VL  Result',repeat_vl_date AS 'Repeat VL Date',vl_12_months_value AS 'VL Result at 12 Months'," +
"vl_12_months_date AS '12-month Vl Date',last_visit_date AS 'Last Visit Date',expected_return_date AS 'Expected return date (TCA)', " +
"patient_status AS 'Patient Status', date_resumed_tx AS 'Date patient resumed Tx', yearmonth AS 'YearMonth' " +
"FROM art_current_net_loss LEFT JOIN months ON art_current_net_loss.month=months.id  WHERE "+where+" ";
     
        
 //Sheet 1 Accounting for Linkage
        System.out.println("query : "+query_account_linkage);
 
conn.rs = conn.st.executeQuery(query_account_linkage);
metaData = conn.rs.getMetaData();
col_count = metaData.getColumnCount(); //number of column
row_num=0;
rowheader=shet1.createRow(row_num);
for(int i=1;i<=col_count;i++){
    shet1.setColumnWidth(i, 5000);
    String column_name = metaData.getColumnLabel(i);
    XSSFCell cell= rowheader.createCell(i-1);
    cell.setCellValue(column_name); 
    cell.setCellStyle(styleHeader);
}
row_num++;
 while(conn.rs.next()){
          XSSFRow row=shet1.createRow(row_num);
          for(int i=1;i<=col_count;i++){
              String value=conn.rs.getString(i);
               XSSFCell cell= row.createCell(i-1);
               if(isNumeric(value)){
               cell.setCellValue(Double.parseDouble(value));
               }
               else{
               cell.setCellValue(value);    
               }
           cell.setCellStyle(stborder);
          }
          row_num++;    
 }
        firstCell = rowheader.getCell(0);
        lastCell = rowheader.getCell(col_count-1);
 shet1.setAutoFilter(new CellRangeAddress( firstCell.getRowIndex(), lastCell.getRowIndex(), firstCell.getColumnIndex(), lastCell.getColumnIndex() ));
 
 
 //Sheet 2 Test&Start cohort Summary
 
 
 
 conn.rs = conn.st.executeQuery(query_test_start);
metaData = conn.rs.getMetaData();
col_count = metaData.getColumnCount(); //number of column
row_num=0;
rowheader=shet2.createRow(row_num);
for(int i=1;i<=col_count;i++){
    shet2.setColumnWidth(i, 5000);
    String column_name = metaData.getColumnLabel(i);
    XSSFCell cell= rowheader.createCell(i-1);
    cell.setCellValue(column_name); 
    cell.setCellStyle(styleHeader);
}
row_num++;
 while(conn.rs.next()){
          XSSFRow row=shet2.createRow(row_num);
          for(int i=1;i<=col_count;i++){
              String value=conn.rs.getString(i);
               XSSFCell cell= row.createCell(i-1);
               if(isNumeric(value)){
               cell.setCellValue(Double.parseDouble(value));
               }
               else{
               cell.setCellValue(value);    
               }
           cell.setCellStyle(stborder);
          }
          row_num++;    
 }
        firstCell = rowheader.getCell(0);
        lastCell = rowheader.getCell(col_count-1);
 shet2.setAutoFilter(new CellRangeAddress( firstCell.getRowIndex(), lastCell.getRowIndex(), firstCell.getColumnIndex(), lastCell.getColumnIndex() ));
 
 
 
 //Sheet 3 ART Current net loss
 
 
 
 conn.rs = conn.st.executeQuery(query_art_net_loss);
metaData = conn.rs.getMetaData();
col_count = metaData.getColumnCount(); //number of column
row_num=0;
rowheader=shet3.createRow(row_num);
for(int i=1;i<=col_count;i++){
    shet3.setColumnWidth(i, 5000);
    String column_name = metaData.getColumnLabel(i);
    XSSFCell cell= rowheader.createCell(i-1);
    cell.setCellValue(column_name); 
    cell.setCellStyle(styleHeader);
}
row_num++;
 while(conn.rs.next()){
          XSSFRow row=shet3.createRow(row_num);
          for(int i=1;i<=col_count;i++){
              String value=conn.rs.getString(i);
               XSSFCell cell= row.createCell(i-1);
               if(isNumeric(value)){
               cell.setCellValue(Double.parseDouble(value));
               }
               else{
               cell.setCellValue(value);    
               }
           cell.setCellStyle(stborder);
          }
          row_num++;    
 }
        firstCell = rowheader.getCell(0);
        lastCell = rowheader.getCell(col_count-1);
 shet3.setAutoFilter(new CellRangeAddress( firstCell.getRowIndex(), lastCell.getRowIndex(), firstCell.getColumnIndex(), lastCell.getColumnIndex() ));
 
 
 
 //end
 
// output
        
        ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
        wb.write(outByteStream);
        byte [] outArray = outByteStream.toByteArray();
        response.setContentType("application/ms-excel");
        response.setContentLength(outArray.length);
        response.setHeader("Expires:", "0"); // eliminates browser caching
        response.setHeader("Content-Disposition", "attachment; filename=Test&Start_Raw_Data_"+manager.getdatekey()+".xlsx");
        OutputStream outStream = response.getOutputStream();
        outStream.write(outArray);
        outStream.flush();    
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
        Logger.getLogger(RawData.class.getName()).log(Level.SEVERE, null, ex);
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
        Logger.getLogger(RawData.class.getName()).log(Level.SEVERE, null, ex);
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
}
