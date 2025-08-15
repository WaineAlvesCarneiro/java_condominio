package com.wa.java_condominio.services;

import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wa.java_condominio.dto.ImovelDTO;
import com.wa.java_condominio.dto.PagedResultDto;
import com.wa.java_condominio.model.Imovel;
import com.wa.java_condominio.repositories.ImovelRepository;
import com.wa.java_condominio.services.shared.Result;

@Service
public class ImovelService {

    @Autowired
    private ImovelRepository imovelRepository;

    @Autowired
    private MoradorService moradorService;

    @Transactional(readOnly = true)
    public Result<List<ImovelDTO>> getAll() {
        List<Imovel> imoveis = imovelRepository.findAll();
        List<ImovelDTO> dtos = imoveis
        	.stream()
    		.map(ImovelDTO::new)
    		.collect(Collectors.toList());
        
        return Result.success(dtos);
    }

    @Transactional(readOnly = true)
    public Result<PagedResultDto<ImovelDTO>> getAllPaged(int page, int size, String orderBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("ASC") ? Sort.by(orderBy).ascending() : Sort.by(orderBy).descending();
        Pageable pageRequest = PageRequest.of(page, size, sort);
        
        Page<Imovel> pageImoveis = imovelRepository.findAll(pageRequest);

        List<ImovelDTO> dtos = pageImoveis
        	.stream()
    		.map(ImovelDTO::new)
    		.collect(Collectors.toList());

        PagedResultDto<ImovelDTO> pagedResult = new PagedResultDto<>();
        pagedResult.setItems(dtos);
        pagedResult.setTotalCount(pageImoveis.getTotalElements());
        pagedResult.setPageIndex(page);
        pagedResult.setLinesPerPage(size);

        return Result.success(pagedResult);
    }

    @Transactional(readOnly = true)
    public Result<ImovelDTO> getById(Long id) {
        Optional<Imovel> imovelOpt = imovelRepository.findById(id);
        if (imovelOpt.isEmpty()) {
            return Result.failure("Imóvel não encontrado.");
        }
        
        ImovelDTO dto = new ImovelDTO(imovelOpt.get());
        
        return Result.success(dto);
    }

    @Transactional
    public Result<ImovelDTO> add(ImovelDTO dto) {
        Imovel imovel = new Imovel();
        imovel.setBloco(dto.getBloco());
        imovel.setApartamento(dto.getApartamento());
        imovel.setBoxGaragem(dto.getBoxGaragem());

        imovel = imovelRepository.save(imovel);

        dto.setId(imovel.getId());
        
        return Result.success(dto, "Imóvel criado com sucesso.");
    }

    @Transactional
    public Result<Void> update(Long id, ImovelDTO dto) {
        Optional<Imovel> imovelOpt = imovelRepository.findById(id);
        if (imovelOpt.isEmpty()) {
            return Result.failure("Imóvel não encontrado.");
        }
        
        Imovel imovel = imovelOpt.get();
        imovel.setBloco(dto.getBloco());
        imovel.setApartamento(dto.getApartamento());
        imovel.setBoxGaragem(dto.getBoxGaragem());

        imovelRepository.save(imovel);

        return Result.success("Imóvel atualizado com sucesso.");
    }

    @Transactional
    public Result<Void> delete(Long id) {
        boolean possuiMorador = moradorService.existsByMoradorId(id);
        if (possuiMorador) {
            return Result.failure("Não é possível excluir o imóvel, pois existem moradores vinculados.");
        }

        Optional<Imovel> imovelOpt = imovelRepository.findById(id);
        if (imovelOpt.isEmpty()) {
            return Result.failure("Imóvel não encontrado.");
        }

        try {
            imovelRepository.deleteById(id);
            return Result.success("Imóvel deletado com sucesso.");
        } catch (DataIntegrityViolationException e) {
            return Result.failure("Erro ao deletar imóvel: possível vínculo com outros registros.");
        }
    }
}
