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
public class Tx_CURR extends HttpServlet {
String facil_data = "";
HttpSession session;
String county,sub_county,facility,where;
int has_data;
String[] periods = {"Oct 2017","Nov 2017","Dec 2017","Jan 2018","Feb 2018","Mar 2018","Apr 2018","May 2018","Jun 2018","July 2018"};  
String columns[] = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z","AA","AB","AC","AD","AE","AF","AG","AH","AI","AJ","AK","AL","AM","AN","AO","AP","AQ","AR","AS","AT","AU","AV","AW","AX","AY","AZ","BA","BB","BC","BD","BE","BF","BG","BH","BI","BJ","BK","BL","BM","BN","BO","BP","BQ","BR","BS","BT","BU","BV","BW","BX","BY","BZ","CA","CB","CC","CD","CE","CF","CG","CH","CI","CJ","CK","CL","CM","CN","CO","CP","CQ","CR","CS","CT","CU","CV","CW","CX","CY","CZ"};
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
       dbConn conn = new dbConn();
       session = request.getSession();
        Manager manager = new Manager();
       
        
        county = request.getParameter("county");
        sub_county = request.getParameter("sub_county");
        facility = request.getParameter("facility");
        
       
        where = "";
        if(facility==null || facility.equals("")){
            where="";
            if(sub_county==null || sub_county.equals("")){
             where="";
                if(county==null || county.equals("")){
                where="";    
                }
                else{
                   String[] county_data = request.getParameter("county").split("_"); 
                   where = " WHERE (";
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
                     where = "";   
                    }
                //where = " WHERE county.CountyID='"+county+"' ";
                   
                }
                
            }
            else{
            String[] sub_county_data = request.getParameter("sub_county").split("_");
               where = " WHERE (";
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
            where = " ";   
           } 
                
              // where = " WHERE district.DistrictID='"+sub_county+"' ";    
            }
        }
        else{
         String[] facility_data = request.getParameter("facility").split("_");   
            where = " WHERE (";
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
            where = " ";   
           } 
            
            //where = " WHERE subpartnera.SubpartnerID='"+facility+"' ";   
        }
        
        
    XSSFWorkbook wb=new XSSFWorkbook();
    XSSFSheet shet0=wb.createSheet("Raw Data");
    XSSFSheet shet1=wb.createSheet("Facility Summary");
    XSSFSheet shet2=wb.createSheet("Sub County Summary");
    XSSFSheet shet3=wb.createSheet("County Summary");
    XSSFSheet shet4=wb.createSheet("Due for Viral Load");
    XSSFSheet shet5=wb.createSheet("Summary By Status");
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
    
    XSSFCellStyle styleTotal = wb.createCellStyle();
    styleTotal.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
    styleTotal.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    styleTotal.setBorderTop(BorderStyle.THIN);
    styleTotal.setBorderBottom(BorderStyle.THIN);
    styleTotal.setBorderLeft(BorderStyle.THIN);
    styleTotal.setBorderRight(BorderStyle.THIN);
    styleTotal.setAlignment(HorizontalAlignment.CENTER);
    
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
    XSSFCell firstCell;
    XSSFCell lastCell;
    int row_num=0;  
    
    
       
      facil_data = "/*Facility summaries */ " +
                    " " +
                    "SELECT county.County AS 'County Name', district.DistrictNom AS 'Sub County', subpartnera.SubPartnerNom AS 'Health Facility',CentreSanteId AS MFLCode, " +
