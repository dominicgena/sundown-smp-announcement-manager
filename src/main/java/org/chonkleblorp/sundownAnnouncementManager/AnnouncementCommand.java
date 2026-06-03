package org.chonkleblorp.sundownAnnouncementManager;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class AnnouncementCommand {
    public static LiteralArgumentBuilder<CommandSourceStack> create() {
        // Map's value type holds the fully built subcommand node
        Map<String, LiteralArgumentBuilder<CommandSourceStack>> subCommands = new HashMap<>();

        // 1. The Base-Player Command (No permissions required)
        subCommands.put("read", Commands.literal("read")
                .executes(AnnouncementCommand::runReadLogic));

        // 2. The Operator Command (Requires the op.node permission)
        subCommands.put("broadcast", Commands.literal("broadcast")
                .requires(source -> source.getSender().hasPermission("op.node"))
                .executes(AnnouncementCommand::runBroadcastLogic));

        return buildCommand("announcement", subCommands);
    }

    private static LiteralArgumentBuilder<CommandSourceStack> buildCommand(String name, Map<String, LiteralArgumentBuilder<CommandSourceStack>> children) {
        LiteralArgumentBuilder<CommandSourceStack> command = Commands.literal(name);
        // attach each subcommand to its parent
        for (LiteralArgumentBuilder<CommandSourceStack> child : children.values()) {
            command.then(child);
        }

        return command;
    }

    private static int runReadLogic(CommandContext<CommandSourceStack> ctx) {
        CommandSender sender = ctx.getSource().getSender();
        Entity executor = ctx.getSource().getExecutor();
        if (!(executor instanceof Player player)) {
            sender.sendPlainMessage("This command can only be executed by players!");
            return Command.SINGLE_SUCCESS;
        }
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, SoundCategory.MASTER, 1.0f, 1.0f);

        if (sender == executor) {
            player.sendPlainMessage("Successfully played harp sound!");
            return Command.SINGLE_SUCCESS;
        }

        sender.sendRichMessage("Successfully played sound for <playername>.", Placeholder.component("playername", player.name()));
        player.sendPlainMessage("Successfully played sound!");
        return Command.SINGLE_SUCCESS;
    }

    private static int runBroadcastLogic(CommandContext<CommandSourceStack> ctx) {
        CommandSender sender = ctx.getSource().getSender();
        Entity executor = ctx.getSource().getExecutor();

        if (!sender.hasPermission("op.node")) {
            sender.sendRichMessage("<red>You do not have permission to broadcast announcements.");
            return Command.SINGLE_SUCCESS; // Exit the method immediately
        }

        if (!(executor instanceof Player player)) {
            sender.sendPlainMessage("This command can only be executed by players!");
            return Command.SINGLE_SUCCESS;
        }
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, SoundCategory.MASTER, 1.0f, 1.0f);
        sender.sendPlainMessage("Hello from broadcast!");

        if (sender == executor) {
            player.sendPlainMessage("Successfully played harp sound!");
            return Command.SINGLE_SUCCESS;
        }

        sender.sendRichMessage("Successfully played sound for <playername>.", Placeholder.component("playername", player.name()));
        player.sendPlainMessage("Successfully played sound!");
        return Command.SINGLE_SUCCESS;
    }
}