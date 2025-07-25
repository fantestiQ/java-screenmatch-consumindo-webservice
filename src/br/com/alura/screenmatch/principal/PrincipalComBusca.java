package br.com.alura.screenmatch.principal;

import br.com.alura.screenmatch.execoes.ErroDeConversaoDeAnoException;
import br.com.alura.screenmatch.modelos.Titulo;
import br.com.alura.screenmatch.modelos.TituloOmdb;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PrincipalComBusca {
    public static void main(String[] args) throws IOException, InterruptedException {
        Scanner leitura = new Scanner(System.in);
        String busca = "";
        List <Titulo> titulos = new ArrayList<>();
        Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE).setPrettyPrinting().create();

        while (!busca.equalsIgnoreCase("Sair")) {
            System.out.println("Digite oum filme que deseja buscar: ");
            busca = leitura.nextLine();

            if (busca.equalsIgnoreCase("sair")){
                break;
            }

            busca = URLEncoder.encode(busca, "UTF-8");
            String endereco = "http://www.omdbapi.com/?t=" + busca + "&apikey=c161499";

            try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(endereco))
                    .build();
            HttpResponse<String> response = client
                    .send(request, HttpResponse.BodyHandlers.ofString());
            //System.out.println(response.body());

            String json = response.body();
            System.out.println(json);


            TituloOmdb meuTituloOmdb = gson.fromJson(json, TituloOmdb.class);
            System.out.println(meuTituloOmdb);


                Titulo meuTitulo = new Titulo(meuTituloOmdb);
                System.out.println(meuTitulo);
                titulos.add(meuTitulo);
            } catch (NumberFormatException e) {
                System.out.println("Ocorreu um erro:");
                System.out.println("Motivo do erro:" + e.getMessage());

            } catch (ErroDeConversaoDeAnoException e) {
                System.out.println(e.getMessage());
            }
        }
            System.out.println(titulos);

            FileWriter escrita = new FileWriter("filme.json");
            escrita.write(gson.toJson(titulos));
            escrita.close();
            System.out.println("Programa finalizado corretamente.");


        }

}
