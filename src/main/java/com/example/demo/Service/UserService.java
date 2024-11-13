package com.example.demo.Service;

import com.example.demo.DTO.UserRegistrationDTO;
import com.example.demo.model.User;
import com.example.demo.Repo.RoleRepository;
import com.example.demo.Repo.UserRepository;
import com.example.demo.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public User registerNewUser(UserRegistrationDTO registrationDTO) {
        // Check if user exists
        if (userRepository.findByUsername(registrationDTO.getUsername()) != null) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.findByEmail(registrationDTO.getEmail()) != null) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setUsername(registrationDTO.getUsername());
        user.setEmail(registrationDTO.getEmail());
        user.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));

        // Set roles
        Set<Role> roles = new HashSet<>();
        if (registrationDTO.getRoleNames() != null && registrationDTO.getRoleNames().length > 0) {
            for (String roleName : registrationDTO.getRoleNames()) {
                Role role = roleRepository.findByName(roleName);
                if (role != null) {
                    roles.add(role);
                }
            }
        }

        // If no roles selected or found, set default role as STANDARD_USER
        if (roles.isEmpty()) {
            Role defaultRole = roleRepository.findByName("STANDARD_USER");
            if (defaultRole == null) {
                defaultRole = new Role();
                defaultRole.setName("STANDARD_USER");
                roleRepository.save(defaultRole);
            }
            roles.add(defaultRole);
        }

        user.setRoles(roles);
        return userRepository.save(user);
    }
}