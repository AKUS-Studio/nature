package works.akus.mauris.objects.fonts;

import java.util.Map;

import com.google.common.collect.Maps;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;

public class Fonts {

	private static Map<String, Glyph> GLYPH_MAP = Maps.newHashMap();
	private static Map<Character, Glyph> GLYPH_MAP_REVERSED = Maps.newHashMap();

	private static final String OFFSET_FONT_NAME = "offset";
	private static Map<Integer, Character> OFFSET_MAP;
	static {
		OFFSET_MAP = Maps.newHashMap();
		OFFSET_MAP.put(-1, '\uE000');
		OFFSET_MAP.put(-2, '\uE001');
		OFFSET_MAP.put(-4, '\uE002');
		OFFSET_MAP.put(-8, '\uE003');
		OFFSET_MAP.put(-16, '\uE004');
		OFFSET_MAP.put(-32, '\uE005');
		OFFSET_MAP.put(-64, '\uE006');
		OFFSET_MAP.put(-128, '\uE007');
		OFFSET_MAP.put(1, '\uE008');
		OFFSET_MAP.put(2, '\uE009');
		OFFSET_MAP.put(4, '\uE010');
		OFFSET_MAP.put(8, '\uE011');
		OFFSET_MAP.put(16, '\uE012');
		OFFSET_MAP.put(32, '\uE013');
		OFFSET_MAP.put(64, '\uE014');
		OFFSET_MAP.put(128, '\uE015');
	}


	// -- Registering Glyphs --
	
	public static void unregisterGlyph(String name) {
		if (!GLYPH_MAP.containsKey(name))
			return;
		final char unicode = GLYPH_MAP.get(name).getUnicode();

		GLYPH_MAP.remove(name);
		GLYPH_MAP_REVERSED.remove(unicode);
	}

	public static Glyph registerGlyph(Glyph glyph) {
		final String name = glyph.getName();
		final char unicode = glyph.getUnicode();

		GLYPH_MAP.put(name, glyph);
		GLYPH_MAP_REVERSED.put(unicode, glyph);

		return glyph;
	}
	
	// -- Glyph Utilities --
	
	public static Glyph getGlyph(String name) {
		return GLYPH_MAP.getOrDefault(name, null);
	}

	public static Glyph getGlyph(char unicode) {
		return GLYPH_MAP_REVERSED.getOrDefault(unicode, null);
	}


	/**
	 * Replaces all placeholders (Glyph names), surrounded with :glyphName: with the
	 * unicode corresponding to the given glyph
	 */
	public static String process(String text) {
		String result = text;

		for (String glyphName : GLYPH_MAP.keySet()) {
			if (!text.contains(glyphName)) {
				continue;
			}

			result = result.replaceAll(":" + glyphName + ":", String.valueOf(GLYPH_MAP.get(glyphName).getUnicode()));
		}

		return result;
	}
	
	// -- Offsets --

	/**
	 * Generates a TextComponent, consisting of negative space symbols that amount
	 * to the given offset in pixels.
	 * 
	 * @param pixels The offset, either negative (left) or positive (right)
	 * @return
	 */
	public static TextComponent getOffset(int pixels) {
		return (TextComponent) Component.text(getOffsetText(pixels)).font(Key.key(OFFSET_FONT_NAME));
	}

	private static String getOffsetText(int pixels) {
		final StringBuilder builder = new StringBuilder();
		if (pixels == 0)
			return builder.toString(); // Return empty string

		final boolean negative = Integer.signum(pixels) == -1;
		pixels = Math.abs(pixels); // Get negative pixels absolute value

		while (pixels > 0) {
			int highestBit = Integer.highestOneBit(pixels);
			if (highestBit > 128)
				highestBit = 128; // Max is 128
			builder.append(OFFSET_MAP.get(negative ? -highestBit : highestBit));

			pixels -= highestBit;
		}

		return builder.toString();
	}
}
