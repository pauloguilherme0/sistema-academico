package com.example.sistemaacademico.service;

import com.example.sistemaacademico.model.Matricula;
import com.example.sistemaacademico.repository.MatriculaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MatriculaService {

    @Autowired
    private MatriculaRepository matriculaRepository;

    public List<Matricula> findAll() {
        return matriculaRepository.findAll();
    }

    public Optional<Matricula> findById(Long id) {
        return matriculaRepository.findById(id);
    }

    public Matricula save(Matricula matricula) {
        return matriculaRepository.save(matricula);
    }

    public void deleteById(Long id) {
        matriculaRepository.deleteById(id);
    }
}