package network.pxl8.endercomms.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import network.pxl8.endercomms.Chat;

import java.util.UUID;

public class PacketDial implements IMessage {
    private UUID player;

    @Override
    public void fromBytes(ByteBuf buf) {
        player = uuidFromBuffer(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        uuidToBuffer(buf, player);
    }

    public PacketDial() {}

    public PacketDial(UUID player) {
        this.player = player;
    }

    public UUID uuidFromBuffer(ByteBuf buf) {
        long leastSig = buf.readLong();
        long mostSig = buf.readLong();
        return new UUID(mostSig, leastSig);
    }

    public void uuidToBuffer(ByteBuf buf, UUID uuid) {
        buf.writeLong(uuid.getLeastSignificantBits());
        buf.writeLong(uuid.getMostSignificantBits());
    }

    public UUID getPlayer() {
        return this.player;
    }

    public static class Handler implements IMessageHandler<PacketDial, IMessage> {
        @Override
        public IMessage onMessage(PacketDial message, MessageContext context) {
            FMLCommonHandler.instance().getWorldThread(context.netHandler).addScheduledTask(() -> handle(message, context));
            return null;
        }

        private void handle(PacketDial message, MessageContext context) {
            EntityPlayer user = context.getServerHandler().player;
            World world = user.getEntityWorld();

            EntityPlayer targetPlayer = world.getPlayerEntityByUUID(message.getPlayer());
            if (targetPlayer == null) {
                user.sendStatusMessage(Chat.fail("chat.endercomms.teleport.no_player"), true);
                return;
            }

            if (user.equals(targetPlayer)) {
                user.sendStatusMessage(Chat.fail("chat.endercomms.teleport.invalid_self"), true);
            } else {
                ITextComponent targetName = targetPlayer.getDisplayName();
                user.sendStatusMessage(Chat.success("chat.endercomms.teleport.success", targetName), true);

                user.attemptTeleport(targetPlayer.posX, targetPlayer.posY, targetPlayer.posZ);
            }
        }
    }
}