package com.example.demo.controller;

import com.example.demo.MultipartFileByteArrayEditor;
import com.example.demo.Repo.CompanyRepository;
import com.example.demo.Repo.EmployeeRepository;
import com.example.demo.model.Company;
import com.example.demo.model.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/company")
public class CompanyController {
    @Autowired
    private CompanyRepository companyRepository;
    private EmployeeRepository employeeRepository;
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(byte[].class, new MultipartFileByteArrayEditor());
    }
    @GetMapping(value = "/{id}")
    public String getCompanyById(@PathVariable(value = "id") Long id, Model model) {
        Company company = companyRepository.getById(id);
        List<Employee> employees = company.getEmployees();

        Map<Long, String> employeeBase64Photos = new HashMap<>();

        for (Employee employee : employees) {
            if (employee.getPhoto() != null && employee.getPhoto().length > 0) {
                String base64Photo = Base64.getEncoder().encodeToString(employee.getPhoto());
                employeeBase64Photos.put(employee.getId(), base64Photo);
            }
        }

        // Add the map to the model
        model.addAttribute("employeeBase64Photos", employeeBase64Photos);
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
