package com.victor_jimenez.test.repository;

import com.victor_jimenez.test.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, String> {

    boolean existsByEmail(String email);
}
