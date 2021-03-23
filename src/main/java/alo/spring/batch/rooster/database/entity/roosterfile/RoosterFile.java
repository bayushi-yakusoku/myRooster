package alo.spring.batch.rooster.database.entity.roosterfile;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Data
public class RoosterFile {

    private Integer roosterFileId;
    private String fileName;
    private String signature;
    private String unit;

    private List<RoosterFileJob> listJob = new ArrayList<>();
    private List<RoosterFileInfo> listInfo = new ArrayList<>();

    public RoosterFile() {}

    public RoosterFile(String fileName, String signature) {
        this.fileName = fileName;
        this.signature = signature;

        getUnitFromFileName();
    }

    @Nullable
    public String getUnitFromFileName() {
        Pattern pattern = Pattern.compile("^(\\p{Alpha}*)_");
        Matcher matcher = pattern.matcher(fileName);

        if (matcher.find())
            return matcher.group(1);
        else
            return "n/a";
    }
}