"COUNT(CASE WHEN tx_curr.oct_17='Active' then 1 END) AS 'Active', " +
"COUNT(CASE WHEN tx_curr.oct_17='Defaulters' OR tx_curr.oct_17='Defaulter' then 1 END) AS 'Defaulter', " +
"COUNT(CASE WHEN tx_curr.oct_17='T.O' OR tx_curr.oct_17='TO' then 1 END) AS 'T.O', " +
"COUNT(CASE WHEN tx_curr.oct_17='LTFU' then 1 END) AS 'LTFU', " +
"COUNT(CASE WHEN tx_curr.oct_17='Dead' then 1 END) AS 'Dead', " +
"COUNT(CASE WHEN tx_curr.oct_17='Stopped ' then 1 END) AS 'Stopped', " +
"COUNT(CASE WHEN tx_curr.oct_17='Pending verification' then 1 END) AS 'Pending verification', " +
"COUNT(CASE WHEN tx_curr.oct_17!='' then 1 END) AS 'Total', " +
" " +
"COUNT(CASE WHEN tx_curr.nov_17='Active' then 1 END) AS 'Active', " +
"COUNT(CASE WHEN tx_curr.nov_17='Defaulters' OR tx_curr.nov_17='Defaulter' then 1 END) AS 'Defaulter', " +
"COUNT(CASE WHEN tx_curr.nov_17='T.O' OR tx_curr.nov_17='TO' then 1 END) AS 'T.O', " +
"COUNT(CASE WHEN tx_curr.nov_17='LTFU' then 1 END) AS 'LTFU', " +
"COUNT(CASE WHEN tx_curr.nov_17='Dead' then 1 END) AS 'Dead', " +
"COUNT(CASE WHEN tx_curr.nov_17='Stopped ' then 1 END) AS 'Stopped', " +
"COUNT(CASE WHEN tx_curr.nov_17='Pending verification' then 1 END) AS 'Pending verification', " +
"COUNT(CASE WHEN tx_curr.nov_17!='' then 1 END) AS 'Total', " +
" " +
"COUNT(CASE WHEN tx_curr.dec_17='Active' then 1 END) AS 'Active', " +
"COUNT(CASE WHEN tx_curr.dec_17='Defaulters' OR tx_curr.dec_17='Defaulter' then 1 END) AS 'Defaulter', " +
"COUNT(CASE WHEN tx_curr.dec_17='T.O' OR tx_curr.dec_17='TO' then 1 END) AS 'T.O', " +
"COUNT(CASE WHEN tx_curr.dec_17='LTFU' then 1 END) AS 'LTFU', " +
"COUNT(CASE WHEN tx_curr.dec_17='Dead' then 1 END) AS 'Dead', " +
"COUNT(CASE WHEN tx_curr.dec_17='Stopped ' then 1 END) AS 'Stopped', " +
"COUNT(CASE WHEN tx_curr.dec_17='Pending verification' then 1 END) AS 'Pending verification', " +
"COUNT(CASE WHEN tx_curr.dec_17!='' then 1 END) AS 'Total', " +
" " +
"COUNT(CASE WHEN tx_curr.jan_18='Active' then 1 END) AS 'Active', " +
"COUNT(CASE WHEN tx_curr.jan_18='Defaulters' OR tx_curr.jan_18='Defaulter' then 1 END) AS 'Defaulter', " +
"COUNT(CASE WHEN tx_curr.jan_18='T.O' OR tx_curr.jan_18='TO' then 1 END) AS 'T.O', " +
"COUNT(CASE WHEN tx_curr.jan_18='LTFU' then 1 END) AS 'LTFU', " +
"COUNT(CASE WHEN tx_curr.jan_18='Dead' then 1 END) AS 'Dead', " +
"COUNT(CASE WHEN tx_curr.jan_18='Stopped ' then 1 END) AS 'Stopped', " +
"COUNT(CASE WHEN tx_curr.jan_18='Pending verification' then 1 END) AS 'Pending verification', " +
"COUNT(CASE WHEN tx_curr.jan_18!='' then 1 END) AS 'Total', " +
" " +
"COUNT(CASE WHEN tx_curr.feb_18='Active' then 1 END) AS 'Active', " +
"COUNT(CASE WHEN tx_curr.feb_18='Defaulters' OR tx_curr.feb_18='Defaulter' then 1 END) AS 'Defaulter', " +
"COUNT(CASE WHEN tx_curr.feb_18='T.O' OR tx_curr.feb_18='TO' then 1 END) AS 'T.O', " +
"COUNT(CASE WHEN tx_curr.feb_18='LTFU' then 1 END) AS 'LTFU', " +
"COUNT(CASE WHEN tx_curr.feb_18='Dead' then 1 END) AS 'Dead', " +
"COUNT(CASE WHEN tx_curr.feb_18='Stopped ' then 1 END) AS 'Stopped', " +
"COUNT(CASE WHEN tx_curr.feb_18='Pending verification' then 1 END) AS 'Pending verification', " +
"COUNT(CASE WHEN tx_curr.feb_18!='' then 1 END) AS 'Total', " +
" " +
"COUNT(CASE WHEN tx_curr.mar_18='Active' then 1 END) AS 'Active', " +
"COUNT(CASE WHEN tx_curr.mar_18='Defaulters' OR tx_curr.mar_18='Defaulter' then 1 END) AS 'Defaulter', " +
"COUNT(CASE WHEN tx_curr.mar_18='T.O' OR tx_curr.mar_18='TO' then 1 END) AS 'T.O', " +
"COUNT(CASE WHEN tx_curr.mar_18='LTFU' then 1 END) AS 'LTFU', " +
"COUNT(CASE WHEN tx_curr.mar_18='Dead' then 1 END) AS 'Dead', " +
"COUNT(CASE WHEN tx_curr.mar_18='Stopped ' then 1 END) AS 'Stopped', " +
"COUNT(CASE WHEN tx_curr.mar_18='Pending verification' then 1 END) AS 'Pending verification', " +
"COUNT(CASE WHEN tx_curr.mar_18!='' then 1 END) AS 'Total', " +
" " +
"COUNT(CASE WHEN tx_curr.apr_18='Active' then 1 END) AS 'Active', " +
"COUNT(CASE WHEN tx_curr.apr_18='Defaulters' OR tx_curr.apr_18='Defaulter' then 1 END) AS 'Defaulter', " +
"COUNT(CASE WHEN tx_curr.apr_18='T.O' OR tx_curr.apr_18='TO' then 1 END) AS 'T.O', " +
"COUNT(CASE WHEN tx_curr.apr_18='LTFU' then 1 END) AS 'LTFU', " +
"COUNT(CASE WHEN tx_curr.apr_18='Dead' then 1 END) AS 'Dead', " +
"COUNT(CASE WHEN tx_curr.apr_18='Stopped ' then 1 END) AS 'Stopped', " +
"COUNT(CASE WHEN tx_curr.apr_18='Pending verification' then 1 END) AS 'Pending verification', " +
"COUNT(CASE WHEN tx_curr.apr_18!='' then 1 END) AS 'Total', " +
" " +
"COUNT(CASE WHEN tx_curr.may_18='Active' then 1 END) AS 'Active', " +
"COUNT(CASE WHEN tx_curr.may_18='Defaulters' OR tx_curr.may_18='Defaulter' then 1 END) AS 'Defaulter', " +
"COUNT(CASE WHEN tx_curr.may_18='T.O' OR tx_curr.may_18='TO' then 1 END) AS 'T.O', " +
"COUNT(CASE WHEN tx_curr.may_18='LTFU' then 1 END) AS 'LTFU', " +
"COUNT(CASE WHEN tx_curr.may_18='Dead' then 1 END) AS 'Dead', " +
"COUNT(CASE WHEN tx_curr.may_18='Stopped ' then 1 END) AS 'Stopped', " +
"COUNT(CASE WHEN tx_curr.may_18='Pending verification' then 1 END) AS 'Pending verification', " +
"COUNT(CASE WHEN tx_curr.may_18!='' then 1 END) AS 'Total', " +
" " +
"COUNT(CASE WHEN tx_curr.jun_18='Active' then 1 END) AS 'Active', " +
"COUNT(CASE WHEN tx_curr.jun_18='Defaulters' OR tx_curr.jun_18='Defaulter' then 1 END) AS 'Defaulter', " +
"COUNT(CASE WHEN tx_curr.jun_18='T.O' OR tx_curr.jun_18='TO' then 1 END) AS 'T.O', " +
"COUNT(CASE WHEN tx_curr.jun_18='LTFU' then 1 END) AS 'LTFU', " +
"COUNT(CASE WHEN tx_curr.jun_18='Dead' then 1 END) AS 'Dead', " +
"COUNT(CASE WHEN tx_curr.jun_18='Stopped ' then 1 END) AS 'Stopped', " +
"COUNT(CASE WHEN tx_curr.jun_18='Pending verification' then 1 END) AS 'Pending verification', " +
"COUNT(CASE WHEN tx_curr.jun_18!='' then 1 END) AS 'Total', " +
" " +
"COUNT(CASE WHEN tx_curr.jul_18='Active' then 1 END) AS 'Active', " +
"COUNT(CASE WHEN tx_curr.jul_18='Defaulters' OR tx_curr.jul_18='Defaulter' then 1 END) AS 'Defaulter', " +
"COUNT(CASE WHEN tx_curr.jul_18='T.O' OR tx_curr.jul_18='TO' then 1 END) AS 'T.O', " +
"COUNT(CASE WHEN tx_curr.jul_18='LTFU' then 1 END) AS 'LTFU', " +
"COUNT(CASE WHEN tx_curr.jul_18='Dead' then 1 END) AS 'Dead', " +
"COUNT(CASE WHEN tx_curr.jul_18='Stopped ' then 1 END) AS 'Stopped', " +
"COUNT(CASE WHEN tx_curr.jul_18='Pending verification' then 1 END) AS 'Pending verification', " +
"COUNT(CASE WHEN tx_curr.jul_18!='' then 1 END) AS 'Total' " +
                    " " +
                    "from tx_curr LEFT JOIN subpartnera ON tx_curr.mflcode=subpartnera.CentreSanteID  " +
                    "LEFT JOIN district ON district.DistrictID=subpartnera.DistrictID  " +
                    "LEFT JOIN county on district.CountyID=county.CountyID "+where+" " +
                    "GROUP BY MFLCode";  

      conn.rs = conn.st.executeQuery(facil_data);
      //read headers and post them
      metaData = conn.rs.getMetaData();
