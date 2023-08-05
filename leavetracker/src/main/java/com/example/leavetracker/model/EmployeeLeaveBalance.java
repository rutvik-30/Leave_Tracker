package com.example.leavetracker.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="Leave_Balance")
public class EmployeeLeaveBalance {
	
	@Id
	private String id;
	
	@Indexed(name = "empId_unique",unique = true)
	private int empId;
	private int CasualleaveBalance;
	private int sickLeaveBalance;
	private int flexiLeaveBalance;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getEmpId() {
		return empId;
	}
	public void setEmpId(int empId) {
		this.empId = empId;
	}
	public int getCasualleaveBalance() {
		return CasualleaveBalance;
	}
	public void setCasualleaveBalance(int casualleaveBalance) {
		CasualleaveBalance = casualleaveBalance;
	}
	public int getSickLeaveBalance() {
		return sickLeaveBalance;
	}
	public void setSickLeaveBalance(int sickLeaveBalance) {
		this.sickLeaveBalance = sickLeaveBalance;
	}
	public int getFlexiLeaveBalance() {
		return flexiLeaveBalance;
	}
	public void setFlexiLeaveBalance(int flexiLeaveBalance) {
		this.flexiLeaveBalance = flexiLeaveBalance;
	}
	
	
	
	

}
