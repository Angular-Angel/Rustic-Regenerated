/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.rusticregen.common.blocks;

import static net.angle.rusticregen.common.blocks.CrossedLogsBlock.LEAVES;
import net.angle.rusticregen.common.blocks.entities.LeafCoveredEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import static net.minecraft.world.level.block.Block.UPDATE_ALL;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;

/**
 *
 * @author angle
 */
public interface LeafCoveredEntityBlock extends EntityBlock {
    
    public static final BooleanProperty LEAVES = BooleanProperty.create("leaves");
    
    public default void removeLeaves(Level level, BlockPos pos) {
        level.setBlock(pos, level.getBlockState(pos).setValue(LEAVES, false), UPDATE_ALL);
        getBlockEntity(level, pos).setLeafState(null);
    }
    
    public default boolean isShears(Item item) {
        return ItemTags.getAllTags().getTag(new ResourceLocation("forge", "shears")).contains(item);
    }
    
    public default boolean canBeShearedByItem(BlockState state, Item item) {
        return state.getValue(LEAVES) && isShears(item);
    }
    
    public default void shearLeaves(Level level, BlockPos pos, ItemStack itemInHand, Player player, InteractionHand hand) {
        Block.popResource(level, pos, new ItemStack(getBlockEntity(level, pos).getLeafState().getBlock().asItem()));
        removeLeaves(level, pos);
        if (!player.isCreative())
            itemInHand.hurtAndBreak(1, player, (p_150686_) -> {
               p_150686_.broadcastBreakEvent(hand);
            });
    }
    
    public default boolean isLeavesItem(Item item) {
        return (item instanceof BlockItem) && ((BlockItem) item).getBlock() instanceof LeavesBlock;
    }
    
    public default LeavesBlock getLeavesFromItem(Item item) {
       return (LeavesBlock) ((BlockItem) item).getBlock();
    }
    
    public default boolean canAcceptLeavesFromItem(BlockState state, Item item) {
        return !state.getValue(LEAVES) && isLeavesItem(item);
    }
    
    public default void setLeaves(BlockState state, Level level, BlockPos pos, ItemStack itemInHand, Player player, InteractionHand hand, BlockHitResult result) {
        Item item = itemInHand.getItem();
        getBlockEntity(level, pos).setLeafState(getLeavesFromItem(item).getStateForPlacement(new BlockPlaceContext(level, player, hand, itemInHand, result)));
        level.setBlock(pos, state.setValue(LEAVES, true), UPDATE_ALL);
        if (!player.isCreative())
            itemInHand.shrink(1);
    }
    
    public default InteractionResult use(BlockState state, Level level, BlockPos pos, ItemStack itemInHand, Player player, InteractionHand hand, BlockHitResult result) {
        if (canAcceptLeavesFromItem(state, itemInHand.getItem())) {
            setLeaves(state, level, pos, itemInHand, player, hand, result);
            return InteractionResult.SUCCESS;
        } else if (canBeShearedByItem(state, itemInHand.getItem())) {
            shearLeaves(level, pos, itemInHand, player, hand);
            return InteractionResult.SUCCESS;
        } else
            return InteractionResult.FAIL;
    }
    
    public default LeafCoveredEntity getBlockEntity(BlockAndTintGetter level, BlockPos pos) {
        return (LeafCoveredEntity) level.getBlockEntity(pos);
    }
    
    @Override
    public default LeafCoveredEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new LeafCoveredEntity(pos, state);
    }
}
