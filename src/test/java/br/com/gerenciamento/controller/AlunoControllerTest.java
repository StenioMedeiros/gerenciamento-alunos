package br.com.gerenciamento.controller;


import br.com.gerenciamento.model.Aluno;
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
public class AlunoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void deveRetornarFormularioDeInsercaoDeAlunos() throws Exception {
        mockMvc.perform(get("/inserirAlunos"))
                .andExpect(status().isOk())
                .andExpect(view().name("Aluno/formAluno"))
                .andExpect(model().attributeExists("aluno"));
    }

    @Test
    public void deveRedirecionarParaListaAposInsercaoValida() throws Exception {
        Aluno aluno = new Aluno();
        aluno.setNome("João da Silva");
        aluno.setMatricula("123456");
        aluno.setCurso(br.com.gerenciamento.enums.Curso.BIOMEDICINA);
        aluno.setStatus(br.com.gerenciamento.enums.Status.ATIVO);
        aluno.setTurno(br.com.gerenciamento.enums.Turno.MATUTINO);

        mockMvc.perform(post("/InsertAlunos")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("nome", aluno.getNome())
                .param("matricula", aluno.getMatricula())
                .param("curso", aluno.getCurso().name())
                .param("status", aluno.getStatus().name())
                .param("turno", aluno.getTurno().name()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/alunos-adicionados"));
    }

    @Test
    public void deveRetornarListaDeAlunos() throws Exception {
        mockMvc.perform(get("/alunos-adicionados"))
                .andExpect(status().isOk())
                .andExpect(view().name("Aluno/listAlunos"))
                .andExpect(model().attributeExists("alunosList"));
    }

    @Test
    public void deveRetornarViewDeFiltroDeAlunos() throws Exception {
        mockMvc.perform(get("/filtro-alunos"))
                .andExpect(status().isOk())
                .andExpect(view().name("Aluno/filtroAlunos"));
    }
}
