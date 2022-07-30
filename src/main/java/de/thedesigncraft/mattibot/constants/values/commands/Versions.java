package de.thedesigncraft.mattibot.constants.values.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public interface Versions {

    static List<String> versionNames() {

        List<String> returnList = new ArrayList<>();

        returnList.add("v1.0.0-alpha.1");
        returnList.add("v1.0.0-alpha.2");
        returnList.add("v1.0.0-alpha.3");
        returnList.add("v1.0.0-alpha.4");
        returnList.add("v1.0.0-alpha.5");
        returnList.add("v1.0.0-alpha.6");
        returnList.add("v1.0.0-alpha.7");

        return returnList;

    }

    static ConcurrentHashMap<String, String> versions() {

        ConcurrentHashMap<String, String> returnList = new ConcurrentHashMap<>();

        versionNames().forEach(s -> {

            if (s.replace(".", "III").split("III")[2].endsWith("alpha")) {

                returnList.put(s.replace("lpha", "").replace(".", "").replace("-", ""), s);

            } else if (s.replace(".", "III").split("III")[2].endsWith("beta")) {

                returnList.put(s.replace("eta", "").replace(".", "").replace("-", ""), s);

            } else {

                returnList.put(s.replace(".", ""), s);

            }

        });

        return returnList;

    }

    static String currentVersion() {

        return versionNames().get(versionNames().size()-1);

    }

}
