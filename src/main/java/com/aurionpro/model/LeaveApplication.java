package com.aurionpro.model;

import java.util.Date;

public class LeaveApplication {
	 private int leaveId;
	    private String employeeId;
	    private Date dateFrom;
	    private Date dateTo;
	    private int noOfDays;
	    private String reason;
	    private String status;
		public int getLeaveId() {
			return leaveId;
		}
		public void setLeaveId(int leaveId) {
			this.leaveId = leaveId;
		}
		public String getEmployeeId() {
			return employeeId;
		}
		public void setEmployeeId(String employeeId) {
			this.employeeId = employeeId;
		}
		public Date getDateFrom() {
			return dateFrom;
		}
		public void setDateFrom(Date dateFrom) {
			this.dateFrom = dateFrom;
		}
		public Date getDateTo() {
			return dateTo;
		}
		public void setDateTo(Date dateTo) {
			this.dateTo = dateTo;
		}
		public int getNoOfDays() {
			return noOfDays;
		}
		public void setNoOfDays(int noOfDays) {
			this.noOfDays = noOfDays;
		}
		public String getReason() {
			return reason;
		}
		public void setReason(String reason) {
			this.reason = reason;
		}
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
		public LeaveApplication() {
			super();
		}
		public LeaveApplication(int leaveId, String employeeId, Date dateFrom, Date dateTo, int noOfDays, String reason,
				String status) {
			super();
			this.leaveId = leaveId;
			this.employeeId = employeeId;
			this.dateFrom = dateFrom;
			this.dateTo = dateTo;
			this.noOfDays = noOfDays;
			this.reason = reason;
			this.status = status;
		}
}
