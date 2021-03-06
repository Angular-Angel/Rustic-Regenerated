/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.rusticregen.common.items;

import net.angle.rusticregen.client.ClientEventRegistry;
import net.angle.rusticregen.common.blocks.ModBlocks;
import net.angle.rusticregen.core.FuelHandler;
import net.angle.rusticregen.core.RusticRegenerated;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

/**
 *
 * @author angle
 */
public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, RusticRegenerated.MODID);
    
    public static final RegistryObject<Item> APPLE_LEAVES = ITEMS.register("apple_leaves", () -> {
        return ClientEventRegistry.registerLeafItem(new BlockItem(ModBlocks.APPLE_LEAVES.get(), new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS)));
    });
    
    public static final RegistryObject<Item> APPLE_SAPLING = ITEMS.register("apple_sapling", () -> {
        return new BlockItem(ModBlocks.APPLE_SAPLING.get(), new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS));
    });
    
    public static final RegistryObject<Item> APPLE_SEEDS = ITEMS.register("apple_seeds", () -> {
        return new ItemNameBlockItem(ModBlocks.APPLE_SEEDS.get(), new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS));
    });
    
    public static final RegistryObject<Item> APPLE_CORE = ITEMS.register("apple_core", () -> {
        return new Item((new Item.Properties()).tab(CreativeModeTab.TAB_MISC));
    });
    
    public static final RegistryObject<Item> CROSSED_LOGS = ITEMS.register("crossed_logs", () -> {
        return new BlockItem(ModBlocks.CROSSED_LOGS.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS));
    });
    
    public static final RegistryObject<Item> VERTICAL_CROSSED_LOGS = ITEMS.register("vertical_crossed_logs", () -> {
        return new BlockItem(ModBlocks.VERTICAL_CROSSED_LOGS.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS));
    });
    
    public static final RegistryObject<Item> STAKE = ITEMS.register("stake", () -> {
        return new BlockItem(ModBlocks.STAKE.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS));
    });
    
    public static void setup() {
        FuelHandler.addFuel(APPLE_SAPLING.get(), 100);
        FuelHandler.addFuel(CROSSED_LOGS.get(), 400);
        FuelHandler.addFuel(VERTICAL_CROSSED_LOGS.get(), 400);
        FuelHandler.addFuel(STAKE.get(), 400);
        ComposterBlock composter = (ComposterBlock) Blocks.COMPOSTER;
        composter.COMPOSTABLES.put(APPLE_SEEDS.get(), 0.3f);
        composter.COMPOSTABLES.put(APPLE_LEAVES.get(), 0.3f);
        composter.COMPOSTABLES.put(APPLE_SAPLING.get(), 0.3f);
        composter.COMPOSTABLES.put(APPLE_CORE.get(), 0.35f);
    }
}
