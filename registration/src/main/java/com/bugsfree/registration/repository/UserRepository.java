package com.bugsfree.registration.repository;

import com.bugsfree.registration.model.Role;
import com.bugsfree.registration.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmailAddress(String emailAddress);

    User findByRole(Role role);

}
