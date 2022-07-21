package de.thedesigncraft.mattibot.commands.categories.misc;

import de.thedesigncraft.mattibot.MattiBot;
import de.thedesigncraft.mattibot.commands.types.ServerCommand;
import de.thedesigncraft.mattibot.constants.methods.EmbedTemplates;
import de.thedesigncraft.mattibot.constants.values.commands.Categories;
import de.thedesigncraft.mattibot.constants.values.commands.Versions;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.List;

public class PingServerCommand implements ServerCommand {


    @Override
    public String version() {
        return Versions.versions().get("v100a1");
    }

    @Override
    public String description() {
        return "Zeigt dir den Ping des Bots an.";
    }

    @Override
    public String category() {
        return Categories.categories().get("misc");
    }

    @Override
    public List<OptionData> options() {
        return null;
    }

    @Override
    public boolean slashCommand() {
        return true;
    }

    @Override
    public Emoji commandEmoji() {
        return Emoji.fromUnicode("U+1F3D3");
    }

    @Override
    public List<Permission> requiredPermissions() {
        return null;
    }

    @Override
    public void performGuildMessageCommand(MessageReceivedEvent event) {

        event.getMessage().replyEmbeds(performCommand()).mentionRepliedUser(false).queue();

    }

    @Override
    public void performSlashCommand(SlashCommandInteractionEvent event) {

        event.replyEmbeds(performCommand()).queue();

    }

    private MessageEmbed performCommand() {

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.copyFrom(EmbedTemplates.standardEmbed(commandEmoji().getName() + " Ping", ""));

        embedBuilder.addField("Discord API Gateway:", "```" + MattiBot.jda.getGatewayPing() + "ms```", true);
        embedBuilder.addField("Discord API REST:", "```" + MattiBot.jda.getRestPing().complete() + "ms```", true);

        return embedBuilder.build();

    }

}
