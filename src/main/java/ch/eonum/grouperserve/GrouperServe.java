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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.swissdrg.grouper.IGrouperKernel;
import org.swissdrg.grouper.WeightingRelation;
import org.swissdrg.grouper.batchgrouper.Catalogue;
import org.swissdrg.grouper.kernel.GrouperKernel;
import org.swissdrg.grouper.specs.SpecificationReader;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class GrouperServe {
	private final static Logger log = LoggerFactory.getLogger(GrouperServe.class);
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
				log.info("Loading grouper " + version);
				String workspace = GROUPERSPECS_FOLDER + version + "/";
				try {
					catalogues.put(version, Catalogue.createFrom( workspace + "catalogue-acute.csv"));
				} catch (FileNotFoundException e) {
					log.error("Could not find DRG catalogue file "
							+ workspace + "catalogue-acute.csv");
					stop();
				}
				try {
					GrouperKernel grouper = reader.loadGrouper(workspace);
					grouperKernels.put(version, grouper);
				} catch (Exception e) {
					log.error("Error while loading DRG workspace " + workspace);
					e.printStackTrace();
					stop();
				}
			}
		} catch (IOException e) {
			log.error("Error during grouper server startup while loading systems: ");
			e.printStackTrace();
			stop();
		}
		String systemsString = sw.toString();

		return systemsString;
	}
}
