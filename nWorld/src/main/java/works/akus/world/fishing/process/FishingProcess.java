package works.akus.world.fishing.process;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.FishHook.HookState;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import com.google.common.collect.Lists;

import net.kyori.adventure.text.Component;
import works.akus.mauris.objects.fonts.GlyphBuilder;
import works.akus.mauris.objects.sounds.CustomSound;
import works.akus.mauris.objects.sounds.SoundPlayData;
import works.akus.mauris.objects.sounds.SoundTask;
import works.akus.mauris.registry.GlyphRegistry;
import works.akus.mauris.registry.ItemRegistry;
import works.akus.mauris.registry.SoundRegistry;
import works.akus.world.World;
import works.akus.world.fishing.types.CustomFishType;

public class FishingProcess {

	private RodState state;

	private Player player;
	private FishHook hook;

	private boolean isReelingInTheLine;
	private boolean isLineReachedLimit;

	private double fishingLineLength;
	private double fishingLineTension;
	private final double MAX_FISHING_LINE_TENSION = 10; // temporary value
	private final double MAX_FISHING_LINE_LENGTH = 20; // temporary value

	private CustomFishType customFishType;
	private double fishStrength;
	private double fishMass;
	private boolean isFishHooked;
	private Vector fishVelocity;

	private final int FREQUENCY_OF_CHANGIN_THE_DIRECTION_OF_FISH = 3; // in ticks

	//Sounds
	private static final CustomSound reelingSound = SoundRegistry.getSound("fishing.reel");
	private static final CustomSound reelingStartSound = SoundRegistry.getSound("fishing.reel.start");

	private static final CustomSound unwindingSound = SoundRegistry.getSound("fishing.unwind");

	private static final SoundTask reelingSTTemplate
			= new SoundTask(null, reelingSound, reelingStartSound, 15,15);

	private static final SoundTask unwindingSTTemplate
			= new SoundTask(null, unwindingSound, 15 ,15);

	private final SoundTask currentReelingTask;
	private final SoundTask currentUnwindingTask;

	private SoundTask currentTask = SoundTask.empty();
	//

	private int ticksCounter;

	public FishingProcess(Player player, FishHook hook) {
		this.player = player;
		this.hook = hook;

		this.isReelingInTheLine = false;
		this.isLineReachedLimit = false;
		this.isFishHooked = false;

		this.fishVelocity = new Vector(0, 0, 0);

		this.state = RodState.WAITING;
		this.currentReelingTask = reelingSTTemplate.copy().setPlayData(SoundPlayData.create(16, player));
		this.currentUnwindingTask = unwindingSTTemplate.copy().setPlayData(SoundPlayData.create(16, player));
	}

