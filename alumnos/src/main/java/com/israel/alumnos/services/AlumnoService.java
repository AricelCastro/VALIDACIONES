package com.israel.alumnos.services;

import com.israel.alumnos.model.Alumno;
import com.israel.alumnos.repository.AlumnoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
@Service
public class AlumnoService {
    @Autowired
    private AlumnoRepository alumnoRepository;
    public List<Alumno> obtenerTodos() {
        return alumnoRepository.findAll();
    }
    public Optional<Alumno> obtenerPorId(Long id) {
        return alumnoRepository.findById(id);
    }
    public Alumno guardarAlumno(Alumno alumno) {
        return alumnoRepository.save(alumno);
    }
    public Optional<Alumno> actualizarAlumno(Long id, Alumno alumnoDetalles) {
        return alumnoRepository.findById(id).map(alumnoExistente -> {
            alumnoExistente.setNombre(alumnoDetalles.getNombre());
            alumnoExistente.setApellido(alumnoDetalles.getApellido());
            alumnoExistente.setEmail(alumnoDetalles.getEmail());
            alumnoExistente.setNumeroContropackage com.israel.alumnos.services;

import com.israel.alumnos.model.Alumno;
import com.israel.alumnos.repository.AlumnoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
@Service
public class AlumnoService {
    @Autowired
    private AlumnoRepository alumnoRepository;
    public List<Alumno> obtenerTodos() {
        return alumnoRepository.findAll();
    }
    public Optional<Alumno> obtenerPorId(Long id) {
        return alumnoRepository.findById(id);
    }
    public Alumno guardarAlumno(Alumno alumno) {
        return alumnoRepository.save(alumno);
    }
    public Optional<Alumno> actualizarAlumno(Long id, Alumno alumnoDetalles) {
        return alumnoRepository.findById(id).map(alumnoExistente -> {
            alumnoExistente.setNombre(alumnoDetalles.getNombre());
            alumnoExistente.setApellido(alumnoDetalles.getApellido());
            alumnoExistente.setEmail(alumnoDetalles.getEmail());
            alumnoExistente.setNumeroControl(alumnoDetalles.getNumeroControl());
            alumnoExistente.setTelefono(alumnoDetalles.getTelefono());
            alumnoExistente.setCarrera(alumnoDetalles.getCarrera());
            alumnoExistente.setImagenURL(alumnoDetalles.getImagenURL());
            return alumnoRepository.save(alumnoExistente);
        });
    }
    public void eliminarAlumno(Long id) {
        alumnoRepository.deleteById(id);
    }
}
l(alumnoDetalles.getNumeroControl());
            alumnoExistente.setTelefono(alumnoDetalles.getTelefono());
            alumnoExistente.setCarrera(alumnoDetalles.getCarrera());
            alumnoExistente.setImagenURL(alumnoDetalles.getImagenURL());
            return alumnoRepository.save(alumnoExistente);
        });
    }
    public void eliminarAlumno(Long id) {
        alumnoRepository.deleteById(id);
    }
}
