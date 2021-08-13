/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.rusticregen.common.blocks.entities;

import net.angle.rusticregen.client.LeafModelLoader;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.IModelData;


public class LeafCoveredBlockEntity extends LeafCoveredEntity {
    
    private BlockState internalBlockState = null;
    
    public LeafCoveredBlockEntity(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    /**
     * @return the internalBlockState
     */
    public BlockState getInternalBlockState() {
        return internalBlockState;
    }

    /**
     * @param internalBlockState the internalBlockState to set
     */
    public void setInternalBlockState(BlockState internalBlockState) {
        this.internalBlockState = internalBlockState;
        requestModelDataUpdate();
    }
    
    public void updateInternalBlockState(Direction direction, BlockState neighbourState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (internalBlockState != null)
            setInternalBlockState(internalBlockState.getBlock().updateShape(internalBlockState, direction, neighbourState, level, pos, neighborPos));
    }
    
    @Override
    public CompoundTag writeTag(CompoundTag tag) {
        super.writeTag(tag);
        if (internalBlockState != null) tag.put("internal_block_state", NbtUtils.writeBlockState(internalBlockState));
        return tag;
    }
    
    @Override
    public void readTag(CompoundTag tag) {
        super.readTag(tag);
        setInternalBlockState(NbtUtils.readBlockState(tag.getCompound("internal_block_state")));
    }

    @Override
    public IModelData getModelData() {
        return new LeafModelLoader.LeafCoveredModelData(getLeafState(), getInternalBlockState());
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        return super.save(writeTag(tag)); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        readTag(tag);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag updateTag = super.getUpdateTag();
        return writeTag(updateTag);
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        readTag(tag);
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return new ClientboundBlockEntityDataPacket(getBlockPos(), -1, getUpdateTag());
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        super.onDataPacket(net, pkt);
        readTag(pkt.getTag());
    }
}
