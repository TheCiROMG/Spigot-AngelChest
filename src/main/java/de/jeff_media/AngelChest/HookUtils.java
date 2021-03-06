package de.jeff_media.AngelChest;

import de.jeff_media.AngelChest.hooks.InventoryPagesHook;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class HookUtils implements Listener {

    final Main main;
    final InventoryPagesHook inventoryPagesHook;
    //ArrayList<Entity> hologramsToBeSpawned = new ArrayList<Entity>();
    //boolean hologramToBeSpawned = false;

    HookUtils(Main main) {
        this.main=main;
        this.inventoryPagesHook = new InventoryPagesHook(main);
    }

    boolean isSlimefunSoulbound(ItemStack item) {
        if(item==null) return false;
        if(!main.getConfig().getBoolean("use-slimefun")) return false;

        try {
            Class.forName("io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils");
            return SlimefunUtils.isSoulbound(item);
        } catch (ClassNotFoundException | NoClassDefFoundError e) {
            main.getConfig().set("use-slimefun",false);
            return false;
        }
    }

    boolean isNativeSoulbound(ItemStack item) {
        if(item==null) return false;
        if(!item.hasItemMeta()) return false;
        ItemMeta meta = item.getItemMeta();
        if(!meta.hasEnchants()) return false;
        for(Enchantment enchant : meta.getEnchants().keySet()) {
            if(enchant.getKey().getKey().equals("soulbound")) {
                return true;
            }
        }
        return false;
    }

    boolean isGenericSoulbound(ItemStack item) {
        if(item==null) return false;
        if(!main.getConfig().getBoolean("check-generic-soulbound")) return false;

        if(!item.hasItemMeta()) return false;
        ItemMeta meta = item.getItemMeta();

        if(meta.getLore()==null) {
            return false;
        }

        for(String line : meta.getLore()) {
            if(line.toLowerCase().contains("soulbound")) {
                main.debug(item.toString() + "is a GENERIC SOULBOUND ITEM. Lore: " + line);
                return true;
            }
        }
        return false;
    }

    boolean isDisabledMaterial(ItemStack item) {
        if(item==null) return false;
        String type = item.getType().name();
        for(String mat : main.disabledMaterials) {
            if(mat.equalsIgnoreCase(type)) return true;
        }
        return false;
    }

    boolean removeOnDeath(ItemStack item) {
        if(item==null) return false;
        if(isDisabledMaterial(item)) return true;
        if(inventoryPagesHook.isButton(item)) return true;
        if(isGenericSoulbound(item)) return true;
        return false;
    }

    boolean keepOnDeath(ItemStack item) {
        if(item==null) return false;
        if( isSlimefunSoulbound(item)) return true;
        if(isNativeSoulbound(item)) return true;
        return false;
    }

}