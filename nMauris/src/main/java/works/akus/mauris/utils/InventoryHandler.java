package works.akus.mauris.utils;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.InternalStructure;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.events.PacketListener;
import com.comphenix.protocol.wrappers.WrappedChatComponent;

import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import works.akus.mauris.Mauris;

public class InventoryHandler {

	private ConcurrentHashMap<String, OpenContainerData> containerData = new ConcurrentHashMap<>();

	private ProtocolManager protocolManager;
	private JavaPlugin plugin;

	public InventoryHandler(ProtocolManager protocolManager, JavaPlugin plugin) {
		this.protocolManager = protocolManager;
		this.plugin = plugin;

		registerPacketListeners();
	}

	// -- Listening to packets --
	
	private void registerPacketListeners() {
		final PacketListener openWindowPacketListener = new PacketAdapter(plugin, ListenerPriority.HIGHEST,
				PacketType.Play.Server.OPEN_WINDOW) {
			@Override
			public void onPacketSending(PacketEvent event) {
				final String playerName = event.getPlayer().getName();

				final int windowId = event.getPacket().getIntegers().read(0);
				final Object containerType = event.getPacket().getStructures().readSafely(0);

				OpenContainerData data = new OpenContainerData(windowId, containerType);
				containerData.put(playerName, data);
			}
		};

		final PacketListener closeWindowPacketListener = new PacketAdapter(plugin, ListenerPriority.HIGHEST,
				PacketType.Play.Client.CLOSE_WINDOW) {
			@Override
			public void onPacketReceiving(PacketEvent event) {
				final String playerName = event.getPlayer().getName();
				containerData.remove(playerName);
			}
		};

		protocolManager.addPacketListener(openWindowPacketListener);
		protocolManager.addPacketListener(closeWindowPacketListener);
	}
	
	// -- API methods --

	public void setPlayerInventoryTitle(Player player, TextComponent newTitle) {
		final InventoryType type = player.getOpenInventory().getType();
		if (type == InventoryType.CRAFTING || type == InventoryType.CREATIVE)
			return;

		final int windowId = getWindowId(player);
		if (windowId == 0)
			return;

		final Object windowType = getWindowType(player);
		sendOpenScreenPacket(player, windowId, windowType, newTitle);
	}

	
	// -- Utility methods --
	
	private static void sendOpenScreenPacket(Player player, int windowId, Object windowType, TextComponent title) {
		final String json = GsonComponentSerializer.gson().serialize(title);
		final WrappedChatComponent wrappedComponent = WrappedChatComponent.fromJson(json);

		PacketContainer openScreen = new PacketContainer(PacketType.Play.Server.OPEN_WINDOW);
		openScreen.getIntegers().write(0, windowId);
		openScreen.getStructures().write(0, (InternalStructure) windowType);
		openScreen.getChatComponents().write(0, wrappedComponent);

		try {
			Mauris.getInstance().getProtocolManager().sendServerPacket(player, openScreen);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
	private OpenContainerData getOpenContainerData(Player player) {
		final String name = player.getName();
		return containerData.getOrDefault(name, null);
	}

	private Object getWindowType(Player player) {
		final OpenContainerData data = getOpenContainerData(player);

		if (data == null) {
			return 0;
		}

		return data.getContainerType();
	}

	private int getWindowId(Player player) {
		final OpenContainerData data = getOpenContainerData(player);

		if (data == null) {
			return 0;
		}

		return data.getWindowId();
	}
//
	
}
