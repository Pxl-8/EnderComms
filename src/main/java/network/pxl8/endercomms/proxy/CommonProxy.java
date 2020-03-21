package network.pxl8.endercomms.proxy;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import network.pxl8.endercomms.EnderComms;
import network.pxl8.endercomms.gui.GuiHandler;
import network.pxl8.endercomms.lib.LibMeta;
import network.pxl8.endercomms.network.EnderCommChannel;

public class CommonProxy implements Proxy {
    @Override
    public void preInit() {
    }

    @Override
    public void init() {
        NetworkRegistry.INSTANCE.registerGuiHandler(EnderComms.instance, new GuiHandler());
        EnderCommChannel.registerChannel(LibMeta.MOD_ID);
    }

    @Override
    public void postInit() {
    }
}
