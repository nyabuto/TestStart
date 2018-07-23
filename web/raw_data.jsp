<%-- 
    Document   : raw_data
    Created on : Jul 19, 2018, 11:01:53 AM
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
    <title>Raw Data Report</title>
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
                        <strong>Generate Raw Data Report</strong>
                      </div>
                      
                        <form action="RawData" method="post" class="form-horizontal">
                         <div class="card-body card-block"> 
                             <div class="row form-group">
                                 <div class="col col-md-12"> <b style="color: red;">Note:</b> Fields marked <b style="color: red;">*</b> are required. Other fields are optional.</div>
                          </div>
                             
                          <div class="row form-group">
                            <div class="col col-md-3">Select Reporting Year</div>
                            <div class="col-12 col-md-3"><select id="year" name="year" class="form-control-sm" style="min-width: 300px;"><option value="">Choose Year</option></select></div>
                          </div>
                          <div class="row form-group">
                            <div class="col col-md-3">Select Reporting Quarter</div>
                            <div class="col-12 col-md-3"><select id="quarter" name="quarter" class="form-control-sm" style="min-width: 300px;"><option value="">Choose Quarter</option></select></div>
                          </div>
<!--                             <div class="row form-group">
                            <div class="col col-md-3">County</div>
                            <div class="col-12 col-md-3"><select id="year" name="year" class="form-control-sm" style="min-width: 300px;"><option value="">Choose County</option></select></div>
                          </div>
                          <div class="row form-group">
                            <div class="col col-md-3">Select Sub County</div>
                            <div class="col-12 col-md-3"><select id="year" name="year" class="form-control-sm" style="min-width: 300px;"><option value="">Choose Sub-County</option></select></div>
                          </div>
                          <div class="row form-group">
                            <div class="col col-md-3">Select Health Facility</div>
                            <div class="col-12 col-md-3"><select id="year" name="year" class="form-control-sm" style="min-width: 300px;"><option value="">Choose Health Facility</option></select></div>
                          </div>
                    -->
                      </div>
                        <div class="card-footer" style="text-align: right;">
                        
                        <button type="reset" class="btn btn-danger btn-sm">
                          <i class="fa fa-ban"></i> Cancel
                        </button>
                        
                            <button type="submit" class="btn btn-primary btn-sm">
                          <i class="fa fa-dot-circle-o"></i> Generate Report
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


    <script src="assets/js/vendor/jquery-2.1.4.min.js"></script>
    <script src="assets/js/popper.min.js"></script>
    <script src="assets/js/plugins.js"></script>
    <script src="assets/js/main.js"></script>
    
    <script type="text/javascript">
         jQuery(document).ready(function() {
       load_years();
    
    jQuery("#year").change(function(){
       load_quarter(); 
    });
    });  
        
        function load_years(){
               jQuery.ajax({
        url:'load_years',
        type:"post",
        dataType:"html",
        success:function(output){  
          jQuery("#year").html(output); 
        }
        
               });
        }
        function load_quarter(){
            var year = jQuery("#year").val();
               jQuery.ajax({
        url:'load_quarter?year='+year,
        type:"post",
        dataType:"html",
        success:function(output){
          jQuery("#quarter").html(output); 
        }
        
               });
        }
        </script>
</body>
</html>
