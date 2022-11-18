package works.akus.social.party.namegenerator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NameGenerator {

    static List<Adjective> adjectives;
    static List<Noun> nouns;

    public static void load(){
        adjectives = new ArrayList<>();
        nouns = new ArrayList<>();

        JSONObject jsonNouns = getJsonFromResources("party/nouns.json");
        JSONObject jsonAdjectives = getJsonFromResources("party/adjectives.json");

        //Nouns
        for(Object object : (JSONArray)jsonNouns.get("nouns")){
            JSONObject jsonObject = (JSONObject) object;

            //Gender
            String genderName = (String) jsonObject.get("gender");
            Noun.Gender gender = Noun.Gender.MASCULINE;
            if(genderName != null) gender = Noun.Gender.getGender(genderName.toLowerCase());

            //Plural
            Boolean pluralJson = (Boolean) jsonObject.get("plural");
            boolean plural = false;
            if(pluralJson != null) plural = pluralJson;

            // Infinitive
            String infinitive = (String) jsonObject.get("noun");
            Noun noun = new Noun(infinitive, gender, plural);

            nouns.add(noun);
        }

        //Adjectives
        for(Object object : (JSONArray)jsonAdjectives.get("adjectives")){
            String s = (String) object;
            adjectives.add(new Adjective(s));
        }
    }

    public static String getPhrase(){
        Random r = new Random();

        Adjective adj = adjectives.get(r.nextInt(adjectives.size()));
        Noun noun = nouns.get(r.nextInt(nouns.size()));

        String adjFormated = adj.formate(noun);
        adjFormated = adjFormated.substring(0, 1).toUpperCase() + adjFormated.substring(1);

        return adjFormated + " " + noun.get();
    }

    private static JSONObject getJsonFromResources(String fileName){
        // The class loader that loaded the class

        ClassLoader classLoader = NameGenerator.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);

        // the stream holding the file content
        try{
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject)jsonParser.parse(
                    new InputStreamReader(inputStream, "UTF-8"));

            return jsonObject;
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

}
