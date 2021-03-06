/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.rusticregen.common.blocks;

import static net.angle.rusticregen.common.blocks.AppleSeedsBlock.AGE;
import net.angle.rusticregen.core.RusticRegenerated;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import static net.minecraft.world.level.block.Block.UPDATE_ALL;
import static net.minecraft.world.level.block.Block.UPDATE_CLIENTS;
import net.minecraft.world.level.block.state.BlockState;

/**
 *
 * @author angle
 */
public interface AppleGrowthExporter {
    
    
    public default boolean isImmatureSeeds(BlockState state) {
        return state.is(ModBlocks.APPLE_SEEDS.get()) && state.getValue(AGE) == 0;
    }
    
    public default boolean isMatureSeeds(BlockState state) {
        return state.is(ModBlocks.APPLE_SEEDS.get()) && state.getValue(AGE) == 1;
    }
    
    public default boolean exportGrowthTo(BlockState state, ServerLevel level, BlockPos pos) {
        return exportGrowth(state, level, pos, 1);
    }
    
    public default boolean exportGrowthTo(BlockState state, ServerLevel level, BlockPos pos, int limit) {
        BlockState block = level.getBlockState(pos);
        if (isImmatureSeeds(block)) {
            level.setBlock(pos, block.setValue(AGE, 1), UPDATE_CLIENTS);
            return true;
        } else if (limit > 1 && isMatureSeeds(block)) {
            level.setBlock(pos, ModBlocks.APPLE_SAPLING.get().defaultBlockState(), UPDATE_ALL);
            return true;
        } else
            return false;
    }
    
    public default boolean exportGrowth(BlockState state, ServerLevel level, BlockPos pos, int limit) {
        return exportGrowthTo(state, level, pos.north(), limit) || exportGrowthTo(state, level, pos.east(), limit) || 
               exportGrowthTo(state, level, pos.south(), limit) || exportGrowthTo(state, level, pos.west(), limit) ||
               exportGrowthTo(state, level, pos.north().east(), limit) || exportGrowthTo(state, level, pos.north().west(), limit) || 
               exportGrowthTo(state, level, pos.south().east(), limit) || exportGrowthTo(state, level, pos.south().west(), limit);
    }
}
