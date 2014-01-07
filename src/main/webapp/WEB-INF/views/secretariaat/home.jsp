<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<script type="text/javascript">
	$(document).ready(function() {				
		initNieuweDienstBtn();
		initDienstenPage();
		
		initNieuweVakantieprojectBtn();
		initVakentieProjectPage();


	});


</script>
<body>
	<b></b>
	<form name="input"
		action="/secretariaat/rapporten/inschrijvingsaanvragen-klassiek.xls"
		method="get">
		<input type="submit" value="Download" class="button" />
	</form>

<div id="msg" class="msgStyle infoStyle">
		<h3 style='display:inline;color:#E9E9E9'>test</h3>
</div>

<!----------------------- Doorverwijzer ------------------------------------->
<jsp:include page="/WEB-INF/views/screens/doorverwijzer.jsp"></jsp:include>

<!----------------------- VAKANTIE PROJECTEN ------------------------------------->
<jsp:include page="/WEB-INF/views/screens/vakantieprojecten.jsp"></jsp:include>


<!----------------------- Deelnemers ------------------------------------->
<jsp:include page="/WEB-INF/views/screens/deelnemers.jsp"></jsp:include>


<!----------------------- Inschrijvingen ------------------------------------->
<jsp:include page="/WEB-INF/views/screens/inschrijvingen.jsp"></jsp:include>





</body>

</html>


