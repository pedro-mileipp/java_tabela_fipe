package br.com.consultafipe.consultaFipe;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import br.com.consultafipe.principal.ClassePrincipal;

@SpringBootApplication
public class ConsultaFipeApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ConsultaFipeApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		ClassePrincipal classePrincipal = new ClassePrincipal();
		classePrincipal.executar();
	}

}
