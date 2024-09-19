package com.example.sistemaacademico.service;

import com.example.sistemaacademico.model.Disciplina;
import com.example.sistemaacademico.repository.DisciplinaRepository;
import com.example.sistemaacademico.repository.MatriculaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class DisciplinaService {

    private final DisciplinaRepository disciplinaRepository;
    private final MatriculaRepository matriculaRepository;

    public DisciplinaService(DisciplinaRepository disciplinaRepository, MatriculaRepository matriculaRepository) {
        this.disciplinaRepository = disciplinaRepository;
        this.matriculaRepository = matriculaRepository;
    }

    // Buscar disciplina por ID
    public Optional<Disciplina> buscarPorId(Long id) {
        return disciplinaRepository.findById(id);
    }

    // Salvar ou atualizar disciplina
    public Disciplina salvar(Disciplina disciplina) {
        return disciplinaRepository.save(disciplina);
    }

    // Listar todas as disciplinas
    public List<Disciplina> listarTodas() {
        return disciplinaRepository.findAll();
    }

    // Excluir disciplina e todas as matrículas associadas
    @Transactional
    public void excluirPorId(Long id) {
        // Excluir todas as matrículas associadas à disciplina
        matriculaRepository.deleteByDisciplinaId(id);
        // Excluir a disciplina
        disciplinaRepository.deleteById(id);
    }
}
