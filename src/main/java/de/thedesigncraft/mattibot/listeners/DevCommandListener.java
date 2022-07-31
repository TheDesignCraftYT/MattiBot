package de.thedesigncraft.mattibot.listeners;

import de.thedesigncraft.mattibot.MattiBot;
import de.thedesigncraft.mattibot.constants.methods.EmbedTemplates;
import de.thedesigncraft.mattibot.constants.methods.MainMethods;
import de.thedesigncraft.mattibot.constants.values.MainValues;
import de.thedesigncraft.mattibot.constants.values.commands.Versions;
import de.thedesigncraft.mattibot.manage.LiteSQL;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class DevCommandListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {

        if (!event.isFromType(ChannelType.PRIVATE))
            return;

        if (event.getChannel().asPrivateChannel().getUser().equals(MainValues.owner)) {

            if (event.getMessage().getContentDisplay().startsWith("dev.")) {

                executeCommand(event, event.getMessage().getContentDisplay().replaceFirst("dev.", ""));

            }

        }

    }

    private void executeCommand(MessageReceivedEvent event, String command) {

        if (command.startsWith("calculate")) {

            calculate(event);

        } else if (command.startsWith("sendNewUpdateMessage")) {

            sendNewUpdateMessage(event);

        } else if (command.startsWith("test")) {

            test(event);

        } else {

            event.getMessage().replyEmbeds(EmbedTemplates.issueEmbed("Das ist kein registrierter DevCommand.", false)).queue();

        }

    }

    private void test(MessageReceivedEvent event) {

        event.getMessage().reply("Test").queue();

    }

    private void sendNewUpdateMessage(MessageReceivedEvent event) {

        try {

            String arg = event.getMessage().getContentDisplay().split(" ")[1];

            String version = Versions.versions().get(arg);

            NewUpdateListener.sendNewUpdate(version, false);

        } catch (ArrayIndexOutOfBoundsException e) {

            NewUpdateListener.sendNewUpdate(Versions.currentVersion(), false);

        }

    }

    private void calculate(MessageReceivedEvent event) {

        String rechnung = event.getMessage().getContentDisplay().replaceFirst("dev.calculate ", "");

        event.getMessage().replyEmbeds(EmbedTemplates.standardEmbed("Rechnung", "Summe für:\n```" + rechnung + "```\nSumme:\n```" + MainMethods.sumOf(rechnung) + "```")).mentionRepliedUser(false).queue();

    }

}
