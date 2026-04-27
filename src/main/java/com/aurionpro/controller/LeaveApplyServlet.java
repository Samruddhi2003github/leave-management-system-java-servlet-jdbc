package com.aurionpro.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import com.aurionpro.model.LeaveApplication;
import com.aurionpro.model.User;
import com.aurionpro.service.LeaveService;

@WebServlet("/LeaveApplyServlet")
public class LeaveApplyServlet extends HttpServlet {
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

        if (user == null || !"EMPLOYEE".equalsIgnoreCase(user.getRole())) {
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            String fromDateStr = request.getParameter("dateFrom");
            String toDateStr = request.getParameter("dateTo");
            String reason = request.getParameter("reason") != null ? request.getParameter("reason").trim() : "";

         
            if (fromDateStr == null || toDateStr == null || reason.isEmpty()) {
                session.setAttribute("error", "⚠ All fields are required.");
                response.sendRedirect("employee.jsp");
                return;
            }

            LocalDate from = LocalDate.parse(fromDateStr);
            LocalDate to = LocalDate.parse(toDateStr);
            LocalDate today = LocalDate.now();

        
            if (from.isBefore(today) || to.isBefore(today)) {
                session.setAttribute("error", "⚠ Leave dates cannot be in the past.");
                response.sendRedirect("employee.jsp");
                return;
            }

            if (from.isAfter(to)) {
                session.setAttribute("error", "⚠ From date cannot be after To date.");
                response.sendRedirect("employee.jsp");
                return;
            }

            long days = ChronoUnit.DAYS.between(from, to) + 1;
            if (days <= 0) {
                session.setAttribute("error", "⚠ Number of leave days must be at least 1.");
                response.sendRedirect("employee.jsp");
                return;
            }

            if (days > 30) {
                session.setAttribute("error", "⚠ You cannot apply for more than 30 days in a single request.");
                response.sendRedirect("employee.jsp");
                return;
            }

            
            boolean overlaps = leaveService.hasOverlappingLeave(user.getEmployeeId(), from, to);
            if (overlaps) {
                session.setAttribute("error", "⚠ You already have a leave overlapping this period (Pending/Approved).");
                response.sendRedirect("employee.jsp");
                return;
            }

            
            LeaveApplication leave = new LeaveApplication();
            leave.setEmployeeId(user.getEmployeeId());
            leave.setDateFrom(java.sql.Date.valueOf(from));
            leave.setDateTo(java.sql.Date.valueOf(to));
            leave.setReason(reason);
            leave.setNoOfDays((int) days);
            leave.setStatus("PENDING");

            
            leaveService.applyLeave(leave);

            session.setAttribute("flash", "✅ Leave applied successfully for " + days + " day(s).");
            response.sendRedirect("employee.jsp");

        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("error", "⚠ Something went wrong while applying for leave.");
            response.sendRedirect("employee.jsp");
        }
    }
}
