package com.example.sistemaacademico.controller;

import com.example.sistemaacademico.model.Aluno;
import com.example.sistemaacademico.service.AlunoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/aluno")
public class AlunoController {

    private final AlunoService alunoService;

    public AlunoController(AlunoService alunoService) {
        this.alunoService = alunoService;
    }

    // Listar todos os alunos
    @GetMapping("/list")
    public String listarAlunos(Model model) {
        List<Aluno> alunos = alunoService.listarTodos();
        model.addAttribute("alunos", alunos);
        return "aluno/list"; // Página aluno/list.html
    }

    // Exibir formulário de adicionar aluno
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("aluno", new Aluno()); // Adiciona um objeto aluno vazio
        return "aluno/add"; // Página aluno/add.html
    }

    // Adicionar um aluno
    @PostMapping("/add")
    public String adicionarAluno(@ModelAttribute Aluno aluno, Model model) {
        alunoService.salvar(aluno);
        model.addAttribute("success", "Aluno adicionado com sucesso!");
        return "redirect:/aluno/list"; // Redireciona para a lista de alunos
    }

    // Exibir formulário de edição de aluno
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long alunoId, Model model) {
        Optional<Aluno> alunoOpt = alunoService.buscarPorId(alunoId);
        if (alunoOpt.isEmpty()) {
            model.addAttribute("error", "Aluno não encontrado.");
            return "redirect:/aluno/list";
        }
        model.addAttribute("aluno", alunoOpt.get()); // Passa o aluno existente para a edição
        return "aluno/edit"; // Página aluno/edit.html
    }

    // Editar um aluno
    @PostMapping("/edit/{id}")
    public String editarAluno(@PathVariable("id") Long alunoId, @ModelAttribute Aluno aluno, Model model) {
        Optional<Aluno> alunoExistente = alunoService.buscarPorId(alunoId);
        if (alunoExistente.isPresent()) {
            Aluno alunoAtualizado = alunoExistente.get();
            alunoAtualizado.setNome(aluno.getNome());
            alunoAtualizado.setEmail(aluno.getEmail());
            alunoAtualizado.setMatricula(aluno.getMatricula());
            alunoService.salvar(alunoAtualizado); // Salva as alterações
            model.addAttribute("success", "Aluno atualizado com sucesso!");
        } else {
            model.addAttribute("error", "Aluno não encontrado.");
        }
        return "redirect:/aluno/list";
    }

    // Excluir um aluno
    @GetMapping("/delete/{id}")
    public String excluirAluno(@PathVariable("id") Long alunoId, Model model) {
        alunoService.excluirPorId(alunoId);
        model.addAttribute("success", "Aluno excluído com sucesso!");
        return "redirect:/aluno/list"; // Redireciona para a lista de alunos
    }
}