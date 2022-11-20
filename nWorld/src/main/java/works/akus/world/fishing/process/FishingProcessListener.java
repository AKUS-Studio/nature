package works.akus.world.fishing.process;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerFishEvent.State;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.InternalStructure;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;

import works.akus.mauris.registry.ItemRegistry;
import works.akus.world.World;

public class FishingProcessListener implements Listener {

	public FishingProcessListener() {
		registerPacketListener();
	}
	
	private void registerPacketListener() {
		ProtocolManager protocolManager = World.get().getProtocolManager();
		protocolManager.addPacketListener(new PacketAdapter(
			    World.get(),
			    ListenerPriority.NORMAL,
				PacketType.Play.Server.WORLD_PARTICLES
			) {
			    @Override
			    public void onPacketSending(PacketEvent event) {
			    	Player player = event.getPlayer();
			    	if(!World.get().getFishingManager().getFishingProcessesHandler().getFishingProcess(player).isFishHooked())
						return;
			    	PacketContainer packet = event.getPacket();
			    	InternalStructure a = packet.getStructures().read(0);
			    	if(a.toString().contains("ParticleType@6ad5b8f7"))
			    		event.setCancelled(true);
			    }
			});
	}
	@EventHandler
	public void onCastAFishingRod(PlayerFishEvent event) {
		if (event.getState() != PlayerFishEvent.State.FISHING)
			return;

		World.get().getFishingManager().getFishingProcessesHandler().startFishingProcess(event.getPlayer(),
				event.getHook());
		World.get().getLogger().info("Player started fishing");
	}

	@EventHandler
	public void onFixTheCoil(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if (event.getItem() == null)
			return;
		if (!((event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)
				&& event.getItem().getType() == Material.FISHING_ROD))
			return;
		if (!World.get().getFishingManager().getFishingProcessesHandler().isFishingProcessActive(player))
			return;

		event.setCancelled(true);
		FishingProcess fishingSession = World.get().getFishingManager().getFishingProcessesHandler()
				.getFishingProcess(player);

		fishingSession.setCoilFixed(!fishingSession.isCoilFixed());
		player.sendMessage("Coil fixed: " + Boolean.toString(fishingSession.isCoilFixed()));

	}

	@EventHandler
	public void onFishHooked(PlayerFishEvent event) {
		if (event.getState() != State.BITE)
			return;
		Player player = event.getPlayer();
		
		if(World.get().getFishingManager().getFishingProcessesHandler().getFishingProcess(player).isFishHooked())
			return;

		ItemStack fish = ItemRegistry.getItemStack("cod");
		World.get().getFishingManager().getFishingProcessesHandler().getFishingProcess(player).hookFish(fish, 0.2, 8);
	}

	@EventHandler
	public void onShiftingWhileFishing(PlayerToggleSneakEvent event) {
		Player player = event.getPlayer();
		if (!World.get().getFishingManager().getFishingProcessesHandler().isFishingProcessActive(player))
			return;

		FishingProcess fishingProcess = World.get().getFishingManager().getFishingProcessesHandler()
				.getFishingProcess(player);

		fishingProcess.setReelingInTheLine(!fishingProcess.isReelingInTheLine());

	}
}
