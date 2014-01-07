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


<ul id="menuList_left">

	<li>
		<spring:url var="secretariaatDropDownLink" value="/secretariaat/home" />
		<form:form method="POST" commandName="screenDropDownObject" action="${secretariaatDropDownLink}" id="selectScreenForm">
			<form:select path="screenName" onchange="this.form.submit()">
				<form:options items="${screenDropDownObject.screens}" />
			</form:select>
		</form:form>
	</li>
	
	<c:if test='${screen == "Doorverwijzer"}'>
		<li>
			<form:form commandName="searchObject" id="doorverwijzer_search_form" method="POST">
				<form:input path="searchterm" id="searchterm" /><input type="submit" id="searchButton" value=" " />
			</form:form>
		</li>

		<li>
			<button type="button" id="nieuweDienstBtn" class="button">Nieuw</button>
		</li>
	</c:if>
	
	<c:if test='${screen == "Vakantieprojecten"}'>
		<li>
			<button type="button" id="nieuwVakantieProjectBtn" class="button">Nieuw</button>
		</li>
	</c:if>
</ul>

<ul id="menuList">
	<sec:authorize access="hasRole('ROLE_SECRETARIAAT') or hasRole('ROLE_SUPERUSER')">
		<c:set var="selected" value="${param.selected}" />
		
		<li>
			<span class="menuItem">
				<a href="<c:url value="/j_spring_security_logout"/>">Log out (<sec:authentication property="principal.username" />)</a>
			</span>
		</li>

		<li>
			<span class="menuItem" <c:if test="${selected eq 'home'}">id="selectedMenuItem"</c:if>> 
				<a href="<c:url value="/secretariaat/home"/>">Home</a>
			</span>
		</li>
	</sec:authorize>
</ul>
