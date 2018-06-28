<%-- 
    Document   : Dashboard
    Created on : May 4, 2018, 9:57:59 AM
    Author     : GNyabuto
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>Dashboard</title>
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
                        <strong>Dashboard</strong>
                      </div>
                      
                    </div>
                  </div>

             <div class="col-xl-3 col-sm-6">
                <div class="card">
                    <div class="card-body">
                        <div class="stat-widget-one">
                            <div class="stat-icon dib"><i class="ti-support text-primary border-primary"></i></div>
                            <div class="stat-content dib">
                                <div class="stat-text">Reports Submitted</div>
                                <div class="stat-digit" id="sites_assessed">0</div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>   

             <div class="col-xl-3 col-lg-6">
                <div class="card">
                    <div class="card-body">
                        <div class="stat-widget-one">
                            <div class="stat-icon dib"><i class="ti-pie-chart text-primary border-primary"></i></div>
                            <div class="stat-content dib">
                                <div class="stat-text">Total Tested & Started</div>
                                <div class="stat-digit" id="average_phamarcy">0</div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>   
             <div class="col-xl-3 col-lg-6">
                <div class="card">
                    <div class="card-body">
                        <div class="stat-widget-one">
                            <div class="stat-icon dib"><i class="ti-bar-chart text-primary border-primary"></i></div>
                            <div class="stat-content dib">
                                <div class="stat-text">No. not Started</div>
                                <div class="stat-digit" id="average_lab">0</div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>   

             <div class="col-xl-3 col-lg-6">
                <div class="card">
                    <div class="card-body">
                        <div class="stat-widget-one">
                            <div class="stat-icon dib"><i class="ti-stats-up text-primary border-primary"></i></div>
                            <div class="stat-content dib">
                                <div class="stat-text">% Tested & Started</div>
                                <div class="stat-digit" id="average_score">0</div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>   
   
  
                        <div class="col-lg-12">
                            <div class="card">
                                <div class="card-body">
                                    <h4 class="mb-3">Overall Tested Against Started Chart</h4>
                                    <canvas id="barChart" style="height: 200px;"></canvas>
                                </div>
                            </div>
                        </div><!-- /# column --
                        
                </div>


            </div><!-- .animated -->
        </div><!-- .content -->


    </div><!-- /#right-panel -->

    <!-- Right Panel -->


    <script src="assets/js/vendor/jquery-2.1.4.min.js"></script>
    <script src="assets/js/popper.min.js"></script>
    <script src="assets/js/plugins.js"></script>
    <script src="assets/js/main.js"></script>
    
            <!--  Chart js -->
    <script src="assets/js/lib/chart-js/Chart.bundle.js"></script>
 <script>
   jQuery(document).ready(function() {
        load_dashboard();
    });
    
    function load_dashboard(){
            jQuery.ajax({
        url:'load_dashboard',
        type:"post",
        dataType:"json",
        success:function(raw_data){
        var sites_assessed=raw_data.sites_assessed;
        var expected_sites=raw_data.expected_sites;
        
        var average_phamarcy=(Math.round(raw_data.average_phamarcy*100)/100)+"%"; 
        var average_lab=(Math.round(raw_data.average_lab*100)/100)+"%"; 
        var average_score=(Math.round(raw_data.average_score*100)/100)+"%"; 
//        var data=raw_data.data;

          var perc_assessed = (Math.round((sites_assessed*100/expected_sites)*100)/100)+"%"; 
        jQuery("#sites_assessed").html(sites_assessed+" ("+perc_assessed+") ");
        
        jQuery("#average_phamarcy").html(average_phamarcy);
        jQuery("#average_lab").html(average_lab);
        jQuery("#average_score").html(average_score);
        
        
        // individual overall score per facility
        var overall_score=raw_data.overall_score;
        
        // end of score per facility
        
          //bar chart
    var ctx = document.getElementById( "barChart" );
        ctx.height = 150;
        
var opt = {
    legend: {
            display: true,
            position: 'bottom',
            labels: {
            }
        },
    tooltips: {
        enabled: true
    },
    hover: {
        animationDuration: 0
    },
    animation: {
        duration: 1,
        onComplete: function () {
            var chartInstance = this.chart,
                ctx = chartInstance.ctx;
            ctx.font = Chart.helpers.fontString(Chart.defaults.global.defaultFontSize, Chart.defaults.global.defaultFontStyle, Chart.defaults.global.defaultFontFamily);
            ctx.textAlign = 'center';
            ctx.textBaseline = 'bottom';

            this.data.datasets.forEach(function (dataset, i) {
                var meta = chartInstance.controller.getDatasetMeta(i);
                meta.data.forEach(function (bar, index) {
                    var data = dataset.data[index];                            
                    ctx.fillText(data+"%", bar._model.x, bar._model.y - 5);
                });
            });
        }
    },
    scales: {
        yAxes: [{
            display: true,
            ticks: {
                suggestedMin: 0,    
                suggestedMax: 110,
                stepSize: 10
               
            }
        }]
    }
};

    var myChart = new Chart( ctx, {
        type: 'bar',
        data: overall_score,
        options: opt
    });
    }
    });
    }

    </script>

</body>
</html>
