package works.akus.mauris.resourcepack;

import net.lingala.zip4j.ZipFile;

import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import java.util.logging.Logger;

public class GitHubAPI {

    String token;
    Logger logger;

    public GitHubAPI(String token){
        this.token = token;

        logger = Logger.getLogger("GitHub API");
    }

    public void downloadRepositoryAsZip(String owner, String repo, String branch, File outputfile) {

        String surl = String.format("https://api.github.com/repos/%s/%s/zipball/%s",
                owner,
                repo,
                branch);

        try {
            URL url = new URL(surl);

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            con.setRequestProperty("Authorization", "Bearer " + token);
            con.setRequestProperty("Content-Type", "application/zip");

            logger.info("Downloading " + owner + "/" + repo + " to a " + outputfile);
            long timestamp = System.currentTimeMillis();

            InputStream stream = con.getInputStream();

            Files.copy(stream, outputfile.toPath(), StandardCopyOption.REPLACE_EXISTING);

            logger.info("Downloaded in " + (System.currentTimeMillis() - timestamp) + "ms");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }



    public void downloadRepositoryAsZip(String owner, String repo, File outputFile){
        downloadRepositoryAsZip(owner, repo, "main", outputFile);
    }


    //Because GitHub creates folder for branch
    public void removeBranchFolder(File zipFile){
        logger.info("Removing branch folder from a Zip File");
        String extractedFolderName = "extracted-" + UUID.randomUUID();

        //Extraction
        File whereToExtract = new File(zipFile.getParentFile().getPath() + File.separator +
                extractedFolderName);

        whereToExtract.mkdirs();

        try(ZipFile downloadedZip = new ZipFile(zipFile)){
            downloadedZip.extractAll(whereToExtract.getPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        zipFile.delete();

        //Zipping
        try(ZipFile newZipFile = new ZipFile(zipFile)){
            for(File file : whereToExtract.listFiles()[0].listFiles()){
                if(file.isDirectory()) newZipFile.addFolder(file);
                else newZipFile.addFile(file);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        logger.info("Success");
        //Deleting Folder
        deleteDir(whereToExtract);
    }

    private void deleteDir(File file) {
        File[] contents = file.listFiles();
        if (contents != null) {
            for (File f : contents) {
                deleteDir(f);
            }
        }
        file.delete();
    }

}
