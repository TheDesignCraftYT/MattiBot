package de.thedesigncraft.mattibot.manage;

public class SQLManager {

    public static void onCreate() {

        // LiteSQL.onUpdate("CREATE TABLE IF NOT EXISTS name(varname VARTYPE)");
        LiteSQL.onUpdate("CREATE TABLE IF NOT EXISTS whitelistedChannels(guildid INTEGER, channels STRING)");
        LiteSQL.onUpdate("CREATE TABLE IF NOT EXISTS prefix(guildid INTEGER, prefix STRING)");
        LiteSQL.onUpdate("CREATE TABLE IF NOT EXISTS punishments(guildid INTEGER, userid INTEGER, punishments STRING)");
        LiteSQL.onUpdate("CREATE TABLE IF NOT EXISTS joinroles(guildid INTEGER, roles STRING)");
        LiteSQL.onUpdate("CREATE TABLE IF NOT EXISTS reportSystem(guildid INTEGER, channel INTEGER, active BOOLEAN)");

    }

}
