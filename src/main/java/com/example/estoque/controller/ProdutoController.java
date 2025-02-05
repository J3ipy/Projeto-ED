package com.example.estoque.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProdutoController {

    @GetMapping("/")
    public String home() {
        return "Bem-vindo à aplicação de gerenciamento de estoque!";
    }
}