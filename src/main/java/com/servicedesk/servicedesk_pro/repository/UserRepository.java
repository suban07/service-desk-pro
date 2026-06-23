package com.servicedesk.servicedesk_pro.repository;

import com.servicedesk.servicedesk_pro.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
    boolean existsByEmail(String email);
}
