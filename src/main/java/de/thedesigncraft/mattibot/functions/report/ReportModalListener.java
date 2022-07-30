package de.thedesigncraft.mattibot.functions.report;

import de.thedesigncraft.mattibot.commands.types.ServerCommand;
import de.thedesigncraft.mattibot.constants.methods.EmbedTemplates;
import de.thedesigncraft.mattibot.constants.methods.MainMethods;
import de.thedesigncraft.mattibot.manage.ServerCommandManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.Date;

public class ReportModalListener extends ListenerAdapter {

    @Override
    public void onModalInteraction(@NotNull ModalInteractionEvent event) {

        if (event.getModalId().equals("reportSystem.reportUser")) {

            if (MainMethods.reportSystemActive(event.getGuild())) {

                try {

                    ServerCommand reportSystemCommand = ServerCommandManager.slashCommandsMap.get("reportsystem");

                    EmbedBuilder embedBuilder = new EmbedBuilder(EmbedTemplates.standardEmbed(reportSystemCommand.commandEmoji().getName() + " Neue Meldung", "Es wurde eine neue Meldung eingereicht."));

                    String informations =
                            "__Gemeldet von__: " + event.getUser().getAsMention() + "\n" +
                            "__Typ__: " + "`USER`" + "\n" +
                            "__Zeitpunkt__: " + "<t:" + Date.from(Instant.now()).getTime() / 1000L + ":R>" + "\n";

                    embedBuilder.addField("Generelle Informationen", informations, false);

                    embedBuilder.addField("Gemeldet", event.getGuild().getMemberById(event.getValue("user").getAsString().split(" ")[1].replace("(", "").replace(")", "")).getAsMention(), true);

                    embedBuilder.addField("Channel", event.getChannel().getAsMention(), false);

                    embedBuilder.addField("Angegebener Grund", "```" + event.getValue("reason").getAsString() + "```", false);

                    MainMethods.getReportChannel(event.getGuild()).sendMessageEmbeds(embedBuilder.build()).queue();

                    event.replyEmbeds(EmbedTemplates.standardEmbed(reportSystemCommand.commandEmoji().getName() + " Deine Meldung", "Folgende Meldung wurde an das ServerTeam weitergeleitet."), embedBuilder.build()).setEphemeral(true).queue();

                } catch (NumberFormatException e) {

                    event.replyEmbeds(EmbedTemplates.issueEmbed("Das ist kein Nutzer.", false)).setEphemeral(true).queue();

                }

            } else {

                event.replyEmbeds(EmbedTemplates.issueEmbed("Das ReportSystem ist auf diesem Server deaktiviert.", false)).setEphemeral(true).queue();

            }

        } else if (event.getModalId().equals("reportSystem.reportMessage")) {

            if (MainMethods.reportSystemActive(event.getGuild())) {

                try {

                    ServerCommand reportSystemCommand = ServerCommandManager.slashCommandsMap.get("reportsystem");

                    EmbedBuilder embedBuilder = new EmbedBuilder(EmbedTemplates.standardEmbed(reportSystemCommand.commandEmoji().getName() + " Neue Meldung", "Es wurde eine neue Meldung eingereicht."));

                    String informations =
                            "__Gemeldet von__: " + event.getUser().getAsMention() + "\n" +
                            "__Typ__: " + "`MESSAGE`" + "\n" +
                            "__Zeitpunkt__: " + "<t:" + Date.from(Instant.now()).getTime() / 1000L + ":R>" + "\n";

                    embedBuilder.addField("Generelle Informationen", informations, false);

                    String id = event.getValue("message").getAsString().split(" ")[1].replace("(", "").replace(")", "");

                    embedBuilder.addField("Author der gemeldeten Nachricht", event.getGuild().getMemberById(id).getAsMention(), true);

                    embedBuilder.addField("Channel", event.getChannel().getAsMention(), false);

                    StringBuilder stringBuilder = new StringBuilder();

                    String[] strings = event.getValue("message").getAsString().split(" ");

                    int i = 2;

                    while (i < strings.length) {

                        stringBuilder
                                .append(strings[i])
                                .append(" ");

                        i = i + 1;

                    }

                    embedBuilder.addField("Nachricht", "```" + stringBuilder + "```", false);

                    embedBuilder.addField("Angegebener Grund", "```" + event.getValue("reason").getAsString() + "```", false);

                    MainMethods.getReportChannel(event.getGuild()).sendMessageEmbeds(embedBuilder.build()).queue();

                    event.replyEmbeds(EmbedTemplates.standardEmbed(reportSystemCommand.commandEmoji().getName() + " Deine Meldung", "Folgende Meldung wurde an das ServerTeam weitergeleitet."), embedBuilder.build()).setEphemeral(true).queue();

                } catch (NumberFormatException e) {

                    event.replyEmbeds(EmbedTemplates.issueEmbed("Das ist kein Nutzer.", false)).setEphemeral(true).queue();

                }

            } else {

                event.replyEmbeds(EmbedTemplates.issueEmbed("Das ReportSystem ist auf diesem Server deaktiviert.", false)).setEphemeral(true).queue();

            }

        }

    }
}
