<%-- 
    Document   : DashCurrRRI
    Created on : Aug 14, 2018, 3:20:36 PM
    Author     : GNyabuto
--%>
<!DOCTYPE HTML>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<title>Reported VS Recounted</title>
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
                      
<form action="#" method="post" class="form-horizontal">
                         <div class="card-body card-block"> 
                             <div class="row form-group">
                          </div>
                             
                          <div class="row form-group">
                            <div class="col col-md-1"></div>
                            <div class="col-12 col-md-2"><select id="county" name="county" class="form-control-sm js-example-basic-multiple" multiple="multiple" style="min-width: 300px;"><option value="">Choose County</option></select></div>

                            <div class="col col-md-1"></div>
                            <div class="col-12 col-md-2"><select id="sub_county" name="sub_county" class="form-control-sm js-example-basic-multiple" multiple="multiple" style="min-width: 300px;"><option value="">Choose Sub County</option></select></div>

                            <div class="col col-md-1"></div>
                            <div class="col-12 col-md-2"><select id="facility" name="facility" class="form-control-sm js-example-basic-multiple" multiple="multiple" style="min-width: 300px;"><option value="">Choose Health Facility</option></select></div>
                          </div>
                      </div>
                        </form>

                        <div id="container" style="min-width: 1410px; min-height: 550px; margin: 0 auto">
                            <img style="margin-left: 35%;" src="images/loader.gif" />
                        </div>
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
<script src="highcharts/code/highcharts.js"></script>
<script src="highcharts/code/modules/exporting.js"></script>
<script src="highcharts/code/modules/export-data.js"></script>
<script src="assets/js/select2/js/select2.min.js"></script>

		<script type="text/javascript">
var data;
//load data from backend



function load_data(){
   var img_src='<img style="margin-left: 35%;" src="images/loader.gif" />';
   jQuery("#container").html(img_src);
    var county = jQuery("#county").val();
            if(county!=null){
            county = county.toString();
             county = county.replace(/,/g,"_");
         }
         else{
             county="";
         }
    
     var sub_county = jQuery("#sub_county").val();
            if(sub_county!=null){
            sub_county = sub_county.toString();
             sub_county = sub_county.replace(/,/g,"_");
         }
         else{
             sub_county="";
         }   
    
     var facility = jQuery("#facility").val();
            if(facility!=null){
            facility = facility.toString();
             facility = facility.replace(/,/g,"_");
         }
         else{
             facility="";
         }   
    
    
    
    
 jQuery.ajax({
        url:'load_cur_art_data?county='+county+'&&sub_county='+sub_county+'&&facility='+facility,
        type:"post",
        dataType:"json",
        success:function(output){ 
          data = output;


Highcharts.chart('container', {
    chart: {
        type: 'column'
    },
    title: {
        text: 'Current on ART RRI Variances, Reported vs Recounted'
    },
    xAxis: {
        categories: ['Oct 2017', 'Nov 2017', 'Dec 2017', 'Jan 2018', 'Feb 2018', 'Mar 2018', 'Apr 2018', 'May 2018', 'Jun 2018', 'Jul 2018']
    },
    yAxis: {
//        min: 0,
        title: {
            text: 'Total Current on ART'
        },
        stackLabels: {
            enabled: true,
            style: {
                fontWeight: 'bold',
                color: (Highcharts.theme && Highcharts.theme.textColor) || 'black'
            }
        }
    },
    legend: {
        align: 'right',
        x: -30,
        verticalAlign: 'top',
        y: 25,
        floating: true,
        backgroundColor: (Highcharts.theme && Highcharts.theme.background2) || 'white',
        borderColor: '#CCC',
        borderWidth: 1,
        shadow: false
    },
    tooltip: {
        headerFormat: '<b>{point.x}</b><br/>',
        pointFormat: '{series.name}: {point.y}<br/>Total: {point.stackTotal}'
    },
    plotOptions: {
        column: {
            stacking: 'normal',
            dataLabels: {
                enabled: true,
                fontWeight: 'bold',
                color: 'black',
                backgroundColor:'yellow',
                shadow:false,
                padding:1
            }
        }
    },
    series: data
});
}      
});
 }
Highcharts.setOptions({ colors: ['red', 'green','yellow'] });
</script>
                
    
    <script type="text/javascript">
         jQuery(document).ready(function() { 
             load_data();
             
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
       load_data();
    });
    
    jQuery("#sub_county").change(function(){
       load_facility(); 
       load_data();
    });
    jQuery("#facility").change(function(){
       load_data();
    });
    
    });  
        
        function load_county(){
       jQuery("#county").val(null);
               jQuery.ajax({
        url:'load_counties',
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
            var county = jQuery("#county").val();
            if(county!=null){
            county = county.toString();
             county = county.replace(/,/g,"_");
               jQuery.ajax({
        url:'load_sub_counties?county='+county,
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
            var sub_county = jQuery("#sub_county").val();
            if(sub_county!=null){
             sub_county = sub_county.toString();
             sub_county = sub_county.replace(/,/g,"_");
               jQuery.ajax({
        url:'load_facilities?sub_county='+sub_county,
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
