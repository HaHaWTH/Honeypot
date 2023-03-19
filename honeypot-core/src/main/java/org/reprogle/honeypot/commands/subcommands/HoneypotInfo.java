package org.reprogle.honeypot.commands.subcommands;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.reprogle.honeypot.Honeypot;
import org.reprogle.honeypot.commands.CommandFeedback;
import org.reprogle.honeypot.commands.HoneypotSubCommand;
import org.reprogle.honeypot.utils.HoneypotPermission;

public class HoneypotInfo implements HoneypotSubCommand {

    @Override
    public String getName() {
        return "info";
    }

    @Override
    public void perform(Player p, String[] args) throws IOException {
        p.sendMessage(CommandFeedback.getChatPrefix() + " Honeypot version "
                + Honeypot.getPlugin().getDescription().getVersion());

        p.sendMessage(CommandFeedback.getChatPrefix() + " Running on " + Bukkit.getVersion());
        Honeypot.checkIfServerSupported();
    }

    @Override
    public List<String> getSubcommands(Player p, String[] args) {
        return new ArrayList<>();
    }

    @Override
    public List<HoneypotPermission> getRequiredPermissions() {
        return new ArrayList<>();
    }

}
