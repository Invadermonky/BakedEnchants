package com.invadermonky.bakedenchants.mixins;

import com.invadermonky.bakedenchants.config.ConfigTags;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.common.IRarity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = Item.class, priority = 999)
public abstract class ItemMixin {
    @Shadow
    protected abstract boolean isInCreativeTab(CreativeTabs tab);

    @Unique
    public Item getItem() {
        return (Item) (Object) this;
    }

    @Inject(method = "getForgeRarity", at = @At("RETURN"), remap = false, cancellable = true)
    private void getForgeRarityMixin(ItemStack stack, CallbackInfoReturnable<IRarity> cir) {
        if (!stack.isEmpty() && ConfigTags.shouldHideRarity(stack)) {
            cir.setReturnValue(EnumRarity.COMMON);
        }
    }

    @SideOnly(Side.CLIENT)
    @ModifyReturnValue(method = "hasEffect", at = @At("RETURN"))
    private boolean hasEffectMixin(boolean original, @Local(argsOnly = true) ItemStack stack) {
        if (original && ConfigTags.shouldHideEffect(stack)) {
            return false;
        }
        return original;
    }

    @Inject(method = "onCreated", at = @At("TAIL"))
    private void onCreatedMixin(ItemStack stack, World world, EntityPlayer player, CallbackInfo ci) {
        if (!stack.isEmpty() && ConfigTags.hasBakedEnchants(stack.getItem())) {
            ConfigTags.addBakedEnchants(stack);
        }
    }

    @Inject(method = "getSubItems", at = @At("TAIL"))
    private void getSubItemsMixin(CreativeTabs tab, NonNullList<ItemStack> items, CallbackInfo ci) {
        if (this.isInCreativeTab(tab) && ConfigTags.hasBakedEnchants(getItem())) {
            ItemStack bakedStack = new ItemStack(getItem());
            boolean did = false;
            for (int i = 0; i < items.size(); i++) {
                ItemStack itemStack = items.get(i);
                if (ItemStack.areItemsEqualIgnoreDurability(itemStack, bakedStack)) {
                    ConfigTags.addBakedEnchants(itemStack);
                    did = true;
                }
            }
            if (!did) {
                ConfigTags.addBakedEnchants(bakedStack);
                items.add(bakedStack);
            }
        }
    }
}