col_count = metaData.getColumnCount(); //number of column

rowheader=shet1.createRow(row_num);
rowheader.setHeightInPoints(25);
    for(int i=4;i<col_count;i++){ 
    XSSFCell cell= rowheader.createCell(i);
    int pos = (i-4)/8;
    if(isEvenlyDivisable(i-4, 8)){
    cell.setCellValue(periods[pos]); 
     shet1.addMergedRegion(new CellRangeAddress(0,0,i,i+7));
    }
    cell.setCellStyle(styleMainHeader);
    }



row_num=1;
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
               
    if(isEvenlyDivisable(i-4, 8) && i>4){
      cell.setCellStyle(styleTotal);      
    }
    else{
   cell.setCellStyle(stborder);     
    }
          }
          row_num++;    
 }
   
 if(row_num>2){
  //for totals
   XSSFRow rowtotals=shet1.createRow(row_num);
   for(int i=4;i<col_count;i++){
       String col=columns[i];
       System.out.println("col:"+col);
       String formulae= "SUM("+col+"3:"+col+""+(row_num)+")";
       XSSFCell cell= rowtotals.createCell(i);
        cell.setCellType(XSSFCell.CELL_TYPE_FORMULA);
        cell.setCellFormula(formulae);
        cell.setCellStyle(styleTotal);          
      }
 }
  
  //end of totals
 
// Raw Data 
  String rawData="" +
        "SELECT county.County AS 'County Name', district.DistrictNom AS 'Sub County', subpartnera.SubPartnerNom AS 'Health Facility',CentreSanteId AS MFLCode," +
        "serialNo AS 'Serial Number',review_date AS 'Review Date',ccc_no AS 'Patient CCC Number',is_ti AS 'Is the patient a T.I', " +
        "oct_17 AS 'Oct 2017',nov_17 AS 'Nov 2017', dec_17 AS 'Dec 2017', jan_18 AS 'Jan 2018',feb_18 AS 'Feb 2018', mar_18 AS 'Mar 2018', apr_18 AS 'Apr 2018'," +
        "may_18 AS 'May 2018',jun_18 AS 'Jun 2018', jul_18 AS 'Jul 2018', month_due_vl AS 'Month due for Viral Load'" +
        "from tx_curr LEFT JOIN subpartnera ON tx_curr.mflcode=subpartnera.CentreSanteID " +
        "LEFT JOIN district ON district.DistrictID=subpartnera.DistrictID " +
        "LEFT JOIN county on district.CountyID=county.CountyID "+where+" " +
        "GROUP BY ccc_no";
  conn.rs = conn.st.executeQuery(rawData);
   metaData = conn.rs.getMetaData();
col_count = metaData.getColumnCount(); //number of column

row_num=0;
rowheader=shet0.createRow(row_num);
for(int i=1;i<=col_count;i++){
    shet0.setColumnWidth(i-1, 5000);
    String column_name = metaData.getColumnLabel(i);
    XSSFCell cell= rowheader.createCell(i-1);
    cell.setCellValue(column_name); 
    cell.setCellStyle(styleHeader);
}

