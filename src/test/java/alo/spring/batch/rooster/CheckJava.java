package alo.spring.batch.rooster;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.core.io.*;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
public class CheckJava {
    @Test
    public void simpleTests() {
        File inputFile = new File("F:/Code/Items/Rooster/unitFile.csv");

        if(inputFile.isFile()) {
            System.out.println("it's a file!");
        }

        try {
            FileUrlResource inputResource = new FileUrlResource("file:F:/Code/Items/Rooster/unitFile.csv");

            System.out.println("resource : " + inputResource.getFilename());

            if (inputResource.isFile()) {
                System.out.println("it's a file too!");
            }

            UrlResource outputResource = new UrlResource("file:F:/Code/Items/testOutput.txt");

            Files.writeString(outputResource.getFile().toPath(), "test");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void readFile() throws MalformedURLException {
        UrlResource inputFile = new UrlResource("file:F:/Code/Items/Rooster/unitFile.csv");

        try (Stream<String> lines = Files.lines(inputFile.getFile().toPath(), Charset.defaultCharset())) {
            lines.forEachOrdered(System.out::println);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void writeFile() throws MalformedURLException {
        UrlResource outputFile = new UrlResource("file:F:/Code/Items/testOutput.txt");

        try {
            Files.writeString(outputFile.getFile().toPath(), "test");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
