/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.rusticregen.common.blocks;

import net.angle.rusticregen.common.items.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 *
 * @author angle
 */
public class VerticalCrossedLogsBlock extends Block implements SimpleWaterloggedBlock, LeafCoveredEntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final EnumProperty<VerticalSlabType> TYPE = EnumProperty.create("type", VerticalSlabType.class);
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final BooleanProperty STAKE = BooleanProperty.create("stake");
    
    protected static final VoxelShape NORTH_AABB = getAABB(Direction.NORTH);
    protected static final VoxelShape EAST_AABB = getAABB(Direction.EAST);
    protected static final VoxelShape SOUTH_AABB = getAABB(Direction.SOUTH);
    protected static final VoxelShape WEST_AABB = getAABB(Direction.WEST);
    
    private static VoxelShape getAABB(Direction direction) {
        double min = 0;
        double max = 8;
        if(direction.getAxisDirection() == AxisDirection.POSITIVE) {
            min = 8;
            max = 16;
        }

        if(direction.getAxis() == Axis.X)
            return Block.box(min, 0, 0, max, 16, 16);
        else return Block.box(0, 0, min, 16, 16, max);
    }

    public VerticalCrossedLogsBlock() {
        super(Properties.copy(ModBlocks.CROSSED_LOGS.get()));
        this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH).setValue(LEAVES, false)
                .setValue(TYPE, VerticalSlabType.FRONT).setValue(WATERLOGGED, false).setValue(STAKE, false));
    }
    
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING, TYPE, WATERLOGGED, STAKE, LEAVES);
    }
    
    public boolean placingBack(BlockPlaceContext context) {
        Direction horizontalDirection = context.getHorizontalDirection();
        BlockPos blockpos = context.getClickedPos();
        
        switch(horizontalDirection.getAxis()) {
            case X:
                switch(horizontalDirection.getAxisDirection()) {
                    case POSITIVE:
                        return context.getClickLocation().x - (double) blockpos.getX() < 0.5D;
                    case NEGATIVE:
                        return context.getClickLocation().x - (double) blockpos.getX() > 0.5D;
                }
                break;
            case Z:
                switch(horizontalDirection.getAxisDirection()) {
                    case POSITIVE:
                        return context.getClickLocation().z - (double) blockpos.getZ() < 0.5D;
                    case NEGATIVE:
                        return context.getClickLocation().z - (double) blockpos.getZ() > 0.5D;
                }
        }
        return false;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState existingState = context.getLevel().getBlockState(context.getClickedPos());
        if (existingState.is(this)) {
            return existingState.setValue(TYPE, VerticalSlabType.DOUBLE);
        }
        
        BlockState state = this.defaultBlockState().setValue(TYPE, VerticalSlabType.FRONT);
        Direction direction = context.getClickedFace();
        
        switch(direction) {
            case NORTH:
            case SOUTH:
            case EAST:
            case WEST:
                state = state.setValue(FACING, direction.getOpposite());
                break;
            default:
                state = state.setValue(FACING, context.getHorizontalDirection());
                if (placingBack(context))
                    state = state.setValue(TYPE, VerticalSlabType.BACK);
        }
        
        if (context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER)
            state = state.setValue(WATERLOGGED, true);
        
        return state;
    }
    
    

    @Override
    public boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
        ItemStack itemstack = context.getItemInHand();
        VerticalSlabType slabtype = state.getValue(TYPE);
        Direction facing = state.getValue(FACING);
        
        if (slabtype == VerticalSlabType.DOUBLE || !itemstack.is(this.asItem()))
            return false;
        
        if (context.replacingClickedOnBlock()) {
            boolean flag = context.getClickLocation().y - (double)context.getClickedPos().getY() > 0.5D;
            Direction direction = context.getClickedFace();

            if (direction == facing || direction == facing.getOpposite())
                return true;

            if (placingBack(context))
                return slabtype == VerticalSlabType.FRONT;
            else
                return slabtype != VerticalSlabType.FRONT;
        }
        
        return true;
    }

    @Override
    public boolean useShapeForLightOcclusion(BlockState p_56395_) {
        return true;
    }
    
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        Direction facing = state.getValue(FACING);
        switch(state.getValue(TYPE)) {
            case FRONT:
                switch(facing) {
                    case NORTH:
                        return NORTH_AABB;
                    case SOUTH:
                        return SOUTH_AABB;
                    case EAST:
                        return EAST_AABB;
                    case WEST:
                        return WEST_AABB;
                }
            case BACK:
                switch(facing) {
                    case NORTH:
                        return SOUTH_AABB;
                    case SOUTH:
                        return NORTH_AABB;
                    case EAST:
                        return WEST_AABB;
                    case WEST:
                        return EAST_AABB;
                }
        }
        return Shapes.block();
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
                level.setBlock(pos, state.setValue(WATERLOGGED, true), UPDATE_ALL);
                level.getLiquidTicks().scheduleTick(pos, fluid.getType(), fluid.getType().getTickDelay(level));
            }
            return true;
        } else
            return false;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
        ItemStack itemInHand = player.getItemInHand(hand);
        if (!state.getValue(STAKE) && itemInHand.getItem() == ModItems.STAKE.get()) {
            level.setBlock(pos, state.setValue(STAKE, true), UPDATE_CLIENTS);
            if (!player.isCreative())
                itemInHand.shrink(1);
            return InteractionResult.SUCCESS;
        } else
            return LeafCoveredEntityBlock.super.use(state, level, pos, itemInHand, player, hand, result);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState updateState, LevelAccessor level, BlockPos pos, BlockPos updatePos) {
        if (state.getValue(WATERLOGGED))
            level.getLiquidTicks().scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));

        return super.updateShape(state, direction, updateState, level, pos, updatePos);
    }

    @Override
   public boolean isPathfindable(BlockState state, BlockGetter getter, BlockPos pos, PathComputationType pathType) {
        switch(pathType) {
            case LAND:
               return false;
            case WATER:
               return getter.getFluidState(pos).is(FluidTags.WATER);
            case AIR:
               return false;
            default:
               return false;
        }
   }
    
    public enum VerticalSlabType implements StringRepresentable {
        FRONT("front"),
        BACK("back"),
        DOUBLE("double");

        private final String name;

        private VerticalSlabType(String p_61775_) {
           this.name = p_61775_;
        }

        @Override
        public String toString() {
           return this.name;
        }

        @Override
        public String getSerializedName() {
           return this.name;
        }
     }
}
