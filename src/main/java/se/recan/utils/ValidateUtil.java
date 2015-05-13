package se.recan.utils;

import org.apache.log4j.Logger;

/**
 *
 * @date 2014-aug-05
 * @author Anders Recksén (recan)
 */
public class ValidateUtil {

    private static final Logger LOGGER = Logger.getLogger("Logger");
    
    public static final int MALE = 1; 
    public static final int FEMALE = 0; 

    /**
     * Validate that the value of a field is no shorter than specified.
     *
     * @param key String key is the name of a field.
     * @param size The minimum allowed size.
     * @return boolean
     */
    public static boolean validate(String key, int size) {
        return !key.isEmpty() && key.length() >= size;
    }

    /**
     * Validate that a field is not empty.
     *
     * @param key String key is the name of a field.
     * @return boolean
     */
    public static boolean validate(String key) {
        return !key.trim().isEmpty();
    }

    public static boolean validateSocialSecurityNumber(String key) {
        String pnr = removeInvalidChars(key);
        System.out.println(pnr);
        if (pnr.isEmpty()) {
            return false;
        }

        // Bygg upp en tiosiffrig intarray av ovanstående, rensade sträng.
        int[] pnrInt = new int[10];

        String[] split = pnr.trim().split("");
        int counter = 0;

        for (int i = 0; i < pnrInt.length; i++) {
            try {
                pnrInt[i] = Integer.parseInt(split[counter]);
                counter++;
            } catch (NumberFormatException e) {
                System.err.println("Not valid int: " + split[counter]);
                return false;
            }
        }

        // Skicka sträng till validering.
        return validateSocialSecurityNumber(pnrInt);
    }

    /**
     * Strängen skall bestå av 10 tecken och endast siffror.
     *
     * @param pnr
     * @return
     */
    private static String removeInvalidChars(String pnr) {
        pnr = pnr.trim();

        // Ta bort bindestreck.
        if (pnr.contains("-")) {
            pnr = pnr.replace("-", "");
        }

        // Ta bort mellanslag.
        if (pnr.contains(" ")) {
            pnr = pnr.replace(" ", "");
        }

        // Ta bort kolon.
        if (pnr.contains(":")) {
            pnr = pnr.replace(":", "");
        }

        // Är strängen längre än tio tecken tar vi bort de inledande två tecknen.
        // Ex. 196210024318 => 6210024318.
        if (pnr.length() > 10) {
            return pnr.substring(2);
        }

        // Strängen skall nu innehålla tio tecken. I annat fall returneras en tom sträng.
        if (pnr.length() != 10) {
            return "";
        }

        return pnr;
    }

    /**
     * Luhn-algoritmen Multiplicera talen med 2 respektive 1, börja med 2: (2*)6
     * (1*)2 (2*)1 (1*)0 (2*)0 (1*)2 (2*)4 (1*)3 (2*)1 (1*)8 12 2 2 0 0 2 8 3 2
     * 8 Tvåsiffriga tal delas upp och adderas: 12 = 1+2 Addera sedan ihop hela
     * resultatet: 1+2 +2 +2 +0 +0 +2 +8 +3 +2 +8 = 30 Resultatet (30) skall
     * vara ett jämnt tiotal alltså jämnt delbart med 10.
     *
     * @param pnr
     * @return
     */
    private static boolean validateSocialSecurityNumber(int[] pnr) {
        int summary = 0;

        // Loopa int array.
        for (int i = 0; i < pnr.length; i++) {
            int result = 0;

            if (i % 2 == 0) {
                result = (2 * pnr[i]);
            } else {
                result = pnr[i];
            }

            if (result > 9) {
                result = (result % 10) + 1;
            }

            summary += result;
        }

        // Är summan jämt delbar med 10 är detta ett korrekt personnummer.
        return (summary % 10 == 0);
    }

    /**
     * Könet framgår av näst sista siffran i personnumret som är udda för män
     * och jämn för kvinnor.
     *
     * @param pnr
     * @return
     */
    public static int getGender(String pnr) {
        if (pnr.trim().isEmpty()) {
            return -1;
        }

        String[] split = pnr.split("");

        int i = Integer.parseInt(split[split.length - 2]);
        if (i % 2 == 0) {
            return 0;
        } else {
            return 1;
        }
    }

    /**
     * Validate that the value of a field contains a specified String.
     *
     * @param key String key is the name of a field.
     * @param mandatory The recuired String.
     * @return boolean
     */
    public static boolean validate(String key, String mandatory) {
        return !key.isEmpty() && key.contains(mandatory);
    }

