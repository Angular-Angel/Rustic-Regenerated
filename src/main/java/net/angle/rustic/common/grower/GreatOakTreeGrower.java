/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.rustic.common.grower;

import java.util.Random;
import net.angle.rustic.core.Rustic;
import net.minecraft.data.worldgen.Features;
import net.minecraft.world.level.block.grower.AbstractMegaTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;

/**
 *
 * @author angle
 */
public class GreatOakTreeGrower extends AbstractMegaTreeGrower {

    @Override
    protected ConfiguredFeature<TreeConfiguration, ?> getConfiguredMegaFeature(Random random) {
        return Rustic.GREAT_OAK;
    }

    @Override
    protected ConfiguredFeature<TreeConfiguration, ?> getConfiguredFeature(Random random, boolean bees) {
        if (random.nextInt(10) == 0) {
            return bees ? Features.FANCY_OAK_BEES_005 : Features.FANCY_OAK;
        } else {
            return bees ? Features.OAK_BEES_005 : Features.OAK;
        }
    }
    
}
