import dto.UsuarioDTOInput;
import org.junit.jupiter.api.Test;
import service.UsuarioService;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ServiceTest {

    @Test
    public void testInserirUsuario() {

        UsuarioService usuarioService = new UsuarioService();


        UsuarioDTOInput usuarioDTOInput = new UsuarioDTOInput();
        usuarioDTOInput.setNome("Teste");
        usuarioDTOInput.setSenha("senha123");


        usuarioService.inserirUsuario(usuarioDTOInput);


        assertEquals(1, usuarioService.listarUsuarios().size(), "Inserção de usuário bem-sucedida");
    }
}
