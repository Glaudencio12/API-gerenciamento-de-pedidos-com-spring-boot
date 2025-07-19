package br.com.gerencimentodepedidos;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Startup {
	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.configure().ignoreIfMissing()
				.directory(System.getenv("DOTENV_PATH") != null ? System.getenv("DOTENV_PATH") : ".")
				.load();
		System.setProperty("MYSQL_DB_URL", dotenv.get("MYSQL_DB_URL"));
		System.setProperty("MYSQL_DB_USERNAME", dotenv.get("MYSQL_DB_USERNAME"));
		System.setProperty("MYSQL_DB_PASSWORD", dotenv.get("MYSQL_DB_PASSWORD"));

		SpringApplication.run(Startup.class, args);
	}
}
