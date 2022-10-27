package works.akus.mauris.utils;

import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;

import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundOpenScreenPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.MenuType;

public class InventoryUtils {

	public static void setPlayerInventoryTitle(Player player, String newTitle) {
		try {
			InventoryType openType = player.getOpenInventory().getType();
			if (openType == InventoryType.CRAFTING || openType == InventoryType.CREATIVE) return;
			
			final ServerPlayer serverPlayer = ((CraftPlayer) player).getHandle();
			final int windowId = serverPlayer.containerMenu.containerId;
			final MenuType<?> type = serverPlayer.containerMenu.getType();

			ClientboundOpenScreenPacket packet = new ClientboundOpenScreenPacket(windowId, type, Component.literal(newTitle));
			serverPlayer.connection.send(packet);

			player.updateInventory();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

}
