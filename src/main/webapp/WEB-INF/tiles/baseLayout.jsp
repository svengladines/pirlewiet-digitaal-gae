<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title><tiles:insertAttribute name="title" ignore="true" /></title>
	
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/bootstrap-3/css/bootstrap.css" />
<!-- 
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/resources/css/templateStyle.css" />
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/resources/css/dialogStyle.css" />
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/resources/css/aanvraagSummary.css" />
<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/resources/css/jquery-ui/jquery-ui-1.8.21.custom.css" />
<style type="text/css" title="currentStyle"></style>
-->
<script type="text/javascript" language="javascript"
	src="<%=request.getContextPath()%>/resources/DataTables/media/js/jquery.js"></script>
<script type="text/javascript" language="javascript"
	src="<%=request.getContextPath()%>/resources/bootstrap/js/bootstrap.js"></script>
<script type="text/javascript" language="javascript"
	src="<%=request.getContextPath()%>/resources/DataTables/media/js/jquery.dataTables.js"></script>
<script type="text/javascript" language="javascript"
	src="<%=request.getContextPath()%>/resources/js/AanvraagInschrijvingsFormulierInitialisatie.js"></script>
<script type="text/javascript" language="javascript"
	src="<%=request.getContextPath()%>/resources/js/SuperUserInitialisatie.js"></script>
<script type="text/javascript" language="javascript"
	src="<%=request.getContextPath()%>/resources/js/screen.js"></script>
<script type="text/javascript" language="javascript"
	src="<%=request.getContextPath()%>/resources/js/AanvraagInschrijvingTabs.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/DialogFormulierInitialisatie.js"></script>	
<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/inschrijvingen.js"></script>	
<!--
<script type="text/javascript" language="javascript"
	src="<%=request.getContextPath()%>/resources/js/jquery.dateFormat-1.0.js"></script>
<script type="text/javascript" language="javascript" src="<%=request.getContextPath()%>/resources/js/jquery-1.7.2.min.js"></script>
-->
<script type="text/javascript" language="javascript" src="<%=request.getContextPath()%>/resources/js/jquery-ui-1.8.21.custom.min.js"></script>
</head>

<body>
	<header class="navbar navbar-inverse navbar-static-top" role="navigation">
		<div class="container">
			<div class="navbar-header">
				<a href="${secretariaatDropDownLink}" class="navbar-brand">Pirlewiet</a>
			</div>
			<tiles:insertAttribute name="menu" />
		</div>
	</header>

	<div id="content" class="container">
		<tiles:insertAttribute name="body" />
	</div>
	
</body>
</html>
