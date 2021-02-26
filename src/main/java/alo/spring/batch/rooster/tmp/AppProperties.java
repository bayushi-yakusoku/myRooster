package alo.spring.batch.rooster.tmp;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Simple test for Annotation ConfigurationProperties
 */
@Configuration
@ConfigurationProperties (prefix = "app.properties")
@Data
public class AppProperties {
    private String firstName;

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
}
