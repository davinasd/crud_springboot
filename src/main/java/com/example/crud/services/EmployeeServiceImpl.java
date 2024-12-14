package com.example.crud.services;

import com.example.crud.entity.Employee;
import com.example.crud.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    // Constructor injection for better testability and immutability
    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Employee saveEmployee(Employee employee) {
        // Validate employee details before saving
        if (employee.getEmployeeName() == null || employee.getEmployeeName().isEmpty()) {
            throw new IllegalArgumentException("Employee name cannot be null or empty");
        }
        if (employee.getEmployeeSalary() <= 0) {
            throw new IllegalArgumentException("Employee salary must be greater than 0");
        }
        return employeeRepository.save(employee);
    }

    @Override
    public List<Employee> fetchAllEmployees() {
        List<Employee> allEmployees = employeeRepository.findAll();
        if (allEmployees.isEmpty()) {
            throw new RuntimeException("No employees found in the database");
        }
        return allEmployees;
    }

    @Override
    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));
    }

    @Override
    public Employee updateEmployeeById(Long id, Employee employee) {
        return employeeRepository.findById(id).map(originalEmployee -> {
            // Update employee name if it's non-null and non-empty
            if (employee.getEmployeeName() != null && !employee.getEmployeeName().isEmpty()) {
                originalEmployee.setEmployeeName(employee.getEmployeeName());
            }
            // Update employee salary if it's greater than 0
            if (employee.getEmployeeSalary() > 0) {
                originalEmployee.setEmployeeSalary(employee.getEmployeeSalary());
            }
            return employeeRepository.save(originalEmployee);
        }).orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));
    }

    @Override
    public String deleteEmployeeById(Long id) {
        if (employeeRepository.existsById(id)) {
            employeeRepository.deleteById(id);
            return "Employee deleted successfully";
        }
        return "No such employee in the database";
    }
}
