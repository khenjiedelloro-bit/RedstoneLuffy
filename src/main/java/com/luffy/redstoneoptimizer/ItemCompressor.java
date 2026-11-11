package com.luffy.redstoneoptimizer;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.phys.AABB;

import java.util.*;

public final class ItemCompressor {
	private static final int THRESHOLD = 4;
	private static final double RADIUS = 48.0;
	private static volatile Set<Integer> hidden = Collections.emptySet();
	private static int counter = 0;

	private record Key(Item item, int x, int y, int z) {}

	private ItemCompressor() {}

	public static boolean isHidden(ItemEntity e) {
		return !hidden.isEmpty() && hidden.contains(e.getId());
	}

	public static void tick(ClientLevel level, double px, double py, double pz) {
		ModConfig c = ModConfig.INSTANCE;
		if (!c.on(c.compressItems)) {
			if (!hidden.isEmpty()) hidden = Collections.emptySet();
			return;
		}
		if (++counter % 2 != 0) return;

		AABB box = new AABB(px - RADIUS, py - RADIUS, pz - RADIUS, px + RADIUS, py + RADIUS, pz + RADIUS);
		List<ItemEntity> items = level.getEntitiesOfClass(ItemEntity.class, box);

		Map<Key, List<ItemEntity>> groups = new HashMap<>();
		for (ItemEntity ie : items) {
			if (ie.isRemoved()) continue;
			Key k = new Key(ie.getItem().getItem(),
					(int) Math.floor(ie.getX() / 2.0),
					(int) Math.floor(ie.getY() / 2.0),
					(int) Math.floor(ie.getZ() / 2.0));
			groups.computeIfAbsent(k, x -> new ArrayList<>()).add(ie);
		}

		Set<Integer> h = new HashSet<>();
		for (List<ItemEntity> list : groups.values()) {
			if (list.size() < THRESHOLD) continue;
			ItemEntity leader = list.get(0);
			for (ItemEntity ie : list) if (ie.getId() < leader.getId()) leader = ie;
			for (ItemEntity ie : list) if (ie != leader) h.add(ie.getId());
		}
		hidden = h;
	}
}
