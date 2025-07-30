package com.emperdog.petcapturetool.item;

import com.emperdog.petcapturetool.PetCaptureToolMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class PetCaptureItems {

    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, PetCaptureToolMod.MODID);

    public static final RegistryObject<Item> PET_CAPTURE_TOOL = ITEMS.register("capture_tool", PetCaptureToolItem::new);


    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }
}
