package works.akus.mauris.objects.menu.component.type;

import works.akus.mauris.objects.fonts.GlyphBuilder;

public interface Visual {

	/**
	 * @return Personal offset of this component
	 */
	public int getOffset();

	/**
	 * @return The visual part of this component
	 */
	public GlyphBuilder getVisual();

}
