package com.example.leavetracker.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.leavetracker.model.Employee;
import com.example.leavetracker.model.EmployeeLeaveBalance;
import com.example.leavetracker.service.EmployeeService;
import com.example.leavetracker.service.LeaveService;

@RestController
@RequestMapping("/leave")
public class LeaveController {
	
	@Autowired
	EmployeeService empService;
	
	@Autowired
	LeaveService leaveService;
	
	@PostMapping(value="/create" , consumes= {"multipart/form-data"},produces="application/json")
	public ResponseEntity<Object> createLeave(@RequestParam(name="empId") int empid,
			@RequestParam(name="leaveType") String leaveType,
			@RequestParam(name="startDate") LocalDate startDate,
			@RequestParam(name="endDate") LocalDate endDate,
			@RequestParam(name="detailedReason") String reason,
			@RequestParam(name="document",required=false) MultipartFile file) throws ParseException, IOException{
		
		byte[] document=null;
		if(file!=null) {
		 document=file.getBytes();
		}
		Employee emp=new Employee();
		emp.setDocument(document);
		emp.setEmpId(empid);
		emp.setStartDate(startDate);
		emp.setEndDate(endDate);
		emp.setLeaveType(leaveType);
		emp.setDetailedReason(reason);
		emp.setStatus("Pending");
		ResponseEntity<Object> employee=empService.createLeave(emp);
		return employee;
	}
	
	@PostMapping(value="/newbalance")
	public ResponseEntity<Object> leaveBalance(@RequestBody EmployeeLeaveBalance employeeLeaveBalance){
		EmployeeLeaveBalance leaveadd=leaveService.addLeave(employeeLeaveBalance);
		return new ResponseEntity<>(leaveadd,HttpStatus.OK);
	}
	
	@GetMapping("/download/{id}")
	public ResponseEntity<Object> downloadFile(@PathVariable int id) throws FileNotFoundException, IOException{
		ResponseEntity<Object> img=empService.getImage(id);
		return img;
	}
	
	
	@GetMapping("/show/{id}")
		public ResponseEntity<Object> showLeave(@PathVariable int id){
		        List<Employee> employee=empService.findById(id);
		        return new ResponseEntity<>(employee,HttpStatus.OK);	
		}
	
	
	@PatchMapping("/update/{id}/{status}")
	public ResponseEntity<Object> updateStatus(@PathVariable int id,@PathVariable String status){
		long list=empService.update(id,status);
		if(list>=1) {
		return new ResponseEntity<>(list,HttpStatus.OK);
		}
		return new ResponseEntity<>("No Modification",HttpStatus.ALREADY_REPORTED);
	}
	
	
	}
	

