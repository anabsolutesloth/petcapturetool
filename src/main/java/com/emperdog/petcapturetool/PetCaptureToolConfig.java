package com.emperdog.petcapturetool;

import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

import java.util.*;

@Mod.EventBusSubscriber(modid = PetCaptureToolMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class PetCaptureToolConfig {

    private static final Map<EntityType<?>, List<String>> navigationOverrides = new HashMap<>();

    public static boolean foxesAllowed;


    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec.ConfigValue<List<?>> NAV_OVERRIDES = BUILDER
            .comment("Entries in this list define Navigation Overrides for these entities.")
            .comment("Nav Overrides will be used to navigate to NBT tags in the data of the Entity to find its Owner UUID.")
            .comment("Examples: \"minecraft:wolf=Owner\", \"somemod:unconventionally_owned_mob=weirdTag->Owner\"")
            .define("nbtNavigationOverrides", List.of(), p -> true);

    public static final ForgeConfigSpec.BooleanValue FOXES_ALLOWED = BUILDER
            .comment("If the Portable Pet Sphere can capture Foxes that Trust the player")
            .define("foxesAllowed", true);

    static final ForgeConfigSpec SPEC = BUILDER.build();

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
        navigationOverrides.clear();
        NAV_OVERRIDES.get().forEach(o -> {
            if(!(o instanceof String entry)) {
                PetCaptureToolMod.LOGGER.warn("");
                return;
            }
            String[] split = entry.split("=");
            Optional<EntityType<?>> entityOpt = EntityType.byString(split[0]);
            String[] path = split[1].split("->");
            PetCaptureToolMod.LOGGER.info("loading nav override for {}. path: {}", split[0], split[1]);

            entityOpt.ifPresentOrElse(entityType -> navigationOverrides.put(entityType, Arrays.asList(path)),
                    () -> PetCaptureToolMod.LOGGER.error("Found Unknown Entity Type \"{}\" while loading Navigation Overrides.", split[0]));
        });

        foxesAllowed = FOXES_ALLOWED.get();
    }

    public static List<String> getNavOverride(EntityType<?> entityType) {
        return navigationOverrides.get(entityType);
    }

    public static boolean hasNavOverride(EntityType<?> entityType) {
        return navigationOverrides.containsKey(entityType);
    }
}
