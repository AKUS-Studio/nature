package works.akus.social.party;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.entity.Player;
import works.akus.social.commands.general.CommandManager;
import works.akus.social.general.SocialPlayer;
import works.akus.social.utils.AKUSColorPalette;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Party {

    private static HashMap<SocialPlayer, Party> partyKeeper = new HashMap<>();

    public Party(SocialPlayer owner){
        this.owner = owner;
        playerList = new ArrayList<>();
        playerList.add(owner);

        this.partyOutcomeInvites = new ArrayList<>();

        this.active = false;
        this.displayName = generateUniqueName();
        this.displayColor = generateColor();
    }

    //Is active if 2 or more players are in Party
    boolean active;

    boolean onlyOwnerCanInvitePlayers = true;

    String displayName;
    TextColor displayColor;

    SocialPlayer owner;
    List<SocialPlayer> playerList;

    List<SocialPlayer> partyOutcomeInvites;

    /*
    Party Invite Methods
     */
    public void accept(SocialPlayer sp){
        if(!partyOutcomeInvites.contains(sp)){
            return;
        }

        partyOutcomeInvites.remove(sp);
        sp.setParty(this);
        addPlayer(sp);
        sp.removePartyInvite(this);
    }

    public void removeOutComeInvite(SocialPlayer sp){
        partyOutcomeInvites.remove(sp);
    }

    public void invitePlayer(SocialPlayer sender, SocialPlayer splayer){
        if(onlyOwnerCanInvitePlayers && sender != owner){
            sender.getPlayer().sendMessage(getMessagePrefix() + "В вашем пати только создатель может приглашать людей");
            return;
        }

        Player p = splayer.getPlayer();

        if(isInParty(splayer)) {
            sender.getPlayer().sendMessage(getMessagePrefix() + p.getName() + "Уже в вашем пати");
            return;
        }

        if(splayer.getParty() != null){
            sender.getPlayer().sendMessage(getMessagePrefix() + p.getName() + "Находится в чужом пати под названием " + splayer.getParty().getDisplayName());
            return;
        }

        partyOutcomeInvites.add(splayer);
        splayer.addPartyInvite(sender, this);

        sender.getPlayer().sendMessage(getMessagePrefix() + "Успешно отправил заявку " + p.getName());

        final TextComponent requestMessage = Component.text(getMessagePrefix() + "Пришло приглашение в пати")
                .append(Component.text("\n"))
                .append(Component.text("[Принять]").clickEvent(ClickEvent.runCommand(
                        "/party accept "
                )).color(AKUSColorPalette.ACCEPT.getTextColor()))
                .append(Component.text("  "))
                .append(Component.text("[Отклонить]").clickEvent(ClickEvent.runCommand(
                        "/party deny "
                )).color(AKUSColorPalette.REJECT.getTextColor()));

        p.sendMessage(requestMessage);
    }

    /*
    Adders and Removers
     */

    public void addPlayers(SocialPlayer... players){
        for(SocialPlayer player : players) addPlayer(player);
    }

    public void addPlayer(SocialPlayer player){
        this.playerList.add(player);
        player.setParty(this);

        if(this.playerList.size() >= 2 && !active){
            owner.setParty(this);
            owner.clearPartyInvites();
            partyKeeper.put(owner, this);
            active = true;
        }
    }

    public void removePlayers(SocialPlayer... players){
        for(SocialPlayer player : players) removePlayer(player);
    }

    public void removePlayer(SocialPlayer player){
        this.playerList.remove(player);
        player.setParty(null);

        if(this.playerList.size() < 2){
            owner.setParty(null);
            this.active = false;
            partyKeeper.remove(this);
            return;
        }

        if(player == owner){
            partyKeeper.remove(this);
            owner = this.playerList.get(0);
            partyKeeper.put(owner, this);
        }
    }

    /*
    Getters
     */
    public List<SocialPlayer> getPlayers(){
        return playerList;
    }

    public boolean isOnlyOwnerCanInvitePlayers(){
        return onlyOwnerCanInvitePlayers;
    }

    public boolean isActive() {
        return active;
    }

    public TextColor getDisplayColor() {
        return displayColor;
    }

    public SocialPlayer getOwner() {
        return owner;
    }

    public List<SocialPlayer> getPartyOutcomeInvites() {
        return partyOutcomeInvites;
    }

    public static Party getParty(SocialPlayer sp){
        return partyKeeper.get(sp);
    }

    public boolean isInParty(SocialPlayer player){
        return playerList.contains(player);
    }

    public String getDisplayName(){
        return displayName;
    }

    public String getMessagePrefix(){
        return displayName + CommandManager.getPrefix();
    }

    /*
    Other
     */

    public void sendMessage(String message){
        sendMessage(Component.text(message));
    }

    public void sendMessage(Component component){
        for(SocialPlayer sp : playerList){
            sp.getPlayer().sendMessage(Component.text(getMessagePrefix()).append(component));
        }
    }

    //TODO
    public String generateUniqueName(){
        return "PartyPlaceholder";
    }

    public TextColor generateColor(){
        return AKUSColorPalette.SIMPLE_GRAY.getTextColor();
    }



}
