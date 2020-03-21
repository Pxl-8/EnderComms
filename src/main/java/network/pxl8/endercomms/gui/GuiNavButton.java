package network.pxl8.endercomms.gui;

import com.sun.org.apache.xpath.internal.operations.Bool;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;
import network.pxl8.endercomms.lib.LibUtil;

import java.awt.*;

public class GuiNavButton extends GuiButton {
    private GuiEnderComm.Nav type;
    private ResourceLocation texture;
    private int texOffX, texOffY, texOnX, texOnY;
    private Color highlight;
    private boolean selected;

    public GuiNavButton(int id, GuiEnderComm.Nav type, int start_x, int start_y, ResourceLocation texture, int textureX, int textureY, Color highlight)
    {
        super(id, (start_x + (18 * id)), start_y, 18, 18, "");

        this.type = type;
        this.texture = texture;
        this.texOffX = textureX; this.texOffY = textureY;
        this.texOnX = textureX + 16; this.texOnY = textureY;
        this.highlight = highlight;
        this.selected = false;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        int xMin = this.x, yMin = this.y;
        int xMax = xMin + this.width + 1, yMax = yMin + this.height + 1;
        boolean hover = (mouseX > xMin && mouseX < xMax && mouseY > yMin && mouseY < yMax);

        if (hover || selected) {
            LibUtil.setGlSMColour(highlight);
            setTexture(mc, texture);
            drawTexturedModalRect(this.x + 1, this.y + 1, texOnX, texOnY, 16, 16);
        } else {
            LibUtil.setGlSMColour();
            setTexture(mc, texture);
            drawTexturedModalRect(this.x + 1, this.y + 1, texOffX, texOffY, 16, 16);
        }

        if (selected) {
            drawRect(this.x, this.y + 17, this.x + 18, this.y + 19, LibUtil.getIntColour(highlight));
        }
    }

    public GuiEnderComm.Nav getType() { return type; }

    public void setSelected(Boolean select) { this.selected = select; }
    private void setTexture(Minecraft mc, ResourceLocation texture) { mc.getTextureManager().bindTexture(texture); }
}
