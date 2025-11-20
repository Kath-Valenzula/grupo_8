package com.demo.demo.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.demo.demo.models.Receta;
import com.demo.demo.service.RecetaService;

import jakarta.validation.Valid;

@Controller
public class RecetasController {

    private final RecetaService recetaService;

    public RecetasController(RecetaService recetaService) {
        this.recetaService = recetaService;
    }

    @GetMapping({"/", "/home"})
    public String home(Model model) {
        List<Receta> recetasPopulares = recetaService.listarPopulares();
        model.addAttribute("recetas", recetasPopulares);
        return "home";
    }

    @GetMapping("/recetas")
    public String recetas(Model model) {
        List<Receta> todasRecetas = recetaService.buscar(null, null, null, null, null, null);
        model.addAttribute("recetas", todasRecetas);
        return "recetas";
    }

    @GetMapping("/recetas/buscar")
    public String buscar(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String tipoCocina,
            @RequestParam(required = false) String ingredientes,
            @RequestParam(required = false) String paisOrigen,
            @RequestParam(required = false) String dificultad,
            @RequestParam(required = false) Boolean popular,
            Model model
    ) {
        List<Receta> recetas = recetaService.buscar(nombre, tipoCocina, ingredientes, paisOrigen, dificultad, popular);
        model.addAttribute("recetas", recetas);
        return "recetas";
    }

    @GetMapping("/recetas/{id}")
    public String detalle(@PathVariable Long id, Model model) {
        return recetaService.obtenerPorId(id)
                .map(receta -> {
                    model.addAttribute("receta", receta);
                    return "receta-detalle";
                })
                .orElse("redirect:/recetas");
    }

    @PostMapping("/recetas")
    public String crear(@Valid Receta receta, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("recetas", recetaService.listarTodas());
            return "recetas";
        }
        
        recetaService.guardar(receta);
        return "redirect:/recetas";
    }
}