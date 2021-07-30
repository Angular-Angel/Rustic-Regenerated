/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.rustic.core;

import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.OptionalInt;
import net.angle.rustic.common.blocks.AppleLeavesBlock;
import net.angle.rustic.common.blocks.AppleSaplingBlock;
import net.angle.rustic.common.blocks.AppleSeedsBlock;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.ThreeLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FancyFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.MegaJungleFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.MegaPineFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.SimpleStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.AlterGroundDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.BeehiveDecorator;
import net.minecraft.world.level.levelgen.feature.trunkplacers.DarkOakTrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.FancyTrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.GiantTrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;
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
    
    public static ConfiguredFeature<TreeConfiguration, ?> APPLE_TREE;
    public static ConfiguredFeature<TreeConfiguration, ?> APPLE_BEES_0002;
    public static ConfiguredFeature<TreeConfiguration, ?> APPLE_BEES_002;
    public static ConfiguredFeature<TreeConfiguration, ?> APPLE_BEES_005;
    public static ConfiguredFeature<TreeConfiguration, ?> FANCY_APPLE_TREE;
    public static ConfiguredFeature<TreeConfiguration, ?> FANCY_APPLE_BEES_0002;
    public static ConfiguredFeature<TreeConfiguration, ?> FANCY_APPLE_BEES_002;
    public static ConfiguredFeature<TreeConfiguration, ?> FANCY_APPLE_BEES_005;
    public static ConfiguredFeature<TreeConfiguration, ?> MEDIUM_APPLE_TREE;
    public static ConfiguredFeature<TreeConfiguration, ?> MEGA_APPLE_TREE;
    
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);

    public static final RegistryObject<Block> APPLE_LEAVES_BLOCK = BLOCKS.register("apple_leaves", () -> registerLeafBlock(new AppleLeavesBlock()));
    
    public static final RegistryObject<Block> APPLE_SAPLING_BLOCK = BLOCKS.register("apple_sapling", () -> new AppleSaplingBlock());

    public static final RegistryObject<Block> APPLE_SEEDS_BLOCK = BLOCKS.register("apple_seeds", () -> new AppleSeedsBlock());
    
    public static final RegistryObject<Item> APPLE_LEAVES_ITEM = ITEMS.register("apple_leaves", () -> {
        return registerLeafItem(new BlockItem(APPLE_LEAVES_BLOCK.get(), new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS)));
    });
    
    public static final RegistryObject<Item> APPLE_SAPLING_ITEM = ITEMS.register("apple_sapling", () -> {
        return new BlockItem(APPLE_SAPLING_BLOCK.get(), new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS));
    });
    
    public static final RegistryObject<Item> APPLE_SEEDS_ITEM = ITEMS.register("apple_seeds", () -> {
        return new ItemNameBlockItem(APPLE_SEEDS_BLOCK.get(), new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS));
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
        APPLE_TREE = BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_FEATURE, "rustic:apple_tree",
            Feature.TREE.configured((new TreeConfiguration.TreeConfigurationBuilder(
                new SimpleStateProvider(Blocks.OAK_LOG.defaultBlockState()), 
                new StraightTrunkPlacer(4, 2, 0), new SimpleStateProvider(APPLE_LEAVES_BLOCK.get().defaultBlockState()), 
                new SimpleStateProvider(APPLE_SAPLING_BLOCK.get().defaultBlockState()), 
                new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3), 
                new TwoLayersFeatureSize(1, 0, 1))).ignoreVines().build()));
        
        APPLE_BEES_0002 = BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_FEATURE, "rustic:apple_bees_0002", 
                Feature.TREE.configured(APPLE_TREE.config().withDecorators(ImmutableList.of(new BeehiveDecorator(0.0002F)))));
        
        APPLE_BEES_002 = BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_FEATURE, "rustic:apple_bees_002", 
                Feature.TREE.configured(APPLE_TREE.config().withDecorators(ImmutableList.of(new BeehiveDecorator(0.002F)))));
        
        APPLE_BEES_005 = BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_FEATURE, "rustic:apple_bees_005", 
                Feature.TREE.configured(APPLE_TREE.config().withDecorators(ImmutableList.of(new BeehiveDecorator(0.005F)))));
        
        FANCY_APPLE_TREE = BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_FEATURE, "rustic:fancy_apple_tree",
            Feature.TREE.configured((new TreeConfiguration.TreeConfigurationBuilder(
                new SimpleStateProvider(Blocks.OAK_LOG.defaultBlockState()), 
                new FancyTrunkPlacer(3, 11, 0), new SimpleStateProvider(APPLE_LEAVES_BLOCK.get().defaultBlockState()), 
                new SimpleStateProvider(APPLE_SAPLING_BLOCK.get().defaultBlockState()), 
                new FancyFoliagePlacer(ConstantInt.of(2), ConstantInt.of(4), 4), 
                new TwoLayersFeatureSize(0, 0, 0, OptionalInt.of(4)))).ignoreVines().build()));
        
        FANCY_APPLE_BEES_0002 = BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_FEATURE, "rustic:fancy_apple_bees_0002", 
                Feature.TREE.configured(FANCY_APPLE_TREE.config().withDecorators(ImmutableList.of(new BeehiveDecorator(.0002F)))));
        
        FANCY_APPLE_BEES_002 = BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_FEATURE, "rustic:fancy_apple_bees_002", 
                Feature.TREE.configured(FANCY_APPLE_TREE.config().withDecorators(ImmutableList.of(new BeehiveDecorator(0.002F)))));
        
        FANCY_APPLE_BEES_005 = BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_FEATURE, "rustic:fancy_apple_bees_005", 
                Feature.TREE.configured(FANCY_APPLE_TREE.config().withDecorators(ImmutableList.of(new BeehiveDecorator(0.005F)))));
        
        MEDIUM_APPLE_TREE = BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_FEATURE, "rustic:medium_apple_tree",
            Feature.TREE.configured((new TreeConfiguration.TreeConfigurationBuilder(
                new SimpleStateProvider(Blocks.OAK_LOG.defaultBlockState()), 
                new DarkOakTrunkPlacer(6, 1, 1), new SimpleStateProvider(APPLE_LEAVES_BLOCK.get().defaultBlockState()), 
                new SimpleStateProvider(APPLE_SAPLING_BLOCK.get().defaultBlockState()), 
                new MegaJungleFoliagePlacer(ConstantInt.of(2), ConstantInt.of(2), 3), 
                new ThreeLayersFeatureSize(1, 1, 0, 1, 2, OptionalInt.empty()))).ignoreVines().decorators(ImmutableList.of(
                new AlterGroundDecorator(
                    new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder()
                    .add(Blocks.PODZOL.defaultBlockState(), 3).add(Blocks.GRASS_BLOCK.defaultBlockState(), 6)
                    .add(Blocks.COARSE_DIRT.defaultBlockState(), 1).build())))).build()));
        
        MEGA_APPLE_TREE = BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_FEATURE, "rustic:mega_apple_tree",
            Feature.TREE.configured((new TreeConfiguration.TreeConfigurationBuilder(
                new SimpleStateProvider(Blocks.OAK_LOG.defaultBlockState()), 
                new GiantTrunkPlacer(22, 2, 2), new SimpleStateProvider(APPLE_LEAVES_BLOCK.get().defaultBlockState()),
                new SimpleStateProvider(APPLE_SAPLING_BLOCK.get().defaultBlockState()), 
                new MegaPineFoliagePlacer(ConstantInt.of(0), ConstantInt.of(0), UniformInt.of(13, 17)), 
                new TwoLayersFeatureSize(1, 1, 2))).ignoreVines().decorators(ImmutableList.of(
                new AlterGroundDecorator(
                    new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder()
                    .add(Blocks.PODZOL.defaultBlockState(), 3).add(Blocks.GRASS_BLOCK.defaultBlockState(), 6)
                    .add(Blocks.COARSE_DIRT.defaultBlockState(), 1).build())))).build()));
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
            ItemBlockRenderTypes.setRenderLayer(APPLE_SEEDS_BLOCK.get(), RenderType.cutout());
        }
    }
}
