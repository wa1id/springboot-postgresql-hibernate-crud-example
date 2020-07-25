package com.walid.springboot.controller;

import com.walid.springboot.exception.ResourceNotFoundException;
import com.walid.springboot.model.Employee;
import com.walid.springboot.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/")
public class EmployeeController {

    @Autowired
    private EmployeeRepository employeeRepository;

    //GET all employees
    @GetMapping("employees")
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    //GET employee by id
    @GetMapping("employees/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable(value = "id") Long employeeId) throws ResourceNotFoundException {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id: " + employeeId));

        return ResponseEntity.ok(employee);
    }

    //POST employee (save, new)
    @PostMapping("employees")
    public Employee createEmployee(@RequestBody Employee employee) {
        return employeeRepository.save(employee);
    }

    //PUT employee (update)
    @PutMapping("employees/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable(value = "id") Long employeeId,@Valid @RequestBody Employee updatedEmployee) throws ResourceNotFoundException {
        Employee employeeToUpdate = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id: " + employeeId));

        employeeToUpdate.setFirstName(updatedEmployee.getFirstName());
        employeeToUpdate.setLastName(updatedEmployee.getLastName());
        employeeToUpdate.setEmail(updatedEmployee.getEmail());

        return ResponseEntity.ok(employeeRepository.save(employeeToUpdate));
    }

    //DELETE employee
    @DeleteMapping("employee/{id}")
    public Map<String, Boolean> deleteEmployee(@PathVariable(value = "id") Long employeeId) throws ResourceNotFoundException {
        Employee employeeToDelete = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id: " + employeeId));

        employeeRepository.delete(employeeToDelete);

        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);

        return response;
    }
}
