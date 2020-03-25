//----------------------------------------------------------------------------------
//     Created By: Jonas Jahns
// Contributor(s): None
//    Description: PDF Creator Test
//----------------------------------------------------------------------------------
package org.kickercup.api.services.impl.tournament.pdf;

import static org.hamcrest.Matchers.*;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.kickercup.api.services.impl.localization.LabelService;
import org.kickercup.api.services.impl.tournament.result.TournamentResult;
import org.kickercup.api.services.impl.tournament.result.table.ResultTableEntry;
import org.kickercup.api.services.impl.tournament.result.table.ResultTable;
import org.springframework.core.io.ClassPathResource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import com.itextpdf.text.DocumentException;

import static org.junit.Assert.*;

public class ResultITextPDFCreatorTests {

    private static Path resources;
    private static ResultTable table;
    private static ResultITextPDFCreator pdfCreator;

    @BeforeClass
    public static void initialize() throws IOException {
        File root = new ClassPathResource("/").getFile();
        String folderPath = Paths.get(root.getAbsolutePath(), "test").toString();
        System.out.println("folderPath " + folderPath);
        File folder = new File(folderPath);
        boolean success = folder.mkdirs();
        if (success) {
            resources = folder.toPath();
        } else {
            throw new IOException("Failed to create test folder");
        }

        pdfCreator = new ResultITextPDFCreator();

        ResultTableEntry firstEntry = new ResultTableEntry(Arrays.asList("John Wayne", "Player 1"), 1, 10, 5, 1);
        ResultTableEntry secondEntry = new ResultTableEntry(Arrays.asList("Edward Nigma", "Player 2"), 2, 5, 10, 0);

        table = new ResultTable();
        table.setEntries(Arrays.asList(firstEntry, secondEntry));
        table.setColumnHeaders(Arrays.asList(LabelService.getString(Locale.ENGLISH, "tournament.result.rank"),
                LabelService.getString(Locale.ENGLISH, "tournament.result.player"),
                LabelService.getString(Locale.ENGLISH, "tournament.result.player")));
    }

    @Test
    public void testCreateMethodCreatesFile() throws DocumentException, IOException {
        TournamentResult result = new TournamentResult("testCreateMethodCreatesFile", Arrays.asList("29. January 2020"), table);
        pdfCreator.create(resources, result);
        File file = resources.toFile();
        List<String> names = Arrays.stream(Objects.requireNonNull(file.listFiles()))
                .map(File::getName)
                .collect(Collectors.toList());
        assertThat(names.stream().filter(name -> name.matches("tournamentResult_[a-fA-F0-9\\-]+(\\.pdf)")).count(), equalTo(1L));
    }

    
    public void testCreateMethodWithEmptyTableReturnsNull() throws DocumentException, IOException {
        TournamentResult result = new TournamentResult("testCreateMethodWithEmptyTableThrowsException", new ArrayList<>(), new ResultTable());
        String file = pdfCreator.create(resources, result);
        assertNull(file);
    }

    @AfterClass
    public static void cleanup() throws IOException {
        FileUtils.deleteDirectory(new File(resources.toFile().getAbsolutePath()));
    }
}