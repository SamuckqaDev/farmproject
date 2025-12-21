package br.com.samuckqadev.farmproject.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.samuckqadev.farmproject.dto.sale.SaleRequestDTO;
import br.com.samuckqadev.farmproject.enums.DuckStatusEnum;
import br.com.samuckqadev.farmproject.exception.customer.CustomerNotFoundException;
import br.com.samuckqadev.farmproject.exception.seller.SellerNotFoundException;
import br.com.samuckqadev.farmproject.exception.duck.DuckNotFoundException;
import br.com.samuckqadev.farmproject.exception.duck.DuckAlreadySaledException;
import br.com.samuckqadev.farmproject.model.Duck;
import br.com.samuckqadev.farmproject.model.Sale;
import br.com.samuckqadev.farmproject.model.SaleItem;
import br.com.samuckqadev.farmproject.repository.CustomerRepository;
import br.com.samuckqadev.farmproject.repository.DuckRepository;
import br.com.samuckqadev.farmproject.repository.SaleRepository;
import br.com.samuckqadev.farmproject.repository.SellerRepository;
import br.com.samuckqadev.farmproject.response.BaseResponse;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SaleService {

    private final SaleRepository saleRepository;
    private final DuckRepository duckRepository;
    private final CustomerRepository customerRepository;
    private final SellerRepository sellerRepository;

    @Transactional
    public BaseResponse<Void> saveSale(SaleRequestDTO saleRequestDTO) {
        // 1. Busca Cliente e Vendedor por nome [cite: 8, 11]
        var customer = customerRepository.findByName(saleRequestDTO.customerName())
                .orElseThrow(CustomerNotFoundException::new);

        var seller = sellerRepository.findByName(saleRequestDTO.sellerName())
                .orElseThrow(SellerNotFoundException::new);

        // 2. Criação da Venda com data automática [cite: 19]
        Sale sale = Sale.builder()
                .customer(customer)
                .seller(seller)
                .items(new ArrayList<>())
                .build();

        BigDecimal subtotal = BigDecimal.ZERO;

        // 3. Processamento dos Patos [cite: 17]
        for (String duckName : saleRequestDTO.duckNames()) {
            Duck duck = duckRepository.findByName(duckName)
                    .orElseThrow(() -> new DuckNotFoundException());

            // Validação: Não deve ser possível realizar a venda do mesmo pato mais de uma
            // vez [cite: 20]
            if (duck.getStatus() == DuckStatusEnum.SALED) {
                throw new DuckAlreadySaledException();
            }

            // Cálculo do valor unitário conforme a quantidade de filhos
            BigDecimal unitPrice = calculateUnitPrice(duck);
            subtotal = subtotal.add(unitPrice);

            // Criação do item da venda [cite: 23]
            SaleItem item = SaleItem.builder()
                    .sale(sale)
                    .duck(duck)
                    .unitPrice(unitPrice)
                    .build();

            sale.getItems().add(item);

            duck.setStatus(DuckStatusEnum.SALED);
            duckRepository.save(duck);
        }

        // 4. Aplicação do desconto de 20% no VALOR TOTAL se elegível [cite: 10, 18]
        if (customer.isEligibleDiscount()) {
            BigDecimal discount = subtotal.multiply(new BigDecimal("0.20"));
            sale.setTotalValue(subtotal.subtract(discount));
        } else {
            sale.setTotalValue(subtotal);
        }

        saleRepository.save(sale);
        return BaseResponse.created(null, "Sale registered successfully!");
    }

    /**
     * Lógica de precificação baseada no número de filhos
     */
    private BigDecimal calculateUnitPrice(Duck duck) {
        long childrenCount = duckRepository.countByMother_IdDuck(duck.getIdDuck());

        if (childrenCount == 1) {
            return new BigDecimal("50.00"); 
        } else if (childrenCount == 2) {
            return new BigDecimal("25.00"); 
        } else {
            return new BigDecimal("70.00"); 
        }
    }
}