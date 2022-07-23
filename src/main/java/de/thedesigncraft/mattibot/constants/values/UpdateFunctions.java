package de.thedesigncraft.mattibot.constants.values;

import de.thedesigncraft.mattibot.constants.values.commands.Versions;

import java.util.concurrent.ConcurrentHashMap;

public interface UpdateFunctions {

    static ConcurrentHashMap<String, String[]> updateFunctions() {

        ConcurrentHashMap<String, String[]> returnMap = new ConcurrentHashMap<>();

        // returnMap.put(Versions.versions().get("VERSION_NAME"), new String[]{"FUNCTION_NAME", "FUNCTION_DESCRIPTION"});

        // v1.0.0-alpha.2
        returnMap.put(Versions.versions().get("v100a2"), new String[]{"/tempban", "Einige sehr große Fehler im Command wurden behoben und er funktioniert nun einwandfrei."});

        // v1.0.0-alpha.3
        returnMap.put(Versions.versions().get("v100a3"), new String[]{"/ban", "Es wurden Bugs im '/ban'-Command behoben und damit das Layout dessen ein wenig überarbeitet."});
        returnMap.put(Versions.versions().get("v100a3"), new String[]{"Generelle Bugs", "Es wurden ein paar generelle Bugs gefixt und Fehler behoben."});
        returnMap.put(Versions.versions().get("v100a3"), new String[]{"/warn", "Es wurden Bugs im '/warn'-Command behoben und damit das Layout dessen ein wenig überarbeitet."});
        returnMap.put(Versions.versions().get("v100a3"), new String[]{"/kick", "Es wurden Bugs im '/kick'-Command behoben und damit das Layout dessen ein wenig überarbeitet."});

        // v1.0.0-alpha.4
        returnMap.put(Versions.versions().get("v100a4"), new String[]{"UpdateNachrichten", "Die Update-Nachrichten wurden überarbeitet und komplett automatisiert."});
        returnMap.put(Versions.versions().get("v100a4"), new String[]{"/ban", "Bugs gefixt."});

        return returnMap;
    }

}
