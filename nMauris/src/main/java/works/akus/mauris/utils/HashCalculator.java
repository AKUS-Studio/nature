package works.akus.mauris.utils;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.security.MessageDigest;

public class HashCalculator {

    public static byte[] calcSHA1Hash(File resourcepackFile) {
        try {
            byte[] fileBytes = Files.readAllBytes(resourcepackFile.toPath());

            MessageDigest md = MessageDigest.getInstance("SHA-1");
            return md.digest(fileBytes);

        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }

}
