package com.example.estoque.service;

import com.example.estoque.model.Produto;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProdutoService {
    private List<Produto> produtos = new ArrayList<>();
    private List<String> historicoVendas = new ArrayList<>();
    private double totalVendas = 0;

    // Adicionar produto
    public void adicionarProduto(Produto produto) {
        produtos.add(produto);
    }

    // MergeSort
    public void ordenarPorNome() {
        produtos = mergeSort(produtos);
    }

    private List<Produto> mergeSort(List<Produto> lista) {
        if (lista.size() <= 1) return lista;
        int meio = lista.size() / 2;
        List<Produto> esquerda = mergeSort(lista.subList(0, meio));
        List<Produto> direita = mergeSort(lista.subList(meio, lista.size()));
        return merge(esquerda, direita);
    }

    private List<Produto> merge(List<Produto> esq, List<Produto> dir) {
        List<Produto> resultado = new ArrayList<>();
        int i = 0, j = 0;
        while (i < esq.size() && j < dir.size()) {
            if (esq.get(i).getDescricao().compareToIgnoreCase(dir.get(j).getDescricao()) < 0) {
                resultado.add(esq.get(i++));
            } else {
                resultado.add(dir.get(j++));
            }
        }
        resultado.addAll(esq.subList(i, esq.size()));
        resultado.addAll(dir.subList(j, dir.size()));
        return resultado;
    }

    // Buscar produto
    public Produto buscarPorCodigo(String codigo) {
        return produtos.stream()
            .filter(p -> p.getCodigo().equals(codigo))
            .findFirst()
            .orElse(null);
    }

    // Repor estoque
    public void reporEstoque(String codigo, int quantidade) {
        Produto p = buscarPorCodigo(codigo);
        if (p != null) {
            p.setQuantidadeEstoque(p.getQuantidadeEstoque() + quantidade);
        }
    }

    // Vender produto
    public boolean venderProduto(String codigo, int quantidade) {
        Produto p = buscarPorCodigo(codigo);
        if (p != null && p.getQuantidadeEstoque() >= quantidade) {
            p.setQuantidadeEstoque(p.getQuantidadeEstoque() - quantidade);
            p.setVendidos(p.getVendidos() + quantidade);
            String venda = String.format("%s - %d un. - R$ %.2f", p.getDescricao(), quantidade, p.getValorSaida() * quantidade);
            historicoVendas.add(venda);
            totalVendas += p.getValorSaida() * quantidade;
            return true;
        }
        return false;
    }

    // Aplicar alteração de preços
    public void aplicarAumento(double percentual) {
        produtos.forEach(p -> {
            p.setValorSaida(p.getValorSaida() * (1 + percentual / 100));
        });
    }

    // Getters
    public List<Produto> getTodosProdutos() { return produtos; }
    public List<String> getHistoricoVendas() { return historicoVendas; }
    public double getTotalVendas() { return totalVendas; }
}