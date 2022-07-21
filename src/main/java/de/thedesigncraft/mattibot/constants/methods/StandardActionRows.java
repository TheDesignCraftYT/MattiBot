package de.thedesigncraft.mattibot.constants.methods;

import de.thedesigncraft.mattibot.constants.values.EmbedValues;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;

import java.util.Objects;

public interface StandardActionRows {

    static Button cancelButton(User user) {

        return Button.of(ButtonStyle.SECONDARY, "cancel&id=" + user.getId(), "Abbrechen");

    }

    static boolean proofButton(ButtonInteractionEvent buttonInteractionEvent, String proofActionRowId, User proofActionRowUser) {

        String[] actionRowIdTiles = Objects.requireNonNull(buttonInteractionEvent.getButton().getId()).replace("&id=", "III").split("III");

        if(actionRowIdTiles[0].split("&")[0].equals(proofActionRowId)) {

            if(proofActionRowUser.getId().equals(actionRowIdTiles[1])) {

                return true;

            } else {

                buttonInteractionEvent.replyEmbeds(EmbedTemplates.issueEmbed("Nur **<@" + actionRowIdTiles[1] + ">** hat Zugriff auf diesen Button.", false)).setEphemeral(true).queue();

            }

        }

        return false;

    }

    static boolean proofSelectMenu(SelectMenuInteractionEvent selectMenuInteractionEvent, String proofActionRowId, User proofActionRowUser) {

        String[] actionRowIdTiles = Objects.requireNonNull(selectMenuInteractionEvent.getSelectMenu().getId()).replace("&id=", "III").split("III");

        if(actionRowIdTiles[0].split("&")[0].equals(proofActionRowId)) {

            if(proofActionRowUser.getId().equals(actionRowIdTiles[1])) {

                return true;

            } else {

                selectMenuInteractionEvent.replyEmbeds(EmbedTemplates.issueEmbed("Nur **<@" + actionRowIdTiles[1] + ">** hat Zugriff auf diesen Button.", false)).setEphemeral(true).queue();

            }

        }

        return false;

    }

}
