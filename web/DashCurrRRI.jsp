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
    <style>
      .stat-digit{font-weight: 400}
      .stat-text{font-weight: 900;text-decoration: underline;}
      .card-totals{background: #f2eaf9;}
      
      
@-webkit-keyframes spaceboots {
	0% { -webkit-transform: translate(2px, 1px) rotate(0deg); }
	10% { -webkit-transform: translate(-1px, -2px) rotate(-1deg); }
	20% { -webkit-transform: translate(-3px, 0px) rotate(1deg); }
	30% { -webkit-transform: translate(0px, 2px) rotate(0deg); }
	40% { -webkit-transform: translate(1px, -1px) rotate(1deg); }
	50% { -webkit-transform: translate(-1px, 2px) rotate(-1deg); }
	60% { -webkit-transform: translate(-3px, 1px) rotate(0deg); }
	70% { -webkit-transform: translate(2px, 1px) rotate(-1deg); }
	80% { -webkit-transform: translate(-1px, -1px) rotate(1deg); }
	90% { -webkit-transform: translate(2px, 2px) rotate(0deg); }
	100% { -webkit-transform: translate(1px, -2px) rotate(-1deg); }
}

.shake{
    -webkit-animation-name: spaceboots;
	-webkit-animation-duration: 2.2s;
	-webkit-transform-origin:50% 50%;
	-webkit-animation-iteration-count: infinite;
	-webkit-animation-timing-function: linear;
}

    </style>
	</head>
	<body>
     <!-- Left Panel -->
    <%@include file="sidebar.jsp" %>

    <!-- Left Panel -->

    <!-- Right Panel -->

    <div id="right-panel" class="right-panel">

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
                            <div class="col-12 col-md-3"><select id="highv" name="highv" class="form-control-sm " style="min-width: 300px;">
                                    <option value="2" selected>All sites</option>
                                    <option value="1">High-volume sites</option>
                                    <option value="0">Non high-volume sites</option>>
                                </select>
                            </div>
                            <div class="col-12 col-md-3"><select id="county" name="county" class="form-control-sm js-example-basic-multiple" multiple="multiple" style="min-width: 300px;"><option value="">Choose County</option></select></div>

                            <div class="col-12 col-md-3"><select id="sub_county" name="sub_county" class="form-control-sm js-example-basic-multiple" multiple="multiple" style="min-width: 300px;"><option value="">Choose Sub County</option></select></div>

                            <div class="col-12 col-md-3"><select id="facility" name="facility" class="form-control-sm js-example-basic-multiple" multiple="multiple" style="min-width: 300px;"><option value="">Choose Health Facility</option></select></div>
                          </div>
                        </div>
                        </form>
                        
                                        <div class="row">

                <div class="col-auto">
                <div class="card">
                    <div class="card-body card-totals"  style="padding: 10px 10px 10px 10px;">
                        <div class="dib">
                            <div class="stat-text card_reported">Reported Sites &nbsp;&nbsp;&nbsp;<img src="images/btnmore.png" class="shake1" id="view_facil"  data-toggle="modal" data-target="#myModal"></div>
                                <div class="stat-digit card_reported" id="t_0" style="text-align: center;">0</div>
                            </div>
                    </div>
                </div>
            </div>    
             <div class="col-auto">
                <div class="card">
                    <div class="card-body card-totals"   style="padding: 10px 10px 10px 10px;">
                            <div class="stat-content dib">
                                <div class="stat-text">Files Reviewed</div>
                                <div class="stat-digit" id="t_2" style="text-align: center;">0</div>
                            </div>
                    </div>
                </div>
            </div>  
            <div class="col-auto">
                <div class="card">
                    <div class="card-body card-totals"   style="padding: 10px 10px 10px 10px;">
                            <div class="stat-content dib">
                                <div class="stat-text">Active Clients</div>
                                <div class="stat-digit" id="t_3" style="text-align: center;">0</div>
                            </div>
                    </div>
                </div>
            </div>
             <div class="col-auto">
                <div class="card">
                    <div class="card-body card-totals"   style="padding: 10px 10px 10px 10px;">
                            <div class="stat-content dib">
                                <div class="stat-text">Due for VL</div>
                                <div class="stat-digit" id="t_4" style="text-align: center;">0</div>
                            </div>
                    </div>
                </div>
            </div>   
             <div class="col-auto">
                <div class="card">
                    <div class="card-body card-totals"   style="padding: 10px 10px 10px 10px;">
                            <div class="stat-content dib">
                                <div class="stat-text">Defaulters</div>
                                <div class="stat-digit" id="t_5" style="text-align: center;">0</div>
                            </div>
                    </div>
                </div>
            </div>   
            <div class="col-auto">
                <div class="card">
                    <div class="card-body card-totals"  style="padding: 10px 10px 10px 10px;">
                            <div class="stat-content dib">
                                <div class="stat-text">LTFU</div>
                                <div class="stat-digit" id="t_6" style="text-align: center;">0</div>
                            </div>
                    </div>
                </div>
            </div>   
             <div class="col-auto">
                <div class="card">
                    <div class="card-body card-totals"  style="padding: 10px 10px 10px 10px;">
                            <div class="stat-content dib">
                                <div class="stat-text">Transferred Out</div>
                                <div class="stat-digit" id="t_7" style="text-align: center;">0</div>
                            </div>
                    </div>
                </div>
            </div>   

             <div class="col-auto">
                <div class="card">
                    <div class="card-body card-totals"  style="padding: 10px 10px 10px 10px;">
                            <div class="stat-content dib">
                                <div class="stat-text">Dead</div>
                                <div class="stat-digit" id="t_8" style="text-align: center;">0</div>
                            </div>
                    </div>
                </div>
            </div> 
             <div class="col-auto">
                <div class="card">
                    <div class="card-body card-totals"  style="padding: 10px 10px 10px 10px;">
                            <div class="stat-content dib">
                                <div class="stat-text">Stopped</div>
                                <div class="stat-digit" id="t_9" style="text-align: center;">0</div>
                            </div>
                    </div>
                </div>
            </div>
             <div class="col-auto">
                <div class="card">
                    <div class="card-body card-totals"  style="padding: 10px 10px 10px 10px;">
                            <div class="stat-content dib">
                                <div class="stat-text">Pending Verification</div>
                                <div class="stat-digit" id="t_10" style="text-align: center;">0</div>
                            </div>
                    </div>
                </div>
            </div>
             <div class="col-auto">
                <div class="card">
                    <div class="card-body card-totals"  style="padding: 10px 10px 10px 10px;">
                            <div class="stat-content dib">
                                <div class="stat-text">Clients without Status &nbsp;&nbsp;&nbsp;<img src="images/why.png" class="shake1" id="view_facil"  data-toggle="modal" data-target="#modalwhy"></div>
                                <div class="stat-digit" id="t_11" style="text-align: center;">0</div>
                            </div>
                    </div>
                </div>
            </div>
             </div>
              
               

            </div><!-- .animated -->
        </div><!-- .content -->

                        
                        <div id="container" style="min-width: 1410px; min-height: 500px; margin: 0 auto">
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

    
  <div id="modalwhy" class="modal fade" role="dialog">
  <div class="modal-dialog modal-lg" >
    <!-- Modal content-->
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">&times;</button>
      </div>
      <div class="modal-body">
          <h2 style="text-align: center; font-weight: 900; text-decoration: underline;">Missing Patient Status</h2>
        <div class="row">
<div class="col-lg-12">
    This is the number of patients whose status was not selected for the month of July. In some records, for example, if a patient <b>Stopped</b>, <b>T.O</b> or <b>LTFU</b>  in Jan-2018 you get that status for <b>Feb,Mar,Apr,May,Jun and Jul</b> is not selected.<br> 
    <b><u>Below is an example</u></b>
    <br>
</div>

        </div>
        <div class="row">
         <div class="col-lg-12">
           <img src="images/missing_status_example.png">        

          </div>

        </div>
      </div>
        </div>
      </div>
      </div>
    
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

function load_facildata(){
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
    
    var highv = jQuery("#highv").val();
    
    
 jQuery.ajax({
        url:'reported_facilities?county='+county+'&&sub_county='+sub_county+'&&facility='+facility+'&&highv='+highv,
        type:"post",
        dataType:"html",
        success:function(output){ 
        jQuery("#facil_data").html(output);
          }
      });
}

function load_totals(){
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
    
    var highv = jQuery("#highv").val();
    
    
 jQuery.ajax({
        url:'load_curr_totals?county='+county+'&&sub_county='+sub_county+'&&facility='+facility+'&&highv='+highv,
        type:"post",
        dataType:"json",
        success:function(output){ 
         var data = output.data;
          var total_sites=0,total_reported=0,sites_output;
          var itemsno = data.length;
          for(var i=0;i<itemsno;i++){
              if(i==0){
                  total_sites=data[i];
              }
              else if(i==1){
               total_reported= data[i];
               
               sites_output=total_reported+"/"+total_sites+" ("+Math.round(total_reported*100/total_sites)+"%)";
               jQuery("#t_0").html(sites_output);
              }
              else{
               jQuery("#t_"+i).html(data[i]);   
              }
          }
          }
      });
}

function load_data(){
   var img_src='<img style="margin-left: 35%;" src="images/loader.gif" />';
   jQuery("#container").html(img_src);
            load_totals();
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
    
    var highv = jQuery("#highv").val();
    
    
 jQuery.ajax({
        url:'load_cur_art_data?county='+county+'&&sub_county='+sub_county+'&&facility='+facility+'&&highv='+highv,
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
            allowOverlap:true,
            style: {
                fontWeight: 'bold',
                color: (Highcharts.theme && Highcharts.theme.textColor) || 'black'
            },
            formatter: function(){
                var sum = 0;
                var series = this.axis.series;

                for (var i in series){
                  sum+=series[i].yData[this.x];
                }
                if (sum < 0 && this.isNegative || sum >= 0 && !this.isNegative)
                  return sum;
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
        jQuery('#highv').select2();
          
       jQuery('#sub_county').select2({
              placeholder: "[Optional] Select sub counties"
          });
        jQuery('#facility').select2({
              placeholder: "[Optional] Select facilities"
          });
          
          
       load_county();
    
    jQuery("#view_facil").click(function(){
       load_facildata();
    });
    
    
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
    
     jQuery("#highv").change(function(){
        load_county();
        load_subcounty(); 
        load_facility();
        load_data();
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
