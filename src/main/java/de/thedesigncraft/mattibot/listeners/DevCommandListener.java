package de.thedesigncraft.mattibot.listeners;

import de.thedesigncraft.mattibot.commands.types.ServerCommand;
import de.thedesigncraft.mattibot.constants.methods.EmbedTemplates;
import de.thedesigncraft.mattibot.constants.methods.MainMethods;
import de.thedesigncraft.mattibot.constants.methods.ServerCommandMethods;
import de.thedesigncraft.mattibot.constants.values.MainValues;
import de.thedesigncraft.mattibot.constants.values.commands.ServerCommands;
import de.thedesigncraft.mattibot.constants.values.commands.Versions;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class DevCommandListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {

        if (!event.isFromType(ChannelType.PRIVATE))
            return;

        if (event.getPrivateChannel().getUser().equals(MainValues.owner)) {

            if (event.getMessage().getContentDisplay().startsWith("dev.")) {

                executeCommand(event, event.getMessage().getContentDisplay().replaceFirst("dev.", ""));

            }

        }

    }

    private void executeCommand(MessageReceivedEvent event, String command) {

        if (command.startsWith("newUpdate")) {

            newUpdate(event);

        } else if (command.startsWith("calculate")) {

            calculate(event);

        } else if (command.startsWith("updateInfos")) {

            newUpdateInfos(event);

        } else {

            event.getMessage().replyEmbeds(EmbedTemplates.issueEmbed("Das ist kein registrierter DevCommand.", false)).queue();

        }

    }

    private void newUpdateInfos(MessageReceivedEvent event) {

        event.getMessage().replyEmbeds(EmbedTemplates.standardEmbed("New Update Command Infos", "```dev.newUpdate [name;beschreibung],[name;beschreibung]```")).queue();

    }

    private void newUpdate(MessageReceivedEvent event) {

        String newFunctionsString = event.getMessage().getContentDisplay().replaceFirst("dev.newUpdate", "").replaceFirst(" ", "");

        ConcurrentHashMap<String, String> newFunctions = new ConcurrentHashMap<>();
        List<ServerCommand> newCommands = new ArrayList<>();

        ServerCommands.serverCommands().forEach(serverCommand -> {

            if (serverCommand.version().equals(Versions.currentVersion())) {

                newCommands.add(serverCommand);

            }

        });

        if (!newFunctionsString.equals("") && !newFunctionsString.equals(" ")) {

            String[] newFunctionsStringSplitted = newFunctionsString.split(",");

            int i = newFunctionsStringSplitted.length - 1;

            while (i + 1 != 0) {

                String newFunction = newFunctionsStringSplitted[i];

                String[] newFunctionSplitted = newFunction.replace("[", "").replace("]", "").split(";");

                newFunctions.put(newFunctionSplitted[0], newFunctionSplitted[1]);

                i = i - 1;

            }

        }

        EmbedBuilder embedBuilder = new EmbedBuilder(EmbedTemplates.standardEmbed("Neue Version: " + Versions.currentVersion(), "Es wurde wieder fleißig am Bot weitergearbeitet und es sind einige neue Funktionen hinzugekommen :sparkles:"));

        StringBuilder stringBuilder = new StringBuilder();

        newCommands.forEach(serverCommand -> {

            stringBuilder.append("➤ `");

            if (serverCommand.slashCommand()) {

                stringBuilder.append("/");

            } else {

                stringBuilder.append("III");

            }

            stringBuilder
                    .append(ServerCommandMethods.getCommandName(serverCommand))
                    .append("` - ")
                    .append(serverCommand.description())
                    .append("\n");

        });

        embedBuilder.addField("Neue Befehle", stringBuilder.toString(), true);

        StringBuilder stringBuilder1 = new StringBuilder();

        newFunctions.forEach((s, s2) -> {

            stringBuilder1
                    .append("➤ `")
                    .append(s)
                    .append("` - ")
                    .append(s2)
                    .append("\n");

        });

        if (!stringBuilder1.toString().equals("") && !stringBuilder1.toString().equals(" ")) {

            embedBuilder.addField("Neue Funktionen", stringBuilder1.toString(), true);

        }

        if (newCommands.isEmpty() && newFunctions.isEmpty()) {

            event.getMessage().replyEmbeds(EmbedTemplates.issueEmbed("Es müssen neue Funktionen oder Commands vorhanden sein.", false)).mentionRepliedUser(false).queue();

        } else {

            MainValues.updateChannels().forEach(textChannel -> {

                textChannel.sendMessageEmbeds(embedBuilder.build()).queue();
                event.getMessage().reply("Folgende UpdateNachricht wurde erfolgreich an alle festgelegten Channel verschickt:").setEmbeds(embedBuilder.build()).mentionRepliedUser(false).queue();

            });

        }

    }

    private void calculate(MessageReceivedEvent event) {

        String rechnung = event.getMessage().getContentDisplay().replaceFirst("dev.calculate ", "");

        event.getMessage().replyEmbeds(EmbedTemplates.standardEmbed("Rechnung", "Summe für:\n```" + rechnung + "```\nSumme:\n```" + MainMethods.sumOf(rechnung) + "```")).mentionRepliedUser(false).queue();

    }

}
