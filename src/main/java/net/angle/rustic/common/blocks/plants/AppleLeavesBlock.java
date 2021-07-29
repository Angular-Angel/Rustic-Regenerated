/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.rustic.common.blocks.plants;

import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
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
        this.registerDefaultState(
            stateDefinition.any()
                .setValue(AGE, 0)
        );
    }

    public int getMaxAge() {
        return 3;
    }
    
//    protected static boolean isAirAdjacent(World world, BlockPos pos, IBlockState state) {
//        if (world.isAirBlock(pos.below()) || world.isAirBlock(pos.north()) || world.isAirBlock(pos.south())
//                || world.isAirBlock(pos.west()) || world.isAirBlock(pos.east())) {
//            return true;
//        }
//        return false;
//    }

    @Override
    public void randomTick(BlockState state, ServerLevel server, BlockPos pos, Random rand) {
            super.randomTick(state, server, pos, rand);

            int i = state.getValue(AGE);

//            if (i < getMaxAge() && isAirAdjacent(worldIn, pos, state)) {
//                    float f = getGrowthChance(this, worldIn, pos);
//
//                    if (net.minecraftforge.common.ForgeHooks.onCropsGrowPre(worldIn, pos, state,
//                                    rand.nextInt((int) (50.0F / f) + 1) == 0)) {
//                            worldIn.setBlockState(pos, state.withProperty(AGE, (i + 1)), 2);
//                            net.minecraftforge.common.ForgeHooks.onCropsGrowPost(worldIn, pos, state, worldIn.getBlockState(pos));
//                    }
//            }
    }
    
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(AGE);
    }
    
}
