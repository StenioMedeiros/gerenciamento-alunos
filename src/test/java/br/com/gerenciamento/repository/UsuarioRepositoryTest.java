package br.com.gerenciamento.repository;

import br.com.gerenciamento.model.Usuario;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UsuarioRepositoryTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    private Usuario criarUsuario(String email, String user, String senha) {
        Usuario u = new Usuario();
        u.setEmail(email);
        u.setUser(user);
        u.setSenha(senha);
        return u;
    }

    @Test
    public void testSaveAndFindById() {
        Usuario usuario = criarUsuario("teste@email.com", "usuario1", "senha123");
        Usuario salvo = usuarioRepository.save(usuario);

        Usuario encontrado = usuarioRepository.findById(salvo.getId()).orElse(null);
        assertThat(encontrado).isNotNull();
        assertThat(encontrado.getEmail()).isEqualTo("teste@email.com");
    }

    @Test
    public void testFindByEmail() {
        Usuario usuario = criarUsuario("marcos@email.com", "marcos", "senha123");
        usuarioRepository.save(usuario);

        Usuario encontrado = usuarioRepository.findByEmail("marcos@email.com");
        assertThat(encontrado).isNotNull();
        assertThat(encontrado.getUser()).isEqualTo("marcos");
    }

    @Test
    public void testBuscarLoginValido() {
        Usuario usuario = criarUsuario("maria@email.com", "maria", "senha456");
        usuarioRepository.save(usuario);

        Usuario encontrado = usuarioRepository.buscarLogin("maria", "senha456");
        assertThat(encontrado).isNotNull();
        assertThat(encontrado.getEmail()).isEqualTo("maria@email.com");
    }

    @Test
    public void testBuscarLoginInvalido() {
        Usuario usuario = criarUsuario("joao@email.com", "joao", "senha789");
        usuarioRepository.save(usuario);

        Usuario encontrado = usuarioRepository.buscarLogin("joao", "senhaErrada");
        assertThat(encontrado).isNull();
    }
}
