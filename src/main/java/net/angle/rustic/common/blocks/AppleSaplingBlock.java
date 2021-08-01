/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.rustic.common.blocks;

import java.util.Random;
import net.angle.rustic.common.grower.MegaAppleTreeGrower;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.state.BlockState;

/**
 *
 * @author angle
 */
public class AppleSaplingBlock extends SaplingBlock implements AppleGrowthExporter {
    
    public AppleSaplingBlock() {
        super(new MegaAppleTreeGrower(), Properties.copy(Blocks.OAK_SAPLING));
    }
    
    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, Random rand) {
        if (level.getMaxLocalRawBrightness(pos.above()) >= 9 && rand.nextInt(7) == 0) {
            if (!exportGrowth(state, level, pos, 2)) {
                if (!level.isAreaLoaded(pos, 1)) return; // Forge: prevent loading unloaded chunks when checking neighbor's light
                this.advanceTree(level, pos, state, rand);
            }
        }
    }
    
}
