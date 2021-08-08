/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.rusticregen.common.items;

import net.angle.rusticregen.common.blocks.ModBlocks;
import net.angle.rusticregen.core.RusticRegenerated;
import static net.angle.rusticregen.core.RusticRegenerated.MODID;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

/**
 *
 * @author angle
 */
public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    
    public static final RegistryObject<Item> APPLE_LEAVES_ITEM = ITEMS.register("apple_leaves", () -> {
        return RusticRegenerated.registerLeafItem(new BlockItem(ModBlocks.APPLE_LEAVES_BLOCK.get(), new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS)));
    });
    
    public static final RegistryObject<Item> APPLE_SAPLING_ITEM = ITEMS.register("apple_sapling", () -> {
        return new BlockItem(ModBlocks.APPLE_SAPLING_BLOCK.get(), new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS));
    });
    
    public static final RegistryObject<Item> APPLE_SEEDS_ITEM = ITEMS.register("apple_seeds", () -> {
        return new ItemNameBlockItem(ModBlocks.APPLE_SEEDS_BLOCK.get(), new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS));
    });
    
    public static final RegistryObject<Item> CROSSED_LOG_ITEM = ITEMS.register("crossed_logs", () -> {
        return new BlockItem(ModBlocks.CROSSED_LOGS_BLOCK.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS));
    });
    
    public static final RegistryObject<Item> STAKE_ITEM = ITEMS.register("stake", () -> {
        return new BlockItem(ModBlocks.STAKE_BLOCK.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS));
    });
}
