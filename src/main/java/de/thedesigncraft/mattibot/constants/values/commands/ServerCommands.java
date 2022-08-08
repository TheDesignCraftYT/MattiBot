package de.thedesigncraft.mattibot.constants.values.commands;

import de.thedesigncraft.mattibot.commands.categories.configuration.PrefixServerCommand;
import de.thedesigncraft.mattibot.commands.categories.misc.CreditsServerCommand;
import de.thedesigncraft.mattibot.commands.categories.misc.PingServerCommand;
import de.thedesigncraft.mattibot.commands.categories.misc.UserinfoServerCommand;
import de.thedesigncraft.mattibot.commands.categories.misc.VersionServerCommand;
import de.thedesigncraft.mattibot.commands.categories.moderation.*;
import de.thedesigncraft.mattibot.commands.types.ServerCommand;
import de.thedesigncraft.mattibot.functions.help.HelpServerCommand;
import de.thedesigncraft.mattibot.functions.joinroles.JoinRolesServerCommand;
import de.thedesigncraft.mattibot.functions.report.settings.ReportSystemServerCommand;
import de.thedesigncraft.mattibot.functions.whitelist.WhitelistServerCommand;
import de.thedesigncraft.mattibot.functions.youtube.YoutubeServerCommand;

import java.util.ArrayList;
import java.util.List;

public interface ServerCommands {

    static List<ServerCommand> serverCommands() {

        List<ServerCommand> returnList = new ArrayList<>();

        //Add ServerCommands
        returnList.add(new PingServerCommand());
        returnList.add(new HelpServerCommand());
        returnList.add(new PrefixServerCommand());
        returnList.add(new UserinfoServerCommand());
        returnList.add(new WhitelistServerCommand());
        returnList.add(new WarnServerCommand());
        returnList.add(new KickServerCommand());
        returnList.add(new BanServerCommand());
        returnList.add(new TempBanServerCommand());
        returnList.add(new ClearServerCommand());
        returnList.add(new UnbanServerCommand());
        returnList.add(new VersionServerCommand());
        returnList.add(new JoinRolesServerCommand());
        returnList.add(new ReportSystemServerCommand());
        returnList.add(new YoutubeServerCommand());
        returnList.add(new CreditsServerCommand());

        return returnList;

    }

}
