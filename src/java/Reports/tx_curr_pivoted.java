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
public class tx_curr_pivoted extends HttpServlet {
String facil_data = "";
HttpSession session;
String county,sub_county,facility,where;
int has_data;
String[] periods = {"Oct 2017","Nov 2017","Dec 2017","Jan 2018","Feb 2018","Mar 2018","Apr 2018","May 2018","Jun 2018","July 2018"};  
String columns[] = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z","AA","AB","AC","AD","AE","AF","AG","AH","AI","AJ","AK","AL","AM","AN","AO","AP","AQ","AR","AS","AT","AU","AV","AW","AX","AY","AZ","BA","BB","BC","BD","BE","BF","BG","BH","BI","BJ","BK","BL","BM","BN","BO","BP","BQ","BR","BS","BT","BU","BV","BW","BX","BY","BZ","CA","CB","CC","CD","CE","CF","CG","CH","CI","CJ","CK","CL","CM","CN","CO","CP","CQ","CR","CS","CT","CU","CV","CW","CX","CY","CZ"};
String highv;
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        
        dbConn conn = new dbConn();
       session = request.getSession();
        Manager manager = new Manager();
       
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
                   String[] county_data = request.getParameterValues("county"); 
                   where = " WHERE high_volume="+highv+" AND (";
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
                     where = " WHERE high_volume="+highv+" ";   
                    }
                //where = " WHERE county.CountyID='"+county+"' ";
                   
                }
                
            }
            else{
            String[] sub_county_data = request.getParameterValues("sub_county");
               where = " WHERE high_volume="+highv+" AND (";
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
            where = " WHERE  high_volume="+highv+" ";   
           } 
                System.out.println(has_data+" where : "+where);    
              // where = " WHERE district.DistrictID='"+sub_county+"' ";    
            }
        }
        else{
         String[] facility_data = request.getParameterValues("facility");   
            where = " WHERE  high_volume="+highv+" AND (";
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
            where = " WHERE high_volume="+highv+" ";   
           } 
            
            //where = " WHERE subpartnera.SubpartnerID='"+facility+"' ";   
        }
        
        
          if(where.contains("high_volume=2")){
      where = where.replace("high_volume=2", " 1=1 ");
  } 
          
        
    XSSFWorkbook wb=new XSSFWorkbook();
    XSSFSheet shet1=wb.createSheet("Facility Pivot Summary");
     XSSFFont font=wb.createFont();
    font.setFontHeightInPoints((short)18);
    font.setFontName("Cambria");
    font.setColor((short)0000);
        
        
    XSSFCellStyle styleMainHeader = wb.createCellStyle();
    styleMainHeader.setFillForegroundColor(HSSFColor.LAVENDER.index);
    styleMainHeader.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    styleMainHeader.setBorderTop(BorderStyle.THIN);
    styleMainHeader.setBorderBottom(BorderStyle.THIN);
    styleMainHeader.setBorderLeft(BorderStyle.THIN);
    styleMainHeader.setBorderRight(BorderStyle.THIN);
    styleMainHeader.setAlignment(HorizontalAlignment.CENTER);
    
    XSSFCellStyle styleHeader = wb.createCellStyle();
    styleHeader.setFillForegroundColor(HSSFColor.GOLD.index);
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
    fontHeader.setFontHeight(13);
    
    styleMainHeader.setFont(fontHeader);
    styleMainHeader.setWrapText(true);
    
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
    int row_num;  
    
    
      String rawData="SELECT County,`Sub County` AS 'Sub County',`Health Facility` AS 'Health Facility',MFLCode,Period,Reported_731 AS 'Reported in 731',recounted AS 'Recounted',variance AS Variance FROM excels.variance_summary "+where;
  
  conn.rs = conn.st.executeQuery(rawData);
        System.out.println("Data : "+rawData);
   metaData = conn.rs.getMetaData();
col_count = metaData.getColumnCount(); //number of column

row_num=0;
rowheader=shet1.createRow(row_num);
for(int i=1;i<=col_count;i++){
    shet1.setColumnWidth(i-1, 5000);
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
   
 // output
        
        ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
        wb.write(outByteStream);
        byte [] outArray = outByteStream.toByteArray();
        response.setContentType("application/ms-excel");
        response.setContentLength(outArray.length);
        response.setHeader("Expires:", "0"); // eliminates browser caching
        response.setHeader("Content-Disposition", "attachment; filename=TX_CURR_RRI_Pivot_Data_"+manager.getdatekey()+".xlsx");
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
        Logger.getLogger(tx_curr_pivoted.class.getName()).log(Level.SEVERE, null, ex);
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
        Logger.getLogger(tx_curr_pivoted.class.getName()).log(Level.SEVERE, null, ex);
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
