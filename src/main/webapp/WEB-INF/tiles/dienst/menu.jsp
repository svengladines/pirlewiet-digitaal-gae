<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="sec"%>

<header class="navbar navbar-inverse navbar-static-top" role="navigation">
	<div class="container">
		<div class="navbar-header">
			<a href="${secretariaatDropDownLink}" class="navbar-brand">Pirlewiet</a>
		</div>
		<ul class="nav navbar-nav">
		 	<li><a href="${pageContext.request.contextPath}/dienst/home">DOORVERWIJZERS</a></li>
			<li>
			<sec:authorize
				access="hasRole('ROLE_SECRETARIAAT') or hasRole('ROLE_DIENST') or hasRole('ROLE_SUPERUSER')">
				<c:set var="selected" value="${param.selected}" />
				<a class="navbar-right" href="<c:url value="/j_spring_security_logout" />">Log out</a>
			</sec:authorize>
			</li>
		</ul>
	</div>
</header>

 