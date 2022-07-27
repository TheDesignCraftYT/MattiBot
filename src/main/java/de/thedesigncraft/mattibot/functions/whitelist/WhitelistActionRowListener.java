package de.thedesigncraft.mattibot.functions.whitelist;

import de.thedesigncraft.mattibot.constants.methods.StandardActionRows;
import de.thedesigncraft.mattibot.manage.LiteSQL;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import org.jetbrains.annotations.NotNull;

public class WhitelistActionRowListener extends ListenerAdapter {

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {

        User user = event.getUser();

        if (StandardActionRows.proofButton(event, "whitelist.addChannels", user)) {

            event.editMessageEmbeds(WhitelistEmbeds.addChannels()).setActionRows(ActionRow.of(WhitelistActionRows.goToMainPage(user))).queue();

        } else if (StandardActionRows.proofButton(event, "whitelist.goToMainPage", user)) {

            if (WhitelistEmbeds.mainPage(event.getMember()).getFields().get(0).getValue().equals("```Alle Kanäle```")) {

                event.editMessageEmbeds(WhitelistEmbeds.mainPage(event.getMember())).setActionRows(WhitelistActionRows.mainPageDisabled(event.getMember().getUser())).queue();

            } else {

                event.editMessageEmbeds(WhitelistEmbeds.mainPage(event.getMember())).setActionRows(WhitelistActionRows.mainPage(event.getMember().getUser())).queue();

            }

        } else if (StandardActionRows.proofButton(event, "whitelist.removeChannels", user)) {

            event.editMessageEmbeds(WhitelistEmbeds.removeChannels()).setActionRows(ActionRow.of(WhitelistActionRows.goToMainPage(user))).queue();

        } else if (StandardActionRows.proofButton(event, "whitelist.setAllChannels", user)) {

            LiteSQL.onUpdate("UPDATE whitelistedChannels SET channels = '' WHERE guildid = " + event.getGuild().getIdLong());

            if (WhitelistEmbeds.mainPage(event.getMember()).getFields().get(0).getValue().equals("```Alle Kanäle```")) {

                event.editMessageEmbeds(new EmbedBuilder(WhitelistEmbeds.mainPage(event.getMember())).appendDescription("\n\n```✅ Alle Kanäle freigeschaltet.```").build()).setActionRows(WhitelistActionRows.mainPageDisabled(event.getMember().getUser())).queue();

            } else {

                event.editMessageEmbeds(new EmbedBuilder(WhitelistEmbeds.mainPage(event.getMember())).appendDescription("\n\n```✅ Alle Kanäle freigeschaltet.```").build()).setActionRows(WhitelistActionRows.mainPage(event.getMember().getUser())).queue();

            }

        }

    }

}
