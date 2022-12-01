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
import org.checkerframework.checker.units.qual.A;

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


    /**
     * via SoundPlayData
     */
    public void play(SoundPlayData data){

        audience = Audience.audience(data.players);
        sound = Sound.sound(Key.key(namespace, keyValue), data.getSource(defaultCategory), data.getVolume(defaultVolume), data.getPitch(defaultPitch));
        Location location;
        if(data.isFromEmitter()) location = ((Entity)data.getEmitter()).getLocation();
                else location = data.getLocation();

        if(data.getRadius() > 0){
            audience = Audience.audience(location.getNearbyPlayers(data.getRadius()));
        }

        if(data.isFromEmitter()){
            audience.playSound(sound, data.getEmitter());
        }
        else if(data.isFromLocation){
            audience.playSound(sound, data.getLocation().getX(), data.getLocation().getY(), data.getLocation().getZ());
        }
        else{
            audience.playSound(sound);
        }

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
