/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.rusticregen.common.blocks;

import net.angle.rusticregen.common.blocks.entities.LeafCoveredBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

/**
 *
 * @author angle
 */
public class LeafCoveredBlock extends LeavesBlock implements LeafCoveredEntityBlock {
    
    public LeafCoveredBlock() {
        super(Properties.copy(Blocks.OAK_LEAVES));
        this.registerDefaultState(this.defaultBlockState().setValue(PERSISTENT, true));
    }
    
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
    }
    
    @Override
    public LeafCoveredBlockEntity getBlockEntity(BlockGetter level, BlockPos pos) {
        return (LeafCoveredBlockEntity) level.getBlockEntity(pos);
    }
    
    @Override
    public LeafCoveredBlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new LeafCoveredBlockEntity(pos, state);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighbourState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        getBlockEntity(level, pos).updateInternalBlockState(direction, neighbourState, level, pos, neighborPos);
        return super.updateShape(state, direction, state, level, pos, pos);
    }

    @Override
    public void onRemove(BlockState oldState, Level level, BlockPos pos, BlockState newState, boolean bool) {
        popResource(level, pos, new ItemStack(getBlockEntity(level, pos).getInternalBlockState().getBlock().asItem()));
        super.onRemove(oldState, level, pos, newState, bool); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public float getDestroyProgress(BlockState state, Player player, BlockGetter getter, BlockPos pos) {
        BlockState internalBlockState = getBlockEntity(getter, pos).getInternalBlockState();
        return internalBlockState.getBlock().getDestroyProgress(internalBlockState, player, getter, pos); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SoundType getSoundType(BlockState state, LevelReader world, BlockPos pos, Entity entity) {
        BlockState internalBlockState = getBlockEntity(world, pos).getInternalBlockState();
        return internalBlockState.getBlock().getSoundType(internalBlockState, world, pos, entity); //To change body of generated methods, choose Tools | Templates.
    }
}
