package com.app.processor;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.app.dto.EmployeeDTO;
import com.app.model.Employee;

@Component
public class EmployeeProcessor implements ItemProcessor<EmployeeDTO ,Employee> {

	@Override
	public Employee process(EmployeeDTO employeeDTO) throws Exception {
		Employee employee = new Employee();
		employee.setEmployeeId(employeeDTO.getEmployeeId());
		employee.setFirstName(employeeDTO.getFirstName());
		employee.setLastName(employeeDTO.getLastName());
		employee.setEmail(employeeDTO.getEmail());
		employee.setAge(employeeDTO.getAge());
		System.out.println("Inside Employee processor" +employee.toString());
		return employee;
	}
 
}
