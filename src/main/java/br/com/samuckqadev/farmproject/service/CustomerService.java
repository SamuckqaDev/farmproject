package br.com.samuckqadev.farmproject.service;

import org.springframework.stereotype.Service;

import br.com.samuckqadev.farmproject.dto.customer.CustomerRequestDTO;
import br.com.samuckqadev.farmproject.exception.customer.CustomerAlreadyRegistredException;
import br.com.samuckqadev.farmproject.model.Customer;
import br.com.samuckqadev.farmproject.repository.CustomerRepository;
import br.com.samuckqadev.farmproject.response.BaseResponse;
import br.com.samuckqadev.farmproject.util.GenericMapperUtil;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    public BaseResponse<Void> saveCustomer(CustomerRequestDTO customerRequestDTO) {
        this.customerRepository.findByName(customerRequestDTO.name()).ifPresent(c -> {
            throw new CustomerAlreadyRegistredException();
        });
        var customerEntity = GenericMapperUtil
                .parseObject(customerRequestDTO, Customer.class);
                    System.out.println("DTO name: " + customerRequestDTO.name());
        System.out.println("Entity name after mapping: " + customerEntity.getName());
        this.customerRepository.save(customerEntity);
        return BaseResponse.created(null, "Customer registred with successfull!");
    }

}
