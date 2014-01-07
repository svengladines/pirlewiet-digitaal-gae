<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/resources/css/templateStyle.css" />
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/resources/css/dialogStyle.css" />
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/resources/css/aanvraagSummary.css" />
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title><tiles:insertAttribute name="title" ignore="true" /></title>
<style type="text/css" title="currentStyle">
@import
	"${pageContext.request.contextPath}/resources/DataTables/media/css/demo_page.css"
	;

@import
	"${pageContext.request.contextPath}/resources/DataTables/media/css/demo_table.css"
	;
</style>
<script type="text/javascript" language="javascript"
	src="<%=request.getContextPath()%>/resources/DataTables/media/js/jquery.js"></script>
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
<script type="text/javascript" language="javascript"
	src="<%=request.getContextPath()%>/resources/js/DialogFormulierInitialisatie.js"></script>
<script type="text/javascript" language="javascript"
	src="<%=request.getContextPath()%>/resources/js/jquery.dateFormat-1.0.js"></script>
<!-- <script type="text/javascript" language="javascript" src="<%=request.getContextPath()%>/resources/js/jquery-1.7.2.min.js"></script>-->
<script type="text/javascript" language="javascript"
	src="<%=request.getContextPath()%>/resources/js/jquery-ui-1.8.21.custom.min.js"></script>
<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/resources/css/jquery-ui/jquery-ui-1.8.21.custom.css" />
</head>
<body>
	<div id="menu">
		<tiles:insertAttribute name="menu" />
	</div>

	<div id="content">
		<tiles:insertAttribute name="body" />
	</div>
</body>
</html>
