/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.rusticregen.core;

import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.OptionalInt;
import net.angle.rusticregen.common.blocks.*;
import net.angle.rusticregen.common.blocks.entities.CrossedLogsEntity;
import net.angle.rusticregen.common.grower.GrandBirchTreeGrower;
import net.angle.rusticregen.common.grower.GreatOakTreeGrower;
import net.angle.rusticregen.common.grower.NormalAppleTreeGrower;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.Features;
import net.minecraft.data.worldgen.biome.VanillaBiomes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.RecipeBook;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.valueproviders.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biome.BiomeCategory;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.blockplacers.SimpleBlockPlacer;
import net.minecraft.world.level.levelgen.feature.configurations.*;
import net.minecraft.world.level.levelgen.feature.featuresize.*;
import net.minecraft.world.level.levelgen.feature.foliageplacers.*;
import net.minecraft.world.level.levelgen.feature.stateproviders.*;
import net.minecraft.world.level.levelgen.feature.treedecorators.*;
import net.minecraft.world.level.levelgen.feature.trunkplacers.*;
import net.minecraft.world.level.levelgen.placement.FeatureDecorator;
import net.minecraft.world.level.levelgen.placement.FrequencyWithExtraChanceDecoratorConfiguration;
import net.minecraft.world.level.levelgen.placement.WaterDepthThresholdConfiguration;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.minecraftforge.client.model.ModelLoader;

/**
 *
 * @author angle
 */

// The value here should match an entry in the META-INF/mods.toml file
@Mod("rusticregen")
public class RusticRegenerated {
    public static final String MODID = "rusticregen";
    public static final String NAME = "Rustic Regenerated";
    public static RusticRegenerated INSTANCE;
    
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
    
