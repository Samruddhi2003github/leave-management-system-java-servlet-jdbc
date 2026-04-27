package com.aurionpro.service;

import java.time.LocalDate;
import java.util.List;

import com.aurionpro.dao.LeaveDao;
import com.aurionpro.model.LeaveApplication;

public class LeaveService {
    private LeaveDao leaveDao;

    public LeaveService() {
        this.leaveDao = new LeaveDao();
    }

    public void applyLeave(LeaveApplication leave) {
        // (No business validation here now; validation is done in servlet)
        // Ensure no_of_days is already set by the servlet.
        leaveDao.applyLeave(leave);
    }

    public boolean hasOverlappingLeave(String employeeId, LocalDate from, LocalDate to) {
        return leaveDao.hasOverlappingLeave(employeeId,
                java.sql.Date.valueOf(from), java.sql.Date.valueOf(to));
    }

    public List<LeaveApplication> getLeavesByEmployee(String employeeId) {
        return leaveDao.getLeavesByEmployee(employeeId);
    }

    public List<LeaveApplication> getAllLeaves() {
        return leaveDao.getAllLeaves();
    }

    public void updateLeaveStatus(int leaveId, String status) {
        leaveDao.updateLeaveStatus(leaveId, status);
    }
}
