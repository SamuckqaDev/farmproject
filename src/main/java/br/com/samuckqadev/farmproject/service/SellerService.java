package br.com.samuckqadev.farmproject.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import br.com.samuckqadev.farmproject.dto.seller.SellerRankingProjectionDTO;
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

        if (saleRepository.existsBySellerIdSeller(seller.getIdSeller())) {
            throw new SellerHasAssociatedSalesException();
        }

        sellerRepository.delete(seller);

        return BaseResponse.success(null, "Seller removed successfuly!");
    }

    public BaseResponse<List<SellerRankingProjectionDTO>> getSellerRanking(LocalDateTime startDate,
            LocalDateTime endDate) {
        // Se as datas vierem nulas, define um período padrão (ex: últimos 30 dias)
        var start = (startDate == null) ? LocalDateTime.now().minusDays(30) : startDate;
        var end = (endDate == null) ? LocalDateTime.now() : endDate;

        var ranking = saleRepository.getSellerRanking(start, end);

        if (ranking.isEmpty()) {
            return BaseResponse.success(ranking, "No sales found for the selected period.");
        }

        return BaseResponse.success(ranking, "Seller ranking retrieved successfully!");
    }

}
