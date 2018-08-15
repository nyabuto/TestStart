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
                   String[] county_data = request.getParameterValues("county"); 
                   where = " WHERE (";
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
                     where = "";   
                    }
                //where = " WHERE county.CountyID='"+county+"' ";
                   
                }
                
            }
            else{
                System.out.println("subcounty : "+request.getParameter("sub_county"));
            String[] sub_county_data = request.getParameterValues("sub_county");
               where = " WHERE (";
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
            where = " ";   
           } 
                System.out.println(has_data+" where : "+where);    
              // where = " WHERE district.DistrictID='"+sub_county+"' ";    
            }
        }
        else{
         String[] facility_data = request.getParameterValues("facility");   
            where = " WHERE (";
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
    XSSFSheet shet5=wb.createSheet("Project Summary By Status");
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
    
    XSSFCellStyle styleRed = wb.createCellStyle();
    styleRed.setFillForegroundColor(HSSFColor.RED.index);
    styleRed.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    styleRed.setBorderTop(BorderStyle.THIN);
    styleRed.setBorderBottom(BorderStyle.THIN);
    styleRed.setBorderLeft(BorderStyle.THIN);
    styleRed.setBorderRight(BorderStyle.THIN);
    styleRed.setAlignment(HorizontalAlignment.CENTER);
    
    XSSFCellStyle styleGreen = wb.createCellStyle();
    styleGreen.setFillForegroundColor(HSSFColor.GREEN.index);
    styleGreen.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    styleGreen.setBorderTop(BorderStyle.THIN);
    styleGreen.setBorderBottom(BorderStyle.THIN);
    styleGreen.setBorderLeft(BorderStyle.THIN);
    styleGreen.setBorderRight(BorderStyle.THIN);
    styleGreen.setAlignment(HorizontalAlignment.CENTER);
    
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
    
    
       
      facil_data = "SELECT * FROM rpt_facil_summary "+where+" " +
                    "GROUP BY MFLCode";  
        System.out.println("facil_data:"+facil_data);
      conn.rs = conn.st.executeQuery(facil_data);
      //read headers and post them
      metaData = conn.rs.getMetaData();
col_count = metaData.getColumnCount()-3; //number of column

rowheader=shet1.createRow(row_num);
rowheader.setHeightInPoints(25);
    for(int i=4;i<col_count;i++){ 
    XSSFCell cell= rowheader.createCell(i);
    int pos = (i-4)/10;
    if(isEvenlyDivisable(i-4, 10)){
    cell.setCellValue(periods[pos]); 
     shet1.addMergedRegion(new CellRangeAddress(0,0,i,i+9));
    }
    cell.setCellStyle(styleMainHeader);
    }



row_num=1;
rowheader=shet1.createRow(row_num);
for(int i=1;i<=col_count;i++){
    shet1.setColumnWidth(i-1, 5000);
    String column_name = metaData.getColumnLabel(i);
    if(column_name.contains("-201")){
        String[] datalabes = column_name.split("-");
        column_name = datalabes[0];
    }
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
               
    if(isEvenlyDivisable(i-4, 10) && i>4){
        if(value==null){value="0";}
      if(Integer.parseInt(value)<=0){
      cell.setCellStyle(styleGreen);  
        }
        else{
        cell.setCellStyle(styleRed);      
        } 
      cell = row.getCell(i-3);
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
        "GROUP BY tx_curr.id";
  conn.rs = conn.st.executeQuery(rawData);
        System.out.println("Raw Data : "+rawData);
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
        String get_subcounty="SELECT `County Name` AS 'County Name', `Sub County` AS 'Sub County', " +
      "SUM(`Active-201710`) AS 'Active',  " +
      "SUM(`Defaulter-201710`) AS 'Defaulter',  " +
      "SUM(`T.O-201710`) AS 'T.O',  " +
      "SUM(`LTFU-201710`) AS 'LTFU',  " +
      "SUM(`Dead-201710`)  AS 'Dead',  " +
      "SUM(`Stopped-201710`)  AS 'Stopped',  " +
      "SUM(`Pending verification-201710`)  AS 'Pending verification',  " +
      "SUM(`Total-201710`) AS 'Total',   " +
      "SUM(`Reported in MOH731-201710`) AS 'Reported in MOH731', " +
      "SUM(`Variance-201710`) AS 'Variance',  " +
      " " +
      "SUM(`Active-201711`) AS 'Active',  " +
      "SUM(`Defaulter-201711`) AS 'Defaulter',  " +
      "SUM(`T.O-201711`) AS 'T.O',  " +
      "SUM(`LTFU-201711`) AS 'LTFU',  " +
      "SUM(`Dead-201711`)  AS 'Dead',  " +
      "SUM(`Stopped-201711`)  AS 'Stopped',  " +
      "SUM(`Pending verification-201711`)  AS 'Pending verification',  " +
      "SUM(`Total-201711`) AS 'Total',   " +
      "SUM(`Reported in MOH731-201711`) AS 'Reported in MOH731', " +
      "SUM(`Variance-201711`) AS 'Variance',  " +
      " " +
      "SUM(`Active-201712`) AS 'Active',  " +
      "SUM(`Defaulter-201712`) AS 'Defaulter',  " +
      "SUM(`T.O-201712`) AS 'T.O',  " +
      "SUM(`LTFU-201712`) AS 'LTFU',  " +
      "SUM(`Dead-201712`)  AS 'Dead',  " +
      "SUM(`Stopped-201712`)  AS 'Stopped',  " +
      "SUM(`Pending verification-201712`)  AS 'Pending verification',  " +
      "SUM(`Total-201712`) AS 'Total',   " +
      "SUM(`Reported in MOH731-201712`) AS 'Reported in MOH731', " +
      "SUM(`Variance-201712`) AS 'Variance',  " +
      " " +
      "SUM(`Active-201801`) AS 'Active',  " +
      "SUM(`Defaulter-201801`) AS 'Defaulter',  " +
      "SUM(`T.O-201801`) AS 'T.O',  " +
      "SUM(`LTFU-201801`) AS 'LTFU',  " +
      "SUM(`Dead-201801`)  AS 'Dead',  " +
      "SUM(`Stopped-201801`)  AS 'Stopped',  " +
      "SUM(`Pending verification-201801`)  AS 'Pending verification',  " +
      "SUM(`Total-201801`) AS 'Total',   " +
      "SUM(`Reported in MOH731-201801`) AS 'Reported in MOH731', " +
      "SUM(`Variance-201801`) AS 'Variance',  " +
      " " +
      "SUM(`Active-201802`) AS 'Active',  " +
      "SUM(`Defaulter-201802`) AS 'Defaulter',  " +
      "SUM(`T.O-201802`) AS 'T.O',  " +
      "SUM(`LTFU-201802`) AS 'LTFU',  " +
      "SUM(`Dead-201802`)  AS 'Dead',  " +
      "SUM(`Stopped-201802`)  AS 'Stopped',  " +
      "SUM(`Pending verification-201802`)  AS 'Pending verification',  " +
      "SUM(`Total-201802`) AS 'Total',   " +
      "SUM(`Reported in MOH731-201802`) AS 'Reported in MOH731', " +
      "SUM(`Variance-201802`) AS 'Variance',  " +
      " " +
      "SUM(`Active-201803`) AS 'Active',  " +
      "SUM(`Defaulter-201803`) AS 'Defaulter',  " +
      "SUM(`T.O-201803`) AS 'T.O',  " +
      "SUM(`LTFU-201803`) AS 'LTFU',  " +
      "SUM(`Dead-201803`)  AS 'Dead',  " +
      "SUM(`Stopped-201803`)  AS 'Stopped',  " +
      "SUM(`Pending verification-201803`)  AS 'Pending verification',  " +
      "SUM(`Total-201803`) AS 'Total',   " +
      "SUM(`Reported in MOH731-201803`) AS 'Reported in MOH731', " +
      "SUM(`Variance-201803`) AS 'Variance', " +
      " " +
      "SUM(`Active-201804`) AS 'Active',  " +
      "SUM(`Defaulter-201804`) AS 'Defaulter',  " +
      "SUM(`T.O-201804`) AS 'T.O',  " +
      "SUM(`LTFU-201804`) AS 'LTFU',  " +
      "SUM(`Dead-201804`)  AS 'Dead',  " +
      "SUM(`Stopped-201804`)  AS 'Stopped',  " +
      "SUM(`Pending verification-201804`)  AS 'Pending verification',  " +
      "SUM(`Total-201804`) AS 'Total',   " +
      "SUM(`Reported in MOH731-201804`) AS 'Reported in MOH731', " +
      "SUM(`Variance-201804`) AS 'Variance',  " +
      " " +
      "SUM(`Active-201805`) AS 'Active',  " +
      "SUM(`Defaulter-201805`) AS 'Defaulter',  " +
      "SUM(`T.O-201805`) AS 'T.O',  " +
      "SUM(`LTFU-201805`) AS 'LTFU',  " +
      "SUM(`Dead-201805`)  AS 'Dead',  " +
      "SUM(`Stopped-201805`)  AS 'Stopped',  " +
      "SUM(`Pending verification-201805`)  AS 'Pending verification',  " +
      "SUM(`Total-201805`) AS 'Total',   " +
      "SUM(`Reported in MOH731-201805`) AS 'Reported in MOH731', " +
      "SUM(`Variance-201805`) AS 'Variance',  " +
      " " +
      "SUM(`Active-201806`) AS 'Active',  " +
      "SUM(`Defaulter-201806`) AS 'Defaulter',  " +
      "SUM(`T.O-201806`) AS 'T.O',  " +
      "SUM(`LTFU-201806`) AS 'LTFU',  " +
      "SUM(`Dead-201806`)  AS 'Dead',  " +
      "SUM(`Stopped-201806`)  AS 'Stopped',  " +
      "SUM(`Pending verification-201806`)  AS 'Pending verification',  " +
      "SUM(`Total-201806`) AS 'Total',   " +
      "SUM(`Reported in MOH731-201806`) AS 'Reported in MOH731', " +
      "SUM(`Variance-201806`) AS 'Variance',  " +
      " " +
      "SUM(`Active-201807`) AS 'Active',  " +
      "SUM(`Defaulter-201807`) AS 'Defaulter',  " +
      "SUM(`T.O-201807`) AS 'T.O',  " +
      "SUM(`LTFU-201807`) AS 'LTFU',  " +
      "SUM(`Dead-201807`)  AS 'Dead',  " +
      "SUM(`Stopped-201807`)  AS 'Stopped',  " +
      "SUM(`Pending verification-201807`)  AS 'Pending verification',  " +
      "SUM(`Total-201807`) AS 'Total',   " +
      "SUM(`Reported in MOH731-201807`) AS 'Reported in MOH731', " +
      "SUM(`Variance-201807`) AS 'Variance' " +
      " FROM rpt_facil_summary "+where+ " GROUP BY SubCountyID";

  
      conn.rs = conn.st.executeQuery(get_subcounty);
      //read headers and post them
      metaData = conn.rs.getMetaData();
col_count = metaData.getColumnCount(); //number of column
System.out.println("columns:"+col_count);
row_num = 0;
rowheader=shet2.createRow(row_num);
rowheader.setHeightInPoints(25);
    for(int i=1;i<col_count;i++){ 
    XSSFCell cell= rowheader.createCell(i);
    int pos = (i-2)/10;
    if(isEvenlyDivisable(i-2, 10) && pos<periods.length){
        System.out.println("pos:"+pos);
    cell.setCellValue(periods[pos]); 
     shet2.addMergedRegion(new CellRangeAddress(0,0,i,i+9));
    }
    cell.setCellStyle(styleMainHeader);
    }



row_num=1;
rowheader=shet2.createRow(row_num);
String column_name = "";
for(int i=1;i<=col_count;i++){
    shet2.setColumnWidth(i-1, 5000);
    column_name = metaData.getColumnLabel(i);
    XSSFCell cell= rowheader.createCell(i-1);
    cell.setCellValue(column_name); 
    cell.setCellStyle(styleHeader);
    
}
row_num++;
 String value="";
 while(conn.rs.next()){

    XSSFRow row=shet2.createRow(row_num);
    for(int i=1;i<=col_count;i++){
        value=conn.rs.getString(i);    
        XSSFCell cell= row.createCell(i-1);
        if(isNumeric(value)){
        cell.setCellValue(Double.parseDouble(value));
        }
        else{
        cell.setCellValue(value);    
        }
               
    if(isEvenlyDivisable(i-2, 10) && i>2){
          if(value==null){value="0";}
     if(Integer.parseInt(value)<=0){
      cell.setCellStyle(styleGreen); 
        }
        else{
        cell.setCellStyle(styleRed);      
        }
      cell = row.getCell(i-3);
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
       String formulae= "SUM("+col+"3:"+col+""+(row_num)+")";
       XSSFCell cell= rowtotals.createCell(i);
        cell.setCellType(XSSFCell.CELL_TYPE_FORMULA);
        cell.setCellFormula(formulae);
        cell.setCellStyle(styleTotal);          
      }
 }
 
  
  //END OF SUBCOUNTY REPORT
  
  //county summaries
            String getcounty="SELECT `County Name` AS 'County Name', " +
        "SUM(`Active-201710`) AS 'Active',  " +
        "SUM(`Defaulter-201710`) AS 'Defaulter',  " +
        "SUM(`T.O-201710`) AS 'T.O',  " +
        "SUM(`LTFU-201710`) AS 'LTFU',  " +
        "SUM(`Dead-201710`)  AS 'Dead',  " +
        "SUM(`Stopped-201710`)  AS 'Stopped',  " +
        "SUM(`Pending verification-201710`)  AS 'Pending verification',  " +
        "SUM(`Total-201710`) AS 'Total',   " +
        "SUM(`Reported in MOH731-201710`) AS 'Reported in MOH731', " +
        "SUM(`Variance-201710`) AS 'Variance',  " +
        " " +
        "SUM(`Active-201711`) AS 'Active',  " +
        "SUM(`Defaulter-201711`) AS 'Defaulter',  " +
        "SUM(`T.O-201711`) AS 'T.O',  " +
        "SUM(`LTFU-201711`) AS 'LTFU',  " +
        "SUM(`Dead-201711`)  AS 'Dead',  " +
        "SUM(`Stopped-201711`)  AS 'Stopped',  " +
        "SUM(`Pending verification-201711`)  AS 'Pending verification',  " +
        "SUM(`Total-201711`) AS 'Total',   " +
        "SUM(`Reported in MOH731-201711`) AS 'Reported in MOH731', " +
        "SUM(`Variance-201711`) AS 'Variance',  " +
        " " +
        "SUM(`Active-201712`) AS 'Active',  " +
        "SUM(`Defaulter-201712`) AS 'Defaulter',  " +
        "SUM(`T.O-201712`) AS 'T.O',  " +
        "SUM(`LTFU-201712`) AS 'LTFU',  " +
        "SUM(`Dead-201712`)  AS 'Dead',  " +
        "SUM(`Stopped-201712`)  AS 'Stopped',  " +
        "SUM(`Pending verification-201712`)  AS 'Pending verification',  " +
        "SUM(`Total-201712`) AS 'Total',   " +
        "SUM(`Reported in MOH731-201712`) AS 'Reported in MOH731', " +
        "SUM(`Variance-201712`) AS 'Variance',  " +
        " " +
        "SUM(`Active-201801`) AS 'Active',  " +
        "SUM(`Defaulter-201801`) AS 'Defaulter',  " +
        "SUM(`T.O-201801`) AS 'T.O',  " +
        "SUM(`LTFU-201801`) AS 'LTFU',  " +
        "SUM(`Dead-201801`)  AS 'Dead',  " +
        "SUM(`Stopped-201801`)  AS 'Stopped',  " +
        "SUM(`Pending verification-201801`)  AS 'Pending verification',  " +
        "SUM(`Total-201801`) AS 'Total',   " +
        "SUM(`Reported in MOH731-201801`) AS 'Reported in MOH731', " +
        "SUM(`Variance-201801`) AS 'Variance',  " +
        " " +
        "SUM(`Active-201802`) AS 'Active',  " +
        "SUM(`Defaulter-201802`) AS 'Defaulter',  " +
        "SUM(`T.O-201802`) AS 'T.O',  " +
        "SUM(`LTFU-201802`) AS 'LTFU',  " +
        "SUM(`Dead-201802`)  AS 'Dead',  " +
        "SUM(`Stopped-201802`)  AS 'Stopped',  " +
        "SUM(`Pending verification-201802`)  AS 'Pending verification',  " +
        "SUM(`Total-201802`) AS 'Total',   " +
        "SUM(`Reported in MOH731-201802`) AS 'Reported in MOH731', " +
        "SUM(`Variance-201802`) AS 'Variance',  " +
        " " +
        "SUM(`Active-201803`) AS 'Active',  " +
        "SUM(`Defaulter-201803`) AS 'Defaulter',  " +
        "SUM(`T.O-201803`) AS 'T.O',  " +
        "SUM(`LTFU-201803`) AS 'LTFU',  " +
        "SUM(`Dead-201803`)  AS 'Dead',  " +
        "SUM(`Stopped-201803`)  AS 'Stopped',  " +
        "SUM(`Pending verification-201803`)  AS 'Pending verification',  " +
        "SUM(`Total-201803`) AS 'Total',   " +
        "SUM(`Reported in MOH731-201803`) AS 'Reported in MOH731', " +
        "SUM(`Variance-201803`) AS 'Variance', " +
        " " +
        "SUM(`Active-201804`) AS 'Active',  " +
        "SUM(`Defaulter-201804`) AS 'Defaulter',  " +
        "SUM(`T.O-201804`) AS 'T.O',  " +
        "SUM(`LTFU-201804`) AS 'LTFU',  " +
        "SUM(`Dead-201804`)  AS 'Dead',  " +
        "SUM(`Stopped-201804`)  AS 'Stopped',  " +
        "SUM(`Pending verification-201804`)  AS 'Pending verification',  " +
        "SUM(`Total-201804`) AS 'Total',   " +
        "SUM(`Reported in MOH731-201804`) AS 'Reported in MOH731', " +
        "SUM(`Variance-201804`) AS 'Variance',  " +
        " " +
        "SUM(`Active-201805`) AS 'Active',  " +
        "SUM(`Defaulter-201805`) AS 'Defaulter',  " +
        "SUM(`T.O-201805`) AS 'T.O',  " +
        "SUM(`LTFU-201805`) AS 'LTFU',  " +
        "SUM(`Dead-201805`)  AS 'Dead',  " +
        "SUM(`Stopped-201805`)  AS 'Stopped',  " +
        "SUM(`Pending verification-201805`)  AS 'Pending verification',  " +
        "SUM(`Total-201805`) AS 'Total',   " +
        "SUM(`Reported in MOH731-201805`) AS 'Reported in MOH731', " +
        "SUM(`Variance-201805`) AS 'Variance',  " +
        " " +
        "SUM(`Active-201806`) AS 'Active',  " +
        "SUM(`Defaulter-201806`) AS 'Defaulter',  " +
        "SUM(`T.O-201806`) AS 'T.O',  " +
        "SUM(`LTFU-201806`) AS 'LTFU',  " +
        "SUM(`Dead-201806`)  AS 'Dead',  " +
        "SUM(`Stopped-201806`)  AS 'Stopped',  " +
        "SUM(`Pending verification-201806`)  AS 'Pending verification',  " +
        "SUM(`Total-201806`) AS 'Total',   " +
        "SUM(`Reported in MOH731-201806`) AS 'Reported in MOH731', " +
        "SUM(`Variance-201806`) AS 'Variance',  " +
        " " +
        "SUM(`Active-201807`) AS 'Active',  " +
        "SUM(`Defaulter-201807`) AS 'Defaulter',  " +
        "SUM(`T.O-201807`) AS 'T.O',  " +
        "SUM(`LTFU-201807`) AS 'LTFU',  " +
        "SUM(`Dead-201807`)  AS 'Dead',  " +
        "SUM(`Stopped-201807`)  AS 'Stopped',  " +
        "SUM(`Pending verification-201807`)  AS 'Pending verification',  " +
        "SUM(`Total-201807`) AS 'Total',   " +
        "SUM(`Reported in MOH731-201807`) AS 'Reported in MOH731', " +
        "SUM(`Variance-201807`) AS 'Variance' " +
        "FROM rpt_facil_summary "+where+ " GROUP BY CountyID";
  
  
      conn.rs = conn.st.executeQuery(getcounty);
      //read headers and post them
      metaData = conn.rs.getMetaData();
col_count = metaData.getColumnCount(); //number of column

row_num = 0;
rowheader=shet3.createRow(row_num);
rowheader.setHeightInPoints(25);
    for(int i=1;i<col_count;i++){
    XSSFCell cell= rowheader.createCell(i);
    int pos = (i-1)/10;
    if(isEvenlyDivisable(i-1, 10) && pos<periods.length){
    cell.setCellValue(periods[pos]); 
     shet3.addMergedRegion(new CellRangeAddress(0,0,i,i+9));
    }
    cell.setCellStyle(styleMainHeader);
    }



row_num=1;
rowheader=shet3.createRow(row_num);
for(int i=1;i<=col_count;i++){
    shet3.setColumnWidth(i-1, 5000);
    column_name = metaData.getColumnLabel(i);
    XSSFCell cell= rowheader.createCell(i-1);
    cell.setCellValue(column_name); 
    cell.setCellStyle(styleHeader);
}
row_num++;
 while(conn.rs.next()){
          XSSFRow row=shet3.createRow(row_num);
          for(int i=1;i<=col_count;i++){
               value=conn.rs.getString(i);
               XSSFCell cell= row.createCell(i-1);
               if(isNumeric(value)){
               cell.setCellValue(Double.parseDouble(value));
               }
               else{
               cell.setCellValue(value);    
               }
               
    if(isEvenlyDivisable(i-1, 10) && i>1){
          if(value==null){value="0";}
        if(Integer.parseInt(value)<=0){
      cell.setCellStyle(styleGreen);  
        }
        else{
        cell.setCellStyle(styleRed);      
        }
      
      cell = row.getCell(i-3);
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
    column_name = metaData.getColumnLabel(i);
    XSSFCell cell= rowheader.createCell(i-1);
    cell.setCellValue(column_name); 
    cell.setCellStyle(styleHeader);
}

row_num++;
  while(conn.rs.next()){
            XSSFRow row=shet4.createRow(row_num);
          for(int i=1;i<=col_count;i++){
              value=conn.rs.getString(i);
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
    column_name = metaData.getColumnLabel(i);
    XSSFCell cell= rowheader.createCell(i-1);
    cell.setCellValue(column_name); 
    cell.setCellStyle(styleHeader);
}

row_num++;
  while(conn.rs.next()){
            XSSFRow row=shet5.createRow(row_num);
          for(int i=1;i<=col_count;i++){
              value=conn.rs.getString(i);
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
