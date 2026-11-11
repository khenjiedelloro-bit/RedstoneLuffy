package com.luffy.redstoneoptimizer.mixin;

import com.luffy.redstoneoptimizer.ModConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net.minecraft.client.renderer.blockentity.PistonHeadRenderer")
public class PistonHeadRendererMixin {
	@Inject(method = "submit", at = @At("HEAD"), cancellable = true)
	private void luffy$skipAnimation(CallbackInfo ci) {
		ModConfig c = ModConfig.INSTANCE;
		if (c.on(c.pistons)) ci.cancel();
	}
}
