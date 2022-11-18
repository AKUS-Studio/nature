package works.akus.social.party;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.entity.Player;
import works.akus.social.commands.general.CommandManager;
import works.akus.social.general.SocialPlayer;
import works.akus.social.party.namegenerator.NameGenerator;
import works.akus.social.utils.AKUSColorPalette;
import works.akus.social.utils.ColorUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

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
            sender.getPlayer().sendMessage(getMessagePrefixText() + "В вашем пати только создатель может приглашать людей");
            return;
        }

        Player p = splayer.getPlayer();

        if(isInParty(splayer)) {
            sender.getPlayer().sendMessage(getMessagePrefixText() + p.getName() + " Уже в вашем пати");
            return;
        }

        if(splayer.getParty() != null){
            sender.getPlayer().sendMessage(getMessagePrefixText() + p.getName() + " Находится в чужом пати под названием " + splayer.getParty().getDisplayName());
            return;
        }

        partyOutcomeInvites.add(splayer);
        splayer.addPartyInvite(sender, this);

        sender.getPlayer().sendMessage(getMessagePrefixText() + "Успешно отправил заявку " + p.getName());

        final Component requestMessage = getMessagePrefixComponent().append(Component.text( "Пришло приглашение в пати")
                .color(getDisplayColor(50)))
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

    public String getMessagePrefixText(){
        return ColorUtils.format(getDisplayColor().asHexString() +
                displayName + " > " +
                getDisplayColor(50).asHexString());
    }

    public Component getMessagePrefixComponent(){
        return Component.text(displayName + " > ").color(getDisplayColor())
                .append(Component.text().color(getDisplayColor(50)));
    }

    /*
    Other
     */

    public void sendMessage(String message){
        sendMessage(Component.text(message));
    }

    public void sendMessage(Component component){
        for(SocialPlayer sp : playerList){
            sp.getPlayer().sendMessage(getMessagePrefixComponent().append(component));
        }
    }

    public String generateUniqueName(){
        return NameGenerator.getPhrase();
    }

    public TextColor getDisplayColor(int lightness){

        int r = Math.min(displayColor.red() + lightness, 255);
        int g = Math.min(displayColor.green() + lightness, 255);
        int b = Math.min(displayColor.blue() + lightness, 255);

        return TextColor.color(r, g , b);
    }

    final int generalColorLightness = 100;
    public TextColor generateColor(){

        Random rand = new Random();
        int r = rand.nextInt(255 - generalColorLightness) + generalColorLightness;
        int g = rand.nextInt(255 - generalColorLightness) + generalColorLightness;
        int b = rand.nextInt(255 - generalColorLightness) + generalColorLightness;


        return TextColor.color(r, g, b);
    }



}
