package works.akus.mauris.objects.sounds;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import works.akus.mauris.Mauris;

/**
 * For sounds that should be repeated until stopped
 */
public class SoundTask {

    CustomSound startSound;
    CustomSound repeatingSound;
    CustomSound endSound;

    SoundPlayData playData;
    
    JavaPlugin plugin;
    long period;
    long delay;
    
    public SoundTask(SoundPlayData data, CustomSound sound, long period, long delay){
        plugin = Mauris.getInstance();
        this.playData = data;
        this.repeatingSound = sound;
        this.period = period;
        this.delay = delay;
    }

    public SoundTask(SoundPlayData data, CustomSound repeatingSound, CustomSound startSound, long period, long delay){
        plugin = Mauris.getInstance();
        this.playData = data;
        this.startSound = startSound;
        this.repeatingSound = repeatingSound;
        this.period = period;
        this.delay = delay;
    }

    public SoundTask(SoundPlayData data, CustomSound repeatingSound, CustomSound startSound, CustomSound endSound, long period, long delay){
        plugin = Mauris.getInstance();
        this.playData = data;
        this.startSound = startSound;
        this.repeatingSound = repeatingSound;
        this.endSound = endSound;
        this.period = period;
        this.delay = delay;
    }
    
    BukkitTask task;
    boolean cancelSync;

    boolean canceled;
    boolean running;

    public void run() {
        if(repeatingSound == null || playData == null) return;

        canceled = false;

        if(startSound != null) startSound.play(playData);
        else repeatingSound.play(playData);

        running = true;
        task = new BukkitRunnable() {
            @Override
            public void run() {
                repeatingSound.play(playData);

                if(cancelSync && canceled){
                    if(endSound != null) endSound.play(playData);
                    task.cancel();
                }
            }
        }
        .runTaskTimer(plugin, delay, period);
    }

    public SoundPlayData getPlayData() {
        return playData;
    }

    public SoundTask setPlayData(SoundPlayData playData) {
        this.playData = playData;
        return this;
    }

    public SoundTask copy(){
        return new SoundTask(playData, repeatingSound, startSound, endSound, period, delay);
    }

    public static SoundTask empty(){
        return new SoundTask(null, null, 0, 0);
    }
    
    public void cancel(){
        if(!running) return;
        canceled = true;

        if(cancelSync) return;
        if(endSound != null) endSound.play(playData);
        task.cancel();
        running = false;
    }
}
