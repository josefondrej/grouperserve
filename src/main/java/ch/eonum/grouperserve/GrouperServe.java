package ch.eonum.grouperserve;

import static spark.Spark.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.swissdrg.grouper.IGrouperKernel;
import org.swissdrg.grouper.WeightingRelation;
import org.swissdrg.grouper.batchgrouper.BatchgrouperExitCode;
import org.swissdrg.grouper.batchgrouper.Catalogue;
import org.swissdrg.grouper.kernel.GrouperKernel;
import org.swissdrg.grouper.specs.SpecificationReader;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class GrouperServe {
	private static final int HTTP_BAD_REQUEST = 400;
	private static final String GROUPERSPECS_FOLDER = "grouperspecs/";
	private static HashMap<String, IGrouperKernel> grouperKernels;
	private static HashMap<String, Map<String, WeightingRelation>> catalogues;
	
	public static void main(String[] args) {
		String systems = loadSystems();
	    
        get("/systems", (request, response) -> {
        	response.status(200);
            response.type("application/json");
            return systems;
        });
        
        post("/group", (req, res) -> {
        	return "";
        });
        
        post("/calculate_ecw", (req, res) -> {
        	return "";
        });
        
        post("/group_and_calculate_ecw", (req, res) -> {
        	return "";
        });
    }

	@SuppressWarnings("unchecked")
	private static String loadSystems() {
		StringWriter sw = new StringWriter();
		
		try {
			ObjectMapper mapper = new ObjectMapper();
			List<Map<String, String>> systemsJSON = mapper.readValue(new FileInputStream(GROUPERSPECS_FOLDER + "systems.json"), ArrayList.class);
			mapper.enable(SerializationFeature.INDENT_OUTPUT);
			mapper.writeValue(sw, systemsJSON);
			
			grouperKernels = new HashMap<>();
			catalogues = new HashMap<>();
			
			SpecificationReader reader = new SpecificationReader();
			
			
			for(Map<String, String> system : systemsJSON){
				String version = system.get("version");
				String workspace = GROUPERSPECS_FOLDER + version + "/";
				try {
					catalogues.put(version, Catalogue.createFrom( workspace + "catalogue-acute.csv"));
				} catch (FileNotFoundException e) {
					System.err.println("Could not find DRG catalogue file "
							+ workspace + "catalogue-acute.csv");
					stop();
				}
				try {
					GrouperKernel grouper = reader.loadGrouper(workspace);
					grouperKernels.put(version, grouper);
				} catch (Exception e) {
					System.err
							.println("Error while loading DRG workspace " + workspace);
					e.printStackTrace();
					System.exit(BatchgrouperExitCode.WORKSPACE_LOADING_ERROR);
				}
			}
		} catch (IOException e) {
			System.err.println("Error during grouper server startup while loading systems: ");
			e.printStackTrace();
			stop();
		}
		String systemsString = sw.toString();

		return systemsString;
	}
}