	protected void fishingLogic() {
		if (hook.isDead()) {
			World.get().getFishingManager().getFishingProcessesHandler().stopFishingProcess(player);
			currentTask.cancel();
			state = RodState.END;
			return;
		}

		ticksCounter += 1;
		if (isFishHooked) {
			fishingLineTension = getNewFishingLineTension();
		}
		if (fishingLineTension > MAX_FISHING_LINE_TENSION) {
			hook.remove();
			player.sendMessage("Рыба сорвалась");
			return;
		}

		Location hookLocation = hook.getLocation();
		Location playerLocation = player.getLocation();

		Vector vectorBetweenPlayerAndHook = new Vector(playerLocation.getX() - hookLocation.getX(),
				playerLocation.getY() - hookLocation.getY(), playerLocation.getZ() - hookLocation.getZ());

		Vector vectorBetweenPlayerAndHookInXZPlane = vectorBetweenPlayerAndHook.clone().setY(0);

		fishingLineLength = player.getLocation().distance(hook.getLocation());

		setLineReachedLimit(fishingLineLength >= MAX_FISHING_LINE_LENGTH);

		if (isLineReachedLimit) {
			Vector vec = vectorBetweenPlayerAndHook.clone().normalize()
					.multiply(playerLocation.distance(hookLocation) - MAX_FISHING_LINE_LENGTH);
			hook.setVelocity(hook.getVelocity().add(vec));
		}

		if (vectorBetweenPlayerAndHookInXZPlane.length() < 1 && isReelingInTheLine) {
			hook.remove();
			World.get().getFishingManager().getFishingProcessesHandler().stopFishingProcess(player);
			currentTask.cancel();
			state = RodState.END;
			if (isFishHooked) {
				ItemStack fishItem = ItemRegistry.getItemStack(customFishType.getId());
				ItemMeta fishItemMeta = fishItem.getItemMeta();

				double fishMassRounded = fishMass;
				long factor = (long) Math.pow(10, 2);
				fishMassRounded = fishMassRounded * factor;
				long tmp = Math.round(fishMassRounded);
				fishMassRounded = (double) tmp / factor;

				String lore = "Масса: " + Double.toString(fishMassRounded);
				fishItemMeta.lore(Lists.newArrayList(Component.text(lore)));
				fishItem.setItemMeta(fishItemMeta);

				player.getInventory().addItem(fishItem);
			};
		}

		Vector hookVelocity = getNewHookVelocity(vectorBetweenPlayerAndHookInXZPlane);
		hook.setVelocity(hookVelocity);

		//Sounds
		switch (state){

			case UNWINDING:
				if(currentTask == currentUnwindingTask) break;
				currentTask.cancel();
				currentTask = currentUnwindingTask;
				currentTask.run();
				break;
			case REELING:
				if(currentTask == currentReelingTask) break;
				currentTask.cancel();
				currentTask = currentReelingTask;
				currentTask.run();
				break;
			default:
				currentTask.cancel();
				currentTask = SoundTask.empty();
				break;
		}

	}

	private void printFishingLineTension(double tension) {
		int tensionLineCoordinate = (int) Math.round((37 / MAX_FISHING_LINE_TENSION) * tension);

		Component component = new GlyphBuilder().offset(tensionLineCoordinate).offset(-40) // idk how but it fixed
																							// offset in action bar
				.append(GlyphRegistry.getGlyph("tension_bar")).offset(tensionLineCoordinate).offset(-40)
				.append(GlyphRegistry.getGlyph("tension_line")).buildAsComponent();

		player.sendActionBar(component);
	}

	private double getNewFishingLineTension() {
		double tension = fishingLineTension;

		if (Math.random() > 0.20 && fishStrength < (fishMass / 5))
			fishStrength += Math.random() * (fishMass / 10);
		else if (fishStrength > fishMass / 10)
			fishStrength -= Math.random() * (fishMass / 10);

		if (isLineReachedLimit || isReelingInTheLine)
			tension += fishStrength / 20;

		if (isReelingInTheLine)
			tension += fishMass / 20;

		if (!isLineReachedLimit && !isReelingInTheLine && (tension - MAX_FISHING_LINE_TENSION / 20) > 0)
			tension -= MAX_FISHING_LINE_TENSION / 20;

		printFishingLineTension(tension);
		return tension;
	}

	private Vector getNewHookVelocity(Vector vectorBetweenPlayerAndHookInXZPlane) {
		Vector hookVelocity = new Vector(0, 0, 0);

		if (isFishHooked && hook.isInWater()) {
			if ((ticksCounter % FREQUENCY_OF_CHANGIN_THE_DIRECTION_OF_FISH == 0 || fishVelocity.length() == 0)
					&& hook.getState() == HookState.BOBBING)
				fishVelocity = getNewFishVelocity(vectorBetweenPlayerAndHookInXZPlane);
			hookVelocity = fishVelocity.clone();
		}

		if (isReelingInTheLine) {
			Vector velocityFromCoil = vectorBetweenPlayerAndHookInXZPlane.clone().normalize().multiply(0.3);
			if (vectorBetweenPlayerAndHookInXZPlane.length() < 2)
				velocityFromCoil.setY(0.25);
			hookVelocity.add(velocityFromCoil);

			state = RodState.REELING;
		}else if(isFishHooked) state = RodState.UNWINDING;

		if (isLineReachedLimit && hookVelocity.dot(vectorBetweenPlayerAndHookInXZPlane) < 0) {
			Vector perpendicularVector = getPerpendicularVectorInXZPlane(vectorBetweenPlayerAndHookInXZPlane);
			hookVelocity = vectorizedProjectionOfAVector1OntoAVector2(hookVelocity, perpendicularVector).clone();
		}

		if (!isReelingInTheLine && !isFishHooked) {
			hookVelocity = hook.getVelocity();
			state = RodState.WAITING;
		}

		return hookVelocity;

	}

