package com.bugsfree.registration.utils;

import com.bugsfree.registration.model.Role;
import com.bugsfree.registration.model.User;
import com.bugsfree.registration.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DataInitializerService {

    private final UserRepository userRepository;

    @PostConstruct
    public void initializeData() {
        User adminAccount = userRepository.findByRole(Role.ADMIN);
        if (null == adminAccount) {
            User user = new User();
            user.setEmailAddress("admin@bugsfree.com");
            user.setRole(Role.ADMIN);
            user.setFirstName("admin");
            user.setLastName("admin");
            user.setPassword(new BCryptPasswordEncoder().encode("admin"));
            userRepository.save(user);
        }
    }
}

