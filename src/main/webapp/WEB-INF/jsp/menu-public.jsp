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
            <li ${param.active eq 'start' ? "class='active'" : ""}><a href="/index.htm">START</a></li>
            <li ${param.active eq 'organisations' ? "class='active'" : ""}><a href="/index.htm">ORGANISATIES</a></li>
            <li ${param.active eq 'help' ? "class='active'" : ""}><a href="/help.html">HELP</a></li>
          </ul>
        </div><!--/.nav-collapse -->
      </div>
    </div>