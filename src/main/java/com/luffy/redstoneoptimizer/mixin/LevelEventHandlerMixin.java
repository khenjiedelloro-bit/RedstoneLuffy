package com.luffy.redstoneoptimizer.mixin;

import com.luffy.redstoneoptimizer.ModConfig;
import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net.minecraft.client.renderer.LevelEventHandler")
public class LevelEventHandlerMixin {
	@Inject(method = "levelEvent", at = @At("HEAD"), cancellable = true)
	private void luffy$events(int type, BlockPos pos, int data, CallbackInfo ci) {
		ModConfig c = ModConfig.INSTANCE;
		if (type == 2000 && c.on(c.dispenserParticles)) ci.cancel();
		else if (type == 2010 && c.on(c.crafterParticles)) ci.cancel();
	}
}
