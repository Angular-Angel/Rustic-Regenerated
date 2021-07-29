/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.rustic.core;

import com.mojang.serialization.Codec;
import java.util.ArrayList;
import net.angle.rustic.common.blocks.AppleLeavesBlock;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.grower.OakTreeGrower;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.fmlserverevents.FMLServerStartingEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author angle
 */

// The value here should match an entry in the META-INF/mods.toml file
@Mod("rustic")
public class Rustic {
    public static final String MODID = "rustic";
    public static final String NAME = "Rustic Regenerated";
    
    public static ArrayList<Block> leafBlocks = new ArrayList<>();
    public static ArrayList<Item> leafItems = new ArrayList<>();
    
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger(MODID);
    
    public static ConfiguredFeature<?, ?> APPLE_TREE;
    
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);

    public static final RegistryObject<Block> APPLE_LEAVES_BLOCK = BLOCKS.register("apple_leaves", () -> registerLeafBlock(new AppleLeavesBlock()));
    
    public static final RegistryObject<Block> APPLE_SAPLING_BLOCK = BLOCKS.register("apple_sapling", () -> new SaplingBlock(new OakTreeGrower(), Properties.copy(Blocks.OAK_SAPLING)));

    public static final RegistryObject<Item> APPLE_LEAVES_ITEM = ITEMS.register("apple_leaves", () -> {
        return registerLeafItem(new BlockItem(APPLE_LEAVES_BLOCK.get(), new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS)));
    });
    
    public static final RegistryObject<Item> APPLE_SAPLING_ITEM = ITEMS.register("apple_sapling", () -> {
        return new BlockItem(APPLE_SAPLING_BLOCK.get(), new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS));
    });
    
    public Rustic() {
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register the enqueueIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }
    
    private static Block registerLeafBlock(Block block) {
        leafBlocks.add(block);
        return block;
    }
    
    private static Item registerLeafItem(Item item) {
        leafItems.add(item);
        return item;
    }

    private void setup(final FMLCommonSetupEvent event) {
//        APPLE_TREE = BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_FEATURE, "rustic:apple_tree", 
//            new ConfiguredFeature<TreeConfiguration, TreeFeature>(new TreeFeature(new Codec<>())), 
//                new TreeConfiguration(p_161217_, p_161218_, p_161219_, p_161220_, p_161221_, p_161222_, p_161223_, p_161224_, true, true) {
//        }));
    }

    private void enqueueIMC(final InterModEnqueueEvent event) {
    }

    public void processIMC(final InterModProcessEvent event) {
    }
    
    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
    }

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
            
        }
        
        @SubscribeEvent
        public static void registerItemColors(final ColorHandlerEvent.Item event) {
            event.getItemColors().register((ItemStack stack, int i) ->
                    FoliageColor.getDefaultColor(), leafItems.toArray(new Item[]{}));
        }
        
        @SubscribeEvent
        public static void registerBlockColors(final ColorHandlerEvent.Block event) {
            event.getBlockColors().register((state, world, pos, tintIndex) ->
                    world != null && pos != null
                            ? BiomeColors.getAverageFoliageColor(world, pos)
                            : FoliageColor.getDefaultColor(), leafBlocks.toArray(new Block[]{}));
        }
        
        @SubscribeEvent
        public static void clientSetup(FMLClientSetupEvent event) {
            ItemBlockRenderTypes.setRenderLayer(APPLE_SAPLING_BLOCK.get(), RenderType.cutout());
        }
    }
}
