/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.rusticregen.common.biomes;

import com.google.common.collect.ImmutableList;
import java.util.OptionalInt;
import net.angle.rusticregen.common.blocks.ModBlocks;
import net.angle.rusticregen.core.Configs;
import static net.angle.rusticregen.core.RusticRegenerated.MODID;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.Features;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.valueproviders.*;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.*;
import net.minecraft.world.level.levelgen.feature.blockplacers.*;
import net.minecraft.world.level.levelgen.feature.configurations.*;
import net.minecraft.world.level.levelgen.feature.featuresize.*;
import net.minecraft.world.level.levelgen.feature.foliageplacers.*;
import net.minecraft.world.level.levelgen.feature.stateproviders.*;
import net.minecraft.world.level.levelgen.feature.treedecorators.*;
import net.minecraft.world.level.levelgen.feature.trunkplacers.*;
import net.minecraft.world.level.levelgen.placement.*;

/**
 *
 * @author angle
 */
public class ModFeatures {
    
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
    
    public static ConfiguredDecorator<?> CHANCE_10_1;
    public static ConfiguredDecorator<?> CHANCE_5_1;
    public static ConfiguredDecorator<?> CHANCE_02;
    public static ConfiguredDecorator<?> CHANCE_01;
    public static ConfiguredDecorator<?> CHANCE_005;
    public static ConfiguredDecorator<?> CHANCE_001;
    
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
    
    public static void setup() {
        PATCH_ALLIUM = BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_FEATURE, MODID + ":patch_allium",
            Feature.RANDOM_PATCH.configured((new RandomPatchConfiguration.GrassConfigurationBuilder(
                new SimpleStateProvider(Blocks.ALLIUM.defaultBlockState()),
                new SimpleBlockPlacer())).tries(64).noProjection().build())
                    .decorated(Features.Decorators.ADD_32).decorated(Features.Decorators.HEIGHTMAP_SQUARE).count(10));
        
