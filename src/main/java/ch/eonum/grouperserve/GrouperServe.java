package ch.eonum.grouperserve;

import static spark.Spark.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class GrouperServe {
	public static void main(String[] args) {
		String systems = loadSystems();
	    
        get("/systems", (request, response) -> {
        	response.status(200);
            response.type("application/json");
            return systems;
        });
        
        post("/group", (req, res) -> "");
        
        post("/calculate_ecw", (req, res) -> "");
        
        post("/group_and_calculate_ecw", (req, res) -> "");
    }

	@SuppressWarnings("unchecked")
	private static String loadSystems() {
		ObjectMapper mapper = new ObjectMapper();
		StringWriter sw = new StringWriter();

		List<Map<String, String>> systemsJSON;
		try {
			systemsJSON = mapper.readValue(new FileInputStream("grouperspecs/systems.json"), ArrayList.class);
			mapper.enable(SerializationFeature.INDENT_OUTPUT);
			mapper.writeValue(sw, systemsJSON);
		} catch (IOException e) {
			System.err.println("Error during grouper server startup while loading systems: ");
			e.printStackTrace();
			stop();
		}
		String systemsString = sw.toString();

		return systemsString;
	}
}
