package de.thedesigncraft.mattibot.functions.joinroles;

import de.thedesigncraft.mattibot.constants.methods.StandardActionRows;
import de.thedesigncraft.mattibot.manage.LiteSQL;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import org.jetbrains.annotations.NotNull;

public class JoinRolesActionRowListener extends ListenerAdapter {

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {

        User user = event.getUser();

        if (StandardActionRows.proofButton(event, "joinroles.addRoles", user)) {

            event.editMessageEmbeds(JoinRolesEmbeds.addChannels()).setActionRows(ActionRow.of(JoinRolesActionRows.goToMainPage(user))).queue();

        } else if (StandardActionRows.proofButton(event, "joinroles.goToMainPage", user)) {

            if (JoinRolesEmbeds.mainPage(event.getMember()).getFields().get(0).getValue().equals("```Keine Rollen```")) {

                event.editMessageEmbeds(JoinRolesEmbeds.mainPage(event.getMember())).setActionRows(JoinRolesActionRows.mainPageDisabled(event.getMember().getUser())).queue();

            } else {

                event.editMessageEmbeds(JoinRolesEmbeds.mainPage(event.getMember())).setActionRows(JoinRolesActionRows.mainPage(event.getMember().getUser())).queue();

            }

        } else if (StandardActionRows.proofButton(event, "joinroles.removeRoles", user)) {

            event.editMessageEmbeds(JoinRolesEmbeds.removeChannels()).setActionRows(ActionRow.of(JoinRolesActionRows.goToMainPage(user))).queue();

        } else if (StandardActionRows.proofButton(event, "joinroles.setNoRoles", user)) {

            LiteSQL.onUpdate("UPDATE joinroles SET roles = '' WHERE guildid = " + event.getGuild().getIdLong());

            if (JoinRolesEmbeds.mainPage(event.getMember()).getFields().get(0).getValue().equals("```Alle Kanäle```")) {

                event.editMessageEmbeds(new EmbedBuilder(JoinRolesEmbeds.mainPage(event.getMember())).appendDescription("\n\n```✅ Keine Beitritts Rollen.```").build()).setActionRows(JoinRolesActionRows.mainPageDisabled(event.getMember().getUser())).queue();

            } else {

                event.editMessageEmbeds(new EmbedBuilder(JoinRolesEmbeds.mainPage(event.getMember())).appendDescription("\n\n```✅ Keine Beitritts Rollen.```").build()).setActionRows(JoinRolesActionRows.mainPage(event.getMember().getUser())).queue();

            }

        }

    }

}
