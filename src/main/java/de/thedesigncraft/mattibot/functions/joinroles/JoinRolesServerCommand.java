package de.thedesigncraft.mattibot.functions.joinroles;

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

public class JoinRolesServerCommand implements ServerCommand {
    @Override
    public String version() {
        return Versions.versions().get("v100a6");
    }

    @Override
    public String description() {
        return "Öffnet das Einstellungsmenü für die JoinRoles.";
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
        return Emoji.fromUnicode("U+1F44B");
    }

    @Override
    public List<Permission> requiredPermissions() {

        List<Permission> returnList = new ArrayList<>();

        returnList.add(Permission.MANAGE_SERVER);

        return returnList;
    }

    @Override
    public void performGuildMessageCommand(MessageReceivedEvent event) {

        if (JoinRolesEmbeds.mainPage(event.getMember()).getFields().get(0).getValue().equals("```Keine Rollen```")) {

            event.getMessage().replyEmbeds(JoinRolesEmbeds.mainPage(event.getMember())).setActionRows(JoinRolesActionRows.mainPageDisabled(event.getMember().getUser())).mentionRepliedUser(false).queue();

        } else {

            event.getMessage().replyEmbeds(JoinRolesEmbeds.mainPage(event.getMember())).setActionRows(JoinRolesActionRows.mainPage(event.getMember().getUser())).mentionRepliedUser(false).queue();

        }

    }

    @Override
    public void performSlashCommand(SlashCommandInteractionEvent event) {

        if (JoinRolesEmbeds.mainPage(event.getMember()).getFields().get(0).getValue().equals("```Keine Rollen```")) {

            event.replyEmbeds(JoinRolesEmbeds.mainPage(event.getMember())).addActionRows(JoinRolesActionRows.mainPageDisabled(event.getUser())).queue();

        } else {

            event.replyEmbeds(JoinRolesEmbeds.mainPage(event.getMember())).addActionRows(JoinRolesActionRows.mainPage(event.getUser())).queue();

        }

    }
}
