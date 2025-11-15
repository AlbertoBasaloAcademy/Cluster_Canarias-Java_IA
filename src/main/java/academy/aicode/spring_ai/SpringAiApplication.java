package academy.aicode.spring_ai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "academy.aicode")
public class SpringAiApplication {

  public static void main(String[] args) {
    SpringApplication.run(SpringAiApplication.class, args);
  }

}
