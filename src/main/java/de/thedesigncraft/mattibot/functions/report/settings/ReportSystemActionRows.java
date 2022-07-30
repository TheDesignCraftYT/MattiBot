package de.thedesigncraft.mattibot.functions.report.settings;

import de.thedesigncraft.mattibot.constants.methods.StandardActionRows;
import de.thedesigncraft.mattibot.manage.LiteSQL;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.ItemComponent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface ReportSystemActionRows {

    static List<ActionRow> mainPageDisabled(User user) {

        List<ActionRow> returnList = new ArrayList<>();

        returnList.add(ActionRow.of(

                Button.of(

                        ButtonStyle.PRIMARY,
                        "reportSystem.setChannel&id=" + user.getIdLong(),
                        "Kanal festlegen"

                ),

                Button.of(

                        ButtonStyle.PRIMARY,
                        "reportSystem.on&id=" + user.getIdLong(),
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

        actionRow.add(Button.of(

                ButtonStyle.PRIMARY,
                "reportSystem.setChannel&id=" + member.getUser().getIdLong(),
                "Kanal festlegen"

        ));

        ResultSet reportSystem = LiteSQL.onQuery("SELECT active FROM reportSystem WHERE guildid = " + member.getGuild().getIdLong());

        try {
            boolean active = reportSystem.getBoolean("active");

            if (active) {

                actionRow.add(Button.of(

                        ButtonStyle.PRIMARY,
                        "reportSystem.off&id=" + member.getUser().getIdLong(),
                        "System deaktivieren"

                ));

            } else {

                actionRow.add(Button.of(

                        ButtonStyle.PRIMARY,
                        "reportSystem.on&id=" + member.getUser().getIdLong(),
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

        return Button.of(ButtonStyle.SECONDARY, "reportSystem.goToMainPage&id=" + user.getIdLong(), "Zurück", Emoji.fromUnicode("U+2B05"));

    }
}
