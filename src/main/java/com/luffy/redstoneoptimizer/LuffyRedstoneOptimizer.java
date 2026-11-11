package com.luffy.redstoneoptimizer;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public class LuffyRedstoneOptimizer implements ClientModInitializer {
	public static KeyMapping toggleKey;
	public static KeyMapping menuKey;

	@Override
	public void onInitializeClient() {
		KeyMapping.Category category = KeyMapping.Category.register(
				Identifier.fromNamespaceAndPath("luffyredstoneoptimizer", "main"));

		toggleKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
				"key.luffyredstoneoptimizer.toggle", InputConstants.Type.KEYSYM, InputConstants.KEY_K, category));
		menuKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
				"key.luffyredstoneoptimizer.menu", InputConstants.Type.KEYSYM, InputConstants.KEY_J, category));

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while (toggleKey.consumeClick()) {
				ModConfig.INSTANCE.enabled = !ModConfig.INSTANCE.enabled;
				ModConfig.save();
				Compat.rebuildChunks(client);
				if (client.player != null) {
					client.player.sendSystemMessage(Component.literal("LuffyRedstoneOptimizer: "
							+ (ModConfig.INSTANCE.enabled ? "ON" : "OFF")));
				}
			}
			while (menuKey.consumeClick()) {
				Compat.openScreen(client, new SettingsScreen(client.gui.screen()));
			}

			if (client.level != null && client.player != null) {
				ItemCompressor.tick(client.level, client.player.getX(), client.player.getY(), client.player.getZ());
			}
		});
	}
}
