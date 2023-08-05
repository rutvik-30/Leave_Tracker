package com.example.leavetracker.repos;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.example.leavetracker.model.Employee;


public interface EmployeeRepo extends MongoRepository<Employee,String> {
	
	List<Employee> findByEmpId(int id);
	List<Employee> findByEmpIdAndStatus(int id, String status);
}
