<%-- 
    Document   : user_profile
    Created on : May 10, 2018, 7:30:55 AM
    Author     : GNyabuto
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>User Profile</title>
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
                        <strong>User Profile Management</strong>
                      </div>
                         <div class="row" style=""> 
                    <div class="col-md-12" style=""> 
                        <form id="" method="post" action="update_user" class="form-horizontal" style="margin-left: 30%;"> 
                           
                              <div class="form-group"> 
                               <div class="col-md-8" style="padding-top: 15px;"> 
                                    <input id="fullname" required name="fullname" type="text" value="" placeholder="Enter fullname" class="form-control" style="width:60%;"> 
                                </div> 
                            </div> 
                            
                            
                              <div class="form-group"> 
                                <div class="col-md-8" style="padding-top: 15px;"> 
                                    <input id="email" required name="email" type="text" value="" placeholder="Enter Email" readonly class="form-control"  style="width:60%;"> 
                                </div> 
                            </div> 
                            
                           
                              <div class="form-group"> 
                                <div class="col-md-8" style="padding-top: 15px;"> 
                                    <input id="phone" required name="phone" type="text" value="" placeholder="Enter Phone" class="form-control"  style="width:60%;"> 
                                </div> 
                            </div> 
                            
                           
                              <div class="form-group"> 
                                <div class="col-md-8" style="padding-top: 15px;"> 
                                    <select id="gender" required name="gender" class="form-control" required  style="width:60%;">
                                    <option value="f">Female</option>
                                    <option value="m">Male</option>
                                    </select> 
                                </div> 
                            </div> 
                            
                            
                              <div class="form-group"> 
                                <div class="col-md-8" style="padding-top: 15px;"> 
                                    <input id="pass1" required name="pass1" type="password" value="" placeholder="Enter Password"  oninput="checkPasswords()" class="form-control"  style="width:60%;"> 
                                </div> 
                            </div> 
                            
                           
                              <div class="form-group"> 
                                <div class="col-md-8" style="padding-top: 15px;"> 
                                    <input id="pass2" required name="pass2" type="password" value="" placeholder="Repeat Password"  oninput="checkPasswords()" class="form-control"  style="width:60%;"> 
                                </div> 
                            </div> 
                            
                            
                              <div class="form-group"> 
                                <div class="col-md-8" style="padding-top: 15px; padding-bottom: 15px;"> 
                                 <input id="" required name="" type="submit" value="Update Profile" placeholder="Repeat Password" class="form-control btn btn-info"  style=" margin-left:20%; width:20%;"> 
                                </div> 
                            </div> 
                            
                         </form> 
                    </div> 
                    </div>
                    </div>
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
    
            function checkPasswords() {
                var password = document.getElementById('pass1');
                var conf_password = document.getElementById('pass2');

                if (password.value != conf_password.value) {
                    conf_password.setCustomValidity('Passwords do not match');
                } else {
                    conf_password.setCustomValidity('');
                }
                
          
        
            }
    
    </script>
	<script>
   $(document).ready(function() {
} ); 
    </script>
    
    <%if(session.getAttribute("message")!=null && session.getAttribute("code")!=null){
    String message = session.getAttribute("message").toString();
    String code =  session.getAttribute("code").toString();
    String theme="";
    if(code.equals("1")){
     theme="bg-success" ;  
    }
    else{
     theme="bg-danger";   
    }
    %>
     <script type="text/javascript">
     $.jGrowl('<%=message%>', {
        position: 'top-center',
        header: 'info',
        theme: '<%=theme%>'
     });
    </script>
    <% 
        session.removeAttribute("message");
        session.removeAttribute("code");}
    %>
    
    <script>
   jQuery(document).ready(function() {
        load_users();
    });
    
    function load_users(){
            jQuery.ajax({
        url:'load_profile',
        type:"post",
        dataType:"json",
        success:function(raw_data){
            var position=0,id,fullname,email,phone,level,level_label,status,status_label,gender,gender_label,output="";
             var dataSet=[];
        var data=raw_data.data;
            position++;
            id=fullname=email=phone=level=level_label=status=status_label=gender=gender_label="";
            if( data.fullname!=null){fullname = data.fullname;}
            if( data.email!=null){email = data.email;}
            if( data.phone!=null){phone = data.phone;}
            if( data.level!=null){level = data.level;}
            if( data.status!=null){status = data.status;}
            if( data.gender!=null){gender = data.gender;}
            
            if(gender=="m"){
               gender_label = '<option value="f">Female</option> '; 
               gender_label += '<option value="m" selected>Male</option> '; 
            }
            else if(gender=="f"){
             gender_label = '<option value="f" selected>Female</option> '; 
             gender_label += '<option value="m">Male</option> '; 
            }
            else{
             gender_label = '<option value="">Select Gender</option> '; 
             gender_label += '<option value="f">Female</option> '; 
             gender_label += '<option value="m">Male</option> ';     
            }
            jQuery("#fullname").val(fullname);
            jQuery("#email").val(email);
            jQuery("#phone").val(phone);
            jQuery("#gender").html(gender_label);

    }
    });
    }

    </script>
</body>
</html>
