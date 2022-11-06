package works.akus.mauris.objects.fonts;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;

public class Glyph {

	private String name;
	private char unicode;
	private String font = "default";
	private int width = 3; // Will be used in future calculations

	public Glyph(String name, char unicode) {
		this.name = name;
		this.unicode = unicode;
	}

	public Glyph(String name, char unicode, String font) {
		this.name = name;
		this.unicode = unicode;
		this.font = font;
	}

	public Glyph(String name, char unicode, String font, int width) {
		this.name = name;
		this.unicode = unicode;
		this.font = font;
		this.width = width;
	}

	public String getName() {
		return name;
	}

	public char getUnicode() {
		return unicode;
	}

	public String getFont() {
		return font;
	}

	public int getWidth() {
		return width;
	}
	
	public TextComponent toTextComponent() {
		TextComponent component = font.equals("default") ? Component.text(unicode) : (TextComponent) Component.text(unicode).font(Key.key(font));
		
		return component;
	}

}
