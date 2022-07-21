package de.thedesigncraft.mattibot.functions.help.methods;

import de.thedesigncraft.mattibot.MattiBot;
import de.thedesigncraft.mattibot.commands.types.ServerCommand;
import de.thedesigncraft.mattibot.constants.methods.EmbedTemplates;
import de.thedesigncraft.mattibot.constants.methods.ServerCommandMethods;
import de.thedesigncraft.mattibot.constants.values.EmbedValues;
import de.thedesigncraft.mattibot.constants.values.commands.ServerCommands;
import de.thedesigncraft.mattibot.manage.LiteSQL;
import de.thedesigncraft.mattibot.manage.ServerCommandManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.interactions.commands.OptionType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public interface HelpEmbeds {

    static MessageEmbed category(String category, Guild guild) {

        EmbedBuilder embedBuilder = new EmbedBuilder();

        List<ServerCommand> categoryCommands = new ArrayList<>();
        ServerCommands.serverCommands().forEach(serverCommand -> {

            if (serverCommand.category().equals(category)) {

                categoryCommands.add(serverCommand);

            }

        });

        categoryCommands.forEach(serverCommand -> {

            if (serverCommand.slashCommand()) {

                embedBuilder.addField(serverCommand.commandEmoji().getName() + " /" + ServerCommandMethods.getCommandName(serverCommand), "```" + serverCommand.description() + "```", true);

            } else {

                ResultSet prefix = LiteSQL.onQuery("SELECT prefix FROM prefix WHERE guildid = " + guild.getIdLong());

                try {
                    String prefix1 = prefix.getString("prefix");

                    embedBuilder.addField(serverCommand.commandEmoji().getName() + " " + prefix1 + ServerCommandMethods.getCommandName(serverCommand), "```" + serverCommand.description() + "```", true);

                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

            }

        });

        embedBuilder.setTitle(category);
        embedBuilder.setColor(EmbedValues.standardColor);
        embedBuilder.setFooter(EmbedValues.embedFooterText, EmbedValues.embedFooterPictureLink);
        embedBuilder.setTimestamp(OffsetDateTime.now());

        return embedBuilder.build();

    }

    static MessageEmbed command(ServerCommand command, TextChannel textChannel) {

        EmbedBuilder embedBuilder = new EmbedBuilder();

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("```➤ ");

        ResultSet prefix = LiteSQL.onQuery("SELECT prefix FROM prefix WHERE guildid = " + textChannel.getGuild().getIdLong());

        String commandPrefix = null;
        try {
            commandPrefix = prefix.getString("prefix");

            if (command.slashCommand()) {

                commandPrefix = "/";

                stringBuilder.append(commandPrefix);

                embedBuilder.setTitle(command.category() + " » " + command.commandEmoji().getName() + " " + commandPrefix + ServerCommandMethods.getCommandName(command));

                stringBuilder
                        .append(ServerCommandMethods.getCommandName(command))
                        .append("\n");

                if (command.options() != null) {

                    String finalCommandPrefix1 = commandPrefix;
                    command.options().forEach(optionData -> {

                        stringBuilder
                                .append("➤ ")
                                .append(finalCommandPrefix1)
                                .append(ServerCommandMethods.getCommandName(command))
                                .append(" [(")
                                .append(optionData.getType().name())
                                .append(") ")
                                .append(optionData.getName())
                                .append("]\n");

                    });

                }

                stringBuilder.append("```");

            } else {

                stringBuilder.append(commandPrefix);

                embedBuilder.setTitle(command.category() + " » " + command.commandEmoji().getName() + " " + commandPrefix + ServerCommandMethods.getCommandName(command));

                stringBuilder
                        .append(ServerCommandMethods.getCommandName(command))
                        .append("\n");

                if (command.options() != null) {

                    String finalCommandPrefix = commandPrefix;

                    command.options().forEach(optionData -> {

                        stringBuilder
                                .append("➤ ")
                                .append(finalCommandPrefix)
                                .append(ServerCommandMethods.getCommandName(command))
                                .append(" [(")
                                .append(optionData.getType().name())
                                .append(") ")
                                .append(optionData.getName())
                                .append("]\n");

                    });

                }

                stringBuilder.append("```");

            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        StringBuilder stringBuilder1 = new StringBuilder();

        stringBuilder1.append("```➤ ");

        if (command.slashCommand()) {

            commandPrefix = "/";

            stringBuilder1.append(commandPrefix);

            stringBuilder1
                    .append(ServerCommandMethods.getCommandName(command))
                    .append("\n");

            if (command.options() != null) {

                String finalCommandPrefix1 = commandPrefix;
                command.options().forEach(optionData -> {

                    stringBuilder1
                            .append("➤ ")
                            .append(finalCommandPrefix1)
                            .append(ServerCommandMethods.getCommandName(command))
                            .append(" ");

                    if (optionData.getChoices().isEmpty()) {

                        if (optionData.getType().equals(OptionType.CHANNEL)) {

                            stringBuilder1.append(textChannel.getAsMention());

                        } else if (optionData.getType().equals(OptionType.BOOLEAN)) {

                            stringBuilder1.append("true");

                        } else if (optionData.getType().equals(OptionType.INTEGER)) {

                            stringBuilder1.append("20");

                        } else if (optionData.getType().equals(OptionType.STRING)) {

                            stringBuilder1.append("hello!");

                        } else if (optionData.getType().equals(OptionType.ROLE)) {

                            stringBuilder1.append(textChannel.getGuild().getRoles().get(0).getAsMention());

                        } else if (optionData.getType().equals(OptionType.USER)) {

                            stringBuilder1.append(textChannel.getGuild().getMembers().get(0).getAsMention());

                        }

                        stringBuilder1.append("\n");

                    } else {

                        stringBuilder1
                                .append(optionData.getChoices().get(0).getName())
                                .append("\n");

                    }

                });

            }

            stringBuilder1.append("```");

        } else {

            stringBuilder1.append(commandPrefix);

            stringBuilder1
                    .append(ServerCommandMethods.getCommandName(command))
                    .append("\n");

            if (command.options() != null) {

                String finalCommandPrefix = commandPrefix;

                command.options().forEach(optionData -> {

                    stringBuilder
                            .append("➤ ")
                            .append(finalCommandPrefix)
                            .append(ServerCommandMethods.getCommandName(command))
                            .append(" ");

                    if (optionData.getChoices().isEmpty()) {

                        if (optionData.getType().equals(OptionType.CHANNEL)) {

                            stringBuilder1.append(textChannel.getAsMention());

                        } else if (optionData.getType().equals(OptionType.BOOLEAN)) {

                            stringBuilder1.append("true");

                        } else if (optionData.getType().equals(OptionType.INTEGER)) {

                            stringBuilder1.append("20");

                        } else if (optionData.getType().equals(OptionType.STRING)) {

                            stringBuilder1.append("hello!");

                        } else if (optionData.getType().equals(OptionType.ROLE)) {

                            stringBuilder1.append(textChannel.getGuild().getRoles().get(0).getAsMention());

                        } else if (optionData.getType().equals(OptionType.USER)) {

                            stringBuilder1.append(textChannel.getGuild().getMembers().get(0).getAsMention());

                        }

                    } else {

                        stringBuilder1
                                .append(optionData.getChoices().get(0).getName())
                                .append("\n");

                    }

                });

            }

            stringBuilder1.append("```");

        }

        embedBuilder.addField("Anwendung", stringBuilder.toString(), true);
        embedBuilder.addField("Beispiele", stringBuilder1.toString(), true);

        if (command.requiredPermissions() != null) {

            StringBuilder stringBuilder2 = new StringBuilder();

            stringBuilder2.append("```");

            command.requiredPermissions().forEach(permission -> {

                stringBuilder2
                        .append("• ")
                        .append(permission.getName())
                        .append("\n");

            });

            stringBuilder2.append("```");

            embedBuilder.addField("Erforderliche Berechtigungen", stringBuilder2.toString(), false);

        }

        embedBuilder.setDescription(command.description());
        embedBuilder.setColor(EmbedValues.standardColor);
        embedBuilder.setFooter(EmbedValues.embedFooterText, EmbedValues.embedFooterPictureLink);
        embedBuilder.setTimestamp(OffsetDateTime.now());

        return embedBuilder.build();

    }

    static MessageEmbed mainPage() {

        ServerCommand helpServerCommand = ServerCommandManager.commandsMap.get("help");

        return EmbedTemplates.standardEmbed(helpServerCommand.commandEmoji().getName() + " Hilfe", "Willkommen in der Hilfe-Station des " + MattiBot.jda.getSelfUser().getAsMention() + "-Bots.\n\nDu kannst entweder unten eine Kategorie auswählen, oder diesen Befehl nochmal ausführen und dabei hinter den Befehlsnamen den Namen einer Kategorie oder eines Befehls schreiben.");

    }

}
