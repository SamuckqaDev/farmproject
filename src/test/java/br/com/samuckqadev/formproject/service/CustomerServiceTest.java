package br.com.samuckqadev.formproject.service;

import br.com.samuckqadev.farmproject.dto.customer.CustomerRequestDTO;
import br.com.samuckqadev.farmproject.exception.customer.CustomerAlreadyRegistredException;
import br.com.samuckqadev.farmproject.model.Customer;
import br.com.samuckqadev.farmproject.repository.CustomerRepository;
import br.com.samuckqadev.farmproject.response.BaseResponse;
import br.com.samuckqadev.farmproject.service.CustomerService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    private CustomerRequestDTO customerRequestDTO;
    private Customer customer;

    @BeforeEach
    void setUp() {
        // Setup usando Builder no Record
        customerRequestDTO = CustomerRequestDTO.builder()
                .name("João Silva")
                .eligibleDiscount(true)
                .build();

        customer = new Customer();
        customer.setIdCustomer(UUID.randomUUID());
        customer.setName("João Silva");
        customer.setEligibleDiscount(true);
    }

    @Test
    @DisplayName("Deve salvar cliente com sucesso quando o nome não estiver duplicado")
    void shouldSaveCustomerSuccessfully() {
        // GIVEN
        when(customerRepository.findByName(customerRequestDTO.name())).thenReturn(Optional.empty());
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        // WHEN
        BaseResponse<Void> response = customerService.saveCustomer(customerRequestDTO);

        // THEN
        assertNotNull(response);
        assertEquals(201, response.getStatusCode());
        assertEquals("Customer registred with successfull!", response.getMessage());
        
        // Verifica se o repositório foi chamado corretamente
        verify(customerRepository, times(1)).findByName(customerRequestDTO.name());
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    @DisplayName("Deve lançar CustomerAlreadyRegistredException quando o nome já existir")
    void shouldThrowExceptionWhenCustomerNameAlreadyExists() {
        // GIVEN
        when(customerRepository.findByName(customerRequestDTO.name())).thenReturn(Optional.of(customer));

        // WHEN & THEN
        assertThrows(CustomerAlreadyRegistredException.class, () -> {
            customerService.saveCustomer(customerRequestDTO);
        });

        // O save nunca deve ser chamado se o nome já existe
        verify(customerRepository, never()).save(any(Customer.class));
    }
}