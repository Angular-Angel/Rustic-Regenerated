/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.rusticregen.common.blocks;

import net.angle.rusticregen.common.blocks.entities.LeafCoveredBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

/**
 *
 * @author angle
 */
public class LeafCoveredBlock extends Block implements LeafCoveredEntityBlock {
    
    public LeafCoveredBlock() {
        super(Properties.copy(Blocks.OAK_LEAVES));
        this.registerDefaultState(this.defaultBlockState().setValue(LEAVES, false));
    }
    
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(LEAVES);
    }
    
    @Override
    public LeafCoveredBlockEntity getBlockEntity(BlockAndTintGetter level, BlockPos pos) {
        return (LeafCoveredBlockEntity) level.getBlockEntity(pos);
    }
    
    @Override
    public LeafCoveredBlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new LeafCoveredBlockEntity(pos, state);
    }
    
}
