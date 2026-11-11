package com.luffy.redstoneoptimizer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ModConfig {
	public boolean enabled = true;
	public boolean pistons = true;
	public boolean dispenserParticles = true;
	public boolean redstoneDustParticles = true;
	public boolean noteParticles = true;
	public boolean crafterParticles = true;
	public boolean breakParticles = true;
	public boolean compressItems = true;
	public boolean waterFlow = true;

	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private static final Path FILE = FabricLoader.getInstance().getConfigDir().resolve("luffyredstoneoptimizer.json");
	public static final ModConfig INSTANCE = load();

	public boolean on(boolean feature) {
		return enabled && feature;
	}

	private static ModConfig load() {
		try {
			if (Files.exists(FILE)) {
				ModConfig c = GSON.fromJson(Files.readString(FILE), ModConfig.class);
				if (c != null) return c;
			}
		} catch (Exception ignored) {
		}
		return new ModConfig();
	}

	public static void save() {
		try {
			Files.writeString(FILE, GSON.toJson(INSTANCE));
		} catch (IOException ignored) {
		}
	}
}
