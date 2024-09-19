package com.example.sistemaacademico.repository;

import com.example.sistemaacademico.model.Disciplina;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DisciplinaRepository extends JpaRepository<Disciplina, Long> {
}