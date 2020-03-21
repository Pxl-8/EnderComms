package network.pxl8.endercomms.network;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class EnderCommChannel {
    public static SimpleNetworkWrapper INSTANCE = null;

    private static int packetID = 0;

    public EnderCommChannel() {}

    public static int newID() { return packetID++; }

    public static void registerChannel(String channel) {
        INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(channel);
        registerMessages();
    }

    public static void registerMessages() {
        INSTANCE.registerMessage(PacketDial.Handler.class, PacketDial.class, newID(), Side.SERVER);
    }
}
