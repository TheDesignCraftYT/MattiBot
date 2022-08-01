package de.thedesigncraft.mattibot.functions.joinroles;

import de.thedesigncraft.mattibot.constants.methods.MainMethods;
import de.thedesigncraft.mattibot.manage.LiteSQL;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.exceptions.HierarchyException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MemberJoinListener extends ListenerAdapter {

    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {

        try {

            MainMethods.getJoinRoleDatas(event.getGuild()).forEach(role -> {

                try {

                    event.getGuild().addRoleToMember(event.getMember(), role).queue();

                } catch (HierarchyException e) {

                    if (MainMethods.getJoinRoleDatas(event.getGuild()) != null && MainMethods.getJoinRoleDatas(event.getGuild()).contains(role)) {

                        StringBuilder stringBuilder = new StringBuilder();

                        if (MainMethods.getJoinRoleDatas(event.getGuild()) != null) {

                            MainMethods.getJoinRoleDatas(event.getGuild()).forEach(role1 -> {

                                if (!role1.getId().equals(role.getId())) {

                                    stringBuilder
                                            .append(role1.getIdLong())
                                            .append(",");

                                }

                            });

                        }

                        LiteSQL.onUpdate("UPDATE joinroles SET roles = '" + stringBuilder + "' WHERE guildid = " + event.getGuild().getIdLong());

                    } else {

                        Logger logger = LoggerFactory.getLogger(MemberJoinListener.class);
                        logger.error("Unerwarteter Fehler.");

                    }

                }

            });

        } catch (NullPointerException ignored) {

        }

    }
}
