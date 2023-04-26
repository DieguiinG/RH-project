package com.diego.ProgramaRh.Controllers;

import com.diego.ProgramaRh.models.Vaga;
import com.diego.ProgramaRh.repository.CandidatoRepository;
import com.diego.ProgramaRh.repository.VagaRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class VagaController {

    @Autowired
    VagaRepository vr;

    @Autowired
    CandidatoRepository cr;

    //--------------------------------- Cadastrar Vaga ------------------------//
    @GetMapping(value = "/cadastrarVaga")
    public String form() {
        return "vaga/formVaga";
    }

    @PostMapping(value = "/cadastrarVaga")
    public String form(@Valid Vaga vaga, BindingResult result, RedirectAttributes attributes) {

        if (result.hasErrors()) {
            attributes.addFlashAttribute("mensagem", "Verifique os campos...");
            return "redirect:/cadastrarVaga";
        }
        vr.save(vaga);
        attributes.addFlashAttribute("mensagem", "Vaga cadastrada com sucesso!");
        return "redirect:/cadastrarVaga";
    }
}
