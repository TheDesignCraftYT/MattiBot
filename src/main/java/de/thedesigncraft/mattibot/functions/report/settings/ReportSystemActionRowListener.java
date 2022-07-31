package de.thedesigncraft.mattibot.functions.report.settings;

import de.thedesigncraft.mattibot.constants.methods.StandardActionRows;
import de.thedesigncraft.mattibot.manage.LiteSQL;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import org.jetbrains.annotations.NotNull;

public class ReportSystemActionRowListener extends ListenerAdapter {

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {

        User user = event.getUser();

        if (StandardActionRows.proofButton(event, "reportSystem.setChannel", user)) {

            event.editMessageEmbeds(ReportSystemEmbeds.setChannel()).setActionRows(ActionRow.of(ReportSystemActionRows.goToMainPage(user))).queue();

        } else if (StandardActionRows.proofButton(event, "reportSystem.goToMainPage", user)) {

            if (ReportSystemEmbeds.mainPage(event.getGuild()).getFields().get(0).getValue().equals("```Kein Kanal festgelegt.```")) {

                event.editMessageEmbeds(ReportSystemEmbeds.mainPage(event.getGuild())).setActionRows(ReportSystemActionRows.mainPageDisabled(event.getUser())).queue();

            } else {

                event.editMessageEmbeds(ReportSystemEmbeds.mainPage(event.getGuild())).setActionRows(ReportSystemActionRows.mainPage(event.getMember())).queue();

            }

        } else if (StandardActionRows.proofButton(event, "reportSystem.on", user)) {

            LiteSQL.onUpdate("UPDATE reportSystem SET active = " + true + " WHERE guildid = " + event.getGuild().getIdLong());

            event.editMessageEmbeds(new EmbedBuilder(ReportSystemEmbeds.mainPage(event.getGuild())).appendDescription("\n\n```✅ System aktiviert.```").build()).setActionRows(ReportSystemActionRows.mainPage(event.getMember())).queue();

        } else if (StandardActionRows.proofButton(event, "reportSystem.off", user)) {

            LiteSQL.onUpdate("UPDATE reportSystem SET active = " + false + " WHERE guildid = " + event.getGuild().getIdLong());

            event.editMessageEmbeds(new EmbedBuilder(ReportSystemEmbeds.mainPage(event.getGuild())).appendDescription("\n\n```✅ System deaktiviert.```").build()).setActionRows(ReportSystemActionRows.mainPage(event.getMember())).queue();

        }

    }

}
