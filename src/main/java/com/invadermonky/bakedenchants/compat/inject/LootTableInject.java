package com.invadermonky.bakedenchants.compat.inject;

import com.invadermonky.bakedenchants.config.ConfigTags;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.storage.loot.*;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraft.world.storage.loot.functions.SetNBT;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.lang.reflect.Field;
import java.util.*;

/**
 * Experimental loot table injection.
 * <p>
 * It doesn't work and would likely need a better way to handle the replacements, but
 * the code is here if anyone else wants to have a crack at it.
 */
@SuppressWarnings("unchecked")
public class LootTableInject {
    private static Field poolsField;
    private static Field lootEntriesField;

    @SubscribeEvent
    public void modifyLootTables(LootTableLoadEvent event) {
        try {
            if (poolsField != null && lootEntriesField != null) {
                //parseTable is separated in order to support recursion for LootEntryTable
                parseTable(event.getLootTableManager(), event.getTable());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parseTable(LootTableManager manager, LootTable table) throws IllegalAccessException, ClassCastException {
        Map<String, LootPool> replacementPools = new HashMap<>();
        List<LootPool> pools = (List<LootPool>) poolsField.get(table);
        //Iterating through the loot pools
        for (LootPool pool : pools) {
            Map<String, LootEntry> replacementEntries = new HashMap<>();
            List<LootEntry> lootEntries = (List<LootEntry>) lootEntriesField.get(pool);

            //Iterating through all entries in the loot pools
            for (LootEntry entry : lootEntries) {
                if (entry instanceof LootEntryItem) {
                    LootEntryItem entryItem = (LootEntryItem) entry;
                    if (ConfigTags.hasBakedEnchants(entryItem.item)) {
                        //Creating a new LootEntry with the enchantments applied as the first loot function
                        List<LootFunction> functions = new ArrayList<>();
                        NBTTagCompound enchTag = ConfigTags.getEnchantmentTag(entryItem.item);
                        functions.add(new SetNBT(new LootCondition[0], enchTag));
                        functions.addAll(Arrays.asList(entryItem.functions));

                        LootEntryItem newEntry = new LootEntryItem(
                                entryItem.item,
                                entryItem.weight,
                                entryItem.quality,
                                functions.toArray(new LootFunction[0]),
                                entryItem.conditions,
                                entryItem.getEntryName()
                        );
                        replacementEntries.put(entry.getEntryName(), newEntry);
                    }
                } else if (entry instanceof LootEntryTable) {
                    parseTable(manager, manager.getLootTableFromLocation(((LootEntryTable) entry).table));
                }
            }

            if (!replacementEntries.isEmpty()) {
                replacementEntries.forEach((name, newEntry) -> {
                    pool.removeEntry(name);
                    pool.addEntry(newEntry);
                });
                replacementPools.put(pool.getName(), pool);
            }
        }

        replacementPools.forEach((name, newPool) -> {
            table.removePool(name);
            table.addPool(newPool);
        });
    }

    static {
        try {
            poolsField = ObfuscationReflectionHelper.findField(LootTable.class, "pools");
            lootEntriesField = ObfuscationReflectionHelper.findField(LootPool.class, "lootEntries");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
