package network.pxl8.endercomms.common.item;

import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import network.pxl8.endercomms.lib.LibMeta;

@GameRegistry.ObjectHolder(LibMeta.MOD_ID)
public class ModItems {
    @GameRegistry.ObjectHolder("endercomm") public static ItemEnderComm enderComm;

    public static void register(IForgeRegistry<Item> itemReg) {
        itemReg.register(new ItemEnderComm());
    }
}
