package works.akus.mauris.objects.sounds;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Namespaced;
import net.kyori.adventure.sound.Sound;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.List;

public class CustomSound {

    String keyValue;
    String namespace;

    public CustomSound(String namespace, String keyValue){
        this.namespace = namespace;
        this.keyValue = keyValue;
    }

    public Sound.Source defaultCategory = Sound.Source.MASTER;

    public float defaultVolume = 1.f;
    public float defaultPitch = 1.f;

    // Playing Methods
    private Sound sound;
    private Audience audience;

    /***/

    public void playForAll(){
        playForAll(defaultCategory, defaultVolume, defaultPitch);
    }

    public void playForAll(Sound.Source category){
        playForAll(category, defaultVolume, defaultPitch);
    }

    public void playForAll(Sound.Source category, float volume){
        playForAll(category, volume, defaultPitch);
    }

    /**
     * Playing sound for all players on the server without Location nor Emitter
     */
    public void playForAll(Sound.Source category, float volume, float pitch){
        sound = Sound.sound(Key.key(namespace, keyValue), category, volume, pitch);
        audience = Audience.audience(Bukkit.getOnlinePlayers());

        audience.playSound(sound);
    }

    /***/

    /**
     * Playing sound for players without location
     */
    public void play(List<Player> players, Sound.Source category, float volume, float pitch){
        sound = Sound.sound(Key.key(namespace, keyValue), category, volume, pitch);
        audience = Audience.audience(players);

        audience.playSound(sound);
    }

    /**
     * Playing sound from location to Players
     */
    public void play(List<Player> players, Location location, Sound.Source category, float volume, float pitch){
        sound = Sound.sound(Key.key(namespace, keyValue), category, volume, pitch);
        audience = Audience.audience(players);

        audience.playSound(sound, location.getX(), location.getY(), location.getZ());
    }

    /**
     * Playing sound from emitter to Players
     */
    public void play(List<Player> players, Sound.Emitter emitter, Sound.Source category, float volume, float pitch){
        sound = Sound.sound(Key.key(namespace, keyValue), category, volume, pitch);
        audience = Audience.audience(players);

        audience.playSound(sound, emitter);
    }

    /***/
    public void play(double radius, Location location){
        play(radius, location, defaultCategory, defaultVolume, defaultPitch);
    }

    public void play(double radius, Location location, Sound.Source category){
        play(radius, location, category, defaultVolume, defaultPitch);
    }

    public void play(double radius, Location location, Sound.Source category, float volume){
        play(radius, location, category, volume, defaultPitch);
    }
    /***/

    /**
     * Playing sound from Location to the radius
     */
    public void play(double radius, Location location, Sound.Source category, float volume, float pitch){
        sound = Sound.sound(Key.key(namespace, keyValue), category, volume, pitch);
        audience = Audience.audience(location.getNearbyPlayers(radius));

        audience.playSound(sound, location.getX(), location.getY(), location.getZ());
    }
    /***/

    /**
     * Playing sound from Location to the Player
     */

    public void play(Player player, Location location, Sound.Source category, float volume, float pitch){
        sound = Sound.sound(Key.key(namespace, keyValue), category, volume, pitch);
        player.playSound(sound, location.getX(), location.getY(), location.getZ());
    }
    /***/

    /**
     * Playing sound from Emitter to the Player
     */
    public void play(Player player, Sound.Emitter emitter, Sound.Source category, float volume, float pitch){
        sound = Sound.sound(Key.key(namespace, keyValue), category, volume, pitch);
        player.playSound(sound, emitter);
    }
    /***/

    public void play(double radius, Sound.Emitter emitter){
        play(radius, emitter, defaultCategory, defaultVolume, defaultPitch);
    }

    /**
     * Playing sound from emitter to the radius
     */
    public void play(double radius, Sound.Emitter emitter, Sound.Source category, float volume, float pitch){
        if(!(emitter instanceof Entity)){
            throw new RuntimeException("Emitter should be an entity");
        }

        Location l = ((Entity) emitter).getLocation();

        sound = Sound.sound(Key.key(namespace, keyValue), category, volume, pitch);
        audience = Audience.audience(l.getNearbyPlayers(radius));

        audience.playSound(sound, emitter);
    }

    /**
     * Stopping sound to saved audience
     */
    public void stop(){
        if(sound == null || audience == null) return;
        audience.stopSound(sound);
    }

    public void stop(Player player){
        if(sound == null) return;
        player.stopSound(sound);
    }

    public void stop(double radius, Location location){
        if(sound == null) return;
        Audience.audience(location.getNearbyPlayers(radius)).stopSound(sound);
    }

    public void stopForAll(){
        if(sound == null) return;
        Audience.audience(Bukkit.getOnlinePlayers()).stopSound(sound);
    }

    // Getters And Setters

    public String getNamespace() {
        return namespace;
    }

    public String getKeyValue() {
        return keyValue;
    }

    public Sound.Source getDefaultCategory() {
        return defaultCategory;
    }

    public CustomSound setDefaultCategory(Sound.Source defaultCategory) {
        this.defaultCategory = defaultCategory;
        return this;
    }

    public float getDefaultVolume() {
        return defaultVolume;
    }

    public CustomSound setDefaultVolume(float defaultVolume) {
        this.defaultVolume = defaultVolume;
        return this;
    }

    public float getDefaultPitch() {
        return defaultPitch;
    }

    public CustomSound setDefaultPitch(float defaultPitch) {
        this.defaultPitch = defaultPitch;
        return this;
    }

}
