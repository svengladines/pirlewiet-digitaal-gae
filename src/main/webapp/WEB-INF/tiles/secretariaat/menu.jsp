<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<script type="text/javascript">
	$(document).ready(function() {				
		$('#doorverwijzer_search_form').submit(function() {
			var searchterm = $("#searchterm").val();	
			console.log(searchterm);
			
			var dataObject = {"searchterm" : searchterm};	


			console.log(searchterm.length)
			if(searchterm.length == 0){
				//info message "Please enter a search term."
				return false;
			}

				$.ajax({
					type : "POST",
					cache : false,
					url : "searchDoorverwijzer",
					data : dataObject,
					success : function(response) {
						console.log("Succes back from server");
						console.log(response.list);

						generateDienstenTable(response.list);
						$("#filter").html("<a href='#' id='filterStyle'>Delete filter: " + searchterm + "</a>");

					}
				}); 

				return false;
		});



		$("#filter").live("click", function(){
			console.log("delete filter");
			$("#filter").html("");

			$.ajax({
				type : "POST",
				cache : false,
				url : "getAllDiensten",
				success : function(response) {

					generateDienstenTable(response.diensten);

					$("#searchterm").val("");

				}
			});
		});

		
	});
</script>

<nav class="nav navbar-collapse bs-navbar-collapse collapse">

	<ul id="menuList_left" class="nav navbar-nav">
		<li>
			<a href="${pageContext.request.contextPath}/secretariaat/vakanties.html">Vakanties</a>
		</li>
		<li>
			<a href="${pageContext.request.contextPath}/secretariaat/diensten.html">Doorverwijzers</a>
		</li>
		<li>
			<a href="${pageContext.request.contextPath}/secretariaat/deelnemers.html">Deelnemers</a>
		</li>
		</ul>
		
		<sec:authorize access="hasRole('ROLE_SECRETARIAAT') or hasRole('ROLE_SUPERUSER')">
	<c:set var="selected" value="${param.selected}" />
	
		<ul class="nav navbar-nav navbar-right">
		
		<li>
			<a href="<c:url value="/j_spring_security_logout"/>">Log out (<sec:authentication property="principal.username" />)</a>
		</li>
	
		</ul>
	</sec:authorize>
		
</nav>
