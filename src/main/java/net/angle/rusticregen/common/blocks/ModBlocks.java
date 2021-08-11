/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.rusticregen.common.blocks;

import net.angle.rusticregen.client.ClientEventRegistry;
import net.angle.rusticregen.common.blocks.entities.LeafCoveredEntity;
import net.angle.rusticregen.core.RusticRegenerated;
import static net.angle.rusticregen.core.RusticRegenerated.MODID;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

/**
 *
 * @author angle
 */
public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, RusticRegenerated.MODID);
    
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, MODID);

    public static final RegistryObject<Block> APPLE_LEAVES = BLOCKS.register("apple_leaves", () -> ClientEventRegistry.registerLeafBlock(new AppleLeavesBlock()));
    
    public static final RegistryObject<Block> APPLE_SAPLING = BLOCKS.register("apple_sapling", () -> new AppleSaplingBlock());

    public static final RegistryObject<Block> APPLE_SEEDS = BLOCKS.register("apple_seeds", () -> new AppleSeedsBlock());
    
    public static final RegistryObject<Block> CROSSED_LOGS = BLOCKS.register("crossed_logs", () -> new CrossedLogsBlock());
    
    public static final RegistryObject<Block> VERTICAL_CROSSED_LOGS = BLOCKS.register("vertical_crossed_logs", () -> new VerticalCrossedLogsBlock());
    
    public static final RegistryObject<Block> STAKE = BLOCKS.register("stake", () -> new StakeBlock());
    
    
    public static final RegistryObject<BlockEntityType<LeafCoveredEntity>> CROSSED_LOGS_ENTITY_TYPE = 
            BLOCK_ENTITIES.register("crossed_logs", () -> BlockEntityType.Builder.of(LeafCoveredEntity::new, ModBlocks.CROSSED_LOGS.get()).build(null));
}
