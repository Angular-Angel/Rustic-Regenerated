/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.rusticregen.client;

import java.util.ArrayList;
import net.angle.rusticregen.common.blocks.*;
import net.angle.rusticregen.core.RusticRegenerated;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.*;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

/**
 *
 * @author angle
 */
@Mod.EventBusSubscriber(modid=RusticRegenerated.MODID, value=Dist.CLIENT, bus=Mod.EventBusSubscriber.Bus.MOD)
public class ClientEventRegistry {
    
    public static ArrayList<Block> leafBlocks = new ArrayList<>();
    public static ArrayList<Item> leafItems = new ArrayList<>();
    
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

        event.getBlockColors().register((state, world, pos, tintIndex) -> {
            try {
                BlockState leafState = ((LeafCoveredEntityBlock) state.getBlock()).getBlockEntity(world, pos).getLeafState();
                return Minecraft.getInstance().getBlockColors().getColor(leafState, world, pos, tintIndex);
            } catch (Exception e) {
                return -1; //No tint!
            }}, ModBlocks.CROSSED_LOGS.get(), ModBlocks.VERTICAL_CROSSED_LOGS.get(), ModBlocks.STAKE.get());
    }

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.APPLE_SEEDS.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.APPLE_SAPLING.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.STAKE.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.CROSSED_LOGS.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.VERTICAL_CROSSED_LOGS.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.LEAF_COVERED_BLOCK.get(), RenderType.cutout());
    }

    @SubscribeEvent
    public static void onRegisterModelLoaders(ModelRegistryEvent event) {
        ModelLoaderRegistry.registerLoader(new ResourceLocation(RusticRegenerated.MODID, "leaf_covered"), LeafModelLoader.INSTANCE);
    }
    
    public static Block registerLeafBlock(Block block) {
        leafBlocks.add(block);
        return block;
    }
    
    public static Item registerLeafItem(Item item) {
        leafItems.add(item);
        return item;
    }
}