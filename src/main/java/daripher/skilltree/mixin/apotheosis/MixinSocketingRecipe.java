package daripher.skilltree.mixin.apotheosis;

import java.util.List;

import javax.annotation.Nullable;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import daripher.skilltree.api.PlayerContainer;
import daripher.skilltree.compat.apotheosis.ApotheosisCompatibility;
import daripher.skilltree.util.PlayerHelper;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import shadows.apotheosis.adventure.affix.socket.SocketHelper;
import shadows.apotheosis.adventure.affix.socket.SocketingRecipe;

@Mixin(value = SocketingRecipe.class, remap = false)
public class MixinSocketingRecipe {
	@Redirect(method = { "matches",
		"m_5818_" }, at = @At(value = "INVOKE", target = "Lshadows/apotheosis/adventure/affix/socket/SocketHelper;hasEmptySockets(Lnet/minecraft/world/item/ItemStack;)Z"))
	private boolean checkPlayerSockets(ItemStack stack, Container container, Level level) {
		Player player = getPlayer(container);
		if (player == null) return SocketHelper.hasEmptySockets(stack);
		return ApotheosisCompatibility.ISNTANCE.hasEmptySockets(stack, player);
	}

	@Redirect(method = { "assemble", "m_5874_" }, at = @At(value = "INVOKE", target = "Ljava/util/List;set(ILjava/lang/Object;)Ljava/lang/Object;"))
	private Object applyGemPower(List<Object> gems, int index, Object gem, Container container) {
		Player player = getPlayer(container);
		if (player != null) {
			ItemStack result = container.getItem(0);
			float power = PlayerHelper.getGemPower(player, result);
			((ItemStack) gem).getOrCreateTag().putFloat("gem_power", power);
		}
		return gems.set(index, gem);
	}

	@Redirect(method = { "assemble",
		"m_5874_" }, at = @At(value = "INVOKE", target = "Lshadows/apotheosis/adventure/affix/socket/SocketHelper;getFirstEmptySocket(Lnet/minecraft/world/item/ItemStack;)I"))
	private int applyPlayerSockets(ItemStack stack, Container container) {
		Player player = getPlayer(container);
		if (player == null) return SocketHelper.getFirstEmptySocket(stack);
		int sockets = ApotheosisCompatibility.ISNTANCE.getSockets(stack, player);
		return ApotheosisCompatibility.ISNTANCE.getFirstEmptySocket(stack, sockets);
	}

	@Redirect(method = { "assemble",
		"m_5874_" }, at = @At(value = "INVOKE", target = "Lshadows/apotheosis/adventure/affix/socket/SocketHelper;getGems(Lnet/minecraft/world/item/ItemStack;)Ljava/util/List;"))
	private List<ItemStack> applyPlayerSockets2(ItemStack stack, Container container) {
		Player player = getPlayer(container);
		if (player == null) return SocketHelper.getGems(stack);
		int sockets = ApotheosisCompatibility.ISNTANCE.getSockets(stack, player);
		return ApotheosisCompatibility.ISNTANCE.getGems(stack, sockets);
	}

	private @Nullable Player getPlayer(Container container) {
		if (!(container instanceof PlayerContainer)) return null;
		return ((PlayerContainer) container).getPlayer().orElse(null);
	}
}
