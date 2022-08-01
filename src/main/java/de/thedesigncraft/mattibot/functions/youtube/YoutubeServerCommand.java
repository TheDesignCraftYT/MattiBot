package de.thedesigncraft.mattibot.functions.youtube;

import de.thedesigncraft.mattibot.commands.types.ServerCommand;
import de.thedesigncraft.mattibot.constants.values.commands.Categories;
import de.thedesigncraft.mattibot.constants.values.commands.Versions;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.List;

public class YoutubeServerCommand implements ServerCommand {
    @Override
    public String version() {
        return Versions.versions().get("v100a8");
    }

    @Override
    public String description() {
        return "Öffnet die Einstellungen für die Youtube Benachrichtigungen.";
    }

    @Override
    public String category() {
        return Categories.categories().get("configuration");
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
        return Emoji.fromUnicode("U+25B6");
    }

    @Override
    public List<Permission> requiredPermissions() {

        List<Permission> returnList = new ArrayList<>();

        returnList.add(Permission.MANAGE_SERVER);

        return returnList;

    }

    @Override
    public void performGuildMessageCommand(MessageReceivedEvent event) {

        if (YoutubeEmbeds.mainPage(event.getGuild()).getFields().get(0).getValue().equals("```Kein Kanal festgelegt.```")) {

            event.getMessage().replyEmbeds(YoutubeEmbeds.mainPage(event.getGuild())).setActionRows(YoutubeActionRows.mainPageDisabled(event.getAuthor())).mentionRepliedUser(false).queue();

        } else {

            event.getMessage().replyEmbeds(YoutubeEmbeds.mainPage(event.getGuild())).setActionRows(YoutubeActionRows.mainPage(event.getMember())).mentionRepliedUser(false).queue();

        }

    }

    @Override
    public void performSlashCommand(SlashCommandInteractionEvent event) {

        if (YoutubeEmbeds.mainPage(event.getGuild()).getFields().get(0).getValue().equals("```Kein Kanal festgelegt.```")) {

            event.replyEmbeds(YoutubeEmbeds.mainPage(event.getGuild())).addActionRows(YoutubeActionRows.mainPageDisabled(event.getUser())).mentionRepliedUser(false).queue();

        } else {

            event.replyEmbeds(YoutubeEmbeds.mainPage(event.getGuild())).addActionRows(YoutubeActionRows.mainPage(event.getMember())).mentionRepliedUser(false).queue();

        }

    }
}
