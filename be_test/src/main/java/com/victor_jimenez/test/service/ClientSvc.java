package com.victor_jimenez.test.service;

import com.victor_jimenez.test.model.Client;
import com.victor_jimenez.test.model.ClientDTO;
import com.victor_jimenez.test.repository.ClientRepository;
import com.victor_jimenez.test.repository.LoanRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

public interface ClientSvc {
    ClientDTO create(ClientDTO client);
    Page<ClientDTO> list(Pageable pageable);
    ClientDTO update(String clientId, ClientDTO updates);
    void delete(String clientId);
}
