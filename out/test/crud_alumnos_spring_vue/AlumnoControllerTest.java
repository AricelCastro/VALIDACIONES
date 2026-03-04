package com.israel.alumnos.controllers;
import com.israel.alumnos.model.Alumno;
import com.israel.alumnos.repository.AlumnoRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;


import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.startsWith;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.DynamicTest.stream;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


@WebMvcTest(AlumnoController.class)
class AlumnoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AlumnoRepository alumnoRepository;
    @Autowired
private ObjectMapper objectMapper;

   @Test
    void debeTraerTodosLosAlumnos() throws Exception {

        Alumno alumno1 = new Alumno();
        alumno1.setId(1L);
        alumno1.setNombre("Israel");
        alumno1.setCarrera("Sistemas");

        Alumno alumno2 = new Alumno();
        alumno2.setId(2L);
        alumno2.setNombre("Juan");
        alumno2.setCarrera("Informatica");

        when(alumnoRepository.findAll())
                .thenReturn(List.of(alumno1, alumno2));

        mockMvc.perform(get("/alumnos/traer-alumnos")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].nombre", is("Israel")));
    }

  @Test
void debeRetornarListaVacia() throws Exception {

    when(alumnoRepository.findAll())
            .thenReturn(List.of());

    mockMvc.perform(get("/alumnos/traer-alumnos")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(0)));
}

@Test
void debeRetornarAlumnoCorrecto() throws Exception {

    Alumno alumno = new Alumno();
    alumno.setId(1L);
    alumno.setNombre("Carlos");
    alumno.setCarrera("Derecho");

    when(alumnoRepository.findAll())
            .thenReturn(List.of(alumno));

    mockMvc.perform(get("/alumnos/traer-alumnos")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].nombre", is("Carlos")));
}
@Test
void debeVerificarCarreraDelPrimerAlumno() throws Exception {

    Alumno alumno1 = new Alumno();
    alumno1.setId(1L);
    alumno1.setNombre("Luis");
    alumno1.setCarrera("Ingeniería");

    Alumno alumno2 = new Alumno();
    alumno2.setId(2L);
    alumno2.setNombre("María");
    alumno2.setCarrera("Arquitectura");

    when(alumnoRepository.findAll())
            .thenReturn(List.of(alumno1, alumno2));

    mockMvc.perform(get("/alumnos/traer-alumnos")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0].carrera", is("Ingeniería")));
}
@Test
void EsperamosMasElementos() throws Exception {

    Alumno alumno = new Alumno();
    alumno.setId(1L);
    alumno.setNombre("Ana");
    alumno.setCarrera("Medicina");

    when(alumnoRepository.findAll())
            .thenReturn(List.of(alumno));

    mockMvc.perform(get("/alumnos/traer-alumnos")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2))); // error
} 

@Test 
public void debeInsertarUnAlumno()throws Exception{
    Alumno alumnoNuevo = new Alumno();
    alumnoNuevo.setNombre("ROLDAN");
    alumnoNuevo.setNumeroControl("226200");

    when(alumnoRepository.save(org.mockito.ArgumentMatchers.any(Alumno.class))).thenReturn(alumnoNuevo);
    mockMvc.perform(post("/alumnos/insertar-alumnos")
    .contentType(MediaType.APPLICATION_JSON)
    .content(objectMapper.writeValueAsString(alumnoNuevo)))
    .andExpect(status().isOk())
    .andExpect(jsonPath("$.nombre",is("ROLDAN")));
}

@Test
void debeEditarUnAlumno() throws Exception {

    Long id = 1L;

    Alumno alumnoExistente = new Alumno();
    alumnoExistente.setId(id);
    alumnoExistente.setNombre("ROLDAN");
    alumnoExistente.setNumeroControl("226200100");

    Alumno alumnoActualizado = new Alumno();
    alumnoActualizado.setId(id);
    alumnoActualizado.setNombre("ROLDAN EDITADO");
    alumnoActualizado.setNumeroControl("226200100");

    when(alumnoRepository.findById(id))
            .thenReturn(java.util.Optional.of(alumnoExistente));

    when(alumnoRepository.save(org.mockito.ArgumentMatchers.any(Alumno.class)))
            .thenReturn(alumnoActualizado);

    mockMvc.perform(put("/alumnos/editar-alumnos/{id}", id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(alumnoActualizado)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.nombre", is("ROLDAN EDITADO")));
}
@Test
public void debeEliminarUnAlumno() throws Exception {

    Long idParaEliminar = 1L;

    mockMvc.perform(delete("/alumnos/eliminar-alumnos/{id}", idParaEliminar)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

    verify(alumnoRepository, times(1)).deleteById(idParaEliminar);
}


}
