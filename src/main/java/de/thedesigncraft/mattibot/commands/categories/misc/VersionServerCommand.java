package de.thedesigncraft.mattibot.commands.categories.misc;

import de.thedesigncraft.mattibot.commands.types.ServerCommand;
import de.thedesigncraft.mattibot.constants.methods.EmbedTemplates;
import de.thedesigncraft.mattibot.constants.methods.MainMethods;
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

public class VersionServerCommand implements ServerCommand {
    @Override
    public String version() {
        return Versions.versions().get("v100a5");
    }

    @Override
    public String description() {
        return "Gibt die aktuelle Version des Bots zurück.";
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
        return Emoji.fromUnicode("U+1F916");
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

        EmbedBuilder embedBuilder = new EmbedBuilder(EmbedTemplates.standardEmbed(commandEmoji().getName() + " Bot Version", "Unten siehst du die aktuelle Version des Bots."));
        embedBuilder.addField("Version", Versions.currentVersion(), true);
        embedBuilder.addField("VersionType", MainMethods.getToken()[0], true);

        return embedBuilder.build();

    }

}
