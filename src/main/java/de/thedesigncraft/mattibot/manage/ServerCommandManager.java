package de.thedesigncraft.mattibot.manage;

import de.thedesigncraft.mattibot.MattiBot;
import de.thedesigncraft.mattibot.commands.types.ServerCommand;
import de.thedesigncraft.mattibot.constants.methods.CommandMethods;
import de.thedesigncraft.mattibot.constants.methods.EmbedTemplates;
import de.thedesigncraft.mattibot.constants.methods.MainMethods;
import de.thedesigncraft.mattibot.constants.values.MainValues;
import de.thedesigncraft.mattibot.constants.values.commands.MessageContextMenus;
import de.thedesigncraft.mattibot.constants.values.commands.ServerCommands;
import de.thedesigncraft.mattibot.constants.values.commands.UserContextMenus;
import de.thedesigncraft.mattibot.contextmenus.types.MessageContextMenu;
import de.thedesigncraft.mattibot.contextmenus.types.UserContextMenu;
import de.thedesigncraft.mattibot.listeners.StandardActionRowListener;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.MessageContextInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.UserContextInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class ServerCommandManager extends ListenerAdapter {

    public static ConcurrentHashMap<String, ServerCommand> slashCommandsMap;
    public static ConcurrentHashMap<String, UserContextMenu> userContextMenuMap;

    public static ConcurrentHashMap<String, MessageContextMenu> messageContextMenuMap;

    public ServerCommandManager() {

        slashCommandsMap = new ConcurrentHashMap<>();
        userContextMenuMap = new ConcurrentHashMap<>();
        messageContextMenuMap = new ConcurrentHashMap<>();

        ServerCommands.serverCommands().forEach(serverCommand -> slashCommandsMap.put(CommandMethods.getServerCommandName(serverCommand), serverCommand));
        UserContextMenus.userContextMenus().forEach(userContextMenu -> userContextMenuMap.put(CommandMethods.getUserContextMenuName(userContextMenu), userContextMenu));
        MessageContextMenus.messageContextMenus().forEach(messageContextMenu -> messageContextMenuMap.put(CommandMethods.getMessageContextMenuName(messageContextMenu), messageContextMenu));

        MattiBot.jda.retrieveCommands().queue(commands -> {

            List<String> oldCommandNames = new ArrayList<>();

            commands.forEach(command -> oldCommandNames.add(command.getName()));

            List<String> newCommandNames = new ArrayList<>();

            slashCommandsMap.forEach((s, serverCommand) -> {

                if (!oldCommandNames.contains(s))
                    newCommandNames.add(s);

            });

            userContextMenuMap.forEach((s, userContextMenu) -> {

                if (!oldCommandNames.contains(s))
                    newCommandNames.add(s);

            });

            messageContextMenuMap.forEach((s, messageContextMenu) -> {

                if (!oldCommandNames.contains(s))
                    newCommandNames.add(s);

            });

            Logger logger = LoggerFactory.getLogger(ServerCommandManager.class);

            newCommandNames.forEach(s -> logger.info("Neuer Befehl registriert: '" + s + "'"));

        });

        CommandListUpdateAction updateAction = MattiBot.jda.updateCommands();

        ServerCommands.serverCommands().forEach(serverCommand -> {

            if (serverCommand.slashCommand()) {

                try {

                    if (serverCommand.options() != null) {

                        updateAction.addCommands(Commands.slash(CommandMethods.getServerCommandName(serverCommand), serverCommand.description()).addOptions(serverCommand.options()).setGuildOnly(true));

                    } else {

                        updateAction.addCommands(Commands.slash(CommandMethods.getServerCommandName(serverCommand), serverCommand.description()).setGuildOnly(true));

                    }

                } catch (IllegalArgumentException e) {
                    Logger logger = LoggerFactory.getLogger(ServerCommandManager.class);
                    logger.error(e.getMessage());
                }

            }

        });

        UserContextMenus.userContextMenus().forEach(userContextMenu -> updateAction.addCommands(Commands.user(CommandMethods.getUserContextMenuName(userContextMenu)).setGuildOnly(true)));

        MessageContextMenus.messageContextMenus().forEach(messageContextMenu -> updateAction.addCommands(Commands.message(CommandMethods.getMessageContextMenuName(messageContextMenu)).setGuildOnly(true)));

        updateAction.queue();

        Logger logger = LoggerFactory.getLogger(ServerCommandManager.class);
        logger.info("Befehle registriert.");

    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {

        String commandName = event.getName();

        ServerCommand serverCommand;

        if ((serverCommand = slashCommandsMap.get(commandName)) != null) {

            List<TextChannel> whitelistedChannels = MainMethods.getWhitelistedChannels(event.getGuild());

            if (!event.getMember().hasPermission(Permission.ADMINISTRATOR) && !whitelistedChannels.contains(event.getChannel().asTextChannel())) {

                StringBuilder stringBuilder = new StringBuilder();

                whitelistedChannels.forEach(textChannel -> stringBuilder
                        .append("> • ")
                        .append(textChannel.getAsMention())
                        .append("\n"));

                event.replyEmbeds(EmbedTemplates.issueEmbed("Du kannst hier keine Befehle ausführen.\n> Befehle können nur in den folgenden Kanälen ausgeführt werden:\n" + stringBuilder, false)).setEphemeral(true).queue();

            } else {

                if (serverCommand.requiredPermissions() != null) {

                    if (event.getMember().hasPermission(serverCommand.requiredPermissions())) {

                        serverCommand.performSlashCommand(event);

                    } else {

                        List<Permission> missingPermissions = new ArrayList<>();

                        serverCommand.requiredPermissions().forEach(permission -> {

                            if (!event.getMember().hasPermission(permission))
                                missingPermissions.add(permission);

                        });

                        StringBuilder stringBuilder = new StringBuilder();

                        missingPermissions.forEach(permission -> stringBuilder
                                .append("> • ")
                                .append(permission.getName())
                                .append("\n"));

                        event.replyEmbeds(EmbedTemplates.issueEmbed("Du hast nicht alle nötigen Berechtigungen für diesen Befehl.\n> Dir fehlen folgende Berechtigungen:\n\n" + stringBuilder, false)).setEphemeral(true).queue();

                    }

                } else {

                    serverCommand.performSlashCommand(event);

                }

            }

        }

        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException ignored) {
        }

        if (!event.isAcknowledged()) {

            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.copyFrom(EmbedTemplates.issueEmbed("Auf einen SlashCommand wurde nicht reagiert.", false));
            embedBuilder.addField("CommandName", "```/" + Objects.requireNonNull(event.getName()).split("&id=")[0] + "```", true);
            embedBuilder.addField("User", "```" + event.getUser().getAsTag() + "```", true);
            embedBuilder.addField("Server", "```" + Objects.requireNonNull(event.getGuild()).getName() + "```", true);

            Logger logger = LoggerFactory.getLogger(StandardActionRowListener.class);
            logger.error("Keine Reaktion auf SlashCommand");

            MainValues.owner.openPrivateChannel().queue(privateChannel -> privateChannel.sendMessageEmbeds(embedBuilder.build()).queue());
            event.replyEmbeds(EmbedTemplates.issueEmbed("Ein Unerwarteter Fehler ist aufgetreten.\n\nDer Fehler wurde an den Entwickler gesendet.", false)).setEphemeral(true).queue();

        }

    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {

        if (!event.isFromGuild())
            return;

        String messageContent = event.getMessage().getContentDisplay();

        if (event.isFromType(ChannelType.TEXT)) {

            TextChannel textChannel = event.getChannel().asTextChannel();

            try {

                ResultSet prefix = LiteSQL.onQuery("SELECT prefix FROM prefix WHERE guildid = " + event.getGuild().getIdLong());

                String prefixString = prefix.getString("prefix");

                if (messageContent.startsWith(prefixString)) {

                    String[] args = messageContent.replaceFirst(prefixString, "").split(" ");

                    if (!args[0].equals("") && !args[0].equals(" ")) {

                        if (slashCommandsMap.containsKey(args[0].toLowerCase())) {

                            List<TextChannel> whitelistedChannels = MainMethods.getWhitelistedChannels(textChannel.getGuild());

                            if (!event.getMember().hasPermission(Permission.ADMINISTRATOR) && !whitelistedChannels.contains(textChannel)) {

                                StringBuilder stringBuilder = new StringBuilder();

                                whitelistedChannels.forEach(textChannel1 -> stringBuilder
                                        .append("> • ")
                                        .append(textChannel.getAsMention())
                                        .append("\n"));

                                event.getMessage().replyEmbeds(EmbedTemplates.issueEmbed("Du kannst hier keine Befehle ausführen.\n> Befehle können nur in den folgenden Kanälen ausgeführt werden:\n" + stringBuilder, true)).mentionRepliedUser(false).queue(message1 -> {

                                    event.getMessage().delete().queueAfter(10, TimeUnit.SECONDS);
                                    message1.delete().queueAfter(10, TimeUnit.SECONDS);

                                });

                            } else {

                                ServerCommand serverCommand = slashCommandsMap.get(args[0].toLowerCase());

                                if (serverCommand.requiredPermissions() != null) {

                                    if (event.getMember().hasPermission(serverCommand.requiredPermissions())) {

                                        serverCommand.performGuildMessageCommand(event);

                                    } else {

                                        List<Permission> missingPermissions = new ArrayList<>();

                                        serverCommand.requiredPermissions().forEach(permission -> {

                                            if (!event.getMember().hasPermission(permission))
                                                missingPermissions.add(permission);

                                        });

                                        StringBuilder stringBuilder = new StringBuilder();

                                        missingPermissions.forEach(permission -> stringBuilder
                                                .append("> • ")
                                                .append(permission.getName())
                                                .append("\n"));

                                        event.getMessage().replyEmbeds(EmbedTemplates.issueEmbed("Du hast nicht alle nötigen Berechtigungen für diesen Befehl.\n>Dir fehlen folgende Berechtigungen:\n\n" + stringBuilder, true)).mentionRepliedUser(false).queue(message1 -> {

                                            event.getMessage().delete().queueAfter(10, TimeUnit.SECONDS);
                                            message1.delete().queueAfter(10, TimeUnit.SECONDS);

                                        });

                                    }

                                } else {

                                    serverCommand.performGuildMessageCommand(event);

                                }

                            }

                        } else {

                            event.getMessage().replyEmbeds(EmbedTemplates.issueEmbed("Das ist kein gültiger Befehl.\n> Für eine Liste an Befehlen, führe `/help` aus.", true)).mentionRepliedUser(false).queue(message -> {

                                event.getMessage().delete().queueAfter(10, TimeUnit.SECONDS);
                                message.delete().queueAfter(10, TimeUnit.SECONDS);

                            });

                        }

                    }

                }

            } catch (SQLException e) {

                LiteSQL.onUpdate("INSERT INTO prefix(guildid, prefix) VALUES(" + event.getGuild().getIdLong() + ", '" + MainValues.standardcommandPrefix + "')");

            }

        }

    }

    @Override
    public void onUserContextInteraction(@NotNull UserContextInteractionEvent event) {

        String commandName = event.getName();

        UserContextMenu userContextMenu;

        if ((userContextMenu = userContextMenuMap.get(commandName)) != null) {

            if (userContextMenu.requiredPermissions() != null) {

                if (event.getMember().hasPermission(userContextMenu.requiredPermissions())) {

                    userContextMenu.performUserContextMenu(event);

                } else {

                    List<Permission> missingPermissions = new ArrayList<>();

                    userContextMenu.requiredPermissions().forEach(permission -> {

                        if (!event.getMember().hasPermission(permission))
                            missingPermissions.add(permission);

                    });

                    StringBuilder stringBuilder = new StringBuilder();

                    missingPermissions.forEach(permission -> stringBuilder
                            .append("> • ")
                            .append(permission.getName())
                            .append("\n"));

                    event.replyEmbeds(EmbedTemplates.issueEmbed("Du hast nicht alle nötigen Berechtigungen für diesen Befehl.\n> Dir fehlen folgende Berechtigungen:\n\n" + stringBuilder, false)).setEphemeral(true).queue();

                }

            } else {

                userContextMenu.performUserContextMenu(event);

            }

        }

        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException ignored) {
        }

        if (!event.isAcknowledged()) {

            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.copyFrom(EmbedTemplates.issueEmbed("Auf ein UserContextMenu wurde nicht reagiert.", false));
            embedBuilder.addField("CommandName", "```/" + Objects.requireNonNull(event.getName()).split("&id=")[0] + "```", true);
            embedBuilder.addField("User", "```" + event.getUser().getAsTag() + "```", true);
            embedBuilder.addField("Server", "```" + Objects.requireNonNull(event.getGuild()).getName() + "```", true);

            Logger logger = LoggerFactory.getLogger(StandardActionRowListener.class);
            logger.error("Keine Reaktion auf UserContextMenu");

            MainValues.owner.openPrivateChannel().queue(privateChannel -> privateChannel.sendMessageEmbeds(embedBuilder.build()).queue());
            event.replyEmbeds(EmbedTemplates.issueEmbed("Ein Unerwarteter Fehler ist aufgetreten.\n\nDer Fehler wurde an den Entwickler gesendet.", false)).setEphemeral(true).queue();

        }

    }

    @Override
    public void onMessageContextInteraction(@NotNull MessageContextInteractionEvent event) {

        String commandName = event.getName();

        MessageContextMenu messageContextMenu;

        if ((messageContextMenu = messageContextMenuMap.get(commandName)) != null) {

            if (messageContextMenu.requiredPermissions() != null) {

                if (event.getMember().hasPermission(messageContextMenu.requiredPermissions())) {

                    messageContextMenu.performMessageContextMenu(event);

                } else {

                    List<Permission> missingPermissions = new ArrayList<>();

                    messageContextMenu.requiredPermissions().forEach(permission -> {

                        if (!event.getMember().hasPermission(permission))
                            missingPermissions.add(permission);

                    });

                    StringBuilder stringBuilder = new StringBuilder();

                    missingPermissions.forEach(permission -> stringBuilder
                            .append("> • ")
                            .append(permission.getName())
                            .append("\n"));

                    event.replyEmbeds(EmbedTemplates.issueEmbed("Du hast nicht alle nötigen Berechtigungen für diesen Befehl.\n> Dir fehlen folgende Berechtigungen:\n\n" + stringBuilder, false)).setEphemeral(true).queue();

                }

            } else {

                messageContextMenu.performMessageContextMenu(event);

            }

        }

        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException ignored) {
        }

        if (!event.isAcknowledged()) {

            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.copyFrom(EmbedTemplates.issueEmbed("Auf ein MessageContextMenu wurde nicht reagiert.", false));
            embedBuilder.addField("CommandName", "```/" + Objects.requireNonNull(event.getName()).split("&id=")[0] + "```", true);
            embedBuilder.addField("User", "```" + event.getUser().getAsTag() + "```", true);
            embedBuilder.addField("Server", "```" + Objects.requireNonNull(event.getGuild()).getName() + "```", true);

            Logger logger = LoggerFactory.getLogger(StandardActionRowListener.class);
            logger.error("Keine Reaktion auf MessageContextMenu");

            MainValues.owner.openPrivateChannel().queue(privateChannel -> privateChannel.sendMessageEmbeds(embedBuilder.build()).queue());
            event.replyEmbeds(EmbedTemplates.issueEmbed("Ein Unerwarteter Fehler ist aufgetreten.\n\nDer Fehler wurde an den Entwickler gesendet.", false)).setEphemeral(true).queue();

        }

    }

}
