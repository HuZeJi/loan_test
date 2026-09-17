package com.victor_jimenez.test.service;

import com.victor_jimenez.test.model.Client;
import com.victor_jimenez.test.model.ClientDTO;
import com.victor_jimenez.test.model.Loan;
import com.victor_jimenez.test.repository.ClientRepository;
import com.victor_jimenez.test.repository.LoanRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
class ClientSvcImpl implements ClientSvc{

    private static final Logger log = LoggerFactory.getLogger(ClientSvcImpl.class);

    private final ClientRepository clientRepository;
    private final LoanRepository loanRepository;

    public ClientSvcImpl(ClientRepository clientRepository, LoanRepository loanRepository) {
        this.clientRepository = clientRepository;
        this.loanRepository = loanRepository;
    }

    @Override
    public ClientDTO create(ClientDTO client) {
        // Note: Falta proceso de rehabilitacion
        if (clientRepository.existsByEmail(client.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe el cliente");
        }
        Client entity = client.toEntity();
        entity.setActive(true);
        Client saved = clientRepository.save(entity);
        log.info("Cliente creado: clientId={}, email={}", saved.getId(), saved.getEmail());
        return ClientDTO.toDTO(saved);
    }

    @Override
    public Page<ClientDTO> list(Pageable pageable) {
        return clientRepository.findByActive(Boolean.TRUE, pageable).map(ClientDTO::toDTO);
    }

    @Override
    public ClientDTO update(String clientId, ClientDTO updates) {
        // TODO: Improve to only data available
        Client existing = findOrThrow(clientId);

        existing.setName(updates.getName());
        existing.setLastName(updates.getLastName());
        existing.setBirthday(updates.getBirthday());
        existing.setAddress(updates.getAddress());
        existing.setEmail(updates.getEmail());
        existing.setPhoneNumber(updates.getPhoneNumber());

        return ClientDTO.toDTO(clientRepository.save(existing));
    }

    @Transactional
    public void delete(String clientId) {
        Client existing = findOrThrow(clientId);
        existing.setActive(Boolean.FALSE);
        // Note: esta ejecucion en cascada seria mejor si se pudiera manejar a travez de un SP
        List<Loan> loans = loanRepository.findByClientId(clientId);
        loans.forEach(loan -> {
            loan.setActive(Boolean.FALSE);
            loanRepository.save(loan);
        });
        clientRepository.save(existing);
        log.info("Cliente desactivado: clientId={}, prestamosDesactivados={}", clientId, loans.size());
    }

    private Client findOrThrow(String clientId) {
        return clientRepository.findById(clientId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado"));
    }
}
