package works.akus.social.friend;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentBuilder;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import works.akus.social.commands.general.CommandManager;
import works.akus.social.general.SocialManager;
import works.akus.social.general.SocialPlayer;
import works.akus.social.general.WritableSource;
import works.akus.social.utils.AKUSColorPalette;

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
        String frname = fr.getOwnerFriend().getName();
        if(currentRequests.containsKey(frname)) return;
        currentRequests.put(frname, fr);


        final TextComponent requestMessage = Component.text("К вам запрос в друзья от " + frname).color(AKUSColorPalette.SIMPLE_GRAY.getTextColor())
                .append(Component.text("\n"))
                .append(Component.text("[Принять]").clickEvent(ClickEvent.runCommand(
                        "/friend accept " + frname
                )).color(AKUSColorPalette.ACCEPT.getTextColor()))
                .append(Component.text("  "))
                .append(Component.text("[Отклонить]").clickEvent(ClickEvent.runCommand(
                        "/friend deny " + frname
                )).color(AKUSColorPalette.REJECT.getTextColor()));

        getPlayer().sendMessage(requestMessage.compact());
    }

    public void addFriend(String name){
        friends.add(name);
    }

    public void removeFriend(String name){
        if(!getFriends().contains(name)) return;

        SocialPlayer sp = SocialManager.getSocialPlayer(name);

        if(!SocialManager.isOnline(name)){
            sp.save();
            return;
        }

        friends.remove(name);
        sp.getAsFriend().removeFriend(getName());
    }

    public void sendFriendRequest(String name){
        if(name.equals(getName())) return;
        if(getFriends().contains(name)) return;
        if(!SocialManager.isOnline(name)) return;

        SocialPlayer sp = SocialManager.getSocialPlayer(name);
        Friend fr = sp.getAsFriend();

        fr.addFriendRequest(new FriendRequest(this, fr));
    }

    public void denyFriendRequest(FriendRequest fr){
        if(fr == null) return;

        String from = fr.getOwnerFriend().getName();
        currentRequests.remove(from);
    }

    public void denyFriendRequest(String from){
        denyFriendRequest(getFriendRequest(from));
    }

    public void denyLastFriendRequest(){
        if(currentRequests.isEmpty()) return;
        denyFriendRequest(currentRequests.lastEntry().getValue());
    }

    public void acceptFriendRequest(FriendRequest fr){
        if(fr == null) return;

        String from = fr.getOwnerFriend().getName();


        currentRequests.remove(from);
        addFriend(from);
        if(!SocialManager.isOnline(from)){
            SocialPlayer sp = SocialManager.getSocialPlayer(from);
            sp.getAsFriend().addFriend(name);
            sp.save();
        }else{
            fr.getOwnerFriend().addFriend(name);
        }
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

    public FriendRequest getLastRequest(){
        return currentRequests.lastEntry().getValue();
    }

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
