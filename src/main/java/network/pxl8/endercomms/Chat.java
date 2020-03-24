package network.pxl8.endercomms;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

public class Chat {
	public static ITextComponent success(String key, Object... args) {
		return new TextComponentTranslation(key, args).setStyle(new Style().setColor(TextFormatting.GREEN));
	}

	public static ITextComponent fail(String key, Object... args) {
		return new TextComponentTranslation(key, args).setStyle(new Style().setColor(TextFormatting.RED));
	}
}
