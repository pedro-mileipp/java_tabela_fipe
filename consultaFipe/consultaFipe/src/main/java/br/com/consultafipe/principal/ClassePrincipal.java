package br.com.consultafipe.principal;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import br.com.consultafipe.model.DadosVeiculo;
import br.com.consultafipe.model.Modelos;
import br.com.consultafipe.model.Veiculo;
import br.com.consultafipe.service.ConsumoAPI;
import br.com.consultafipe.service.ConverteDados;

public class ClassePrincipal {
    private Scanner scanner = new Scanner(System.in);
    private static final String URL_BASE = "https://parallelum.com.br/fipe/api/v1/";
    private final ConsumoAPI consumoAPI = new ConsumoAPI();
    private final ConverteDados conversor = new ConverteDados();

    public void executar() {
        String tipoVeiculo = escolherTipoVeiculo();
        String enderecoMarcas = URL_BASE + tipoVeiculo + "/marcas";

        List<DadosVeiculo> marcas = obterListaDados(enderecoMarcas, DadosVeiculo.class);
        exibirListaOrdenada("Marcas disponíveis:", marcas);

        String codigoMarca = lerEntrada("Informe o código da marca para consulta:");
        String enderecoModelos = enderecoMarcas + "/" + codigoMarca + "/modelos";

        Modelos modelos = conversor.obterDados(consumoAPI.obterDados(enderecoModelos), Modelos.class);
        exibirListaOrdenada("Modelos dessa marca:", modelos.modelos());

        String nomeModelo = lerEntrada("Digite um trecho do nome do modelo:");
        List<DadosVeiculo> modelosFiltrados = filtrarPorNome(modelos.modelos(), nomeModelo);
        exibirListaOrdenada("Modelos filtrados:", modelosFiltrados);

        String codigoModelo = lerEntrada("Digite o código do modelo:");
        String enderecoAnos = enderecoModelos + "/" + codigoModelo + "/anos";

        List<DadosVeiculo> anos = obterListaDados(enderecoAnos, DadosVeiculo.class);
        List<Veiculo> veiculos = obterVeiculos(tipoVeiculo, codigoMarca, codigoModelo, anos);

        exibirLista("Veículos avaliados:", veiculos);
    }

    private String escolherTipoVeiculo() {
        String menu = """
            *** OPÇÕES ***
            Carro 
            Moto
            Caminhão 
            Digite uma das opções acima:
            """;
        System.out.println(menu);
        String opcao = scanner.nextLine().toLowerCase();

        return switch (opcao) {
            case "carro" -> "carros";
            case "moto" -> "motos";
            case "caminhao", "caminhão" -> "caminhoes";
            default -> throw new IllegalArgumentException("Opção inválida");
        };
    }

    private <T> List<T> obterListaDados(String url, Class<T> classe) {
        String json = consumoAPI.obterDados(url);
        return conversor.obterLista(json, classe);
    }

    private List<DadosVeiculo> filtrarPorNome(List<DadosVeiculo> lista, String nome) {
        return lista.stream()
            .filter(m -> m.nome().toLowerCase().contains(nome.toLowerCase()))
            .collect(Collectors.toList());
    }

    private List<Veiculo> obterVeiculos(String tipo, String marca, String modelo, List<DadosVeiculo> anos) {
        List<Veiculo> veiculos = new ArrayList<>();
        for (DadosVeiculo ano : anos) {
            String url = URL_BASE + tipo + "/marcas/" + marca + "/modelos/" + modelo + "/anos/" + ano.codigo();
            String json = consumoAPI.obterDados(url);
            veiculos.add(conversor.obterDados(json, Veiculo.class));
        }
        return veiculos;
    }

    private String lerEntrada(String mensagem) {
        System.out.println(mensagem);
        return scanner.nextLine();
    }

    private <T> void exibirLista(String titulo, List<T> lista) {
        System.out.println("\n" + titulo);
        lista.forEach(System.out::println);
    }

    private void exibirListaOrdenada(String titulo, List<DadosVeiculo> lista) {
        exibirLista(titulo, lista.stream()
            .sorted(Comparator.comparing(DadosVeiculo::nome))
            .collect(Collectors.toList()));
    }
}
