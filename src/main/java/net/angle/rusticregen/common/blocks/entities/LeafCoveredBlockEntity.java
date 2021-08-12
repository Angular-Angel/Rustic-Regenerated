/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.rusticregen.common.blocks.entities;

import net.angle.rusticregen.client.LeafModelLoader;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
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
}
