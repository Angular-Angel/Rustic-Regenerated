/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.rusticregen.core;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

/**
 *
 * @author angle
 */
@EventBusSubscriber(modid = RusticRegenerated.MODID)
public class FuelHandler {

    private static Map<Item, Integer> fuelMap = new HashMap<>();

    public static void addFuel(Item item, int fuel) {
        fuelMap.put(item, fuel);
    }

    @SubscribeEvent
    public static void getFuel(FurnaceFuelBurnTimeEvent event) {
        Item item = event.getItemStack().getItem();
        if(fuelMap.containsKey(item))
            event.setBurnTime(fuelMap.get(item));
    }

}