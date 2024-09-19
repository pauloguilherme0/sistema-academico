package com.example.sistemaacademico.service;

import com.example.sistemaacademico.model.Aluno;
import com.example.sistemaacademico.repository.AlunoRepository;
import com.example.sistemaacademico.repository.MatriculaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AlunoService {

    private final AlunoRepository alunoRepository;
    private final MatriculaRepository matriculaRepository;

    public AlunoService(AlunoRepository alunoRepository, MatriculaRepository matriculaRepository) {
        this.alunoRepository = alunoRepository;
        this.matriculaRepository = matriculaRepository;
    }

    public Optional<Aluno> buscarPorId(Long id) {
        return alunoRepository.findById(id);
    }

    public Aluno salvar(Aluno aluno) {
        return alunoRepository.save(aluno);
    }

    // Anotação @Transactional para garantir que a operação de exclusão esteja dentro de uma transação
    @Transactional
    public void excluirPorId(Long id) {
        // Exclui todas as matrículas associadas ao aluno
        matriculaRepository.deleteByAlunoId(id);
        // Exclui o aluno
        alunoRepository.deleteById(id);
    }

    public List<Aluno> listarTodos() {
        return alunoRepository.findAll();
    }

}