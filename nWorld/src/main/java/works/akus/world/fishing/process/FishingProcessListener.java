package works.akus.world.fishing.process;

import org.bukkit.Material;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerFishEvent.State;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.InternalStructure;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;

import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import works.akus.mauris.objects.sounds.CustomSound;
import works.akus.mauris.objects.sounds.SoundPlayData;
import works.akus.mauris.objects.sounds.SoundTask;
import works.akus.mauris.registry.SoundRegistry;
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
			    	if(!World.get().getFishingManager().getFishingProcessesHandler().isFishingProcessActive(player))
			    		return;
			    	if(!World.get().getFishingManager().getFishingProcessesHandler().getFishingProcess(player).isFishHooked())
						return;
			    	PacketContainer packet = event.getPacket();
			    	InternalStructure a = packet.getStructures().read(0);
			    	if(a.toString().contains("ParticleType@6ad5b8f7"))
			    		event.setCancelled(true);
			    }
			});
	}

	private static final CustomSound throwSoundStart = SoundRegistry.getSound("fishing.throw.start");
	private static final CustomSound throwSound = SoundRegistry.getSound("fishing.throw");
	private static final CustomSound throwSoundEnd = SoundRegistry.getSound("fishing.throw.end");

	private static final SoundTask throwSoundTaskTemplate =
			new SoundTask(null, throwSound, throwSoundStart, throwSoundEnd, 15, 15);

	@EventHandler
	public void onCastAFishingRod(PlayerFishEvent event) {
		if (event.getState() != PlayerFishEvent.State.FISHING)
			return;

		World.get().getFishingManager().getFishingProcessesHandler().startFishingProcess(event.getPlayer(),
				event.getHook());
		World.get().getLogger().info("Player started fishing");

		startSoundThrowTask(event.getPlayer(), event.getHook());
	}

	private void startSoundThrowTask(Player player, FishHook hook){

		SoundTask task = throwSoundTaskTemplate.copy()
				.setPlayData(SoundPlayData.create(16, player));

		task.run();

		new BukkitRunnable() {
			@Override
			public void run() {

				if(hook.isDead() || hook.isOnGround()
				|| hook.getState() != FishHook.HookState.UNHOOKED){
					task.cancel();
					cancel();
				}

			}
		}.runTaskTimer(World.get(), 14, 15);

	}

	@EventHandler
	public void onRightClickWhileFishing(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if (event.getItem() == null)
			return;
		if (!((event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)
				&& event.getItem().getType() == Material.FISHING_ROD))
			return;
		if (!World.get().getFishingManager().getFishingProcessesHandler().isFishingProcessActive(player))
			return;

		event.setCancelled(true);

	}

	@EventHandler
	public void onFishHooked(PlayerFishEvent event) {
		if (event.getState() != State.BITE)
			return;
		Player player = event.getPlayer();
		
		if(World.get().getFishingManager().getFishingProcessesHandler().getFishingProcess(player).isFishHooked())
			return;

		World.get().getFishingManager().getFishingProcessesHandler().getFishingProcess(player).hookRandomBalancedGeneratedFish();
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
