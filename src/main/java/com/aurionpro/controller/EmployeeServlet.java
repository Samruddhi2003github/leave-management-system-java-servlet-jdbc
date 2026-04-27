package com.aurionpro.controller;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import com.aurionpro.model.LeaveApplication;
import com.aurionpro.model.User;
import com.aurionpro.service.LeaveService;

@WebServlet("/EmployeeServlet")
public class EmployeeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private LeaveService leaveService;

    @Override
    public void init() throws ServletException {
        leaveService = new LeaveService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null || !"EMPLOYEE".equalsIgnoreCase(user.getRole())) {
            response.sendRedirect("login.jsp");
            return;
        }

       
        List<LeaveApplication> leaves = leaveService.getLeavesByEmployee(user.getEmployeeId());

        // Count summary
        long pendingCount = leaves.stream().filter(l -> "PENDING".equalsIgnoreCase(l.getStatus())).count();
        long approvedCount = leaves.stream().filter(l -> "APPROVED".equalsIgnoreCase(l.getStatus())).count();
        long rejectedCount = leaves.stream().filter(l -> "REJECTED".equalsIgnoreCase(l.getStatus())).count();

        request.setAttribute("leaves", leaves);
        request.setAttribute("pendingCount", pendingCount);
        request.setAttribute("approvedCount", approvedCount);
        request.setAttribute("rejectedCount", rejectedCount);

        request.getRequestDispatcher("employee.jsp").forward(request, response);
    }
}
