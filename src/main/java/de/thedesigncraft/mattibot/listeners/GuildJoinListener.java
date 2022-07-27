package de.thedesigncraft.mattibot.listeners;

import de.thedesigncraft.mattibot.constants.values.MainValues;
import de.thedesigncraft.mattibot.manage.LiteSQL;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class GuildJoinListener extends ListenerAdapter {

    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent event) {

        sqlValues(event.getGuild());

    }

    public static void sqlValues(Guild guild) {

        LiteSQL.onUpdate("INSERT INTO prefix(guildid, prefix) VALUES(" + guild.getIdLong() + ", '" + MainValues.standardcommandPrefix + "')");
        LiteSQL.onUpdate("INSERT INTO whitelistedChannels(guildid) VALUES(" + guild.getIdLong() + ")");
        LiteSQL.onUpdate("INSERT INTO joinroles(guildid) VALUES(" + guild.getIdLong() + ")");

    }

}
