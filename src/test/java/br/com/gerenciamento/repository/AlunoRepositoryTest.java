package br.com.gerenciamento.repository;

import br.com.gerenciamento.enums.Curso;
import br.com.gerenciamento.enums.Status;
import br.com.gerenciamento.enums.Turno;
import br.com.gerenciamento.model.Aluno;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class AlunoRepositoryTest {

    @Autowired
    private AlunoRepository alunoRepository;

    private Aluno criarAluno(String nome, String matricula, Status status) {
        Aluno aluno = new Aluno();
        aluno.setNome(nome);
        aluno.setMatricula(matricula);
        aluno.setCurso(Curso.BIOMEDICINA); // use um valor válido do enum
        aluno.setStatus(status);
        aluno.setTurno(Turno.MATUTINO); // use um valor válido do enum
        return aluno;
    }

    @Test
    public void testFindByStatusAtivo() {
        Aluno a1 = criarAluno("Carlos", "2023001", Status.ATIVO);
        Aluno a2 = criarAluno("Maria", "2023002", Status.INATIVO);
        alunoRepository.save(a1);
        alunoRepository.save(a2);

        List<Aluno> ativos = alunoRepository.findByStatusAtivo();
        assertThat(ativos).hasSize(1);
        assertThat(ativos.get(0).getNome()).isEqualTo("Carlos");
    }

    @Test
    public void testFindByStatusInativo() {
        Aluno a1 = criarAluno("Carlos", "2023001", Status.ATIVO);
        Aluno a2 = criarAluno("Maria", "2023002", Status.INATIVO);
        alunoRepository.save(a1);
        alunoRepository.save(a2);

        List<Aluno> inativos = alunoRepository.findByStatusInativo();
        assertThat(inativos).hasSize(1);
        assertThat(inativos.get(0).getNome()).isEqualTo("Maria");
    }

    @Test
    public void testFindByNomeContainingIgnoreCase() {
        Aluno a1 = criarAluno("Carlos Silva", "2023001", Status.ATIVO);
        Aluno a2 = criarAluno("cArLa", "2023002", Status.ATIVO);
        alunoRepository.save(a1);
        alunoRepository.save(a2);

        List<Aluno> encontrados = alunoRepository.findByNomeContainingIgnoreCase("carl");
        assertThat(encontrados).hasSize(2);
    }

    @Test
    public void testSaveAndFindById() {
        Aluno aluno = criarAluno("João Pedro", "2023003", Status.ATIVO);
        Aluno salvo = alunoRepository.save(aluno);

        Aluno encontrado = alunoRepository.findById(salvo.getId()).orElse(null);
        assertThat(encontrado).isNotNull();
        assertThat(encontrado.getNome()).isEqualTo("João Pedro");
    }
}
