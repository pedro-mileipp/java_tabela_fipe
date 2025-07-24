package br.com.consultafipe.principal;

import java.util.Scanner;

import br.com.consultafipe.service.ConsumoAPI;

public class ClassePrincipal {
    private Scanner sc = new Scanner(System.in);
    private final String URL_BASE = "https://parallelum.com.br/fipe/api/v1/";
    private ConsumoAPI consumoAPI = new ConsumoAPI();

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
    }
}
