/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.rustic.common.grower;

import java.util.Random;
import net.angle.rustic.core.Rustic;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;

/**
 *
 * @author angle
 */
public class NormalAppleTreeGrower extends AbstractTreeGrower {

    @Override
    protected ConfiguredFeature<TreeConfiguration, ?> getConfiguredFeature(Random random, boolean bees) {
        switch(random.nextInt(10)) {
            case 0:
                return bees ? Rustic.FANCY_APPLE_BEES_005 : Rustic.FANCY_APPLE_TREE;
            default:
                return bees ? Rustic.APPLE_BEES_005 : Rustic.APPLE_TREE;
        }
    }
    
}
