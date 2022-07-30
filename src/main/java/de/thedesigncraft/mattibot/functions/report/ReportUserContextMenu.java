package de.thedesigncraft.mattibot.functions.report;

import de.thedesigncraft.mattibot.constants.methods.EmbedTemplates;
import de.thedesigncraft.mattibot.constants.methods.MainMethods;
import de.thedesigncraft.mattibot.constants.values.commands.Categories;
import de.thedesigncraft.mattibot.constants.values.commands.Versions;
import de.thedesigncraft.mattibot.contextmenus.types.UserContextMenu;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.UserContextInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Modal;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;

import java.util.List;

public class ReportUserContextMenu implements UserContextMenu {
    @Override
    public String version() {
        return Versions.versions().get("v100a7");
    }

    @Override
    public String description() {
        return "Meldet einen Nutzer an das ServerTeam.";
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
    public void performUserContextMenu(UserContextInteractionEvent event) {

        if (MainMethods.reportSystemActive(event.getGuild())) {

            TextInput user = TextInput.create("user", "Nutzer", TextInputStyle.SHORT)
                    .setPlaceholder("Der Nutzer, den du melden möchtest.")
                    .setValue(event.getTarget().getAsTag() + " (" + event.getTarget().getIdLong() + ")")
                    .setRequired(true)
                    .build();

            TextInput reason = TextInput.create("reason", "Grund", TextInputStyle.PARAGRAPH)
                    .setPlaceholder("Der Grund, warum du diesen Nutzer melden möchtest.")
                    .setRequired(true)
                    .setMinLength(30)
                    .build();

            Modal modal = Modal.create("reportSystem.reportUser", "Nutzer Meldung")
                    .addActionRows(ActionRow.of(user), ActionRow.of(reason))
                    .build();

            event.replyModal(modal).queue();

        } else {

            event.replyEmbeds(EmbedTemplates.issueEmbed("Das ReportSystem ist auf diesem Server deaktiviert.", false)).setEphemeral(true).queue();

        }

    }
}
