package com.example.sistemaacademico.controller;

import com.example.sistemaacademico.dto.MatriculaDTO;
import com.example.sistemaacademico.model.Aluno;
import com.example.sistemaacademico.model.Disciplina;
import com.example.sistemaacademico.model.Matricula;
import com.example.sistemaacademico.service.AlunoService;
import com.example.sistemaacademico.service.DisciplinaService;
import com.example.sistemaacademico.service.MatriculaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/matricula")
public class MatriculaController {

    private final MatriculaService matriculaService;
    private final AlunoService alunoService;  // Injetando AlunoService
    private final DisciplinaService disciplinaService;  // Injetando DisciplinaService

    public MatriculaController(MatriculaService matriculaService, AlunoService alunoService, DisciplinaService disciplinaService) {
        this.matriculaService = matriculaService;
        this.alunoService = alunoService;  // Injetando no construtor
        this.disciplinaService = disciplinaService;  // Injetando no construtor
    }

    // Carrega o formulário de adição de matrícula ao clicar no botão "Matricular"
    @GetMapping("/add")
    public String showMatriculaForm(Model model) {
        model.addAttribute("alunos", alunoService.listarTodos());
        model.addAttribute("disciplinas", disciplinaService.listarTodas());
        model.addAttribute("matricula", new Matricula());
        return "matricula/add"; // Carrega a página de formulário de matrícula
    }

    @PostMapping("/add")
    public String matricularAluno(@RequestParam Long alunoId, @RequestParam Long disciplinaId, Model model) {
        Matricula matricula = new Matricula();
        matricula.setAlunoId(alunoId);
        matricula.setDisciplinaId(disciplinaId);

        matriculaService.matricularAluno(matricula);
        model.addAttribute("success", "Aluno matriculado com sucesso!");
        return "redirect:/matricula/list";
    }

    @GetMapping("/list")
    public String listarMatriculas(Model model) {
        // Carrega todas as matrículas
        List<Matricula> matriculas = matriculaService.listarTodas();

        // Cria uma lista de MatriculaDTO com os nomes dos alunos e disciplinas
        List<MatriculaDTO> matriculaDTOs = matriculas.stream().map(matricula -> {
            Aluno aluno = alunoService.buscarPorId(matricula.getAlunoId()).orElse(null);
            Disciplina disciplina = disciplinaService.buscarPorId(matricula.getDisciplinaId()).orElse(null);

            String alunoNome = (aluno != null) ? aluno.getNome() : "Aluno desconhecido";
            String disciplinaNome = (disciplina != null) ? disciplina.getNome() : "Disciplina desconhecida";

            return new MatriculaDTO(matricula.getId(), alunoNome, disciplinaNome);
        }).collect(Collectors.toList());

        // Adiciona a lista de DTOs ao modelo
        model.addAttribute("matriculas", matriculaDTOs);
        return "matricula/list";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long matriculaId, Model model) {
        Optional<Matricula> matricula = matriculaService.buscarPorId(matriculaId);
        if (matricula.isPresent()) {
            model.addAttribute("matricula", matricula.get());
            List<Aluno> alunos = alunoService.listarTodos();
            List<Disciplina> disciplinas = disciplinaService.listarTodas();
            model.addAttribute("alunos", alunos);
            model.addAttribute("disciplinas", disciplinas);
            return "matricula/edit";
        } else {
            model.addAttribute("error", "Matrícula não encontrada.");
            return "redirect:/matricula/list";
        }
    }

    @PostMapping("/edit/{id}")
    public String editarMatricula(@PathVariable("id") Long matriculaId, @ModelAttribute Matricula matricula, Model model) {
        matriculaService.atualizarMatricula(matriculaId, matricula);
        model.addAttribute("success", "Matrícula atualizada com sucesso!");
        return "redirect:/matricula/list";
    }

    @GetMapping("/delete/{id}")
    public String deletarMatricula(@PathVariable("id") Long matriculaId, Model model) {
        matriculaService.deletarMatricula(matriculaId);
        model.addAttribute("success", "Matrícula excluída com sucesso!");
        return "redirect:/matricula/list";
    }

    @GetMapping("/listByAluno/{id}")
    public String listarDisciplinasPorAluno(@PathVariable("id") Long alunoId, Model model) {
        Aluno aluno = alunoService.buscarPorId(alunoId).orElse(null);
        if (aluno == null) {
            model.addAttribute("error", "Aluno não encontrado.");
            return "redirect:/aluno/list";
        }

        List<Disciplina> disciplinas = matriculaService.buscarDisciplinasPorAluno(alunoId)
                .stream()
                .filter(disciplina -> disciplina != null)  // Filtra disciplinas excluídas
                .collect(Collectors.toList());

        if (disciplinas.isEmpty()) {
            model.addAttribute("error", "Nenhuma disciplina encontrada para este aluno.");
        } else {
            model.addAttribute("disciplinas", disciplinas);
        }

        model.addAttribute("aluno", aluno);
        return "matricula/listByAluno";
    }

    @GetMapping("/listByDisciplina/{id}")
    public String listarAlunosPorDisciplina(@PathVariable("id") Long disciplinaId, Model model) {
        Optional<Disciplina> disciplinaOpt = disciplinaService.buscarPorId(disciplinaId);
        if (disciplinaOpt.isEmpty()) {
            model.addAttribute("error", "Disciplina não encontrada.");
            return "redirect:/disciplina/list";
        }

        Disciplina disciplina = disciplinaOpt.get();
        List<Aluno> alunos = matriculaService.buscarAlunosPorDisciplina(disciplinaId);

        if (alunos.isEmpty()) {
            model.addAttribute("error", "Nenhum aluno matriculado nesta disciplina.");
        } else {
            model.addAttribute("alunos", alunos);
        }

        model.addAttribute("disciplina", disciplina);
        return "matricula/listByDisciplina"; // Página de lista de alunos por disciplina
    }

}