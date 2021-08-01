/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.rustic.core;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

/**
 *
 * @author angle
 */
public class Configs {
    public static final ServerConfig SERVER;
    public static final ForgeConfigSpec SERVER_SPECIFICATION;
    public static final CommonConfig COMMON;
    public static final ForgeConfigSpec COMMON_SPECIFICATION;

    static {
        Pair<ServerConfig, ForgeConfigSpec> serverSpecificationPair = new ForgeConfigSpec.Builder().configure(ServerConfig::new);
        SERVER_SPECIFICATION = serverSpecificationPair.getRight();
        SERVER = serverSpecificationPair.getLeft();
        Pair<CommonConfig, ForgeConfigSpec> commonSpecificationPair = new ForgeConfigSpec.Builder().configure(CommonConfig::new);
        COMMON_SPECIFICATION = commonSpecificationPair.getRight();
        COMMON = commonSpecificationPair.getLeft();
    }

    public static class CommonConfig {
        public final ForgeConfigSpec.BooleanValue modifyBiomes;
        public final ForgeConfigSpec.BooleanValue addGiantTrees;

        CommonConfig(ForgeConfigSpec.Builder builder) {
            modifyBiomes = builder.comment("If Rustic should modify existing biomes.")
                .define("modifyBiomes", true);
            
            addGiantTrees = builder.comment("If Rustic should add giant versions of its trees, and of vanilla oaks and birches.")
                .define("addGiantTrees", true);
        }
    }

    public static class ServerConfig {
        public final ForgeConfigSpec.DoubleValue appleMaturationChance;

        ServerConfig(ForgeConfigSpec.Builder builder) {

            appleMaturationChance = builder.comment("The chance of an apple leaf block maturing one stage each time it gets updated.")
                .defineInRange("appleMaturationChance", 0.10d, 0, Float.MAX_VALUE);
        }
    }
}
