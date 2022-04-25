<!DOCTYPE html>

	<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
	<%@ taglib uri = "http://www.springframework.org/tags" prefix = "spring"%>

	<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
				<c:set value="${applicationResult.object}" var="application"/>
				<c:set value="${qnaResult.object}" var="qna"/>
				
				<div class="modal-dialog">
					<div class="modal-content">
						<div class="modal-header">
			
							<h2>Vragenlijst</h2>
						</div>
					<div class="modal-body">
						<form class="form-horizontal">
						
						<c:forEach items="${qna}" var="question">
								<c:if test="${question.tag eq 'application'}">
									<div class="form-group">
										<label class="col-sm-6 control-label">${question.question} (*)</label>
										<c:choose>
											<c:when test="${question.type eq 'YesNo'}">
												<div class="col-sm-6">
													<div class="checkbox">
														<label>
															<input type="radio" name="${question.uuid}" class="q" data-tag="${question.tag}" value="Y" ${question.answer eq 'Y' ? "checked='checked'" : ""}>&nbsp;Ja
														</label>
														&nbsp;&nbsp;&nbsp;
														<label>
															<input type="radio" name="${question.uuid}" class="q" data-tag="${question.tag}" value="N" ${question.answer eq 'N' ? "checked='checked'" : ""}>&nbsp;Neen
														</label>
													</div>
												</div>
											</c:when>
											<c:when test="${question.type eq 'Text'}">
												<div class="col-sm-3">
													<input id="${question.uuid}" type="text" class="form-control q" data-tag="${question.tag}" value="${question.answer}"></input>
												</div>
											</c:when>
											<c:when test="${question.type eq 'Area'}">
												<div class="col-sm-6">
													<textarea id="${question.uuid}" class="form-control q" rows="10" cols="64" data-tag="${question.tag}">${question.answer}</textarea>
												</div>
											</c:when>
										</c:choose>
										</div>
								</c:if>
								<c:if test="${question.tag eq 'mobility'}">
									<div class="form-group">
										<label class="col-sm-6 control-label">${question.question} (*)</label>
										<c:choose>
											<c:when test="${question.type eq 'YesNo'}">
												<div class="col-sm-6">
													<div class="checkbox">
														<label>
															<input type="radio" name="${question.uuid}" class="q" data-tag="${question.tag}" value="Y" ${question.answer eq 'Y' ? "checked='checked'" : ""}>&nbsp;Ja
														</label>
														&nbsp;&nbsp;&nbsp;
														<label>
															<input type="radio" name="${question.uuid}" class="q" data-tag="${question.tag}" value="N" ${question.answer eq 'N' ? "checked='checked'" : ""}>&nbsp;Neen
														</label>
													</div>
												</div>
											</c:when>
											<c:when test="${question.type eq 'Text'}">
												<div class="col-sm-3">
													<input id="${question.uuid}" type="text" class="form-control q" data-tag="${question.tag}" value="${question.answer}"></input>
												</div>
											</c:when>
											<c:when test="${question.type eq 'Area'}">
												<div class="col-sm-6">
													<textarea id="${question.uuid}" class="form-control q" rows="10" cols="64" data-tag="${question.tag}">${question.answer}</textarea>
												</div>
											</c:when>
										</c:choose>
										</div>
								</c:if>
						</c:forEach>
							</form>
					</div>
					<div class="modal-footer">
						<div class="form-group">
							<button type="button" class="btn btn-default" data-dismiss="modal">Annuleer</button>
							<button type="button" id="q-save-application" class="btn btn-primary"><i class="fa fa-3 fa-save"></i>&nbsp;&nbsp;Sla op</button>
							<span id ="q-status-application"></span>
						</div>
					</div>
				</div>
			</div>
