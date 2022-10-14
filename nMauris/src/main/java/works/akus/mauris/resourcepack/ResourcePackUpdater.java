package works.akus.mauris.resourcepack;

import works.akus.mauris.Mauris;

import java.io.File;

public class ResourcePackUpdater {

    Mauris mauris;
    GitHubAPI gitHubAPI;

    public ResourcePackUpdater(){
        this.mauris = Mauris.getInstance();
        this.gitHubAPI = new GitHubAPI(mauris.getConfig().getString("resource-pack-updater.github-token"));

    }

    public void check(){

        //If new
        File file = new File(mauris.getDataFolder() + File.separator + "resourcepack");
        file.mkdirs();

        File resourcePackZip = new File(file + File.separator + "rp.zip");

        gitHubAPI.downloadRepositoryAsZip("AKUS-Studio", "nature-resourcepack", resourcePackZip);
        gitHubAPI.removeBranchFolder(resourcePackZip);

    }

}
