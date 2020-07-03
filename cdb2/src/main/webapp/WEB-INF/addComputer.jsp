<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page isELIgnored="false"%>
<!DOCTYPE html>
<html>
<head>
<title>Computer Database</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<!-- Bootstrap -->
<link href=<c:url value="/resources/css/bootstrap.min.css"/> rel="stylesheet" media="screen">
<link href=<c:url value="/resources/css/font-awesome.css"/> rel="stylesheet" media="screen">
<link href=<c:url value="/resources/css/main.css"/> rel="stylesheet" media="screen">
</head>
<body>
  <header class="navbar navbar-inverse navbar-fixed-top">
      <div class="container">
          <a class="navbar-brand" href="${map.get('beforedashboard') }"> Application - Computer Database </a>
      </div>
  </header>

  <section id="main">
    <div class="container">
      <div class="row">
        <div class="col-xs-8 col-xs-offset-2 box">
          <h1>Add Computer</h1>
          <form action="${map.get('add') }" method="POST">
            <fieldset>
              <div class="form-group">
                  <label for="computerName">Computer name</label>
                  <input type="text" class="form-control" id="computerName" placeholder="Computer name" name="name" required>
              </div>
              <div class="form-group">
                  <label for="introduced">Introduced date</label>
                  <input type="date" class="form-control" id="introduced" placeholder="Introduced date" name="introduced" min="1970-01-01" max="2038-01-01">
              </div>
              <div class="form-group">
                  <label for="discontinued">Discontinued date</label>
                  <input type="date" class="form-control" id="discontinued" placeholder="Discontinued date" name="discontinued" min="1970-01-01" max="2038-01-01">
              </div>
              <div class="form-group">
                  <label for="companyId">Company</label>
                  <select class="form-control" id="companyId" name="company">
                      <option value="0">--</option>
                      <c:forEach items="${list}" var="c">
                        <option value="${c.id}">${c.name}</option>
                      </c:forEach>
                  </select>
                  
              </div>                  
            </fieldset>
            <div class="actions pull-right">
                <input type="submit" value="Add" class="btn btn-primary">
                or
                <a href="${map.get('beforedashboard') }" class="btn btn-default">Cancel</a>
            </div>
          </form>
        </div>
      </div>
    </div>
  </section>
  <script src=<c:url value="/resources/js/jquery.min.js"/>></script>
  <script src=<c:url value="/resources/js/bootstrap.min.js"/>></script>
  <script src=<c:url value="/resources/js/dashboard.js"/>></script>
</body>
</html>