<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
    com.aurionpro.model.User user = (com.aurionpro.model.User) session.getAttribute("user");
    if (user == null || !"EMPLOYEE".equalsIgnoreCase(user.getRole())) {
        response.sendRedirect("login.jsp");
        return;
    }

    java.util.List<com.aurionpro.model.LeaveApplication> leaves =
        (java.util.List<com.aurionpro.model.LeaveApplication>) request.getAttribute("leaves");
    if (leaves == null) {
        com.aurionpro.service.LeaveService leaveService = new com.aurionpro.service.LeaveService();
        leaves = leaveService.getLeavesByEmployee(user.getEmployeeId());
        request.setAttribute("leaves", leaves);
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>Employee Dashboard</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
<nav class="navbar navbar-light bg-white shadow-sm">
  <div class="container-fluid">
    <span class="navbar-brand mb-0 h1">Employee Dashboard</span>
    <div class="d-flex align-items-center">
      <span class="me-3">Welcome, ${sessionScope.user.employeeName}</span>
      <form action="LogoutServlet" method="post" class="m-0">
          <button type="submit" class="btn btn-outline-danger btn-sm">Logout</button>
      </form>
    </div>
  </div>
</nav>

<div class="container mt-4">
    <!-- Flash / Error -->
    <c:if test="${not empty sessionScope.flash}">
        <div class="alert alert-success alert-dismissible fade show" role="alert">
            ${sessionScope.flash}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            <c:remove var="flash" scope="session"/>
        </div>
    </c:if>
    <c:if test="${not empty sessionScope.error}">
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
            ${sessionScope.error}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            <c:remove var="error" scope="session"/>
        </div>
    </c:if>

    <div class="row">
        <div class="col-lg-5 mb-4">
            <div class="card shadow-sm p-3">
                <h5 class="card-title">Apply for Leave</h5>
                <form action="LeaveApplyServlet" method="post">
                    <div class="mb-3">
                        <label class="form-label">From Date</label>
                        <input type="date" name="dateFrom" class="form-control" required>
                    </div>
                    <div class="mb-3">
                        <label class="form-label">To Date</label>
                        <input type="date" name="dateTo" class="form-control" required>
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Reason</label>
                        <input type="text" name="reason" class="form-control" placeholder="Enter reason" required>
                    </div>
                    <button type="submit" class="btn btn-primary">Apply Leave</button>
                </form>
            </div>
        </div>

        <div class="col-lg-7">
            <div class="card shadow-sm p-3">
                <h5 class="card-title">Your Leave Applications</h5>
                <table class="table table-bordered table-hover mt-3">
                    <thead class="table-light text-center">
                        <tr>
                            <th>From</th>
                            <th>To</th>
                            <th>No. of Days</th>
                            <th>Reason</th>
                            <th>Status</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="leave" items="${leaves}">
                            <tr class="text-center">
                                <td>${leave.dateFrom}</td>
                                <td>${leave.dateTo}</td>
                                <td>${leave.noOfDays}</td>
                                <td>${leave.reason}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${leave.status eq 'APPROVED'}">
                                            <span class="badge bg-success">${leave.status}</span>
                                        </c:when>
                                        <c:when test="${leave.status eq 'PENDING'}">
                                            <span class="badge bg-warning text-dark">${leave.status}</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge bg-danger">${leave.status}</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty leaves}">
                            <tr><td colspan="5" class="text-center text-muted">No leave applications found.</td></tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
