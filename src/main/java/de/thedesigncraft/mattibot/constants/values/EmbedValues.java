package de.thedesigncraft.mattibot.constants.values;

import de.thedesigncraft.mattibot.MattiBot;

public interface EmbedValues {

    int standardColor = 0xffffff;

    String embedFooterPictureLink = MattiBot.jda.getSelfUser().getEffectiveAvatarUrl();

    String embedFooterText = "Powered by " + MainValues.name;

    int issueColor = 0xff5555;

}
