package de.thedesigncraft.mattibot.constants.methods;

import de.thedesigncraft.mattibot.constants.values.EmbedValues;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.time.OffsetDateTime;

public interface EmbedTemplates {

    static MessageEmbed issueEmbed(String issue, boolean footer) {

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("❌ Fehler");
        embedBuilder.setColor(EmbedValues.issueColor);
        embedBuilder.setDescription("> " + issue);

        if (footer) {

            embedBuilder.setFooter("Diese Nachricht wird in 10 Sekunden automatisch gelöscht.");

        }

        return embedBuilder.build();

    }

    static MessageEmbed standardEmbed(String title, String description) {

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(title);
        embedBuilder.setDescription(description);
        embedBuilder.setTimestamp(OffsetDateTime.now());
        embedBuilder.setFooter(EmbedValues.embedFooterText, EmbedValues.embedFooterPictureLink);
        embedBuilder.setColor(EmbedValues.standardColor);

        return embedBuilder.build();

    }

}
