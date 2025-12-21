package br.com.samuckqadev.farmproject.service;

import org.springframework.stereotype.Service;

import br.com.samuckqadev.farmproject.dto.duck.DuckRequestDTO;
import br.com.samuckqadev.farmproject.enums.DuckStatusEnum;
import br.com.samuckqadev.farmproject.exception.duck.DuckAlreadyExistsException;
import br.com.samuckqadev.farmproject.exception.duck.DuckMotherNotFoundException;
import br.com.samuckqadev.farmproject.model.Duck;
import br.com.samuckqadev.farmproject.repository.DuckRepository;
import br.com.samuckqadev.farmproject.response.BaseResponse;
import br.com.samuckqadev.farmproject.util.GenericMapperUtil;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class DuckService {

    private final DuckRepository duckRepository;

    public BaseResponse<Void> saveDuck(DuckRequestDTO duckRequestDTO) {
        this.duckRepository.findByName(duckRequestDTO.name()).ifPresent(duck -> {
            throw new DuckAlreadyExistsException();
        });

        var mother = this.duckRepository.findByName(duckRequestDTO.mother())
                .orElseThrow(DuckMotherNotFoundException::new);
        var duckEntity = GenericMapperUtil.parseObject(duckRequestDTO, Duck.class);

        duckEntity.setMother(mother);
        duckEntity.setStatus(DuckStatusEnum.AVAILABLE);
        this.duckRepository.save(duckEntity);

        return BaseResponse.created(null, "Duck registered successfully!");
    }

}
