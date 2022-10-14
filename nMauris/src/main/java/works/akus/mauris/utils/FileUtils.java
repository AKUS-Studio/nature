package works.akus.mauris.utils;

import com.google.common.io.Files;

import java.io.File;
import java.io.InputStream;

public class FileUtils {

    public static void inputStreamToAFile(InputStream stream, File outputFile){

        try {
            byte[] buffer = new byte[stream.available()];
            stream.read(buffer);

            Files.write(buffer, outputFile);
        }catch (Exception e){
            throw new RuntimeException(e);
        }

    }

}
