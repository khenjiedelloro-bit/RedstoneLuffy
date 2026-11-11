package com.luffy.redstoneoptimizer.mixin;

import com.luffy.redstoneoptimizer.ItemCompressor;
import com.luffy.redstoneoptimizer.ModConfig;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net.minecraft.client.renderer.entity.EntityRenderer")
public class EntityRendererMixin {
	@Inject(method = "shouldRender", at = @At("HEAD"), cancellable = true)
	private void luffy$compressItems(Entity entity, Frustum frustum, double camX, double camY, double camZ,
									 CallbackInfoReturnable<Boolean> cir) {
		if (entity instanceof ItemEntity item) {
			ModConfig c = ModConfig.INSTANCE;
			if (c.on(c.compressItems) && ItemCompressor.isHidden(item)) cir.setReturnValue(false);
		}
	}
}
