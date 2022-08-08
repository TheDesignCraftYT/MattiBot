package de.thedesigncraft.mattibot.commands.types;

import de.thedesigncraft.mattibot.constants.methods.EmbedTemplates;
import de.thedesigncraft.mattibot.constants.methods.StandardActionRows;
import de.thedesigncraft.mattibot.manage.ServerCommandManager;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenu;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public interface OneSelectionMenu extends ServerCommand {

    MessageEmbed mainPage();

    String[] settings();

    ConcurrentHashMap<String, String[]> choices();

    MessageEmbed choiceEmbed(String name, String[] args);

    default List<OptionData> options() {

        List<OptionData> returnList = new ArrayList<>();

        List<Command.Choice> choices = new ArrayList<>();

        choices().forEach((s, strings) -> choices.add(new Command.Choice(s, s)));

        returnList.add(new OptionData(OptionType.STRING, settings()[1], settings()[2]).addChoices(choices));

        return returnList;

    }

    default void performGuildMessageCommand(MessageReceivedEvent event) {

        try {

            String arg = event.getMessage().getContentDisplay().split(" ")[1];

            ConcurrentHashMap<String, String[]> targets = targets(choices(), arg);

            if (targets.isEmpty()) {

                event.getMessage().replyEmbeds(replyEmbed(arg, targets, settings(), mainPage(), null)).setActionRows(actionRow(event.getAuthor(), arg, choices(), settings())).mentionRepliedUser(false).queue();

            } else {

                String key = targets.keySet().stream().findFirst().get();

                event.getMessage().replyEmbeds(replyEmbed(arg, targets, settings(), mainPage(), choiceEmbed(key, targets.get(key)))).setActionRows(actionRow(event.getAuthor(), arg, choices(), settings())).mentionRepliedUser(false).queue();

            }

        } catch (ArrayIndexOutOfBoundsException e) {

            event.getMessage().replyEmbeds(replyEmbed(null, targets(choices(), null), settings(), mainPage(), null)).setActionRows(actionRow(event.getAuthor(), null, choices(), settings())).mentionRepliedUser(false).queue();

        }

    }

    default void performSlashCommand(SlashCommandInteractionEvent event) {

        if (event.getOption(settings()[1]) != null) {

            String arg = event.getOption(settings()[1]).getAsString();

            ConcurrentHashMap<String, String[]> targets = targets(choices(), arg);

            String key = targets.keySet().stream().findFirst().get();

            event.replyEmbeds(replyEmbed(arg, targets, settings(), mainPage(), choiceEmbed(key, targets.get(key)))).addActionRows(actionRow(event.getUser(), arg, choices(), settings())).queue();

        } else {

            event.replyEmbeds(replyEmbed(null, targets(choices(), null), settings(), mainPage(), null)).addActionRows(actionRow(event.getUser(), null, choices(), settings())).queue();

        }

    }

    static MessageEmbed replyEmbed(String arg, ConcurrentHashMap<String, String[]> targets, String[] settings, MessageEmbed mainPage, MessageEmbed choiceEmbed) {

        if (targets.size() > 1)
            return EmbedTemplates.issueEmbed("Ein unerwarteter Fehler ist aufgetreten.", false);

        if (targets.size() == 0) {

            if (arg != null) {

                return EmbedTemplates.issueEmbed("Das ist kein registrierter Wert für die Option '" + settings[1] + "'.", false);

            } else {

                return mainPage;

            }

        }

        return choiceEmbed;

    }

    static List<ActionRow> actionRow(User user, String arg, ConcurrentHashMap<String, String[]> choices, String[] settings) {

        ConcurrentHashMap<String, String[]> targets = targets(choices, arg);

        if (targets.size() > 1)
            return Collections.singletonList(ActionRow.of(StandardActionRows.cancelButton(user)));

        if (targets.size() == 0) {

            if (arg != null) {

                return Collections.singletonList(ActionRow.of(StandardActionRows.cancelButton(user)));

            } else {

                List<ActionRow> returnList = new ArrayList<>();

                List<SelectOption> options = new ArrayList<>();

                choices.forEach((s, strings) -> options.add(SelectOption.of(s, s)));

                returnList.add(ActionRow.of(SelectMenu.create(settings[0] + "Menu&id=" + user.getIdLong()).addOptions(options).build()));

                returnList.add(ActionRow.of(StandardActionRows.cancelButton(user)));

                return returnList;

            }

        }

        return Collections.singletonList(ActionRow.of(Button.of(ButtonStyle.SECONDARY, settings[0] + ".goToMainPage&id=" + user.getIdLong(), "Zurück", Emoji.fromUnicode("U+25c0"))));

    }

    static ConcurrentHashMap<String, String[]> targets(ConcurrentHashMap<String, String[]> choices, String arg) {

        ConcurrentHashMap<String, String[]> targets = new ConcurrentHashMap<>();

        choices.forEach((s, strings) -> {

            if (s.equalsIgnoreCase(arg)) {

                targets.put(s, strings);

            }

        });

        return targets;

    }

    static void onButtonInteraction(ButtonInteractionEvent event) {

        ServerCommand serverCommand = ServerCommandManager.slashCommandsMap.get(event.getButton().getId().replace(".goToMainPage", "").toLowerCase().split("&id=")[0]);

        OneSelectionMenu oneSelectionMenu = (OneSelectionMenu) serverCommand;

        if (oneSelectionMenu != null && StandardActionRows.proofButton(event, event.getButton().getId().split("&id=")[0], event.getUser())) {

            event.editMessageEmbeds(oneSelectionMenu.mainPage()).setActionRows(actionRow(event.getUser(), null, oneSelectionMenu.choices(), oneSelectionMenu.settings())).queue();

        }

    }

    static void onSelectMenuInteraction(SelectMenuInteractionEvent event) {

        ServerCommand serverCommand = ServerCommandManager.slashCommandsMap.get(event.getSelectMenu().getId().replace("Menu", "").toLowerCase().split("&id=")[0]);

        OneSelectionMenu oneSelectionMenu = (OneSelectionMenu) serverCommand;

        if (oneSelectionMenu != null && StandardActionRows.proofSelectMenu(event, event.getSelectMenu().getId().split("&id=")[0], event.getUser())) {

            String arg = event.getSelectedOptions().get(0).getValue();

            ConcurrentHashMap<String, String[]> targets = targets(oneSelectionMenu.choices(), arg);

            event.editMessageEmbeds(replyEmbed(arg, targets, oneSelectionMenu.settings(), oneSelectionMenu.mainPage(), oneSelectionMenu.choiceEmbed(targets.keySet().stream().findFirst().get(), targets.get(targets.keySet().stream().findFirst().get())))).setActionRows(actionRow(event.getUser(), arg, oneSelectionMenu.choices(), oneSelectionMenu.settings())).queue();

        }

    }

}
