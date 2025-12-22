package br.com.samuckqadev.farmproject.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
        var customer = customerRepository.findByName(saleRequestDTO.customerName())
                .orElseThrow(CustomerNotFoundException::new);

        var seller = sellerRepository.findByName(saleRequestDTO.sellerName())
                .orElseThrow(SellerNotFoundException::new);

        var sale = Sale.builder()
                .customer(customer)
                .seller(seller)
                .items(new ArrayList<>())
                .build();

        List<SaleItem> items = saleRequestDTO.duckNames().stream()
                .map(duckName -> {
                    var duck = duckRepository.findByName(duckName)
                            .orElseThrow(DuckNotFoundException::new);

                    if (duck.getStatus() == DuckStatusEnum.SALED) {
                        throw new DuckAlreadySaledException();
                    }

                    var unitPrice = calculateUnitPrice(duck);

                    duck.setStatus(DuckStatusEnum.SALED);
                    duckRepository.save(duck);

                    return SaleItem.builder()
                            .sale(sale)
                            .duck(duck)
                            .unitPrice(unitPrice)
                            .build();
                })
                .toList();

        sale.getItems().addAll(items);

        var subtotal = items.stream()
                .map(SaleItem::getUnitPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        var totalValue = customer.isEligibleDiscount()
                ? subtotal.multiply(new BigDecimal("0.80"))
                : subtotal;

        sale.setTotalValue(totalValue);

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