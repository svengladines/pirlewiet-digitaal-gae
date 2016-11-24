<!DOCTYPE html>

	<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
	<%@ taglib uri = "http://www.springframework.org/tags" prefix = "spring"%>

	<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
	
	<c:set value="${applicationResult.object}" var="application"/>
	
	<fmt:bundle basename="pirlewiet-messages">
	
				<div class="modal-dialog">
					<div class="modal-content">
						<div class="modal-header">			
							<h2>Vakantie(s)</h2>
							<p>
								Selecteer de vakantie(s) waarvoor je wil inschrijven.
							</p>
						</div>
						<div class="modal-body">
							<c:set value="${holidaysResult.object}" var="currentHolidays"/>
							<c:if test="${empty currentHolidays}">
								<p class="text=info">Er is momenteel geen vakantie waar men voor kan inschrijven</p>
							</c:if>
							<c:forEach items="${currentHolidays}" var="h">
								<fmt:formatDate type="date" pattern="dd/MM/yyyy" value="${h.start}" var="start"></fmt:formatDate>	
								<fmt:formatDate type="date" pattern="dd/MM/yyyy" value="${h.end}" var="end"></fmt:formatDate>
								<c:set var="contains" value="false" />
								<c:forEach items="${application.holidays}" var="holiday">	
									<c:choose>
										<c:when test="${holiday.uuid eq h.uuid}">
											<c:set var="contains" value="true" />
										</c:when>
									</c:choose>
									</c:forEach>
									<div class="checkbox">
										<label>
											<input type="checkbox" name="vak" class="holiday" value="${h.uuid}" ${contains == true ? "checked='checked'" : ""}>&nbsp;${h.name}&nbsp;&nbsp;&nbsp;(${start} t.e.m. ${end})
										</label>
									</div>
							</c:forEach>
						</div>
						<div class="modal-footer">
							<button type="button" class="btn btn-default" data-dismiss="modal">Annuleer</button>
							<button type="button" id="holiday-save" class="btn btn-primary"><i class="fa fa-3 fa-save"></i>&nbsp;&nbsp;Sla op</button>
							<span id ="holiday-status"></span>
						</div>
					</div>
				</div>
				
  </fmt:bundle>
