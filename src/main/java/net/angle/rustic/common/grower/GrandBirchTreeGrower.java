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
public class GrandBirchTreeGrower extends AbstractMegaTreeGrower {

    @Override
    protected ConfiguredFeature<TreeConfiguration, ?> getConfiguredMegaFeature(Random random) {
        return Rustic.GRAND_BIRCH;
    }

    @Override
    protected ConfiguredFeature<TreeConfiguration, ?> getConfiguredFeature(Random random, boolean bees) {
        return bees ? Features.BIRCH_BEES_005 : Features.BIRCH;
    }
    
}
