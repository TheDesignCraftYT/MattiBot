package de.thedesigncraft.mattibot.functions.report;

import de.thedesigncraft.mattibot.constants.methods.EmbedTemplates;
import de.thedesigncraft.mattibot.constants.methods.MainMethods;
import de.thedesigncraft.mattibot.constants.values.commands.Categories;
import de.thedesigncraft.mattibot.constants.values.commands.Versions;
import de.thedesigncraft.mattibot.contextmenus.types.MessageContextMenu;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.MessageContextInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Modal;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;

import java.util.List;

public class ReportMessageContextMenu implements MessageContextMenu {
    @Override
    public String version() {
        return Versions.versions().get("v100a7");
    }

    @Override
    public String description() {
        return "Meldet eine Nachricht an das ServerTeam.";
    }

    @Override
    public String category() {
        return Categories.categories().get("report");
    }

    @Override
    public Emoji commandEmoji() {
        return Emoji.fromUnicode("U+1F6D1");
    }

    @Override
    public List<Permission> requiredPermissions() {
        return null;
    }

    @Override
    public void performMessageContextMenu(MessageContextInteractionEvent event) {

        if (MainMethods.reportSystemActive(event.getGuild())) {

            TextInput messageWithContent = TextInput.create("message", "Nachricht", TextInputStyle.PARAGRAPH)
                    .setPlaceholder("Die Nachricht, die du melden möchtest.")
                    .setValue(event.getTarget().getAuthor().getAsTag() + " (" + event.getTarget().getAuthor().getIdLong() + ") \n\n" + event.getTarget().getContentDisplay())
                    .setRequired(true)
                    .build();

            TextInput messageWithoutContent = TextInput.create("message", "Nachricht", TextInputStyle.PARAGRAPH)
                    .setPlaceholder("Die Nachricht, die du melden möchtest.")
                    .setValue(event.getTarget().getAuthor().getAsTag() + " (" + event.getTarget().getAuthor().getIdLong() + ") \n\n" + "Diese Nachricht ist leer.")
                    .setRequired(true)
                    .build();

            TextInput reason = TextInput.create("reason", "Grund", TextInputStyle.PARAGRAPH)
                    .setPlaceholder("Der Grund, warum du diese Nachricht melden möchtest.")
                    .setRequired(true)
                    .setMinLength(30)
                    .build();

            if (!event.getTarget().getContentDisplay().equals(" ") && !event.getTarget().getContentDisplay().equals("")) {

                Modal modal = Modal.create("reportSystem.reportMessage", "Nachricht Meldung")
                        .addActionRows(ActionRow.of(messageWithContent), ActionRow.of(reason))
                        .build();

                event.replyModal(modal).queue();

            } else {

                Modal modal = Modal.create("reportSystem.reportMessage", "Nachricht Meldung")
                        .addActionRows(ActionRow.of(messageWithoutContent), ActionRow.of(reason))
                        .build();

                event.replyModal(modal).queue();

            }

        } else {

            event.replyEmbeds(EmbedTemplates.issueEmbed("Das ReportSystem ist auf diesem Server deaktiviert.", false)).setEphemeral(true).queue();

        }

    }
}
