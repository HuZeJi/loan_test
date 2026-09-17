package com.victor_jimenez.test.repository;

import com.victor_jimenez.test.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}
