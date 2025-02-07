package com.example.estoque.controller;

import com.example.estoque.model.Produto;
import com.example.estoque.service.ProdutoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.ArrayList;


@Controller
@RequestMapping("/produtos")
public class ProdutoController {
    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
        System.out.println("ProdutoService injetado: " + produtoService);
    }

    // Listar produtos
    @GetMapping
    public String listarProdutos(Model model) {
        model.addAttribute("produtos", produtoService.getTodosProdutos());
        return "listar";
    }

    // Formulário de cadastro
    @GetMapping("/novo")
    public String formNovoProduto(Model model) {
        model.addAttribute("produto", new Produto());
        return "form";
    }

    // Salvar produto
    // ProdutoController.java
    @PostMapping("/salvar")
    public String salvarProduto(@ModelAttribute Produto produto){
        produtoService.salvarProduto(produto);
        return "redirect:/produtos";
    }


    // Editar produto
    @GetMapping("/editar/{codigo}")
    public String editar(@PathVariable String codigo, Model model) {
        Produto produto = produtoService.buscarPorCodigo(codigo);
        model.addAttribute("produto", produto);
        return "form";
    }

    // Repor estoque
    @PostMapping("/repor")
    public String repor(@RequestParam String codigo, @RequestParam int quantidade) {
        produtoService.reporEstoque(codigo, quantidade);
        return "redirect:/produtos";
    }

    // Vender produto
    @PostMapping("/vender")
    public String vender(@RequestParam String codigo, @RequestParam int quantidade) {
        produtoService.venderProduto(codigo, quantidade);
        return "redirect:/produtos";
    }

    @GetMapping("/ordenar")
    public String ordenar(Model model) {
        List<Produto> produtos = produtoService.getTodosProdutos();
        produtoService.mergeSortPorNome(produtos); // Ordenação pelo nome usando Merge Sort
        model.addAttribute("produtos", produtos);
        return "redirect:/produtos";
    }

    // Aplicar aumento
    @PostMapping("/aumento")
    public String aumento(@RequestParam double percentual) {
        produtoService.aplicarAumento(percentual);
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

    // Rota para a página inicial (index)
    @GetMapping("/")
    public String index() {
        return "index"; // Nome do template Thymeleaf: index.html
    }
    
    // Buscar produto pelo código
    @GetMapping("/buscar")
    public String buscarProduto(@RequestParam String codigo, Model model) {
        Produto produto = produtoService.buscarPorCodigo(codigo);
    
        if (produto != null) {
            model.addAttribute("produtos", List.of(produto)); // Lista com apenas um produto
        } else {
            model.addAttribute("produtos", produtoService.getTodosProdutos());
            model.addAttribute("mensagem", "Produto não encontrado!");
        }
    
        return "listar"; // Retorna a página da listagem com o resultado
    }

}