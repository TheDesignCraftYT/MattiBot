package de.thedesigncraft.mattibot.listeners;

import de.thedesigncraft.mattibot.commands.types.ServerCommand;
import de.thedesigncraft.mattibot.constants.methods.EmbedTemplates;
import de.thedesigncraft.mattibot.constants.methods.ServerCommandMethods;
import de.thedesigncraft.mattibot.constants.values.MainValues;
import de.thedesigncraft.mattibot.constants.values.UpdateFunctions;
import de.thedesigncraft.mattibot.constants.values.commands.ServerCommands;
import de.thedesigncraft.mattibot.constants.values.commands.Versions;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Webhook;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class NewUpdateListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {

        List<Webhook> webhooks = new ArrayList<>();

        event.getChannel().asTextChannel().retrieveWebhooks().queue(webhooks::addAll);

        if (event.getGuild().getIdLong() == MainValues.githubLog.getGuild().getIdLong() &&
                event.getChannel().asTextChannel().getIdLong() == MainValues.githubLog.getIdLong() &&
                event.getMessage().getAuthor().getName().equals(webhooks.get(0).getName()) &&
                !event.getMessage().getEmbeds().isEmpty() &&
                event.getMessage().getEmbeds().get(0).getTitle().split(" ")[0].split("/")[1].equals(MainValues.projectName) &&
                event.getMessage().getEmbeds().get(0).getTitle().split(" ")[1].equals("New") &&
                event.getMessage().getEmbeds().get(0).getTitle().split(" ")[2].equals("release") &&
                event.getMessage().getEmbeds().get(0).getTitle().split(" ")[3].equals("published:") &&
                event.getMessage().getEmbeds().get(0).getTitle().split(" ")[4].equals(Versions.currentVersion())) {

            EmbedBuilder embedBuilder = new EmbedBuilder(EmbedTemplates.standardEmbed("Neue Version: " + Versions.currentVersion(), "Es wurde wieder fleißig am Bot weitergearbeitet und es sind einige neue Funktionen hinzugekommen :sparkles:\n\nEine ausführliche Beschreibung des Updates bekommst du [hier](https://github.com/TheDesignCraftYT/" + MainValues.projectName + "/releases/tag/" + Versions.currentVersion() + ")"));

            List<ServerCommand> newCommands = new ArrayList<>();
            List<String[]> newFunctions = new ArrayList<>();

            ServerCommands.serverCommands().forEach(serverCommand -> {

                if (serverCommand.version().equals(Versions.currentVersion())) {

                    newCommands.add(serverCommand);

                }

            });

            UpdateFunctions.updateFunctions().forEach((s, strings) -> {

                if (s.equals(Versions.currentVersion())) {

                    newFunctions.add(strings);

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

            MainValues.owner.openPrivateChannel().queue(privateChannel -> privateChannel.sendMessageEmbeds(new EmbedBuilder(EmbedTemplates.standardEmbed("Neues Update", "Folgendes UpdateEmbed wurde an alle festgelegten Channel verschickt:")).addField("UpdateChannel", "```" + stringBuilder + "```", false).build()).queue());

        }

    }
}
