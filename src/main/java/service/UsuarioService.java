package service;

import dto.UsuarioDTOOutput;
import dto.UsuarioDTOInput;
import model.Usuario;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class UsuarioService {

    private final List<Usuario> listaUsuarios;
    private final ModelMapper modelMapper;

    public UsuarioService() {
        this.listaUsuarios = new ArrayList<>();
        this.modelMapper = new ModelMapper();
    }


    public List<UsuarioDTOOutput> listarUsuarios() {
        return listaUsuarios.stream()
                .map(usuario -> modelMapper.map(usuario, UsuarioDTOOutput.class))
                .collect(Collectors.toList());
    }


    public UsuarioDTOOutput inserirUsuario(UsuarioDTOInput usuarioDTOInput) {
        Usuario usuario = modelMapper.map(usuarioDTOInput, Usuario.class);
        listaUsuarios.add(usuario);
        return modelMapper.map(usuario, UsuarioDTOOutput.class);
    }


    public Optional<UsuarioDTOOutput> alterarUsuario(Long id, UsuarioDTOInput novoUsuarioDTOInput) {
        for (int i = 0; i < listaUsuarios.size(); i++) {
            Usuario usuario = listaUsuarios.get(i);
            if (usuario.getId().equals(id)) {

                modelMapper.map(novoUsuarioDTOInput, usuario);
                return Optional.of(modelMapper.map(usuario, UsuarioDTOOutput.class));
            }
        }
        return Optional.empty();
    }


    public Optional<UsuarioDTOOutput> buscarUsuarioPorId(Long id) {
        return listaUsuarios.stream()
                .filter(usuario -> usuario.getId().equals(id))
                .map(usuario -> modelMapper.map(usuario, UsuarioDTOOutput.class))
                .findFirst();
    }


    public void excluirUsuario(Long id) {
        listaUsuarios.removeIf(usuario -> usuario.getId().equals(id));
    }
}
