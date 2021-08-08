/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.rusticregen.common.biomes;

import static net.angle.rusticregen.core.RusticRegenerated.MODID;
import net.minecraft.data.worldgen.biome.VanillaBiomes;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

/**
 *
 * @author angle
 */
public class ModBiomes {
    
    public static final DeferredRegister<Biome> BIOMES = DeferredRegister.create(ForgeRegistries.BIOMES, MODID);
    
    public static final RegistryObject<Biome> GREAT_OAK_FOREST_BIOME = BIOMES.register("great_oak_forest", () -> {
        return VanillaBiomes.theVoidBiome();
    });
    
    public static final RegistryObject<Biome> GRAND_BIRCH_FOREST_BIOME = BIOMES.register("grand_birch_forest", () -> {
        return VanillaBiomes.theVoidBiome();
    });
    
    public static final RegistryObject<Biome> APPLE_ORCHARD_BIOME = BIOMES.register("apple_orchard", () -> {
        return VanillaBiomes.theVoidBiome();
    });
    
    public static final RegistryObject<Biome> MEDIUM_APPLE_ORCHARD_BIOME = BIOMES.register("medium_apple_orchard", () -> {
        return VanillaBiomes.theVoidBiome();
    });
    
    public static final RegistryObject<Biome> MEGA_APPLE_ORCHARD_BIOME = BIOMES.register("mega_apple_orchard", () -> {
        return VanillaBiomes.theVoidBiome();
    });
    
}
