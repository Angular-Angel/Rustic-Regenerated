/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.rusticregen.common.blocks;

import static net.angle.rusticregen.common.blocks.LeafCoveredEntityBlock.LEAVES;
import net.angle.rusticregen.common.items.ModItems;
import net.angle.rusticregen.core.RusticRegenerated;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.Shapes;

/**
 *
 * @author angle
 */
public class StakeBlock extends RotatedPillarBlock implements SimpleWaterloggedBlock, LeafCoveredEntityBlock {
   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
   
    protected static final VoxelShape STAKE_AABB_X = Block.box(0.0D, 6.0D, 6.0F, 16.0D, 10.0D, 10.0D);
    protected static final VoxelShape STAKE_AABB_Y = Block.box(6.0D, 0.0F, 6.0D, 10.0D, 16.0D, 10.0D);
    protected static final VoxelShape STAKE_AABB_Z = Block.box(6.0D, 6.0D, 0.0F, 10.0D, 10.0D, 16.0D);
    
    public StakeBlock() {
        super(Properties.copy(ModBlocks.CROSSED_LOGS.get()));
        this.registerDefaultState(this.defaultBlockState().setValue(WATERLOGGED, false).setValue(LEAVES, false));
    }
    
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(WATERLOGGED, LEAVES);
    }
    
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        if (state.getValue(LEAVES))
            return Shapes.block();
        else
            switch(state.getValue(AXIS)) { 
                case X:
                    return STAKE_AABB_X; 
                case Y:
                    return STAKE_AABB_Y; 
                case Z:
                    return STAKE_AABB_Z;
                default:
                    return STAKE_AABB_Y;
            }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = super.getStateForPlacement(context);
        if (state == null) return state;
        if (context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER)
            state = state.setValue(WATERLOGGED, true);
        return state;
    }

    @Override
   public FluidState getFluidState(BlockState state) {
      return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
   }
    
    @Override
    public boolean canPlaceLiquid(BlockGetter getter, BlockPos pos, BlockState state, Fluid fluid) {
        return !state.getValue(WATERLOGGED) && fluid == Fluids.WATER;
    }
    
    @Override
    public boolean placeLiquid(LevelAccessor level, BlockPos pos, BlockState state, FluidState fluid) {
        if (!state.getValue(WATERLOGGED) && fluid.getType() == Fluids.WATER) {
            if (!level.isClientSide()) {
                level.setBlock(pos, state.setValue(WATERLOGGED, true), 3);
                level.getLiquidTicks().scheduleTick(pos, fluid.getType(), fluid.getType().getTickDelay(level));
            }
            return true;
        } else
            return false;
    }
    
    public InteractionResult useCrossedLogs(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
        ItemStack itemInHand = player.getItemInHand(hand);
        BlockPlaceContext context = new BlockPlaceContext(level, player, hand, itemInHand, result);
        BlockState newState = ModBlocks.CROSSED_LOGS.get().getStateForPlacement(context);
        if (newState == null)
            return InteractionResult.FAIL;

        newState = newState.setValue(CrossedLogsBlock.STAKE, true);
        newState = newState.setValue(CrossedLogsBlock.WATERLOGGED, state.getValue(WATERLOGGED));
        if (context.getClickedFace() == Direction.UP)
            newState = newState.setValue(SlabBlock.TYPE, SlabType.TOP);
        else if (context.getClickedFace() == Direction.DOWN)
            newState = newState.setValue(SlabBlock.TYPE, SlabType.BOTTOM);
        level.setBlock(pos, newState, 2);
        if (!player.isCreative())
            itemInHand.shrink(1);
        return InteractionResult.SUCCESS;
    }
    
    public InteractionResult useVerticalCrossedLogs(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
        ItemStack itemInHand = player.getItemInHand(hand);
        BlockPlaceContext context = new BlockPlaceContext(level, player, hand, itemInHand, result);
        BlockState newState = ModBlocks.VERTICAL_CROSSED_LOGS.get().getStateForPlacement(context);
        if (newState == null)
            return InteractionResult.FAIL;

        newState = newState.setValue(CrossedLogsBlock.STAKE, true);
        newState = newState.setValue(CrossedLogsBlock.WATERLOGGED, state.getValue(WATERLOGGED));
        level.setBlock(pos, newState, 2);
        if (!player.isCreative())
            itemInHand.shrink(1);
        return InteractionResult.SUCCESS;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
        ItemStack itemInHand = player.getItemInHand(hand);
        if (itemInHand.getItem() == ModItems.CROSSED_LOGS.get() && state.getValue(AXIS) == Direction.Axis.Y) {
            return useCrossedLogs(state, level, pos, player, hand, result);
        } else if (itemInHand.getItem() == ModItems.VERTICAL_CROSSED_LOGS.get() && state.getValue(AXIS) != Direction.Axis.Y) {
            return useVerticalCrossedLogs(state, level, pos, player, hand, result);
        } else 
            return LeafCoveredEntityBlock.super.use(state, level, pos, itemInHand, player, hand, result);
    }
}
