/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.rusticregen.common.blocks.entities;

import net.angle.rusticregen.client.LeafModelLoader;
import net.angle.rusticregen.core.RusticRegenerated;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.IModelData;

/**
 *
 * @author angle
 */
public class CrossedLogsEntity extends BlockEntity {
    
    private BlockState leafState = null;
    
    public CrossedLogsEntity(BlockPos p_155229_, BlockState p_155230_) {
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
    }

    @Override
    public IModelData getModelData() {
        return new LeafModelLoader.LeafCoveredModelData(getLeafState());
    }
    
    
    
}
