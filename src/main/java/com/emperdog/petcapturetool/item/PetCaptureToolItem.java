package com.emperdog.petcapturetool.item;

import com.emperdog.petcapturetool.PetCaptureToolConfig;
import com.emperdog.petcapturetool.tag.PetCaptureToolTags;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class PetCaptureToolItem extends Item {

    public static final String KEY_ENTITY = "entity";

    public PetCaptureToolItem() {
        super(new Item.Properties().stacksTo(1).fireResistant());
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        if(entity instanceof LivingEntity livingEntity
                && stack.getOrCreateTag().getCompound(KEY_ENTITY).isEmpty()
                && canCapture(livingEntity, player)) {
            capture(livingEntity, stack);
            player.playSound(SoundEvents.BOTTLE_FILL, 1.0f, 1.0f);
        }
        return true;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        CompoundTag tag = context.getItemInHand().getOrCreateTag();
        CompoundTag entityTag = tag.getCompound(KEY_ENTITY);
        if(!entityTag.isEmpty()) {
            Player player = context.getPlayer();
            Optional<Entity> entityOpt = EntityType.create(entityTag, player.level());

            BlockPos pos = context.getClickedPos();
            Direction dir = context.getClickedFace();
            double x = pos.getX() + dir.getStepX() + 0.5;
            double y = pos.getY() + dir.getStepY();
            double z = pos.getZ() + dir.getStepZ() + 0.5;

            entityOpt.ifPresent(entity -> {
                /*
                if(entity instanceof OwnableEntity ownableEntity && ownableEntity.getOwnerUUID() != null)
                    PetCaptureToolMod.LOGGER.info("entity Owener UUID: {}, Player UUID: {}",
                            ownableEntity.getOwnerUUID().toString(), player.getUUID());
                 */
                entity.setPos(x, y, z);
                player.level().addFreshEntity(entity);
            });
            tag.remove(KEY_ENTITY);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag pIsAdvanced) {
        tooltip.add(Component.translatable("item.petcapturetool.capture_tool.desc").withStyle(ChatFormatting.YELLOW));
        if(stack.hasTag() && stack.getTag().contains(KEY_ENTITY)) {
            CompoundTag entityTag = stack.getTag().getCompound(KEY_ENTITY);
            Optional<Entity> entityOpt = EntityType.create(entityTag, level);
            entityOpt.ifPresent(entity ->
                    tooltip.add(Component.translatable("item.petcapturetool.capture_tool.contained_entity", entity.getDisplayName()).withStyle(ChatFormatting.GRAY)));
        }
    }

    public boolean canCapture(LivingEntity livingEntity, Player player) {
        EntityType<?> entityType = livingEntity.getType();
        if(entityType.is(PetCaptureToolTags.NEVER_CAPTURE)) {
            player.displayClientMessage(Component.translatable("message.petcapturetool.never_capturable", entityType.getDescription()).withStyle(ChatFormatting.RED), true);
            return false;
        }
        //PetCaptureToolMod.LOGGER.info("always_capture: {}, never_capture: {}",
        //        entityType.is(PetCaptureToolTags.ALWAYS_CAPTURE), entityType.is(PetCaptureToolTags.NEVER_CAPTURE));
        return entityType.is(PetCaptureToolTags.ALWAYS_CAPTURE)
                || isOwner(player.getUUID(), livingEntity);
    }

    @Nullable
    public UUID getOwner(LivingEntity livingEntity) {
        List<String> navOverride = PetCaptureToolConfig.getNavOverride(livingEntity.getType());
        if(navOverride != null && !navOverride.isEmpty()) {
            CompoundTag tag = livingEntity.serializeNBT();
            String nav = "";
            for (String s : navOverride) {
                nav = s;
                tag = tag.getCompound(nav);
            }
            return tag.getUUID(nav);
        } else if(livingEntity instanceof OwnableEntity ownableEntity) {
            return ownableEntity.getOwnerUUID();
        }
        return null;
    }

    public boolean isOwner(UUID ownerUUID, LivingEntity livingEntity) {
        UUID entityOwner = getOwner(livingEntity);
        if(entityOwner != null)
            return ownerUUID.equals(entityOwner);
        else if(PetCaptureToolConfig.foxesAllowed
                && livingEntity instanceof Fox fox) {
            Optional<UUID> trusted = fox.getTrustedUUIDs().stream()
                    .filter(u -> u.equals(ownerUUID))
                    .findFirst();
            return trusted.isPresent();
        }
        return false;
    }

    public void capture(LivingEntity livingEntity, ItemStack stack) {
        CompoundTag serializedEntity = livingEntity.serializeNBT();
        CompoundTag itemTag = stack.getOrCreateTag();
        itemTag.put(KEY_ENTITY, serializedEntity);
        stack.setTag(itemTag);
        livingEntity.discard();
    }
}
