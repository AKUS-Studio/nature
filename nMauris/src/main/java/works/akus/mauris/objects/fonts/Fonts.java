package works.akus.mauris.objects.fonts;

import java.util.Map;

import com.google.common.collect.Maps;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;

public class Fonts {

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

	public static Glyph getGlyph(String name) {
		return null;
	}

	public static Glyph getGlyph(String name, String font) {
		return null;
	}

	public static String process(String text) {
		return null;
	}

	public static String process(String text, String font) {
		return null;
	}

	/**
	 * Generates a String, consisting of negative space symbols that amount to the
	 * given offset in pixels.
	 * 
	 * @deprecated Use getOffsetComponent
	 * @param pixels The offset, either negative (left) or positive (right)
	 * @return
	 */
	public static String getOffsetString(int pixels) {
		final StringBuilder builder = new StringBuilder();
		if (pixels == 0) return builder.toString(); // Return empty string
		
		final boolean negative = Integer.signum(pixels) == -1;
		pixels = Math.abs(pixels); // Get negative pixels absolute value
		
		while (pixels > 0) {
			int highestBit = Integer.highestOneBit(pixels);
			if (highestBit > 128) highestBit = 128; // Max is 128
			builder.append(OFFSET_MAP.get(negative ? -highestBit : highestBit));
			
			pixels -= highestBit;
		}

		return builder.toString();
	}

	/**
	 * Generates a TextComponent, consisting of negative space symbols that amount
	 * to the given offset in pixels.
	 * 
	 * @param pixels The offset, either negative (left) or positive (right)
	 * @return
	 */
	public static TextComponent getOffsetComponent(int pixels) {
		return (TextComponent) Component.empty().font(Key.key(OFFSET_FONT_NAME));
	}

}
