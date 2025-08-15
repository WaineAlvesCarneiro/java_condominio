package com.wa.java_condominio.controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import com.wa.java_condominio.dto.PagedResultDto;
import com.wa.java_condominio.services.MoradorService;
import com.wa.java_condominio.services.shared.Result;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "morador")
public class MoradorController {

    @Autowired
    private MoradorService moradorService;

    @GetMapping
    public ResponseEntity<Result<List<MoradorDTO>>> getAll() {
        Result<List<MoradorDTO>> resultado = moradorService.getAll();
        
        return ResponseEntity.ok(resultado);
    }
    
    @GetMapping("/paginado")
    public ResponseEntity<Result<PagedResultDto<MoradorDTO>>> findAllPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "nome") String orderBy,
            @RequestParam(defaultValue = "ASC") String direction) {
        Result<PagedResultDto<MoradorDTO>> result = moradorService.getAllPaged(page, pageSize, orderBy, direction);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Result<MoradorDTO>> findById(@PathVariable long id) {
        Result<MoradorDTO> resultado = moradorService.getById(id);

        if (!resultado.isSucesso()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resultado);
        }

        return ResponseEntity.ok(resultado);
    }

    @PostMapping
    public ResponseEntity<Result<MoradorDTO>> add(@Valid @RequestBody MoradorDTO dto) {
        Result<MoradorDTO> resultado = moradorService.add(dto);

        if (!resultado.isSucesso()) {
            return ResponseEntity.badRequest().body(resultado);
        }

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(resultado.getDados().getId())
                .toUri();

        return ResponseEntity.created(uri).body(resultado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Result<Void>> update(@PathVariable long id, @Valid @RequestBody MoradorDTO dto) {
        Result<Void> resultado = moradorService.update(id, dto);

        if (!resultado.isSucesso()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resultado);
        }

        return ResponseEntity.ok(resultado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Result<Void>> delete(@PathVariable long id) {
        Result<Void> resultado = moradorService.delete(id);

        if (!resultado.isSucesso()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resultado);
        }

        return ResponseEntity.noContent().build();
    }
}
