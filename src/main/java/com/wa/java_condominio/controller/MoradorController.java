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

import com.wa.java_condominio.dto.MoradorDTO;
import com.wa.java_condominio.services.MoradorService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "morador")
public class MoradorController {

	@Autowired
	private MoradorService moradorService;
	
	@Operation(summary = "Lista todos os moradores com paginação.")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso.") })	
	@GetMapping
	public ResponseEntity<Page<MoradorDTO>> findAllPaged(
			@RequestParam(defaultValue = "0") Integer page,
			@RequestParam(defaultValue = "10") Integer linesPerPage,
			@RequestParam(defaultValue = "ASC") String direction,
			@RequestParam(defaultValue = "nome") String orderBy
			){
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction.toUpperCase()), orderBy.toLowerCase());
		
		Page<MoradorDTO> moradorDTO = moradorService.findAllPaged(pageRequest);
		
		return ResponseEntity.ok().body(moradorDTO);
	}
	
	@Operation(summary = "Busca um morador por ID.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Morador encontrado com sucesso."),
			@ApiResponse(responseCode = "404", description = "Morador não encontrado.")
	})
	@GetMapping(value = "/{id}")
	public ResponseEntity<MoradorDTO> findById(@PathVariable long id){
		MoradorDTO moradorDTO = moradorService.findById(id);
		
		return ResponseEntity.ok().body(moradorDTO);
	}
	
	@Operation(summary = "Cadastrar um morador.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Morador criado com sucesso.")
	})
	@PostMapping
	public ResponseEntity<MoradorDTO> saveMorador(@RequestBody @Valid MoradorDTO moradorDTO){
		moradorDTO = moradorService.saveMorador(moradorDTO);
		
		URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri()
				.path("/{id}").buildAndExpand(moradorDTO.getId()).toUri();
		
		return ResponseEntity.created(uri).body(moradorDTO);
	}
	
	@Operation(summary = "Atualiza os dados do morador existente.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Morador autlizado com sucesso."),
			@ApiResponse(responseCode = "404", description = "Morador não encontrado.")
	})
	@PutMapping(value = "/{id}")
	public ResponseEntity<MoradorDTO> updateMorador(@PathVariable long id, @RequestBody @Valid MoradorDTO moradorDTO){
		moradorDTO = moradorService.updateMorador(id, moradorDTO);
		
		return ResponseEntity.ok().body(moradorDTO);
	}
	
	@Operation(summary = "Deleta morador existente por ID.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Morador deletado com sucesso."),
			@ApiResponse(responseCode = "404", description = "Morador não encontrado.")
	})
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<MoradorDTO> deleteMorador(@PathVariable long id){
		moradorService.deleteMorador(id);
		
		return ResponseEntity.noContent().build();
	}
}
