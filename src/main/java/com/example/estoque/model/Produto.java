package com.example.estoque.model;

import lombok.Data;

@Data
public class Produto {
    private String codigo;
    private String descricao;
    private String nome;
    private String marca;
    private double valorEntrada;
    private double valorSaida;
    private int quantidadeEstoque;
    private int vendidos;
}