<%-- 
    Document   : upload_data
    Created on : May 4, 2018, 12:52:41 PM
    Author     : GNyabuto
--%>

<%@page import="org.json.simple.JSONArray"%>
<%@page import="org.json.simple.JSONObject"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>Test&Start Data</title>
    <meta name="description" content="Sufee Admin - HTML5 Admin Template">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <link rel="apple-touch-icon" href="images/logo.png">
    <link rel="shortcut icon" href="images/logo.png">

    <link rel="stylesheet" href="assets/css/normalize.css">
    <link rel="stylesheet" href="assets/css/bootstrap.min.css">
    <link rel="stylesheet" href="assets/css/font-awesome.min.css">
    <link rel="stylesheet" href="assets/css/themify-icons.css">
    <link rel="stylesheet" href="assets/css/flag-icon.min.css">
    <link rel="stylesheet" href="assets/css/cs-skin-elastic.css">
    <!-- <link rel="stylesheet" href="assets/css/bootstrap-select.less"> -->
    <link rel="stylesheet" href="assets/scss/style.css">

    <link href='https://fonts.googleapis.com/css?family=Open+Sans:400,600,700,800' rel='stylesheet' type='text/css'>

    <!-- <script type="text/javascript" src="https://cdn.jsdelivr.net/html5shiv/3.7.3/html5shiv.min.js"></script> -->

</head>
<body>
        <!-- Left Panel -->
    <%@include file="sidebar.jsp" %>

    <!-- Left Panel -->

    <!-- Right Panel -->

    <div id="right-panel" class="right-panel">

        <!-- Header-->
       <%@include file="header.jsp" %>
        <!-- Header-->

        <div class="content mt-3">
            <div class="animated fadeIn">


                <div class="row">

                  <div class="col-lg-12">
                    <div class="card">
                      <div class="card-header">
                        <strong>Test&Start Data Upload Module</strong>
                      </div>
                      
                        <form action="UploadExcels" method="post" enctype="multipart/form-data" class="form-horizontal">
                         <div class="card-body card-block"> 
                          <div class="row form-group">
                            <div class="col col-md-3"><label for="file-multiple-input" class=" form-control-label">Select Excel files to upload</label></div>
                            <div class="col-12 col-md-6"><input type="file" id="file" name="file" multiple="" class="form-control-file" required=""></div>
                          </div>
                    
                      </div>
                        <div class="card-footer" style="text-align: right;">
                        
                        <button type="reset" class="btn btn-danger btn-sm">
                          <i class="fa fa-ban"></i> Cancel
                        </button>
                        
                            <button type="submit" class="btn btn-primary btn-sm">
                          <i class="fa fa-dot-circle-o"></i> Upload
                        </button>
                      </div>
                            </form>
                    </div>
                  </div>
                    <div style="margin-left: 2%;">
                    <div id="response" class="thead-light" style="text-align: center;">
                    <!--upload response-->
                    </div>
                    <br>
                    <div id="errors" class="thead-light">
                    </div >
                    </div>
                </div>


            </div><!-- .animated -->
        </div><!-- .content -->


    </div><!-- /#right-panel -->

    <!-- Right Panel -->


    <script src="assets/js/vendor/jquery-1.11.3.min.js"></script>
    <script src="assets/js/popper.min.js"></script>
    <script src="assets/js/plugins.js"></script>
    <script src="assets/js/main.js"></script>


    <%if(session.getAttribute("upload_errors")!=null){
      String output="";
        JSONArray finalarray = new JSONArray();
        finalarray = (JSONArray)session.getAttribute("upload_errors");
        for(int j=0;j<finalarray.size();j++){
           JSONObject obj_data =  (JSONObject)finalarray.get(j);
           String filename = obj_data.get("file_name").toString();
           
           output += "<div style=\"font-size:20px; text-decoration: underline; font-weight: 900;\"><br><br>File "+(j+1)+". "+filename+"<br></div>";
        JSONArray jarray = new JSONArray();
       
        jarray = (JSONArray)obj_data.get("upload_info");
         if(jarray.size()>0){
        for(int i=0;i<jarray.size();i++){
            output += "<div>";
            JSONObject objdata = new JSONObject();
            objdata =  (JSONObject)jarray.get(i);
            if(objdata.containsKey("period_error")){
                //show upload error missing year or month
                String error = objdata.get("period_error").toString();
                String sheetname =  objdata.get("sheetname").toString();
                out.println(sheetname);
                output+="<div style=\"font-size:20px;\"><b>"+sheetname+"</b><br><font color=\"red\">"+error+"</font><br></div>";
            }
            else{
                String added="",skipped_details="",sheetname="";
                int skipped=0;
                if(objdata.containsKey("added")){
                added = objdata.get("added").toString();
                }
                if(objdata.containsKey("skipped_details")){
                skipped_details =objdata.get("skipped_details").toString();
                    }
                if(objdata.containsKey("skipped")){
                skipped =  Integer.parseInt(objdata.get("skipped").toString());
                 }
                if(objdata.containsKey("sheetname")){
                sheetname =  objdata.get("sheetname").toString();
                }
//                out.println(sheetname);
//                String sheetname =  "";
                
                output+="<div style=\"font-size:20px;\"><br><b>"+sheetname+"</b><br><font color=\"blue\">"+added+" records added</font> and <font color=\"red\">"+skipped+" records skipped:</font><br></div>";
                if(skipped>0){
                output+="<table class=\"table\" style=\"font-size:12px;\">"+skipped_details+"</table>";
                }
            }
            output+="</div></div>";
        }
       }
         else{
             output +="Nothing to display";
         }
    }
    
//        out.println(output);

    %>
     <script type="text/javascript">
           jQuery(document).ready(function() {
           jQuery("#response").html('<%=output%>');
           
            });
    </script>
    <% 
        session.removeAttribute("upload_errors");
        }
    %>
    
</body>
</html>
