package de.thedesigncraft.mattibot.functions.youtube;

import de.thedesigncraft.mattibot.constants.methods.StandardActionRows;
import de.thedesigncraft.mattibot.manage.LiteSQL;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import org.jetbrains.annotations.NotNull;

public class YoutubeActionRowListener extends ListenerAdapter {

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {

        User user = event.getUser();

        if (StandardActionRows.proofButton(event, "youtube.setChannel", user)) {

            event.editMessageEmbeds(YoutubeEmbeds.setChannel()).setActionRows(ActionRow.of(YoutubeActionRows.goToMainPage(user))).queue();

        } else if (StandardActionRows.proofButton(event, "youtube.goToMainPage", user)) {

            if (YoutubeEmbeds.mainPage(event.getGuild()).getFields().get(0).getValue().equals("```Kein Kanal festgelegt.```")) {

                event.editMessageEmbeds(YoutubeEmbeds.mainPage(event.getGuild())).setActionRows(YoutubeActionRows.mainPageDisabled(event.getUser())).queue();

            } else {

                event.editMessageEmbeds(YoutubeEmbeds.mainPage(event.getGuild())).setActionRows(YoutubeActionRows.mainPage(event.getMember())).queue();

            }

        } else if (StandardActionRows.proofButton(event, "youtube.on", user)) {

            LiteSQL.onUpdate("UPDATE youtube SET active = 'true' WHERE guildid = " + event.getGuild().getIdLong());

            event.editMessageEmbeds(new EmbedBuilder(YoutubeEmbeds.mainPage(event.getGuild())).appendDescription("\n\n```✅ System aktiviert.```").build()).setActionRows(YoutubeActionRows.mainPage(event.getMember())).queue();

        } else if (StandardActionRows.proofButton(event, "youtube.off", user)) {

            LiteSQL.onUpdate("UPDATE youtube SET active = 'false' WHERE guildid = " + event.getGuild().getIdLong());

            event.editMessageEmbeds(new EmbedBuilder(YoutubeEmbeds.mainPage(event.getGuild())).appendDescription("\n\n```✅ System deaktiviert.```").build()).setActionRows(YoutubeActionRows.mainPage(event.getMember())).queue();

        } else if (StandardActionRows.proofButton(event, "youtube.setMessage", user)) {

            event.editMessageEmbeds(YoutubeEmbeds.setMessage()).setActionRows(ActionRow.of(YoutubeActionRows.goToMainPage(user))).queue();

        }

    }

}
