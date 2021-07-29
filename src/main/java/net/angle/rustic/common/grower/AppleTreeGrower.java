/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.rustic.common.grower;

import java.util.Random;
import net.minecraft.data.worldgen.Features;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;

/**
 *
 * @author angle
 */
public class AppleTreeGrower extends AbstractTreeGrower {

    @Override
    protected ConfiguredFeature<TreeConfiguration, ?> getConfiguredFeature(Random random, boolean bln) {
        switch(random.nextInt(10)) {
            case 0:
                return Features.FANCY_OAK;
            default:
                return Features.OAK;
        }
    }
    
    
    
}
