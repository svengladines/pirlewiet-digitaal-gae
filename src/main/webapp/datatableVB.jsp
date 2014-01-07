<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
	<head>
		<meta http-equiv="content-type" content="text/html; charset=utf-8" />
		<link rel="shortcut icon" type="image/ico" href="http://www.datatables.net/media/images/favicon.ico" />
		
		<title>CUSTOM example</title>
		<style type="text/css" title="currentStyle">
			@import "resources/DataTables/media/css/demo_page.css";
			@import "resources/DataTables/media/css/demo_table.css";
		</style>
		<script type="text/javascript" language="javascript" src="resources/DataTables/media/js/jquery.js"></script>
		<script type="text/javascript" language="javascript" src="resources/DataTables/media/js/jquery.dataTables.js"></script>
		<script type="text/javascript" charset="utf-8">
			var mijnTabel;
			var geselecteerdeDeelnemers = new Array();
			
			$(document).ready(function() {		
				mijnTabel = $('#example').dataTable( {
						"sAjaxSource": '/PirlewietRegistrations/datatabledatagenerated',
						"sPaginationType": "full_numbers",
						"fnCreatedRow": function( nRow, aData, iDataIndex ) {

								var newcheckbox = document.createElement('td');
								newcheckbox.innerHTML = '<input type="checkbox" />';
								nRow.appendChild(newcheckbox);
								
								var newtd = document.createElement('td');
								newtd.innerHTML = '<a class="edit" href="">Edit</a>';
								nRow.appendChild(newtd);	

								$("input",nRow).live("click", function() {
									$(nRow).toggleClass('row_selected');
								});								
							},
							"aoColumns":[{ "bSearchable": false,"bVisible": false },null,null,null,null],
							"bAutoWidth" : false } );
				
				
						//START THE EDITING
				
											var nEditing = null;
					 
											$('#example a.edit').live('click', function (e) {
												e.preventDefault();
												 
												/* Get the row as a parent of the link that was clicked on */
												var nRow = $(this).parents('tr')[0];
												 
												if ( nEditing !== null && nEditing != nRow ) {
													/* A different row is being edited - the edit should be cancelled and this row edited */
													restoreRow( mijnTabel, nEditing );
													editRow( mijnTabel, nRow );
													nEditing = nRow;
												}
												else if ( nEditing == nRow && this.innerHTML == "Save" ) {
													/* This row is being edited and should be saved */
													saveRow( mijnTabel, nEditing );
													nEditing = null;
												}
												else {
													/* No row currently being edited */
													editRow( mijnTabel, nRow );
													nEditing = nRow;
												}
										} );
										
				$('#new').click( function (e) {
					e.preventDefault();
					var aiNew = mijnTabel.fnAddData( [ '', '', '', '', '', '<a class="edit" href="">Edit</a>', '<a class="delete" href="">Delete</a>' ] );
					var nRow = mijnTabel.fnGetNodes( aiNew[0] );
					editRow( mijnTabel, nRow );
					nEditing = nRow;
				} );
			} );

			function doSomething() {
				var lijstGeslecteerdeDeelnemers = new Array();
				console.log('Dit zijn de geselecteerde IDs -' + fnGetSelected(mijnTabel));
				for (i=0; i<fnGetSelected(mijnTabel).length; i++)
				  {
					console.log(mijnTabel.fnGetData(fnGetSelected(mijnTabel)[i]));
					lijstGeslecteerdeDeelnemers.push(mijnTabel.fnGetData(fnGetSelected(mijnTabel)[i])[0]);					
				  }
				console.log(lijstGeslecteerdeDeelnemers);
				alert(lijstGeslecteerdeDeelnemers);
			}
			
			function fnGetSelected( oTableLocal )
			{
				return oTableLocal.$('tr.row_selected');
			}
			
			
			//CRUD OPERATIONS FOR DATATABLE
			function editRow ( oTable, nRow )
			{
				var aData = oTable.fnGetData(nRow);
				var jqTds = $('>td', nRow);
				jqTds[0].innerHTML = '<input type="text" value="'+aData[1]+'">';
				jqTds[1].innerHTML = '<input type="text" value="'+aData[2]+'">';
				jqTds[2].innerHTML = '<input type="text" value="'+aData[3]+'">';
				jqTds[3].innerHTML = '<input type="text" value="'+aData[4]+'">';				
				jqTds[5].innerHTML = '<a class="edit" href="">Save</a>';
			}
			
			function saveRow ( oTable, nRow )
			{
				var jqInputs = $('input', nRow);
				var jqTds = $('>td', nRow);
				oTable.fnUpdate( jqInputs[0].value, nRow, 1, false );
				oTable.fnUpdate( jqInputs[1].value, nRow, 2, false );
				oTable.fnUpdate( jqInputs[2].value, nRow, 3, false );
				oTable.fnUpdate( jqInputs[3].value, nRow, 4, false );				
				jqTds[5].innerHTML = '<a class="edit" href="">Edit</a>';
				oTable.fnDraw();
				
				var sData = oTable.fnGetData(nRow);
				alert(sData);
			}
			
			function restoreRow ( oTable, nRow )
			{
				var aData = oTable.fnGetData(nRow);
				var jqTds = $('>td', nRow);
				
				for ( var i=0, iLen = jqTds.length-1 ; i<iLen ; i++ ) {
					console.log(aData[i] + ' is the restore value for ' + i);
					oTable.fnUpdate( aData[i], nRow, i, false );
				}
				jqTds[5].innerHTML = '<a class="edit" href="">Edit</a>';		
				
				//oTable.fnDraw();
			}
		</script>
	</head>
	<body id="dt_example">
		<div id="container">
			<div id="dynamic">
<table cellpadding="0" cellspacing="0" border="0" class="display" id="example">
	<thead>
		<tr>
			<th width="20%">Id</th>			
			<th width="20%">Voornaam</th>
			<th width="25%">Achternaam</th>
			<th width="25%">Geboortedatum</th>
			<th width="15%">Geslacht</th>
			<th width="15%">Selecteer</th>
			<th width="15%">Acties</th>
		</tr>
	</thead>
	<tbody>
		
	</tbody>
	<tfoot>
		<tr>
			<th width="20%">Id</th>			
			<th width="20%">Voornaam</th>
			<th width="25%">Achternaam</th>
			<th width="25%">Geboortedatum</th>
			<th width="15%">Geslacht</th>
			<th width="15%">Selecteer</th>
			<th width="15%">Acties</th>
		</tr>
	</tfoot>
</table>
			</div>
			<div class="spacer"></div>
			
	</body>
</html>