/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.rustic.common.blocks;

import java.util.List;
import java.util.Random;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
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
        super(Properties.copy(Blocks.OAK_LEAVES));
        this.registerDefaultState(
            stateDefinition.any()
                .setValue(AGE, 0)
                .setValue(DISTANCE, 1)
                .setValue(PERSISTENT, false)
        );
    }

    public int getMaxAge() {
        return 3;
    }
    
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(AGE);
    }
    
    protected static boolean isAirAdjacent(BlockGetter bg, BlockPos pos) {
        return bg.getBlockState(pos.above()).isAir() || bg.getBlockState(pos.below()).isAir() || bg.getBlockState(pos.north()).isAir() || bg.getBlockState(pos.south()).isAir()
                || bg.getBlockState(pos.east()).isAir() || bg.getBlockState(pos.north()).isAir();
    }
    
    public boolean canGrow(BlockGetter getter, BlockPos pos, BlockState state) {
            return state.getValue(AGE) < getMaxAge() && isAirAdjacent(getter, pos);
    }
    
    protected static float getGrowthChance() {
        return 0.1f;
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return true;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, Random rand) {
        super.randomTick(state, level, pos, rand);
        
        if (canGrow(level, pos, state)) {
            //Not actually sure if I should be directly calling forge hooks here, 
            //but this is copied more or less verbatim from rustic for 1.12, and I don't know how else to do it.
            if (ForgeHooks.onCropsGrowPre(level, pos, state,
                        rand.nextFloat() <= getGrowthChance())) {
                
                level.setBlock(pos, state.setValue(AGE, (state.getValue(AGE) + 1)), 2);
                net.minecraftforge.common.ForgeHooks.onCropsGrowPost(level, pos, state);
            }
        }
    }
    
    @Override
    public boolean isShearable(@Nonnull ItemStack item, Level world, BlockPos pos) {
        return true;
    }
    
    @Override
    public List<ItemStack> onSheared(@Nullable Player player, @Nonnull ItemStack item, Level world, BlockPos pos, int fortune) {
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!");
        return NonNullList.withSize(1, new ItemStack(asItem()));
    }

    @Override
    public boolean isValidBonemealTarget(BlockGetter getter, BlockPos pos, BlockState state, boolean isClient) {
        return canGrow(getter, pos, state);
    }
    
    @Override
    public boolean isBonemealSuccess(Level level, Random random, BlockPos bp, BlockState bs) {
        return random.nextFloat() <= 0.8f;
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
    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        super.playerWillDestroy(level, pos, state, player); //To change body of generated methods, choose Tools | Templates.
        dropApple(level, pos, state);
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
