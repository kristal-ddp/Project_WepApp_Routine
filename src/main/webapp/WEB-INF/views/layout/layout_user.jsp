<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>

  <meta http-equiv="X-UA-Compatible" content="IE=edge" />
  <meta name="viewport" content="width=device-width, initial-scale=1" />

  <title>Routine</title>

  <!-- GOOGLE FONTS -->
  <link href="https://fonts.googleapis.com/css?family=Karla:400,700|Roboto" rel="stylesheet">
  <link href="/plugins/material/css/materialdesignicons.min.css" rel="stylesheet" />
  <link href="/plugins/simplebar/simplebar.css" rel="stylesheet" />

  <!-- PLUGINS CSS STYLE -->
  <link href="/plugins/nprogress/nprogress.css" rel="stylesheet" />
  
  <!-- MONO CSS -->
  <link id="main-css-href" href="/css/style.css" rel="stylesheet"/>

  <!-- LAYOUT USER CSS -->
  <link rel="stylesheet" href="/css/layout_user.css"/>

  <!-- FAVICON -->
  <link href="/images/favicon.png" rel="shortcut icon" />

  <!-- nprogress script -->
  <script src="/plugins/nprogress/nprogress.js"></script>

</head>
<body class="bg-light-gray" id="body">
  <div class="container d-flex align-items-center justify-content-center container_routine">
    <div class="d-flex flex-column justify-content-between">
      <div class="row justify-content-center row_routine">
        <div class="col-lg-6 col-xl-5 col-md-10 col-routine-1">
          <div class="card card-default mb-0 card_routine">
            <tiles:insertAttribute name="body"/>
          </div>
        </div>
      </div>
    </div>
    <tiles:insertAttribute name="navbar"/>
  </div>

  <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"></script>
  <script type="text/javascript" src="/js/bootstrap.bundle.min.js"></script>

</body>
</html>