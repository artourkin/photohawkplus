package utils;

import org.supercsv.cellprocessor.FmtBool;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ParseBool;
import org.supercsv.cellprocessor.ParseDouble;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import dao.*;

/**
 * Created by artur on 16/09/15.
 */
public class CSVWriter {
    File file;
    String[] header;
    ICsvBeanWriter beanWriter = null;
    CellProcessor[] processors = getProcessors();
    private static CellProcessor[] getProcessors() {

        final CellProcessor[] processors = new CellProcessor[] {
                new ParseDouble(), // SSIM
                null,//new Optional(new FmtBool("Y", "N")),// new FmtBool("true", "false"), // isSimilar
                new NotNull(), // Original
                new NotNull(), // Result
                new NotNull(), // Original_PNG
                new NotNull() // Result_PNG

        };

        return processors;
    }



    public CSVWriter(File file){
        this.file=file;
        header= new String[] { "SSIM", "isSimilar" ,"Original", "Result", "Original_PNG", "Result_PNG" };

        try {
            beanWriter = new CsvBeanWriter(new FileWriter(file.getAbsolutePath()),
                    CsvPreference.STANDARD_PREFERENCE);
            beanWriter.writeHeader(header);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void write(ImageBean image) throws IOException {
        System.out.println("Writing a line to CSV");
        beanWriter.write(image, header, processors);
    }

    public void destroy(){

        try {
            beanWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




}
