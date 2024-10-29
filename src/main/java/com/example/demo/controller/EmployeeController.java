package com.example.demo.controller;

import com.example.demo.Repo.EmployeeRepository;
import com.example.demo.model.Employee;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class EmployeeController {
    @Autowired
    private EmployeeRepository employeeRepository;

    @RequestMapping(value = "/list")
    public String getAllEmployees(Model model){
        List<Employee> employees = employeeRepository.findAll();
        model.addAttribute("employees", employees);
        return "employeeList";
    }

    @RequestMapping(value = "/{id}")
    public String getEmployeeById(Model model, @PathVariable Long id){
        Employee employee = employeeRepository.findById(id).orElse(null);
        model.addAttribute("employee", employee);
        return "employeeDetail";
    }

    @GetMapping(value = "/add")
    public String addEmployeeForm(Model model){
        model.addAttribute("employee", new Employee());
        return "addEmployee";
    }
    @PostMapping("/add")
    public String addEmployee(@Valid @ModelAttribute("employee") Employee employee, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "addEmployee";
        }
        // Process the employee object
        employeeRepository.save(employee);
        return "redirect:/";
    }

    @GetMapping(value = "/update/{id}")
    public String updateEmployeeForm(@PathVariable Long id, Model model){
        Employee employee = employeeRepository.findById(id).orElse(null);
        model.addAttribute("employee", employee);
        return "updateEmployee";
    }
    @PostMapping(value = "/update/{id}")
    public String updateEmployee(@Valid Employee employee, @RequestParam(value = "id", required = false) Long id, BindingResult result){
        Employee existingEmployee = employeeRepository.findById(id).orElse(null);
        if(result.hasErrors()){
            return "addEmployee";
        }
        existingEmployee.setName(employee.getName());
        existingEmployee.setAge(employee.getAge());
        existingEmployee.setAddress(employee.getAddress());
        existingEmployee.setImage(employee.getImage());
        employeeRepository.save(existingEmployee);
        return "redirect:/list";
    }

    @GetMapping(value = "/delete/{id}")
    public String deleteEmployee(@PathVariable Long id){
        employeeRepository.deleteById(id);
        return "redirect:/";
    }
}
