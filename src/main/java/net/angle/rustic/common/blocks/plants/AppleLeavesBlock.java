/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.rustic.common.blocks.plants;

import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.ForgeHooks;

/**
 *
 * @author angle
 */

public class AppleLeavesBlock extends LeavesBlock implements BonemealableBlock {
    
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
    
    protected static boolean isAirAdjacent(BlockGetter bg, BlockPos pos) {
        if (bg.getBlockState(pos.above()).isAir() || bg.getBlockState(pos.below()).isAir() || bg.getBlockState(pos.north()).isAir() || bg.getBlockState(pos.south()).isAir()
                || bg.getBlockState(pos.east()).isAir() || bg.getBlockState(pos.north()).isAir()) {
            return true;
        }
        return false;
    }
    
    public boolean canGrow(BlockGetter getter, BlockPos pos, BlockState state) {
            return state.getValue(AGE) < getMaxAge() && isAirAdjacent(getter, pos);
    }
    
    protected static float getGrowthChance() {
        return 1F;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, Random rand) {
        super.randomTick(state, level, pos, rand);
        System.out.println("?");
        if (canGrow(level, pos, state)) {
            float f = getGrowthChance();
            System.out.println("??????????????");

            if (ForgeHooks.onCropsGrowPre(level, pos, state,
                        rand.nextInt((int) (50.0F / f) + 1) == 0)) {
                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                level.setBlock(pos, state.setValue(AGE, (state.getValue(AGE) + 1)), 2);
                net.minecraftforge.common.ForgeHooks.onCropsGrowPost(level, pos, state);
            }
        }
    }
    
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(AGE);
    }

    @Override
    public boolean isValidBonemealTarget(BlockGetter getter, BlockPos pos, BlockState state, boolean isClient) {
        return canGrow(getter, pos, state);
    }
    
    @Override
    public boolean isBonemealSuccess(Level level, Random random, BlockPos bp, BlockState bs) {
        return true;
    }
    
    protected int getBonemealAgeIncrease(Random random) {
        return 1 + random.nextInt(3);
    }
    
    @Override
    public void performBonemeal(ServerLevel level, Random random, BlockPos pos, BlockState state) {
        int i = state.getValue(AGE) + this.getBonemealAgeIncrease(random);
        int j = this.getMaxAge();
        if (i > j) {
                i = j;
        }
        level.setBlock(pos, state.setValue(AGE, i), 2);
    }
    
    protected void dropApple(Level level, BlockPos pos, BlockState state) {
        if (state.getValue(AGE) == getMaxAge()) {
            popResource(level, pos, new ItemStack(Items.APPLE));
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
        if (state.getValue(AGE) < this.getMaxAge())
            return InteractionResult.FAIL;
        
        dropApple(level, pos, state);
        
        level.setBlock(pos, state.setValue(AGE, 0), 2);
        return InteractionResult.SUCCESS;
    }
    
    
    
    
}
