/*
 * Honeypot is a tool for griefing auto-moderation
 * Copyright TerrorByte (c) 2022-2023
 * Copyright Honeypot Contributors (c) 2022-2023
 *
 * This program is free software: You can redistribute it and/or modify it under the terms of the Mozilla Public License 2.0
 * as published by the Mozilla under the Mozilla Foundation.
 *
 * This program is distributed in the hope that it will be useful, but provided on an "as is" basis,
 * without warranty of any kind, either expressed, implied, or statutory, including, without limitation,
 * warranties that the Covered Software is free of defects, merchantable, fit for a particular purpose or non-infringing.
 * See the MPL 2.0 license for more details.
 *
 * For a full copy of the license in its entirety, please visit <https://www.mozilla.org/en-US/MPL/2.0/>
 */

package org.reprogle.honeypot.common.events;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.reprogle.honeypot.Honeypot;
import org.reprogle.honeypot.common.utils.HoneypotConfigManager;
import org.reprogle.honeypot.common.utils.HoneypotLogger;

public class Listeners {

	private final Honeypot plugin;
	private final HoneypotConfigManager configManager;
	private final HoneypotLogger logger;

	// Yay DI!
	@Inject BlockBreakEventListener blockBreakEventListener;
	@Inject BlockBurnEventListener blockBurnEventListener;
	@Inject EntityChangeBlockEventListener entityChangeBlockEventListener;
	@Inject EntityExplodeEventListener entityExplodeEventListener;
	@Inject PistonExtendRetractListener pistonExtendRetractListener;
	@Inject InventoryMoveItemEventListener inventoryMoveItemEventListener;
	@Inject StructureGrowEventListener structureGrowEventListener;
	@Inject PlayerCommandPreprocessEventListener playerCommandPreprocessEventListener;
	@Inject PlayerJoinEventListener playerJoinEventListener;

	@Inject BlockFormEventListener blockFormEventListener;
	@Inject LeavesDecayEventListener leavesDecayEventListener;
	@Inject SignChangeEventListener signChangeEventListener;
	@Inject BlockFromToEventListener blockFromToEventListener;

	@Inject InventoryClickDragEventListener inventoryClickDragEventListener;
	@Inject PlayerInteractEventListener playerInteractEventListener;

    /**
	 * Create package listener to hide implicit one
	 */
	@Inject
	Listeners(Honeypot plugin, HoneypotConfigManager configManager, HoneypotLogger logger) {
		this.plugin = plugin;
		this.configManager = configManager;
		this.logger = logger;
    }

	/**
	 * Set's up all the listeners in the entire plugin
	 */
	public void setupListeners() {

		// All primary listners go here
		final List<Listener> primaryListeners = new ArrayList<>(List.of(blockBreakEventListener,
				blockBurnEventListener, entityChangeBlockEventListener,
				entityExplodeEventListener, pistonExtendRetractListener,
				inventoryMoveItemEventListener, structureGrowEventListener,
				playerCommandPreprocessEventListener, playerJoinEventListener));

		// All secondary listeners here
		final List<Listener> secondaryListeners = new ArrayList<>(
				List.of(blockFormEventListener, leavesDecayEventListener, signChangeEventListener,
						blockFromToEventListener));

		// Initial registration of events
		PluginManager manager = plugin.getServer().getPluginManager();
		primaryListeners.forEach(event -> manager.registerEvents(event, plugin));

		// Register the proper events for container actions and their processors
		if (configManager.getPluginConfig().getBoolean("container-actions.enable-container-actions")) {
			if (configManager.getPluginConfig().getBoolean("container-actions.use-inventory-click")) {
				logger.info("Using inventory click for containers");
				manager.registerEvents(inventoryClickDragEventListener, plugin);
			} else {
				logger.info("Using player interact for containers");
				manager.registerEvents(playerInteractEventListener, plugin);
			}
		}

		// Register extra unnecessary events
		if (configManager.getPluginConfig().getBoolean("enable-extra-events")) {
			logger.info(
					"Extra events have been enabled. Some of the events can be noisy, and may cause additional lag on low-performance hardware, such as budget server hosts. If you experience lag, disable these events, Honeypot can still function without them!");
			secondaryListeners.forEach(event -> manager.registerEvents(event, plugin));
		}
	}

}