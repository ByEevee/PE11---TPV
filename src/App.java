
import java.util.Scanner;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;


public class App {
    
    Scanner sc = new Scanner(System.in);
    
    public static void main(String[] args) {
        App program = new App();
        program.run();
    }
    
    private JSONArray loadArticlesJSON() {
        DatabaseConnection dbConn = new DatabaseConnection();
        return dbConn.loadDataAsArray("articles.json");
    }
    
    public void run() {
        boolean exit = false;
       do{ 
        // Menú principal
       }while (!exit);
            
        
    }
    
    
}




