package com.luffy.redstoneoptimizer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public final class Compat {
	private Compat() {}

	public static void openScreen(Minecraft mc, Screen screen) {
		try {
			Object gui = mc.getClass().getField("gui").get(mc);
			for (String name : new String[] {"setScreenAndShow", "setScreen"}) {
				try {
					Method m = gui.getClass().getMethod(name, Screen.class);
					m.invoke(gui, screen);
					return;
				} catch (NoSuchMethodException ignored) {
				}
			}
		} catch (Throwable ignored) {
		}
		try {
			mc.getClass().getMethod("setScreen", Screen.class).invoke(mc, screen);
		} catch (Throwable ignored) {
		}
	}

	public static void rebuildChunks(Minecraft mc) {
		try {
			Field f = mc.getClass().getField("levelRenderer");
			Object lr = f.get(mc);
			if (lr == null) return;
			for (String name : new String[] {"invalidateCompiledGeometry", "allChanged"}) {
				try {
					lr.getClass().getMethod(name).invoke(lr);
					return;
				} catch (NoSuchMethodException ignored) {
				}
			}
		} catch (Throwable ignored) {
		}
	}
}
