package com.example.sistemaacademico.repository;

import com.example.sistemaacademico.model.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlunoRepository extends JpaRepository<Aluno, Long> {
}