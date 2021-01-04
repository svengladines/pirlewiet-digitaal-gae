<!DOCTYPE html>

	<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
	<%@ taglib uri = "http://www.springframework.org/tags" prefix = "spring"%>

	<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
				<c:set value="${applicationResult.object}" var="application"/>
				<c:set value="${contactResult.object}" var="contact"/>
				
				<div class="modal-dialog">
					<div class="modal-content">
						<div class="modal-header">	
							<h2>Contactgegevens doorverwijzer</h2>
							
							<p>
								<strong>Opgelet!</strong> Zorg ervoor dat de persoon die je aanmeldt bereikbaar is tijdens de vakantie!
							</p>
						</div>
						<div class="modal-body">
							<form class="form-horizontal">
								<input type="hidden" id="contact-uuid" value="${contact.uuid}">
								<div class="form-group">
									<label for="contact-naam" class="col-sm-4 control-label">Voornaam (*)</label>
										<div class="col-sm-6">	
											<input id="contact-given-name" type="text" class="form-control" value="${contact.givenName}"></input>
										</div>
								</div>
								<div class="form-group">
									<label for="contact-naam" class="col-sm-4 control-label">Naam (*)</label>
										<div class="col-sm-6">	
											<input id="contact-family-name" type="text" class="form-control" value="${contact.familyName}"></input>
										</div>
								</div>
								<div class="form-group">
									<label for="contact-naam" class="col-sm-4 control-label">Telefoon (*)</label>
										<div class="col-sm-4">	
											<input id="contact-phone" type="text" class="form-control" value="${contact.phone}"></input>
										</div>
								</div>
								<div class="form-group">
									<label for="contact-naam" class="col-sm-4 control-label">E-mail (*)</label>
										<div class="col-sm-6">	
											<input id="contact-email" type="email" class="form-control" value="${contact.email}"></input>
										</div>
								</div>
							</form>
						</div>
						<div class="modal-footer">
							<div class="form-group">
								<button type="button" class="btn btn-default" data-dismiss="modal">Annuleer</button>
								<button type="button" id="contact-save" class="btn btn-primary"><i class="fa fa-3 fa-save"></i>&nbsp;&nbsp;Sla op</button>
								<span id ="contact-status"></span>
							</div>
						</div>
					</div>
				</div>
