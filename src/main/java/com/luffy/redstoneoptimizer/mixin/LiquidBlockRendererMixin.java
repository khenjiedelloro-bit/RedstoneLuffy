package com.luffy.redstoneoptimizer.mixin;

import com.luffy.redstoneoptimizer.ModConfig;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net.minecraft.client.renderer.block.LiquidBlockRenderer")
public class LiquidBlockRendererMixin {
	@Inject(method = "tesselate", at = @At("HEAD"), cancellable = true)
	private void luffy$noFlowingWater(BlockAndTintGetter level, BlockPos pos, VertexConsumer consumer,
								  BlockState blockState, FluidState fluidState, CallbackInfo ci) {
		ModConfig c = ModConfig.INSTANCE;
		if (c.on(c.waterFlow) && fluidState.is(FluidTags.WATER) && !fluidState.isSource()) ci.cancel();
	}
}