row_num++;
  while(conn.rs.next()){
            XSSFRow row=shet0.createRow(row_num);
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
  
  //SUB COUNTY REPORT
  String get_subcounty="/*Sub County summaries */ " +
" " +
"SELECT county.County AS 'County Name', district.DistrictNom AS 'Sub County', " +
"COUNT(CASE WHEN tx_curr.oct_17='Active' then 1 END) AS 'Active', " +
"COUNT(CASE WHEN tx_curr.oct_17='Defaulters' OR tx_curr.oct_17='Defaulter' then 1 END) AS 'Defaulter', " +
"COUNT(CASE WHEN tx_curr.oct_17='T.O' OR tx_curr.oct_17='TO' then 1 END) AS 'T.O', " +
"COUNT(CASE WHEN tx_curr.oct_17='LTFU' then 1 END) AS 'LTFU', " +
"COUNT(CASE WHEN tx_curr.oct_17='Dead' then 1 END) AS 'Dead', " +
"COUNT(CASE WHEN tx_curr.oct_17='Stopped ' then 1 END) AS 'Stopped', " +
"COUNT(CASE WHEN tx_curr.oct_17='Pending verification' then 1 END) AS 'Pending verification', " +
"COUNT(CASE WHEN tx_curr.oct_17!='' then 1 END) AS 'Total', " +
" " +
"COUNT(CASE WHEN tx_curr.nov_17='Active' then 1 END) AS 'Active', " +
"COUNT(CASE WHEN tx_curr.nov_17='Defaulters' OR tx_curr.nov_17='Defaulter' then 1 END) AS 'Defaulter', " +
"COUNT(CASE WHEN tx_curr.nov_17='T.O' OR tx_curr.nov_17='TO' then 1 END) AS 'T.O', " +
"COUNT(CASE WHEN tx_curr.nov_17='LTFU' then 1 END) AS 'LTFU', " +
"COUNT(CASE WHEN tx_curr.nov_17='Dead' then 1 END) AS 'Dead', " +
"COUNT(CASE WHEN tx_curr.nov_17='Stopped ' then 1 END) AS 'Stopped', " +
"COUNT(CASE WHEN tx_curr.nov_17='Pending verification' then 1 END) AS 'Pending verification', " +
"COUNT(CASE WHEN tx_curr.nov_17!='' then 1 END) AS 'Total', " +
" " +
"COUNT(CASE WHEN tx_curr.dec_17='Active' then 1 END) AS 'Active', " +
"COUNT(CASE WHEN tx_curr.dec_17='Defaulters' OR tx_curr.dec_17='Defaulter' then 1 END) AS 'Defaulter', " +
"COUNT(CASE WHEN tx_curr.dec_17='T.O' OR tx_curr.dec_17='TO' then 1 END) AS 'T.O', " +
"COUNT(CASE WHEN tx_curr.dec_17='LTFU' then 1 END) AS 'LTFU', " +
"COUNT(CASE WHEN tx_curr.dec_17='Dead' then 1 END) AS 'Dead', " +
"COUNT(CASE WHEN tx_curr.dec_17='Stopped ' then 1 END) AS 'Stopped', " +
"COUNT(CASE WHEN tx_curr.dec_17='Pending verification' then 1 END) AS 'Pending verification', " +
"COUNT(CASE WHEN tx_curr.dec_17!='' then 1 END) AS 'Total', " +
" " +
"COUNT(CASE WHEN tx_curr.jan_18='Active' then 1 END) AS 'Active', " +
"COUNT(CASE WHEN tx_curr.jan_18='Defaulters' OR tx_curr.jan_18='Defaulter' then 1 END) AS 'Defaulter', " +
"COUNT(CASE WHEN tx_curr.jan_18='T.O' OR tx_curr.jan_18='TO' then 1 END) AS 'T.O', " +
"COUNT(CASE WHEN tx_curr.jan_18='LTFU' then 1 END) AS 'LTFU', " +
"COUNT(CASE WHEN tx_curr.jan_18='Dead' then 1 END) AS 'Dead', " +
"COUNT(CASE WHEN tx_curr.jan_18='Stopped ' then 1 END) AS 'Stopped', " +
"COUNT(CASE WHEN tx_curr.jan_18='Pending verification' then 1 END) AS 'Pending verification', " +
"COUNT(CASE WHEN tx_curr.jan_18!='' then 1 END) AS 'Total', " +
" " +
"COUNT(CASE WHEN tx_curr.feb_18='Active' then 1 END) AS 'Active', " +
"COUNT(CASE WHEN tx_curr.feb_18='Defaulters' OR tx_curr.feb_18='Defaulter' then 1 END) AS 'Defaulter', " +
"COUNT(CASE WHEN tx_curr.feb_18='T.O' OR tx_curr.feb_18='TO' then 1 END) AS 'T.O', " +
"COUNT(CASE WHEN tx_curr.feb_18='LTFU' then 1 END) AS 'LTFU', " +
"COUNT(CASE WHEN tx_curr.feb_18='Dead' then 1 END) AS 'Dead', " +
"COUNT(CASE WHEN tx_curr.feb_18='Stopped ' then 1 END) AS 'Stopped', " +
"COUNT(CASE WHEN tx_curr.feb_18='Pending verification' then 1 END) AS 'Pending verification', " +
"COUNT(CASE WHEN tx_curr.feb_18!='' then 1 END) AS 'Total', " +
" " +
"COUNT(CASE WHEN tx_curr.mar_18='Active' then 1 END) AS 'Active', " +
"COUNT(CASE WHEN tx_curr.mar_18='Defaulters' OR tx_curr.mar_18='Defaulter' then 1 END) AS 'Defaulter', " +
"COUNT(CASE WHEN tx_curr.mar_18='T.O' OR tx_curr.mar_18='TO' then 1 END) AS 'T.O', " +
"COUNT(CASE WHEN tx_curr.mar_18='LTFU' then 1 END) AS 'LTFU', " +
"COUNT(CASE WHEN tx_curr.mar_18='Dead' then 1 END) AS 'Dead', " +
"COUNT(CASE WHEN tx_curr.mar_18='Stopped ' then 1 END) AS 'Stopped', " +
"COUNT(CASE WHEN tx_curr.mar_18='Pending verification' then 1 END) AS 'Pending verification', " +
"COUNT(CASE WHEN tx_curr.mar_18!='' then 1 END) AS 'Total', " +
" " +
"COUNT(CASE WHEN tx_curr.apr_18='Active' then 1 END) AS 'Active', " +
"COUNT(CASE WHEN tx_curr.apr_18='Defaulters' OR tx_curr.apr_18='Defaulter' then 1 END) AS 'Defaulter', " +
"COUNT(CASE WHEN tx_curr.apr_18='T.O' OR tx_curr.apr_18='TO' then 1 END) AS 'T.O', " +
"COUNT(CASE WHEN tx_curr.apr_18='LTFU' then 1 END) AS 'LTFU', " +
"COUNT(CASE WHEN tx_curr.apr_18='Dead' then 1 END) AS 'Dead', " +
"COUNT(CASE WHEN tx_curr.apr_18='Stopped ' then 1 END) AS 'Stopped', " +
"COUNT(CASE WHEN tx_curr.apr_18='Pending verification' then 1 END) AS 'Pending verification', " +
"COUNT(CASE WHEN tx_curr.apr_18!='' then 1 END) AS 'Total', " +
" " +
"COUNT(CASE WHEN tx_curr.may_18='Active' then 1 END) AS 'Active', " +
"COUNT(CASE WHEN tx_curr.may_18='Defaulters' OR tx_curr.may_18='Defaulter' then 1 END) AS 'Defaulter', " +
"COUNT(CASE WHEN tx_curr.may_18='T.O' OR tx_curr.may_18='TO' then 1 END) AS 'T.O', " +
"COUNT(CASE WHEN tx_curr.may_18='LTFU' then 1 END) AS 'LTFU', " +
"COUNT(CASE WHEN tx_curr.may_18='Dead' then 1 END) AS 'Dead', " +
"COUNT(CASE WHEN tx_curr.may_18='Stopped ' then 1 END) AS 'Stopped', " +
"COUNT(CASE WHEN tx_curr.may_18='Pending verification' then 1 END) AS 'Pending verification', " +
"COUNT(CASE WHEN tx_curr.may_18!='' then 1 END) AS 'Total', " +
" " +
"COUNT(CASE WHEN tx_curr.jun_18='Active' then 1 END) AS 'Active', " +
"COUNT(CASE WHEN tx_curr.jun_18='Defaulters' OR tx_curr.jun_18='Defaulter' then 1 END) AS 'Defaulter', " +
"COUNT(CASE WHEN tx_curr.jun_18='T.O' OR tx_curr.jun_18='TO' then 1 END) AS 'T.O', " +
"COUNT(CASE WHEN tx_curr.jun_18='LTFU' then 1 END) AS 'LTFU', " +
"COUNT(CASE WHEN tx_curr.jun_18='Dead' then 1 END) AS 'Dead', " +
"COUNT(CASE WHEN tx_curr.jun_18='Stopped ' then 1 END) AS 'Stopped', " +
"COUNT(CASE WHEN tx_curr.jun_18='Pending verification' then 1 END) AS 'Pending verification', " +
"COUNT(CASE WHEN tx_curr.jun_18!='' then 1 END) AS 'Total', " +
" " +
"COUNT(CASE WHEN tx_curr.jul_18='Active' then 1 END) AS 'Active', " +
"COUNT(CASE WHEN tx_curr.jul_18='Defaulters' OR tx_curr.jul_18='Defaulter' then 1 END) AS 'Defaulter', " +
"COUNT(CASE WHEN tx_curr.jul_18='T.O' OR tx_curr.jul_18='TO' then 1 END) AS 'T.O', " +
"COUNT(CASE WHEN tx_curr.jul_18='LTFU' then 1 END) AS 'LTFU', " +
"COUNT(CASE WHEN tx_curr.jul_18='Dead' then 1 END) AS 'Dead', " +
"COUNT(CASE WHEN tx_curr.jul_18='Stopped ' then 1 END) AS 'Stopped', " +
"COUNT(CASE WHEN tx_curr.jul_18='Pending verification' then 1 END) AS 'Pending verification', " +
"COUNT(CASE WHEN tx_curr.jul_18!='' then 1 END) AS 'Total' " +
" " +
"from tx_curr LEFT JOIN subpartnera ON tx_curr.mflcode=subpartnera.CentreSanteID  " +
"LEFT JOIN district ON district.DistrictID=subpartnera.DistrictID  " +
"LEFT JOIN county on district.CountyID=county.CountyID  "+where+ 
" GROUP BY district.DistrictNom ";
  
  
      conn.rs = conn.st.executeQuery(get_subcounty);
      //read headers and post them
      metaData = conn.rs.getMetaData();
col_count = metaData.getColumnCount(); //number of column

row_num = 0;
rowheader=shet2.createRow(row_num);
rowheader.setHeightInPoints(25);
    for(int i=2;i<col_count;i++){ 
    XSSFCell cell= rowheader.createCell(i);
    int pos = (i-2)/8;
    if(isEvenlyDivisable(i-2, 8)){
    cell.setCellValue(periods[pos]); 
     shet2.addMergedRegion(new CellRangeAddress(0,0,i,i+7));
    }
    cell.setCellStyle(styleMainHeader);
    }



row_num=1;
rowheader=shet2.createRow(row_num);
for(int i=1;i<=col_count;i++){
    shet2.setColumnWidth(i-1, 5000);
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
               
    if(isEvenlyDivisable(i-2, 8) && i>2){
      cell.setCellStyle(styleTotal);      
    }
    else{
   cell.setCellStyle(stborder);     
    }
          }
          row_num++;    
 }
   
 if(row_num>2){
  //for totals
   XSSFRow rowtotals=shet2.createRow(row_num);
   for(int i=2;i<col_count;i++){
       String col=columns[i];
       System.out.println("col:"+col);
       String formulae= "SUM("+col+"3:"+col+""+(row_num)+")";
       XSSFCell cell= rowtotals.createCell(i);
        cell.setCellType(XSSFCell.CELL_TYPE_FORMULA);
        cell.setCellFormula(formulae);
        cell.setCellStyle(styleTotal);          
      }
 }
 
  
  //END OF SUBCOUNTY REPORT
  
  //county summaries
    String getcounty="/*County summaries */ " +
