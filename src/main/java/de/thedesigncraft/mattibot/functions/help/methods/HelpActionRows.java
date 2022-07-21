package de.thedesigncraft.mattibot.functions.help.methods;

import de.thedesigncraft.mattibot.commands.types.ServerCommand;
import de.thedesigncraft.mattibot.constants.methods.ServerCommandMethods;
import de.thedesigncraft.mattibot.constants.methods.StandardActionRows;
import de.thedesigncraft.mattibot.constants.values.commands.Categories;
import de.thedesigncraft.mattibot.constants.values.commands.ServerCommands;
import de.thedesigncraft.mattibot.manage.LiteSQL;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenu;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface HelpActionRows {

    static List<ActionRow> category(String category, long userId, Guild guild) {
        List<Button> buttons = new ArrayList<>();
        List<ServerCommand> categoryCommands = new ArrayList<>();
        ServerCommands.serverCommands().forEach(serverCommand -> {

            if(serverCommand.category().equals(category)) {

                categoryCommands.add(serverCommand);

            }

        });

        categoryCommands.forEach(serverCommand -> {

            if(serverCommand.slashCommand()) {

                buttons.add(Button.of(ButtonStyle.SUCCESS, "help.goToCommand&command=" + ServerCommandMethods.getCommandName(serverCommand) + "&id=" + userId, "/" + ServerCommandMethods.getCommandName(serverCommand), serverCommand.commandEmoji()));

            } else {

                ResultSet prefix = LiteSQL.onQuery("SELECT prefix FROM prefix WHERE guildid = " + guild.getIdLong());

                try {
                    String prefix1 = prefix.getString("prefix");

                    buttons.add(Button.of(ButtonStyle.SUCCESS, "help.goToCommand&command=" + ServerCommandMethods.getCommandName(serverCommand) + "&id=" + userId, prefix1 + ServerCommandMethods.getCommandName(serverCommand), serverCommand.commandEmoji()));

                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

            }

        });

        List<ActionRow> returnList = new ArrayList<>();

        if (buttons.size() > 5) {

            returnList.add(ActionRow.of(buttons.subList(0, 5)));

            if (buttons.size() == 6) {

                returnList.add(ActionRow.of(buttons.get(5)));

            } else if (buttons.size() > 6 && buttons.size() <= 10) {

                returnList.add(ActionRow.of(buttons.subList(6, buttons.size())));

            } else if (buttons.size() == 11) {

                returnList.add(ActionRow.of(buttons.get(10)));

            } else if (buttons.size() > 11 && buttons.size() <= 15) {

                returnList.add(ActionRow.of(buttons.subList(6, 10)));
                returnList.add(ActionRow.of(buttons.subList(11, buttons.size())));
                
            }

        } else {

            returnList.add(ActionRow.of(buttons));

        }

        returnList.add(ActionRow.of(Button.of(ButtonStyle.SECONDARY, "help.goToMainPage&id=" + userId, "Zurück", Emoji.fromUnicode("U+25c0"))));

        return returnList;

    }

    static Button command(ServerCommand serverCommand, long userId) {

        return Button.of(ButtonStyle.SECONDARY, "help.goToCategory&category=" + serverCommand.category() + "&id=" + userId, "Zurück", Emoji.fromUnicode("U+25c0"));

    }

    static SelectMenu allCommands(User user) {

        List<SelectOption> selectOptions = new ArrayList<>();

        Categories.categories().forEach((s, s2) -> selectOptions.add(SelectOption.of(s2, s)));

        return SelectMenu.create("help.goToCategory&id=" + user.getId()).addOptions(selectOptions).setMaxValues(1).setPlaceholder("Welche Kategorie möchtest du sehen?").build();

    }

    static List<ActionRow> mainPage(User user) {

        List<ActionRow> returnList = new ArrayList<>();
        returnList.add(ActionRow.of(allCommands(user)));
        returnList.add(ActionRow.of(StandardActionRows.cancelButton(user)));

        return returnList;

    }

}
