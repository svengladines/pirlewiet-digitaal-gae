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
						
							<input id="participant-id-${enrollment.uuid}" type="hidden" value="${enrollment.participant.uuid}"></input>
							<div class="form-group">
								<label for="participant-voor-${enrollment.uuid}" class="col-sm-4 control-label">Voornaam (*)</label>
								<div class="col-sm-6">	
									<input id="participant-voor-${enrollment.uuid}" name="pd-given" type="text" class="form-control" value="${enrollment.participant.voorNaam}"></input>
								</div>
							</div>
							<div class="form-group">
								<label for="participant-familie-${enrollment.uuid}" class="col-sm-4 control-label">Familienaam (*)</label>
								<div class="col-sm-6">	
										<input id="participant-familie-${enrollment.uuid}" name="pd-family" type="text" class="form-control" value="${enrollment.participant.familieNaam}"></input>
								</div>
							</div>
							<div class="form-group">
								<label for="participant-geslacht-${enrollment.uuid}" class="col-sm-4 control-label">Geslacht (*)</label>
								<div class="col-sm-3">
									<c:choose>
									<c:when test="${enrollment.participant.gender eq 'F'}">
										<div class="checkbox">
											<label>
												<input type="radio" name="gender" class="participant-geslacht-${enrollment.uuid}" value="F" checked="checked">&nbsp;Vrouw
											</label>
										</div>
										<div class="checkbox">
											<label>
												<input type="radio" name="gender" class="participant-geslacht-${enrollment.uuid}" value="M">&nbsp;Man
											</label>
										</div>
									</c:when>
									<c:when test="${enrollment.participant.geslacht eq 'M'}">
										<div class="checkbox">
											<label>
												<input type="radio" name="gender" class="participant-geslacht-${enrollment.uuid}" value="F">&nbsp;Vrouw
											</label>
										</div>
										<div class="checkbox">
											<label>
												<input type="radio" name="gender" class="participant-geslacht-${enrollment.uuid}" value="M" checked="checked">&nbsp;Man
											</label>
										</div>
									</c:when>
									<c:otherwise>
									<div class="checkbox">
											<label>
												<input type="radio" name="gender" class="participant-geslacht-${enrollment.uuid}" value="F">&nbsp;Vrouw
											</label>
										</div>
										<div class="checkbox">
											<label>
												<input type="radio" name="gender" class="participant-geslacht-${enrollment.uuid}" value="M">&nbsp;Man
											</label>
										</div>
									</c:otherwise>
									</c:choose>
								</div>
							</div>
						<div class="form-group">
								<label for="participant-geboorte-${enrollment.uuid}" class="col-sm-4 control-label">Geboortedatum (*)</label>
								<div class="col-sm-3">
									<fmt:formatDate type="date" pattern="dd/MM/yyyy" value="${enrollment.participant.birthDay}" var="gd"></fmt:formatDate>	
									<input id="participant-geboorte-${enrollment.uuid}" type="text" class="form-control" value="${gd}" placeholder="28/08/1977"></input>
								</div>
						</div>
						<div class="form-group">
								<label for="participant-telefoon-${enrollment.uuid}" class="col-sm-4 control-label">Telefoonnummer (*)</label>
								<div class="col-sm-4">
									<input id="participant-telefoon-${enrollment.uuid}" type="tel" class="form-control" value="${enrollment.participant.phone}"></input>
								</div>
						</div>
						<div class="form-group">
								<label for="participant-email-${enrollment.uuid}" class="col-sm-4 control-label">E-mail</label>
								<div class="col-sm-6">
									<input id="participant-email-${enrollment.uuid}" name="pd-email" type="email" class="form-control" value="${enrollment.participant.email}"></input>
								</div>
						</div>
						<div class="form-group">
								<label for="address-zipcode-${enrollment.uuid}" class="col-sm-4 control-label">PostCode (*)</label>
								<div class="col-sm-4">
									<input id="adres-zipcode-${enrollment.uuid}" name="pd-zip" type="text" class="form-control" value="${enrollment.participant.address.zipCode}"></input>
								</div>
						</div>
						<div class="form-group">
								<label for="adres-gemeente-${enrollment.uuid}" class="col-sm-4 control-label">Gemeente (*)</label>
								<div class="col-sm-6">
									<input id="adres-gemeente-${enrollment.uuid}" type="tel" class="form-control" value="${enrollment.participant.address.gemeente}"></input>
								</div>
						</div>
						<div class="form-group">
								<label for="adres-straat-${enrollment.uuid}" class="col-sm-4 control-label">Straat (*)</label>
								<div class="col-sm-6">
									<input id="adres-straat-${enrollment.uuid}" type="tel" class="form-control" value="${enrollment.participant.address.straat}"></input>
								</div>
						</div>
						<div class="form-group">
								<label for="adres-nummer-${enrollment.uuid}" class="col-sm-4 control-label">Huisnummer (*)</label>
								<div class="col-sm-2">
									<input id="adres-nummer-${enrollment.uuid}" type="tel" class="form-control" value="${enrollment.participant.address.nummer}"></input>
								</div>
						</div>
						
								<c:forEach items="${qnaResult.object}" var="qna">
								<c:if test="${qna.tag eq 'history'}">
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
							<label for="participant-save-${enrollment.uuid}" class="col-sm-4 control-label"></label>
							<div class="col-sm-4">
								<button type="button" class="btn btn-default" data-dismiss="modal">Annuleer</button>
								<button type="button" id="participant-save-${enrollment.uuid}" class="btn btn-primary participant-save" data-uuid="${enrollment.uuid}"><i class="fa fa-3 fa-save"></i>&nbsp;&nbsp;Sla op</button>
								<span id ="participant-status-${enrollment.uuid}"></span>
							</div>
						</div>
						
						</form>
			
				</div>
			</div>
			</div>


  </fmt:bundle>
