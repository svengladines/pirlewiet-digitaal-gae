<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="sec"%>

<ul id="menuList_left">
	<sec:authorize access="isAuthenticated()">
		<li><span class="menuItem"> Welkom,<sec:authentication
					property="principal.username" /></span></li>
	</sec:authorize>
</ul>

<ul id="menuList">
	<sec:authorize
		access="hasRole('ROLE_SECRETARIAAT') or hasRole('ROLE_SUPERUSER')">
		<c:set var="selected" value="${param.selected}" />
		<li><span class="menuItem"> <a
				href="<c:url value="/j_spring_security_logout"/>">Log out</a>
		</span></li>

		<li><span class="menuItem"
			<c:if test="${selected eq 'home'}">id="selectedMenuItem"</c:if>>
				<a href="<c:url value="/superuser/home"/>">Home</a>
		</span></li>
	</sec:authorize>
</ul>