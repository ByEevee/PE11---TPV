package DAO;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import objectes.Article;
import objectes.Camisa;
import objectes.Pantalo;
import objectes.PropostaCompra;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JSONConnection {

    public List<Article> leerArticles(String ruta) {
        List<Article> articulos = new ArrayList<>();
        JSONParser parser = new JSONParser();

        try (FileReader reader = new FileReader(ruta)) {
            // El archivo es un JSONArray de objetos
            JSONArray jsonArray = (JSONArray) parser.parse(reader);

            for (Object obj : jsonArray) {
                JSONObject item = (JSONObject) obj;

                // Extraemos como JSONObject y hacemos casting a Long (estándar de json-simple 1.1.1)
                int id = ((Long) item.get("id")).intValue();
                String nom = (String) item.get("nom");
                String familia = (String) item.get("familia");
                double preuBase = ((Number) item.get("preu_base")).doubleValue();
                int iva = ((Long) item.get("iva")).intValue();
                int stock = ((Long) item.get("stock")).intValue();

                if (familia.equalsIgnoreCase("camisa")) {
                    int tColl = ((Long) item.get("talla_coll")).intValue();
                    int aPit = ((Long) item.get("amplada_pit")).intValue();
                    articulos.add(new Camisa(id, nom, preuBase, iva, stock, tColl, aPit));
                } else {
                    int tCin = ((Long) item.get("talla_cintura")).intValue();
                    int lCam = ((Long) item.get("llargada_camal")).intValue();
                    articulos.add(new Pantalo(id, nom, preuBase, iva, stock, tCin, lCam));
                }
            }
        } catch (IOException | ParseException e) {
            System.err.println("Error procesando JSON: " + e.getMessage());
        }
        return articulos;
    }

    public void guardarRecompra(String ruta, List<PropostaCompra> propuestas) {
        JSONArray root = new JSONArray();

        for (PropostaCompra p : propuestas) {
            JSONObject obj = new JSONObject();
            obj.put("codi", p.getCodi());
            obj.put("nom", p.getNom());
            obj.put("quantitat", p.getQuantitat());
            root.add(obj);
        }

        try (FileWriter file = new FileWriter(ruta)) {
            file.write(root.toJSONString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}