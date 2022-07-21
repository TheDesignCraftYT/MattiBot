package de.thedesigncraft.mattibot.commands.categories.configuration;

import de.thedesigncraft.mattibot.commands.types.ServerCommand;
import de.thedesigncraft.mattibot.constants.methods.EmbedTemplates;
import de.thedesigncraft.mattibot.constants.values.commands.Categories;
import de.thedesigncraft.mattibot.constants.values.commands.Versions;
import de.thedesigncraft.mattibot.manage.LiteSQL;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.List;

public class PrefixServerCommand implements ServerCommand {
    @Override
    public String version() {
        return Versions.versions().get("v100a1");
    }

    @Override
    public String description() {
        return "Mit diesem Befehl kann man den Bot-Prefix ändern.";
    }

    @Override
    public String category() {
        return Categories.categories().get("configuration");
    }

    @Override
    public List<OptionData> options() {
        List<OptionData> returnList = new ArrayList<>();

        returnList.add(new OptionData(OptionType.STRING, "newprefix", "Der neue Prefix."));

        return returnList;
    }

    @Override
    public boolean slashCommand() {
        return true;
    }

    @Override
    public Emoji commandEmoji() {
        return Emoji.fromUnicode("U+2699");
    }

    @Override
    public List<Permission> requiredPermissions() {
        List<Permission> returnList = new ArrayList<>();

        returnList.add(Permission.MANAGE_SERVER);

        return returnList;
    }

    @Override
    public void performGuildMessageCommand(MessageReceivedEvent event) {

        event.getMessage().replyEmbeds(performCommand(event.getMessage().getContentDisplay().replace("prefix", "").split(" ")[1], event.getGuild())).queue();

    }

    @Override
    public void performSlashCommand(SlashCommandInteractionEvent event) {

        try {

            event.replyEmbeds(performCommand(event.getOption("newprefix").getAsString(), event.getGuild())).queue();

        } catch (NullPointerException e) {

            event.replyEmbeds(performCommand("", event.getGuild())).queue();

        }

    }

    private MessageEmbed performCommand(String newPrefix, Guild guild) {

        newPrefix = newPrefix.replace(" ", "");

        if(!newPrefix.equals("") && !newPrefix.equals(" ")) {

            LiteSQL.onUpdate("UPDATE prefix SET prefix = '" + newPrefix + "' WHERE guildid = " + guild.getIdLong());

            return EmbedTemplates.standardEmbed(commandEmoji().getName() + " Prefix", "Der Prefix wurde auf `" + newPrefix + "` gestellt.");

        } else {

            return EmbedTemplates.issueEmbed("Du musst einen Prefix angeben.", false);

        }

    }

}
