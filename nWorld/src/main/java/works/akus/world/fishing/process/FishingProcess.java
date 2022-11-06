package works.akus.world.fishing.process;

import org.bukkit.Location;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.FishHook.HookState;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import net.kyori.adventure.text.Component;
import works.akus.mauris.objects.fonts.GlyphBuilder;
import works.akus.mauris.registry.GlyphRegistry;
import works.akus.world.World;

public class FishingProcess {

	private Player player;
	private FishHook hook;
	private ItemStack fishingRod;

	private boolean isReelingInTheLine;
	private boolean isCoilFixed;

	private double fishingLineLength;
	private double fishingLineTension;
	private double maxFishingLineTension = 10; // temporary value

	private ItemStack fish;
	private double fishStrength;
	private double fishMass;
	private boolean isFishHooked;
	private Vector fishVelocity;

	private final int FREQUENCY_OF_CHANGIN_THE_DIRECTION_OF_FISH = 5; // in ticks

	private int ticksCounter;

	public FishingProcess(Player player, FishHook hook) {
		this.player = player;
		this.hook = hook;

		this.isReelingInTheLine = false;
		this.isCoilFixed = false;
		this.isFishHooked = false;

		this.fishVelocity = new Vector(0, 0, 0);
	}

	protected void fishingLogic() {
		if (hook.isDead())
			World.get().getFishingManager().getFishingProcessesHandler().stopFishingProcess(player);

		ticksCounter += 1;
		if (isFishHooked) {
			if (ticksCounter % FREQUENCY_OF_CHANGIN_THE_DIRECTION_OF_FISH == 0)
				fishingLineTension = getNewFishingLineTension();
			fishingLineTension = 0;
		}
		if (fishingLineTension > maxFishingLineTension) {
			hook.remove();
			player.sendMessage("Рыба сорвалась");
		}

		Location hookLocation = hook.getLocation();
		Location playerLocation = player.getLocation();

		if (!isCoilFixed || isReelingInTheLine) {
			fishingLineLength = new Vector(playerLocation.getX() - hookLocation.getX(),
					playerLocation.getY() - hookLocation.getY(), playerLocation.getZ() - hookLocation.getZ()).length();
		}

		Vector vectorBetweenPlayerAndHookInXZPlane = new Vector(playerLocation.getX() - hookLocation.getX(), 0,
				playerLocation.getZ() - hookLocation.getZ());

		if (vectorBetweenPlayerAndHookInXZPlane.length() < 1 && isReelingInTheLine) {
			hook.remove();
			World.get().getFishingManager().getFishingProcessesHandler().stopFishingProcess(player);
			if (isFishHooked)
				player.getInventory().addItem(fish);
		}

		Vector hookVelocity = getNewHookVelocity(vectorBetweenPlayerAndHookInXZPlane);
		hook.setVelocity(hookVelocity);
	}

	private void printFishingLineTension(double tension) {
		int tensionLineCoordinate = (int) Math.round((37 / maxFishingLineTension) * tension);

		Component component = new GlyphBuilder()
				.offset(tensionLineCoordinate).offset(-40) // idk how but it fixed offset in action bar
				.append(GlyphRegistry.getGlyph("tension_bar"))
				.offset(tensionLineCoordinate).offset(-40)
				.append(GlyphRegistry.getGlyph("tension_line")).buildAsComponent();
		
		player.sendActionBar(component);
	}

	private double getNewFishingLineTension() {
		double tension = 0;

		if (Math.random() > 0.20 && fishStrength < (fishMass / 5))
			fishStrength += Math.random() * (fishMass / 10);
		else if (fishStrength > fishMass / 10)
			fishStrength -= Math.random() * (fishMass / 10);

		if (isCoilFixed || isReelingInTheLine)
			tension += fishStrength;

		if (isReelingInTheLine) {
			tension += fishMass;
		}

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

		}

		if (isCoilFixed && hookVelocity.dot(vectorBetweenPlayerAndHookInXZPlane) < 0) {
			Vector perpendicularVector = getPerpendicularVectorInXZPlane(vectorBetweenPlayerAndHookInXZPlane);
			hookVelocity = vectorizedProjectionOfAVector1OntoAVector2(hookVelocity, perpendicularVector).clone();

		}

		if (!isReelingInTheLine && !isFishHooked) {
			hookVelocity = hook.getVelocity();
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

	public void setCoilFixed(boolean value) {
		isCoilFixed = value;
	}

	public void setReelingInTheLine(boolean value) {
		isReelingInTheLine = value;
	}

	public void hookFish(ItemStack fish, double fishStrength, double fishMass) {
		this.fish = fish;
		this.fishStrength = fishStrength;
		this.fishMass = fishMass;

		isFishHooked = true;
	}

	public FishHook getHook() {
		return hook;
	}

	public boolean isReelingInTheLine() {
		return isReelingInTheLine;
	}

	public boolean isCoilFixed() {
		return isCoilFixed;
	}

	private Vector vectorizedProjectionOfAVector1OntoAVector2(Vector vector1, Vector vector2) {
		double dotProduct = vector1.dot(vector2);
		double projectionLength = dotProduct / (vector2.length());
		Vector result = vector2.normalize().multiply(projectionLength);
		double a = result.dot(result);
		return result;
	}

	private Vector getPerpendicularVectorInXZPlane(Vector vector) {
		Vector result = new Vector(-vector.getZ(), 0, vector.getX());
		return result;
	}

}