        BlockStateProvider appleLeavesProvider = new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder()
                .add(ModBlocks.APPLE_LEAVES.get().defaultBlockState(), (int) (100 * Configs.COMMON.appleTreeFruitiness.get()))
                .add(Blocks.OAK_LEAVES.defaultBlockState(), (int) (100 * (1 - Configs.COMMON.appleTreeFruitiness.get()))).build());
        
        APPLE_TREE = BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_FEATURE, MODID + ":apple_tree",
            Feature.TREE.configured((new TreeConfiguration.TreeConfigurationBuilder(
                new SimpleStateProvider(Blocks.OAK_LOG.defaultBlockState()),
                new StraightTrunkPlacer(4, 2, 0), appleLeavesProvider,
                new SimpleStateProvider(ModBlocks.APPLE_SAPLING.get().defaultBlockState()),
                new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3),
                new TwoLayersFeatureSize(1, 0, 1))).ignoreVines().build()));
        
        APPLE_BEES_0002 = BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_FEATURE, MODID + ":apple_bees_0002",
                Feature.TREE.configured(APPLE_TREE.config().withDecorators(ImmutableList.of(Features.Decorators.BEEHIVE_0002))));
        
        APPLE_BEES_002 = BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_FEATURE, MODID + ":apple_bees_002",
                Feature.TREE.configured(APPLE_TREE.config().withDecorators(ImmutableList.of(Features.Decorators.BEEHIVE_002))));
        
        APPLE_BEES_005 = BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_FEATURE, MODID + ":apple_bees_005",
                Feature.TREE.configured(APPLE_TREE.config().withDecorators(ImmutableList.of(Features.Decorators.BEEHIVE_005))));
        
        FANCY_APPLE_TREE = BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_FEATURE, MODID + ":fancy_apple_tree",
            Feature.TREE.configured((new TreeConfiguration.TreeConfigurationBuilder(
                new SimpleStateProvider(Blocks.OAK_LOG.defaultBlockState()),
                new FancyTrunkPlacer(3, 11, 0), appleLeavesProvider,
                new SimpleStateProvider(ModBlocks.APPLE_SAPLING.get().defaultBlockState()),
                new FancyFoliagePlacer(ConstantInt.of(2), ConstantInt.of(4), 4),
                new TwoLayersFeatureSize(0, 0, 0, OptionalInt.of(4)))).ignoreVines().build()));
        
        FANCY_APPLE_BEES_0002 = BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_FEATURE, MODID + ":fancy_apple_bees_0002", 
                Feature.TREE.configured(FANCY_APPLE_TREE.config().withDecorators(ImmutableList.of(Features.Decorators.BEEHIVE_0002))));
        
        FANCY_APPLE_BEES_002 = BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_FEATURE, MODID + ":fancy_apple_bees_002", 
                Feature.TREE.configured(FANCY_APPLE_TREE.config().withDecorators(ImmutableList.of(Features.Decorators.BEEHIVE_002))));
        
        FANCY_APPLE_BEES_005 = BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_FEATURE, MODID + ":fancy_apple_bees_005", 
                Feature.TREE.configured(FANCY_APPLE_TREE.config().withDecorators(ImmutableList.of(Features.Decorators.BEEHIVE_005))));
        
        AlterGroundDecorator groundDecorator = new AlterGroundDecorator(
            new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder()
                .add(Blocks.PODZOL.defaultBlockState(), 3).add(Blocks.GRASS_BLOCK.defaultBlockState(), 12)
                .add(Blocks.COARSE_DIRT.defaultBlockState(), 1).add(Blocks.ROOTED_DIRT.defaultBlockState(), 1).build()));
        
        MEDIUM_APPLE_TREE = BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_FEATURE, MODID + ":medium_apple_tree",
            Feature.TREE.configured((new TreeConfiguration.TreeConfigurationBuilder(
                new SimpleStateProvider(Blocks.OAK_LOG.defaultBlockState()), 
                new DarkOakTrunkPlacer(6, 1, 1), appleLeavesProvider, 
                new SimpleStateProvider(ModBlocks.APPLE_SAPLING.get().defaultBlockState()), 
                new MegaJungleFoliagePlacer(ConstantInt.of(2), ConstantInt.of(2), 3), 
                new ThreeLayersFeatureSize(1, 1, 0, 1, 2, OptionalInt.empty()))).ignoreVines().decorators(ImmutableList.of(groundDecorator)).build()));
        
        MEGA_APPLE_TREE = BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_FEATURE, MODID + ":mega_apple_tree",
            Feature.TREE.configured((new TreeConfiguration.TreeConfigurationBuilder(
                new SimpleStateProvider(Blocks.OAK_LOG.defaultBlockState()), 
                new GiantTrunkPlacer(22, 2, 2), appleLeavesProvider,
                new SimpleStateProvider(ModBlocks.APPLE_SAPLING.get().defaultBlockState()), 
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
        
        CHANCE_10_1 = FeatureDecorator.COUNT_EXTRA.configured(new FrequencyWithExtraChanceDecoratorConfiguration(10, 0.1F, 1));
        CHANCE_5_1 = FeatureDecorator.COUNT_EXTRA.configured(new FrequencyWithExtraChanceDecoratorConfiguration(5, 0.1F, 1));
        CHANCE_02 = FeatureDecorator.COUNT_EXTRA.configured(new FrequencyWithExtraChanceDecoratorConfiguration(0, 0.02F, 1));
        CHANCE_01 = FeatureDecorator.COUNT_EXTRA.configured(new FrequencyWithExtraChanceDecoratorConfiguration(0, 0.01F, 1));
        CHANCE_005 = FeatureDecorator.COUNT_EXTRA.configured(new FrequencyWithExtraChanceDecoratorConfiguration(0, 0.005F, 1));
        CHANCE_001 = FeatureDecorator.COUNT_EXTRA.configured(new FrequencyWithExtraChanceDecoratorConfiguration(0, 0.001F, 1));
        
        TREES_APPLE = BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_FEATURE, MODID + ":trees_apple", 
                Feature.RANDOM_SELECTOR.configured(new RandomFeatureConfiguration(
            ImmutableList.of(FANCY_APPLE_BEES_0002.weighted(0.02F)), 
                APPLE_BEES_0002)).decorated(Features.Decorators.HEIGHTMAP_SQUARE).decorated(CHANCE_5_1));
        
        APPLE_TREES_02 = BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_FEATURE, MODID + ":apple_trees_02", 
                Feature.RANDOM_SELECTOR.configured(new RandomFeatureConfiguration(
            ImmutableList.of(FANCY_APPLE_BEES_0002.weighted(0.02F)), 
                APPLE_BEES_0002)).decorated(Features.Decorators.HEIGHTMAP_SQUARE).decorated(CHANCE_02));
        
        TREES_MEDIUM_APPLE = BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_FEATURE, MODID + ":trees_medium_apple", 
                Feature.RANDOM_SELECTOR.configured(new RandomFeatureConfiguration(
            ImmutableList.of(FANCY_APPLE_BEES_0002.weighted(0.02F)), 
                MEDIUM_APPLE_TREE)).decorated(Features.Decorators.HEIGHTMAP_SQUARE).decorated(CHANCE_5_1));
        
        MEDIUM_APPLE_TREES_02 = BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_FEATURE, MODID + ":medium_apple_trees_02", 
                Feature.RANDOM_SELECTOR.configured(new RandomFeatureConfiguration(
            ImmutableList.of(FANCY_APPLE_BEES_0002.weighted(0.02F), MEDIUM_APPLE_TREE.weighted(0.02F), MEGA_APPLE_TREE.weighted(0.01F)), 
                APPLE_BEES_0002)).decorated(Features.Decorators.HEIGHTMAP_SQUARE).decorated(CHANCE_02));
        
        APPLE_TREES_001 = BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_FEATURE, MODID + ":apple_trees_001", 
                Feature.RANDOM_SELECTOR.configured(new RandomFeatureConfiguration(
            ImmutableList.of(FANCY_APPLE_BEES_0002.weighted(0.02F)), 
                APPLE_BEES_0002)).decorated(Features.Decorators.HEIGHTMAP_SQUARE).decorated(CHANCE_001));
        
        TREES_MEGA_APPLE = BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_FEATURE, MODID + ":trees_mega_apple", 
                Feature.RANDOM_SELECTOR.configured(new RandomFeatureConfiguration(
            ImmutableList.of(FANCY_APPLE_BEES_0002.weighted(0.02F)), 
                MEGA_APPLE_TREE)).decorated(Features.Decorators.HEIGHTMAP_SQUARE).decorated(CHANCE_5_1));
        
        TREES_GREAT_OAK = BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_FEATURE, MODID + ":trees_great_oak", 
                GREAT_OAK.decorated(Features.Decorators.HEIGHTMAP_SQUARE).decorated(CHANCE_10_1));
        
        GREAT_OAK_01 = BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_FEATURE, MODID + ":great_oak_01", 
                GREAT_OAK.decorated(Features.Decorators.HEIGHTMAP_SQUARE).decorated(CHANCE_01));
        
        TREES_GRAND_BIRCH = BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_FEATURE, MODID + ":trees_grand_birch", 
                GRAND_BIRCH.decorated(Features.Decorators.HEIGHTMAP_SQUARE).decorated(CHANCE_10_1));
        
        GRAND_BIRCH_01 = BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_FEATURE, MODID + ":grand_birch_01", 
                GRAND_BIRCH.decorated(Features.Decorators.HEIGHTMAP_SQUARE).decorated(CHANCE_01));
        
        MEGA_PINE_005 = BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_FEATURE, MODID + ":mega_pine_005", 
                Features.MEGA_PINE.decorated(Features.Decorators.HEIGHTMAP_SQUARE).decorated(CHANCE_005));
        
        MEGA_SPRUCE_005 = BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_FEATURE, MODID + ":mega_spruce_005", 
                Features.MEGA_PINE.decorated(Features.Decorators.HEIGHTMAP_SQUARE).decorated(CHANCE_005));
    }
    
}
