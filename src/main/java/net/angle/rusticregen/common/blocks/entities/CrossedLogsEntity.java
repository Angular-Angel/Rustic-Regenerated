/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.rusticregen.common.blocks.entities;

import com.mojang.blaze3d.vertex.PoseStack;
import net.angle.rusticregen.core.RusticRegenerated;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/**
 *
 * @author angle
 */
public class CrossedLogsEntity extends BlockEntity {
    
    private BlockState leafState = null;
    
    public CrossedLogsEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(RusticRegenerated.CROSSED_LOGS_ENTITY_TYPE.get(), p_155229_, p_155230_);
    }
    
    public static class CrossedLogsRenderer implements BlockEntityRenderer<CrossedLogsEntity> {

        @Override
        public void render(CrossedLogsEntity entity, float partialTicks, PoseStack pose, MultiBufferSource source, int combinedLightIn, int p_112312_) {
            
        }
    
    }
    
}
