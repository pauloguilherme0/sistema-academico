package com.example.sistemaacademico.repository;

import com.example.sistemaacademico.model.Matricula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface MatriculaRepository extends JpaRepository<Matricula, Long> {

    List<Matricula> findByDisciplinaId(Long disciplinaId);

    List<Matricula> findByAlunoId(Long alunoId);

    // Anotação @Transactional para garantir que a operação de remoção seja transacional
    @Transactional
    void deleteByAlunoId(Long alunoId);

    // Excluir matrículas por ID da disciplina
    @Transactional
    void deleteByDisciplinaId(Long disciplinaId);
}