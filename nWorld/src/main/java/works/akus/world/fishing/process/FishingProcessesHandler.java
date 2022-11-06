package works.akus.world.fishing.process;

import java.util.HashMap;

import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

public class FishingProcessesHandler{
	
	private JavaPlugin plugin;
	
	private HashMap<Player, FishingProcess> activeFishingProcesses;
	
	private Runnable fishingSimulateTask;
	private int taskId;
	
	public FishingProcessesHandler(JavaPlugin plugin) {
		this.plugin = plugin;
		activeFishingProcesses = new HashMap<>();
		
		createFishingSimulateTask();
		runFishingSimulateTask();
	}
	
	public void startFishingProcess(Player player, FishHook hook) {
		FishingProcess fishingProcess = new FishingProcess(player, hook);
		activeFishingProcesses.put(player, fishingProcess);
	}
	
	public FishingProcess getFishingProcess(Player player) {
		return activeFishingProcesses.get(player);
	}
	
	public boolean isFishingProcessActive(Player player) {
		return activeFishingProcesses.containsKey(player);
	}
	
	public void stopFishingProcess(Player player) {
		activeFishingProcesses.remove(player);
	}
	
	public void runFishingSimulateTask() {
		BukkitScheduler scheduler = plugin.getServer().getScheduler();
		taskId = scheduler.scheduleSyncRepeatingTask(plugin, fishingSimulateTask, 0, 1);
	}
	
	public void cancelFishingSimulateTask() {
		BukkitScheduler scheduler = plugin.getServer().getScheduler();
		scheduler.cancelTask(taskId);
	}

	private void createFishingSimulateTask() {
		fishingSimulateTask = new Runnable() {
			@Override
			public void run() {
				for(FishingProcess fishingProcess : activeFishingProcesses.values()) {
					fishingProcess.fishingLogic();
				}
			}
		};
	}
}
