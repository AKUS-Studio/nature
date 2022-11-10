package works.akus.mauris.objects.fonts;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import works.akus.mauris.registry.GlyphRegistry;

public class GlyphBuilder {

	private TextComponent component;
	private String nextFont;

	public GlyphBuilder() {
		component = Component.empty();
	}

	public GlyphBuilder setFont(String font) {
		this.nextFont = font;
		return this;
	}

	public GlyphBuilder append(String text) {
		TextComponent appended = Component.text(text);

		if (nextFont != null) {
			appended.font(Key.key(nextFont));
		}

		return append(appended);
	}

	public GlyphBuilder append(GlyphBuilder builder) {
		this.component = this.component.append(builder.buildAsComponent());
		return this;
	}
	
	public GlyphBuilder append(TextComponent component) {
		this.component = this.component.append(component);
		return this;
	}

	public GlyphBuilder append(Glyph glyph) {
		return append(glyph.toTextComponent());
	}

	public GlyphBuilder offset(int pixels) {
		return append(GlyphRegistry.getOffset(pixels));
	}

	public GlyphBuilder leftShift(int pixels) {
		return offset(-pixels);
	}

	public GlyphBuilder rightShift(int pixels) {
		return offset(Math.abs(pixels));
	}

	public TextComponent buildAsComponent() {
		return this.component;
	}

	/**
	 * @deprecated Fonts aren't supported in vanilla strings
	 */
	public String buildAsString() {
		return component.toString();
	}

}
