package com.example.sistemaacademico.controller;

import com.example.sistemaacademico.model.Disciplina;
import com.example.sistemaacademico.service.DisciplinaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/disciplina")
public class DisciplinaController {

    private final DisciplinaService disciplinaService;

    public DisciplinaController(DisciplinaService disciplinaService) {
        this.disciplinaService = disciplinaService;
    }

    // Listar todas as disciplinas
    @GetMapping("/list")
    public String listarDisciplinas(Model model) {
        model.addAttribute("disciplinas", disciplinaService.listarTodas());
        return "disciplina/list"; // Página disciplina/list.html
    }

    // Exibir o formulário de adicionar disciplina
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("disciplina", new Disciplina());
        return "disciplina/add"; // Página disciplina/add.html
    }

    // Adicionar disciplina
    @PostMapping("/add")
    public String adicionarDisciplina(@ModelAttribute Disciplina disciplina, Model model) {
        disciplinaService.salvar(disciplina);
        model.addAttribute("success", "Disciplina adicionada com sucesso!");
        return "redirect:/disciplina/list";
    }

    // Exibir o formulário de edição de disciplina
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long disciplinaId, Model model) {
        Optional<Disciplina> disciplinaOpt = disciplinaService.buscarPorId(disciplinaId);
        if (disciplinaOpt.isEmpty()) {
            model.addAttribute("error", "Disciplina não encontrada.");
            return "redirect:/disciplina/list";
        }
        model.addAttribute("disciplina", disciplinaOpt.get()); // Passa a disciplina para edição
        return "disciplina/edit"; // Página disciplina/edit.html
    }

    // Editar disciplina
    @PostMapping("/edit/{id}")
    public String editarDisciplina(@PathVariable("id") Long disciplinaId, @ModelAttribute Disciplina disciplina, Model model) {
        Optional<Disciplina> disciplinaExistente = disciplinaService.buscarPorId(disciplinaId);
        if (disciplinaExistente.isPresent()) {
            Disciplina disciplinaAtualizada = disciplinaExistente.get();
            disciplinaAtualizada.setNome(disciplina.getNome());
            disciplinaService.salvar(disciplinaAtualizada); // Salva as alterações
            model.addAttribute("success", "Disciplina atualizada com sucesso!");
        } else {
            model.addAttribute("error", "Disciplina não encontrada.");
        }
        return "redirect:/disciplina/list";
    }

    // Excluir disciplina
    @GetMapping("/delete/{id}")
    public String excluirDisciplina(@PathVariable("id") Long disciplinaId, Model model) {
        disciplinaService.excluirPorId(disciplinaId);
        model.addAttribute("success", "Disciplina excluída com sucesso!");
        return "redirect:/disciplina/list";
    }
}