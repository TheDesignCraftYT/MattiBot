package de.thedesigncraft.mattibot.commands.types;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.List;

public interface ServerCommand {

    String version();

    String description();

    String category();

    List<OptionData> options();

    boolean slashCommand();

    Emoji commandEmoji();

    List<Permission> requiredPermissions();

    void performGuildMessageCommand(MessageReceivedEvent event);

    void performSlashCommand(SlashCommandInteractionEvent event);

}
