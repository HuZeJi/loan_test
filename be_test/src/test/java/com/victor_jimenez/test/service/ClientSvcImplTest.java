package com.victor_jimenez.test.service;

import com.victor_jimenez.test.model.Client;
import com.victor_jimenez.test.model.ClientDTO;
import com.victor_jimenez.test.model.Loan;
import com.victor_jimenez.test.repository.ClientRepository;
import com.victor_jimenez.test.repository.LoanRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClientSvcImplTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private LoanRepository loanRepository;

    @InjectMocks
    private ClientSvcImpl clientSvc;

    private Client client;
    private ClientDTO clientDTO;

    @BeforeEach
    void setUp() {
        client = new Client();
        client.setId("client-1");
        client.setName("Maria");
        client.setLastName("Gonzalez");
        client.setBirthday(LocalDate.of(1990, 5, 20));
        client.setAddress("Ciudad de Guatemala");
        client.setEmail("maria.gonzalez@example.com");
        client.setPhoneNumber("35157895");
        client.setActive(true);

        clientDTO = ClientDTO.toDTO(client);
    }

    @Test
    void when_create_OK() {
        when(clientRepository.existsByEmail(clientDTO.getEmail())).thenReturn(false);
        when(clientRepository.save(any(Client.class))).thenReturn(client);

        ClientDTO result = clientSvc.create(clientDTO);

        assertThat(result.getId()).isEqualTo(client.getId());
        assertThat(result.getEmail()).isEqualTo(client.getEmail());
        verify(clientRepository).save(any(Client.class));
    }

    @Test
    void when_create_email_already_exists() {
        when(clientRepository.existsByEmail(clientDTO.getEmail())).thenReturn(true);

        assertThatThrownBy(() -> clientSvc.create(clientDTO))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Ya existe el cliente");

        verify(clientRepository, never()).save(any());
    }

    @Test
    void when_list_OK() {
        Pageable pageable = Pageable.unpaged();
        Page<Client> page = new PageImpl<>(List.of(client));
        when(clientRepository.findByActive(anyBoolean(), eq(pageable))).thenReturn(page);

        Page<ClientDTO> result = clientSvc.list(pageable);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getId()).isEqualTo(client.getId());
    }

    @Test
    void when_update_client_exists() {
        when(clientRepository.findById("client-1")).thenReturn(Optional.of(client));
        when(clientRepository.save(any(Client.class))).thenAnswer(inv -> inv.getArgument(0));

        ClientDTO updates = new ClientDTO();
        updates.setName("Maria Jose");
        updates.setLastName("Gonzalez Lopez");
        updates.setBirthday(LocalDate.of(1990, 5, 20));
        updates.setAddress("Antigua Guatemala");
        updates.setEmail("mariajose@example.com");
        updates.setPhoneNumber("55512345");

        ClientDTO result = clientSvc.update("client-1", updates);

        assertThat(result.getName()).isEqualTo("Maria Jose");
        assertThat(result.getEmail()).isEqualTo("mariajose@example.com");
        verify(clientRepository).save(client);
    }

    @Test
    void when_update_client_not_found() {
        when(clientRepository.findById("missing")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> clientSvc.update("missing", clientDTO))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Cliente no encontrado");

        verify(clientRepository, never()).save(any());
    }

    @Test
    void when_delete_ok() {
        Loan loan1 = new Loan();
        loan1.setId("loan-1");
        loan1.setActive(true);
        Loan loan2 = new Loan();
        loan2.setId("loan-2");
        loan2.setActive(true);

        when(clientRepository.findById("client-1")).thenReturn(Optional.of(client));
        when(loanRepository.findByClientId("client-1")).thenReturn(List.of(loan1, loan2));

        clientSvc.delete("client-1");

        assertThat(client.getActive()).isFalse();
        assertThat(loan1.getActive()).isFalse();
        assertThat(loan2.getActive()).isFalse();
        verify(loanRepository, times(2)).save(any(Loan.class));
        verify(clientRepository).save(client);
    }

    @Test
    void when_delete_client_not_exists() {
        when(clientRepository.findById("missing")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> clientSvc.delete("missing"))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Cliente no encontrado");

        verify(loanRepository, never()).findByClientId(anyString());
    }
}
