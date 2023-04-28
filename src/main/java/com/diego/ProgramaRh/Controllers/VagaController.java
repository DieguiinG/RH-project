package com.diego.ProgramaRh.Controllers;

import com.diego.ProgramaRh.models.Candidato;
import com.diego.ProgramaRh.models.Vaga;
import com.diego.ProgramaRh.repository.CandidatoRepository;
import com.diego.ProgramaRh.repository.VagaRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
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
    //--------------------------------- Listar Vaga ------------------------//

    @RequestMapping("/vagas")
    public ModelAndView listaVagas() {
        ModelAndView mv = new ModelAndView("/vaga/listaVaga");
        Iterable<Vaga> vagas = vr.findAll();
        mv.addObject("vagas", vagas);
        return mv;
    }

    //--------------------------------- Detalhes Vaga ------------------------//
    @GetMapping(value = "{codigo}")
    public ModelAndView detalhesVaga(@PathVariable("codigo") long codigo) {
        Vaga vaga = vr.findByCodigo(codigo);
        ModelAndView mv = new ModelAndView("vaga/detalhesVaga");
        mv.addObject("vaga", vaga);

        Iterable<Candidato> candidatos = cr.findByVaga(vaga);
        mv.addObject("candidatos", candidatos);
        return mv;

    }

    //--------------------------------- Deletar Vaga ------------------------//

    @RequestMapping("/deletarVaga")
    public String deletarVaga(long codigo) {
        Vaga vaga = vr.findByCodigo(codigo);
        vr.delete(vaga);
        return "redirect:/vagas";

    }

    //--------------------------------- Adicionar Candidato ------------------------//

    @PostMapping(value = "/{codigo}")
    public String detalhesVagaPost(@PathVariable("codigo") long codigo, @Valid Candidato candidato, BindingResult result,
                                   RedirectAttributes attributes) {

        if (result.hasErrors()) {
            attributes.addFlashAttribute("mensagem", "Verifique os campos...");
            return "redirect:/{codigo}";
        }

        //RG duplicado
        if (cr.findByRg(candidato.getRg()) != null) {
            attributes.addFlashAttribute("mensagem", "RG duplicado");
            {
                return "redirect:/{codigo}";
            }
        }

        Vaga vaga = vr.findByCodigo(codigo);
        candidato.setVaga(vaga);
        cr.save(candidato);
        attributes.addFlashAttribute("mensagem", "Candidato adicionado com sucesso!");
        return "redirect:/{codigo}";

    }
    //--------------------------------- Deletar Candidato pelo RG ------------------------//

    @RequestMapping("/deletarCandidato")
    public String deletarCandidato(String rg) {
        Candidato candidato = cr.findByRg(rg);
        Vaga vaga = candidato.getVaga();
        String codigo = "" + vaga.getCodigo();

        cr.delete(candidato);
        return "redirect:/" + codigo;
    }
//--------------------------------- MÉTODOS UPDATE ------------------------//

    //Formulário para editar vaga

    @GetMapping(value = "/editar-vaga")
    public ModelAndView editarVaga(long codigo) {
        Vaga vaga = vr.findByCodigo(codigo);
        ModelAndView mv = new ModelAndView("vaga/update-vaga");
        mv.addObject("vaga", vaga);
        return mv;
    }

    //UPDATE vaga
    @PostMapping(value = "/editar-vaga")
    public String uptadeVaga(@Valid Vaga vaga, BindingResult result, RedirectAttributes attributes) {
        vr.save(vaga);
        attributes.addFlashAttribute("sucess", "Vaga alterada com sucesso!");

        long codigoLong = vaga.getCodigo();
        String codigo = "" + codigoLong;
        return "redirect:/" + codigo;
    }
}