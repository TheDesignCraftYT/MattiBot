package de.thedesigncraft.mattibot.functions.joinroles;

import de.thedesigncraft.mattibot.commands.types.ServerCommand;
import de.thedesigncraft.mattibot.constants.methods.EmbedTemplates;
import de.thedesigncraft.mattibot.constants.methods.MainMethods;
import de.thedesigncraft.mattibot.manage.ServerCommandManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;

public interface JoinRolesEmbeds {

    static MessageEmbed mainPage(Member member) {

        ServerCommand joinrolesCommand = ServerCommandManager.slashCommandsMap.get("joinroles");

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.copyFrom(EmbedTemplates.standardEmbed(joinrolesCommand.commandEmoji().getName() + " Beitritts Rollen", "Hier kannst du bestimmte Rollen festlegen, die alle Nutzer beim betreten des Servers automatisch bekommen."));

        StringBuilder stringBuilder = new StringBuilder();

        MainMethods.getJoinRoles(member.getGuild()).forEach(role -> stringBuilder
                .append("> • ")
                .append(role.getAsMention())
                .append("\n"));

        if (MainMethods.getJoinRoles(member.getGuild()).equals(member.getGuild().getRoles())) {

            embedBuilder.addField("Beitritts Rollen:", "```Keine Rollen```", true);

        } else {

            embedBuilder.addField("Beitritts Rollen:", stringBuilder.toString(), true);

        }

        return embedBuilder.build();

    }

    static MessageEmbed addChannels() {

        ServerCommand joinrolesCommand = ServerCommandManager.slashCommandsMap.get("joinroles");

        return EmbedTemplates.standardEmbed(joinrolesCommand.commandEmoji().getName() + " Beitritts Rollen", "Antworte auf diese Nachricht mit einer Rolle, um sie zu den Beitritts Rollen **__hinzuzufügen__**.");

    }

    static MessageEmbed removeChannels() {

        ServerCommand joinrolesCommand = ServerCommandManager.slashCommandsMap.get("whitelist");

        return EmbedTemplates.standardEmbed(joinrolesCommand.commandEmoji().getName() + " Beitritts Rollen", "Antworte auf diese Nachricht mit einer Rolle, um sie aus den Beitritts Rollen zu **__entfernen__**.");

    }

}
