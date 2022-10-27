package works.akus.mauris.registry;

import java.util.Collections;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Nullable;

import works.akus.mauris.Mauris;
import works.akus.mauris.objects.items.ItemStackBuilder;
import works.akus.mauris.objects.items.MaurisItem;
import works.akus.mauris.utils.ItemUtils;

public  class ItemRegistry {

	private static final String MAURIS_KEY = "maurisid";
	private static ConcurrentHashMap<String, MaurisItem> registeredItems = new ConcurrentHashMap<>();

	public static @Nullable MaurisItem getMaurisItem(String id) {
		return registeredItems.getOrDefault(id, null);
	}
	
	public static @Nullable MaurisItem getMaurisItem(ItemStack item) {
		final String id = getMaurisId(item);
		if (id == null) return null;

		return getMaurisItem(id);
	}
	
	public static @Nullable ItemStackBuilder getItemStackBuilder(String maurisItemId) {
		MaurisItem item = getMaurisItem(maurisItemId);
		if (item == null) return null;
		return item.getBuilder();
	}
	
	public static boolean isRegistered(String maurisItemId) {
		return (getMaurisItem(maurisItemId) != null);
	}

	public static void register(String id, MaurisItem item) {
		registeredItems.put(id, item);
	}
	
	public static void unregister(String id) {
		registeredItems.remove(id);
	}
	
	public static Set<Entry<String, MaurisItem>> getEntrySet() {
		return Collections.unmodifiableSet(registeredItems.entrySet());
	}
	
	public static Set<String> getIdSet() {
		return Collections.unmodifiableSet(registeredItems.keySet());
	}

	public static @Nullable String getMaurisId(ItemStack item) {
		if (!ItemUtils.hasItemMeta(item)) return null;

		final PersistentDataContainer pdc = item.getItemMeta().getPersistentDataContainer();
		final NamespacedKey key = new NamespacedKey(Mauris.getInstance(), MAURIS_KEY);
		
		return pdc.get(key, PersistentDataType.STRING);
	}
	
	public static @Nullable ItemStack getItemStack(String maurisItemId) {
		if (!isRegistered(maurisItemId)) return null;

		MaurisItem item = getMaurisItem(maurisItemId);

		ItemStackBuilder builder = item.getBuilder();
		builder.addStringKeysData(MAURIS_KEY, maurisItemId);

		return builder.createItemStack();
	}
	
}
