<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
    com.aurionpro.model.User user = (com.aurionpro.model.User) session.getAttribute("user");
    if (user == null || !"ADMIN".equalsIgnoreCase(user.getRole())) {
        response.sendRedirect("login.jsp");
        return;
    }

    java.util.List<com.aurionpro.model.LeaveApplication> leaves =
        (java.util.List<com.aurionpro.model.LeaveApplication>) request.getAttribute("leaves");
    if (leaves == null) {
        com.aurionpro.service.LeaveService leaveService = new com.aurionpro.service.LeaveService();
        leaves = leaveService.getAllLeaves();
        request.setAttribute("leaves", leaves);
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>Admin Dashboard</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
<nav class="navbar navbar-light bg-white shadow-sm">
  <div class="container-fluid">
    <span class="navbar-brand mb-0 h1">Admin Panel</span>
    <div class="d-flex align-items-center">
      <span class="me-3">${sessionScope.user.username}</span>
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

    <div class="card shadow-sm p-3">
        <h5 class="card-title">All Leave Applications</h5>
        <table class="table table-bordered table-hover mt-3">
            <thead class="table-dark text-center">
                <tr>
                    <th>ID</th>
                    <th>Employee ID</th>
                    <th>From</th>
                    <th>To</th>
                    <th>Days</th>
                    <th>Reason</th>
                    <th>Status</th>
                    <th>Action</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="leave" items="${leaves}">
                    <tr class="text-center">
                        <td>${leave.leaveId}</td>
                        <td>${leave.employeeId}</td>
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
                        <td>
                            <form action="LeaveApprovalServlet" method="post" class="d-inline">
                                <input type="hidden" name="leaveId" value="${leave.leaveId}">
                                <button type="submit" name="action" value="APPROVED" class="btn btn-success btn-sm">Approve</button>
                            </form>
                            <form action="LeaveApprovalServlet" method="post" class="d-inline">
                                <input type="hidden" name="leaveId" value="${leave.leaveId}">
                                <button type="submit" name="action" value="REJECTED" class="btn btn-danger btn-sm">Reject</button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty leaves}">
                    <tr><td colspan="8" class="text-center text-muted">No leave applications found.</td></tr>
                </c:if>
            </tbody>
        </table>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
