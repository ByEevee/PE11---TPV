package objectes;

public class Familia {

    public static final int CAMISA  = 1;
    public static final int PANTALO = 2;
    // Pròxima família: public static final int SABATA = 3;

    public static String toNom(int codi) {
        switch (codi) {
            case CAMISA:  return "camisa";
            case PANTALO: return "pantaló";
            default:      return "desconegut";
        }
    }

    public static int fromNom(String nom) {
        if (nom == null) return -1;
        switch (nom.toLowerCase()) {
            case "camisa":  return CAMISA;
            case "pantaló":
            case "pantalo": return PANTALO;
            default:        return -1;
        }
    }

    public static boolean esValid(int codi) {
        return codi == CAMISA || codi == PANTALO;
    }
}