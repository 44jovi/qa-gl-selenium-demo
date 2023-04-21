package com.qa.demo.selenium;

import java.time.Duration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Sql(scripts = { "classpath:cat-schema.sql", "classpath:cat-data.sql" })
public class SpringSeleniumTest {

	private WebDriver driver;

	@LocalServerPort
	private int port;

	private WebDriverWait wait;

	@BeforeEach
	void init() {
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--remote-allow-origins=*");
		this.driver = new ChromeDriver(options);
		this.driver.manage().window().maximize();
		// Wait for UP TO time specified
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(3));
	}

	@AfterEach
	void tearDown() {
//		this.driver.close();
	}

	@Test
	void testTitle() {
		this.driver.get("http://localhost:" + port);

		WebElement title = this.driver.findElement(By.cssSelector("body > header > h1"));
		Assertions.assertEquals("CATS", title.getText());
	}

	@Test
	void testGetAll() throws InterruptedException {
		this.driver.get("http://localhost:" + port);

		// One way to wait, using Thread but would always wait the specified time
		// Thread.sleep(3000);

		WebElement card = this.wait
				.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#output > div > div > div")));

		Assertions.assertTrue(card.getText().contains("Mr Bigglesworth"));
	}

}
