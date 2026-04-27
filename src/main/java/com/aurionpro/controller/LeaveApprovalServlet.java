package com.aurionpro.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import com.aurionpro.model.User;
import com.aurionpro.service.LeaveService;

@WebServlet("/LeaveApprovalServlet")
public class LeaveApprovalServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private LeaveService leaveService;

    @Override
    public void init() throws ServletException {
        leaveService = new LeaveService();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null || !"ADMIN".equalsIgnoreCase(user.getRole())) {
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            int leaveId = Integer.parseInt(request.getParameter("leaveId"));
            String status = request.getParameter("action");

            if (!"APPROVED".equalsIgnoreCase(status) && !"REJECTED".equalsIgnoreCase(status)) {
                session.setAttribute("error", "Invalid action.");
                response.sendRedirect("admin.jsp");
                return;
            }

            leaveService.updateLeaveStatus(leaveId, status.toUpperCase());
            session.setAttribute("flash", "Leave #" + leaveId + " " + status.toUpperCase() + " successfully.");
            response.sendRedirect("admin.jsp");

        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("error", "Failed to update leave status.");
            response.sendRedirect("admin.jsp");
        }
    }
}
