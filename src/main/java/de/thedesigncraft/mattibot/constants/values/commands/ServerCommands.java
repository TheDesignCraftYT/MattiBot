package de.thedesigncraft.mattibot.constants.values.commands;

import de.thedesigncraft.mattibot.commands.categories.misc.PingServerCommand;
import de.thedesigncraft.mattibot.commands.categories.configuration.PrefixServerCommand;
import de.thedesigncraft.mattibot.commands.categories.misc.UserinfoServerCommand;
import de.thedesigncraft.mattibot.commands.categories.moderation.BanServerCommand;
import de.thedesigncraft.mattibot.commands.categories.moderation.KickServerCommand;
import de.thedesigncraft.mattibot.commands.categories.moderation.TempBanServerCommand;
import de.thedesigncraft.mattibot.commands.categories.moderation.WarnServerCommand;
import de.thedesigncraft.mattibot.commands.types.ServerCommand;
import de.thedesigncraft.mattibot.commands.categories.moderation.ClearServerCommand;
import de.thedesigncraft.mattibot.functions.help.HelpServerCommand;
import de.thedesigncraft.mattibot.functions.whitelist.WhitelistServerCommand;

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

        return returnList;

    }

}