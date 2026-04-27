package com.aurionpro.controller;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import com.aurionpro.model.LeaveApplication;
import com.aurionpro.model.User;
import com.aurionpro.service.LeaveService;

@WebServlet("/AdminServlet")
public class AdminServlet extends HttpServlet {
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

        if (user == null || !"ADMIN".equalsIgnoreCase(user.getRole())) {
            response.sendRedirect("login.jsp");
            return;
        }

     
        List<LeaveApplication> allLeaves = leaveService.getAllLeaves();

        request.setAttribute("leaves", allLeaves);

        request.getRequestDispatcher("admin.jsp").forward(request, response);
    }
}
