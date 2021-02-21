package alo.spring.batch.rooster;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.core.io.FileUrlResource;
import org.springframework.core.io.UrlResource;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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

    @Test
    public void testRegex() {
        String data = "NATION_file_name.csv";
        Pattern pattern = Pattern.compile("^(\\p{Alpha}*)_");
        Matcher matcher = pattern.matcher(data);

        if (matcher.find()) {
            System.out.println("extract : " + matcher.group(1));
        }
        else {
            System.out.println("Nothing found!!");
        }
    }
}
