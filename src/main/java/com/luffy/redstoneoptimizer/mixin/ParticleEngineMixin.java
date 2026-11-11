package com.luffy.redstoneoptimizer.mixin;

import com.luffy.redstoneoptimizer.ModConfig;
import net.minecraft.client.particle.Particle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net.minecraft.client.particle.ParticleEngine")
public class ParticleEngineMixin {
	@Inject(method = "createParticle", at = @At("HEAD"), cancellable = true)
	private void luffy$filter(ParticleOptions options, double x, double y, double z,
							  double vx, double vy, double vz, CallbackInfoReturnable<Particle> cir) {
		ModConfig c = ModConfig.INSTANCE;
		if (!c.enabled) return;
		ParticleType<?> type = options.getType();
		if ((type == ParticleTypes.NOTE && c.noteParticles)
				|| (type == ParticleTypes.DUST && c.redstoneDustParticles)) {
			cir.setReturnValue(null);
		}
	}

	@Inject(method = "destroy", at = @At("HEAD"), cancellable = true)
	private void luffy$noBreakParticles(BlockPos pos, BlockState state, CallbackInfo ci) {
		ModConfig c = ModConfig.INSTANCE;
		if (c.on(c.breakParticles)) ci.cancel();
	}

	@Inject(method = "crack", at = @At("HEAD"), cancellable = true)
	private void luffy$noCrackParticles(BlockPos pos, Direction side, CallbackInfo ci) {
		ModConfig c = ModConfig.INSTANCE;
		if (c.on(c.breakParticles)) ci.cancel();
	}
}
