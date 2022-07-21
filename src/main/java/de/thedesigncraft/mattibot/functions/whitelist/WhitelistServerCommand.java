package de.thedesigncraft.mattibot.functions.whitelist;

import de.thedesigncraft.mattibot.commands.types.ServerCommand;
import de.thedesigncraft.mattibot.constants.methods.StandardActionRows;
import de.thedesigncraft.mattibot.constants.values.commands.Categories;
import de.thedesigncraft.mattibot.constants.values.commands.Versions;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;

import java.util.ArrayList;
import java.util.List;

public class WhitelistServerCommand implements ServerCommand {
    @Override
    public String version() {
        return Versions.versions().get("v100a1");
    }

    @Override
    public String description() {
        return "Legt für Befehle erlaubte Kanäle fest.";
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
        return Emoji.fromUnicode("U+1F4DC");
    }

    @Override
    public List<Permission> requiredPermissions() {
        List<Permission> returnList = new ArrayList<>();

        returnList.add(Permission.MANAGE_SERVER);

        return returnList;
    }

    @Override
    public void performGuildMessageCommand(MessageReceivedEvent event) {

        if (WhitelistEmbeds.mainPage(event.getMember()).getFields().get(0).getValue().equals("```Alle Kanäle```")) {

            event.getMessage().replyEmbeds(WhitelistEmbeds.mainPage(event.getMember())).setActionRows(WhitelistActionRows.mainPageDisabled(event.getMember().getUser())).mentionRepliedUser(false).queue();

        } else {

            event.getMessage().replyEmbeds(WhitelistEmbeds.mainPage(event.getMember())).setActionRows(WhitelistActionRows.mainPage(event.getMember().getUser())).mentionRepliedUser(false).queue();

        }

    }

    @Override
    public void performSlashCommand(SlashCommandInteractionEvent event) {

        if (WhitelistEmbeds.mainPage(event.getMember()).getFields().get(0).getValue().equals("```Alle Kanäle```")) {

            event.replyEmbeds(WhitelistEmbeds.mainPage(event.getMember())).addActionRows(WhitelistActionRows.mainPageDisabled(event.getUser())).queue();

        } else {

            event.replyEmbeds(WhitelistEmbeds.mainPage(event.getMember())).addActionRows(WhitelistActionRows.mainPage(event.getUser())).queue();

        }

    }

}
