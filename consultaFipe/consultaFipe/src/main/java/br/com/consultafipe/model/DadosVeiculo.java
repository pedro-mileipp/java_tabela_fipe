package br.com.consultafipe.model;

public record DadosVeiculo(String codigo, String nome) {
    @Override
    public final String toString() {
        return "Código: " + codigo + " | Nome da marca: " + nome;
    }
}
