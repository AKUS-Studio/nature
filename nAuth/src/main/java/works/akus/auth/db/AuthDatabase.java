package works.akus.auth.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.entity.Player;
import works.akus.auth.Auth;
import works.akus.auth.auth.AuthPlayer;
import works.akus.auth.auth.discord.DiscordAPI;
import works.akus.auth.auth.discord.DiscordUser;
import works.akus.auth.auth.discord.TokenInfo;

import java.io.File;
import java.io.IOException;
import java.sql.*;

//Do not kill me because of this class please
public class AuthDatabase {

    Auth auth;

    private static final String DATABASE_NAME = "AuthDB";
    private static final String DATABASE_PATH = "db";

    public void setUp(){
        auth = Auth.getInstance();

        createDatabase();
        createTables();
    }

    HikariConfig hikariConfig;
    HikariDataSource dataSource;
    private void createDatabase(){

        //Creating file
        File dataFolder = auth.getDataFolder();
        File dbFolder = new File(dataFolder.getAbsolutePath() + File.separator + DATABASE_PATH);
        dbFolder.mkdirs();

        File dbFile = new File(dbFolder.getAbsolutePath() + File.separator + DATABASE_NAME + ".db");
        if(!dbFile.exists()) {
            try {
                dbFile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        //Creating Database
        HikariConfig config = new HikariConfig();
        this.hikariConfig = config;

        config.setJdbcUrl("jdbc:sqlite:" + dbFile.getAbsolutePath());
        config.setConnectionTestQuery("SELECT 1");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        dataSource = new HikariDataSource(config);
    }

    public String getMinecraftUsername(String discord_id){
        try(Connection con = dataSource.getConnection()){
            String selectRequest = "SELECT * FROM AuthUsers WHERE discord_id='" +  discord_id + "'";

            ResultSet set = con.prepareStatement(selectRequest).executeQuery();
            if(!set.next()) return null;

            return set.getString("minecraft_username");
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    public AuthPlayer loadAuthPlayer(Player player){
        String minecraft_username = player.getName();
        try(Connection con = dataSource.getConnection()){

            // AuthUsers
            String selectRequest = "SELECT * FROM AuthUsers WHERE minecraft_username='" + minecraft_username +"'";

            ResultSet set = con.prepareStatement(selectRequest).executeQuery();
            if(!set.next()) return null;

            String discord_id = set.getString("discord_id");
            String last_ip = set.getString("last_ip");
            long last_logged = set.getLong("last_logged");
            long last_authorized = set.getLong("last_authorized");

            //TokenInfo
            String tokenSelectRequest = "SELECT * FROM TokenInfo WHERE discord_id='" + discord_id + "'";

            ResultSet tokenSet = con.prepareStatement(tokenSelectRequest).executeQuery();
            if(!tokenSet.next()) throw new RuntimeException("Token For This Person should exist discord_id=" + discord_id + " minecraft_username=" + minecraft_username);

            String access_token = tokenSet.getString("access_token");
            String refresh_token = tokenSet.getString("refresh_token");
            String expires_at = tokenSet.getString("expires_at");
            long taken_at = tokenSet.getLong("taken_at");

            TokenInfo ti = new TokenInfo(
                    access_token,
                    DiscordAPI.TOKEN_TYPE,
                    expires_at,
                    refresh_token,
                    DiscordAPI.SCOPES,
                    taken_at
            );

            DiscordUser user = new DiscordUser(discord_id, null, null, ti);
            AuthPlayer au = new AuthPlayer(user, player, last_ip, last_logged, last_authorized);

            return au;

        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    public void saveAuthPlayer(AuthPlayer authPlayer){

        //TODO
        String minecraft_username = authPlayer.getPlayer().getName();

        boolean exists = false;
        try(Connection con = dataSource.getConnection()){
            ResultSet set = con.prepareStatement("SELECT * FROM AuthUsers WHERE minecraft_username='"+ minecraft_username + "'").executeQuery();
            if(set.next()) exists = true;

            if(exists){

                String updateRequest = "UPDATE AuthUsers SET " +
                        "last_ip='" + authPlayer.getLastIp() + "'" +
                        ",last_logged='" + authPlayer.getLastLogged() + "'" +
                        ",last_authorized='" + authPlayer.getLastAuthorized() + "'" +
                        "WHERE minecraft_username='" + minecraft_username+ "'";

                con.prepareStatement(updateRequest).execute();

            }else{

                String insertIntoRequest = "INSERT INTO AuthUsers (minecraft_username,discord_id,last_logged,last_authorized,last_ip) VALUES (" +
                        "'" + minecraft_username + "'," +
                        "'" + authPlayer.getUser().getId() + "'," +
                        "'" + authPlayer.getLastLogged() +"'," +
                        "'" + authPlayer.getLastAuthorized() + "'," +
                        "'" + authPlayer.getLastIp() + "'" +
                        ");";

                con.prepareStatement(insertIntoRequest).execute();
            }
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    public void saveToken(String discordId, TokenInfo info){

        //TODO
        boolean exists = false;
        try(Connection con = dataSource.getConnection()){
            ResultSet set = con.prepareStatement("SELECT * FROM TokenInfo WHERE discord_id='"+ discordId + "'").executeQuery();
            if(set.next()) exists = true;

            if(exists){

                String updateRequest = "UPDATE TokenInfo SET " +
                        "access_token='" + info.getAccessToken() + "'" +
                        ",refresh_token='" + info.getRefreshToken() + "'" +
                        ",expires_at='" + info.getExpiresIn() + "'" +
                        ",taken_at='" + info.getTakenAt() + "'" +
                        "WHERE discord_id='" + discordId + "'";

                con.prepareStatement(updateRequest).execute();

            }else{

                String insertIntoRequest = "INSERT INTO TokenInfo (discord_id,access_token,refresh_token,expires_at,taken_at) VALUES (" +
                        "'" + discordId + "'," +
                        "'" + info.getAccessToken() + "'," +
                        "'" + info.getRefreshToken() +"'," +
                        "'" + info.getExpiresIn() + "'," +
                        "'" + info.getTakenAt() + "'" +
                        ");";

                con.prepareStatement(insertIntoRequest).execute();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void createTables(){
        String authUsersTableRequest = "CREATE TABLE IF NOT EXISTS AuthUsers (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "minecraft_username TEXT NOT NULL UNIQUE," +
                "discord_id TEXT NOT NULL UNIQUE," +
                "last_logged BIGINT," +
                "last_authorized BIGINT," +
                "last_ip TEXT" +
                ");";

        String tokenInfoTableRequest = "CREATE TABLE IF NOT EXISTS TokenInfo (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "discord_id TEXT," +
                "access_token TEXT," +
                "refresh_token TEXT," +
                "expires_at BIGINT," +
                "taken_at BIGINT" +
                ");";

        //
        try(Connection con = dataSource.getConnection()){
            con.prepareStatement(authUsersTableRequest).execute();
            con.prepareStatement(tokenInfoTableRequest).execute();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

}
