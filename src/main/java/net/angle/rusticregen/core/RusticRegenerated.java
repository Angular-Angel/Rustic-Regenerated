/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.rusticregen.core;

import net.angle.rusticregen.common.biomes.ModBiomes;
import net.angle.rusticregen.common.biomes.ModFeatures;
import net.angle.rusticregen.common.blocks.*;
import static net.angle.rusticregen.common.blocks.LeafCoveredEntityBlock.LEAVES;
import net.angle.rusticregen.common.blocks.entities.LeafCoveredBlockEntity;
import net.angle.rusticregen.common.grower.*;
import net.angle.rusticregen.common.items.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biome.BiomeCategory;
import net.minecraft.world.level.block.*;
import static net.minecraft.world.level.block.Block.UPDATE_ALL;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
    
    // Directly reference a log4j logger.
    public static final Logger LOGGER = LogManager.getLogger(MODID);
    
    public RusticRegenerated() {
        INSTANCE = this;
        ModLoadingContext modLoadingContext = ModLoadingContext.get();
        modLoadingContext.registerConfig(ModConfig.Type.SERVER, Configs.SERVER_SPECIFICATION);
        modLoadingContext.registerConfig(ModConfig.Type.COMMON, Configs.COMMON_SPECIFICATION);
        
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        
        ModBlocks.BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        
        ModBlocks.BLOCK_ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
        
        ModItems.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        
        ModBiomes.BIOMES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    private void setup(final FMLCommonSetupEvent event) {
        if (Configs.COMMON.addGiantTrees.get() && Configs.COMMON.addGiantTreeGrowth.get()) {
            ((SaplingBlock) Blocks.OAK_SAPLING).treeGrower = new GreatOakTreeGrower();
            ((SaplingBlock) Blocks.BIRCH_SAPLING).treeGrower = new GrandBirchTreeGrower();
        } else
            ((SaplingBlock) ModBlocks.APPLE_SAPLING.get()).treeGrower = new NormalAppleTreeGrower();
        
        ModItems.setup();
        
        ModFeatures.setup();
        
        if (Configs.COMMON.addNewBiomes.get()) {
        
            ResourceKey<Biome> great_oak_forest = ResourceKey.create(Registry.BIOME_REGISTRY, ModBiomes.GREAT_OAK_FOREST_BIOME.getId());
            BiomeDictionary.addTypes(great_oak_forest, Type.OVERWORLD, Type.FOREST, Type.DENSE, Type.RARE);
            BiomeManager.addBiome(BiomeManager.BiomeType.COOL, new BiomeManager.BiomeEntry(great_oak_forest, 1));

            ResourceKey<Biome> grand_birch_forest = ResourceKey.create(Registry.BIOME_REGISTRY, ModBiomes.GRAND_BIRCH_FOREST_BIOME.getId());
            BiomeDictionary.addTypes(grand_birch_forest, Type.OVERWORLD, Type.FOREST, Type.DENSE, Type.RARE);
            BiomeManager.addBiome(BiomeManager.BiomeType.COOL, new BiomeManager.BiomeEntry(grand_birch_forest, 1));

            ResourceKey<Biome> apple_orchard = ResourceKey.create(Registry.BIOME_REGISTRY, ModBiomes.APPLE_ORCHARD_BIOME.getId());
            BiomeDictionary.addTypes(apple_orchard, Type.OVERWORLD, Type.FOREST, Type.RARE);
            BiomeManager.addBiome(BiomeManager.BiomeType.COOL, new BiomeManager.BiomeEntry(apple_orchard, 4));

            ResourceKey<Biome> medium_apple_orchard = ResourceKey.create(Registry.BIOME_REGISTRY, ModBiomes.MEDIUM_APPLE_ORCHARD_BIOME.getId());
            BiomeDictionary.addTypes(medium_apple_orchard, Type.OVERWORLD, Type.FOREST, Type.DENSE, Type.RARE);
            BiomeManager.addBiome(BiomeManager.BiomeType.COOL, new BiomeManager.BiomeEntry(medium_apple_orchard, 2));
            
            ResourceKey<Biome> mega_apple_orchard = ResourceKey.create(Registry.BIOME_REGISTRY, ModBiomes.MEGA_APPLE_ORCHARD_BIOME.getId());
            BiomeDictionary.addTypes(mega_apple_orchard, Type.OVERWORLD, Type.FOREST, Type.DENSE, Type.RARE);
            BiomeManager.addBiome(BiomeManager.BiomeType.COOL, new BiomeManager.BiomeEntry(mega_apple_orchard, 1));
        }
    }
    
    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.FORGE)
    public static class ForgeEvents {
        
        @SubscribeEvent
	public static void onItemUsed(BlockEvent.BreakEvent event) {
//            if (state.getValue(LEAVES))
//                level.setBlock(pos, state.setValue(LEAVES, false), UPDATE_ALL);
        }
        
        @SubscribeEvent
	public static void onItemUsed(LivingEntityUseItemEvent.Finish event) {
		if (!(event.getEntity() instanceof Player)) return;
                if (!ItemTags.getAllTags().getTag(new ResourceLocation("rusticregen", "crops/apple")).contains(event.getItem().getItem())) return;
		if (Configs.SERVER.giveAppleCores.get())
                    ((Player) event.getEntity()).addItem(new ItemStack(ModItems.APPLE_CORE.get()));
        }
        
        @SubscribeEvent
	public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
            Item item = event.getItemStack().getItem();
            if (!(item instanceof BlockItem && ((BlockItem) item).getBlock() instanceof LeavesBlock)) return;
            BlockPos pos = event.getPos();
            BlockState blockState = event.getWorld().getBlockState(pos);
            
            if (!(blockState.getBlock() instanceof FenceBlock)) return;
            
            BlockState newBlockState = ModBlocks.LEAF_COVERED_BLOCK.get().defaultBlockState();
            event.getWorld().setBlock(pos, newBlockState, UPDATE_ALL);
            
            LeavesBlock leavesBlock = (LeavesBlock) ((BlockItem) item).getBlock();
            LeafCoveredBlockEntity blockEntity = ((LeafCoveredBlock) ModBlocks.LEAF_COVERED_BLOCK.get()).getBlockEntity(event.getWorld(), pos);
            blockEntity.setLeafState(leavesBlock.defaultBlockState());
            blockEntity.setInternalBlockState(blockState);
            event.setCanceled(true);
        }
        
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
                    event.getGeneration().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModFeatures.APPLE_TREES_001);
            }
            
            boolean addGiants = Configs.COMMON.addGiantTrees.get() && Configs.COMMON.addGiantTreesInNonGiantBiomes.get();
            if (category == BiomeCategory.FOREST || category == BiomeCategory.TAIGA) {
                if (name.contains("birch")) {
                    //System.out.println("Birch: " + name);
                    if (addApples)
                        event.getGeneration().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModFeatures.APPLE_TREES_001);
                    if (addGiants) {
                        event.getGeneration().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModFeatures.GRAND_BIRCH_01);
                        if (name.contains("tall"))
                            event.getGeneration().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModFeatures.GRAND_BIRCH_01);
                    }
                } else if (name.contains("dark")) {
                    //System.out.println("Dark: " + name);
                    if (addGiants) {
                        if (addApples)
                            event.getGeneration().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModFeatures.MEDIUM_APPLE_TREES_02);
                        event.getGeneration().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModFeatures.GREAT_OAK_01);
                    } else if (addApples)
                        event.getGeneration().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModFeatures.APPLE_TREES_02);
                } else if (category == BiomeCategory.TAIGA || event.getClimate().temperature < 0.4 || name.contains("spruce")) {
                    //System.out.println("Spruce/Taiga: " + name);
                    if (addGiants) {
                        event.getGeneration().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModFeatures.MEGA_PINE_005);
                        event.getGeneration().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModFeatures.MEGA_SPRUCE_005);
                    }
                } else {
                    //System.out.println("Forest: " + name);
                    if (addApples)
                        event.getGeneration().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModFeatures.APPLE_TREES_02);
                    if (addGiants)
                        event.getGeneration().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModFeatures.GREAT_OAK_01);
                }
            }
        }
    }
}
