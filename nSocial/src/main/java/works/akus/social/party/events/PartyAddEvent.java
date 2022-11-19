package works.akus.social.party.events;

import works.akus.social.general.SocialPlayer;
import works.akus.social.party.Party;

public class PartyAddEvent extends PartyEvent {

    SocialPlayer player;

    public PartyAddEvent(Party party, SocialPlayer player) {
        super(party);
        this.player = player;
    }

    public SocialPlayer getPlayer() {
        return player;
    }
}
