package com.example.leavetracker.repos;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.leavetracker.model.Employee;
import com.example.leavetracker.model.EmployeeLeaveBalance;

@Repository
public interface LeaveRepo extends MongoRepository<EmployeeLeaveBalance,String> {
	
	EmployeeLeaveBalance findByEmpId(int empId);
	
}
