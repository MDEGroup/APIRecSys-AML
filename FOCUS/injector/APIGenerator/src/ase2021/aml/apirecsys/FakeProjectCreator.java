package ase2021.aml.apirecsys;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class FakeProjectCreator {	
	
	private String srcDir;
		
	private int testingStartPos;
	private int testingEndPos;
		
	public FakeProjectCreator(String sourceDir) {
		this.srcDir = sourceDir;
	}
			
	public FakeProjectCreator(String sourceDir, int teStartPos, int teEndPos) {		
		this.srcDir = sourceDir;		
		this.testingStartPos = teStartPos;
		this.testingEndPos = teEndPos;
		
	}
	
	
	
	
	
	/*insert fake APIs to a set of projects*/
	
	public void insertFakeAPIs(double alpha, double gamma, int num, String output) throws IOException {	
		
		DataReader reader = new DataReader();		
		
		Map<Integer,String> testingProjects = new HashMap<Integer,String>();
		Path copied = Paths.get(output, "List.csv");
		Path originalPath = Paths.get(this.srcDir, "List.csv");
	    Files.copy(originalPath, copied, StandardCopyOption.REPLACE_EXISTING);
		testingProjects = reader.readProjectList(this.srcDir + "List.csv",this.testingStartPos,this.testingEndPos);
				
		
		Set<Integer> keyTestingProjects = testingProjects.keySet();		
		
		String fakeAPI = "malware/fakeAPI/Stealer/sendData(java.lang.String,java.lang.String)";						
		
		int numOfProjects = keyTestingProjects.size();	
			
		int numOfInfectedProjects = (int) Math.round(alpha * numOfProjects);
		
		/*randomly insert to a certain number of projects.
		 * The shuffle() function mixes all the keys, so as to create a random order*/
		
		List<Integer> list = new ArrayList<Integer>(keyTestingProjects);			
		Collections.shuffle(list);					
		Object[] array = new Object[list.size()];
		list.toArray(array);		
					
		for(int i=0;i<numOfInfectedProjects;i++) {
			int key = (Integer) array[i];
			String testingPro = testingProjects.get(key);			
			String filename = testingPro;				
			System.out.println(filename);
			insertFakeAPI2Project(this.srcDir, filename, fakeAPI, gamma, num, output);		
		}		
				
		return;
	}
	
	
	
	
	
		
	
	/*first step: insert to all declarations*/
	/*the ratio of declarations that get injected with the fake API*/
	
	public void insertFakeAPI2Project(String path, String filename, String fakeAPI, double ratio, int num, String output) {
		
		String line = null;
		
		LinkedHashMap<String,List<String>> map = new LinkedHashMap<String,List<String>>();
		
//		Map<String,ArrayList<String>> map = new HashMap<String,ArrayList<String>>();
				
		List<String> set = null;
		
		int numOfDeclarations = 0;
		
		int numOfInfectedDeclarations = 0;
		
		try {			
			BufferedReader reader = new BufferedReader(new FileReader(path + filename));						
			while ((line = reader.readLine()) != null) {											
				String[] parts = line.split("#");	
				String md = parts[0].trim();
				String mi = parts[1].trim();								
				if(map.containsKey(md))set = map.get(md);
				else set = new ArrayList<String>();								
				set.add(mi);			
				map.put(md,set);
			}			
			reader.close();
									
			Set<String> keySet = map.keySet();		
			
			numOfDeclarations = keySet.size();
			
			numOfInfectedDeclarations = (int) Math.round(ratio * numOfDeclarations);						
			
//			System.out.println("num: " + numOfInfectedDeclarations + "\t" + numOfDeclarations);
			
			/*randomly insert into the declarations*/			
						
			List<String> list = new ArrayList<String>(keySet);			
			Collections.shuffle(list);					
			Object[] array = new Object[list.size()];
			list.toArray(array);
									
			for(int i=0;i<numOfInfectedDeclarations;i++) {
				String key = (String) array[i];
				set = map.get(key);
				set.add(fakeAPI);				
				if(num==2) {
					String fakeAPI2 = "malware/fakeAPI/Stealer/sendData(java.lang.String)";
					set.add(fakeAPI2);					
				}
				map.put(key,set);
			}				
						
			
			/*save to file*/
			BufferedWriter writer = new BufferedWriter(new FileWriter(output + filename));
						
			keySet = map.keySet();
						
			for(String key:keySet) {				
				set = map.get(key);
				String content = "";
				for(String s:set) {
					content = key + "#" + s;
					writer.append(content);							
					writer.newLine();
					writer.flush();	
				}							
			}
						
			writer.close();		
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}	
		
		return;		
	}	
	
}
