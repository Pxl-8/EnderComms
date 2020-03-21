package network.pxl8.endercomms.gui;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.Gui;
import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import network.pxl8.endercomms.lib.LibMeta;
import network.pxl8.endercomms.lib.LibUtil;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.Map;

public class GuiPlayerDisplay extends Gui {
    private static final ResourceLocation ENDERCOMM_BASE = new ResourceLocation("endercomms","textures/gui/endercomm_base.png");
    public enum Pressed { DISPLAY, DIAL, CONTACT_ADD, CONTACT_REMOVE, BLOCK_ADD, BLOCK_REMOVE, NULL }

    private EntityPlayer player;
    private Color text, foreground, background, highlight;
    private int id, screenX, screenY, scrollOffset, x, y, width, height;
    private boolean expanded = false;
    private GuiEnderComm GUI;
    private GuiEnderComm.Nav navType;

    public GuiPlayerDisplay(int id, GuiEnderComm.Nav type, EntityPlayer player, GuiEnderComm gui, int screenX, int screenY, int scrollOffset, Color text, Color bg, Color fg, Color highlight)
    {
        this.id = id;
        this.player = player;
        this.navType = type;
        this.GUI = gui;

        this.screenX = screenX; this.screenY = screenY;
        this.scrollOffset = scrollOffset;
        this.width = 116; this.height = 18;

        this.x = screenX + 2;

        this.text = text; this.foreground = fg; this.background = bg; this.highlight = highlight;
    }

    public void drawDisplay(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        this.y = getOffsetY();
        int gui_scale = new ScaledResolution(mc).getScaleFactor();

        GL11.glEnable(GL11.GL_SCISSOR_TEST);

        setTexture(mc, ENDERCOMM_BASE);
        LibUtil.setGlSMColour();

        drawTexturedModalRect(this.x, getOffsetY(), 144, 144, 18, 18);

        setTexture(mc, getSkin(mc.getSkinManager(), player));
        LibUtil.setGlSMColour();
        drawModalRectWithCustomSizedTexture(this.x + 1, this.y + 1, 16, 16, 16, 16, 128, 128);

        setTexture(mc, ENDERCOMM_BASE);
        if (!expanded) {
            this.height = 18;
            LibUtil.setGlSMColour(foreground);
            drawTexturedModalRect(this.x + 20, this.y, 144, 162, 96, 18);
            LibUtil.setGlSMColour(background);
            drawTexturedModalRect(this.x + 23, this.y + 3, 147, 165, 90, 12);
        } else {
            this.height = 36;
            LibUtil.setGlSMColour(foreground);
            drawTexturedModalRect(this.x + 20, this.y, 144, 180, 96, 36);
            LibUtil.setGlSMColour(background);
            drawTexturedModalRect(this.x + 23, this.y + 3, 147, 183, 90, 12);

            drawExpansionButtons(mc, mouseX, mouseY);
        }

        mc.fontRenderer.drawString(player.getDisplayNameString(), this.x + 25, this.y + 5, LibUtil.getIntColour(text), true);

        GL11.glScissor(screenX * gui_scale, (mc.displayHeight - ((screenY * gui_scale) + (160 * gui_scale))), (124 * gui_scale), (160 * gui_scale));
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }

    private int getX(Pressed btn) {
        switch (btn) {
            case DIAL:
                return this.x + 21;
            case CONTACT_ADD:
                return this.x + 83;
            case CONTACT_REMOVE: case BLOCK_ADD: case BLOCK_REMOVE:
                return this.x + 99;
            default: return 0;
        }
    }
    private int getY(Pressed btn) {
        switch (btn) {
            case DIAL: case CONTACT_ADD: case CONTACT_REMOVE: case BLOCK_ADD: case BLOCK_REMOVE:
                return this.y + 19;
            default: return 0;
        }
    }

