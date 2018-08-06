<%-- 
    Document   : db
    Created on : Apr 13, 2018, 8:46:26 AM
    Author     : GNyabuto
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>Excel Uploads</title>
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
     <link rel="stylesheet" href="assets/css/bootstrap-select.less"> 
    <link rel="stylesheet" href="assets/scss/style.css">

    <link href='https://fonts.googleapis.com/css?family=Open+Sans:400,600,700,800' rel='stylesheet' type='text/css'>
<link href='https://fonts.googleapis.com/css?family=Aclonica' rel='stylesheet'>
<style>
.title {
    font-family: 'Aclonica';font-size: 25px;
}
.login-form{
    padding:  80px 50px 80px 50px;
}
</style>
 
</head>
<body >
		<div class="sufee-login d-flex align-content-center flex-wrap">
                    <div class="container" style="margin-top: 3%; width: 35%;">
                            
				<!--<div class="panel-heading">Log in</div>-->
				<div class="panel-body">
                                        <form action="dbsetup">
						<div class="panel panel-body login-form">
							<div class="text-center header">
                                <div class="title">Test & Start Database Set-Up</div>
                             <br>
							</div>

							<!--<div class="form-group ">-->
                                                        <div class="form-group">
                                                                <label><b>Host Name: </b></label>
                                                                <input id="hostname" type=text required name="hostname" placeholder="e.g localhost:3306" value="localhost:3306" class="form-control" >
                                                            </div>
                                                            
                                                        
                                                          <div class="form-group">
                                                                <label><b>Database: </b></label>
                                                                <input id="database"  type=text required name="database" value="excels"  class="form-control" placeholder="test and start system">
                                                            </div>
                                                        
                                                        
                                                          <div class="form-group">
                                                                <label><b>Username: </b></label>
                                                                <input id="user"  type=text required name="user" class="form-control" value="root" placeholder="e.g root"  >
                                                            </div>
                                                        
                                                        
                                                          <div class="form-group">
                                                                <label><b>Password: </b></label>
                                                                <input id="password"  type=password  name="password" placeholder="Password" class="form-control">
                                                            </div>
                                                        <div class="form-group">
								<button type="submit" class="btn btn-success btn-flat m-b-30 m-t-30"><b>Update Connection</b> <i class="icon-arrow-right14 position-right"></i></button>
							</div>
                                                        </div>
					</form>
				</div>
			</div>
		</div><!-- /.col-->
	
    <script src="assets/js/vendor/jquery-2.1.4.min.js"></script>
    <script src="assets/js/popper.min.js"></script>
    <script src="assets/js/plugins.js"></script>
    <script src="assets/js/main.js"></script>
</body>
</html>
