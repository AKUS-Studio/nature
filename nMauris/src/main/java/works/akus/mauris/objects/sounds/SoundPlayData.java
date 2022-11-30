package works.akus.mauris.objects.sounds;

import net.kyori.adventure.sound.Sound;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SoundPlayData {

    boolean isFromEmitter;
    boolean isFromLocation;

    Sound.Emitter emitter;
    Location location;
    Collection<Player> players;

    Float volume;
    Float pitch;
    Sound.Source source;

    double radius = -1;

    /*
    Creators are simple constructors
    if you want to change volume pitch or source you should set it yourself
     */

    //To All

    public static SoundPlayData create(){
        return create(getOnlinePlayers());
    }

    public static SoundPlayData create(Location location){
        return create()
                .setLocation(location);
    }

    public static SoundPlayData create(Sound.Emitter emitter){
        return create()
                .setEmitter(emitter);
    }

    //
    // To player
    //

    public static SoundPlayData create(Player player){
        return new SoundPlayData()
                .setPlayer(player);
    }

    public static SoundPlayData create(Player player, Location location){
        return create(player)
                .setLocation(location);
    }

    public static SoundPlayData create(Player player, Sound.Emitter emitter){
        return create(player)
                .setEmitter(emitter);
    }

    //
    // To Players
    //

    public static SoundPlayData create(Collection<Player> players){
        return new SoundPlayData()
                .setPlayers(players);
    }

    public static SoundPlayData create(Collection<Player> players, Location location){
        return create(players)
                .setLocation(location);
    }

    public static SoundPlayData create(Collection<Player> players, Sound.Emitter emitter){
        return create(players)
                .setEmitter(emitter);
    }

    //
    // To Radius
    //

    public static SoundPlayData create(double radius, Location location){
        return new SoundPlayData()
                .setRadius(radius)
                .setLocation(location);
    }

    public static SoundPlayData create(double radius, Sound.Emitter emitter){
        return create(radius, ((Entity)emitter).getLocation())
                .setEmitter(emitter);
    }


    // Setters and Adders

    public SoundPlayData setEmitter(Sound.Emitter emitter) {
        this.emitter = emitter;
        isFromEmitter = true;
        return this;
    }

    public SoundPlayData setSource(Sound.Source source) {
        this.source = source;
        return this;
    }

    public SoundPlayData setLocation(Location location) {
        this.location = location;
        isFromLocation = true;
        return this;
    }

    public SoundPlayData setPitch(float pitch) {
        this.pitch = pitch;
        return this;
    }

    public SoundPlayData setVolume(float volume) {
        this.volume = volume;
        return this;
    }

    public SoundPlayData setRadius(double radius) {
        this.radius = radius;
        return this;
    }

    public SoundPlayData setPlayer(Player p){
        players.clear();
        players.add(p);
        return this;
    }

    public SoundPlayData setPlayers(Collection<Player> players){
        this.players = players;
        return this;
    }

    public SoundPlayData addPlayer(Player p){
        players.add(p);
        return this;
    }

    public SoundPlayData addPlayers(Player... players){
        for(Player player : players){
            addPlayer(player);
        }
        return this;
    }

    //Getters


    public boolean isFromEmitter() {
        return isFromEmitter;
    }

    public boolean isFromLocation() {
        return isFromLocation;
    }

    public Sound.Emitter getEmitter() {
        return emitter;
    }

    public Location getLocation() {
        return location;
    }

    public static Collection<Player> getOnlinePlayers() {
        return new ArrayList<>(Bukkit.getOnlinePlayers());
    }

    public Collection<Player> getPlayers() {
        return players;
    }

    public Float getVolume(float defaultVolume) {
        if(volume == null) return defaultVolume;
        return volume;
    }

    public Float getVolume() {
        return volume;
    }

    public Float getPitch(float defaultPitch) {
        if(pitch == null) return defaultPitch;
        return pitch;
    }

    public Float getPitch() {
        return pitch;
    }

    public Sound.Source getSource(Sound.Source defaultSource) {
        if(source == null) return defaultSource;
        return source;
    }

    public Sound.Source getSource() {
        return source;
    }

    public double getRadius() {
        return radius;
    }
}
