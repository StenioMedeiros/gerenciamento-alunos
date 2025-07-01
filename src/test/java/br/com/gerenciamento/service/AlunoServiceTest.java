package br.com.gerenciamento.service;

import br.com.gerenciamento.enums.Curso;
import br.com.gerenciamento.enums.Status;
import br.com.gerenciamento.enums.Turno;
import br.com.gerenciamento.model.Aluno;
import jakarta.validation.ConstraintViolationException;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AlunoServiceTest {

    @Autowired
    private ServiceAluno serviceAluno;

    private Aluno criarAlunoValido(String nome, String matricula) {
        Aluno aluno = new Aluno();
        aluno.setNome(nome);
        aluno.setMatricula(matricula);
        aluno.setCurso(Curso.ADMINISTRACAO);
        aluno.setStatus(Status.ATIVO);
        aluno.setTurno(Turno.NOTURNO);
        return aluno;
    }

    @Test
    public void getById() {
        Aluno aluno = criarAlunoValido("Vinicius", "123456");
        this.serviceAluno.save(aluno);

        Aluno alunoRetorno = this.serviceAluno.getById(aluno.getId());
        Assert.assertEquals("Vinicius", alunoRetorno.getNome());
    }

    @Test
    public void salvarSemNome() {
        Aluno aluno = criarAlunoValido("", "123456");
        Assert.assertThrows(ConstraintViolationException.class, () -> {
            this.serviceAluno.save(aluno);
        });
    }

    @Test
    public void findByNomeContainingIgnoreCase() {
        Aluno aluno1 = criarAlunoValido("Ana Silva", "2023001");
        Aluno aluno2 = criarAlunoValido("Anastácia", "2023002");
        this.serviceAluno.save(aluno1);
        this.serviceAluno.save(aluno2);

        List<Aluno> resultados = this.serviceAluno.findByNomeContainingIgnoreCase("ana");
        Assert.assertEquals(2, resultados.size());
    }

    @Test
    public void deleteById() {
        Aluno aluno = criarAlunoValido("Carlos", "2023010");
        this.serviceAluno.save(aluno);
        Long id = aluno.getId();

        this.serviceAluno.deleteById(id);
        Assert.assertFalse(this.serviceAluno.findAll().stream().anyMatch(a -> a.getId().equals(id)));
    }
}
