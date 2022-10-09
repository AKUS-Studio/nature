package works.akus.mauris.things.items;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import works.akus.mauris.Mauris;
import works.akus.mauris.utils.ColorUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class ItemStackBuilder {

    Material material = Material.PAPER;

    //Meta Things
    Component displayName;
    List<Component> lore = new ArrayList<>();

    HashMap<Enchantment, Integer> enchantments = new HashMap<>();
    List<ItemFlag> itemFlags = new ArrayList<>();
    Multimap<Attribute, AttributeModifier> attributes = ArrayListMultimap.create();

    int customModelData = 0;

    boolean unbreakable = false;

    //PersistentDataContainer Data
    HashMap<String, String> stringKeysData = new HashMap<>();

    /*
    Basic Adders, Getters and Setters
     */
    public Material getMaterial() {
        return material;
    }

    public ItemStackBuilder setMaterial(Material material) {
        this.material = material;
        return this;
    }

    public Component getDisplayName() {
        return displayName;
    }

    public ItemStackBuilder setDisplayName(Component displayName) {
        this.displayName = displayName;
        return this;
    }

    public ItemStackBuilder setDisplayName(String displayName) {
        this.displayName = Component.text(ColorUtils.format(displayName));
        return this;
    }

    public List<Component> getLore() {
        return lore;
    }

    public ItemStackBuilder addLore(Component lore){
        this.lore.add(lore);
        return this;
    }

    public ItemStackBuilder addLore(String lore){
        this.lore.add(Component.text(ColorUtils.format(lore)));
        return this;
    }

    public ItemStackBuilder setLore(List<Component> lore) {
        this.lore = lore;
        return this;
    }

    public HashMap<Enchantment, Integer> getEnchantments() {
        return enchantments;
    }

    public ItemStackBuilder addEnchant(Enchantment enchantment, Integer integer){
        enchantments.put(enchantment, integer);
        return this;
    }

    public ItemStackBuilder setEnchantments(HashMap<Enchantment, Integer> enchantments) {
        this.enchantments = enchantments;
        return this;
    }

    public List<ItemFlag> getItemFlags() {
        return itemFlags;
    }

    public ItemStackBuilder addItemFlags(ItemFlag... itemFlags) {
        Collections.addAll(this.itemFlags, itemFlags);

        return this;
    }

    public ItemStackBuilder setItemFlags(List<ItemFlag> itemFlags) {
        this.itemFlags = itemFlags;
        return this;
    }

    public Multimap<Attribute, AttributeModifier> getAttributes() {
        return attributes;
    }

    public ItemStackBuilder setAttributes(Multimap<Attribute, AttributeModifier> attributes) {
        this.attributes = attributes;
        return this;
    }

    public int getCustomModelData() {
        return customModelData;
    }

    public ItemStackBuilder setCustomModelData(int customModelData) {
        this.customModelData = customModelData;
        return this;
    }

    public boolean isUnbreakable() {
        return unbreakable;
    }

    public ItemStackBuilder setUnbreakable(boolean unbreakable) {
        this.unbreakable = unbreakable;
        return this;
    }

    public ItemStackBuilder addStringKeysData(String key, String value){
        stringKeysData.put(key, value);
        return this;
    }

    public HashMap<String, String> getStringKeysData() {
        return stringKeysData;
    }

    public ItemStackBuilder setStringKeysData(HashMap<String, String> stringKeysData) {
        this.stringKeysData = stringKeysData;
        return this;
    }

    /*
    Creating ItemStack from Variables
     */
    public ItemStack createItemStack(){
        ItemStack item = new ItemStack(material);

        ItemMeta meta = item.getItemMeta();
        meta.displayName(displayName);

        meta.lore(lore);
        meta.addItemFlags(itemFlags.toArray(ItemFlag[]::new));
        meta.setAttributeModifiers(attributes);
        enchantments.forEach((e, i) -> meta.addEnchant(e, i, true));

        meta.setUnbreakable(unbreakable);
        meta.setCustomModelData(customModelData);

        PersistentDataContainer data = meta.getPersistentDataContainer();

        stringKeysData.forEach((key, value) -> data.set(new NamespacedKey(Mauris.getInstance(), key), PersistentDataType.STRING, value));

        item.setItemMeta(meta);

        return item;
    }



}
