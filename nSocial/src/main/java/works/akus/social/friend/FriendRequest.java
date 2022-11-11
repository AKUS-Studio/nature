package works.akus.social.friend;

import org.bukkit.entity.Player;

public class FriendRequest {

    public FriendRequest(Friend from, Friend to){
        this.owner = from;
        this.to = to;
        timestamp = System.currentTimeMillis();
    }

    long timestamp;

    Friend owner;
    Friend to;

    public Friend getOwnerFriend(){
        return owner;
    }

    public Friend getToFriend(){
        return to;
    }

    public Player getOwnerPlayer(){
        return owner.getPlayer();
    }

    public Player getToPlayer(){
        return to.getPlayer();
    }

    public long getTimestamp() {
        return timestamp;
    }
}
