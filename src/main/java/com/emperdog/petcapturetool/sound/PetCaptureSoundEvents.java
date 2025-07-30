package com.emperdog.petcapturetool.sound;

import com.emperdog.petcapturetool.PetCaptureToolMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class PetCaptureSoundEvents {

    private static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(Registries.SOUND_EVENT, PetCaptureToolMod.MODID);


    public static final RegistryObject<SoundEvent> PICK_UP = register("item.petcapturetool.pick_up");

    public static final RegistryObject<SoundEvent> PLACE_DOWN = register("item.petcapturetool.release");


    private static RegistryObject<SoundEvent> register(String name) {
        return SOUNDS.register(name, () -> SoundEvent.createVariableRangeEvent(PetCaptureToolMod.resource(name)));
    }

    public static void register(IEventBus bus) {
        SOUNDS.register(bus);
    }
}
