package works.akus.auth.auth.discord;

import com.sun.net.httpserver.HttpServer;
import org.bukkit.entity.Player;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.UUID;

public class OAuthDiscord {

    public final static int port = 9090;
    public final static String REDIRECT_URI = "http%3A%2F%2Flocalhost%3A9090%2Fauth";
    public final static String AUTH_LINK = "https://discord.com/oauth2/authorize?client_id=1032909655984328724&redirect_uri=" + REDIRECT_URI + "&response_type=code&scope=email%20identify";
    int TIMEOUT = 10000; // In seconds

    HashMap<String, Player> PendingUsers = new HashMap<>();
    HashMap<Player, String> PendingUsersInverse = new HashMap<>();

    HashMap<Player, OAuthTask> tasks = new HashMap<>();

    public void setUp(){
        PendingUsers.clear();
        PendingUsersInverse.clear();

        startServer();
    }

    //Server Stuff
    OAuthServer server;
    public void startServer(){
        server = new OAuthServer(port, "localhost", "auth");
        server.start(((code, state) -> {
            Player p = getPlayer(state);
            if(p == null) return false;

            OAuthTask task = tasks.get(p);
            if(task != null) task.run(null);
            removePending(p);
            return true;
        }));
    }

    public void stopServer(){
        server.stop();
    }

    //
    /**
     *
     * @param player Player that needed to generate ID
     * @param task Will run when user is Authorized
     * @return Generated Authentication URL
     */
    public String addPending(Player player, OAuthTask task){
        if(PendingUsersInverse.containsKey(player)) removePending(player);

        String authid = generateId();
        PendingUsers.put(authid, player);
        PendingUsersInverse.put(player, authid);
        tasks.put(player, task);

        return AUTH_LINK + "&state=" + authid;
    }

    private void removePending(Player p){
        String authId = PendingUsersInverse.get(p);
        if(authId == null) return;
        PendingUsersInverse.remove(p);
        PendingUsers.remove(authId);
        tasks.remove(p);
    }

    public Player getPlayer(String authId){
        return PendingUsers.get(authId);
    }

    public String getAuthId(Player p){
        return PendingUsersInverse.get(p);
    }

    public String getAuthLink(Player p){
        return AUTH_LINK + "&state=" + getAuthId(p);
    }

    private String generateId(){
        return UUID.randomUUID().toString();
    }


}
