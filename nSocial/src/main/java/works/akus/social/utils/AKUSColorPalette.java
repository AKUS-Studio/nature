package works.akus.social.utils;

import net.kyori.adventure.text.format.TextColor;

public enum AKUSColorPalette {

    SIMPLE_GRAY("#B4B4B4"),
    ACCEPT("#79FF87"),
    REJECT("#ED8E8E");

    String hexColor;
    AKUSColorPalette(String hexColor){
        this.hexColor = hexColor;
    }

    public String getHexColor() {
        return hexColor;
    }

    public TextColor getTextColor(){
        return TextColor.fromHexString(hexColor);
    }

}
