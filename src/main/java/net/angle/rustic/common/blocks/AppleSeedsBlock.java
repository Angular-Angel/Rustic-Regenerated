/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.rustic.common.blocks;

import java.util.Random;
import net.angle.rustic.core.Rustic;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraftforge.common.ForgeHooks;

/**
 *
 * @author angle
 */
public class AppleSeedsBlock extends BushBlock implements BonemealableBlock, AppleGrowthExporter {
    
    public static final IntegerProperty AGE = BlockStateProperties.AGE_1;

    public AppleSeedsBlock() {
        super(Properties.copy(Blocks.FERN));
        this.registerDefaultState(
            stateDefinition.any()
                .setValue(AGE, 0)
        );
    }
    
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(AGE);
    }
    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return true;
    }

    public int getMaxAge() {
        return 1;
    }
    
    protected static float getGrowthChance() {
        return 0.15f;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, Random rand) {
        super.randomTick(state, level, pos, rand);
        
        if (level.getMaxLocalRawBrightness(pos.above()) >= 9) {
            int i = state.getValue(AGE);
            //Not actually sure if I should be directly calling forge hooks here, 
            //but this is copied more or less verbatim from rustic for 1.12, and I don't know how else to do it.
            if (ForgeHooks.onCropsGrowPre(level, pos, state,
                        rand.nextFloat() <= getGrowthChance())) {
                if (i < this.getMaxAge()) {
                    level.setBlock(pos, state.setValue(AGE, 1), 2);
                } else {
                    if (!exportGrowth(state, level, pos, 1))
                        level.setBlock(pos, Rustic.APPLE_SAPLING_BLOCK.get().defaultBlockState(), 3);
                }
                net.minecraftforge.common.ForgeHooks.onCropsGrowPost(level, pos, state);
            }
        }
    }

    @Override
    public boolean isValidBonemealTarget(BlockGetter p_50897_, BlockPos p_50898_, BlockState p_50899_, boolean p_50900_) {
        return true;
    }

    @Override
    public boolean isBonemealSuccess(Level p_50901_, Random rand, BlockPos p_50903_, BlockState p_50904_) {
        return rand.nextFloat() <= 0.8f;
    }

    @Override
    public void performBonemeal(ServerLevel level, Random random, BlockPos pos, BlockState state) {
        if (state.getValue(AGE) == 0 && random.nextFloat() < 0.25) {
            level.setBlock(pos, state.setValue(AGE, 1), 2);
        } else {
            level.setBlock(pos, Rustic.APPLE_SAPLING_BLOCK.get().defaultBlockState(), 3);
        }
    }
    
}
