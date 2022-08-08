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

        } catch (SQLException e) {

            LiteSQL.onUpdate("INSERT INTO whitelistedChannels(guildid) VALUES(" + guild.getIdLong() + ")");

            return new ArrayList<>(guild.getTextChannels());

        } catch (NullPointerException e) {

            return new ArrayList<>(guild.getTextChannels());

        }

    }

    static List<Role> getJoinRoles(Guild guild) {

        ResultSet joinroles = LiteSQL.onQuery("SELECT roles FROM joinroles WHERE guildid = " + guild.getIdLong());

        List<Role> joinrolesList = new ArrayList<>();

        try {

            String[] joinrolesString = joinroles.getString("roles").split(",");

            List<String> list = List.of(joinrolesString);

            if (!list.get(0).equals("") && !list.get(0).equals(" ")) {

                list.forEach(s -> joinrolesList.add(guild.getRoleById(s)));

                return joinrolesList;

            } else {

                return new ArrayList<>(guild.getRoles());

            }

        } catch (SQLException e) {

            LiteSQL.onUpdate("INSERT INTO joinroles(guildid) VALUES(" + guild.getIdLong() + ")");

            return new ArrayList<>(guild.getRoles());

        } catch (NullPointerException e) {

            return new ArrayList<>(guild.getRoles());

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

    static List<String[]> getPunishments(User user, Guild guild, String punishmentType) {

        ResultSet punishments = LiteSQL.onQuery("SELECT punishments FROM punishments WHERE guildid = " + guild.getIdLong() + " AND userid = " + user.getIdLong());

        try {

            String[] punishmentsString = punishments.getString("punishments").replace("<", "").split(">");

            List<String[]> allPunishments = new ArrayList<String[]>();

            List<String> al = Arrays.asList(punishmentsString);

            al.forEach(s -> allPunishments.add(s.replace("(", "").replace(")", "III").split("III")));

            List<String[]> returnList = new ArrayList<>();

            if (punishmentType.equals("warn") || punishmentType.equals("kick") || punishmentType.equals("ban") || punishmentType.equals("tempban") || punishmentType.equals("unban") || punishmentType.equals("unwarn") || punishmentType.equals("timeout") || punishmentType.equals("removetimeout")) {

                allPunishments.forEach(s -> {

                    if (s[0].equals(punishmentType)) {

                        returnList.add(s);

                    }

                });

                return returnList;

            } else if (punishmentType.equals("all")) {

                return allPunishments;

            } else {

                return null;

            }

        } catch (SQLException e) {
            return new ArrayList<>();
        }

    }

    static void addPunishment(String punishment, User user, Guild guild) {

        if (getPunishments(user, guild, "all").isEmpty()) {

            LiteSQL.onUpdate("INSERT INTO punishments(guildid, userid, punishments) VALUES(" + guild.getIdLong() + ", " + user.getIdLong() + ", '" + punishment + "')");

        } else {

            ResultSet punishmentsSet = LiteSQL.onQuery("SELECT punishments FROM punishments WHERE guildid = " + guild.getIdLong() + " AND userid = " + user.getIdLong());

            try {
                LiteSQL.onUpdate("UPDATE punishments SET punishments = '" + punishmentsSet.getString("punishments") + punishment + "' WHERE guildid = " + guild.getIdLong() + " AND userid = " + user.getIdLong());
            } catch (SQLException e) {
                Logger logger = LoggerFactory.getLogger(MainMethods.class);
                logger.error("Unerwarteter Fehler");
            }

        }

    }

    static String[] getToken() {

        try {

            String versionType = Versions.currentVersion().split("-")[1].replace(".", "III").split("III")[0];

            if (versionType.equals("alpha")) {

                return new String[]{"Alpha", TOKEN.alphaToken};

            } else if (versionType.equals("beta")) {

                return new String[]{"Beta", TOKEN.betaToken};

            }

        } catch (ArrayIndexOutOfBoundsException e) {

            return new String[]{"Release", TOKEN.releaseToken};

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

    static List<Role> getJoinRoleDatas(Guild guild) {

        ResultSet joinroles = LiteSQL.onQuery("SELECT roles FROM joinroles WHERE guildid = " + guild.getIdLong());

        List<Role> joinrolesList = new ArrayList<>();

        try {
            String[] joinrolesString = joinroles.getString("roles").split(",");

            List<String> list = List.of(joinrolesString);

            if (!list.get(0).equals("") && !list.get(0).equals(" ")) {

                list.forEach(s -> joinrolesList.add(guild.getRoleById(s)));

                return joinrolesList;

            } else {

                return null;

            }

        } catch (SQLException | NullPointerException e) {

            return null;

        }

    }

    static boolean reportSystemActive(Guild guild) {

        ResultSet reportSystem = LiteSQL.onQuery("SELECT active FROM reportSystem WHERE guildid = " + guild.getIdLong());

        try {
            return Boolean.parseBoolean(reportSystem.getString("active"));
        } catch (SQLException ignored) {

        }

        return false;

    }

    static boolean youtubeActive(Guild guild) {

        ResultSet youtube = LiteSQL.onQuery("SELECT active FROM youtube WHERE guildid = " + guild.getIdLong());

        try {
            return Boolean.parseBoolean(youtube.getString("active"));
        } catch (SQLException ignored) {
        }

        return false;
    }

    static String getReportChannelString(Guild guild) {

        ResultSet reportSystem = LiteSQL.onQuery("SELECT channel FROM reportSystem WHERE guildid = " + guild.getIdLong());

        try {
            TextChannel reportChannel = guild.getTextChannelById(reportSystem.getLong("channel"));

            return reportChannel.getAsMention();

        } catch (SQLException e) {

            LiteSQL.onUpdate("INSERT INTO reportSystem(guildid) VALUES(" + guild.getIdLong() + ")");

            return "```Kein Kanal festgelegt.```";

        } catch (NullPointerException e) {

            return "```Kein Kanal festgelegt.```";

        }

    }

    static String getYoutubeChannelString(Guild guild) {

        ResultSet youtube = LiteSQL.onQuery("SELECT channel FROM youtube WHERE guildid = " + guild.getIdLong());

        try {
            TextChannel notificationsChannel = guild.getTextChannelById(youtube.getLong("channel"));

            return notificationsChannel.getAsMention();

        } catch (SQLException e) {

            LiteSQL.onUpdate("INSERT INTO youtube(guildid, message, active) VALUES(" + guild.getIdLong() + ", '" + "{%channel_name} hat ein neues Video hochgeladen:\n\n{%video_link}" + "', 'false')");

            return "```Kein Kanal festgelegt.```";

        } catch (NullPointerException e) {

            return "```Kein Kanal festgelegt.```";

        }

    }

    static TextChannel getReportChannel(Guild guild) {

        if (!getReportChannelString(guild).equals("```Kein Kanal festgelegt.```")) {

            return guild.getTextChannelById(getReportChannelString(guild).replace("<#", "").replace(">", ""));

        } else {

            return null;

        }

    }

    static TextChannel getYoutubeChannel(Guild guild) {

        if (!getYoutubeChannelString(guild).equals("```Kein Kanal festgelegt.```")) {

            return guild.getTextChannelById(getYoutubeChannelString(guild).replace("<#", "").replace(">", ""));

        } else {

            return null;

        }

    }

    static String getYoutubeMessage(Guild guild) {

        ResultSet youtube = LiteSQL.onQuery("SELECT message FROM youtube WHERE guildid = " + guild.getIdLong());

        try {

            assert youtube != null;
            return "```" + youtube.getString("message") + "```";

        } catch (SQLException e) {

            Logger logger = LoggerFactory.getLogger(MainMethods.class);
            logger.error("Unerwarteter Fehler.");

            return null;

        }

    }
}
