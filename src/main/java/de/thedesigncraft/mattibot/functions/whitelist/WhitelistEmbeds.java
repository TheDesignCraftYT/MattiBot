package de.thedesigncraft.mattibot.functions.whitelist;

import de.thedesigncraft.mattibot.commands.types.ServerCommand;
import de.thedesigncraft.mattibot.constants.methods.EmbedTemplates;
import de.thedesigncraft.mattibot.constants.methods.MainMethods;
import de.thedesigncraft.mattibot.manage.ServerCommandManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;

public interface WhitelistEmbeds {

    static MessageEmbed mainPage(Member member) {

        ServerCommand whiteListCommand = ServerCommandManager.commandsMap.get("whitelist");

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.copyFrom(EmbedTemplates.standardEmbed(whiteListCommand.commandEmoji().getName() + " Kanal Whitelist", "Hier kannst du bestimmte Kanäle für Befehle freischalten.\nBenutzer mit Admin Berechtigungen können weiterhin jeden Kanal nutzen."));

        StringBuilder stringBuilder = new StringBuilder();

        MainMethods.getWhitelistedChannels(member.getGuild()).forEach(textChannel -> stringBuilder
                .append("> • ")
                .append(textChannel.getAsMention())
                .append("\n"));

        if (MainMethods.getWhitelistedChannels(member.getGuild()).equals(member.getGuild().getTextChannels())) {

            embedBuilder.addField("Freigeschaltete Kanäle:", "```Alle Kanäle```", true);

        } else {

            embedBuilder.addField("Freigeschaltete Kanäle:", stringBuilder.toString(), true);

        }

        return embedBuilder.build();

    }

    static MessageEmbed addChannels() {

        ServerCommand whiteListCommand = ServerCommandManager.commandsMap.get("whitelist");

        return EmbedTemplates.standardEmbed(whiteListCommand.commandEmoji().getName() + " Kanal Whitelist", "Antworte auf diese Nachricht mit einem Kanal, um ihn zu den gewhitelisteten Kanälen **__hinzuzufügen__**.");

    }

    static MessageEmbed removeChannels() {

        ServerCommand whiteListCommand = ServerCommandManager.commandsMap.get("whitelist");

        return EmbedTemplates.standardEmbed(whiteListCommand.commandEmoji().getName() + " Kanal Whitelist", "Antworte auf diese Nachricht mit einem Kanal, um ihn aus den gewhitelisteten Kanälen zu **__entfernen__**.");

    }
}
