import java.io.FileReader;
import java.io.IOException;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class DatabaseConnection {
    
    private static final String DB_PATH = "src/BBDD/";
    private JSONParser parser;
    
    public DatabaseConnection() {
        this.parser = new JSONParser();
    }
    
    /**
     * Carga les dades del fitxer JSON especificat
     * @param fileName Nom del fitxer (sense ruta)
     * @return JSONArray o JSONObject depenent del fitxer
     */
    public Object loadData(String fileName) {
        try {
            String filePath = DB_PATH + fileName;
            Object obj = parser.parse(new FileReader(filePath));
            return obj;
        } catch (IOException | ParseException e) {
            System.err.println("Error al carregat el fitxer " + fileName + ": " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Carga un fitxer JSON com a JSONArray
     * @param fileName Nom del fitxer
     * @return JSONArray o null si hi ha error
     */
    public JSONArray loadDataAsArray(String fileName) {
        Object obj = loadData(fileName);
        if (obj instanceof JSONArray) {
            return (JSONArray) obj;
        }
        return null;
    }
    
    /**
     * Carga un fitxer JSON com a JSONObject
     * @param fileName Nom del fitxer
     * @return JSONObject o null si hi ha error
     */
    public JSONObject loadDataAsObject(String fileName) {
        Object obj = loadData(fileName);
        if (obj instanceof JSONObject) {
            return (JSONObject) obj;
        }
        return null;
    }
    
    /**
     * Obté la ruta completa d'un fitxer en la carpeta BBDD
     * @param fileName Nom del fitxer
     * @return Ruta completa
     */
    public String getFilePath(String fileName) {
        return DB_PATH + fileName;
    }
}
