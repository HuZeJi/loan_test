package com.victor_jimenez.test.service;

import com.victor_jimenez.test.model.Client;
import com.victor_jimenez.test.model.ClientDTO;
import com.victor_jimenez.test.model.Loan;
import com.victor_jimenez.test.repository.ClientRepository;
import com.victor_jimenez.test.repository.LoanRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Reglas de negocio de gestion de clientes.
 * Ver docs/DIAGRAMS.md - "Gestion de clientes".
 */
@Service
class ClientSvcImpl implements ClientSvc{

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
        return ClientDTO.toDTO(clientRepository.save(client.toEntity()));
    }

    @Override
    public Page<ClientDTO> list(Pageable pageable) {
        return clientRepository.findAll(pageable).map(ClientDTO::toDTO);
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
    }

    private Client findOrThrow(String clientId) {
        return clientRepository.findById(clientId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado"));
    }
}
