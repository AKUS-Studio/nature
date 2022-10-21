package works.akus.auth.utils;

import org.bukkit.GameMode;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;

public class LockedPlayer {

    public LockedPlayer(Player player){
        this.player = player;
    }

    Player player;
    ArmorStand stand;

    public void setStand(ArmorStand stand) {
        this.stand = stand;
    }

    public Player getPlayer() {
        return player;
    }

    public ArmorStand getStand() {
        return stand;
    }
}
