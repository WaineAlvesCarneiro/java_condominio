package com.wa.java_condominio.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wa.java_condominio.dto.MoradorDTO;
import com.wa.java_condominio.model.Imovel;
import com.wa.java_condominio.model.Morador;
import com.wa.java_condominio.repositories.ImovelRepository;
import com.wa.java_condominio.repositories.MoradorRepository;
import com.wa.java_condominio.services.exceptions.DatabaseException;
import com.wa.java_condominio.services.exceptions.ResourceNotFoundException;

@Service
public class MoradorService {

	@Autowired
	private MoradorRepository moradorRepository;
	
	@Autowired
	private ImovelRepository imovelRepository;
	
	@Transactional(readOnly = true)
	public Page<MoradorDTO> findAllPaged(PageRequest pageRequest){
		Page<Morador> moradores = moradorRepository.findAll(pageRequest);
		
		return moradores.map(x -> new MoradorDTO(x, x.getImovel()));
	}
	
	@Transactional(readOnly = true)
	public MoradorDTO findById(long id) {
		Optional<Morador> obj = moradorRepository.findById(id);		
		Morador morador = obj.orElseThrow(() -> new ResourceNotFoundException("Id não enontrado."));
		
		return new MoradorDTO(morador, morador.getImovel());
	}
	
	@Transactional
	public MoradorDTO saveMorador(MoradorDTO moradorDTO) {
		Morador morador = new Morador();
		
		morador.setNome(moradorDTO.getNome());
		morador.setCelular(moradorDTO.getCelular());
		morador.setEmail(moradorDTO.getEmail());
		morador.setIsProprietario(moradorDTO.getIsProprietario());
		morador.setDataEntrada(moradorDTO.getDataEntrada());

		Imovel imovel = imovelRepository.getReferenceById(moradorDTO.getImovel().getId());		
		morador.setImovel(imovel);
		
		morador = moradorRepository.save(morador);
		
		return new MoradorDTO(morador, morador.getImovel());
	}
	
	@Transactional
	public MoradorDTO updateMorador(long id, MoradorDTO moradorDTO) {
		Morador morador = moradorRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Id não encontrado" + id + "."));
		
		morador.setNome(moradorDTO.getNome());
		morador.setCelular(moradorDTO.getCelular());
		morador.setEmail(moradorDTO.getEmail());
		morador.setIsProprietario(moradorDTO.getIsProprietario());
		morador.setDataEntrada(moradorDTO.getDataEntrada());
		morador.setDataSaida(moradorDTO.getDataSaida());

		Imovel imovel = imovelRepository.getReferenceById(moradorDTO.getImovel().getId());		
		morador.setImovel(imovel);
		
		return new MoradorDTO(morador, morador.getImovel());
	}
	
	public void deleteMorador(long id) {
		if(!moradorRepository.existsById(id)) {
			throw new ResourceNotFoundException("Id não encontrado: " + id + ".");
		}
		
		try {
			moradorRepository.deleteById(id);			
		} catch (DataIntegrityViolationException e) {
			throw new DatabaseException("Não foi possível excluir a produto. Pois pode estar vinculada a outros regristros.");
		}
	}
}