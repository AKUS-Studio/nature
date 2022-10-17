package works.akus.mauris.resourcepack.github;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import net.lingala.zip4j.ZipFile;

public class GitRepository {

    String owner;
    String repo;
    String branch;
    GitHubAPI github;

    public GitRepository(String owner, String repo, String branch, GitHubAPI github) {
        this.owner = owner;
        this.repo = repo;
        this.branch = branch;
        this.github = github;
    }

    public String getLastCommitId(){
        String requestUrl = String.format("https://api.github.com/repos/%s/%s/commits",
                owner,
                repo);

        try {
            URL url = new URL(requestUrl);

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            con.setRequestProperty("Authorization", "Bearer " + github.token);
            con.setRequestProperty("Content-Type", "application/json");

            //Fuck this json api
            JSONObject object = (JSONObject) getJsonArrayFromInputStream(con.getInputStream()).get(0);
            return (String) object.get("sha");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void downloadRepositoryAsZip(File outputfile) {

        String surl = String.format("https://api.github.com/repos/%s/%s/zipball/%s",
                owner,
                repo,
                branch);

        try {
            URL url = new URL(surl);

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            con.setRequestProperty("Authorization", "Bearer " + github.token);
            con.setRequestProperty("Content-Type", "application/zip");

            github.logger.info("Downloading " + owner + "/" + repo + " to a " + outputfile);
            long timestamp = System.currentTimeMillis();

            InputStream stream = con.getInputStream();

            Files.copy(stream, outputfile.toPath(), StandardCopyOption.REPLACE_EXISTING);

            github.logger.info("Downloaded in " + (System.currentTimeMillis() - timestamp) + "ms");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    //Because GitHub creates folder for branch
    public void removeBranchFolder(File zipFile){
        github.logger.info("Removing branch folder from a Zip File");
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

        github.logger.info("Success");
        //Deleting Folder
        deleteDir(whereToExtract);
    }

    //

    private JSONObject getJsonFromInputStream(InputStream inputStream){
        return (JSONObject) getObjectJsonFromInputStream(inputStream);
    }

    private JSONArray getJsonArrayFromInputStream(InputStream inputStream){
        return (JSONArray) getObjectJsonFromInputStream(inputStream);
    }

    private Object getObjectJsonFromInputStream(InputStream inputStream){
        JSONParser jsonParser = new JSONParser();

        try {
            return jsonParser.parse(
                    new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }

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



    //
    public String getOwner() {
        return owner;
    }

    public String getRepo() {
        return repo;
    }

    public String getBranch() {
        return branch;
    }



}
