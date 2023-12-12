package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dto.UsuarioDTOInput;
import dto.UsuarioDTOOutput;
import service.UsuarioService;
import spark.Request;
import spark.Response;
import spark.Route;

import java.io.IOException;

import static spark.Spark.put;

public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
        configurarEndpoints();
    }

    private void configurarEndpoints() {

        put("/usuarios/:id", (req, res) -> alterarUsuario(req, res), toJson());
    }

    private Object alterarUsuario(Request req, Response res) {
        try {
            Long id = Long.parseLong(req.params(":id"));
            UsuarioDTOInput novoUsuarioDTOInput = new ObjectMapper().readValue(req.body(), UsuarioDTOInput.class);


            if (!usuarioService.buscarUsuarioPorId(id).isPresent()) {
                res.status(404);
                return "Usuário não encontrado.";
            }

            UsuarioDTOOutput usuarioDTOOutput = usuarioService.alterarUsuario(id, novoUsuarioDTOInput)
                    .orElseThrow(() -> new RuntimeException("Erro ao alterar usuário."));

            res.status(200);
            return usuarioDTOOutput;
        } catch (NumberFormatException e) {
            res.status(400);
            return "ID de usuário inválido.";
        } catch (IOException e) {
            res.status(400);
            return "Erro ao ler o corpo da requisição JSON.";
        } catch (Exception e) {
            res.status(500);
            return "Erro ao alterar usuário.";
        }
    }

    private Route toJson() {
        return (req, res) -> {
            res.type("application/json");
            return toJsonResponse(req);
        };
    }

    private String toJsonResponse(Request req) {
        try {
            Object resultado = req.attribute("spark.route.response");
            return new ObjectMapper().writeValueAsString(resultado);
        } catch (Exception e) {
            res.status(500);
            return "Erro ao processar resposta JSON.";
        }
    }

    public static void main(String[] args) {

        UsuarioService usuarioService = new UsuarioService();
        new UsuarioController(usuarioService);
    }
}
