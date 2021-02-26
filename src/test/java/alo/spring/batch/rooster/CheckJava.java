package alo.spring.batch.rooster;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.junit.Test;
import org.springframework.core.io.FileUrlResource;
import org.springframework.core.io.UrlResource;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
        UrlResource inputFile = new UrlResource("file:F:/Code/Items/Rooster/NATION_roosterFile.csv");

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
    public void chekSumFile() throws NoSuchAlgorithmException, IOException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        UrlResource inputFile = new UrlResource("file:F:/Code/Items/Rooster/NATION_roosterFile.csv");

        byte[] digest;

        // file hashing with DigestInputStream
        try (DigestInputStream dis = new DigestInputStream(
                new BufferedInputStream(
                        new FileInputStream(inputFile.getFile())),
                md))
        {
            while (dis.read() != -1) ; //empty loop to clear the data

            digest = md.digest();
        }

        System.out.println("Checksum : " + Hex.encodeHexString(digest, false));
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

    @Test
    public void testSplitLine() {

        String line = "1;Halestorm;Lzzy;30;Los Angeles;USA";

        String[] fields = line.split(";");

        for (String field : fields) {
            System.out.println(field);
        }
    }
}
