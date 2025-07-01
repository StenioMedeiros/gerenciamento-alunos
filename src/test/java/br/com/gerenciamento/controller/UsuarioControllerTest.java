package br.com.gerenciamento.controller;

import br.com.gerenciamento.model.Usuario;
import br.com.gerenciamento.repository.UsuarioRepository;
import br.com.gerenciamento.service.ServiceUsuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ServiceUsuario serviceUsuario;

    @BeforeEach
    public void setup() {
        Usuario usuario = new Usuario();
        usuario.setEmail("teste@teste.com");
        usuario.setUser("testeuser");
        usuario.setSenha("123"); // senha será criptografada no método salvarUsuario
        try {
            serviceUsuario.salvarUsuario(usuario);
        } catch (Exception e) {
            // ignora erro de email duplicado
        }
    }

    @Test
    public void deveMostrarTelaDeLogin() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("login/login"))
                .andExpect(model().attributeExists("usuario"));
    }

    @Test
    public void deveMostrarTelaDeCadastro() throws Exception {
        mockMvc.perform(get("/cadastro"))
                .andExpect(status().isOk())
                .andExpect(view().name("login/cadastro"))
                .andExpect(model().attributeExists("usuario"));
    }

    @Test
    public void deveRedirecionarParaLoginAposCadastro() throws Exception {
        mockMvc.perform(post("/salvarUsuario")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("email", "novo@exemplo.com")
                .param("user", "usuarioNovo")
                .param("senha", "senha123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    public void deveRedirecionarParaIndexAposLoginValido() throws Exception {
        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("user", "testeuser")
                .param("senha", "123")) // senha em texto plano, será criptografada na lógica
                .andExpect(status().isOk())
                .andExpect(view().name("home/index"));
    }
}

