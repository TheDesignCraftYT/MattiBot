package de.thedesigncraft.mattibot.functions.youtube;

import de.thedesigncraft.mattibot.MattiBot;
import de.thedesigncraft.mattibot.constants.methods.MainMethods;
import de.thedesigncraft.mattibot.constants.values.MainValues;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class YoutubeListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {

        if (event.getChannel().getIdLong() == MainValues.youtubeChannel.getIdLong() && event.getAuthor().getIdLong() == MainValues.youtubeBot.getIdLong()) {

            MattiBot.jda.getGuilds().forEach(guild -> {

                if (MainMethods.youtubeActive(guild)) {

                    String channelName = event.getMessage().getContentDisplay().replace("**", "III").split("III")[1];

                    String youtubeLinkPrefix = "https://www.youtube.com/watch";

                    String videoLink = youtubeLinkPrefix + event.getMessage().getContentDisplay().split(youtubeLinkPrefix)[1];

                    String messageText = MainMethods.getYoutubeMessage(guild).replace("```", "").replace("{%channel_name}", channelName).replace("{%video_link}", videoLink);

                    MainMethods.getYoutubeChannel(guild).sendMessage(messageText).queue();

                }

            });

        }

    }
}
