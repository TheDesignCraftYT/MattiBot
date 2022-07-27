package de.thedesigncraft.mattibot.functions.help;

import de.thedesigncraft.mattibot.commands.types.ServerCommand;
import de.thedesigncraft.mattibot.constants.methods.EmbedTemplates;
import de.thedesigncraft.mattibot.constants.methods.StandardActionRows;
import de.thedesigncraft.mattibot.contextmenus.types.MessageContextMenu;
import de.thedesigncraft.mattibot.contextmenus.types.UserContextMenu;
import de.thedesigncraft.mattibot.functions.help.methods.HelpActionRows;
import de.thedesigncraft.mattibot.functions.help.methods.HelpEmbeds;
import de.thedesigncraft.mattibot.manage.ServerCommandManager;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class HelpActionRowListener extends ListenerAdapter {

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {

        User user = event.getUser();

        if (StandardActionRows.proofButton(event, "help.goToCommand", user)) {

            String arg = event.getButton().getId().split("&")[1].replace("slashCommand=", "");

            ServerCommand slashCommand = ServerCommandManager.slashCommandsMap.get(arg);

            UserContextMenu userContextMenu = ServerCommandManager.userContextMenuMap.get(arg);

            MessageContextMenu messageContextMenu = ServerCommandManager.messageContextMenuMap.get(arg);

            if (slashCommand != null) {

                event.editMessageEmbeds(HelpEmbeds.slashCommand(slashCommand, event.getChannel().asTextChannel())).setActionRow(HelpActionRows.command(slashCommand.category(), event.getMember().getIdLong())).queue();

            } else if (userContextMenu != null) {

                event.editMessageEmbeds(HelpEmbeds.userCommand(userContextMenu)).setActionRow(HelpActionRows.command(userContextMenu.category(), event.getMember().getIdLong())).queue();

            } else if (messageContextMenu != null) {

                event.editMessageEmbeds(HelpEmbeds.messageCommand(messageContextMenu)).setActionRow(HelpActionRows.command(messageContextMenu.category(), event.getMember().getIdLong())).queue();

            } else {

                event.editMessageEmbeds(EmbedTemplates.issueEmbed("Ein unerwarteter Fehler ist aufgetreten.\n\nWenn dies passiert, melde es bitte umgehend dem Entwickler des Bots.", true)).queue();

            }

        } else if (StandardActionRows.proofButton(event, "help.goToCategory", user)) {

            String arg = event.getButton().getId().split("&")[1].replace("category=", "");

            event.editMessageEmbeds(HelpEmbeds.category(arg, event.getGuild())).setActionRows(HelpActionRows.category(arg, user.getIdLong(), event.getGuild())).queue();

        } else if (StandardActionRows.proofButton(event, "help.goToMainPage", user)) {

            event.editMessageEmbeds(HelpEmbeds.mainPage()).setActionRows(HelpActionRows.mainPage(user)).queue();

        }

    }

    @Override
    public void onSelectMenuInteraction(@NotNull SelectMenuInteractionEvent event) {

        if(StandardActionRows.proofSelectMenu(event, "help.goToCategory", event.getUser())) {

            String arg = event.getSelectedOptions().get(0).getLabel();

            event.editMessageEmbeds(HelpEmbeds.category(arg, event.getGuild())).setActionRows(HelpActionRows.category(arg, event.getUser().getIdLong(), event.getGuild())).queue();

        }

    }
}
