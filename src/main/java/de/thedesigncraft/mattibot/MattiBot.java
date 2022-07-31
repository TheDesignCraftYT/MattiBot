package de.thedesigncraft.mattibot;

import de.thedesigncraft.mattibot.commands.categories.moderation.ClearServerCommand;
import de.thedesigncraft.mattibot.commands.categories.moderation.TempBanServerCommand;
import de.thedesigncraft.mattibot.constants.methods.CommandMethods;
import de.thedesigncraft.mattibot.constants.methods.MainMethods;
import de.thedesigncraft.mattibot.constants.values.MainValues;
import de.thedesigncraft.mattibot.constants.values.commands.ServerCommands;
import de.thedesigncraft.mattibot.constants.values.commands.Versions;
import de.thedesigncraft.mattibot.functions.help.HelpActionRowListener;
import de.thedesigncraft.mattibot.functions.joinroles.JoinRolesActionRowListener;
import de.thedesigncraft.mattibot.functions.joinroles.JoinRolesMessageListener;
import de.thedesigncraft.mattibot.functions.joinroles.MemberJoinListener;
import de.thedesigncraft.mattibot.functions.report.ReportModalListener;
import de.thedesigncraft.mattibot.functions.report.settings.ReportSystemActionRowListener;
import de.thedesigncraft.mattibot.functions.report.settings.ReportSystemMessageListener;
import de.thedesigncraft.mattibot.functions.whitelist.WhitelistActionRowListener;
import de.thedesigncraft.mattibot.functions.whitelist.WhitelistMessageListener;
import de.thedesigncraft.mattibot.functions.youtube.YoutubeActionRowListener;
import de.thedesigncraft.mattibot.functions.youtube.YoutubeListener;
import de.thedesigncraft.mattibot.functions.youtube.YoutubeMessageListener;
import de.thedesigncraft.mattibot.listeners.*;
import de.thedesigncraft.mattibot.manage.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MattiBot {

    public static MattiBot INSTANCE;

    public static JDA jda;

    public static ServerCommandManager serverCmdMan;

    private Thread loop;

    public static void main(String[] args) throws LoginException {

        new MattiBot();

    }

    public MattiBot() throws LoginException {

        INSTANCE = this;

        LiteSQL.connect();
        SQLManager.onCreate();

        JDABuilder jdaBuilder = JDABuilder.createDefault(MainMethods.getToken()[1]);
        Logger logger = LoggerFactory.getLogger(Versions.class);
        logger.info("BotVersion: '" + Versions.currentVersion() + "'");
        logger.info("Starting '" + MainMethods.getToken()[0] + "' Bot.");
        jdaBuilder.setStatus(OnlineStatus.IDLE);
        jdaBuilder.setActivity(Activity.playing("Bot startet..."));

        // Add Action-Row-Listeners
        jdaBuilder.addEventListeners(

                new HelpActionRowListener(),
                new WhitelistActionRowListener(),
                new ModerationActionRowListener(),
                new ClearServerCommand(),
                new JoinRolesActionRowListener(),
                new ReportSystemActionRowListener(),
                new YoutubeActionRowListener()

        );

        // Add Main-Action-Row-Listener
        jdaBuilder.addEventListeners(

                new StandardActionRowListener()

        );

        // Add Other Listeners
        jdaBuilder.addEventListeners(

                new WhitelistMessageListener(),
                new DevCommandListener(),
                new NewUpdateListener(),
                new JoinRolesMessageListener(),
                new MemberJoinListener(),
                new ReportSystemMessageListener(),
                new ReportModalListener(),
                new YoutubeMessageListener(),
                new YoutubeListener()

        );

        // Enable Intents
        jdaBuilder.enableIntents(
                GatewayIntent.GUILD_PRESENCES,
                GatewayIntent.GUILD_MEMBERS,
                GatewayIntent.GUILD_VOICE_STATES,
                GatewayIntent.MESSAGE_CONTENT
        );

        // Set MemberCachePolicy
        jdaBuilder.setMemberCachePolicy(MemberCachePolicy.ALL);

        jda = jdaBuilder.build();

        Logger logger1 = LoggerFactory.getLogger(MattiBot.class);
        logger1.info("StatusUpdate: Online");

        jda.addEventListener(serverCmdMan = new ServerCommandManager());

        consoleCommands();
        runLoop();

    }

    public void consoleCommands() {

        new Thread(() -> {

            String line;
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

            try {

                while ((line = bufferedReader.readLine()) != null) {

                    if (line.equalsIgnoreCase(MainValues.shutdownCommand)) {

                        if (jda != null) {

                            System.out.println("-------------------------");
                            System.out.println(MainValues.name + ": Fahre herunter...");
                            System.out.println("-------------------------");

                            jda.getPresence().setStatus(OnlineStatus.OFFLINE);
                            jda.shutdown();
                            LiteSQL.disconnect();
                            Logger logger = LoggerFactory.getLogger(MattiBot.class);
                            logger.info("StatusUpdate: Offline");
                            System.exit(0);

                        } else {

                            Logger logger = LoggerFactory.getLogger(MattiBot.class);
                            logger.error("JDA ist null");

                        }

                        if (loop != null) {

                            loop.interrupt();

                        }

                        bufferedReader.close();
                        break;

                    } else if (line.equalsIgnoreCase("commands")) {

                        System.out.println("-------------------------");
                        System.out.println(MainValues.name + ": Hier ist eine Liste von allen Konsolbefehlen: ");
                        System.out.println("| - commands");
                        System.out.println("| - " + MainValues.shutdownCommand);
                        System.out.println("| - ping");
                        System.out.println("| - alldiscordcommands");
                        System.out.println("-------------------------");

                    } else if (line.equalsIgnoreCase("ping")) {

                        System.out.println("-------------------------");
                        System.out.println(MainValues.name + ": Bot Status:");
                        System.out.println("Interne Berechnungen:");
                        System.out.println("| " + jda.getResponseTotal() + "ms");
                        System.out.println("Discord API Gateway:");
                        System.out.println("| " + jda.getGatewayPing() + "ms");
                        System.out.println("Discord API REST:");
                        System.out.println("| " + jda.getRestPing().complete() + "ms");
                        System.out.println("-------------------------");

                    } else if (line.equalsIgnoreCase("alldiscordcommands")) {

                        System.out.println("-------------------------");
                        System.out.println(MainValues.name + ": AllDiscordCommands:");

                        if (!ServerCommands.serverCommands().isEmpty()) {

                            ServerCommands.serverCommands().forEach(serverCommand -> {

                                if (serverCommand.slashCommand()) {

                                    System.out.println("| - /" + CommandMethods.getServerCommandName(serverCommand));

                                } else {

                                    System.out.println("| - " + MainValues.standardcommandPrefix + CommandMethods.getServerCommandName(serverCommand));

                                }

                            });

                        } else {

                            System.out.println("| Es sind keine Befehle registriert.");

                        }

                        System.out.println("-------------------------");

                    } else {

                        System.out.println("-------------------------");
                        System.out.println(MainValues.name + ": Das ist kein Registrierter Befehl.");
                        System.out.println("Fuer eine Liste an Befehlen, fuehre 'commands' aus.");
                        System.out.println("-------------------------");

                    }

                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }).start();

    }

    public void runLoop() {

        this.loop = new Thread(() -> {

            long time = System.currentTimeMillis();

            while (true) {

                if (System.currentTimeMillis() >= time + 1000) {

                    time = System.currentTimeMillis();
                    onSecond();

                }

            }

        });

        this.loop.setName("Loop");
        this.loop.start();

    }

    int next = MainValues.loopStartTime;

    public void onSecond() {

        if (next <= 0) {

            jda.getPresence().setStatus(OnlineStatus.ONLINE);
            jda.getPresence().setActivity(MainMethods.getActivity());

            jda.getGuilds().forEach(guild -> {

                ResultSet prefix = LiteSQL.onQuery("SELECT prefix FROM prefix WHERE guildid = " + guild.getIdLong());

                try {
                    guild.modifyNickname(guild.getMemberById(jda.getSelfUser().getIdLong()), jda.getSelfUser().getName() + " [" + prefix.getString("prefix") + "] [/]").queue();
                } catch (SQLException e) {

                    LiteSQL.onUpdate("INSERT INTO prefix(guildid, prefix) VALUES(" + guild.getIdLong() + ", '" + MainValues.standardcommandPrefix + "')");

                }

            });

            TempBanServerCommand.checkTempBans();

            next = 5;

        } else {

            next--;

        }

    }

}
