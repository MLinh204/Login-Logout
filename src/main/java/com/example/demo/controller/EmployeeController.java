package com.example.demo.controller;

import com.example.demo.Repo.CompanyRepository;
import com.example.demo.Repo.EmployeeRepository;
import com.example.demo.model.Company;
import com.example.demo.model.Employee;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private CompanyRepository companyRepository;

    @RequestMapping("/list")
    public String getAllEmployees(Model model){
        List<Employee> employees = employeeRepository.findAll();
        model.addAttribute("employees", employees);
        return "employeeList";
    }

    @RequestMapping("/{id}")
    public String getEmployeeById(Model model, @PathVariable Long id){
        Employee employee = employeeRepository.findById(id).orElse(null);
        model.addAttribute("employee", employee);
        return "employeeDetail";
    }

    @GetMapping("/add")
    public String addEmployeeForm(Model model){
        List<Company> companies = companyRepository.findAll();
        model.addAttribute("companies", companies);
        model.addAttribute("employee", new Employee());
        return "addEmployee";
    }
    @PostMapping("/add")
    public String addEmployee(@Valid @ModelAttribute("employee") Employee employee, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "addEmployee";
        }
        employeeRepository.save(employee);
        return "redirect:/";
    }

    @GetMapping("/update/{id}")
    public String updateEmployeeForm(@PathVariable Long id, Model model){
        Employee employee = employeeRepository.findById(id).orElse(null);
        List<Company> companies = companyRepository.findAll();
        model.addAttribute("companies", companies);
        model.addAttribute("employee", employee);
        return "updateEmployee";
    }
    @PostMapping("/update/{id}")
    public String updateEmployee(@Valid Employee employee, @PathVariable Long id, BindingResult result){
        Employee existingEmployee = employeeRepository.findById(id).orElse(null);
        if(result.hasErrors()){
            return "addEmployee";
        }
        existingEmployee.setName(employee.getName());
        existingEmployee.setAge(employee.getAge());
        existingEmployee.setAddress(employee.getAddress());
        existingEmployee.setImage(employee.getImage());
        existingEmployee.setCompany(employee.getCompany());
        employeeRepository.save(existingEmployee);
        return "redirect:/employee/list";
    }

    @GetMapping(value = "/delete/{id}")
    public String deleteEmployee(@PathVariable Long id){
        employeeRepository.deleteById(id);
        return "redirect:/employee/list";
    }

    @GetMapping("/search")
    public String searchEmployee(Model model, @RequestParam(value = "name") String name){
        List<Employee> employees = employeeRepository.findByNameContaining(name);
        model.addAttribute("employees", employees);
        return "employeeList";
    }
}
