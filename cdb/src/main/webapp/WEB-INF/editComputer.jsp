<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page isELIgnored="false"%>
<!DOCTYPE html>
<html>
<head>
<title>Computer Database</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<!-- Bootstrap -->
<link href="./css/bootstrap.min.css" rel="stylesheet" media="screen">
<link href="./css/font-awesome.css" rel="stylesheet" media="screen">
<link href="./css/main.css" rel="stylesheet" media="screen">
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
          <h1>Edit Computer</h1>
          <form action="${map.get('edit') }" method="POST">
            <fieldset>
            <input type="hidden" name="id" value="${computer.id}">
              <div class="form-group">
                  <label for="computerName">Computer name</label>
                  <input type="text" class="form-control" id="computerName" placeholder="Computer name" name="name" value="${computer.name }" required>
              </div>
              <div class="form-group">
                  <label for="introduced">Introduced date</label>
                  <input type="date" class="form-control" id="introduced" placeholder="Introduced date" name="introduced" value="${computer.introduced}" min="1970-01-01" max="3000-01-01">
              </div>
              <div class="form-group">
                  <label for="discontinued">Discontinued date</label>
                  <input type="date" class="form-control" id="discontinued" placeholder="Discontinued date" name="discontinued" value="${computer.discontinued}" min="1970-01-01" max="3000-01-01">
              </div>
              <div class="form-group">
                  <label for="companyId">Company</label>
                  <select class="form-control" id="companyId" name="company">
                      <option value="0">--</option>
                      <c:forEach items="${list}" var="c">
                        <c:choose>
                          <c:when test="${c.id == computer.company.id }">
                             <option value="${c.id}" selected>${c.name}</option>
                          </c:when>
                          <c:otherwise>
                            <option value="${c.id}">${c.name}</option>
                          </c:otherwise>
                        </c:choose>
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
  <script src="./js/jquery.min.js"></script>
  <script src="./js/bootstrap.min.js"></script>
  <script src="./js/validation.js"></script>
</body>
</html>