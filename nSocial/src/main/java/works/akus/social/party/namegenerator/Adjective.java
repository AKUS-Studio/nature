package works.akus.social.party.namegenerator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Adjective {

    private static final List<String> lettersYI = List.of("к", "г", "х", "ж", "ш", "щ", "ч");
    private static final List<String> lettersYA = List.of("к", "г", "х");
    private static final List<String> lettersIYI = List.of("ч", "ш", "ж", "щ");

    public Adjective(String infinitive){
        this.infinitive = infinitive.toLowerCase();
    }

    public String infinitive;

    public String getInfinitive() {
        return infinitive;
    }

    public String formate(Noun noun){
        String inf = getInfinitive();

        if(noun.getGender() == Noun.Gender.MASCULINE && !noun.isPlural()){
            return infinitive;
        }

        String end = inf.substring(inf.length() - 2);
        String withoutEnd = inf.substring(0, inf.length() - 2);
        String letterBeforeEnd = inf.substring(inf.length() - 3, inf.length() - 2);

        String newEnd = "END_ERROR";

        if(noun.isPlural()){

            if(end.equalsIgnoreCase("ый")) newEnd = "ые";
            else if(end.equalsIgnoreCase("ой")) {
                if(lettersYI.contains(letterBeforeEnd)) newEnd = "ие";
                else newEnd = "ые";
            }
            else if(end.equalsIgnoreCase("ий")) newEnd = "ие";

        }
        else if(noun.gender == Noun.Gender.FEMININE){

            if(end.equalsIgnoreCase("ый")) newEnd = "ая";
            else if(end.equalsIgnoreCase("ой")) newEnd = "ая";
            else if(end.equalsIgnoreCase("ий")) {
                if(lettersYA.contains(letterBeforeEnd)) newEnd = "ая";
                else if(lettersIYI.contains(letterBeforeEnd)) newEnd = "ая";
                else newEnd = "яя";
            }

        }else if(noun.gender == Noun.Gender.NEUTER){

            if(end.equalsIgnoreCase("ый")) newEnd = "ое";
            else if(end.equalsIgnoreCase("ой")) newEnd = "ое";
            else if(end.equalsIgnoreCase("ий")) {
                if(lettersYA.contains(letterBeforeEnd)) newEnd = "ое";
                else newEnd = "ее";
            }

        }


        return withoutEnd + newEnd;
    }

}
