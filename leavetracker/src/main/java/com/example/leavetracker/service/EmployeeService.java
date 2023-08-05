package com.example.leavetracker.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.leavetracker.model.Employee;
import com.example.leavetracker.model.EmployeeLeaveBalance;
import com.example.leavetracker.repos.EmployeeRepo;
import com.example.leavetracker.repos.LeaveRepo;
import com.mongodb.client.result.UpdateResult;
import com.example.leavetracker.error.CustomErrors;

@Service
public class EmployeeService {
	
	@Autowired
	EmployeeRepo employeeRepo;
	
	@Autowired
	LeaveRepo leaveRepo;
	
	@Autowired
	LeaveService leaveService;
	
	@Autowired
	MongoTemplate mongoTemplate;
	
	private Logger logger=LoggerFactory.getLogger("EmployeeService.class");
	private final static String RESPONSE="OK";
	
	public ResponseEntity<Object> createLeave(Employee employee) throws ParseException {
		
		
		LocalDate startDate=employee.getStartDate();
		LocalDate endDate=employee.getEndDate();
		
		if(startDate!=null && endDate!=null) {
			String response=checkValidDate(startDate,endDate);
			if(response.equals(RESPONSE)) {
				
				int empId=employee.getEmpId();
				String leaveType=employee.getLeaveType();
				long diffInDays = ChronoUnit.DAYS.between(startDate, endDate);
				
				if(diffInDays>=7) {
					String res=checkDocumentAvailable(employee);
					if(!res.equals("OK")) {
						return new ResponseEntity<>("Document is Mandatory to upload",HttpStatus.BAD_REQUEST);
					}
				}
				
				String result=checkLeaveStatus(empId,leaveType,diffInDays);
				if(result.equals("OK")) {
				Employee emp=employeeRepo.save(employee);
				return new ResponseEntity<>(emp,HttpStatus.OK);
				}
				else 
				{
			    return new ResponseEntity<>("Leave balance not sufficient",HttpStatus.BAD_REQUEST);
				}
			}
			else {
			CustomErrors error=new CustomErrors(400,"Not a valid startDate/endDate");
			return new ResponseEntity<>(error,HttpStatus.BAD_REQUEST);
			     }
			}
		else
		{
			CustomErrors custom=new CustomErrors(400,"Invalid startDate/endDate");
			return new ResponseEntity<>(custom,HttpStatus.BAD_REQUEST);
		}		
	}

	private String checkDocumentAvailable(Employee employee) {
		if(employee.getDocument()==null) {
			return "NOOK";
		}
		else
		{
			return "OK";
		}

	}

	private String checkLeaveStatus(int empId,String leaveType,long diffInDays) {
		
		 EmployeeLeaveBalance emp;
		 emp=leaveRepo.findByEmpId(empId);
		 int leaveBalance=0;
		 String result="";
		 if(leaveType.equalsIgnoreCase("sick")) {
			
		 leaveBalance=emp.getSickLeaveBalance();
		 result=checkLeaveAvailability(empId,leaveBalance,leaveType,diffInDays);
		
		}
		else if(leaveType.equalsIgnoreCase("flexi")) {
		leaveBalance=emp.getFlexiLeaveBalance();
		result=checkLeaveAvailability(empId,leaveBalance,leaveType,diffInDays);
		}
		else if(leaveType.equalsIgnoreCase("casual")) {
		leaveBalance=emp.getCasualleaveBalance();
		result=checkLeaveAvailability(empId,leaveBalance,leaveType,diffInDays);
		}
		return result;
		}
	
	 private String checkLeaveAvailability(int empId,int leaveBalance, String leaveType, long diffInDays) {
		   
		 if(diffInDays<=leaveBalance){
				  leaveBalance-=diffInDays; 
				  String res=leaveService.update(empId,leaveType,leaveBalance);
				  if(res!=null) {
					  return "OK";
				  }  
		 	}
		 	return "NO_OK";
	}

	 
	private String checkValidDate(LocalDate startDate, LocalDate endDate) {
		LocalDate currentDate=LocalDate.now();
		System.out.println(startDate.compareTo(currentDate)<0);
		System.out.println(startDate.compareTo(endDate)>0);
		if((startDate.compareTo(currentDate)<0) || startDate.compareTo(endDate)>0  ) {
			return "error";
		}
		return RESPONSE;
	}

	public ResponseEntity<Object> getImage(int id) throws FileNotFoundException, IOException {
		List<Employee> emp1=employeeRepo.findByEmpId(id);
		Employee emp=emp1.get(0);
		byte[] docs=emp.getDocument();
		File outputFile = new File("./src/main/resources/output.pdf");
		try (FileOutputStream outputStream = new FileOutputStream(outputFile)) {
		    outputStream.write(docs);
		}
		return new ResponseEntity<>("File downloaded",HttpStatus.OK);
	}

	

	public List<Employee> findById(int id) {
		List<Employee> employee=employeeRepo.findByEmpId(id);
		return employee;
	}

	public long update(int id,String status) {
		List<Employee> list=employeeRepo.findByEmpIdAndStatus(id,status);
		if(list.size()<=0) {
			return 0;
		}
		Employee emp=list.get(0);
		int empId=emp.getEmpId();
		
		Criteria criteria = Criteria.where("empId").is(id).and("status").is(status);
        Query query = Query.query(criteria);
        Update update=new Update();
        update.set("status","Approved");
        UpdateResult res=mongoTemplate.updateFirst(query,update,Employee.class);
        long count=res.getModifiedCount();
		System.out.println(list.toString());
		return count;
		
	}
}
