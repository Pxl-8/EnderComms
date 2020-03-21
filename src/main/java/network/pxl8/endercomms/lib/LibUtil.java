package network.pxl8.endercomms.lib;

import net.minecraft.client.renderer.GlStateManager;

import java.awt.*;

public class LibUtil {
    public static void setGlSMColour(Color colour) {
        float red   = (float) colour.getRed()   / 255F;
        float green = (float) colour.getGreen() / 255F;
        float blue  = (float) colour.getBlue()  / 255F;
        float alpha = (float) colour.getAlpha() / 255F;
        GlStateManager.color(red, green, blue, alpha);
    }
    public static void setGlSMColour() {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    }

    public static int getIntColour(Color colour) {
        int alpha = colour.getAlpha(), red = colour.getRed(), green = colour.getGreen(), blue = colour.getBlue();
        int argb = ((alpha & 0xFF) << 24) + ((red & 0xFF) << 16) + ((green & 0xFF) << 8) + (blue & 0xFF);
        return argb;
    }
}
