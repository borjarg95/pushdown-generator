package main;

import java.util.Arrays;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;


@SpringBootApplication
public class App {

	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(App.class, args);		
        String[] beanNames = ctx.getBeanDefinitionNames();
        
        System.out.println("Let's inspect the beans provided by Spring Boot:");
        
        Arrays.sort(beanNames);
        for (String beanName : beanNames) {
            System.out.println(beanName);
        }
	}

}
