package br.com.samuckqadev.farmproject.service;

import org.springframework.stereotype.Service;

import br.com.samuckqadev.farmproject.repository.CustomerRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;



}
