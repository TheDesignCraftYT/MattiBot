package de.thedesigncraft.mattibot.functions.report.settings;

import de.thedesigncraft.mattibot.commands.types.ServerCommand;
import de.thedesigncraft.mattibot.constants.methods.EmbedTemplates;
import de.thedesigncraft.mattibot.constants.methods.MainMethods;
import de.thedesigncraft.mattibot.manage.ServerCommandManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;

public interface ReportSystemEmbeds {

    static MessageEmbed mainPage(Guild guild) {

        ServerCommand reportSystemCommand = ServerCommandManager.slashCommandsMap.get("reportsystem");

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.copyFrom(EmbedTemplates.standardEmbed(reportSystemCommand.commandEmoji().getName() + " ReportSystem Einstellungen", "Hier kannst du das Report-System de-/aktivieren und den Kanal für Reports festlegen."));

        embedBuilder.addField("ReportChannel", MainMethods.getReportChannelString(guild), true);

        return embedBuilder.build();

    }

    static MessageEmbed setChannel() {

        ServerCommand reportSystemCommand = ServerCommandManager.slashCommandsMap.get("reportsystem");

        return EmbedTemplates.standardEmbed(reportSystemCommand.commandEmoji().getName() + " ReportSystem Einstellungen", "Antworte auf diese Nachricht mit einem Kanal, um ihn als ReportSystem-Kanal festzulegen.");

    }

}
