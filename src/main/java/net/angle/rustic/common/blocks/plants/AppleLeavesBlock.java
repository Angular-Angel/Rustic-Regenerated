/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.rustic.common.blocks.plants;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

/**
 *
 * @author angle
 */

public class AppleLeavesBlock extends LeavesBlock {
    
    public AppleLeavesBlock() {
        super(AppleLeavesBlock.Properties.copy(Blocks.OAK_LEAVES));
//        this.registerDefaultState(
//            this.stateDefinition.any()
//                .setValue(BlockStateProperties.AGE_3, 0)
//                .setValue(DISTANCE, 0)
//                .setValue(PERSISTENT, false)
//        );
    }
    
}