    public static ConfiguredFeature<?, ?> PATCH_ALLIUM;
    public static ConfiguredFeature<?, ?> TREES_APPLE;
    public static ConfiguredFeature<?, ?> APPLE_TREES_02;
    public static ConfiguredFeature<?, ?> APPLE_TREES_001;
    public static ConfiguredFeature<?, ?> TREES_MEDIUM_APPLE;
    public static ConfiguredFeature<?, ?> MEDIUM_APPLE_TREES_02;
    public static ConfiguredFeature<?, ?> TREES_MEGA_APPLE;
    public static ConfiguredFeature<?, ?> TREES_GREAT_OAK;
    public static ConfiguredFeature<?, ?> GREAT_OAK_01;
    public static ConfiguredFeature<?, ?> TREES_GRAND_BIRCH;
    public static ConfiguredFeature<?, ?> GRAND_BIRCH_01;
    public static ConfiguredFeature<?, ?> MEGA_PINE_005;
    public static ConfiguredFeature<?, ?> MEGA_SPRUCE_005;
    
    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, MODID);
    
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    
    private static final DeferredRegister<Biome> BIOMES = DeferredRegister.create(ForgeRegistries.BIOMES, MODID);
    
    public static final RegistryObject<BlockEntityType<CrossedLogsEntity>> CROSSED_LOGS_ENTITY_TYPE = 
            BLOCK_ENTITIES.register("crossed_logs", () -> BlockEntityType.Builder.of(CrossedLogsEntity::new, ModBlocks.CROSSED_LOGS_BLOCK.get()).build(null));
    
    public static final RegistryObject<Item> APPLE_LEAVES_ITEM = ITEMS.register("apple_leaves", () -> {
        return registerLeafItem(new BlockItem(ModBlocks.APPLE_LEAVES_BLOCK.get(), new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS)));
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
    
    public static final RegistryObject<Biome> GREAT_OAK_FOREST_BIOME = BIOMES.register("great_oak_forest", () -> {
        return VanillaBiomes.theVoidBiome();
    });
    
    public static final RegistryObject<Biome> GRAND_BIRCH_FOREST_BIOME = BIOMES.register("grand_birch_forest", () -> {
        return VanillaBiomes.theVoidBiome();
    });
    
    public static final RegistryObject<Biome> APPLE_ORCHARD_BIOME = BIOMES.register("apple_orchard", () -> {
        return VanillaBiomes.theVoidBiome();
    });
    
    public static final RegistryObject<Biome> MEDIUM_APPLE_ORCHARD_BIOME = BIOMES.register("medium_apple_orchard", () -> {
        return VanillaBiomes.theVoidBiome();
    });
    
    public static final RegistryObject<Biome> MEGA_APPLE_ORCHARD_BIOME = BIOMES.register("mega_apple_orchard", () -> {
        return VanillaBiomes.theVoidBiome();
    });
    
    public RusticRegenerated() {
        INSTANCE = this;
        ModLoadingContext modLoadingContext = ModLoadingContext.get();
        modLoadingContext.registerConfig(ModConfig.Type.SERVER, Configs.SERVER_SPECIFICATION);
        modLoadingContext.registerConfig(ModConfig.Type.COMMON, Configs.COMMON_SPECIFICATION);
        
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        
        ModBlocks.BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        
        BLOCK_ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
        
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        
        BIOMES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
    
    public static Block registerLeafBlock(Block block) {
        leafBlocks.add(block);
        return block;
    }
    
    private static Item registerLeafItem(Item item) {
        leafItems.add(item);
        return item;
    }

    private void setup(final FMLCommonSetupEvent event) {
        if (Configs.COMMON.addGiantTrees.get() && Configs.COMMON.addGiantTreeGrowth.get()) {
            ((SaplingBlock) Blocks.OAK_SAPLING).treeGrower = new GreatOakTreeGrower();
            ((SaplingBlock) Blocks.BIRCH_SAPLING).treeGrower = new GrandBirchTreeGrower();
        } else
            ((SaplingBlock) ModBlocks.APPLE_SAPLING_BLOCK.get()).treeGrower = new NormalAppleTreeGrower();
        
        PATCH_ALLIUM = BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_FEATURE, MODID + ":patch_allium",
            Feature.RANDOM_PATCH.configured((new RandomPatchConfiguration.GrassConfigurationBuilder(
                new SimpleStateProvider(Blocks.ALLIUM.defaultBlockState()),
                new SimpleBlockPlacer())).tries(64).noProjection().build()).decorated(
                    FeatureDecorator.SPREAD_32_ABOVE.configured(NoneDecoratorConfiguration.INSTANCE)).decorated(
                    FeatureDecorator.HEIGHTMAP.configured(new HeightmapConfiguration(Heightmap.Types.OCEAN_FLOOR)).decorated(
                    FeatureDecorator.WATER_DEPTH_THRESHOLD.configured(new WaterDepthThresholdConfiguration(0))).squared()).count(10));
        
        BlockStateProvider appleLeavesProvider = new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder()
                .add(ModBlocks.APPLE_LEAVES_BLOCK.get().defaultBlockState(), (int) (100 * Configs.COMMON.appleTreeFruitiness.get()))
                .add(Blocks.OAK_LEAVES.defaultBlockState(), (int) (100 * (1 - Configs.COMMON.appleTreeFruitiness.get()))).build());
        
        APPLE_TREE = BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_FEATURE, MODID + ":apple_tree",
            Feature.TREE.configured((new TreeConfiguration.TreeConfigurationBuilder(
                new SimpleStateProvider(Blocks.OAK_LOG.defaultBlockState()),
                new StraightTrunkPlacer(4, 2, 0), appleLeavesProvider,
                new SimpleStateProvider(ModBlocks.APPLE_SAPLING_BLOCK.get().defaultBlockState()),
                new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3),
                new TwoLayersFeatureSize(1, 0, 1))).ignoreVines().build()));
        
        APPLE_BEES_0002 = BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_FEATURE, MODID + ":apple_bees_0002",
                Feature.TREE.configured(APPLE_TREE.config().withDecorators(ImmutableList.of(new BeehiveDecorator(0.0002F)))));
        
        APPLE_BEES_002 = BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_FEATURE, MODID + ":apple_bees_002",
                Feature.TREE.configured(APPLE_TREE.config().withDecorators(ImmutableList.of(new BeehiveDecorator(0.002F)))));
        
        APPLE_BEES_005 = BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_FEATURE, MODID + ":apple_bees_005",
                Feature.TREE.configured(APPLE_TREE.config().withDecorators(ImmutableList.of(new BeehiveDecorator(0.005F)))));
        
        FANCY_APPLE_TREE = BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_FEATURE, MODID + ":fancy_apple_tree",
            Feature.TREE.configured((new TreeConfiguration.TreeConfigurationBuilder(
                new SimpleStateProvider(Blocks.OAK_LOG.defaultBlockState()),
                new FancyTrunkPlacer(3, 11, 0), appleLeavesProvider,
                new SimpleStateProvider(ModBlocks.APPLE_SAPLING_BLOCK.get().defaultBlockState()),
                new FancyFoliagePlacer(ConstantInt.of(2), ConstantInt.of(4), 4),
                new TwoLayersFeatureSize(0, 0, 0, OptionalInt.of(4)))).ignoreVines().build()));
        
        FANCY_APPLE_BEES_0002 = BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_FEATURE, MODID + ":fancy_apple_bees_0002", 
                Feature.TREE.configured(FANCY_APPLE_TREE.config().withDecorators(ImmutableList.of(new BeehiveDecorator(.0002F)))));
        
        FANCY_APPLE_BEES_002 = BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_FEATURE, MODID + ":fancy_apple_bees_002", 
                Feature.TREE.configured(FANCY_APPLE_TREE.config().withDecorators(ImmutableList.of(new BeehiveDecorator(0.002F)))));
        
        FANCY_APPLE_BEES_005 = BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_FEATURE, MODID + ":fancy_apple_bees_005", 
                Feature.TREE.configured(FANCY_APPLE_TREE.config().withDecorators(ImmutableList.of(new BeehiveDecorator(0.005F)))));
        
        AlterGroundDecorator groundDecorator = new AlterGroundDecorator(
            new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder()
                .add(Blocks.PODZOL.defaultBlockState(), 3).add(Blocks.GRASS_BLOCK.defaultBlockState(), 12)
                .add(Blocks.COARSE_DIRT.defaultBlockState(), 1).add(Blocks.ROOTED_DIRT.defaultBlockState(), 1).build()));
        
        MEDIUM_APPLE_TREE = BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_FEATURE, MODID + ":medium_apple_tree",
            Feature.TREE.configured((new TreeConfiguration.TreeConfigurationBuilder(
                new SimpleStateProvider(Blocks.OAK_LOG.defaultBlockState()), 
                new DarkOakTrunkPlacer(6, 1, 1), appleLeavesProvider, 
                new SimpleStateProvider(ModBlocks.APPLE_SAPLING_BLOCK.get().defaultBlockState()), 
                new MegaJungleFoliagePlacer(ConstantInt.of(2), ConstantInt.of(2), 3), 
                new ThreeLayersFeatureSize(1, 1, 0, 1, 2, OptionalInt.empty()))).ignoreVines().decorators(ImmutableList.of(groundDecorator)).build()));
        
        MEGA_APPLE_TREE = BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_FEATURE, MODID + ":mega_apple_tree",
            Feature.TREE.configured((new TreeConfiguration.TreeConfigurationBuilder(
                new SimpleStateProvider(Blocks.OAK_LOG.defaultBlockState()), 
                new GiantTrunkPlacer(22, 2, 2), appleLeavesProvider,
                new SimpleStateProvider(ModBlocks.APPLE_SAPLING_BLOCK.get().defaultBlockState()), 
                new MegaPineFoliagePlacer(ConstantInt.of(0), ConstantInt.of(0), UniformInt.of(13, 17)), 
                new TwoLayersFeatureSize(1, 1, 2))).ignoreVines().decorators(ImmutableList.of(groundDecorator)).build()));
        
        GREAT_OAK = BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_FEATURE, MODID + ":great_oak",
            Feature.TREE.configured((new TreeConfiguration.TreeConfigurationBuilder(
                new SimpleStateProvider(Blocks.OAK_LOG.defaultBlockState()),
                new MegaJungleTrunkPlacer(19, 4, 15), new SimpleStateProvider(Blocks.OAK_LEAVES.defaultBlockState()),
                new SimpleStateProvider(Blocks.OAK_SAPLING.defaultBlockState()),
                new FancyFoliagePlacer(ConstantInt.of(3), ConstantInt.of(4), 7),
                new TwoLayersFeatureSize(1, 1, 2))).ignoreVines().decorators(ImmutableList.of(groundDecorator)).build()));
        
        GRAND_BIRCH = BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_FEATURE, MODID + ":grand_birch",
            Feature.TREE.configured((new TreeConfiguration.TreeConfigurationBuilder(
                new SimpleStateProvider(Blocks.BIRCH_LOG.defaultBlockState()),
                new GiantTrunkPlacer(22, 4, 5), new SimpleStateProvider(Blocks.BIRCH_LEAVES.defaultBlockState()),
                new SimpleStateProvider(Blocks.BIRCH_SAPLING.defaultBlockState()),
                new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 7),
                new TwoLayersFeatureSize(1, 1, 2))).ignoreVines().decorators(ImmutableList.of(groundDecorator)).build()));
        
        TREES_APPLE = BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_FEATURE, MODID + ":trees_apple", 
                Feature.RANDOM_SELECTOR.configured(new RandomFeatureConfiguration(
            ImmutableList.of(FANCY_APPLE_BEES_0002.weighted(0.02F)), 
                APPLE_BEES_0002)).decorated(FeatureDecorator.HEIGHTMAP.configured(
                    new HeightmapConfiguration(Heightmap.Types.OCEAN_FLOOR)).decorated(
                        FeatureDecorator.WATER_DEPTH_THRESHOLD.configured(new WaterDepthThresholdConfiguration(0)))
                        .squared()).decorated(FeatureDecorator.COUNT_EXTRA.configured(
                            new FrequencyWithExtraChanceDecoratorConfiguration(5, 0.1F, 1))));
        
        APPLE_TREES_02 = BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_FEATURE, MODID + ":apple_trees_02", 
                Feature.RANDOM_SELECTOR.configured(new RandomFeatureConfiguration(
            ImmutableList.of(FANCY_APPLE_BEES_0002.weighted(0.02F)), 
                APPLE_BEES_0002)).decorated(FeatureDecorator.HEIGHTMAP.configured(
                    new HeightmapConfiguration(Heightmap.Types.OCEAN_FLOOR)).decorated(
                        FeatureDecorator.WATER_DEPTH_THRESHOLD.configured(new WaterDepthThresholdConfiguration(0)))
                        .squared()).decorated(FeatureDecorator.COUNT_EXTRA.configured(
                            new FrequencyWithExtraChanceDecoratorConfiguration(0, 0.02F, 1))));
        
        TREES_MEDIUM_APPLE = BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_FEATURE, MODID + ":trees_medium_apple", 
                Feature.RANDOM_SELECTOR.configured(new RandomFeatureConfiguration(
            ImmutableList.of(FANCY_APPLE_BEES_0002.weighted(0.02F)), 
                MEDIUM_APPLE_TREE)).decorated(FeatureDecorator.HEIGHTMAP.configured(
                    new HeightmapConfiguration(Heightmap.Types.OCEAN_FLOOR)).decorated(
                        FeatureDecorator.WATER_DEPTH_THRESHOLD.configured(new WaterDepthThresholdConfiguration(0)))
                        .squared()).decorated(FeatureDecorator.COUNT_EXTRA.configured(
                            new FrequencyWithExtraChanceDecoratorConfiguration(5, 0.1F, 1))));
        
        MEDIUM_APPLE_TREES_02 = BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_FEATURE, MODID + ":medium_apple_trees_02", 
                Feature.RANDOM_SELECTOR.configured(new RandomFeatureConfiguration(
            ImmutableList.of(FANCY_APPLE_BEES_0002.weighted(0.02F), MEDIUM_APPLE_TREE.weighted(0.02F), MEGA_APPLE_TREE.weighted(0.01F)), 
                APPLE_BEES_0002)).decorated(FeatureDecorator.HEIGHTMAP.configured(
                    new HeightmapConfiguration(Heightmap.Types.OCEAN_FLOOR)).decorated(
                        FeatureDecorator.WATER_DEPTH_THRESHOLD.configured(new WaterDepthThresholdConfiguration(0)))
                        .squared()).decorated(FeatureDecorator.COUNT_EXTRA.configured(
                            new FrequencyWithExtraChanceDecoratorConfiguration(0, 0.02F, 1))));
        
        APPLE_TREES_001 = BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_FEATURE, MODID + ":apple_trees_001", 
                Feature.RANDOM_SELECTOR.configured(new RandomFeatureConfiguration(
            ImmutableList.of(FANCY_APPLE_BEES_0002.weighted(0.02F)), 
                APPLE_BEES_0002)).decorated(FeatureDecorator.HEIGHTMAP.configured(
                    new HeightmapConfiguration(Heightmap.Types.OCEAN_FLOOR)).decorated(
                        FeatureDecorator.WATER_DEPTH_THRESHOLD.configured(new WaterDepthThresholdConfiguration(0)))
                        .squared()).decorated(FeatureDecorator.COUNT_EXTRA.configured(
                            new FrequencyWithExtraChanceDecoratorConfiguration(0, 0.001F, 1))));
        
        TREES_MEGA_APPLE = BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_FEATURE, MODID + ":trees_mega_apple", 
                Feature.RANDOM_SELECTOR.configured(new RandomFeatureConfiguration(
            ImmutableList.of(FANCY_APPLE_BEES_0002.weighted(0.02F)), 
                MEGA_APPLE_TREE)).decorated(FeatureDecorator.HEIGHTMAP.configured(
                    new HeightmapConfiguration(Heightmap.Types.OCEAN_FLOOR)).decorated(
                        FeatureDecorator.WATER_DEPTH_THRESHOLD.configured(new WaterDepthThresholdConfiguration(0)))
                        .squared()).decorated(FeatureDecorator.COUNT_EXTRA.configured(
                            new FrequencyWithExtraChanceDecoratorConfiguration(5, 0.1F, 1))));
        
        TREES_GREAT_OAK = BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_FEATURE, MODID + ":trees_great_oak", 
                GREAT_OAK.decorated(FeatureDecorator.HEIGHTMAP.configured(
                    new HeightmapConfiguration(Heightmap.Types.OCEAN_FLOOR)).decorated(
                        FeatureDecorator.WATER_DEPTH_THRESHOLD.configured(new WaterDepthThresholdConfiguration(0)))
                        .squared()).decorated(FeatureDecorator.COUNT_EXTRA.configured(
                            new FrequencyWithExtraChanceDecoratorConfiguration(10, 0.1F, 1))));
        
        GREAT_OAK_01 = BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_FEATURE, MODID + ":great_oak_01", 
                GREAT_OAK.decorated(FeatureDecorator.HEIGHTMAP.configured(
                    new HeightmapConfiguration(Heightmap.Types.OCEAN_FLOOR)).decorated(
                        FeatureDecorator.WATER_DEPTH_THRESHOLD.configured(new WaterDepthThresholdConfiguration(0)))
                        .squared()).decorated(FeatureDecorator.COUNT_EXTRA.configured(
                            new FrequencyWithExtraChanceDecoratorConfiguration(0, 0.01F, 1))));
        
        TREES_GRAND_BIRCH = BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_FEATURE, MODID + ":trees_grand_birch", 
                GRAND_BIRCH.decorated(FeatureDecorator.HEIGHTMAP.configured(
                    new HeightmapConfiguration(Heightmap.Types.OCEAN_FLOOR)).decorated(
                        FeatureDecorator.WATER_DEPTH_THRESHOLD.configured(new WaterDepthThresholdConfiguration(0)))
                        .squared()).decorated(FeatureDecorator.COUNT_EXTRA.configured(
                            new FrequencyWithExtraChanceDecoratorConfiguration(10, 0.1F, 1))));
        
        GRAND_BIRCH_01 = BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_FEATURE, MODID + ":grand_birch_01", 
                GRAND_BIRCH.decorated(FeatureDecorator.HEIGHTMAP.configured(
                    new HeightmapConfiguration(Heightmap.Types.OCEAN_FLOOR)).decorated(
                        FeatureDecorator.WATER_DEPTH_THRESHOLD.configured(new WaterDepthThresholdConfiguration(0)))
                        .squared()).decorated(FeatureDecorator.COUNT_EXTRA.configured(
                            new FrequencyWithExtraChanceDecoratorConfiguration(0, 0.01F, 1))));
        
        MEGA_PINE_005 = BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_FEATURE, MODID + ":mega_pine_005", 
                Features.MEGA_PINE.decorated(FeatureDecorator.HEIGHTMAP.configured(
                    new HeightmapConfiguration(Heightmap.Types.OCEAN_FLOOR)).decorated(
                        FeatureDecorator.WATER_DEPTH_THRESHOLD.configured(new WaterDepthThresholdConfiguration(0)))
                        .squared()).decorated(FeatureDecorator.COUNT_EXTRA.configured(
                            new FrequencyWithExtraChanceDecoratorConfiguration(0, 0.005F, 1))));
        
        MEGA_SPRUCE_005 = BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_FEATURE, MODID + ":mega_spruce_005", 
                Features.MEGA_PINE.decorated(FeatureDecorator.HEIGHTMAP.configured(
                    new HeightmapConfiguration(Heightmap.Types.OCEAN_FLOOR)).decorated(
                        FeatureDecorator.WATER_DEPTH_THRESHOLD.configured(new WaterDepthThresholdConfiguration(0)))
                        .squared()).decorated(FeatureDecorator.COUNT_EXTRA.configured(
                            new FrequencyWithExtraChanceDecoratorConfiguration(0, 0.005F, 1))));
        
        if (Configs.COMMON.addNewBiomes.get()) {
        
            ResourceKey<Biome> great_oak_forest = ResourceKey.create(Registry.BIOME_REGISTRY, GREAT_OAK_FOREST_BIOME.getId());
            BiomeDictionary.addTypes(great_oak_forest, Type.OVERWORLD, Type.FOREST, Type.DENSE, Type.RARE);
            BiomeManager.addBiome(BiomeManager.BiomeType.COOL, new BiomeManager.BiomeEntry(great_oak_forest, 1));

            ResourceKey<Biome> grand_birch_forest = ResourceKey.create(Registry.BIOME_REGISTRY, GRAND_BIRCH_FOREST_BIOME.getId());
            BiomeDictionary.addTypes(grand_birch_forest, Type.OVERWORLD, Type.FOREST, Type.DENSE, Type.RARE);
            BiomeManager.addBiome(BiomeManager.BiomeType.COOL, new BiomeManager.BiomeEntry(grand_birch_forest, 1));

            ResourceKey<Biome> apple_orchard = ResourceKey.create(Registry.BIOME_REGISTRY, APPLE_ORCHARD_BIOME.getId());
            BiomeDictionary.addTypes(apple_orchard, Type.OVERWORLD, Type.FOREST, Type.RARE);
            BiomeManager.addBiome(BiomeManager.BiomeType.COOL, new BiomeManager.BiomeEntry(apple_orchard, 4));

            ResourceKey<Biome> medium_apple_orchard = ResourceKey.create(Registry.BIOME_REGISTRY, MEDIUM_APPLE_ORCHARD_BIOME.getId());
            BiomeDictionary.addTypes(medium_apple_orchard, Type.OVERWORLD, Type.FOREST, Type.DENSE, Type.RARE);
            BiomeManager.addBiome(BiomeManager.BiomeType.COOL, new BiomeManager.BiomeEntry(medium_apple_orchard, 2));
            
            ResourceKey<Biome> mega_apple_orchard = ResourceKey.create(Registry.BIOME_REGISTRY, MEGA_APPLE_ORCHARD_BIOME.getId());
            BiomeDictionary.addTypes(mega_apple_orchard, Type.OVERWORLD, Type.FOREST, Type.DENSE, Type.RARE);
            BiomeManager.addBiome(BiomeManager.BiomeType.COOL, new BiomeManager.BiomeEntry(mega_apple_orchard, 1));
        }
    }

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
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.APPLE_SAPLING_BLOCK.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.APPLE_SEEDS_BLOCK.get(), RenderType.cutout());
        }
        
        @SubscribeEvent
        public static void onRegisterModelLoaders(ModelRegistryEvent event) {
            //ModelLoaderRegistry.registerLoader(new ResourceLocation(MODID, "leaf_models"), );
        }
        
    }
    
    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.FORGE)
    public static class ForgeEvents {
        @SubscribeEvent
        public static void loadBiome(BiomeLoadingEvent event) {
            if (!Configs.COMMON.modifyBiomes.get())
                return;
            
            ResourceKey<Biome> biome = ResourceKey.create(Registry.BIOME_REGISTRY, event.getName());
            String name = biome.toString().split(":")[2];
            
            BiomeCategory category = event.getCategory();
            if (category == BiomeCategory.NETHER || category == BiomeCategory.THEEND 
                || category == BiomeCategory.UNDERGROUND || category == BiomeCategory.NONE)
                return;
            
            if (event.getClimate().temperature > 1 || event.getClimate().downfall < 0.5)
                return;
            
            boolean addApples = Configs.COMMON.addAppleTreesInNonAppleBiomes.get();
            if (category == BiomeCategory.PLAINS || category == BiomeCategory.RIVER || category == BiomeCategory.SWAMP) {
                if (addApples)
                    event.getGeneration().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, APPLE_TREES_001);
            }
            
            boolean addGiants = Configs.COMMON.addGiantTrees.get() && Configs.COMMON.addGiantTreesInNonGiantBiomes.get();
            if (category == BiomeCategory.FOREST || category == BiomeCategory.TAIGA) {
                if (name.contains("birch")) {
                    //System.out.println("Birch: " + name);
                    if (addApples)
                        event.getGeneration().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, APPLE_TREES_001);
                    if (addGiants) {
                        event.getGeneration().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, GRAND_BIRCH_01);
                        if (name.contains("tall"))
                            event.getGeneration().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, GRAND_BIRCH_01);
                    }
                } else if (name.contains("dark")) {
                    //System.out.println("Dark: " + name);
                    if (addGiants) {
                        if (addApples)
                            event.getGeneration().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MEDIUM_APPLE_TREES_02);
                        event.getGeneration().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, GREAT_OAK_01);
                    } else if (addApples)
                        event.getGeneration().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, APPLE_TREES_02);
                } else if (category == BiomeCategory.TAIGA || event.getClimate().temperature < 0.4 || name.contains("spruce")) {
                    //System.out.println("Spruce/Taiga: " + name);
                    if (addGiants) {
                        event.getGeneration().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MEGA_PINE_005);
                        event.getGeneration().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MEGA_SPRUCE_005);
                    }
                } else {
                    //System.out.println("Forest: " + name);
                    if (addApples)
                        event.getGeneration().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, APPLE_TREES_02);
                    if (addGiants)
                        event.getGeneration().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, GREAT_OAK_01);
                }
            }
        }
    }
}
