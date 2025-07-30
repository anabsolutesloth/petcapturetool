package com.emperdog.petcapturetool;

import com.emperdog.petcapturetool.item.PetCaptureItems;
import com.emperdog.petcapturetool.item.PetCaptureToolItem;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;

public class PetCaptureToolClient {

    public static final ResourceLocation HAS_ENTITY_PREDICATE_ID = PetCaptureToolMod.resource("has_entity");

    public static void init() {
        registerItemProperties();
    }

    public static void registerItemProperties() {
        ItemProperties.register(PetCaptureItems.PET_CAPTURE_TOOL.get(), HAS_ENTITY_PREDICATE_ID,
                (stack, level, holder, holderid) ->
                        stack.hasTag() && stack.getTag().contains(PetCaptureToolItem.KEY_ENTITY) ? 1.0f : 0.0f);
    }
}
