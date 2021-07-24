/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.rustic.common.blocks.plants;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

/**
 *
 * @author angle
 */

public class AppleLeavesBlock extends LeavesBlock {
    
    public static final IntegerProperty AGE = BlockStateProperties.AGE_3;
    
    public AppleLeavesBlock() {
        super(AppleLeavesBlock.Properties.copy(Blocks.OAK_LEAVES));
//        this.registerDefaultState(
//            this.stateDefinition.any()
//                .setValue(AGE, 0)
//                .setValue(DISTANCE, 0)
//                .setValue(PERSISTENT, false)
//        );
    }
    
//    @Override
//    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
//        builder.add(AGE, DISTANCE, PERSISTENT);
//    }
    
}
