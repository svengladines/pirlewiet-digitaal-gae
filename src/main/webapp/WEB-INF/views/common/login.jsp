<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE head PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/resources/css/loginPageStyles.css" />
</head>
<body>
<div id="loginForm">
	<form method="POST"
		action="${pageContext.request.contextPath}/j_spring_security_check"
		name="f">
		<table>
			<tbody>
				<tr>
					<td colspan="2" id="loginLogo" >
						<img src="${pageContext.request.contextPath}/resources/images/logo_small.png"/>
					</td>
				</tr>
				<tr>
					<td>Gebruikersnaam:</td>
					<td><input type="text" value="s" name="j_username"
						placeholder="Username" ></td>
				</tr>
				<tr>
					<td>Wachtwoord:</td>
					<td><input type="password" name="j_password"
						placeholder="Password" value="s"></td>
				</tr>
				<tr>
					<td></td>
					<td><input type="submit" value="Login" name="submit" id="submit"></td>
				</tr>
			</tbody>
		</table>
	</form>
	<c:if test="${not empty error}">
		<div class="errorblock">
			<p>Deze gebruikersnaam en/of wachtwoord is ongeldig. Probeer opnieuw, in geval van blijven problemen, contacteer Pirlewiet VZW.</p>
			<!--  Caused : ${sessionScope["SPRING_SECURITY_LAST_EXCEPTION"].message} -->
		</div>
	</c:if>
</div>
</body>
</html>