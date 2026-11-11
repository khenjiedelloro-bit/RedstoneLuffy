package com.luffy.redstoneoptimizer.mixin;

import com.luffy.redstoneoptimizer.ModConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.PistonBaseBlock;
import net.minecraft.world.level.block.PistonHeadBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net.minecraft.client.renderer.chunk.RenderSectionRegion")
public class RenderSectionRegionMixin {
	@Inject(method = "getBlockState", at = @At("RETURN"), cancellable = true)
	private void luffy$pistons(BlockPos pos, CallbackInfoReturnable<BlockState> cir) {
		ModConfig c = ModConfig.INSTANCE;
		if (!c.on(c.pistons)) return;
		BlockState state = cir.getReturnValue();
		if (state == null) return;
		Block block = state.getBlock();
		if (block instanceof PistonHeadBlock) {
			cir.setReturnValue(Blocks.AIR.defaultBlockState());
		} else if (block instanceof PistonBaseBlock && state.getValue(PistonBaseBlock.EXTENDED)) {
			cir.setReturnValue(state.setValue(PistonBaseBlock.EXTENDED, false));
		}
	}
}
