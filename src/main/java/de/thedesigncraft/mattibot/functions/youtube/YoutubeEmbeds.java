package de.thedesigncraft.mattibot.functions.youtube;

import de.thedesigncraft.mattibot.commands.types.ServerCommand;
import de.thedesigncraft.mattibot.constants.methods.EmbedTemplates;
import de.thedesigncraft.mattibot.constants.methods.MainMethods;
import de.thedesigncraft.mattibot.manage.ServerCommandManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;

public interface YoutubeEmbeds {

    static MessageEmbed mainPage(Guild guild) {

        ServerCommand youtubeCommand = ServerCommandManager.slashCommandsMap.get("youtube");

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.copyFrom(EmbedTemplates.standardEmbed(youtubeCommand.commandEmoji().getName() + " Youtube Benachrichtigungen", "Hier kannst du die Youtube Benachrichtigungen de-/aktivieren, einen Kanal einstellen und eine eigene Nachricht festlegen."));

        embedBuilder.addField("Benachrichtigungs-Kanal", MainMethods.getYoutubeChannelString(guild), true);

        embedBuilder.addField("Benachrichtigungs-Nachricht", MainMethods.getYoutubeMessage(guild), true);

        if (MainMethods.youtubeActive(guild)) {

            embedBuilder.addField("SystemStatus", "```Aktiviert```", true);

        } else {

            embedBuilder.addField("SystemStatus", "```Deaktiviert```", true);

        }

        return embedBuilder.build();

    }

    static MessageEmbed setChannel() {

        ServerCommand youtubeCommand = ServerCommandManager.slashCommandsMap.get("youtube");

        return EmbedTemplates.standardEmbed(youtubeCommand.commandEmoji().getName() + " Youtube Benachrichtigungen", "Antworte auf diese Nachricht mit einem Kanal, um ihn als Benachrichtigungs-Kanal festzulegen.");

    }

    static MessageEmbed setMessage() {

        ServerCommand youtubeCommand = ServerCommandManager.slashCommandsMap.get("youtube");

        return EmbedTemplates.standardEmbed(youtubeCommand.commandEmoji().getName() + " Youtube Benachrichtigungen", "Antworte auf diese Nachricht mit der Nachricht, die bei neuen Benachrichtigungen angezeigt werden soll.\n\nDu kannst für die Nachricht folgende Placeholder nutzen:\n```\n• {%channel_name}\n• {%video_link}\n```");

    }

}
