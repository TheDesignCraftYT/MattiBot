package de.thedesigncraft.mattibot.constants.values.commands;

import java.util.concurrent.ConcurrentHashMap;

public interface Categories {

    static ConcurrentHashMap<String, String> categories() {

        ConcurrentHashMap<String, String> returnList = new ConcurrentHashMap<>();

        returnList.put("misc", "Nützliches");
        returnList.put("moderation", "Moderation");
        returnList.put("configuration", "Konfiguration");

        return returnList;

    }

}
