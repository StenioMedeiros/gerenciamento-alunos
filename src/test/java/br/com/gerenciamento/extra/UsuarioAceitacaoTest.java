package br.com.gerenciamento.extra;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class UsuarioAceitacaoTest {

    private WebDriver driver;

    @BeforeEach
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.get("http://localhost:8080/");
    }

    @Test
    public void deveRealizarLoginComSucesso() {
        WebElement userInput = driver.findElement(By.name("user"));
        WebElement senhaInput = driver.findElement(By.name("senha"));
        WebElement loginButton = driver.findElement(By.xpath("//button[@type='submit']"));

        userInput.sendKeys("testeuser");
        senhaInput.sendKeys("123");
        loginButton.click();

        // Esperar o carregamento de qualquer conteúdo (mais confiável que depender da
        // URL)
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));

        // Debug: print the page source to help diagnose the failure
        System.out.println(driver.getPageSource());

        // Verificar se o texto "Cadastro de Usuário" aparece na página
        assertTrue(driver.getPageSource().toLowerCase().contains("cadastro de usuário"),
                "Esperado que a página contenha 'Cadastro de Usuário' após login");
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
