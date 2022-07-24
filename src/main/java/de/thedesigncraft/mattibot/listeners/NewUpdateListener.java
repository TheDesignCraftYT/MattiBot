package de.thedesigncraft.mattibot.listeners;

import de.thedesigncraft.mattibot.commands.types.ServerCommand;
import de.thedesigncraft.mattibot.constants.methods.EmbedTemplates;
import de.thedesigncraft.mattibot.constants.methods.ServerCommandMethods;
import de.thedesigncraft.mattibot.constants.values.MainValues;
import de.thedesigncraft.mattibot.constants.values.UpdateFunctions;
import de.thedesigncraft.mattibot.constants.values.commands.ServerCommands;
import de.thedesigncraft.mattibot.constants.values.commands.Versions;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class NewUpdateListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {

        if (!event.isFromGuild())
            return;

        if (event.getGuild().getIdLong() == MainValues.githubLog.getGuild().getIdLong() &&
                event.getChannel().asTextChannel().getIdLong() == MainValues.githubLog.getIdLong() &&
                event.getMessage().getAuthor().getName().equals("GitHub") &&
                !event.getMessage().getEmbeds().isEmpty()) {

            String[] titleArgs = event.getMessage().getEmbeds().get(0).getTitle().split(" ");

            if (titleArgs[0].split("/")[1].startsWith(MainValues.projectName) &&
                    titleArgs[1].equals("New") &&
                    titleArgs[2].equals("release") &&
                    titleArgs[3].equals("published:")) {

                sendNewUpdate(Versions.currentVersion());

            }

        }

    }

    public static void sendNewUpdate(String version) {

        EmbedBuilder embedBuilder = new EmbedBuilder(EmbedTemplates.standardEmbed("Neue Version: " + version, "Es wurde wieder fleißig am Bot weitergearbeitet und es sind einige neue Funktionen hinzugekommen :sparkles:\n\nEine ausführliche Beschreibung des Updates bekommst du [hier](https://github.com/TheDesignCraftYT/" + MainValues.projectName + "/releases/tag/" + version + ")."));

        List<ServerCommand> newCommands = new ArrayList<>();
        List<String[]> newFunctions = new ArrayList<>();

        ServerCommands.serverCommands().forEach(serverCommand -> {

            if (serverCommand.version().equals(version)) {

                newCommands.add(serverCommand);

            }

        });

        UpdateFunctions.updateFunctions().forEach(strings -> {

            if (strings[0].equals(version)) {

                newFunctions.add(new String[]{strings[1], strings[2]});

            }

        });

        if (!newCommands.isEmpty()) {

            StringBuilder stringBuilder = new StringBuilder();

            newCommands.forEach(serverCommand -> {

                stringBuilder.append("➤ `");

                if (serverCommand.slashCommand()) {

                    stringBuilder.append("/");

                } else {

                    stringBuilder.append(MainValues.standardcommandPrefix);

                }

                stringBuilder
                        .append(ServerCommandMethods.getCommandName(serverCommand))
                        .append("` - ")
                        .append(serverCommand.description())
                        .append("\n");

            });

            embedBuilder.addField("Neue Befehle", stringBuilder.toString(), true);

        }

        if (!newFunctions.isEmpty()) {

            StringBuilder stringBuilder = new StringBuilder();

            newFunctions.forEach(strings -> {

                stringBuilder.append("➤ `").append(strings[0]).append("` - ").append(strings[1]).append("\n");

            });

            embedBuilder.addField("Neue Funktionen", stringBuilder.toString(), true);

        }

        MainValues.updateChannels().forEach(messageChannel -> messageChannel.sendMessageEmbeds(embedBuilder.build()).queue());

        StringBuilder stringBuilder = new StringBuilder();

        MainValues.updateChannels().forEach(textChannel -> stringBuilder
                .append("➤ ")
                .append(textChannel.getName())
                .append(" (")
                .append(textChannel.getGuild().getName())
                .append(")\n"));

        MainValues.owner.openPrivateChannel().queue(privateChannel -> privateChannel.sendMessageEmbeds(new EmbedBuilder(EmbedTemplates.standardEmbed("Neues Update", "Folgendes UpdateEmbed wurde an alle festgelegten Channel verschickt:")).addField("UpdateChannel", "```" + stringBuilder + "```", false).build(), embedBuilder.build()).queue());

    }

}
