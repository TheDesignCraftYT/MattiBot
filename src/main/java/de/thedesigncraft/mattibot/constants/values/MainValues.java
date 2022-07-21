package de.thedesigncraft.mattibot.constants.values;

import de.thedesigncraft.mattibot.MattiBot;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.util.ArrayList;
import java.util.List;

public interface MainValues {

    String name = MattiBot.jda.getSelfUser().getName();
    String shutdownCommand = "shutdown";
    int loopStartTime = 15;
    String activityText = "/help | %reachableUsers erreichbare Nutzer";
    String standardcommandPrefix = "M.";
    User owner = MattiBot.jda.retrieveUserById(810769870521106464L).complete();

    static List<MessageChannel> updateChannels() {

        List<MessageChannel> returnList = new ArrayList<>();

        returnList.add(MattiBot.jda.getGuildById(995733324884623410L).getTextChannelById(997799689858601040L));

        return returnList;

    }

}
