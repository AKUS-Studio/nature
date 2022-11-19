package works.akus.social.party.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import works.akus.social.party.Party;

public class PartyEvent extends Event {

    Party party;

    public PartyEvent(Party party){
        this.party = party;
    }

    public Party getParty() {
        return party;
    }

    private static final HandlerList HANDLERS = new HandlerList();

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }



}
