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
import net.angle.rustic.common.blocks.CrossedLogsBlock;
import net.angle.rustic.common.grower.GrandBirchTreeGrower;
import net.angle.rustic.common.grower.GreatOakTreeGrower;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.Features;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.HeightmapConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RandomFeatureConfiguration;
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
import net.minecraft.world.level.levelgen.feature.trunkplacers.MegaJungleTrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;
import net.minecraft.world.level.levelgen.placement.FeatureDecorator;
import net.minecraft.world.level.levelgen.placement.FrequencyWithExtraChanceDecoratorConfiguration;
import net.minecraft.world.level.levelgen.placement.WaterDepthThresholdConfiguration;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.event.world.BiomeLoadingEvent;
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
    public static ConfiguredFeature<TreeConfiguration, ?> GREAT_OAK;
    public static ConfiguredFeature<TreeConfiguration, ?> GRAND_BIRCH;
    
    public static ConfiguredFeature<?, ?> APPLE_TREES_02;
    public static ConfiguredFeature<?, ?> APPLE_TREES_001;
    public static ConfiguredFeature<?, ?> MEDIUM_APPLE_TREES_02;
    public static ConfiguredFeature<?, ?> GREAT_OAK_01;
    public static ConfiguredFeature<?, ?> GRAND_BIRCH_01;
    public static ConfiguredFeature<?, ?> MEGA_PINE_005;
    public static ConfiguredFeature<?, ?> MEGA_SPRUCE_005;
    
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);

    public static final RegistryObject<Block> APPLE_LEAVES_BLOCK = BLOCKS.register("apple_leaves", () -> registerLeafBlock(new AppleLeavesBlock()));
    
    public static final RegistryObject<Block> APPLE_SAPLING_BLOCK = BLOCKS.register("apple_sapling", () -> new AppleSaplingBlock());

    public static final RegistryObject<Block> APPLE_SEEDS_BLOCK = BLOCKS.register("apple_seeds", () -> new AppleSeedsBlock());
    
    
    public static final RegistryObject<Block> CROSSED_LOGS_BLOCK = BLOCKS.register("crossed_logs", () -> new CrossedLogsBlock());
    
    public static final RegistryObject<Item> APPLE_LEAVES_ITEM = ITEMS.register("apple_leaves", () -> {
        return registerLeafItem(new BlockItem(APPLE_LEAVES_BLOCK.get(), new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS)));
    });
    
    public static final RegistryObject<Item> APPLE_SAPLING_ITEM = ITEMS.register("apple_sapling", () -> {
        return new BlockItem(APPLE_SAPLING_BLOCK.get(), new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS));
    });
    
    public static final RegistryObject<Item> APPLE_SEEDS_ITEM = ITEMS.register("apple_seeds", () -> {
        return new ItemNameBlockItem(APPLE_SEEDS_BLOCK.get(), new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS));
    });
    
    public static final RegistryObject<Item> CROOSSED_LOG_ITEM = ITEMS.register("crossed_logs", () -> {
        return new BlockItem(CROSSED_LOGS_BLOCK.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS));
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
        ((SaplingBlock) Blocks.OAK_SAPLING).treeGrower = new GreatOakTreeGrower();
        ((SaplingBlock) Blocks.BIRCH_SAPLING).treeGrower = new GrandBirchTreeGrower();
        
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
        
        AlterGroundDecorator groundDecorator = new AlterGroundDecorator(
            new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder()
                .add(Blocks.PODZOL.defaultBlockState(), 3).add(Blocks.GRASS_BLOCK.defaultBlockState(), 12)
                .add(Blocks.COARSE_DIRT.defaultBlockState(), 1).add(Blocks.ROOTED_DIRT.defaultBlockState(), 1).build()));
        
        MEDIUM_APPLE_TREE = BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_FEATURE, "rustic:medium_apple_tree",
            Feature.TREE.configured((new TreeConfiguration.TreeConfigurationBuilder(
                new SimpleStateProvider(Blocks.OAK_LOG.defaultBlockState()), 
                new DarkOakTrunkPlacer(6, 1, 1), new SimpleStateProvider(APPLE_LEAVES_BLOCK.get().defaultBlockState()), 
                new SimpleStateProvider(APPLE_SAPLING_BLOCK.get().defaultBlockState()), 
                new MegaJungleFoliagePlacer(ConstantInt.of(2), ConstantInt.of(2), 3), 
                new ThreeLayersFeatureSize(1, 1, 0, 1, 2, OptionalInt.empty()))).ignoreVines().decorators(ImmutableList.of(groundDecorator)).build()));
        
        MEGA_APPLE_TREE = BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_FEATURE, "rustic:mega_apple_tree",
            Feature.TREE.configured((new TreeConfiguration.TreeConfigurationBuilder(
                new SimpleStateProvider(Blocks.OAK_LOG.defaultBlockState()), 
                new GiantTrunkPlacer(22, 2, 2), new SimpleStateProvider(APPLE_LEAVES_BLOCK.get().defaultBlockState()),
                new SimpleStateProvider(APPLE_SAPLING_BLOCK.get().defaultBlockState()), 
                new MegaPineFoliagePlacer(ConstantInt.of(0), ConstantInt.of(0), UniformInt.of(13, 17)), 
                new TwoLayersFeatureSize(1, 1, 2))).ignoreVines().decorators(ImmutableList.of(groundDecorator)).build()));
        
        GREAT_OAK = BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_FEATURE, "rustic:great_oak",
            Feature.TREE.configured((new TreeConfiguration.TreeConfigurationBuilder(
                new SimpleStateProvider(Blocks.OAK_LOG.defaultBlockState()),
                new MegaJungleTrunkPlacer(19, 4, 15), new SimpleStateProvider(Blocks.OAK_LEAVES.defaultBlockState()),
                new SimpleStateProvider(Blocks.OAK_SAPLING.defaultBlockState()),
                new BlobFoliagePlacer(ConstantInt.of(4), ConstantInt.of(0), 5),
                new TwoLayersFeatureSize(1, 1, 2))).ignoreVines().decorators(ImmutableList.of(groundDecorator)).build()));
        
        GRAND_BIRCH = BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_FEATURE, "rustic:grand_birch",
            Feature.TREE.configured((new TreeConfiguration.TreeConfigurationBuilder(
                new SimpleStateProvider(Blocks.BIRCH_LOG.defaultBlockState()),
                new GiantTrunkPlacer(22, 4, 5), new SimpleStateProvider(Blocks.BIRCH_LEAVES.defaultBlockState()),
                new SimpleStateProvider(Blocks.BIRCH_SAPLING.defaultBlockState()),
                new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 7),
                new TwoLayersFeatureSize(1, 1, 2))).ignoreVines().decorators(ImmutableList.of(groundDecorator)).build()));
        
        APPLE_TREES_02 = Feature.RANDOM_SELECTOR.configured(new RandomFeatureConfiguration(
            ImmutableList.of(FANCY_APPLE_BEES_0002.weighted(0.02F), MEGA_APPLE_TREE.weighted(0.02F)), 
                APPLE_BEES_0002)).decorated(FeatureDecorator.HEIGHTMAP.configured(
                    new HeightmapConfiguration(Heightmap.Types.OCEAN_FLOOR)).decorated(
                        FeatureDecorator.WATER_DEPTH_THRESHOLD.configured(new WaterDepthThresholdConfiguration(0)))
                        .squared()).decorated(FeatureDecorator.COUNT_EXTRA.configured(
                            new FrequencyWithExtraChanceDecoratorConfiguration(0, 0.02F, 1)));
        
        MEDIUM_APPLE_TREES_02 = Feature.RANDOM_SELECTOR.configured(new RandomFeatureConfiguration(
            ImmutableList.of(FANCY_APPLE_BEES_0002.weighted(0.02F), MEDIUM_APPLE_TREE.weighted(0.02F), MEGA_APPLE_TREE.weighted(0.01F)), 
                APPLE_BEES_0002)).decorated(FeatureDecorator.HEIGHTMAP.configured(
                    new HeightmapConfiguration(Heightmap.Types.OCEAN_FLOOR)).decorated(
                        FeatureDecorator.WATER_DEPTH_THRESHOLD.configured(new WaterDepthThresholdConfiguration(0)))
                        .squared()).decorated(FeatureDecorator.COUNT_EXTRA.configured(
                            new FrequencyWithExtraChanceDecoratorConfiguration(0, 0.02F, 1)));
        
        APPLE_TREES_001 = Feature.RANDOM_SELECTOR.configured(new RandomFeatureConfiguration(
            ImmutableList.of(FANCY_APPLE_BEES_0002.weighted(0.02F)), 
                APPLE_BEES_0002)).decorated(FeatureDecorator.HEIGHTMAP.configured(
                    new HeightmapConfiguration(Heightmap.Types.OCEAN_FLOOR)).decorated(
                        FeatureDecorator.WATER_DEPTH_THRESHOLD.configured(new WaterDepthThresholdConfiguration(0)))
                        .squared()).decorated(FeatureDecorator.COUNT_EXTRA.configured(
                            new FrequencyWithExtraChanceDecoratorConfiguration(0, 0.001F, 1)));
        
        GREAT_OAK_01 = GREAT_OAK.decorated(FeatureDecorator.HEIGHTMAP.configured(
                    new HeightmapConfiguration(Heightmap.Types.OCEAN_FLOOR)).decorated(
                        FeatureDecorator.WATER_DEPTH_THRESHOLD.configured(new WaterDepthThresholdConfiguration(0)))
                        .squared()).decorated(FeatureDecorator.COUNT_EXTRA.configured(
                            new FrequencyWithExtraChanceDecoratorConfiguration(0, 0.01F, 1)));
        
        GRAND_BIRCH_01 = GRAND_BIRCH.decorated(FeatureDecorator.HEIGHTMAP.configured(
                    new HeightmapConfiguration(Heightmap.Types.OCEAN_FLOOR)).decorated(
                        FeatureDecorator.WATER_DEPTH_THRESHOLD.configured(new WaterDepthThresholdConfiguration(0)))
                        .squared()).decorated(FeatureDecorator.COUNT_EXTRA.configured(
                            new FrequencyWithExtraChanceDecoratorConfiguration(0, 0.01F, 1)));
        
        MEGA_PINE_005 = Features.MEGA_PINE.decorated(FeatureDecorator.HEIGHTMAP.configured(
                    new HeightmapConfiguration(Heightmap.Types.OCEAN_FLOOR)).decorated(
                        FeatureDecorator.WATER_DEPTH_THRESHOLD.configured(new WaterDepthThresholdConfiguration(0)))
                        .squared()).decorated(FeatureDecorator.COUNT_EXTRA.configured(
                            new FrequencyWithExtraChanceDecoratorConfiguration(0, 0.005F, 1)));
        
        MEGA_SPRUCE_005 = Features.MEGA_PINE.decorated(FeatureDecorator.HEIGHTMAP.configured(
                    new HeightmapConfiguration(Heightmap.Types.OCEAN_FLOOR)).decorated(
                        FeatureDecorator.WATER_DEPTH_THRESHOLD.configured(new WaterDepthThresholdConfiguration(0)))
                        .squared()).decorated(FeatureDecorator.COUNT_EXTRA.configured(
                            new FrequencyWithExtraChanceDecoratorConfiguration(0, 0.005F, 1)));
    }

    private void enqueueIMC(final InterModEnqueueEvent event) {
    }

    public void processIMC(final InterModProcessEvent event) {
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
    
    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.FORGE)
    public static class ForgeEvents {
        @SubscribeEvent
        public static void loadBiome(BiomeLoadingEvent event) {
            ResourceKey<Biome> biome = ResourceKey.create(Registry.BIOME_REGISTRY, event.getName());
            
            if (!BiomeDictionary.getTypes(biome).contains(Type.OVERWORLD))
                return;
            
            if (BiomeDictionary.getTypes(biome).contains(Type.SAVANNA) || 
                BiomeDictionary.getTypes(biome).contains(Type.JUNGLE) ||
                BiomeDictionary.getTypes(biome).contains(Type.HOT))
                return;
            
            if (BiomeDictionary.getTypes(biome).contains(Type.PLAINS) || BiomeDictionary.getTypes(biome).contains(Type.RIVER)) {
                event.getGeneration().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, APPLE_TREES_001);
            }
            
            if (BiomeDictionary.getTypes(biome).contains(Type.FOREST)) {
                String name = biome.toString();
                if (name.contains("birch")) {
                    //System.out.println("Birch: " + name);
                    event.getGeneration().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, APPLE_TREES_001);
                    event.getGeneration().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, GRAND_BIRCH_01);
                    if (name.contains("tall"))
                        event.getGeneration().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, GRAND_BIRCH_01);
                } else if (name.contains("dark")) {
                    //System.out.println("Dark: " + name);
                    event.getGeneration().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MEDIUM_APPLE_TREES_02);
                    event.getGeneration().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, GREAT_OAK_01);
                } else if (BiomeDictionary.getTypes(biome).contains(Type.COLD) || BiomeDictionary.getTypes(biome).contains(Type.CONIFEROUS) ||
                        name.contains("spruce") || name.contains("taiga")) {
                    //System.out.println("Spruce/Taiga: " + name);
                    event.getGeneration().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MEGA_PINE_005);
                    event.getGeneration().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MEGA_SPRUCE_005);
                } else {
                    //System.out.println("Forest: " + name);
                    event.getGeneration().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, APPLE_TREES_02);
                    event.getGeneration().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, GREAT_OAK_01);
                }
            }
        }
    }
}
