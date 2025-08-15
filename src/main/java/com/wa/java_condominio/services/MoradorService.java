package com.wa.java_condominio.services;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wa.java_condominio.dto.MoradorDTO;
import com.wa.java_condominio.dto.ImovelDTO;
import com.wa.java_condominio.dto.PagedResultDto;
import com.wa.java_condominio.model.Imovel;
import com.wa.java_condominio.model.Morador;
import com.wa.java_condominio.repositories.ImovelRepository;
import com.wa.java_condominio.repositories.MoradorRepository;
import com.wa.java_condominio.services.exceptions.DatabaseException;
import com.wa.java_condominio.services.shared.Result;
import com.wa.java_condominio.utils.*;

@Service
public class MoradorService {

    @Autowired
    private MoradorRepository moradorRepository;

    @Autowired
    private ImovelRepository imovelRepository;

    @Transactional(readOnly = true)
    public Result<List<MoradorDTO>> getAll() {
        List<MoradorDTO> dtos = moradorRepository.findAll()
            .stream()
            .map(m -> new MoradorDTO(m, m.getImovel()))
            .collect(Collectors.toList());

        return Result.success(dtos);
    }
    
    @Transactional(readOnly = true)
    public Result<PagedResultDto<MoradorDTO>> getAllPaged(int page, int size, String orderBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("ASC") ? Sort.by(orderBy).ascending() : Sort.by(orderBy).descending();
        PageRequest pageRequest = PageRequest.of(page, size, sort);
        
        Page<MoradorDTO> pageDtos = moradorRepository.findAll(pageRequest)
                .map(m -> new MoradorDTO(m, m.getImovel()));

            PagedResultDto<MoradorDTO> pagedResult = new PagedResultDto<>();
            pagedResult.setItems(pageDtos.getContent());
            pagedResult.setTotalCount(pageDtos.getTotalElements());
            pagedResult.setPageIndex(page);
            pagedResult.setLinesPerPage(size);

            return Result.success(pagedResult);
    }

    @Transactional(readOnly = true)
    public Result<MoradorDTO> getById(long id) {
        return moradorRepository.findById(id)
            .map(m -> Result.success(new MoradorDTO(m, m.getImovel())))
            .orElse(Result.failure("Morador não encontrado."));
    }

    @Transactional
    public Result<MoradorDTO> add(MoradorDTO dto) {
        Imovel imovel = imovelRepository.findById(dto.getImovelId())
            .orElse(null);

        if (imovel == null) {
            return Result.failure("Imóvel informado não existe.");
        }

        Morador morador = new Morador();
        morador.setNome(dto.getNome());
        morador.setCelular(dto.getCelular());
        morador.setEmail(dto.getEmail());
        morador.setIsProprietario(dto.getIsProprietario());

        LocalTime horaAgora = LocalTime.now();

    	//morador.setDataEntrada(DateTimeUtils.toUTC(dto.getDataEntrada()));
        LocalDateTime dataEntrada = dto.getDataEntrada();
        if (dataEntrada != null) {
            LocalDateTime dataEntradaComHora = LocalDateTime.of(dataEntrada.toLocalDate(), horaAgora);
            morador.setDataEntrada(DateTimeUtils.toUTC(dataEntradaComHora));
        } else {
            morador.setDataEntrada(null);
        }
        
        //morador.setDataSaida(DateTimeUtils.toUTC(dto.getDataSaida()));
        LocalDateTime dataSaida = dto.getDataSaida();
        if (dataSaida != null) {
            LocalDateTime dataSaidaComHora = LocalDateTime.of(dataSaida.toLocalDate(), horaAgora);
            morador.setDataSaida(DateTimeUtils.toUTC(dataSaidaComHora));
        } else {
            morador.setDataSaida(null);
        }
        
        //morador.setDataInclusao(DateTimeUtils.toUTC(dto.getDataInclusao()));
        morador.setDataInclusao(DateTimeUtils.toUTC(dto.getDataInclusao()));
        LocalDateTime dataInclusao = dto.getDataInclusao();
        if (dataInclusao != null) {
            LocalDateTime dataInclusaoComHora = LocalDateTime.of(dataInclusao.toLocalDate(), horaAgora);
            morador.setDataInclusao(DateTimeUtils.toUTC(dataInclusaoComHora));
        } else {
            morador.setDataInclusao(null);
        }
        
        //morador.setDataAlteracao(DateTimeUtils.toUTC(dto.getDataAlteracao()));
        LocalDateTime dataAlteracao = dto.getDataAlteracao();
        if (dataAlteracao != null) {
            LocalDateTime dataAlteracaoComHora = LocalDateTime.of(dataAlteracao.toLocalDate(), horaAgora);
            morador.setDataAlteracao(DateTimeUtils.toUTC(dataAlteracaoComHora));
        } else {
            morador.setDataAlteracao(null);
        }

        morador.setImovel(imovel);

        morador = moradorRepository.save(morador);

        dto.setId(morador.getId());
        dto.setImovelDto(new ImovelDTO(imovel));
        return Result.success(dto, "Morador criado com sucesso.");
    }


