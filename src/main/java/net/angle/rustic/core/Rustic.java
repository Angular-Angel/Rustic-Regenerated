/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.rustic.core;

import java.util.ArrayList;
import net.angle.rustic.common.blocks.plants.AppleLeavesBlock;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
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
    
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger(MODID);
    
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);

    public static final RegistryObject<Block> APPLE_LEAVES_BLOCK = BLOCKS.register("apple_leaves", () -> registerBlock(new AppleLeavesBlock()));
    
    public Rustic() {
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register the enqueueIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }
    
    private static Block registerBlock(Block block) {

        if (block instanceof LeavesBlock) {
            leafBlocks.add(block);
        }
        return block;
    }

    private void setup(final FMLCommonSetupEvent event)
    {
    }

    private void enqueueIMC(final InterModEnqueueEvent event)
    {
    }

    public void processIMC(final InterModProcessEvent event)
    {
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
        public static void registerBlockColors(final ColorHandlerEvent.Block event) {
            event.getBlockColors().register((state, world, pos, tintIndex) ->
                    world != null && pos != null
                            ? BiomeColors.getAverageFoliageColor(world, pos)
                            : FoliageColor.getDefaultColor(), leafBlocks.toArray(new Block[]{}));
        }
    }
}
