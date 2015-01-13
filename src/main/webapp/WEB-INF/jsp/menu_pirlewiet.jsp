<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
	
    <!-- Fixed navbar -->
    <div class="navbar navbar-inverse navbar-fixed-top">
      <div class="container">
        <div class="navbar-header">
          <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <a class="navbar-brand" href="#">PIRLEWIET DIGITAAL</a>
        </div>
        <div class="navbar-collapse collapse">
          <ul class="nav navbar-nav navbar-right">
            <li><a href="/index.htm">START</a></li>
            <li ${param.active eq 'organisation' ? "class='active'" : ""}><a href="/rs/organisation.html">PIRLEWIET</a></li>
          	<li ${param.active eq 'enrollments' ? "class='active'" : ""}><a href="/rs/inschrijvingen.html">INSCHRIJVINGEN</a>
            </li>
            <li><a href="/help.htm">HELP</a></li>
            <li><a id="logout" title="Uitloggen" href="/rs/logout.html"><i class="fa fa-sign-out"></i>&nbsp;Uitloggen</a></li>
          </ul>
        </div><!--/.nav-collapse -->
      </div>
    </div>