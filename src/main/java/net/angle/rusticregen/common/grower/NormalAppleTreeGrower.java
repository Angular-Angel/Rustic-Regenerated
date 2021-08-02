/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.rusticregen.common.grower;

import java.util.Random;
import net.angle.rusticregen.core.RusticRegenerated;
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
                return bees ? RusticRegenerated.FANCY_APPLE_BEES_005 : RusticRegenerated.FANCY_APPLE_TREE;
            default:
                return bees ? RusticRegenerated.APPLE_BEES_005 : RusticRegenerated.APPLE_TREE;
        }
    }
    
}
