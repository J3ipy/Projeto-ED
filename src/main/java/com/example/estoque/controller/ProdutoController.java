package com.example.estoque.controller;

import com.example.estoque.model.Produto;
import com.example.estoque.service.ProdutoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/produtos")
public class ProdutoController {
    private final ProdutoService service;

    public ProdutoController(ProdutoService service) {
        this.service = service;
    }

    // Listar produtos
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("produtos", service.getTodosProdutos());
        return "listar";
    }

    // Formulário de cadastro
    @GetMapping("/novo")
    public String novoProduto(Model model) {
        model.addAttribute("produto", new Produto());
        return "form";
    }

    // Salvar produto
    @PostMapping("/salvar")
    public String salvar(@ModelAttribute Produto produto) {
        service.adicionarProduto(produto);
        return "redirect:/produtos";
    }

    // Editar produto
    @GetMapping("/editar/{codigo}")
    public String editar(@PathVariable String codigo, Model model) {
        Produto produto = service.buscarPorCodigo(codigo);
        model.addAttribute("produto", produto);
        return "form";
    }

    // Repor estoque
    @PostMapping("/repor")
    public String repor(@RequestParam String codigo, @RequestParam int quantidade) {
        service.reporEstoque(codigo, quantidade);
        return "redirect:/produtos";
    }

    // Vender produto
    @PostMapping("/vender")
    public String vender(@RequestParam String codigo, @RequestParam int quantidade) {
        service.venderProduto(codigo, quantidade);
        return "redirect:/produtos";
    }

    // Ordenar por nome
    @GetMapping("/ordenar")
    public String ordenar() {
        service.ordenarPorNome();
        return "redirect:/produtos";
    }

    // Aplicar aumento
    @PostMapping("/aumento")
    public String aumento(@RequestParam double percentual) {
        service.aplicarAumento(percentual);
        return "redirect:/produtos";
    }

    // Relatórios
    @GetMapping("/relatorios")
    public String relatorios(Model model) {
        model.addAttribute("produtos", produtoService.getTodosProdutos());
        model.addAttribute("vendas", produtoService.getHistoricoVendas());
        model.addAttribute("total", produtoService.getTotalVendas());
        return "relatorios";
    }
}