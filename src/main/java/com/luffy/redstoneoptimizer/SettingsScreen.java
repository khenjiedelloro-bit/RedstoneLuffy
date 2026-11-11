package com.luffy.redstoneoptimizer;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class SettingsScreen extends Screen {
	private final Screen parent;

	public SettingsScreen(Screen parent) {
		super(Component.literal("LuffyRedstoneOptimizer"));
		this.parent = parent;
	}

	@Override
	protected void init() {
		ModConfig c = ModConfig.INSTANCE;
		int left = this.width / 2 - 155;
		int right = this.width / 2 + 5;
		int y = 20;

		Button header = Button.builder(Component.literal("LuffyRedstoneOptimizer"), b -> {})
				.bounds(left, y, 310, 20).build();
		header.active = false;
		this.addRenderableWidget(header);
		y += 28;

		toggle(left, y, 310, "Optimizer (master)", () -> c.enabled, v -> c.enabled = v, true);
		y += 28;
		toggle(left, y, 150, "Pistons", () -> c.pistons, v -> c.pistons = v, true);
		toggle(right, y, 150, "Dispenser particles", () -> c.dispenserParticles, v -> c.dispenserParticles = v, false);
		y += 24;
		toggle(left, y, 150, "Redstone dust particles", () -> c.redstoneDustParticles, v -> c.redstoneDustParticles = v, false);
		toggle(right, y, 150, "Note block particles", () -> c.noteParticles, v -> c.noteParticles = v, false);
		y += 24;
		toggle(left, y, 150, "Crafter particles", () -> c.crafterParticles, v -> c.crafterParticles = v, false);
		toggle(right, y, 150, "Block break particles", () -> c.breakParticles, v -> c.breakParticles = v, false);
		y += 24;
		toggle(left, y, 150, "Compress dropped items", () -> c.compressItems, v -> c.compressItems = v, false);
		toggle(right, y, 150, "Water flow", () -> c.waterFlow, v -> c.waterFlow = v, true);

		this.addRenderableWidget(Button.builder(Component.literal("Done"), b -> onClose())
				.bounds(this.width / 2 - 100, this.height - 28, 200, 20).build());
	}

	private void toggle(int x, int y, int w, String name, Supplier<Boolean> get, Consumer<Boolean> set, boolean rebuildChunks) {
		this.addRenderableWidget(Button.builder(label(name, get.get()), b -> {
			set.accept(!get.get());
			ModConfig.save();
			b.setMessage(label(name, get.get()));
			if (rebuildChunks) Compat.rebuildChunks(this.minecraft);
		}).bounds(x, y, w, 20).build());
	}

	private static Component label(String name, boolean on) {
		return Component.literal(name + ": " + (on ? "ON" : "OFF"));
	}

	@Override
	public void onClose() {
		Compat.openScreen(this.minecraft, parent);
	}
}