    /**
     * Validate that a String is a valid email address.
     * Hanterar endast se-domänen
     *
     * @param mail
     * @return boolean
     */
    public static boolean validateMail(String mail) {
        if (!validate(mail, "@")) {
            return false;
        }
        
        if (!validate(mail, ".se")) {
            return false;
        }

        // Det skall finnas text före och efter ett at-tecken
        // ord@ord
        String[] at = mail.split("@");
        if (at.length < 2) {
            return false;
        }
        if (at[0].isEmpty()) {
            return false;
        }

        // Efter at-tecknet skall texten bestå av två ord och en punkt
        // minst ord.ord
        String[] dot = at[1].split("\\.");
        
        return dot.length >= 2;
    }
    
    private static final int ETISKA_FONDER = 1;
    private static final int FOND_BETYG = 2;
    private static final int VERY_LOW_RISK = 4;
    private static final int LOW_RISK = 8;
    private static final int MEDIUM_RISK = 16;
    private static final int HIGH_RISK = 32;
    private static final int VERY_HIGH_RISK = 64;
    private static final int RANTE_FONDER = 128;
    private static final int BLAND_FONDER = 256;
    private static final int GENERATIONS_FONDER = 512;
    private static final int AKTIE_FONDER = 1024;
 
    private int filter;

    public void setFilter(int filter) {
        this.filter = filter;
    }
    
    public int getFilter() { return filter; }

    public void setEtiskaFonder(boolean filter) {
        this.filter = filter ? this.filter | ETISKA_FONDER : this.filter & ~ETISKA_FONDER;
    }

    public void setFondBetyg(boolean filter) {
        this.filter = filter ? this.filter | FOND_BETYG : this.filter & ~FOND_BETYG;
    }

    public void setVeryLowRisk(boolean filter) {
        this.filter = filter ? this.filter | VERY_LOW_RISK : this.filter & ~VERY_LOW_RISK;
    }
    
    public void setLowRisk(boolean filter) {
        this.filter = filter ? this.filter | LOW_RISK : this.filter & ~LOW_RISK;
    }
    
    public void setMediumRisk(boolean filter) {
        this.filter = filter ? this.filter | MEDIUM_RISK : this.filter & ~MEDIUM_RISK;
    }
    
    public void setHighRisk(boolean filter) {
        this.filter = filter ? this.filter | HIGH_RISK : this.filter & ~HIGH_RISK;
    }

    public void setVeryHighRisk(boolean filter) {
        this.filter = filter ? this.filter | VERY_HIGH_RISK : this.filter & ~VERY_HIGH_RISK;
    }

    public void setRanteFonder(boolean filter) {
        this.filter = filter ? this.filter | RANTE_FONDER : this.filter & ~RANTE_FONDER;
    }
    
    public void setBlandFonder(boolean filter) {
        this.filter = filter ? this.filter | BLAND_FONDER : this.filter & ~BLAND_FONDER;
    }
    
    public void setGenerationsFonder(boolean filter) {
        this.filter = filter ? this.filter | GENERATIONS_FONDER : this.filter & ~GENERATIONS_FONDER;
    }

    public void setAktieFonder(boolean filter) {
        this.filter = filter ? this.filter | AKTIE_FONDER : this.filter & ~AKTIE_FONDER;
    }
    
    public boolean isEtiskaFonder() { return (filter & ETISKA_FONDER) == ETISKA_FONDER; }
    public boolean isFondBetyg() { return (filter & FOND_BETYG) == FOND_BETYG; }
    public boolean isVeryLowRisk() { return (filter & VERY_LOW_RISK) == VERY_LOW_RISK; }
    public boolean isLowRisk() { return (filter & LOW_RISK) == LOW_RISK; }
    public boolean isMediumRisk() { return (filter & MEDIUM_RISK) == MEDIUM_RISK; }
    public boolean isHighRisk() { return (filter & HIGH_RISK) == HIGH_RISK; }
    public boolean isVeryHighRisk() { return (filter & VERY_HIGH_RISK) == VERY_HIGH_RISK; }
    public boolean isRanteFonder() { return (filter & RANTE_FONDER) == RANTE_FONDER; }
    public boolean isBlandFonder() { return (filter & BLAND_FONDER) == BLAND_FONDER; }
    public boolean isGenerationsFonder() { return (filter & GENERATIONS_FONDER) == GENERATIONS_FONDER; }
    public boolean isAktieFonder() { return (filter & AKTIE_FONDER) == AKTIE_FONDER; }

    public boolean validate(int filter) {
        switch (filter) {
            case 1:
                return true;
            case 2:
                return true;
            case 4:
                return true;
            case 8:
                return true;
            case 16:
                return true;
            case 32:
                return true;
            case 64:
                return true;
            case 128:
                return true;
            case 256:
                return true;
            case 512:
                return true;
            case 1024:
                return true;
            case 1025:
                return true;
            case 1027:
                return true;
            case 1028:
                return true;
            case 1032:
                return true;
            case 1040:
                return true;
            case 1056:
                return true;
            case 148:
                return true;
            case 7:
                return true;
            case 15:
                return true;
            case 48:
                return true;
            default:
                return false;
        }
    }
}
