package com.example.sistemaacademico.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MatriculaDTO {

    private Long id;
    private String alunoNome;
    private String disciplinaNome;

    public MatriculaDTO(Long id, String alunoNome, String disciplinaNome) {
        this.id = id;
        this.alunoNome = alunoNome;
        this.disciplinaNome = disciplinaNome;
    }

}