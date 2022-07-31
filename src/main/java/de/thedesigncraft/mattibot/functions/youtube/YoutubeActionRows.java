package de.thedesigncraft.mattibot.functions.youtube;

import de.thedesigncraft.mattibot.constants.methods.StandardActionRows;
import de.thedesigncraft.mattibot.manage.LiteSQL;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface YoutubeActionRows {

    static List<ActionRow> mainPageDisabled(User user) {

        List<ActionRow> returnList = new ArrayList<>();

        returnList.add(ActionRow.of(

                Button.of(

                        ButtonStyle.PRIMARY,
                        "youtube.setChannel&id=" + user.getIdLong(),
                        "Kanal festlegen"

                ),

                Button.of(

                        ButtonStyle.PRIMARY,
                        "youtube.setMessage&id=" + user.getIdLong(),
                        "Nachricht festlegen"

                ),

                Button.of(

                        ButtonStyle.PRIMARY,
                        "youtube.on&id=" + user.getIdLong(),
                        "System aktivieren"

                ).asDisabled()

        ));

        returnList.add(ActionRow.of(

                StandardActionRows.cancelButton(user)

        ));

        return returnList;

    }

    static List<ActionRow> mainPage(Member member) {

        List<ActionRow> returnList = new ArrayList<>();

        List<Button> actionRow = new ArrayList<>();

        actionRow.add(

                Button.of(

                        ButtonStyle.PRIMARY,
                        "youtube.setChannel&id=" + member.getUser().getIdLong(),
                        "Kanal festlegen"

                )

        );

        actionRow.add(

                Button.of(

                        ButtonStyle.PRIMARY,
                        "youtube.setMessage&id=" + member.getUser().getIdLong(),
                        "Nachricht festlegen"

                )

        );

        ResultSet youtube = LiteSQL.onQuery("SELECT active FROM youtube WHERE guildid = " + member.getGuild().getIdLong());

        try {

            boolean active = Boolean.parseBoolean(youtube.getString("active"));

            if (active) {

                actionRow.add(Button.of(

                        ButtonStyle.PRIMARY,
                        "youtube.off&id=" + member.getUser().getIdLong(),
                        "System deaktivieren"

                ));

            } else {

                actionRow.add(Button.of(

                        ButtonStyle.PRIMARY,
                        "youtube.on&id=" + member.getUser().getIdLong(),
                        "System aktivieren"

                ));

            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        returnList.add(ActionRow.of(actionRow));

        returnList.add(ActionRow.of(

                StandardActionRows.cancelButton(member.getUser())

        ));

        return returnList;

    }

    static Button goToMainPage(User user) {

        return Button.of(ButtonStyle.SECONDARY, "youtube.goToMainPage&id=" + user.getIdLong(), "Zurück", Emoji.fromUnicode("U+2B05"));

    }
}
