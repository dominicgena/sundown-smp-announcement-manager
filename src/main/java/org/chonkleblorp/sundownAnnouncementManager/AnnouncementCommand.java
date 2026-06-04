package org.chonkleblorp.sundownAnnouncementManager;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class AnnouncementCommand {
    private final AnnouncementConfigManager manager = AnnouncementConfigManager.getInstance();

    public LiteralArgumentBuilder<CommandSourceStack> create() {
        Map<String, LiteralArgumentBuilder<CommandSourceStack>> subCommands = new HashMap<>();

        // base players and operators
        subCommands.put("read", Commands.literal("read")
                .requires(source -> source.getExecutor() instanceof Player)
                .executes(this::runReadLogic)
        );

        // only operator
        subCommands.put("broadcast", Commands.literal("broadcast")
                .requires(source -> source.getSender().hasPermission("op.node"))
                .then(
                        Commands.argument("message", StringArgumentType.greedyString())
                                .executes(this::runBroadcastLogic)
                )
        );

        return buildCommand("announcement", subCommands);
    }

    private LiteralArgumentBuilder<CommandSourceStack> buildCommand(String name, Map<String, LiteralArgumentBuilder<CommandSourceStack>> children) {
        LiteralArgumentBuilder<CommandSourceStack> command = Commands.literal(name);
        for (LiteralArgumentBuilder<CommandSourceStack> child : children.values()) {
            command.then(child);
        }
        return command;
    }

    private int runReadLogic(CommandContext<CommandSourceStack> ctx) {
        CommandSender sender = ctx.getSource().getSender();
        if (!(ctx.getSource().getExecutor() instanceof Player player)) {
            sender.sendPlainMessage("You must be an in-game player to read the announcement.");
            return Command.SINGLE_SUCCESS;
        }

        String savedMessage = manager.getMessage();

        // prevent errors if the config hasn't been set yet
        if (savedMessage == null || savedMessage.isEmpty() || savedMessage.equals("null")) {
            player.sendRichMessage("<red>There is currently no announcement to read.</red>");
            return Command.SINGLE_SUCCESS;
        }

        // create the announcement object using the saved message and open the book
        Announcement announcement = new Announcement(savedMessage);
        player.openBook(announcement.getBook());

        return Command.SINGLE_SUCCESS;
    }

    private int runBroadcastLogic(CommandContext<CommandSourceStack> ctx) {
        CommandSender sender = ctx.getSource().getSender();
        String message = StringArgumentType.getString(ctx, "message");

        // save the message to the config
        manager.setMessage(message);

        if (sender instanceof Player) {
            sender.sendRichMessage("<green>Announcement updated successfully!</green>");
        } else {
            System.out.println("Announcement updated successfully!");
        }
        return Command.SINGLE_SUCCESS;
    }
}