package network.pxl8.endercomms.common.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import network.pxl8.endercomms.EnderComms;
import network.pxl8.endercomms.common.event.Register;
import network.pxl8.endercomms.lib.LibMeta;

public class ItemEnderComm extends Item {
    ItemEnderComm() {
        this.setCreativeTab(Register.enderCommTab);

        String name = "endercomm";
        this.setRegistryName(name);
        this.setUnlocalizedName(LibMeta.MOD_ID + "." + name);

        this.setMaxStackSize(1);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        if (world.isRemote) {
            LibMeta.LOG.info("Open ender comm gui");
            player.openGui(EnderComms.instance, 0, world, (int) player.posX, (int) player.posY, (int) player.posZ);
        }
        return new ActionResult<>(EnumActionResult.PASS, player.getHeldItem(hand));
    }
}