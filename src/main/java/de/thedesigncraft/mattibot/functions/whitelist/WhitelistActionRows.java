package de.thedesigncraft.mattibot.functions.whitelist;

import de.thedesigncraft.mattibot.constants.methods.StandardActionRows;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;

import java.util.ArrayList;
import java.util.List;

public interface WhitelistActionRows {

    static List<ActionRow> mainPage(User user) {

        List<ActionRow> returnList = new ArrayList<>();

        returnList.add(ActionRow.of(

                Button.of(

                        ButtonStyle.PRIMARY,
                        "whitelist.addChannels&id=" + user.getIdLong(),
                        "Kanäle hinzufügen"

                ),

                Button.of(

                        ButtonStyle.PRIMARY,
                        "whitelist.removeChannels&id=" + user.getIdLong(),
                        "Kanäle entfernen"

                ),

                Button.of(

                        ButtonStyle.PRIMARY,
                        "whitelist.setAllChannels&id=" + user.getIdLong(),
                        "Alle Kanäle freischalten"

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
                        "whitelist.addChannels&id=" + user.getIdLong(),
                        "Kanäle hinzufügen"

                ),

                Button.of(

                        ButtonStyle.PRIMARY,
                        "whitelist.removeChannels&id=" + user.getIdLong(),
                        "Kanäle entfernen"

                ).asDisabled(),

                Button.of(

                        ButtonStyle.PRIMARY,
                        "whitelist.setAllChannels&id=" + user.getIdLong(),
                        "Alle Kanäle freischalten"

                ).asDisabled()

        ));

        returnList.add(ActionRow.of(

                StandardActionRows.cancelButton(user)

        ));

        return returnList;

    }

    static Button goToMainPage(User user) {

        return Button.of(ButtonStyle.SECONDARY, "whitelist.goToMainPage&id=" + user.getIdLong(), "Zurück", Emoji.fromUnicode("U+2B05"));

    }

}
