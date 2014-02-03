<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<div>
	<p>
		<button type="button" class="btn btn-primary btn-add">+ vakantie</button>
	</p>
	<table id="vakantieprojectenTable" class="table table-bordered">
		<thead>
		<tr>
			<th>Type</th>
			<th>Vakantie periode</th>
			<th>Inschrijving periode</th>
			<th>Max. deelnemers</th>
			<th>Min. leeftijd</th>
			<th>Max. leeftijd</th>
			<th></th>
		</tr>
		</thead>
		<tbody id="vakanties">
		</tbody>
	</table>
</div>

<div class="modal modal-add">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<h3>Nieuwe Vakantie</h3>	
			</div>
			<div class="modal-body modal-form-add">
				
			</div>
			<div class="modal-footer result">
	      		<div class="form-group">
	      			<span class="col-sm-5 result-message"></span>
	      			<div class="col-sm-5">
	      				<button type="button" value="Voeg toe" class="btn btn-default btn-close">Sluit</button>
	      				<button type="button" value="Voeg toe" class="btn btn-primary btn-action btn-action-add">Voeg toe</button>
	      			</div>
	      		</div>
      	</div>
		</div>
	</div>
</div>

<div class="modal modal-edit">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<h3>Wijzig vakantie</h3>	
			</div>
			<div class="modal-body modal-form-edit">
				
			</div>
			<div class="modal-footer result">
	      		<div class="form-group">
	      			<span class="col-sm-5 result-message"></span>
	      			<div class="col-sm-5">
	      				<button type="button" value="Voeg toe" class="btn btn-default btn-close">Sluit</button>
	      				<button type="button" value="Voeg toe" class="btn btn-primary btn-action btn-action-edit">Wijzig</button>
	      			</div>
	      		</div>
      	</div>
		</div>
	</div>
</div>

<div class="modal modal-delete">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<h3>Verwijder vakantie</h3>	
			</div>
			<div class="modal-body modal-form-delete">
				
			</div>
			<div class="modal-footer result">
	      		<div class="form-group">
	      			<span class="col-sm-5 result-message"></span>
	      			<div class="col-sm-5">
	      				<button type="button" value="Voeg toe" class="btn btn-default btn-close">Sluit</button>
	      				<button type="button" value="Voeg toe" class="btn btn-primary btn-action btn-action-delete">Verwijder</button>
	      			</div>
	      		</div>
      	</div>
		</div>
	</div>
</div>

<script type="text/javascript">
	laadVakanties();
	// initNieuweVakantieProjectForm();
	// initNieuweVakantieprojectBtn();
</script>