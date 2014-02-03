<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

	<form:form commandName="vakantie" id="nieuwVakantieProjectForm" action="vakantieprojecten" method="POST" class="form-horizontal form-form" role="form">
		<div class="form-group">
		 	<label for="vakatieDropDown" class="control-label col-sm-5">Type</label>
		 	<div class="col-sm-5">
				<form:select path="vakantietype.id" id="vakatieDropDown" class="form-control">
					<c:forEach items="${vakantieTypes}" var="vakantieType">
						<form:option value="${vakantieType.id}">${vakantieType.displaynaam}</form:option>
					</c:forEach>
				</form:select>
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-sm-5">Datum vertrek</label>
			<div class="col-sm-5">
				<form:input type="date" path="beginDatum" class="form-control" />
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-sm-5">Datum terugkomst</label>
			<div class="col-sm-5">
				<form:input type="date" path="eindDatum" class="form-control" />
				</div>
		</div>
		<div class="form-group">
				<label class="control-label col-sm-5">Datum start inschrijving</label>
				<div class="col-sm-5">
				<form:input type="date" path="beginInschrijving" class="form-control" />
				</div>
		</div>
		<div class="form-group">
				<label class="control-label col-sm-5">Datum einde inschrijving</label>
				<div class="col-sm-5">
				<form:input type="date" path="eindInschrijving" class="form-control datepicker" />
				</div>
		</div>
		<div class="form-group">
				<label class="control-label col-sm-5">Max. aantal deelnemers</label>
				<div class="col-sm-5">
				<form:input type="number" path="maxDeelnemers" class="form-control"/>
				</div>
		</div>
		<div class="form-group">
				<label class="control-label col-sm-5">Min. leeftijd</label>
				<div class="col-sm-5">
				<form:input type="number" path="minLeeftijd" class="form-control" min="1" max="100"/>
				</div>
		</div>
		<div class="form-group">
				<label class="control-label col-sm-5">Max. leeftijd</label>
				<div class="col-sm-5">
				<form:input type="number" path="maxLeeftijd" class="form-control" min="1" max="100"/>
				</div>
		</div>
      	</form:form>
</div>