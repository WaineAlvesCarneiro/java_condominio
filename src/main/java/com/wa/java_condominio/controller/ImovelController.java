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

import com.wa.java_condominio.dto.ImovelDTO;
import com.wa.java_condominio.dto.PagedResultDto;
import com.wa.java_condominio.services.ImovelService;
import com.wa.java_condominio.services.shared.Result;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "imovel")
public class ImovelController {

    @Autowired
    private ImovelService imovelService;

    @GetMapping
    public ResponseEntity<Result<List<ImovelDTO>>> getAll() {
        Result<List<ImovelDTO>> resultado = imovelService.getAll();
        
        return ResponseEntity.ok(resultado);
    }

    @GetMapping("/paginado")
    public ResponseEntity<Result<PagedResultDto<ImovelDTO>>> getAllPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "bloco") String orderBy,
            @RequestParam(defaultValue = "ASC") String direction) {
        Result<PagedResultDto<ImovelDTO>> resultado = imovelService.getAllPaged(page, pageSize, orderBy, direction);
        
        return ResponseEntity.ok(resultado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Result<ImovelDTO>> getById(@PathVariable Long id) {
        Result<ImovelDTO> resultado = imovelService.getById(id);

        if (!resultado.isSucesso()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resultado);
        }

        return ResponseEntity.ok(resultado);
    }

    @PostMapping
    public ResponseEntity<Result<ImovelDTO>> add(@RequestBody @Valid ImovelDTO dto) {
        Result<ImovelDTO> resultado = imovelService.add(dto);
        
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(resultado.getDados().getId())
                .toUri();

        return ResponseEntity.created(uri).body(resultado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Result<Void>> update(@PathVariable Long id, @RequestBody @Valid ImovelDTO dto) {
        Result<Void> resultado = imovelService.update(id, dto);

        if (!resultado.isSucesso()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resultado);
        }

        return ResponseEntity.ok(resultado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Result<Void>> delete(@PathVariable Long id) {
        Result<Void> resultado = imovelService.delete(id);

        if (!resultado.isSucesso()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultado);
        }

        return ResponseEntity.noContent().build();
    }
}
