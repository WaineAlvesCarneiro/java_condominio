package com.wa.java_condominio.controller;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.wa.java_condominio.dto.ImovelDTO;
import com.wa.java_condominio.services.ImovelService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "imovel")
public class ImovelController {

	@Autowired
	private ImovelService imovelService;
	
	@Operation(summary = "Lista todos os imoveis com paginação.")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso.") })	
	@GetMapping
	public ResponseEntity<Page<ImovelDTO>> findAllPaged(
			@RequestParam(defaultValue = "0") Integer page,
			@RequestParam(defaultValue = "10") Integer linesPerPage,
			@RequestParam(defaultValue = "ASC") String direction,
			@RequestParam(defaultValue = "bloco") String orderBy
			){
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction.toUpperCase()), orderBy.toLowerCase());
		
		Page<ImovelDTO> imovelDTO = imovelService.findAllPaged(pageRequest);
		
		return ResponseEntity.ok().body(imovelDTO);
	}
	
	@Operation(summary = "Busca um imovel por ID.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Imovel encontrado com sucesso."),
			@ApiResponse(responseCode = "404", description = "Imovel não encontrado.")
	})
	@GetMapping(value = "/{id}")
	public ResponseEntity<ImovelDTO> findById(@PathVariable long id){
		ImovelDTO imovelDTO = imovelService.findById(id);
		
		return ResponseEntity.ok().body(imovelDTO);
	}
	
	@Operation(summary = "Cadastrar um Imovel.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Imovel criado com sucesso.")
	})
	@PostMapping
	public ResponseEntity<ImovelDTO> saveImovel(@RequestBody @Valid ImovelDTO imovelDTO){
		imovelDTO = imovelService.saveImovel(imovelDTO);
		
		URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri()
				.path("/{id}").buildAndExpand(imovelDTO.getId()).toUri();
		
		return ResponseEntity.created(uri).body(imovelDTO);
	}
	
	@Operation(summary = "Atualiza os dados do imovel existente.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Imovel autlizado com sucesso."),
			@ApiResponse(responseCode = "404", description = "Imovel não encontrado.")
	})
	@PutMapping(value = "/{id}")
	public ResponseEntity<ImovelDTO> updateImovel(@PathVariable long id, @RequestBody @Valid ImovelDTO imovelDTO){
		imovelDTO = imovelService.updateImovel(id, imovelDTO);
		
		return ResponseEntity.ok().body(imovelDTO);
	}
	
	@Operation(summary = "Deleta imovel existente por ID.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Imovel deletado com sucesso."),
			@ApiResponse(responseCode = "404", description = "Imovel não encontrado.")
	})
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<ImovelDTO> deleteImovel(@PathVariable long id){
		imovelService.deleteImovel(id);
		
		return ResponseEntity.noContent().build();
	}
}
