package com.javadabadu.disney.controller;


import com.javadabadu.disney.exception.ExceptionBBDD;
import com.javadabadu.disney.models.dto.ResponseInfoDTO;
import com.javadabadu.disney.models.entity.Genero;
import com.javadabadu.disney.service.GeneroService;
import com.javadabadu.disney.util.PathGenero;
import com.javadabadu.disney.util.Uri;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.Map;


@RestController
@CrossOrigin("*")
@RequestMapping(value = Uri.GENEROS)
public class GeneroController {

    @Autowired
    private GeneroService generoService;

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findById(@PathVariable Integer id, HttpServletRequest request) {
        try {
            return ResponseEntity.ok().body(generoService.findById(id));
        } catch (ExceptionBBDD e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseInfoDTO(e.getMessage(),request.getRequestURI(), HttpStatus.NOT_FOUND.value()));
        }
    }

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findAll() {
        return ResponseEntity.ok().body(generoService.findAll());
    }

    @PostMapping("/")
    public ResponseEntity<?> lastId(HttpServletRequest request) {
        return ResponseEntity.created(URI.create(request.getRequestURI() + generoService.lastValueId())).body("se creo un registro");
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> create(@RequestBody Genero genero, @PathVariable Integer id, HttpServletRequest request) {
        try {
            return ResponseEntity.ok().body(generoService.save(genero, id));
        } catch (ExceptionBBDD ebd) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseInfoDTO(ebd.getMessage(),request.getRequestURI(), HttpStatus.BAD_REQUEST.value()));
        }
    }

   @PatchMapping(path = "/{id}", consumes = "application/merge-patch+json", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateCustomer(@PathVariable Integer id, @RequestBody Map<String, Object> propiedades, HttpServletRequest request) {
        try {
            Genero searchedGenero = generoService.findById(id);
            PathGenero pathGenero = new PathGenero();
            propiedades.forEach((k, v) -> {
                if (pathGenero.contains(k, searchedGenero))
                    pathGenero.parcharGenero(k, v, searchedGenero);
            });
            return ResponseEntity.status(HttpStatus.OK).body(generoService.update(searchedGenero));

        } catch (ExceptionBBDD ebd) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseInfoDTO(ebd.getMessage(), request.getRequestURI(), HttpStatus.NOT_FOUND.value()));
    }}

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> delete( @PathVariable Integer id) throws Exception {
        return ResponseEntity.ok().body(generoService.softDelete(generoService.findById(id).getId()));

    }

}