" " +
"SELECT county.County AS 'County Name', " +
"COUNT(CASE WHEN tx_curr.oct_17='Active' then 1 END) AS 'Active', " +
"COUNT(CASE WHEN tx_curr.oct_17='Defaulters' OR tx_curr.oct_17='Defaulter' then 1 END) AS 'Defaulter', " +
"COUNT(CASE WHEN tx_curr.oct_17='T.O' OR tx_curr.oct_17='TO' then 1 END) AS 'T.O', " +
"COUNT(CASE WHEN tx_curr.oct_17='LTFU' then 1 END) AS 'LTFU', " +
"COUNT(CASE WHEN tx_curr.oct_17='Dead' then 1 END) AS 'Dead', " +
"COUNT(CASE WHEN tx_curr.oct_17='Stopped ' then 1 END) AS 'Stopped', " +
"COUNT(CASE WHEN tx_curr.oct_17='Pending verification' then 1 END) AS 'Pending verification', " +
"COUNT(CASE WHEN tx_curr.oct_17!='' then 1 END) AS 'Total', " +
" " +
"COUNT(CASE WHEN tx_curr.nov_17='Active' then 1 END) AS 'Active', " +
"COUNT(CASE WHEN tx_curr.nov_17='Defaulters' OR tx_curr.nov_17='Defaulter' then 1 END) AS 'Defaulter', " +
"COUNT(CASE WHEN tx_curr.nov_17='T.O' OR tx_curr.nov_17='TO' then 1 END) AS 'T.O', " +
"COUNT(CASE WHEN tx_curr.nov_17='LTFU' then 1 END) AS 'LTFU', " +
"COUNT(CASE WHEN tx_curr.nov_17='Dead' then 1 END) AS 'Dead', " +
"COUNT(CASE WHEN tx_curr.nov_17='Stopped ' then 1 END) AS 'Stopped', " +
"COUNT(CASE WHEN tx_curr.nov_17='Pending verification' then 1 END) AS 'Pending verification', " +
"COUNT(CASE WHEN tx_curr.nov_17!='' then 1 END) AS 'Total', " +
" " +
"COUNT(CASE WHEN tx_curr.dec_17='Active' then 1 END) AS 'Active', " +
"COUNT(CASE WHEN tx_curr.dec_17='Defaulters' OR tx_curr.dec_17='Defaulter' then 1 END) AS 'Defaulter', " +
"COUNT(CASE WHEN tx_curr.dec_17='T.O' OR tx_curr.dec_17='TO' then 1 END) AS 'T.O', " +
"COUNT(CASE WHEN tx_curr.dec_17='LTFU' then 1 END) AS 'LTFU', " +
"COUNT(CASE WHEN tx_curr.dec_17='Dead' then 1 END) AS 'Dead', " +
"COUNT(CASE WHEN tx_curr.dec_17='Stopped ' then 1 END) AS 'Stopped', " +
"COUNT(CASE WHEN tx_curr.dec_17='Pending verification' then 1 END) AS 'Pending verification', " +
"COUNT(CASE WHEN tx_curr.dec_17!='' then 1 END) AS 'Total', " +
" " +
"COUNT(CASE WHEN tx_curr.jan_18='Active' then 1 END) AS 'Active', " +
"COUNT(CASE WHEN tx_curr.jan_18='Defaulters' OR tx_curr.jan_18='Defaulter' then 1 END) AS 'Defaulter', " +
"COUNT(CASE WHEN tx_curr.jan_18='T.O' OR tx_curr.jan_18='TO' then 1 END) AS 'T.O', " +
"COUNT(CASE WHEN tx_curr.jan_18='LTFU' then 1 END) AS 'LTFU', " +
"COUNT(CASE WHEN tx_curr.jan_18='Dead' then 1 END) AS 'Dead', " +
"COUNT(CASE WHEN tx_curr.jan_18='Stopped ' then 1 END) AS 'Stopped', " +
"COUNT(CASE WHEN tx_curr.jan_18='Pending verification' then 1 END) AS 'Pending verification', " +
"COUNT(CASE WHEN tx_curr.jan_18!='' then 1 END) AS 'Total', " +
" " +
"COUNT(CASE WHEN tx_curr.feb_18='Active' then 1 END) AS 'Active', " +
"COUNT(CASE WHEN tx_curr.feb_18='Defaulters' OR tx_curr.feb_18='Defaulter' then 1 END) AS 'Defaulter', " +
"COUNT(CASE WHEN tx_curr.feb_18='T.O' OR tx_curr.feb_18='TO' then 1 END) AS 'T.O', " +
"COUNT(CASE WHEN tx_curr.feb_18='LTFU' then 1 END) AS 'LTFU', " +
"COUNT(CASE WHEN tx_curr.feb_18='Dead' then 1 END) AS 'Dead', " +
"COUNT(CASE WHEN tx_curr.feb_18='Stopped ' then 1 END) AS 'Stopped', " +
"COUNT(CASE WHEN tx_curr.feb_18='Pending verification' then 1 END) AS 'Pending verification', " +
"COUNT(CASE WHEN tx_curr.feb_18!='' then 1 END) AS 'Total', " +
" " +
"COUNT(CASE WHEN tx_curr.mar_18='Active' then 1 END) AS 'Active', " +
"COUNT(CASE WHEN tx_curr.mar_18='Defaulters' OR tx_curr.mar_18='Defaulter' then 1 END) AS 'Defaulter', " +
"COUNT(CASE WHEN tx_curr.mar_18='T.O' OR tx_curr.mar_18='TO' then 1 END) AS 'T.O', " +
"COUNT(CASE WHEN tx_curr.mar_18='LTFU' then 1 END) AS 'LTFU', " +
"COUNT(CASE WHEN tx_curr.mar_18='Dead' then 1 END) AS 'Dead', " +
"COUNT(CASE WHEN tx_curr.mar_18='Stopped ' then 1 END) AS 'Stopped', " +
"COUNT(CASE WHEN tx_curr.mar_18='Pending verification' then 1 END) AS 'Pending verification', " +
"COUNT(CASE WHEN tx_curr.mar_18!='' then 1 END) AS 'Total', " +
" " +
"COUNT(CASE WHEN tx_curr.apr_18='Active' then 1 END) AS 'Active', " +
"COUNT(CASE WHEN tx_curr.apr_18='Defaulters' OR tx_curr.apr_18='Defaulter' then 1 END) AS 'Defaulter', " +
"COUNT(CASE WHEN tx_curr.apr_18='T.O' OR tx_curr.apr_18='TO' then 1 END) AS 'T.O', " +
"COUNT(CASE WHEN tx_curr.apr_18='LTFU' then 1 END) AS 'LTFU', " +
"COUNT(CASE WHEN tx_curr.apr_18='Dead' then 1 END) AS 'Dead', " +
"COUNT(CASE WHEN tx_curr.apr_18='Stopped ' then 1 END) AS 'Stopped', " +
"COUNT(CASE WHEN tx_curr.apr_18='Pending verification' then 1 END) AS 'Pending verification', " +
"COUNT(CASE WHEN tx_curr.apr_18!='' then 1 END) AS 'Total', " +
" " +
"COUNT(CASE WHEN tx_curr.may_18='Active' then 1 END) AS 'Active', " +
"COUNT(CASE WHEN tx_curr.may_18='Defaulters' OR tx_curr.may_18='Defaulter' then 1 END) AS 'Defaulter', " +
"COUNT(CASE WHEN tx_curr.may_18='T.O' OR tx_curr.may_18='TO' then 1 END) AS 'T.O', " +
"COUNT(CASE WHEN tx_curr.may_18='LTFU' then 1 END) AS 'LTFU', " +
"COUNT(CASE WHEN tx_curr.may_18='Dead' then 1 END) AS 'Dead', " +
"COUNT(CASE WHEN tx_curr.may_18='Stopped ' then 1 END) AS 'Stopped', " +
"COUNT(CASE WHEN tx_curr.may_18='Pending verification' then 1 END) AS 'Pending verification', " +
"COUNT(CASE WHEN tx_curr.may_18!='' then 1 END) AS 'Total', " +
" " +
"COUNT(CASE WHEN tx_curr.jun_18='Active' then 1 END) AS 'Active', " +
"COUNT(CASE WHEN tx_curr.jun_18='Defaulters' OR tx_curr.jun_18='Defaulter' then 1 END) AS 'Defaulter', " +
"COUNT(CASE WHEN tx_curr.jun_18='T.O' OR tx_curr.jun_18='TO' then 1 END) AS 'T.O', " +
"COUNT(CASE WHEN tx_curr.jun_18='LTFU' then 1 END) AS 'LTFU', " +
"COUNT(CASE WHEN tx_curr.jun_18='Dead' then 1 END) AS 'Dead', " +
"COUNT(CASE WHEN tx_curr.jun_18='Stopped ' then 1 END) AS 'Stopped', " +
"COUNT(CASE WHEN tx_curr.jun_18='Pending verification' then 1 END) AS 'Pending verification', " +
"COUNT(CASE WHEN tx_curr.jun_18!='' then 1 END) AS 'Total', " +
" " +
"COUNT(CASE WHEN tx_curr.jul_18='Active' then 1 END) AS 'Active', " +
"COUNT(CASE WHEN tx_curr.jul_18='Defaulters' OR tx_curr.jul_18='Defaulter' then 1 END) AS 'Defaulter', " +
"COUNT(CASE WHEN tx_curr.jul_18='T.O' OR tx_curr.jul_18='TO' then 1 END) AS 'T.O', " +
"COUNT(CASE WHEN tx_curr.jul_18='LTFU' then 1 END) AS 'LTFU', " +
"COUNT(CASE WHEN tx_curr.jul_18='Dead' then 1 END) AS 'Dead', " +
"COUNT(CASE WHEN tx_curr.jul_18='Stopped ' then 1 END) AS 'Stopped', " +
"COUNT(CASE WHEN tx_curr.jul_18='Pending verification' then 1 END) AS 'Pending verification', " +
"COUNT(CASE WHEN tx_curr.jul_18!='' then 1 END) AS 'Total' " +
" " +
"from tx_curr LEFT JOIN subpartnera ON tx_curr.mflcode=subpartnera.CentreSanteID  " +
"LEFT JOIN district ON district.DistrictID=subpartnera.DistrictID  " +
"LEFT JOIN county on district.CountyID=county.CountyID  "+where+ 
" GROUP BY county.County ";
  
  
      conn.rs = conn.st.executeQuery(getcounty);
      //read headers and post them
      metaData = conn.rs.getMetaData();
