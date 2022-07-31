package de.thedesigncraft.mattibot.constants.values;

import de.thedesigncraft.mattibot.MattiBot;
import net.dv8tion.jda.api.audit.AuditLogEntry;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import javax.management.MBeanAttributeInfo;
import java.util.ArrayList;
import java.util.List;

public interface MainValues {

    String name = MattiBot.jda.getSelfUser().getName();
    String shutdownCommand = "shutdown";
    int loopStartTime = 15;
    String activityText = "/help | %reachableUsers erreichbare Nutzer";
    String standardcommandPrefix = "M.";

    User owner = MattiBot.jda.retrieveUserById(810769870521106464L).complete();
    String projectName = "MattiBot";
    TextChannel youtubeChannel = MattiBot.jda.getGuildById(961974918617108511L).getTextChannelById(1002934247604093050L);

    User youtubeBot = MattiBot.jda.retrieveUserById(204255221017214977L).complete();

    static List<TextChannel> updateChannels() {

        List<TextChannel> returnList = new ArrayList<>();

        returnList.add(MattiBot.jda.getGuildById(995733324884623410L).getTextChannelById(997799689858601040L));

        return returnList;

    }

    TextChannel githubLog = MattiBot.jda.getGuildById(995733324884623410L).getTextChannelById(999682448474513438L);

}
