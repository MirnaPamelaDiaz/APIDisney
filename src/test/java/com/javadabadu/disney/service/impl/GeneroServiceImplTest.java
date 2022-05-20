package com.javadabadu.disney.service.impl;


import com.javadabadu.disney.data.GeneroData;
import com.javadabadu.disney.exception.ExceptionBBDD;
import com.javadabadu.disney.models.entity.Genero;
import com.javadabadu.disney.models.mapped.ModelMapperDTOImp;
import com.javadabadu.disney.repository.GeneroRepository;
import com.javadabadu.disney.service.GeneroService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class GeneroServiceImplTest {

    @Autowired
    private GeneroService generoService;

    @MockBean
    private ModelMapperDTOImp mapperDTO;

    @MockBean
    private GeneroRepository generoRepository;

    @Test
    void findByIdTest() throws ExceptionBBDD {

        Optional<Genero> generoOptional = Optional.of(GeneroData.crearGeneroUno());

        when(generoRepository.findById(1)).thenReturn(generoOptional);
        when(mapperDTO.generoToResponseDTO(GeneroData.crearGeneroUno())).thenReturn(GeneroData.crearGeneroDTOUno());

        assertNotNull(generoService.findById(1));

        assertEquals(1, generoService.findById(1).getId());
        assertEquals("Terror", generoService.findById(1).getNombre());

        assertThrows(ExceptionBBDD.class, () -> {
            generoService.findById(500);
        });
    }

    @Test
    void findAllTest() throws ExceptionBBDD {

        when(generoRepository.findAll(Sort.by(Sort.Direction.ASC, "id"))).thenReturn(GeneroData.crearListaGeneros());
        when(mapperDTO.listGeneroToResponseDTO(GeneroData.crearListaGeneros())).thenReturn(GeneroData.crearListaGenerosDto());

        assertNotNull(generoService.findAll());

        assertEquals(1, generoService.findAll().get(0).getId());
        assertEquals(2, generoService.findAll().get(1).getId());

        assertEquals("Terror", generoService.findAll().get(0).getNombre());
        assertEquals("Comedia", generoService.findAll().get(1).getNombre());

        assertTrue(generoService.findAll().size() > 1);

        verify(generoRepository, times(6)).findAll(Sort.by(Sort.Direction.ASC, "id"));

    }

    @Test
    void saveTest() throws ExceptionBBDD {

        when(generoRepository.save(any())).thenReturn(GeneroData.crearGeneroUno());
        when(mapperDTO.generoToResponseDTO(any())).thenReturn(GeneroData.crearGeneroDTOUno());

        assertEquals("Terror", generoService.save(GeneroData.crearGeneroUno()).getNombre());
        assertEquals(1, generoService.save(GeneroData.crearGeneroUno()).getId());

        verify(generoRepository, times(2)).save(any());
        verify(mapperDTO, times(2)).generoToResponseDTO(any());
    }

    @Test
    void softDeleteTest() throws ExceptionBBDD {

        when(generoRepository.softDelete(1)).thenReturn(true);

        assertEquals("Se elimino el genero seleccionado", generoService.softDelete(1));

        assertThrows(ExceptionBBDD.class, () -> {
            generoService.softDelete(100);
        });
    }

    @Test
    void existsByIdTest() throws ExceptionBBDD {

        when(generoRepository.existsById(1)).thenReturn(true);

        assertTrue(generoService.existsById(1));
        assertFalse(generoService.existsById(100));
    }

    @Test
    void lastValueIdTest() throws ExceptionBBDD {

        when(generoRepository.lastValueId()).thenReturn(5);
        assertEquals(5, generoService.lastValueId());

        when(generoRepository.lastValueId()).thenReturn(-1);

        assertThrows(ExceptionBBDD.class, () -> {
            generoService.lastValueId();
        });
    }


}
