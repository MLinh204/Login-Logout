package com.example.demo.Service;

import com.example.demo.Repo.EmployeeRepository;
import com.example.demo.model.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class PhotoService {
    private final String UPLOAD_DIR = "src/main/resources/static/photos/";
    @Autowired
    private EmployeeRepository employeeRepository;

    public void saveEmployeePhoto(Employee employee, MultipartFile photo){
        if (!photo.isEmpty()){
            try {
                byte[] bytes = photo.getBytes();
                employee.setPhoto(bytes);
                employeeRepository.save(employee);

                String fileName = employee.getName() + "_" + photo.getOriginalFilename();
                Path path = Paths.get(UPLOAD_DIR + fileName);
                Files.write(path, bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public byte[] getEmployeePhoto(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId).orElse(null);
        return employee != null ? employee.getPhoto() : null;
    }
}
