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
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.material.*;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.*;

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
    
    public static final VoxelShape NORTH_STAKE = Shapes.or(NORTH_AABB, StakeBlock.STAKE_AABB_Z);
    public static final VoxelShape EAST_STAKE = Shapes.or(EAST_AABB, StakeBlock.STAKE_AABB_X);
    public static final VoxelShape SOUTH_STAKE = Shapes.or(SOUTH_AABB, StakeBlock.STAKE_AABB_Z);
    public static final VoxelShape WEST_STAKE = Shapes.or(WEST_AABB, StakeBlock.STAKE_AABB_X);
    
    private static VoxelShape getAABB(Direction direction) {
        int start = 0;
        int end = 8;
        
        if(direction.getAxisDirection() == AxisDirection.POSITIVE) {
            start = 8;
            end = 16;
        }

        if(direction.getAxis() == Axis.X)
            return Block.box(start, 0, 0, end, 16, 16);
        else return Block.box(0, 0, start, 16, 16, end);
    }

    public VerticalCrossedLogsBlock() {
        super(Properties.copy(ModBlocks.CROSSED_LOGS.get()));
        this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH).setValue(LEAVES, false)
                .setValue(TYPE, VerticalSlabType.FRONT).setValue(WATERLOGGED, false).setValue(STAKE, false));
    }
    

    @Override
   public BlockState rotate(BlockState state, Rotation rotation) {
      return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
   }
    
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING, TYPE, WATERLOGGED, STAKE, LEAVES);
    }
    
    public boolean placingBack(BlockPlaceContext context) {
        return placingBack(context, context.getHorizontalDirection());
    }
    
    public boolean placingBack(BlockPlaceContext context, Direction direction) {
        BlockPos blockpos = context.getClickedPos();
        Vec3 clickLocation = context.getClickLocation();
        System.out.println(clickLocation);
        switch(direction.getAxis()) {
            case X:
                switch(direction.getAxisDirection()) {
                    case POSITIVE:
                        System.out.println("Positive X!");
                        return clickLocation.x % 1 < 0.5D;
                    case NEGATIVE:
                        System.out.println("Negative X: " + (clickLocation.x % 1 > 0.5D));
                        return clickLocation.x % 1 > 0.5D;
                }
                break;
            case Z:
                switch(direction.getAxisDirection()) {
                    case POSITIVE:
                        System.out.println("Positive Z!");
                        return clickLocation.z % 1 < 0.5D;
                    case NEGATIVE:
                        System.out.println("Negative Z!");
                        return clickLocation.z % 1 > 0.5D;
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
        if (state.getValue(LEAVES) || state.getValue(TYPE) == VerticalSlabType.DOUBLE)
            return Shapes.block();
        Direction facing = state.getValue(FACING);
        switch(state.getValue(TYPE)) {
            case FRONT:
                switch(facing) {
                    case NORTH:
                        if (state.getValue(STAKE))
                            return NORTH_STAKE;
                        else
                            return NORTH_AABB;
                    case SOUTH:
                        if (state.getValue(STAKE))
                            return SOUTH_STAKE;
                        else
                            return SOUTH_AABB;
                    case EAST:
                        if (state.getValue(STAKE))
                            return EAST_STAKE;
                        else
                            return EAST_AABB;
                    case WEST:
                        if (state.getValue(STAKE))
                            return WEST_STAKE;
                        else
                            return WEST_AABB;
                }
            case BACK:
                switch(facing) {
                    case NORTH:
                        if (state.getValue(STAKE))
                            return SOUTH_STAKE;
                        else
                            return SOUTH_AABB;
                    case SOUTH:
                        if (state.getValue(STAKE))
                            return NORTH_STAKE;
                        else
                            return NORTH_AABB;
                    case EAST:
                        if (state.getValue(STAKE))
                            return WEST_STAKE;
                        else
                            return WEST_AABB;
                    case WEST:
                        if (state.getValue(STAKE))
                            return EAST_STAKE;
                        else
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
