package works.akus.mauris.objects.fonts;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;

public class GlyphBuilder {

	private StringBuilder builder;
	private TextComponent component;
	
	public GlyphBuilder() {
		builder = new StringBuilder();
		component = Component.empty();
	}

	public GlyphBuilder setFont(String font) {
		return this;
	}

	public GlyphBuilder append(String text) {
		return this;
	}

	public GlyphBuilder append(TextComponent component) {
		return this;
	}

	public GlyphBuilder append(Glyph glyph) {
		return this;
	}

	public GlyphBuilder offset(int pixels) {
		return this;
	}

	public GlyphBuilder leftShift(int pixels) {
		return this;
	}

	public GlyphBuilder rightShift(int pixels) {
		return this;
	}

	public TextComponent buildAsComponent() {
		return null;
	}

	public TextComponent buildAsString() {
		return null;
	}

}
