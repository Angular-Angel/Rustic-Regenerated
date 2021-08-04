/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.rusticregen.common.blocks;

import net.angle.rusticregen.common.blocks.entities.CrossedLogsEntity;
import net.angle.rusticregen.core.RusticRegenerated;
import net.minecraft.client.renderer.blockentity.CampfireRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.SlabBlock;
import static net.minecraft.world.level.block.SlabBlock.TYPE;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.CampfireBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.ToolType;

/**
 *
 * @author angle
 */
public class CrossedLogsBlock extends SlabBlock implements EntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty STAKE = BooleanProperty.create("stake");
    public static final BooleanProperty LEAVES = BooleanProperty.create("leaves");

    public CrossedLogsBlock() {
        super(Properties.of(Material.WOOD).noOcclusion().strength(2).harvestTool(ToolType.AXE));
        this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH).setValue(STAKE, false).setValue(LEAVES, false));
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

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = super.getStateForPlacement(context);
        if (state == null) return state;
        if (state.getValue(TYPE) != SlabType.DOUBLE)
            state = state.setValue(FACING, context.getHorizontalDirection());
        if (context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER)
            state = state.setValue(WATERLOGGED, true);
        return state;
    }
    
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING, STAKE, LEAVES);
    }
    
    public boolean isLeavesItem(Item item) {
        return (item instanceof BlockItem) && ((BlockItem) item).getBlock() instanceof LeavesBlock;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
        ItemStack itemInHand = player.getItemInHand(hand);
        Item item = itemInHand.getItem();
        if (item == null) return InteractionResult.FAIL;
        if (!state.getValue(STAKE) && item == RusticRegenerated.STAKE_ITEM.get()) {
            level.setBlock(pos, state.setValue(STAKE, true), 2);
            if (!player.isCreative())
                itemInHand.shrink(1);
            return InteractionResult.SUCCESS;
        } else if (!state.getValue(LEAVES) && isLeavesItem(item)) {
            level.setBlock(pos, state.setValue(LEAVES, true), 2);
            if (!player.isCreative())
                itemInHand.shrink(1);
            return InteractionResult.SUCCESS;
        } else
            return InteractionResult.FAIL;
    }
    
    @Override
    public CrossedLogsEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CrossedLogsEntity(pos, state);
    }
}
