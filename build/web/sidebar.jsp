<%-- 
    Document   : sidebar
    Created on : May 4, 2018, 10:00:00 AM
    Author     : GNyabuto
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>sidebar</title>
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

<link href='https://fonts.googleapis.com/css?family=Aclonica' rel='stylesheet'>
<style>
.navbar-brand {
    font-family: 'Aclonica';font-size: 35px;
}

</style>
</head>
<body>
        <!-- Left Panel -->

    <aside id="left-panel" class="left-panel">
        <nav class="navbar navbar-expand-sm navbar-default">

            <div class="navbar-header">
                <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#main-menu" aria-controls="main-menu" aria-expanded="false" aria-label="Toggle navigation">
                    <i class="fa fa-bars"></i>
                </button>
                <div class="navbar-brand">Excel uploads</div>
            </div>

            <div id="main-menu" class="main-menu collapse navbar-collapse">
                <ul class="nav navbar-nav">
                 
                    <h3 class="menu-title" style="text-align: center; font-size: 130%;">Modules</h3><!-- /.menu-title -->
                    <li>
                     <a href="DashCurrRRI.jsp"> <i class="menu-icon fa fa-dashboard"></i>Dashboard </a>
                    </li>
                   <li class="menu-item-has-children active dropdown">
                        <a href="#" class="dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false"> <i class="menu-icon fa fa-upload"></i>Upload Data</a>
                        <ul class="sub-menu children dropdown-menu">
                            <!--<li><i class="fa fa-upload"></i><a href="ExcelUpload.jsp">Test&Start Data</a></li>-->
                            <li><i class="fa fa-upload"></i><a href="TXCURRUpload.jsp">Upload Current on ART Data</a></li> 
                        </ul>
                    </li>
<!--                    <li class="">
                       <a href="TXCURRUpload.jsp"   aria-haspopup="true" aria-expanded="false"><i class="menu-icon fa fa-upload"></i>Upload Current on ART Data</a>
                    </li>-->
                    
                    <li class="menu-item-has-children active dropdown">
                        <a href="#" class="dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false"> <i class="menu-icon fa fa-table"></i>Reports</a>
                        <ul class="sub-menu children dropdown-menu">
                            <!--<li><i class="fa fa-table"></i><a href="raw_data.jsp">Test&Start Raw Data</a></li>-->
                            <li><i class="fa fa-table"></i><a href="TX_CURR_Report.jsp">Current on ART Data</a></li>
                        </ul>
                    </li>
<!--                    <li class="menu-item-has-children active dropdown">
                        <a href="#" class="dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false"> <i class="menu-icon fa fa-th"></i>Management</a>
                        <ul class="sub-menu children dropdown-menu">
                            <li><i class="menu-icon fa fa-th"></i><a href="#">Manage Users</a></li>
                            <li><i class="menu-icon fa fa-th"></i><a href="#">Manage Uploads</a></li>
                        </ul>
                    </li>-->


                    <li class="">
                        <a href="user_profile.jsp"  aria-haspopup="true" aria-expanded="false"> <i class="menu-icon fa fa-user-circle"></i>User Profile</a>
                    </li>
<!--                    <li class="active">
                        <a href="logout"  aria-haspopup="true" aria-expanded="false"> <i class="menu-icon fa fa-lock"></i>Logout</a>
                    </li>
                    <li>-->
                      
                </ul>
            </div><!-- /.navbar-collapse -->
        </nav>
    </aside><!-- /#left-panel -->

    <!-- Left Panel -->

    <!-- Right Panel -->


</body>
</html>
