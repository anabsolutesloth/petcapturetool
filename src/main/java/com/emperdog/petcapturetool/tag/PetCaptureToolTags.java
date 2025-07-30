package com.emperdog.petcapturetool.tag;

import com.emperdog.petcapturetool.PetCaptureToolMod;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

public class PetCaptureToolTags {

    //Entities under this tag will NEVER be capturable
    public static final TagKey<EntityType<?>> NEVER_CAPTURE = entityTag("never_capture");

    //Entities under this tag will ALWAYS be capturable
    public static final TagKey<EntityType<?>> ALWAYS_CAPTURE = entityTag("always_capture");


    private static TagKey<EntityType<?>> entityTag(String path) {
        return  modTag(Registries.ENTITY_TYPE, path);
    }

    private static <T> TagKey<T> modTag(ResourceKey<Registry<T>> registryKey, String path) {
        return TagKey.create(registryKey, PetCaptureToolMod.resource(path));
    }
}
