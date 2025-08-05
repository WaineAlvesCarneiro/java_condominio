package com.wa.java_condominio.services;

import java.util.Optional;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wa.java_condominio.dto.ImovelDTO;
import com.wa.java_condominio.model.Imovel;
import com.wa.java_condominio.repositories.ImovelRepository;
import com.wa.java_condominio.services.exceptions.DatabaseException;
import com.wa.java_condominio.services.exceptions.ResourceNotFoundException;

@Service
public class ImovelService {
	@Autowired
	private ImovelRepository imovelRepository;
	
	@Transactional(readOnly = true)
	public List<ImovelDTO> findAll() {
		List<Imovel> lista = imovelRepository.findAll();
		return lista.stream().map(ImovelDTO::new).toList();
	}
	
	@Transactional(readOnly = true)
	public Page<ImovelDTO> findAllPaged(PageRequest pageRequest) {
		Page<Imovel> imoveis = imovelRepository.findAll(pageRequest);
		
		return imoveis.map(x -> new ImovelDTO(x));
	}
	
	@Transactional(readOnly = true)
	public ImovelDTO findById(long id) {
		Optional<Imovel> obj = imovelRepository.findById(id);
		Imovel imovel = obj.orElseThrow(() -> new ResourceNotFoundException("Id não encontrado."));
		return new ImovelDTO(imovel);
	}
	
	@Transactional
	public ImovelDTO saveImovel(ImovelDTO imovelDTO) {
		Imovel imovel = new Imovel();
		imovel.setBloco(imovelDTO.getBloco());
		imovel.setApartamento(imovelDTO.getApartamento());
		imovel.setBoxGaragem(imovelDTO.getBoxGaragem());
		
		imovel = imovelRepository.save(imovel);
		
		return new ImovelDTO(imovel);
	}
	
	@Transactional
	public ImovelDTO updateImovel(long id, ImovelDTO imovelDTO) {
		Imovel imovel = imovelRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Id não encontrado: " + id + "."));
		
		imovel.setBloco(imovelDTO.getBloco());
		imovel.setApartamento(imovelDTO.getApartamento());
		imovel.setBoxGaragem(imovelDTO.getBoxGaragem());
		
		return new ImovelDTO(imovel);
	}
	
	public void deleteImovel(long id) {
		if(!imovelRepository.existsById(id)) {
			throw new ResourceNotFoundException("Id não encontrado.");
		}
		
		try {
			imovelRepository.deleteById(id);
		}
		catch(DataIntegrityViolationException e) {
			throw new DatabaseException("Não foi possível excluir o imovel. Pois pode estar vinculado a outro registro.");
		}
	}
}