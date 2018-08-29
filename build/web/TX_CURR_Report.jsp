<%-- 
    Document   : TX_CURR_Report
    Created on : Aug 7, 2018, 4:22:58 PM
    Author     : GNyabuto
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>TX_CURR Report</title>
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
    <link rel="stylesheet" href="assets/js/select2/css/select2.min.css">
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
       <%@include file="header2.jsp" %>
        <!-- Header-->

        <div class="content mt-3">
            <div class="animated fadeIn">


                <div class="row">

                  <div class="col-lg-12">
                    <div class="card">
                      <div class="card-header">
                        <strong>Generate Current on ART RRI Report</strong>
                      </div>
                      
                        <form action="Tx_CURR" method="post" class="form-horizontal">
                         <div class="card-body card-block"> 
                             <div class="row form-group">
                                 <div class="col col-md-12"> <b style="color: red;">Note:</b> <b>ALL FIELDS ARE OPTIONAL.</b>
                                 <br>1. To generate report for all project sites, do not select anything. Just click on <b>Generate Report</b>.
                                 <br>2. To generate county report for some counties, select the <b>county</b> and do not select any sub-county or facility.
                                 <br>3. To generate report for some sub-counties, select the <b>county,sub-county</b> and do not select any facility.
                                 <br>4. To generate Facility report for some facilities, select <b>county,sub-county and then facility</b>.
                                 
                                 </div>
                          </div>
                             
                          <div class="row form-group">
                            <div class="col col-md-3">WorkLoad</div>
                            <div class="col-12 col-md-3"><select id="highv" name="highv" class="form-control-sm js-example-basic-multiple" style="min-width: 300px;">
                                    <option value="2" selected>All sites</option>
                                    <option value="1">High-volume sites</option>
                                    <option value="0">Non high-volume sites</option>>
                                </select>
                            </div>
                          </div>
                             
                          <div class="row form-group">
                            <div class="col col-md-3">Select County</div>
                            <div class="col-12 col-md-3"><select id="county" name="county" class="form-control-sm js-example-basic-multiple" multiple="multiple" style="min-width: 300px;"><option value="">Choose County</option></select></div>
                          </div>
                          <div class="row form-group">
                            <div class="col col-md-3">Select Sub County</div>
                            <div class="col-12 col-md-3"><select id="sub_county" name="sub_county" class="form-control-sm js-example-basic-multiple" multiple="multiple" style="min-width: 300px;"><option value="">Choose Sub County</option></select></div>
                          </div>
                             
                          <div class="row form-group">
                            <div class="col col-md-3">Select Health Facility</div>
                            <div class="col-12 col-md-3"><select id="facility" name="facility" class="form-control-sm js-example-basic-multiple" multiple="multiple" style="min-width: 300px;"><option value="">Choose Health Facility</option></select></div>
                          </div>
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
    <script src="assets/js/select2/js/select2.min.js"></script>
    
    <script type="text/javascript">
         jQuery(document).ready(function() { 
             
        jQuery('#highv').select2();
        jQuery('#county').select2({
              placeholder: "[Optional] Select counties"
          });
          
       jQuery('#sub_county').select2({
              placeholder: "[Optional] Select sub counties"
          });
        jQuery('#facility').select2({
              placeholder: "[Optional] Select facilities"
          });
          
          
       load_county();
    
    jQuery("#county").change(function(){
       load_subcounty(); 
    });
    
    jQuery("#sub_county").change(function(){
       load_facility(); 
    });
    jQuery("#highv").change(function(){
        load_county();
        load_subcounty(); 
        load_facility();
    });
    
    });  
        
        function load_county(){
            var highv = jQuery("#highv").val();
       jQuery("#county").val(null);
               jQuery.ajax({
        url:'load_counties?highv='+highv,
        type:"post",
        dataType:"html",
        success:function(output){  
          jQuery("#county").html(output); 
          jQuery('#county').select2({
              placeholder: "[Optional] Select counties"
          });
        }
        
               });
        }
        function load_subcounty(){
              var highv = jQuery("#highv").val();
            var county = jQuery("#county").val();
            if(county!=null){
            county = county.toString();
             county = county.replace(/,/g,"_");
               jQuery.ajax({
        url:'load_sub_counties?county='+county+"&&highv="+highv,
        type:"post",
        dataType:"html",
        success:function(output){
          jQuery("#sub_county").html(output); 
          jQuery('#sub_county').select2({
              placeholder: "[Optional] Select sub counties"
          });
          
        }
        
               });
           }
        }
        
        function load_facility(){
              var highv = jQuery("#highv").val();
            var sub_county = jQuery("#sub_county").val();
            if(sub_county!=null){
             sub_county = sub_county.toString();
             sub_county = sub_county.replace(/,/g,"_");
               jQuery.ajax({
        url:'load_facilities?sub_county='+sub_county+"&&highv="+highv,
        type:"post",
        dataType:"html",
        success:function(output){
          jQuery("#facility").html(output); 
          jQuery('#facility').select2({
              placeholder: "[Optional] Select facilities"
          });
        }
        
               });
           }
        }
        </script>
</body>
</html>
