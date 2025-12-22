package br.com.samuckqadev.farmproject.service;

import org.springframework.stereotype.Service;

import br.com.samuckqadev.farmproject.dto.seller.SellerRequestDTO;
import br.com.samuckqadev.farmproject.exception.seller.SellerCpfAlreadyExists;
import br.com.samuckqadev.farmproject.exception.seller.SellerHasAssociatedSalesException;
import br.com.samuckqadev.farmproject.exception.seller.SellerNotFoundException;
import br.com.samuckqadev.farmproject.model.Seller;
import br.com.samuckqadev.farmproject.repository.SaleRepository;
import br.com.samuckqadev.farmproject.repository.SellerRepository;
import br.com.samuckqadev.farmproject.response.BaseResponse;
import br.com.samuckqadev.farmproject.util.GenericMapperUtil;
import br.com.samuckqadev.farmproject.util.SellerRegistrationGenerationUtil;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SellerService {

    private final SellerRepository sellerRepository;

    private final SaleRepository saleRepository;

    @Transactional
    public BaseResponse<Void> saveSeller(SellerRequestDTO sellerRequestDTO) {

        sellerRepository.findByCpf(sellerRequestDTO.cpf()).ifPresent(s -> {
            throw new SellerCpfAlreadyExists();
        });

        var sellerEntity = GenericMapperUtil
                .parseObject(sellerRequestDTO, Seller.class);
        sellerEntity.setRegistration(SellerRegistrationGenerationUtil.generate());

        sellerRepository.save(sellerEntity);
        return BaseResponse.created(null, "Seller registred Successfuly!");
    }

    @Transactional
    public BaseResponse<Void> deleteSeller(String cpf) {
        var seller = sellerRepository.findByCpf(cpf)
                .orElseThrow(() -> new SellerNotFoundException());

        if (saleRepository.existsBySellerUuid(seller.getIdSeller())) {
            throw new SellerHasAssociatedSalesException();
        }

        sellerRepository.delete(seller);

        return BaseResponse.success(null, "Seller removed successfuly!");
    }

}