	public Vector getNewFishVelocity(Vector vectorBetweenPlayerAndHookInXZPlane) {
		double randomNum = Math.random();
		double subMultiplier = randomNum > 0.5 ? 2 : 1;
		double alpha = ((1 - randomNum * randomNum) * subMultiplier) // alpha - random generated angle between fish
																		// velocity and fishing line
				* (Math.PI / 2) - (Math.PI / 2);

		double cosAlpha = Math.cos(alpha);
		double sinAlpha = Math.sin(alpha);

		Vector normalizedVectorBetweenPlayerAndHookInXZPlane = vectorBetweenPlayerAndHookInXZPlane.clone().normalize()
				.multiply(-1);
		double cosBeta = normalizedVectorBetweenPlayerAndHookInXZPlane.getX(); // beta - angle between ^ this vector
																				// and oX axis
		double sinBeta = normalizedVectorBetweenPlayerAndHookInXZPlane.getZ();

		double cosBetaPlusAlpha = cosBeta * cosAlpha - sinBeta * sinAlpha;
		double sinBetaPlusAlpha = sinAlpha * cosBeta + cosAlpha * sinBeta;
		Vector fishVelocity = new Vector(cosBetaPlusAlpha, 0, sinBetaPlusAlpha);

		fishVelocity.multiply(0.2);

		return fishVelocity;

	}

	public void setLineReachedLimit(boolean value) {
		isLineReachedLimit = value;
	}

	public void setReelingInTheLine(boolean value) {
		isReelingInTheLine = value;

	}

	public void hookRandomBalancedGeneratedFish() {
		CustomFishType fishType = CustomFishType.getRandomFishType();
		Random random = new Random();
		double mass = random.nextDouble(MAX_FISHING_LINE_TENSION - 4, MAX_FISHING_LINE_TENSION);
		double strength = random.nextDouble(mass / 60, mass / 35);
		hookFish(fishType, strength, mass);
	}

	public void hookFish(CustomFishType fishType, double fishStrength, double fishMass) {
		this.customFishType = fishType;
		this.fishStrength = fishStrength;
		this.fishMass = fishMass;

		isFishHooked = true;
	}

	public FishHook getHook() {
		return hook;
	}

	public RodState getState() {
		return state;
	}

	public boolean isReelingInTheLine() {
		return isReelingInTheLine;
	}

	public boolean isLineReachedLimit() {
		return isLineReachedLimit;
	}

	public boolean isFishHooked() {
		return isFishHooked;
	}

	private Vector vectorizedProjectionOfAVector1OntoAVector2(Vector vector1, Vector vector2) {
		double dotProduct = vector1.dot(vector2);
		double projectionLength = dotProduct / (vector2.length());
		Vector result = vector2.normalize().multiply(projectionLength);
		return result;
	}

	private Vector getPerpendicularVectorInXZPlane(Vector vector) {
		Vector result = new Vector(-vector.getZ(), 0, vector.getX());
		return result;
	}

	public double getFishingLineLength() {
		return fishingLineLength;
	}

	enum RodState{
		REELING,
		UNWINDING,
		WAITING,
		END,
	}

}
