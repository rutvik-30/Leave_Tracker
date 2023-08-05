package com.example.leavetracker.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.example.leavetracker.dao.LeaveDao;
import com.example.leavetracker.model.EmployeeLeaveBalance;
import com.example.leavetracker.repos.LeaveRepo;

@Service
public class LeaveService {
	
	@Autowired
	LeaveRepo leaveRepo;
	
	@Autowired
	MongoTemplate mongoTemplate;
	
	@Autowired
	LeaveDao leaveDao;

	public EmployeeLeaveBalance addLeave(EmployeeLeaveBalance employeeLeaveBalance) {
		EmployeeLeaveBalance leaveAdded=leaveRepo.save(employeeLeaveBalance);
		return leaveAdded;
	}

	public String update(int empId, String leaveType, int leaveBalance) {
		long count=leaveDao.update(empId,leaveType,leaveBalance);
		String res="OK";
		if(count>=1) {
			return res;
		}
		return null;

	}
	
	
}
