<!DOCTYPE html>

	<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
	<%@ taglib uri = "http://www.springframework.org/tags" prefix = "spring"%>

	<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
	
	<c:set value="${applicationResult.object}" var="application"/>
	<c:set value="${enrollmentResult.object}" var="enrollment"/>
	
	<fmt:bundle basename="pirlewiet-messages">
	
				<div class="modal-dialog">
					<div class="modal-content">
						<div class="modal-header">
			
							<h2>Deelnemer</h2>
						</div>
					<div class="modal-body">
			
						<c:if test="${application.uuid != enrollment.uuid}">
						
							<p class="alert alert-warning">
								Opgelet: voeg hier enkel deelnemers toe van hetzelfde gezin. Voor andere deelnemers moet je een aparte inschrijving maken.
							</p>
						
						</c:if>
				
						<form class="form-horizontal">
						
							<input id="participant-id" type="hidden" value="${enrollment.participant.uuid}"></input>
							<div class="form-group">
								<label for="participant-voor" class="col-sm-4 control-label">Voornaam (*)</label>
								<div class="col-sm-6">	
									<input id="participant-given-name" name="pd-given" type="text" class="form-control" value="${enrollment.participant.givenName}"></input>
								</div>
							</div>
							<div class="form-group">
								<label for="participant-familie" class="col-sm-4 control-label">Familienaam (*)</label>
								<div class="col-sm-6">	
										<input id="participant-family-name" name="pd-family" type="text" class="form-control" value="${enrollment.participant.familyName}"></input>
								</div>
							</div>
							<div class="form-group">
								<label for="participant-gender" class="col-sm-4 control-label">Geslacht (*)</label>
								<div class="col-sm-3">
									<c:choose>
									<c:when test="${enrollment.participant.gender eq 'F'}">
										<div class="checkbox">
											<label>
												<input type="radio" name="participant-gender" class="participant-gender" value="F" checked="checked">&nbsp;Vrouw
											</label>
										</div>
										<div class="checkbox">
											<label>
												<input type="radio" name="participant-gender" class="participant-gender" value="M">&nbsp;Man
											</label>
										</div>
									</c:when>
									<c:when test="${enrollment.participant.gender eq 'M'}">
										<div class="checkbox">
											<label>
												<input type="radio" name="participant-gender" class="participant-gender" value="F" required>&nbsp;Vrouw
											</label>
										</div>
										<div class="checkbox">
											<label>
												<input type="radio" name="participant-gender" class="participant-gender" value="M" checked="checked" required>&nbsp;Man
											</label>
										</div>
									</c:when>
									<c:otherwise>
									<div class="checkbox">
											<label>
												<input type="radio" name="participant-gender" class="participant-gender" value="F" checked="checked" required>&nbsp;Vrouw
											</label>
										</div>
										<div class="checkbox">
											<label>
												<input type="radio" name="participant-gender" class="participant-gender" value="M" required>&nbsp;Man
											</label>
										</div>
									</c:otherwise>
									</c:choose>
								</div>
							</div>
						<div class="form-group">
								<label for="participant-birth-day" class="col-sm-4 control-label">Geboortedatum (*)</label>
								<div class="col-sm-3">
									<input id="participant-birth-day" type="text" class="form-control" value="${enrollment.participant.birthDay}" placeholder="28/08/1977"></input>
								</div>
						</div>
						<div class="form-group">
								<label for="participant-phone" class="col-sm-4 control-label">Telefoonnummer (*)</label>
								<div class="col-sm-4">
									<input id="participant-phone" type="tel" class="form-control" value="${enrollment.participant.phone}"></input>
								</div>
						</div>
						<div class="form-group">
								<label for="participant-email" class="col-sm-4 control-label">E-mail</label>
								<div class="col-sm-6">
									<input id="participant-email" name="pd-email" type="email" class="form-control" value="${enrollment.participant.email}"></input>
								</div>
						</div>
						<div class="form-group">
								<label for="participant-state-number" class="col-sm-4 control-label">Rijksregisternummer</label>
								<div class="col-sm-6">
									<input id="participant-state-number" name="pd-state-number" type="text" class="form-control" value="${enrollment.participant.stateNumber}"></input>
								</div>
						</div>
						<div class="form-group">
								<label for="address-zipcode" class="col-sm-4 control-label">PostCode (*)</label>
								<div class="col-sm-4">
									<input id="address-zipcode" name="pd-zip" type="text" class="form-control" value="${enrollment.address.zipCode}"></input>
								</div>
						</div>
						<div class="form-group">
								<label for="address-city" class="col-sm-4 control-label">Gemeente (*)</label>
								<div class="col-sm-6">
									<input id="address-city" type="tel" class="form-control" value="${enrollment.address.city}"></input>
								</div>
						</div>
						<div class="form-group">
								<label for="address-street" class="col-sm-4 control-label">Straat (*)</label>
								<div class="col-sm-6">
									<input id="address-street" type="tel" class="form-control" value="${enrollment.address.street}"></input>
								</div>
						</div>
						<div class="form-group">
								<label for="address-number" class="col-sm-4 control-label">Huisnummer (*)</label>
								<div class="col-sm-2">
									<input id="address-number" type="tel" class="form-control" value="${enrollment.address.number}"></input>
								</div>
						</div>
						
								<c:forEach items="${qnaResult.object}" var="qna">
									<c:if test="${qna.tag eq 'participant'}">
										<div class="form-group">
											<label class="col-sm-6 control-label">${qna.question} (*)</label>
											<c:choose>
												<c:when test="${qna.type eq 'YesNo'}">
													<div class="col-sm-6">
														<div class="checkbox">
															<label>
																<input type="radio" name="${qna.uuid}" class="q" data-tag="${qna.tag}" value="Y" ${qna.antwoord eq 'Y' ? "checked='checked'" : ""}>&nbsp;Ja
															</label>
															&nbsp;&nbsp;&nbsp;
															<label>
																<input type="radio" name="${qna.uuid}" class="q" data-tag="${qna.tag}" value="N" ${qna.antwoord eq 'N' ? "checked='checked'" : ""}>&nbsp;Neen
															</label>
														</div>
													</div>
												</c:when>
												<c:when test="${qna.type eq 'Text'}">
													<div class="col-sm-3">
														<input id="${qna.uuid}" type="text" class="form-control q" data-tag="${qna.tag}" value="${qna.antwoord}"></input>
													</div>
												</c:when>
												<c:when test="${qna.type eq 'Area'}">
													<div class="col-sm-6">
														<textarea id="${qna.uuid}" class="form-control q" rows="10" cols="64" data-tag="${qna.tag}">${qna.antwoord}</textarea>
													</div>
												</c:when>
											</c:choose>
											</div>
									</c:if>
							</c:forEach>
						<div class="form-group">
							<label for="participant-save" class="col-sm-4 control-label"></label>
							<div class="col-sm-6">
								<button type="button" class="btn btn-default" data-dismiss="modal">Annuleer</button>
								<button type="button" id="participant-save" class="btn btn-primary participant-save" data-attribute-uuid="${enrollment.uuid}"><i class="fa fa-3 fa-save"></i>&nbsp;&nbsp;Sla op</button>
								<span id ="participant-status"></span><br/>
								<span id="error"></span>
							</div>
						</div>
						
						</form>
			
				</div>
			</div>
			</div>


  </fmt:bundle>
