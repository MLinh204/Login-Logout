package com.example.demo.controller;

import com.example.demo.Repo.CompanyRepository;
import com.example.demo.Repo.EmployeeRepository;
import com.example.demo.model.Company;
import com.example.demo.model.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/company")
public class CompanyController {
    @Autowired
    private CompanyRepository companyRepository;
    private EmployeeRepository employeeRepository;
    @GetMapping(value = "/{id}")
    public String getCompanyById(@PathVariable(value = "id") Long id, Model model) {
        Company company = companyRepository.getById(id);
        List<Employee> employees = company.getEmployees();
        model.addAttribute("company", company);
        model.addAttribute("employees", employees);
        return "companyDetail";
    }
    @GetMapping(value = "/list")
    public String getAllCompany(Model model){
        List<Company> companies = companyRepository.findAll();
        model.addAttribute("companies", companies);
        return "companyList";
    }
}
