package com.example.demo.controller;

import com.example.demo.MultipartFileByteArrayEditor;
import com.example.demo.Service.PhotoService;
import com.example.demo.model.User;
import com.example.demo.Repo.CompanyRepository;
import com.example.demo.Repo.EmployeeRepository;
import com.example.demo.Repo.UserRepository;
import com.example.demo.model.Company;
import com.example.demo.model.Employee;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.AccessDeniedException;
import java.security.Principal;
import java.util.Base64;
import java.util.List;

@Controller
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PhotoService photoService;

    @RequestMapping("/list")
    public String getAllEmployees(Model model, Principal principal){
        if(principal == null){
            return "redirect:/login";
        }
        List<Employee> employees = employeeRepository.findAll();
        model.addAttribute("employees", employees);
        return "employeeList";
    }

    @RequestMapping("/{id}")
    public String getEmployeeById(Model model, @PathVariable Long id, Principal principal){
        if(principal == null){
            return "redirect:/login";
        }
        Employee employee = employeeRepository.findById(id).orElse(null);
        String base64Photo = employee.getPhoto() != null ?
                Base64.getEncoder().encodeToString(employee.getPhoto()) : null;
        model.addAttribute("employee", employee);
        model.addAttribute("base64Photo", base64Photo);
        return "employeeDetail";
    }

    @GetMapping("/add")
    public String addEmployeeForm(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        String username = principal.getName();
        User user = userRepository.findByUsername(username);
            List<Company> companies = companyRepository.findAll();
            model.addAttribute("companies", companies);
            model.addAttribute("employee", new Employee());
            return "addEmployee";
    }
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(byte[].class, new MultipartFileByteArrayEditor());
    }
    @PostMapping("/add")
    public String addEmployee(@Valid @ModelAttribute("employee") Employee employee, BindingResult result, @RequestParam(name = "photo", required = false) MultipartFile photo, Model model) {
        if (result.hasErrors()) {
            return "addEmployee";
        }
        employeeRepository.save(employee);
        if (photo != null && !photo.isEmpty()) {
            photoService.saveEmployeePhoto(employee, photo);
        }
        return "redirect:/";
    }

    @GetMapping("/update/{id}")
    public String updateEmployeeForm(@PathVariable Long id, Model model, Principal principal){
        if(principal == null){
            return "redirect:/login";
        }
        Employee employee = employeeRepository.findById(id).orElse(null);
        List<Company> companies = companyRepository.findAll();
        model.addAttribute("companies", companies);
        model.addAttribute("employee", employee);
        return "updateEmployee";
    }
    @PostMapping("/update/{id}")
    public String updateEmployee(@Valid Employee employee, @PathVariable Long id, BindingResult result, @RequestParam("photo") MultipartFile photo){
        Employee existingEmployee = employeeRepository.findById(id).orElse(null);
        if(result.hasErrors()){
            return "addEmployee";
        }
        existingEmployee.setName(employee.getName());
        existingEmployee.setAge(employee.getAge());
        existingEmployee.setAddress(employee.getAddress());
        existingEmployee.setCompany(employee.getCompany());
        employeeRepository.save(existingEmployee);
        photoService.saveEmployeePhoto(existingEmployee, photo);
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
    @GetMapping("/sort/name/asc")
    public String sortEmployeeAscending(Model model) {
        List<Employee> employees = employeeRepository.findAll(Sort.by(Sort.Direction.ASC, "name"));
        model.addAttribute("employees", employees);
        model.addAttribute("sortOrder", "asc");
        return "employeeList";
    }

    @GetMapping("/sort/name/desc")
    public String sortEmployeeDescending(Model model) {
        List<Employee> employees = employeeRepository.findAll(Sort.by(Sort.Direction.DESC, "name"));
        model.addAttribute("employees", employees);
        model.addAttribute("sortOrder", "desc");
        return "employeeList";
    }
    @GetMapping("/sort/id/asc")
    public String sortEmployeeIdAscending(Model model) {
        List<Employee> employees = employeeRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
        model.addAttribute("employees", employees);
        model.addAttribute("sortOrder", "asc");
        return "employeeList";
    }
    @GetMapping("/sort/id/desc")
    public String sortEmployeeIdDescending(Model model) {
        List<Employee> employees = employeeRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        model.addAttribute("employees", employees);
        model.addAttribute("sortOrder", "desc");
        return "employeeList";
    }
}
