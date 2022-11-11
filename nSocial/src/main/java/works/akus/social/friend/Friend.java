package works.akus.social.friend;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import works.akus.social.commands.general.CommandManager;
import works.akus.social.general.SocialManager;
import works.akus.social.general.SocialPlayer;
import works.akus.social.general.WritableSource;

import java.util.*;

public class Friend extends WritableSource {

    public Friend(String name, List<String> friends){
        this.name = name;
        this.friends = friends;
        currentRequests = new TreeMap<>();
    }

    String name;

    List<String> friends;

    NavigableMap<String, FriendRequest> currentRequests;

    public Player getPlayer(){
        return Bukkit.getPlayer(name);
    }

    //
    // WRITE AND READ YAMLConfiguration
    //

    @Override
    public void write(){
        sec.set("friends", friends);
    }

    public static Friend read(String name, YamlConfiguration config){

        String secName = Friend.class.getSimpleName();
        ConfigurationSection sec = config.getConfigurationSection(secName);
        if(sec == null){
            sec = config.createSection(secName);
        }

        return read(name, sec);
    }

    private static Friend read(String name, ConfigurationSection sec){
        List<String> friends = sec.getStringList("friends");

        Friend f = new Friend(name, friends);
        f.setSec(sec);

        return f;
    }

    //
    // ADDERS
    //

    private void addFriendRequest(FriendRequest fr){
        if(currentRequests.containsKey(fr.getOwnerPlayer().getName())) return;
        currentRequests.put(fr.getOwnerPlayer().getName(), fr);

        getPlayer().sendMessage(CommandManager.getPrefix() + "Получил запрос от " + fr.getOwnerPlayer().getName());
    }

    private void addFriendRequest(Friend f){
        if(currentRequests.containsKey(f.getName())) return;
        FriendRequest fr = new FriendRequest(f, this);

        addFriendRequest(fr);
    }

    public void addFriend(String name){
        friends.add(name);
    }

    public void removeFriend(String name){
        if(!getFriends().contains(name)) return;

        SocialPlayer sp = SocialManager.getSocialPlayer(name);
        sp.getAsFriend().removeFriend(getName());

        if(!SocialManager.isOnline(name)){
            sp.save();
            return;
        }

        friends.remove(name);
    }

    public void sendFriendRequest(String name){
        if(name.equals(getName())) return;
        if(getFriends().contains(name)) return;
        if(!SocialManager.isOnline(name)) return;

        SocialPlayer sp = SocialManager.getSocialPlayer(name);
        Friend fr = sp.getAsFriend();

        fr.addFriendRequest(new FriendRequest(this, fr));
    }

    public void acceptFriendRequest(FriendRequest fr){
        if(fr == null) return;

        String from = fr.getOwnerFriend().getName();

        currentRequests.remove(from);
        addFriend(from);
        fr.getOwnerFriend().addFriend(getName());
    }

    public void acceptFriendRequest(String from){
        acceptFriendRequest(getFriendRequest(from));
    }

    public void acceptLastFriendRequest(){
        if(currentRequests.isEmpty())return;

        acceptFriendRequest(currentRequests.lastEntry().getValue());
    }

    //
    // GETTERS
    //

    public String getName() {
        return name;
    }

    public NavigableMap<String, FriendRequest> getFriendRequests(){
        return currentRequests;
    }

    /**
     * @return Get FriendRequest from Current Friend Requests
     */
    public FriendRequest getFriendRequest(String name){
        return currentRequests.get(name);
    }

    /**
     * @return Get Friend Object of Friend of that FriendPlayer
     */
    public Friend getFriend(String name){
        if(!friends.contains(name)) return null;

        return SocialManager.getSocialPlayer(name).getAsFriend();
    }

    /**
     * @return Friends of that FriendPlayer
     */
    public List<String> getFriends() {
        return friends;
    }



}
