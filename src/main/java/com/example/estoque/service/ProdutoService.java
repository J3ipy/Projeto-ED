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
    
    public void salvarProduto(Produto produto) {
        Produto existente = buscarPorCodigo(produto.getCodigo());
        if (existente != null) {
            // Atualiza os dados do produto existente
            existente.setDescricao(produto.getDescricao());
            existente.setQuantidadeEstoque(produto.getQuantidadeEstoque());
            existente.setValorSaida(produto.getValorSaida());
            // Atualize outros campos, se houver
        } else {
            // Se não existir, adiciona como novo
            produtos.add(produto);
        }
    }


    // Adicionar produto
    public void adicionarProduto(Produto produto) {
        produtos.add(produto);
    }

    public void mergeSortPorNome(List<Produto> produtos) {
        if (produtos == null || produtos.size() < 2) {
            return;
        }
        mergeSort(produtos, 0, produtos.size() - 1);
    }

    private void mergeSort(List<Produto> produtos, int esquerda, int direita) {
        if (esquerda < direita) {
            int meio = esquerda + (direita - esquerda) / 2;
            mergeSort(produtos, esquerda, meio);
            mergeSort(produtos, meio + 1, direita);
            merge(produtos, esquerda, meio, direita);
        }
    }

    private void merge(List<Produto> produtos, int esquerda, int meio, int direita) {
        int tamanhoEsquerda = meio - esquerda + 1;
        int tamanhoDireita = direita - meio;

        Produto[] esquerdaArray = new Produto[tamanhoEsquerda];
        Produto[] direitaArray = new Produto[tamanhoDireita];

        for (int i = 0; i < tamanhoEsquerda; i++) {
            esquerdaArray[i] = produtos.get(esquerda + i);
        }
        for (int j = 0; j < tamanhoDireita; j++) {
            direitaArray[j] = produtos.get(meio + 1 + j);
        }

        int i = 0, j = 0, k = esquerda;
        while (i < tamanhoEsquerda && j < tamanhoDireita) {
            if (esquerdaArray[i].getNome().compareToIgnoreCase(direitaArray[j].getNome()) <= 0) {
                produtos.set(k, esquerdaArray[i]);
                i++;
            } else {
                produtos.set(k, direitaArray[j]);
                j++;
            }
            k++;
        }

        while (i < tamanhoEsquerda) {
            produtos.set(k, esquerdaArray[i]);
            i++;
            k++;
        }

        while (j < tamanhoDireita) {
            produtos.set(k, direitaArray[j]);
            j++;
            k++;
        }
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
    

    public List<Produto> getTodosProdutos() {
        return produtos;
    }
    
    public List<String> getHistoricoVendas() {
        return historicoVendas;
    }

    public double getTotalVendas() {
        return totalVendas;
    }
}