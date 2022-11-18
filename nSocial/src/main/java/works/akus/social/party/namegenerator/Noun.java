package works.akus.social.party.namegenerator;

import java.util.HashMap;

public class Noun {

    public static final HashMap<String, Gender> genderAliases = new HashMap<>();


    public Noun(String infinitive, Gender gender, boolean plural){
        this.infinitive = infinitive.toLowerCase();
        this.gender = gender;
        this.plural = plural;
    }

    public Noun(String infinitive, Gender gender){
        this.infinitive = infinitive.toLowerCase();
        this.gender = gender;
        this.plural = false;
    }

    String infinitive;
    Gender gender;

    boolean plural;

    public String getInfinitive() {
        return infinitive;
    }

    public String get(){
        return infinitive;
    }

    public Gender getGender() {
        return gender;
    }

    public boolean isPlural() {
        return plural;
    }

    enum Gender{
        MASCULINE("m", "male"),
        FEMININE("f", "female"),
        NEUTER("n", "-", "none");

        Gender(String... aliases){

            genderAliases.put(this.name().toLowerCase(), this);
            for(String s : aliases){
                genderAliases.put(s.toLowerCase(), this);
            }

        }

        public static Gender getGender(String name){
            return genderAliases.get(name.toLowerCase());
        }

    }

}
