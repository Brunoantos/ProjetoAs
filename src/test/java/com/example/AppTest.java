import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ApiTest {

    @Test
    public void testInserirUsuario() throws IOException {

        String randomUserApiUrl = "https://randomuser.me/api/";
        String usuarioJson = obterDadosUsuario(randomUserApiUrl);


        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode usuarioNode = objectMapper.readTree(usuarioJson);


        String nome = usuarioNode.at("/results/0/name/first").asText();
        String senha = "senhaAleatoria";  // Pode gerar uma senha aleatória aqui


        String apiUrl = "http://localhost:4567/usuarios";


        URL url = new URL(apiUrl);


        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        try {

            connection.setRequestMethod("POST");
            connection.setDoOutput(true);


            String requestBody = String.format("{\"nome\":\"%s\",\"senha\":\"%s\"}", nome, senha);


            connection.getOutputStream().write(requestBody.getBytes());


            int responseCode = connection.getResponseCode();


            assertEquals(201, responseCode, "Inserção de usuário bem-sucedida");
        } finally {

            connection.disconnect();
        }
    }

    private String obterDadosUsuario(String apiUrl) throws IOException {

        URL url = new URL(apiUrl);


        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        try {

            connection.setRequestMethod("GET");


            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                return response.toString();
            }
        } finally {

            connection.disconnect();
        }
    }
}
