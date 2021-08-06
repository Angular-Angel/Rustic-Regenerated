/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.rusticregen.common.blocks.entities;

import net.angle.rusticregen.client.LeafModelLoader;
import net.angle.rusticregen.core.RusticRegenerated;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraft.nbt.NbtUtils;

/**
 *
 * @author angle
 */
public class LeafCoveredEntity extends BlockEntity {
    
    private BlockState leafState = null;
    
    public LeafCoveredEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(RusticRegenerated.CROSSED_LOGS_ENTITY_TYPE.get(), p_155229_, p_155230_);
    }

    /**
     * @return the leafState
     */
    public BlockState getLeafState() {
        return leafState;
    }

    /**
     * @param leafState the leafState to set
     */
    public void setLeafState(BlockState leafState) {
        this.leafState = leafState;
        requestModelDataUpdate();
    }

    @Override
    public IModelData getModelData() {
        return new LeafModelLoader.LeafCoveredModelData(getLeafState());
    }
    
    public CompoundTag writeTag(CompoundTag tag) {
        if (leafState != null) tag.put("leaf_state", NbtUtils.writeBlockState(leafState));
        return tag;
    }
    
    public void readTag(CompoundTag tag) {
        setLeafState(NbtUtils.readBlockState(tag.getCompound("leaf_state")));
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
