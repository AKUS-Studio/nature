package works.akus.social.general;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import works.akus.social.Social;
import works.akus.social.friend.Friend;

import java.io.File;
import java.io.IOException;

public class SocialPlayer {

    public SocialPlayer(YamlConfiguration config, File dataFile,
                        Friend friend){

        this.config = config;
        this.dataFile = dataFile;
        this.friend = friend;
        this.name = friend.getName();
        this.player = friend.getPlayer();
    }

    YamlConfiguration config;
    File dataFile;

    Friend friend;

    String name;
    Player player;

    public Player getPlayer(){
        player = Bukkit.getPlayer(name);
        return player;
    }

    public Friend getAsFriend(){
        return friend;
    }

    public void save(){
        friend.write();

        try {
            config.save(dataFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isSocialPlayer(String name){
        File file = getPlayerDataFile(name);
        return file.exists();
    }

    public static SocialPlayer load(String name){
        File file = createPlayerDataFile(name);
        YamlConfiguration playerData = YamlConfiguration.loadConfiguration(file);

        Friend friend = Friend.read(name, playerData);

        return new SocialPlayer(playerData, file, friend);
    }

    public static final String DATA_DIR = "data/players/";
    private static File getPlayerDataFile(String name){
        File dir = new File(Social.get().getDataFolder() + File.separator + DATA_DIR);
        File file = new File(dir + File.separator + name + ".yml");

        return file;
    }

    private static File createPlayerDataFile(String name){
        File dir = new File(Social.get().getDataFolder() + File.separator + DATA_DIR);
        dir.mkdirs();
        File file = new File(dir + File.separator + name + ".yml");

        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return file;
    }

}
