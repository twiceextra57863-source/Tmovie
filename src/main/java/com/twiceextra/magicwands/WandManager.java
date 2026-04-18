package com.twiceextra.magicwands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class WandManager {

    private final MagicWands plugin;
    public final NamespacedKey WAND_KEY;

    public WandManager(MagicWands plugin) {
        this.plugin = plugin;
        this.WAND_KEY = new NamespacedKey(plugin, "magic_wand_type");
    }

    public void registerRecipes() {
        registerPhoenixFlare();
        registerVoidGrasp();
        registerMjolnirFragment();
    }

    private void registerPhoenixFlare() {
        ItemStack item = createWand(Material.BLAZE_ROD, "Phoenix Flare", "phoenix", NamedTextColor.GOLD);
        ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(plugin, "phoenix_flare"), item);
        recipe.shape(" F ", " R ", " R ");
        recipe.setIngredient('F', Material.FIRE_CHARGE);
        recipe.setIngredient('R', Material.BLAZE_ROD);
        plugin.getServer().addRecipe(recipe);
    }

    private void registerVoidGrasp() {
        ItemStack item = createWand(Material.ECHO_SHARD, "Void Grasp", "void", NamedTextColor.DARK_PURPLE);
        ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(plugin, "void_grasp"), item);
        recipe.shape(" E ", " S ", " S ");
        recipe.setIngredient('E', Material.ENDER_EYE);
        recipe.setIngredient('S', Material.STICK);
        plugin.getServer().addRecipe(recipe);
    }

    private void registerMjolnirFragment() {
        ItemStack item = createWand(Material.IRON_INGOT, "Mjolnir Fragment", "mjolnir", NamedTextColor.AQUA);
        ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(plugin, "mjolnir_fragment"), item);
        recipe.shape(" N ", " I ", " I ");
        recipe.setIngredient('N', Material.NETHERITE_INGOT);
        recipe.setIngredient('I', Material.IRON_INGOT);
        plugin.getServer().addRecipe(recipe);
    }

    private ItemStack createWand(Material material, String name, String type, NamedTextColor color) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.displayName(Component.text(name).color(color).decorate(TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false));
            List<Component> lore = new ArrayList<>();
            lore.add(Component.text("A mystical artifact from the movies.").color(NamedTextColor.GRAY));
            meta.lore(lore);
            meta.getPersistentDataContainer().set(WAND_KEY, PersistentDataType.STRING, type);
            item.setItemMeta(meta);
        }
        return item;
    }
}