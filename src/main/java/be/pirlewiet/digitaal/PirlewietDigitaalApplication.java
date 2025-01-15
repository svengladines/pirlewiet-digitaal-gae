package be.pirlewiet.digitaal;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.Banner;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
public class PirlewietDigitaalApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder()
                .bannerMode(Banner.Mode.OFF)
                .sources(PirlewietDigitaalApplication.class)
                .run(args);
    }

}