col_count = metaData.getColumnCount(); //number of column

row_num = 0;
rowheader=shet3.createRow(row_num);
rowheader.setHeightInPoints(25);
    for(int i=1;i<col_count;i++){
    XSSFCell cell= rowheader.createCell(i);
    int pos = (i-1)/8;
    if(isEvenlyDivisable(i-1, 8)){
    cell.setCellValue(periods[pos]); 
     shet3.addMergedRegion(new CellRangeAddress(0,0,i,i+7));
    }
    cell.setCellStyle(styleMainHeader);
    }



row_num=1;
rowheader=shet3.createRow(row_num);
for(int i=1;i<=col_count;i++){
    shet3.setColumnWidth(i-1, 5000);
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
               
    if(isEvenlyDivisable(i-1, 8) && i>1){
      cell.setCellStyle(styleTotal);      
    }
    else{
   cell.setCellStyle(stborder);     
    }
          }
          row_num++;    
 }
   
 if(row_num>2){
  //for totals
   XSSFRow rowtotals=shet3.createRow(row_num);
   for(int i=1;i<col_count;i++){
       String col=columns[i];
       System.out.println("col:"+col);
       String formulae= "SUM("+col+"3:"+col+""+(row_num)+")";
       XSSFCell cell= rowtotals.createCell(i);
        cell.setCellType(XSSFCell.CELL_TYPE_FORMULA);
        cell.setCellFormula(formulae);
        cell.setCellStyle(styleTotal);          
      }
 }
 
  
 
 // Month Due for VL
  String due_viral_load="" +
        "/*County summaries */ " +
        " " +
        "SELECT county.County AS 'County Name', district.DistrictNom AS 'Sub County', subpartnera.SubPartnerNom AS 'Health Facility',CentreSanteId AS MFLCode,  " +
        "COUNT(CASE WHEN (tx_curr.month_due_vl='Nov-17' OR tx_curr.month_due_vl='Nov-2017') then 1 END) AS 'Nov 2017', " +
        "COUNT(CASE WHEN (tx_curr.month_due_vl='Dec-17' OR tx_curr.month_due_vl='Dec-2017') then 1 END) AS 'Dec 2017', " +
        "COUNT(CASE WHEN (tx_curr.month_due_vl='Jan-18' OR tx_curr.month_due_vl='Jan-2018') then 1 END) AS 'Jan 2018', " +
        "COUNT(CASE WHEN (tx_curr.month_due_vl='Feb-18' OR tx_curr.month_due_vl='Feb-2018') then 1 END) AS 'Feb 2018', " +
        "COUNT(CASE WHEN (tx_curr.month_due_vl='Mar-18' OR tx_curr.month_due_vl='Mar-2018') then 1 END) AS 'Mar 2018', " +
        "COUNT(CASE WHEN (tx_curr.month_due_vl='Apr-18' OR tx_curr.month_due_vl='Apr-2018') then 1 END) AS 'Apr 2018', " +
        "COUNT(CASE WHEN (tx_curr.month_due_vl='May-18' OR tx_curr.month_due_vl='May-2018') then 1 END) AS 'May 2018', " +
        "COUNT(CASE WHEN (tx_curr.month_due_vl='Jun-18' OR tx_curr.month_due_vl='Jun-2018') then 1 END) AS 'Jun 2018', " +
        "COUNT(CASE WHEN (tx_curr.month_due_vl='Jul-18' OR tx_curr.month_due_vl='Jul-2018') then 1 END) AS 'Jul 2018', " +
        "COUNT(CASE WHEN (tx_curr.month_due_vl='Aug-18' OR tx_curr.month_due_vl='Aug-2018') then 1 END) AS 'Aug 2018', " +
        "COUNT(CASE WHEN (tx_curr.month_due_vl='Sep-18' OR tx_curr.month_due_vl='Sep-2018') then 1 END) AS 'Sep 2018', " +
        "COUNT(CASE WHEN (tx_curr.month_due_vl='Oct-18' OR tx_curr.month_due_vl='Oct-2018') then 1 END) AS 'Oct 2018', " +
        "COUNT(CASE WHEN tx_curr.month_due_vl!='' then 1 END) AS 'Total' " +
        " " +
        "from tx_curr LEFT JOIN subpartnera ON tx_curr.mflcode=subpartnera.CentreSanteID  " +
        "LEFT JOIN district ON district.DistrictID=subpartnera.DistrictID  " +
        "LEFT JOIN county on district.CountyID=county.CountyID "+where+" " +
        "GROUP BY MFLCode";
  
  conn.rs = conn.st.executeQuery(due_viral_load);
   metaData = conn.rs.getMetaData();
