package de.thedesigncraft.mattibot.functions.help;

import de.thedesigncraft.mattibot.commands.types.ServerCommand;
import de.thedesigncraft.mattibot.constants.methods.CommandMethods;
import de.thedesigncraft.mattibot.constants.methods.EmbedTemplates;
import de.thedesigncraft.mattibot.constants.values.commands.*;
import de.thedesigncraft.mattibot.contextmenus.types.MessageContextMenu;
import de.thedesigncraft.mattibot.contextmenus.types.UserContextMenu;
import de.thedesigncraft.mattibot.functions.help.methods.HelpActionRows;
import de.thedesigncraft.mattibot.functions.help.methods.HelpEmbeds;
import de.thedesigncraft.mattibot.manage.LiteSQL;
import de.thedesigncraft.mattibot.manage.ServerCommandManager;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class HelpServerCommand implements ServerCommand {
    @Override
    public String version() {
        return Versions.versions().get("v100a1");
    }

    @Override
    public String description() {
        return "Zeigt das Help-Menü des Bots an.";
    }

    @Override
    public String category() {
        return Categories.categories().get("misc");
    }

    @Override
    public List<OptionData> options() {
        List<OptionData> options = new ArrayList<>();
        OptionData command = new OptionData(OptionType.STRING, "command", "Welchen Command möchtest du öffnen?", false);

        ServerCommands.serverCommands().forEach(serverCommand -> command.addChoice("/" + CommandMethods.getServerCommandName(serverCommand), "/" + CommandMethods.getServerCommandName(serverCommand)));
        UserContextMenus.userContextMenus().forEach(userContextMenu -> command.addChoice("USER/" + CommandMethods.getUserContextMenuName(userContextMenu), "USER/" + CommandMethods.getUserContextMenuName(userContextMenu)));
        MessageContextMenus.messageContextMenus().forEach(messageContextMenu -> command.addChoice("MESSAGE/" + CommandMethods.getMessageContextMenuName(messageContextMenu), "MESSAGE/" + CommandMethods.getMessageContextMenuName(messageContextMenu)));

        options.add(command);
        OptionData category = new OptionData(OptionType.STRING, "category", "Welche Kategorie möchtest du öffnen?", false);
        Categories.categories().forEach(category::addChoice);
        options.add(category);
        return options;
    }

    @Override
    public boolean slashCommand() {
        return true;
    }

    @Override
    public Emoji commandEmoji() {
        return Emoji.fromUnicode("U+2139");
    }

    @Override
    public List<Permission> requiredPermissions() {
        return null;
    }

    @Override
    public void performGuildMessageCommand(MessageReceivedEvent event) {

        ResultSet prefix = LiteSQL.onQuery("SELECT prefix FROM prefix WHERE guildid = " + event.getGuild().getIdLong());

        try {
            String prefix1 = prefix.getString("prefix");

            String messageContent = event.getMessage().getContentDisplay().toLowerCase().replace(prefix1.toLowerCase() + "help", "");

            if (!messageContent.equals("") && !messageContent.equals(" ")) {

                String arg = event.getMessage().getContentDisplay().replace(prefix1 + "help", "").replace(" ", "");

                String arg2 = Categories.categories().get(arg);

                ServerCommand slashCommand = ServerCommandManager.slashCommandsMap.get(arg.replace("/", ""));

                UserContextMenu userContextMenu = ServerCommandManager.userContextMenuMap.get(arg.replace("USER/", ""));

                MessageContextMenu messageContextMenu = ServerCommandManager.messageContextMenuMap.get(arg.replace("MESSAGE/", ""));

                if (arg2 != null) {

                    event.getMessage().replyEmbeds(HelpEmbeds.category(arg2, event.getGuild())).setActionRows(HelpActionRows.category(arg2, event.getMember().getIdLong(), event.getGuild())).mentionRepliedUser(false).queue();

                } else if (slashCommand != null) {

                    event.getMessage().replyEmbeds(HelpEmbeds.slashCommand(slashCommand, event.getChannel().asTextChannel())).setActionRow(HelpActionRows.command(slashCommand.category(), event.getMember().getIdLong())).mentionRepliedUser(false).queue();

                } else if (userContextMenu != null) {

                    event.getMessage().replyEmbeds(HelpEmbeds.userCommand(userContextMenu)).setActionRow(HelpActionRows.command(userContextMenu.category(), event.getMember().getIdLong())).mentionRepliedUser(false).queue();

                } else if (messageContextMenu != null) {

                    event.getMessage().replyEmbeds(HelpEmbeds.messageCommand(messageContextMenu)).setActionRow(HelpActionRows.command(messageContextMenu.category(), event.getMember().getIdLong())).mentionRepliedUser(false).queue();

                } else {

                    event.getMessage().replyEmbeds(EmbedTemplates.issueEmbed("Das ist kein gültiger Wert.\n\nDu kannst Command-Namen und Kategorie-Namen angeben.", true)).mentionRepliedUser(false).queue(message1 -> {

                        event.getMessage().delete().queueAfter(10, TimeUnit.SECONDS);
                        message1.delete().queueAfter(10, TimeUnit.SECONDS);

                    });

                }

            } else {

                event.getMessage().replyEmbeds(HelpEmbeds.mainPage()).setActionRows(HelpActionRows.mainPage(event.getMember().getUser())).mentionRepliedUser(false).queue();

            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void performSlashCommand(SlashCommandInteractionEvent event) {

        if (event.getOption("category") == null && event.getOption("command") == null) {

            event.replyEmbeds(HelpEmbeds.mainPage()).addActionRows(HelpActionRows.mainPage(event.getUser())).queue();

        } else if (event.getOption("category") != null && event.getOption("command") != null) {

            event.replyEmbeds(EmbedTemplates.issueEmbed("Du kannst **entweder** einen Command **oder** eine Kategorie angeben.", false)).setEphemeral(true).queue();

        } else if (event.getOption("category") != null && event.getOption("command") == null) {

            String arg2 = event.getOption("category").getAsString();

            event.replyEmbeds(HelpEmbeds.category(arg2, event.getGuild())).addActionRows(HelpActionRows.category(arg2, event.getMember().getIdLong(), event.getGuild())).queue();

        } else if (event.getOption("category") == null && event.getOption("command") != null) {

            String[] commandArgs = event.getOption("command").getAsString().split("/");

            ServerCommand slashCommand = ServerCommandManager.slashCommandsMap.get(commandArgs[1]);

            UserContextMenu userContextMenu = ServerCommandManager.userContextMenuMap.get(commandArgs[1]);

            MessageContextMenu messageContextMenu = ServerCommandManager.messageContextMenuMap.get(commandArgs[1]);

            if (commandArgs.length == 2 && commandArgs[0].equals("")) {

                event.replyEmbeds(HelpEmbeds.slashCommand(slashCommand, event.getChannel().asTextChannel())).addActionRow(HelpActionRows.command(slashCommand.category(), event.getMember().getIdLong())).queue();

            } else if (commandArgs.length == 2 && commandArgs[0].equals("USER")) {

                event.replyEmbeds(HelpEmbeds.userCommand(userContextMenu)).addActionRow(HelpActionRows.command(userContextMenu.category(), event.getMember().getIdLong())).queue();

            } else if (commandArgs.length == 2 && commandArgs[0].equals("MESSAGE")) {

                event.replyEmbeds(HelpEmbeds.messageCommand(messageContextMenu)).addActionRow(HelpActionRows.command(messageContextMenu.category(), event.getMember().getIdLong())).queue();

            } else {

                event.replyEmbeds(EmbedTemplates.issueEmbed("Ein unerwarteter Fehler ist aufgetreten.\n\nWenn dies passiert, melde es bitte umgehend dem Entwickler des Bots.", false)).queue();

            }

        } else {

            event.replyEmbeds(EmbedTemplates.issueEmbed("Das ist kein gültiger Wert.\n\nDu kannst Command-Namen und Kategorie-Namen angeben.", false)).setEphemeral(true).queue();

        }

    }
}
