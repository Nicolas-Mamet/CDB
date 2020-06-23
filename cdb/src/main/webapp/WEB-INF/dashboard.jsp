<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page isELIgnored="false"%>
<!DOCTYPE html>

<html>
<head>
<title>Computer Database</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta charset="utf-8">
<!-- Bootstrap -->
<link href="./css/bootstrap.min.css" rel="stylesheet" media="screen">
<link href="./css/font-awesome.css" rel="stylesheet" media="screen">
<link href="./css/main.css" rel="stylesheet" media="screen">
</head>
<body>
  <header class="navbar navbar-inverse navbar-fixed-top">
    <div class="container">
      <a class="navbar-brand" href="${map.get('beforedashboard') }"> Application -
        Computer Database </a>
    </div>
  </header>

  <section id="main">
    <div class="container">
      <h1 id="homeTitle">${pagemanager.nbItem} Computers found</h1>
      <div id="actions" class="form-horizontal">
        <div class="pull-left">
          <form id="searchForm" action="#" method="GET"
            class="form-inline">
            <input type="hidden" name="limit" value="${pagemanager.limit}"/>
            <input type="search" id="searchbox" name="search"
              class="form-control" placeholder="Search name" /> <input
              type="submit" id="searchsubmit" value="Filter by name"
              class="btn btn-primary" />
          </form>
        </div>
        <div class="pull-right">
          <a class="btn btn-success" id="addComputer"
            href="${map.get('beforeadd') }">Add Computer</a> <a
            class="btn btn-default" id="editComputer" href="#"
            onclick="$.fn.toggleEditMode();">Edit</a>
        </div>
      </div>
    </div>

    <form id="deleteForm" action="${map.get('delete') }" method="GET">
      <input type="hidden" name="selection" value="">
    </form>

    <div class="container" style="margin-top: 10px;">
      <table class="table table-striped table-bordered">
        <thead>
          <tr>
            <!-- Variable declarations for passing labels as parameters -->
            <!-- Table header for Computer Name -->

            <th class="editMode" style="width: 60px; height: 22px;">
              <input type="checkbox" id="selectall" /> <span
              style="vertical-align: top;"> - <a href="#"
                id="deleteSelected" onclick="$.fn.deleteSelected();">
                  <i class="fa fa-trash-o fa-lg"></i>
              </a>
            </span>
            </th>
            <th>Computer name</th>
            <th>Introduced date</th>
            <!-- Table header for Discontinued Date -->
            <th>Discontinued date</th>
            <!-- Table header for Company -->
            <th>Company</th>

          </tr>
        </thead>
        <!-- Browse attribute computers -->

        <tbody id="results">
          <c:forEach items="${list}" var="u">
            <tr>
              <td class="editMode"><input type="checkbox" name="cb"
                class="cb" value="${u.id}" form="deleteForm"></td>
              <td><a href="${map.get('beforeedit') }?id=${u.id}" onclick="">${u.name}</a>
              </td>
              <td><c:out value="${u.introduced}"></c:out></td>
              <td><c:out value="${u.discontinued}"></c:out></td>
              <td><c:out value="${u.company.name}"></c:out></td>
            </tr>
          </c:forEach>
        </tbody>
      </table>
    </div>
  </section>

  <footer class="navbar-fixed-bottom">
    <div class="container text-center">
        <ul class="pagination">
          <li>
            <a href="?offset=0&limit=${pagemanager.limit}&search=${search }">1</a>
          <li>
            <a href="?offset=${pagemanager.previousOffset()}&limit=${pagemanager.limit}&search=${search }" aria-label="Previous">
              <span aria-hidden="true">&laquo;</span>
            </a>
          </li>
          <li>
            <a href="?offset=${pagemanager.nextOffset()}&limit=${pagemanager.limit}&search=${search }" aria-label="Next">
              <span aria-hidden="true">&raquo;</span>
            </a>
          </li>
          <li>
            <a href="?offset=${pagemanager.lastPageOffset()}&limit=${pagemanager.limit}&search=${search }">${pagemanager.nbPages()}</a>
          <li>
        </ul>
      <div class="btn-group btn-group-sm pull-right" role="group">
        <a href="?offset=${pagemanager.offset}&limit=10&search=${search }"><button type="button" class="btn btn-default">10</button></a>
        <a href="?offset=${pagemanager.offset}&limit=50&search=${search }"><button type="button" class="btn btn-default">50</button></a>
        <a href="?offset=${pagemanager.offset}&limit=100&search=${search }"><button type="button" class="btn btn-default">100</button></a>
      </div>
    </div>
  </footer>
  <script src="./js/jquery.min.js"></script>
  <script src="./js/bootstrap.min.js"></script>
  <script src="./js/dashboard.js"></script>

</body>
</html>