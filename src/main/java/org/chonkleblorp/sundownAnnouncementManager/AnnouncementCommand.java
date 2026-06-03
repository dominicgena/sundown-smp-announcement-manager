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
        Map<String, Command<CommandSourceStack>> commandLogics = new HashMap<>();
        commandLogics.put("read", AnnouncementCommand::runReadLogic);// associate the command string with the correct method

        return buildCommand("announcement", commandLogics);
    }

    private static LiteralArgumentBuilder<CommandSourceStack> buildCommand(String name, Map<String, Command<CommandSourceStack>> children) {
        LiteralArgumentBuilder<CommandSourceStack> command = Commands.literal(name);
        for (Map.Entry<String, Command<CommandSourceStack>> child : children.entrySet()) {
            command.then(Commands.literal(child.getKey())
                    .executes(child.getValue())
            );
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
}