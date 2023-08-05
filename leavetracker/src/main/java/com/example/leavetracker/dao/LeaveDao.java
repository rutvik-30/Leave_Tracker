package com.example.leavetracker.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.example.leavetracker.model.EmployeeLeaveBalance;
import com.mongodb.client.result.UpdateResult;

@Component
public class LeaveDao {
	
	@Autowired
	MongoTemplate mongoTemplate;

	public long update(int empId, String leaveType, int leaveBalance) {
		
		Query query1=new Query(Criteria.where("empId").is(empId));
		Update update1 = new Update();
		 if(leaveType.equalsIgnoreCase("sick")) {
			 update1.set("sickLeaveBalance",leaveBalance);
		 }
		 else if(leaveType.equalsIgnoreCase("flexi")) {
			 update1.set("flexiLeaveBalance",leaveBalance);
		 }
		 else if(leaveType.equalsIgnoreCase("casual")) {
			 update1.set("CasualleaveBalance",leaveBalance);
		 }
		UpdateResult result=mongoTemplate.updateFirst(query1, update1, EmployeeLeaveBalance.class);
		long count=result.getModifiedCount();
		if(count>=1) {
			return count;
		}
		
		return 0;
		
	}

}
