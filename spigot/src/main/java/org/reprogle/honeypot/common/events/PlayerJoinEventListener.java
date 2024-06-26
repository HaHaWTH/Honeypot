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

import com.google.inject.Inject;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.reprogle.honeypot.Honeypot;
import org.reprogle.honeypot.common.commands.CommandFeedback;
import org.reprogle.honeypot.common.utils.HoneypotLogger;
import org.reprogle.honeypot.common.utils.HoneypotUpdateChecker;

public class PlayerJoinEventListener implements Listener {

	private final CommandFeedback commandFeedback;
	private final HoneypotLogger logger;
	private final Honeypot plugin;

	/**
	 * Create a private constructor to hide the implicit one
	 */
	@Inject
	PlayerJoinEventListener(Honeypot plugin, CommandFeedback commandFeedback, HoneypotLogger logger) {
		this.plugin = plugin;
		this.commandFeedback = commandFeedback;
		this.logger = logger;
	}

	// Player join event
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	@SuppressWarnings("deprecation")
	public void playerJoinEvent(PlayerJoinEvent event) {
		Player p = event.getPlayer();

		if (p.hasPermission("honeypot.update") || p.hasPermission("honeypot.*") || p.isOp()) {
			new HoneypotUpdateChecker(plugin,
					"https://raw.githubusercontent.com/TerrorByteTW/Honeypot/master/version.txt").getVersion(latest -> {
						if (Integer.parseInt(latest.replace(".", "")) > Integer
								.parseInt(plugin.getDescription().getVersion().replace(".", ""))) {
							TextComponent message = new TextComponent(
									commandFeedback.sendCommandFeedback("updateavailable"));
							message.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL,
									"https://github.com/TerrorByteTW/Honeypot"));
							message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
									new Text("Click me to download the latest update!")));
							p.spigot().sendMessage(message);
						}
					}, logger);
		}
	}

}
