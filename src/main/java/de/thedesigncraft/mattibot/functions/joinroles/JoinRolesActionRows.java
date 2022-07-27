package de.thedesigncraft.mattibot.functions.joinroles;

import de.thedesigncraft.mattibot.constants.methods.StandardActionRows;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;

import java.util.ArrayList;
import java.util.List;

public interface JoinRolesActionRows {

    static List<ActionRow> mainPage(User user) {

        List<ActionRow> returnList = new ArrayList<>();

        returnList.add(ActionRow.of(

                Button.of(

                        ButtonStyle.PRIMARY,
                        "joinroles.addRoles&id=" + user.getIdLong(),
                        "Rollen hinzufügen"

                ),

                Button.of(

                        ButtonStyle.PRIMARY,
                        "joinroles.removeRoles&id=" + user.getIdLong(),
                        "Rollen entfernen"

                ),

                Button.of(

                        ButtonStyle.PRIMARY,
                        "joinroles.setNoRoles&id=" + user.getIdLong(),
                        "Keine Beitritts Rollen"

                )

        ));

        returnList.add(ActionRow.of(

                StandardActionRows.cancelButton(user)

        ));

        return returnList;

    }

    static List<ActionRow> mainPageDisabled(User user) {

        List<ActionRow> returnList = new ArrayList<>();

        returnList.add(ActionRow.of(

                Button.of(

                        ButtonStyle.PRIMARY,
                        "joinroles.addRoles&id=" + user.getIdLong(),
                        "Rollen hinzufügen"

                ),

                Button.of(

                        ButtonStyle.PRIMARY,
                        "joinroles.removeRoles&id=" + user.getIdLong(),
                        "Rollen entfernen"

                ).asDisabled(),

                Button.of(

                        ButtonStyle.PRIMARY,
                        "joinroles.setNoRoles&id=" + user.getIdLong(),
                        "Keine Beitritts Rollen"

                ).asDisabled()

        ));

        returnList.add(ActionRow.of(

                StandardActionRows.cancelButton(user)

        ));

        return returnList;

    }

    static Button goToMainPage(User user) {

        return Button.of(ButtonStyle.SECONDARY, "joinroles.goToMainPage&id=" + user.getIdLong(), "Zurück", Emoji.fromUnicode("U+2B05"));

    }

}
