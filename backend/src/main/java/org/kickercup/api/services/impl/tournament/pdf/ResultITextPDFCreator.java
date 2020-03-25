//----------------------------------------------------------------------------------
//     Created By: Jonas Jahns
// Contributor(s): None
//    Description: PDF Service
//----------------------------------------------------------------------------------
package org.kickercup.api.services.impl.tournament.pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.kickercup.api.services.impl.tournament.result.TournamentResult;
import org.kickercup.api.services.impl.tournament.result.table.ResultTable;
import org.kickercup.api.services.interfaces.TournamentPDFCreator;
import org.springframework.core.io.ClassPathResource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.kickercup.api.FileServeConfig.TEMP_URL;

public class ResultITextPDFCreator implements TournamentPDFCreator {

    private static final Logger LOGGER = Logger.getLogger(ResultITextPDFCreator.class.getName());

    private final Font font = FontFactory.getFont(FontFactory.HELVETICA, 16, BaseColor.BLACK);

    public ResultITextPDFCreator() { }

    public String create(final Path folder, final TournamentResult result) throws DocumentException, IOException {
        String file;
        String fileName;
        try {
            fileName = getResultFileName(result);
            file = getResultFile(folder, fileName);
        } catch (final NullPointerException ex) {
            throw new IOException("Failed to get file name", ex);
        }
        try (FileOutputStream out = new FileOutputStream(file)) {
            final Document document = new Document(PageSize.A4, 40, 40, 100, 40);
            PdfWriter.getInstance(document, out);
            document.open();

            try {
                appendImage(document);
            } catch (final DocumentException e) {
                LOGGER.log(Level.SEVERE, "Failed to append image to document");
                throw e;
            }

            try {
                addHeaderToDocument(document, result.getHeader());
                for (final String subHeader : result.getSubHeaders()) {
                    appendTextToDocument(document, subHeader);
                }
            } catch (final DocumentException e) {
                LOGGER.log(Level.SEVERE, "Failed to append text to document");
                throw e;
            }

            try {
                appendTableToDocument(document, result.getTable());
            } catch (final DocumentException e) {
                LOGGER.log(Level.SEVERE, "Failed to append table to document");
                throw e;
            }

            document.close();
            LOGGER.log(Level.FINE, "Created result pdf file '" + file + "'");
        } catch (final IOException ex) {
            LOGGER.log(Level.SEVERE, "Failed to open File Stream");
            throw ex;
        } catch (final DocumentException ex) {
            LOGGER.log(Level.SEVERE, "Failed to get PDF Writer Instance");
            throw ex;
        }
        return TEMP_URL + fileName;
    }

    private void addHeaderToDocument(final Document document, final String text) throws DocumentException {
        Font headerFont =new Font(Font.FontFamily.HELVETICA, 30.0f, Font.NORMAL, BaseColor.DARK_GRAY);
        Paragraph paragraph = new Paragraph(text, headerFont);
        paragraph.setSpacingBefore(40f);
        document.add(paragraph);
    }

    private void appendTextToDocument(final Document document, final String text) throws DocumentException {
        Paragraph paragraph = new Paragraph(text);
        paragraph.setFont(font);
        document.add(paragraph);
    }


    private void appendImage(final Document document) throws IOException, DocumentException {
        File file = new ClassPathResource("Logo.png").getFile();
        final Image img = Image.getInstance(file.toPath().toAbsolutePath().toString());
        img.setAlignment(TabStop.Alignment.RIGHT.ordinal());
        img.scaleAbsoluteHeight(100);
        img.scaleAbsoluteWidth(200);
        document.add(img);
    }

    private void appendTableToDocument(final Document document, final ResultTable table)
            throws DocumentException {
        if (null == table || null == table.getColumnHeaders() || null == table.getEntries()) {
            throw new DocumentException("Table cannot be appended to pdf because it is empty");
        }

        final PdfPTable pdfPTable = new PdfPTable(table.getColumnHeaders().size());

        table.getColumnHeaders().forEach(columnHeader -> {
            final PdfPCell header = new PdfPCell();
            header.setBackgroundColor(BaseColor.LIGHT_GRAY);
            header.setBorderWidth(2);
            header.setPhrase(new Phrase(columnHeader));
            pdfPTable.addCell(header);
        });
        table.getEntries().forEach(entry -> {
            final PdfPCell header = new PdfPCell();
            header.setPhrase(new Phrase(entry.getPlace() + "."));
            pdfPTable.addCell(header);
            entry.getPlayers().forEach(pdfPTable::addCell);
            pdfPTable.completeRow();
        });

        pdfPTable.setSpacingBefore(70f);
        document.add(pdfPTable);
    }



    private String getResultFileName(final TournamentResult result) {
        if (null == result) {
            throw new NullPointerException();
        }
        return "tournamentResult_" + java.util.UUID.randomUUID().toString() + ".pdf";
    }

    private String getResultFile(final Path filePath, final String fileName) {
        if (null == filePath || null == fileName) {
            throw new NullPointerException();
        }
        return Paths.get(filePath.toAbsolutePath().toString(), fileName).toString();
    }
}
