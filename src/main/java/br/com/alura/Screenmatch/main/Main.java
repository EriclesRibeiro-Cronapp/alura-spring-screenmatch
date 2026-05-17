package br.com.alura.Screenmatch.main;

import br.com.alura.Screenmatch.model.DadosEpisodio;
import br.com.alura.Screenmatch.model.DadosSerie;
import br.com.alura.Screenmatch.model.DadosTemporada;
import br.com.alura.Screenmatch.service.ConsumoApi;
import br.com.alura.Screenmatch.service.ConverteDados;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    private Scanner leitura = new Scanner(System.in);
    private ConsumoApi consumo = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();

    private final String ENDERECO = "https://omdbapi.com?t=";
    private final String API_KEY = "&apikey=6585022c";

    public void exibeMenu() {
        System.out.println("Digite o nome da série:");
        var nomeSerie = leitura.nextLine();

        var json = consumo.obterDados(
                ENDERECO +
                        nomeSerie
                                .toLowerCase()
                                .replace(" ", "+") +
                        API_KEY
        );

        DadosSerie dadosSerie = conversor.obterDados(json, DadosSerie.class);
        System.out.println("Título: " + dadosSerie.titulo());
        System.out.println("Temporadas: " + dadosSerie.totalTemporadas());
        System.out.println("Avaliação: " + dadosSerie.avaliacao());

        List<DadosTemporada> dadosTemporadas = new ArrayList<>();
		for (int i = 1; i<= dadosSerie.totalTemporadas(); i++) {
			json = consumo
                    .obterDados("https://omdbapi.com?t=gilmore+girls&season=" + i + "&apikey=6585022c");
            json = consumo
                    .obterDados(
                            ENDERECO +
                                    nomeSerie
                                            .toLowerCase()
                                            .replace(" ", "+") +
                                    API_KEY +
                                    "&season=" + i
                            );
			DadosTemporada temporada = conversor
                    .obterDados(
                            json,
                            DadosTemporada.class
                    );

			dadosTemporadas.add(temporada);
		}

        dadosTemporadas
                .forEach(t -> t.episodios()
                        .forEach(e -> System.out.println(e.numero() + " - " + e.titulo())));
    }
}
