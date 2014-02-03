<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="sec"%>


 <ul class="nav navbar-nav">
	<li class="navbar-right">
	<sec:authorize
		access="hasRole('ROLE_SECRETARIAAT') or hasRole('ROLE_DIENST') or hasRole('ROLE_SUPERUSER')">
		<c:set var="selected" value="${param.selected}" />
		<a class="navbar-right" href="<c:url value="/j_spring_security_logout" />">Log out</a>
	</sec:authorize>
	</li>
</ul>