col_count = metaData.getColumnCount(); //number of column

row_num=0;
rowheader=shet4.createRow(row_num);
for(int i=1;i<=col_count;i++){
    shet4.setColumnWidth(i-1, 5000);
    String column_name = metaData.getColumnLabel(i);
    XSSFCell cell= rowheader.createCell(i-1);
    cell.setCellValue(column_name); 
    cell.setCellStyle(styleHeader);
}

row_num++;
  while(conn.rs.next()){
            XSSFRow row=shet4.createRow(row_num);
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
  
  
   
// Summary by Status 
  String summary_by_status="SELECT * FROM summary_by_status";
  conn.rs = conn.st.executeQuery(summary_by_status);
   metaData = conn.rs.getMetaData();
col_count = metaData.getColumnCount(); //number of column
System.out.println("counter records : "+col_count);
row_num=0;
rowheader=shet5.createRow(row_num);
for(int i=1;i<=col_count;i++){
    shet5.setColumnWidth(i-1, 5000);
    String column_name = metaData.getColumnLabel(i);
    XSSFCell cell= rowheader.createCell(i-1);
    cell.setCellValue(column_name); 
    cell.setCellStyle(styleHeader);
}

row_num++;
  while(conn.rs.next()){
            XSSFRow row=shet5.createRow(row_num);
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
  
  
  
  
  
  //end of county summaries
  
 // output
        
        ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
        wb.write(outByteStream);
        byte [] outArray = outByteStream.toByteArray();
        response.setContentType("application/ms-excel");
        response.setContentLength(outArray.length);
        response.setHeader("Expires:", "0"); // eliminates browser caching
        response.setHeader("Content-Disposition", "attachment; filename=TX_CURR_RRI_"+manager.getdatekey()+".xlsx");
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
        Logger.getLogger(Tx_CURR.class.getName()).log(Level.SEVERE, null, ex);
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
        Logger.getLogger(Tx_CURR.class.getName()).log(Level.SEVERE, null, ex);
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
