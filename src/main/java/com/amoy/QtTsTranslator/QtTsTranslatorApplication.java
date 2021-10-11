package com.amoy.QtTsTranslator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class QtTsTranslatorApplication {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(QtTsTranslatorApplication.class, args);

		TsParser parser = new TsParser("src/main/resources/hellotr_fr.ts");
		parser.doParse();
		parser.save();
	}
}
