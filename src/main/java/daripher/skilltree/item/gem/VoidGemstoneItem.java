package daripher.skilltree.item.gem;

import java.util.List;

import org.apache.commons.lang3.tuple.Triple;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class VoidGemstoneItem extends GemstoneItem {
	public VoidGemstoneItem() {
		super(0x796767);
	}

	@Override
	public void insertInto(Player player, ItemStack itemStack, int gemstoneSlot, double gemstoneStrength) {
		itemStack.getTag().remove(GEMSTONES_TAG);
	}

	@Override
	public boolean canInsertInto(Player currentPlayer, ItemStack baseItem, int gemstoneSlot) {
		return GemstoneItem.hasGemstone(baseItem, 0);
	}

	@Override
	protected Triple<Attribute, Double, Operation> getGemstoneBonus(Player player, ItemStack itemStack) {
		return null;
	}

	@Override
	protected void appenBonusesTooltip(List<Component> components) {
		components.add(Component.translatable(getDescriptionId() + ".bonus").withStyle(ChatFormatting.GOLD));
	}
}
