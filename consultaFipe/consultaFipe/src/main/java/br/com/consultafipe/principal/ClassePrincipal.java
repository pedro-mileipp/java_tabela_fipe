package br.com.consultafipe.principal;

import java.util.Comparator;
import java.util.Scanner;

import br.com.consultafipe.model.DadosVeiculo;
import br.com.consultafipe.model.Modelos;
import br.com.consultafipe.service.ConsumoAPI;
import br.com.consultafipe.service.ConverteDados;

public class ClassePrincipal {
    private Scanner sc = new Scanner(System.in);
    private final String URL_BASE = "https://parallelum.com.br/fipe/api/v1/";
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConverteDados conversor = new ConverteDados();

    public void exibeMenu(){
        var menu = """
            *** OPÇÕES ***

            Carro 
            Moto
            Caminhão 

            Digite uma das opções acima:
            """;
        System.out.println(menu);
        var opcaoEscolhida = sc.nextLine();

        String endereco;

        if (opcaoEscolhida.toLowerCase().contains("carr")){
            endereco = URL_BASE + "carros/marcas";
        } else if (opcaoEscolhida.toLowerCase().contains("mot")) {
            endereco = URL_BASE + "motos/marcas";
        } else {
            endereco = URL_BASE + "caminhoes/marcas";
        }

        var json = consumoAPI.obterDados(endereco);
        System.out.println(json);

        var marcas = conversor.obterLista(json, DadosVeiculo.class);

        marcas.stream()
            .sorted(Comparator.comparing(DadosVeiculo::codigo))
            .forEach(System.out::println);
        
        System.out.println("Informe o código da marca para consulta:");
        var codigoMarca = sc.nextLine();

        endereco = endereco + "/" + codigoMarca + "/modelos";
        json = consumoAPI.obterDados(endereco);
        
        var modelosLista = conversor.obterDados(json, Modelos.class);

        System.out.println("\nModelos dessa marca: ");
        modelosLista.modelos().stream()
        .sorted(Comparator.comparing(DadosVeiculo::codigo))
        .forEach(System.out::println);

    }
}
