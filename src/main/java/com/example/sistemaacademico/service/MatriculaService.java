package com.example.sistemaacademico.service;

import com.example.sistemaacademico.model.Aluno;
import com.example.sistemaacademico.model.Disciplina;
import com.example.sistemaacademico.model.Matricula;
import com.example.sistemaacademico.repository.MatriculaRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MatriculaService {

    private final MatriculaRepository matriculaRepository;
    private final AlunoService alunoService;
    private final DisciplinaService disciplinaService;

    public MatriculaService(MatriculaRepository matriculaRepository, AlunoService alunoService, DisciplinaService disciplinaService) {
        this.matriculaRepository = matriculaRepository;
        this.alunoService = alunoService;
        this.disciplinaService = disciplinaService;
    }

    public List<Matricula> listarTodas() {
        return matriculaRepository.findAll();
    }

    public Matricula matricularAluno(Matricula matricula) {
        return matriculaRepository.save(matricula);
    }

    public Optional<Matricula> buscarPorId(Long id) {
        return matriculaRepository.findById(id); // Retorna um Optional diretamente
    }

    public void atualizarMatricula(Long id, Matricula matriculaAtualizada) {
        Matricula matricula = matriculaRepository.findById(id).orElseThrow(() -> new RuntimeException("Matrícula não encontrada"));
        matricula.setAlunoId(matriculaAtualizada.getAlunoId());
        matricula.setDisciplinaId(matriculaAtualizada.getDisciplinaId());
        matriculaRepository.save(matricula);
    }

    public void deletarMatricula(Long id) {
        matriculaRepository.deleteById(id);
    }

    public List<Disciplina> buscarDisciplinasPorAluno(Long alunoId) {
        List<Matricula> matriculas = matriculaRepository.findByAlunoId(alunoId);
        return matriculas.stream()
                .map(matricula -> disciplinaService.buscarPorId(matricula.getDisciplinaId()).orElse(null))
                .collect(Collectors.toList());
    }

    public List<Aluno> buscarAlunosPorDisciplina(Long disciplinaId) {
        List<Matricula> matriculas = matriculaRepository.findByDisciplinaId(disciplinaId);
        return matriculas.stream()
                .map(matricula -> alunoService.buscarPorId(matricula.getAlunoId()).orElse(null))
                .collect(Collectors.toList());
    }
}