    @Transactional
    public Result<Void> update(long id, MoradorDTO dto) {
        Imovel imovel = imovelRepository.findById(dto.getImovelId())
            .orElse(null);

        if (imovel == null) {
            return Result.failure("Imóvel informado não existe.");
        }

        Morador morador = moradorRepository.findById(id)
            .orElse(null);

        if (morador == null) {
            return Result.failure("Morador não encontrado.");
        }

        morador.setNome(dto.getNome());
        morador.setCelular(dto.getCelular());
        morador.setEmail(dto.getEmail());
        morador.setIsProprietario(dto.getIsProprietario());
        
        LocalTime horaAgora = LocalTime.now();

        //morador.setDataEntrada(DateTimeUtils.toUTC(dto.getDataEntrada()));
        LocalDateTime dataEntrada = dto.getDataEntrada();
        if (dataEntrada != null) {
            LocalDateTime dataEntradaComHora = LocalDateTime.of(dataEntrada.toLocalDate(), horaAgora);
            morador.setDataEntrada(DateTimeUtils.toUTC(dataEntradaComHora));
        } else {
            morador.setDataEntrada(null);
        }
        
        //morador.setDataSaida(DateTimeUtils.toUTC(dto.getDataSaida()));
        LocalDateTime dataSaida = dto.getDataSaida();
        if (dataSaida != null) {
            LocalDateTime dataSaidaComHora = LocalDateTime.of(dataSaida.toLocalDate(), horaAgora);
            morador.setDataSaida(DateTimeUtils.toUTC(dataSaidaComHora));
        } else {
            morador.setDataSaida(null);
        }
        
        //morador.setDataInclusao(DateTimeUtils.toUTC(morador.getDataInclusao()));
        morador.setDataInclusao(DateTimeUtils.toUTC(morador.getDataInclusao()));
        LocalDateTime dataInclusao = dto.getDataInclusao();
        if (dataInclusao != null) {
            LocalDateTime dataInclusaoComHora = LocalDateTime.of(dataInclusao.toLocalDate(), horaAgora);
            morador.setDataInclusao(DateTimeUtils.toUTC(dataInclusaoComHora));
        } else {
            morador.setDataInclusao(null);
        }
        
        //morador.setDataAlteracao(DateTimeUtils.toUTC(dto.getDataAlteracao()));
        LocalDateTime dataAlteracao = dto.getDataAlteracao();
        if (dataAlteracao != null) {
            LocalDateTime dataAlteracaoComHora = LocalDateTime.of(dataAlteracao.toLocalDate(), horaAgora);
            morador.setDataAlteracao(DateTimeUtils.toUTC(dataAlteracaoComHora));
        } else {
            morador.setDataAlteracao(null);
        }

        morador.setImovel(imovel);

        moradorRepository.save(morador);

        return Result.success("Morador atualizado com sucesso.");
    }

    @Transactional
    public Result<Void> delete(long id) {
        boolean exists = moradorRepository.existsById(id);
        if (!exists) {
            return Result.failure("Morador não encontrado.");
        }

        try {
            moradorRepository.deleteById(id);
            return Result.success("Morador deletado com sucesso.");
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Não foi possível excluir o morador. Pode estar vinculado a outros registros.");
        }
    }

    @Transactional(readOnly = true)
    public boolean existsByMoradorId(long id) {
        return moradorRepository.existsById(id);
    }
}
