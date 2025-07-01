package br.com.gerenciamento.service;

import br.com.gerenciamento.exception.EmailExistsException;
import br.com.gerenciamento.model.Usuario;
import br.com.gerenciamento.repository.UsuarioRepository;
import br.com.gerenciamento.util.Util;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UsuarioServiceTest {

    @Autowired
    private ServiceUsuario serviceUsuario;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private Usuario criarUsuarioValido(String email, String user) {
        Usuario usuario = new Usuario();
        usuario.setUser(user);
        usuario.setEmail(email);
        usuario.setSenha("123456");
        return usuario;
    }

    @Before
    public void limparBanco() {
        usuarioRepository.deleteAll();
    }

    @Test
    public void salvarUsuario_comDadosValidos() throws Exception {
        Usuario usuario = criarUsuarioValido("teste1@exemplo.com", "user1");
        serviceUsuario.salvarUsuario(usuario);

        Usuario salvo = usuarioRepository.findByEmail("teste1@exemplo.com");
        Assert.assertNotNull(salvo);
        Assert.assertEquals("user1", salvo.getUser());
    }

    @Test(expected = EmailExistsException.class)
    public void salvarUsuario_comEmailDuplicado() throws Exception {
        Usuario usuario1 = criarUsuarioValido("teste2@exemplo.com", "user2");
        serviceUsuario.salvarUsuario(usuario1);

        Usuario usuario2 = criarUsuarioValido("teste2@exemplo.com", "user3");
        serviceUsuario.salvarUsuario(usuario2); // deve lançar EmailExistsException
    }


    @Test
    public void loginUser_comDadosCorretos() throws Exception {
        Usuario usuario = criarUsuarioValido("teste3@exemplo.com", "user3");
        serviceUsuario.salvarUsuario(usuario);

        String senhaCriptografada = Util.md5("123456");

        Usuario encontrado = serviceUsuario.loginUser("user3", senhaCriptografada);
        Assert.assertNotNull(encontrado);
        Assert.assertEquals("teste3@exemplo.com", encontrado.getEmail());
    }
}