    public Pressed mousePressed(Minecraft mc, int mouseX, int mouseY) {
        switch (navType) {
            case DIAL:
                if (expanded && getHover(mouseX, mouseY, getX(Pressed.DIAL),            getY(Pressed.DIAL),             16, 16)) { return Pressed.DIAL; }
                if (expanded && getHover(mouseX, mouseY, getX(Pressed.CONTACT_ADD),     getY(Pressed.CONTACT_ADD),      16, 16)) { return Pressed.CONTACT_ADD; }
                if (expanded && getHover(mouseX, mouseY, getX(Pressed.BLOCK_ADD),       getY(Pressed.BLOCK_ADD),        16, 16)) { return Pressed.BLOCK_ADD; }
            case CONTACTS:
                if (expanded && getHover(mouseX, mouseY, getX(Pressed.DIAL),            getY(Pressed.DIAL),             16, 16)) { return Pressed.DIAL; }
                if (expanded && getHover(mouseX, mouseY, getX(Pressed.CONTACT_REMOVE),  getY(Pressed.CONTACT_REMOVE),   16, 16)) { return Pressed.CONTACT_REMOVE; }
            case BLOCKED:
                if (expanded && getHover(mouseX, mouseY, getX(Pressed.BLOCK_REMOVE),    getY(Pressed.BLOCK_REMOVE),     16, 16)) { return Pressed.BLOCK_REMOVE; }
            default:
                if (getHover(mouseX, mouseY, this.x, this.y, this.width, this.height)) {
                    return Pressed.DISPLAY;
                }
        }
        return Pressed.NULL;
    }

    private void drawExpansionButtons(Minecraft mc, int mouseX, int mouseY) {
        setTexture(mc, ENDERCOMM_BASE);
        switch (navType) {
            case DIAL:
                if (getHover(mouseX, mouseY, getX(Pressed.DIAL), getY(Pressed.DIAL), 16, 16)) {
                    drawExpansionButton(Pressed.DIAL, 160, 16, true);
                } else {
                    drawExpansionButton(Pressed.DIAL, 144, 16, false);
                }
                if (getHover(mouseX, mouseY, getX(Pressed.CONTACT_ADD), getY(Pressed.CONTACT_ADD), 16, 16)) {
                    drawExpansionButton(Pressed.CONTACT_ADD, 160, 32, true);
                    drawExpansionButton(Pressed.CONTACT_ADD, 176, 0, false);
                } else {
                    drawExpansionButton(Pressed.CONTACT_ADD, 144, 32, false);
                    drawExpansionButton(Pressed.CONTACT_ADD, 176, 0, false);
                }
                if (getHover(mouseX, mouseY, getX(Pressed.CONTACT_REMOVE), getY(Pressed.CONTACT_REMOVE), 16, 16)) {
                    drawExpansionButton(Pressed.BLOCK_ADD, 160, 48, true);
                    drawExpansionButton(Pressed.BLOCK_ADD, 208, 0, false);
                } else {
                    drawExpansionButton(Pressed.BLOCK_ADD, 144, 48, false);
                    drawExpansionButton(Pressed.BLOCK_ADD, 208, 0, false);
                }
                break;
            case CONTACTS:
                break;
            case BLOCKED:
                break;
        }
    }

    public void toggleExpanded() { this.expanded = !expanded; }
    public void clearExpand() { this.expanded = false; }

    private void drawExpansionButton(Pressed btn, int textureX, int textureY, boolean useHighlight) {
        if (useHighlight) {
            LibUtil.setGlSMColour(highlight);
        } else {
            LibUtil.setGlSMColour();
        }
        drawTexturedModalRect(getX(btn), getY(btn), textureX, textureY, 16, 16);
    }

    public int getOffsetY() {
        int offset = screenY + 2 + scrollOffset;
        for (GuiPlayerDisplay display : GUI.getDialDisplayList()) {
            if (display.getId() < this.id) {
                offset += display.getHeight() + 2;
            }
        }
        return offset;
    }

    private ResourceLocation getSkin(SkinManager skinManager, EntityPlayer player) {
        GameProfile profile = player.getGameProfile();
        Map<Type, MinecraftProfileTexture> skin = skinManager.loadSkinFromCache(profile);

        if (skin.containsKey(Type.SKIN)) {
            return skinManager.loadSkin(skin.get(Type.SKIN), Type.SKIN);
        } else {
            return DefaultPlayerSkin.getDefaultSkin(profile.getId());
        }
    }

    private boolean getHover(int mouseX, int mouseY, int x, int y, int width, int height) {
        boolean hover = (mouseX > (x - 1) && mouseX < x + width && mouseY > (y - 1) && mouseY < y + height);
        //LibMeta.LOG.info("Get Hover x-min: " + x + ", y-min: " + y + ", x-max: " + (x + width) + ", y-max: " + (y + height));
        return hover;
    }

    public EntityPlayer getPlayer(){ return this.player; }
    public int getId() { return this.id; }
    public int getHeight() { return this.height; }

    private void setTexture(Minecraft mc, ResourceLocation texture) { mc.getTextureManager().bindTexture(texture); }
}