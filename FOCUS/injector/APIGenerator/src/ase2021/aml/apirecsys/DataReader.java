package ase2021.aml.apirecsys;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;



public class DataReader {

	
	
	public DataReader() {		
		
	}
	
	public int getNumberOfProjects(String filename) {		
		int count = 0;
		String line = "";
		
		try {					
			BufferedReader reader = new BufferedReader(new FileReader(filename));
			
			while (((line = reader.readLine()) != null)) {				
				count++;
			}
			
			reader.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return count;		
	}

	
	
	/*read the list of all projects*/
	
	public Map<Integer, String> readProjectList(String filename){		
		Map<Integer,String> ret = new HashMap<Integer,String>();		
		String line="",repo="";
		int count=1;
				
		try {					
			BufferedReader reader = new BufferedReader(new FileReader(filename));					
			while (((line = reader.readLine()) != null)) {
				repo = line.trim();			
				ret.put(count,repo);				
				count++;				
			}			
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		
		
		return ret;				
	}
	
		
	/*read the list of some projects*/
	
	public Map<Integer, String> readProjectList(String filename, int startPos, int endPos){		
		Map<Integer,String> ret = new HashMap<Integer,String>();		
		String line="",repo="";
		int count=1;
		int id=startPos;
		
		try {					
			BufferedReader reader = new BufferedReader(new FileReader(filename));			
			while (count< startPos) {
				line = reader.readLine();
				count++;
			}			
			while (((line = reader.readLine()) != null)) {
				repo = line.trim();			
				ret.put(id,repo);				
				id++;
				count++;
				if(count>endPos)break;
			}			
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		
		
		return ret;				
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
		
	
	
	
	
	
	
	
		
	
	/*read the whole file*/
	
		
	
	public Map<Integer,String> readRecommendationFile(String filename) {							
		Map<Integer,String> ret = new HashMap<Integer,String>();	
		String line = null;		
		String[] vals = null;
		int id=1;

		try {
			BufferedReader reader = new BufferedReader(new FileReader(filename));					
			while ((line = reader.readLine()) != null) {										
				vals = line.split("\t");			
				String invocation = vals[0].trim();			
				ret.put(id, invocation);
				id++;
			}			
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
						
		return ret;		
	}
	

	
	

	
	
	
	
	/*read the ground-truth invocations for a given project*/
	
	public Map<Integer,String> readGroundTruthInvocations(String filename) {							
		Map<Integer,String> ret = new HashMap<Integer,String>();	
		String line = null;		
		String[] vals = null;
		int id=1;

		try {
			BufferedReader reader = new BufferedReader(new FileReader(filename));					
			while ((line = reader.readLine()) != null) {										
				vals = line.split("\t");			
				String invocation = vals[1].trim();			
				ret.put(id, invocation);
				id++;
			}			
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
						
		return ret;		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	/*read a specific number of lines from the file*/
	
	public Set<String> readRecommendationFile(String filename, int size) {							
		Set<String> ret = new HashSet<String>();	
		String line = null;		
		String[] vals = null;
		int count=0;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filename));					
			while ((line = reader.readLine()) != null) {										
				vals = line.split("\t");			
				String library = vals[0].trim();					
				ret.add(library);
				count++;
				if(count==size)break;
			}			
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
						
		return ret;		
	}
	
	
}