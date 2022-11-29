package works.akus.mauris.registry;

import com.google.common.collect.Maps;
import works.akus.mauris.objects.fonts.Glyph;
import works.akus.mauris.objects.sounds.CustomSound;

import java.util.Map;

public class SoundRegistry {

    private static Map<String, CustomSound> SOUNDS_MAP = Maps.newHashMap();
    private static Map<String, CustomSound> SOUNDS_SIMPLE_MAP = Maps.newHashMap();

    public static CustomSound getSound(String value){
        return SOUNDS_SIMPLE_MAP.get(value);
    }

    public static CustomSound getSound(String namespace, String value){
        return SOUNDS_MAP.get(formateId(namespace, value));
    }

    public static void unregisterSound(String value) {
        CustomSound sound = SOUNDS_SIMPLE_MAP.get(value);
        if(sound == null) return;
        String formate = formateId(sound.getNamespace(), sound.getKeyValue());

        SOUNDS_MAP.remove(formate);
        SOUNDS_SIMPLE_MAP.remove(value);
    }

    public static void unregisterSound(String namespace, String value) {
        SOUNDS_MAP.remove(formateId(namespace, value));
    }

    public static void registerSound(CustomSound sound) {
        SOUNDS_MAP.put(formateId(sound.getNamespace(), sound.getKeyValue()), sound);
        SOUNDS_SIMPLE_MAP.put(sound.getKeyValue(), sound);
    }

    private static String formateId(String namespace, String value){
        return namespace + ":" + value;
    }

}
