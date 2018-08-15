<%-- 
    Document   : TXCURRUpload
    Created on : Aug 1, 2018, 4:09:59 PM
    Author     : GNyabuto
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>TX CURR Excel Upload</title>
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
       <%@include file="header2.jsp" %>
        <!-- Header-->

        <div class="content mt-3">
            <div class="animated fadeIn">


                <div class="row">

                  <div class="col-lg-12">
                    <div class="card">
                      <div class="card-header">
                        <strong>TX CURR Data Upload Module</strong>
                      </div>
                      
                        <form action="UploadTXCURR" method="post" enctype="multipart/form-data" class="form-horizontal">
                         <div class="card-body card-block"> 
                          <div class="row form-group">
                              <div class="col col-md-12"><label for="file-multiple-input" class=" form-control-label" style="font-weight: 900;"><font color="red">NOTE:</font><br> 1. Kindly counter check your files to ensure the correct health facility has been selected. Files without MFL Code will be skipped. <br>2. More than one file can be uploaded at once. Press and hold Control(Ctrl) button as you select files to upload.<br>3. Check the upload response and in case there are  errors, correct them and try re-uploading the affected file(s).</label></div>
                          </div>
                    
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
    
     <%if(session.getAttribute("output")!=null){
      String output=session.getAttribute("output").toString();
      %>
       <script type="text/javascript">
           jQuery(document).ready(function() {
           jQuery("#response").html('<%=output%>');
           
            });
    </script>
       <% 
        session.removeAttribute("output");
        }
    else{
%>
 <script type="text/javascript">
           jQuery(document).ready(function() {
           jQuery("#response").html("");
           
            });
    </script>

<%
}
%>
</body>
</html>
