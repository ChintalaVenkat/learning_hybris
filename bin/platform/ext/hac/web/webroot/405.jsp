<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page isErrorPage="true" import="java.io.*" %>   
<!DOCTYPE html>
<html>
  <head>
  	<meta charset="utf-8"/>
    	<title>hybris administration console - 404 Not Found</title>
    
		<link rel="stylesheet" href="<c:url value="/static/css/blueprint.css"/>" type="text/css" media="screen, projection">
		<link rel="stylesheet" href="<c:url value="/static/css/plugins/fancy-type/screen.css"/>" type="text/css" media="screen, projection">
		<link rel="stylesheet" href="<c:url value="/static/css/plugins/buttons/screen.css"/>" type="text/css" media="screen, projection">
		<link rel="stylesheet" href="<c:url value="/static/css/custom-theme/jquery-ui-1.8.12.custom.css"/>" type="text/css" media="screen, projection">
		<link rel="stylesheet" href="<c:url value="/static/css/style.css"/>" type="text/css" media="screen, projection">
		<link rel="shortcut icon" href="<c:url value="/static/img/favicon.png"/>"/>
		<link rel="icon" href="<c:url value="/static/img/favicon.png"/>" type="image/x-icon">		
		
		<script type="text/javascript" src="<c:url value="/static/js/modernizr-1.7.min.js"/>"></script>
		<script type="text/javascript" src="<c:url value="/static/js/jquery/jquery-1.6.2.min.js"/>"></script>
		<script type="text/javascript" src="<c:url value="/static/js/jquery-ui-1.8.12.custom.min.js"/>"></script>
		<script type="text/javascript" src="<c:url value="/static/js/global.js"/>"></script>
		<script type="text/javascript" src="<c:url value="/static/js/application.js"/>"></script>
		<script type="text/javascript" src="<c:url value="/static/js/jquery.cookie.js"/>"></script>
		<!--[if IE]>
			<script src="<c:url value="/static/js/IEhtml5.js"/>"></script>
		<![endif]-->	
  </head>
  <body>
		<div class="container">
			<header class="span-24 last">
				<a href="<c:url value="/"/>"><img id="logo" src="<c:url value="/static/img/logo.png"/>" alt="logo"></a>
					 
			</header>
	
			<div id="notification" class="span-24 last">
				<div id="msg" class="msg">This is a beautiful notification message.</div>
			</div>
						
			<!-- begin page content -->
			<div class="prepend-top span-24" id="content">
				<div class="marginLeft marginRight marginBottom">
					<h2>
						405 - Request method 'GET' not supported
					</h2>	
					<p>We&#8217;re sorry, but this method is not supported.  
				</div>
			</div>
  
    		<!-- end page content -->			
		</div>
		
		<footer>
			<a href="#">&copy; hybris AG, 2013</a>
		</footer>  
  </body>
</html>