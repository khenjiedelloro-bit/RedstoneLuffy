package com.luffy.redstoneoptimizer.mixin;

import com.luffy.redstoneoptimizer.ModConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net.minecraft.client.renderer.chunk.RenderSectionRegion")
public class RenderSectionRegionMixin {
	@Inject(method = "getBlockState", at = @At("RETURN"), cancellable = true)
	private void luffy$visuals(BlockPos pos, CallbackInfoReturnable<BlockState> cir) {
		ModConfig c = ModConfig.INSTANCE;
		if (!c.enabled) return;
		BlockState state = cir.getReturnValue();
		if (state == null) return;

		if (c.pistons) {
			if (state.is(Blocks.PISTON_HEAD)) {
				cir.setReturnValue(Blocks.AIR.defaultBlockState());
				return;
			}
			if ((state.is(Blocks.PISTON) || state.is(Blocks.STICKY_PISTON))
					&& state.getValue(BlockStateProperties.EXTENDED)) {
				cir.setReturnValue(state.setValue(BlockStateProperties.EXTENDED, false));
				return;
			}
		}

		if (c.waterFlow && state.is(Blocks.WATER)) {
			FluidState fluid = state.getFluidState();
			if (!fluid.isSource()) {
				cir.setReturnValue(Blocks.AIR.defaultBlockState());
			}
		}
	}
}
