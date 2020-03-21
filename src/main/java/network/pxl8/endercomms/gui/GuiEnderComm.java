package network.pxl8.endercomms.gui;

import com.google.common.collect.Lists;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import network.pxl8.endercomms.lib.LibMeta;
import network.pxl8.endercomms.lib.LibUtil;
import network.pxl8.endercomms.network.EnderCommChannel;
import network.pxl8.endercomms.network.PacketDial;
import org.lwjgl.input.Keyboard;

import java.awt.Color;
import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class GuiEnderComm extends GuiScreen {
    private static final ResourceLocation ENDERCOMM_BASE = new ResourceLocation("endercomms","textures/gui/endercomm_base.png");
    private static final ResourceLocation ENDERCOMM_SCREEN = new ResourceLocation("endercomms","textures/gui/endercomm_screen.png");
    public enum Nav { DIAL, CONTACTS, BLOCKED }

    private int startX, startY, screenX, screenY, navbarX, navbarY, scale;
    private Color phoneColour, textColour, bgColour, fgColour, highlightColour;
    private GuiTextField searchField;
    private int selectNav;
    private List<GuiPlayerDisplay> dialDisplayList = Lists.newArrayList();
    private List<GuiPlayerDisplay> contactDisplayList = Lists.newArrayList();
    private List<GuiPlayerDisplay> blockDisplayList = Lists.newArrayList();

    @Override
    public boolean doesGuiPauseGame() { return false; }

    @Override
    public void initGui() {
        startX = (this.width - 144) / 2; startY = (this.height - 256) / 2;
        screenX = startX + 10; screenY = startY + 37;
        navbarX = startX + 9; navbarY = startY + 221;
        scale = new ScaledResolution(mc).getScaleFactor();

        selectNav = 0;

        getColours();

        Keyboard.enableRepeatEvents(true);
        createSearchField();

        initPlayerDisplays();
        initNav();
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (this.searchField.textboxKeyTyped(typedChar, keyCode)) {
            //updatePlayerList
        }
        else { super.keyTyped(typedChar, keyCode); }
    }
    
    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (mouseButton == 0)
        {
            for (GuiPlayerDisplay display : dialDisplayList) {
                LibMeta.LOG.info("Display id: " + display.getId() + ", press state: " + display.mousePressed(mc, mouseX, mouseY));
                switch (display.mousePressed(mc, mouseX, mouseY)) {
                    case DISPLAY:
                        display.toggleExpanded();
                        break;
                    case DIAL:
                        //send dial packet
                        EnderCommChannel.INSTANCE.sendToServer(new PacketDial(display.getPlayer().getUniqueID()));
                        this.mc.displayGuiScreen(null);
                        this.mc.setIngameFocus();
                        break;
                    case CONTACT_ADD:
                        //add to contacts and send packet to server
                        //contactDisplayList.add();
                        break;
                    case CONTACT_REMOVE:
                        //remove from contacts and send packet to server
                        break;
                    case BLOCK_ADD:
                        //add to blocked and send packet to server
                        break;
                    case BLOCK_REMOVE:
                        //remove from blocked and send packet to server
                        break;
                    case NULL:
                        display.clearExpand();
                        break;
                }
            }
            LibMeta.LOG.info(mouseX + ", " + mouseY);
        }

        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button instanceof GuiNavButton) { selectNav = button.id; }
    }

    @Override
    public void drawDefaultBackground() {
        super.drawDefaultBackground();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        drawPhone();

        drawClock();
        drawCharge();

        drawPhoneScreen(mouseX, mouseY, partialTicks);

        searchField.drawTextBox();

        for (GuiButton button : buttonList) { if (button instanceof GuiNavButton) { ((GuiNavButton) button).setSelected(button.id == selectNav); } }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private void drawPhone() {
        setTexture(ENDERCOMM_BASE);
        LibUtil.setGlSMColour(phoneColour);
        drawTexturedModalRect(startX, startY, 0, 0, 144, 256);

        setTexture(ENDERCOMM_SCREEN);
        LibUtil.setGlSMColour(fgColour);
        drawTexturedModalRect(startX + 8, startY + 35, 1, 1, 128, 182);
        LibUtil.setGlSMColour(bgColour);
        drawTexturedModalRect(startX + 10, startY + 37, 131, 3, 124, 160);
        drawTexturedModalRect(startX + 12, startY + 203, 133, 169, 120, 10);
        drawTexturedModalRect(startX + 8, startY + 220, 1, 186, 128, 20);
    }

    private void drawPhoneScreen(int mouseX, int mouseY, float partialTicks) {
        Nav navType = ((GuiNavButton) buttonList.get(selectNav)).getType();

        if (navType.equals(Nav.DIAL)) {
            for (GuiPlayerDisplay display : dialDisplayList) {
                display.drawDisplay(mc, mouseX, mouseY, partialTicks);
            }
        }
    }

    private void initNav() {
        buttonList.clear();
        buttonList.add(new GuiNavButton(0, Nav.DIAL, navbarX, navbarY, ENDERCOMM_BASE, 144, 16, highlightColour));
        buttonList.add(new GuiNavButton(1, Nav.CONTACTS, navbarX, navbarY, ENDERCOMM_BASE, 144, 32, highlightColour));
        buttonList.add(new GuiNavButton(2, Nav.BLOCKED, navbarX, navbarY, ENDERCOMM_BASE, 144, 48, highlightColour));
    }

    private void initPlayerDisplays() {
        List<EntityPlayer> players = mc.world.playerEntities;


        int id = 0;

        dialDisplayList.clear();
        for (EntityPlayer player : players) {
            dialDisplayList.add(new GuiPlayerDisplay(id, Nav.DIAL, player, this, screenX, screenY, 0, textColour, bgColour, fgColour, highlightColour));
            id++;

            //dialDisplayList.add(new GuiPlayerDisplay(id, Nav.DIAL, player, this, screenX, screenY, 0, textColour, bgColour, fgColour, highlightColour));
            //id++;
            //dialDisplayList.add(new GuiPlayerDisplay(id, Nav.DIAL, player, this, screenX, screenY, 0, textColour, bgColour, fgColour, highlightColour));
            //id++;
        }

        //Get and update contact and block list from server
    }

    protected List<GuiPlayerDisplay> getDialDisplayList() { return dialDisplayList; }

    private void drawClock() {
        World world = mc.world;
        long worldTimeInTicks = (world.getWorldTime()%24000);
        long worldTimeInSeconds = Math.round(worldTimeInTicks * 3.6) + 21600;

        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm").withZone(ZoneId.of("UTC"));
        Instant instant = Instant.ofEpochSecond(worldTimeInSeconds);

        fontRenderer.drawString(timeFormat.format(instant), startX + 12, startY + 21, LibUtil.getIntColour(textColour));
    }

    private void drawCharge() {
        boolean useBattery = true;

        int cur_pearls = 8;
        int max_pearls = 16;
        float cur_charge = 500;
        float max_charge = 1000;

        if (!useBattery) {
            String pearl_counter = (cur_pearls + "/" + max_pearls);
            int counter_width = this.fontRenderer.getStringWidth(pearl_counter);
            fontRenderer.drawString(pearl_counter, startX + 119 - counter_width, startY + 21, LibUtil.getIntColour(textColour));

            setTexture(ENDERCOMM_BASE);
            LibUtil.setGlSMColour();
            drawTexturedModalRect(startX + 120, startY + 16, 176, 0 , 16, 16);

        } else {
            int charge = Math.round((cur_charge / max_charge) * 100.0F);
            int barWidth = Math.round((cur_charge / max_charge) * 10.0F);

            String chargeDisplay = (charge + "%");
            int charge_width = this.fontRenderer.getStringWidth(chargeDisplay);
            fontRenderer.drawString(chargeDisplay, startX + 119 - charge_width, startY + 21, LibUtil.getIntColour(textColour));

            setTexture(ENDERCOMM_BASE);
            LibUtil.setGlSMColour(highlightColour);
            this.drawTexturedModalRect(startX + 120, startY + 16, 144, 0 , 16, 16);
            LibUtil.setGlSMColour(fgColour);
            this.drawTexturedModalRect(startX + 120, startY + 16, 160, 0 , 16, 16);

            if (barWidth > 0 && barWidth < 10) {
                drawRect(startX + 122, startY + 22, startX + 122 + barWidth, startY + 26, LibUtil.getIntColour(highlightColour));
            } else if (barWidth >= 10) {
                drawRect(startX + 122, startY + 22, startX + 132, startY + 26, LibUtil.getIntColour(highlightColour));
            }
        }
    }

    private void getColours() {
        //DEFAULTS
        phoneColour         = new Color(40, 40, 40, 255);
        textColour          = new Color(255, 255, 255, 255);
        bgColour            = new Color(139, 139, 139, 255);
        fgColour            = new Color(198, 198, 198, 255);
        highlightColour     = new Color(41, 142, 121, 255);

        phoneColour         = new Color(16, 16, 16, 255);
        textColour          = new Color(40, 178, 85, 255);
        bgColour            = new Color(4, 4, 4, 255);
        fgColour            = new Color(40, 40, 40, 255);
        highlightColour     = new Color(40, 178, 85, 255);
    }

    private void createSearchField() {
        searchField = new GuiTextField(0, this.fontRenderer, startX + 13, startY + 204, 112, this.fontRenderer.FONT_HEIGHT);
        searchField.setMaxStringLength(16);
        searchField.setEnableBackgroundDrawing(false);
        searchField.setTextColor(LibUtil.getIntColour(textColour));

        searchField.setVisible(true);
        searchField.setFocused(true);
        searchField.setCanLoseFocus(false);
    }

    private void setTexture(ResourceLocation texture) { mc.getTextureManager().bindTexture(texture); }
}