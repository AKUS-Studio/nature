package works.akus.social.general;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import works.akus.social.Social;
import works.akus.social.friend.Friend;
import works.akus.social.party.Party;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class SocialPlayer {

    public SocialPlayer(YamlConfiguration config, File dataFile,
                        Friend friend){

        this.config = config;
        this.dataFile = dataFile;
        this.friend = friend;
        this.name = friend.getName();
        this.player = friend.getPlayer();

        partyInvites = new LinkedHashMap<>();
    }

    YamlConfiguration config;
    File dataFile;

    Friend friend;

    String name;
    Player player;

    Party party;
    LinkedHashMap<Party, SocialPlayer> partyInvites;
    /*
    Party Methods
     */

    public void setParty(Party party){
        clearPartyInvites();
        this.party = party;
    }

    public LinkedHashMap<Party, SocialPlayer> getPartyInvites(){
        return partyInvites;
    }

    public void clearPartyInvites(){
        for(Party party : getPartyInvites().keySet()){
            party.removeOutComeInvite(this);
        }

        partyInvites.clear();
    }

    public void addPartyInvite(SocialPlayer sender, Party party){
        partyInvites.put(party, sender);
    }

    public void removePartyInvite(Party party){
        partyInvites.remove(party);
    }

    /*
    GETTERS
     */

    public String getName(){
        return name;
    }

    public Party getParty(){
        return party;
    }

    public Player getPlayer(){
        player = Bukkit.getPlayer(name);
        return player;
    }

    public Friend getAsFriend(){
        return friend;
    }

    /*
    FILE UTILS
     */

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
