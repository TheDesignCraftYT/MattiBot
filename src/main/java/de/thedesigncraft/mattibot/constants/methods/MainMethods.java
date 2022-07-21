package de.thedesigncraft.mattibot.constants.methods;

import de.thedesigncraft.mattibot.MattiBot;
import de.thedesigncraft.mattibot.constants.values.MainValues;
import de.thedesigncraft.mattibot.constants.values.TOKEN;
import de.thedesigncraft.mattibot.constants.values.commands.Versions;
import de.thedesigncraft.mattibot.manage.LiteSQL;
import net.dv8tion.jda.api.entities.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public interface MainMethods {

    static Activity getActivity() {

        List<Long> userIds = new ArrayList<>();

        MattiBot.jda.getGuilds().forEach(guild -> guild.getMembers().forEach(member -> {

            if (!userIds.contains(member.getIdLong()) && !member.getUser().isBot())
                userIds.add(member.getIdLong());

        }));

        int sizeOfReachableUsers = userIds.size();

        return Activity.listening(MainValues.activityText.replace("%reachableUsers", String.valueOf(sizeOfReachableUsers)));

    }

    static List<TextChannel> getWhitelistedChannels(Guild guild) {

        ResultSet whitelistedChannels = LiteSQL.onQuery("SELECT channels FROM whitelistedChannels WHERE guildid = " + guild.getIdLong());

        List<TextChannel> onlyWhitelistedChannels = new ArrayList<>();

        try {

            String[] whitelistedChannels1 = whitelistedChannels.getString("channels").split(",");

            List<String> list = List.of(whitelistedChannels1);

            if (!list.get(0).equals("") && !list.get(0).equals(" ")) {

                list.forEach(s -> onlyWhitelistedChannels.add(guild.getTextChannelById(s)));

                return onlyWhitelistedChannels;

            } else {

                return new ArrayList<>(guild.getTextChannels());

            }

        } catch (SQLException | NullPointerException e) {

            return new ArrayList<>(guild.getTextChannels());

        }

    }

    static List<TextChannel> getWhitelistedChannelDatas(Guild guild) {

        ResultSet whitelistedChannels = LiteSQL.onQuery("SELECT channels FROM whitelistedChannels WHERE guildid = " + guild.getIdLong());

        List<TextChannel> onlyWhitelistedChannels = new ArrayList<>();

        try {
            String[] whitelistedChannels1 = whitelistedChannels.getString("channels").split(",");

            List<String> list = List.of(whitelistedChannels1);

            if (!list.get(0).equals("") && !list.get(0).equals(" ")) {

                list.forEach(s -> onlyWhitelistedChannels.add(guild.getTextChannelById(s)));

                return onlyWhitelistedChannels;

            } else {

                return null;

            }

        } catch (SQLException | NullPointerException e) {

            return null;

        }

    }

    static List<String> getPunishments(User user, Guild guild, String punishmentType) {

        ResultSet punishments = LiteSQL.onQuery("SELECT punishments FROM punishments WHERE guildid = " + guild.getIdLong() + " AND userid = " + user.getIdLong());

        try {
            List<String> punishmentsList = Arrays.stream(punishments.getString("punishments").replace(">", "").split("<")).toList();

            List<String> returnList = new ArrayList<>();

            if (punishmentType.equals("warn") || punishmentType.equals("kick") || punishmentType.equals("ban") || punishmentType.equals("tempban") || punishmentType.equals("unban") || punishmentType.equals("unwarn") || punishmentType.equals("timeout") || punishmentType.equals("removetimeout")) {

                punishmentsList.forEach(s -> {

                    if (s.startsWith("(" + punishmentType + ")"))
                        returnList.add(s);

                });

                return returnList;

            } else if (punishmentType.equals("all")) {

                return punishmentsList;

            } else {

                return null;

            }

        } catch (SQLException e) {
            return new ArrayList<>();
        }

    }

    static void addPunishment(String punishment, Member member) {

        if (getPunishments(member.getUser(), member.getGuild(), "all").isEmpty()) {

            LiteSQL.onUpdate("INSERT INTO punishments(guildid, userid, punishments) VALUES(" + member.getGuild().getIdLong() + ", " + member.getIdLong() + ", '" + punishment + "')");

        } else {

            ResultSet punishmentsSet = LiteSQL.onQuery("SELECT punishments FROM punishments WHERE guildid = " + member.getGuild().getIdLong() + " AND userid = " + member.getIdLong());

            try {
                LiteSQL.onUpdate("UPDATE punishments SET punishments = '" + punishmentsSet.getString("punishments") + punishment + "' WHERE guildid = " + member.getGuild().getIdLong() + " AND userid = " + member.getIdLong());
            } catch (SQLException e) {
                Logger logger = LoggerFactory.getLogger(MainMethods.class);
                logger.error("Unerwarteter Fehler");
            }

        }

    }

    static String getToken() {

        Logger logger = LoggerFactory.getLogger(Versions.class);
        logger.info("BotVersion: '" + Versions.currentVersion() + "'");

        try {

            String versionType = Versions.currentVersion().split("-")[1].replace(".", "III").split("III")[0];

            if(versionType.equals("alpha")) {

                logger.info("Starting 'Alpha' Bot");
                return TOKEN.alphaToken;

            } else if (versionType.equals("beta")) {

                logger.info("Starting 'Beta' Bot");
                return TOKEN.betaToken;

            }

        } catch (ArrayIndexOutOfBoundsException e) {

            logger.info("Starting 'Main' Bot");
            return TOKEN.releaseToken;

        }

        return null;

    }

    static long sumOf(String calculation) {

        int l = calculation.length();
        long sum = 0;
        StringBuilder temp = new StringBuilder();

        for (int i = 0; i < l; i++) {

            if (Character.isDigit(calculation.charAt(i))) {

                if (i < l - 1 && Character.isDigit(calculation.charAt(i + 1))) {

                    temp.append(calculation.charAt(i));

                } else {

                    temp.append(calculation.charAt(i));
                    sum += Long.parseLong(temp.toString());
                    temp = new StringBuilder();

                }

            }

        }

        return sum;

    }

}
