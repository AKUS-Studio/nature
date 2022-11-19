package works.akus.social.party.events;

import works.akus.social.general.SocialPlayer;
import works.akus.social.party.Party;

public class PartyRemoveEvent extends PartyEvent {

    SocialPlayer player;

    public SocialPlayer getPlayer() {
        return player;
    }

    public PartyRemoveEvent(Party party, SocialPlayer socialPlayer) {
        super(party);
        this.player = socialPlayer;
    }

}
