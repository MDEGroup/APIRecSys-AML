package ase2021.aml.apirecsys;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class Metrics {
		
	private String srcDir;		
	private String recDir;
	
	
	
	private int numItems;	
	private int testingStartPos;
	private int testingEndPos;
	
	public Metrics(int k, int numItems, String srcDir, String subFolder,
			int trStartPos1,
			int trEndPos1,
			int trStartPos2,
			int trEndPos2,
			int teStartPos,
			int teEndPos) {
		
		
		this.numItems = numItems;
		this.srcDir = srcDir;				
		
		this.recDir = this.srcDir + subFolder + "/" + "Recommendations" + "/";
		
		this.testingStartPos = teStartPos;
		this.testingEndPos = teEndPos;		
	}
	
	
	
	public int HitRatio(String item1, String item2, boolean all) {
		
		DataReader reader = new DataReader();
		
		Map<Integer,String> testingProjects = new HashMap<Integer,String>();
		if(all)testingProjects = reader.readProjectList(this.srcDir + "List.txt");
		else testingProjects = reader.readProjectList(this.srcDir + "List.txt",this.testingStartPos,this.testingEndPos);
		Set<Integer> keyTestingProjects = testingProjects.keySet();
		
		/*Select top libraries*/				
		Set<String> recommendationFile = null;
		
		int count = 0;
	
		for(Integer keyTesting:keyTestingProjects){			
			String testingPro = testingProjects.get(keyTesting);			
			String filename = testingPro;//.replace("git://github.com/", "").replace(".git", "").replace("/", "__");			
			String tmp = this.recDir + filename;						
			recommendationFile = reader.readRecommendationFile(tmp,numItems);
			
			if(recommendationFile.contains(item1) || recommendationFile.contains(item2))count++;										
		}	
		
//		System.out.println("Number of hits: " + count);
		
//		int total = keyTestingProjects.size();			
			
		return count;
	}
	
	
	
	
	
}
