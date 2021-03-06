package it.univaq.disim.seagroup.FOCUS;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;


public class ContextAwareRecommendation {
	
	private String srcDir;
	private String groundTruth;
	private String simDir;
	private String recDir;
	private String subFolder;
	 
	private int testingStartPos;
	private int testingEndPos;
	
	private int numOfNeighbours;
	private int numOfRows;
	private int numOfCols;
	private int numOfSlices;
		
	public ContextAwareRecommendation(String sourceDir, String suFolder, int numOfNeighbours,	int teStartPos, int teEndPos) {			
		this.srcDir = sourceDir;
		this.subFolder = suFolder;		
		this.numOfNeighbours = numOfNeighbours;		
		this.groundTruth = this.srcDir + subFolder + "/" + "GroundTruth" + "/";
		this.recDir = this.srcDir + subFolder + "/" + "Recommendations" + "/";
		this.simDir = this.srcDir + subFolder + "/" + "Similarities" + "/";
		this.testingStartPos = teStartPos;
		this.testingEndPos = teEndPos;	
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/*Build a 3-D user-item-context ratings matrix*/
	
	public byte[][][] BuildUserItemContextMatrix(String testingPro, List<String> listOfProjects, List<String> listOfMethodInvocations) {		
				
		DataReader reader = new DataReader();
				
		String filename = testingPro;
		
		String tmp = this.simDir + filename;		
				
		Map<Integer, String> simProjects =  reader.getMostSimilarProjects(tmp, numOfNeighbours);		
		Set<Integer> keySet = simProjects.keySet();			
		Set<String> keySet2 = null;
		Map<String,Set<String>> tmpMethodInvocations = new HashMap<String,Set<String>>();
		
		
		Map<String,Map<String,Set<String>>> allProjects = new HashMap<String,Map<String,Set<String>>>();
		
		Set<String> allMDs = new HashSet<String>();		
		Set<String> allMIs = new HashSet<String>();
			
		int sz = keySet.size();
		/*An ordered list of all projects*/
		List<String> listOfPRs = new ArrayList<String>();
		
		for(int key=0;key<sz;key++) {				
			String project = simProjects.get(key);				
			filename = project;
			
			tmpMethodInvocations =  reader.getProjectDetails(this.srcDir,filename);
			
//			tmpMethodInvocations =  reader.getProjectDetailsFromARFF2(this.srcDir,filename);	
			
			
			/*all method declarations of a project/library*/			
			keySet2 = tmpMethodInvocations.keySet();						
			allMDs.addAll(keySet2);
			for(String key2:keySet2) {			
				allMIs.addAll(tmpMethodInvocations.get(key2));				
			}
			
			allProjects.put(project, tmpMethodInvocations);			
			listOfPRs.add(project);						
		}
		
		/*the slice for the testing project is located at the end of the matrix*/		
		listOfPRs.add(testingPro);
				
		/*read the corresponding grouth-truth file to remove the invocations in there*/		
		Set<String> groundTruthInvocations = reader.getGroundTruthInvocations(this.groundTruth,testingPro);
			
		Map<String,Set<String>> testingMIs = new HashMap<String,Set<String>>();
		String testingMD = "";
		
		/*add the testing project, exclude ground-truth method invocations*/				
			
		tmpMethodInvocations =  reader.getTestingProjectDetails(this.srcDir,testingPro,groundTruthInvocations,testingMIs);	
		
		Set<String> set1 =  tmpMethodInvocations.keySet();
		Set<String> set11 = new HashSet<String>();
						
		for(String s1:set1)set11.add(s1);
		
		allMDs.addAll(set1);
				
		for(String s:set1) {			
			allMIs.addAll(tmpMethodInvocations.get(s));				
		}
				
		/*all method declarations: include also the testing */
		
		Set<String> set2 = testingMIs.keySet();
		Set<String> tmpSet = null;		
		for(String s:set2) {
			testingMD = s;	
			tmpSet = testingMIs.get(testingMD);	
			break;						
		}
				
		tmpMethodInvocations.putAll(testingMIs);
		allProjects.put(testingPro, tmpMethodInvocations);
						
		
		/*Convert to an ordered list of all method declarations*/
		List<String> listOfMDs = new ArrayList<String>(allMDs);		
		/*to make sure that the testing method declaration locates at the end of the list*/		
		if(listOfMDs.contains(testingMD))listOfMDs.remove(listOfMDs.indexOf(testingMD));
		listOfMDs.add(testingMD);		
						
		/*Convert to an ordered list of all method invocations*/
		List<String> listOfMIs = new ArrayList<String>(allMIs);
		/*to make sure that all testing method invocations locate at the end of the list*/
		for(String testingMI:tmpSet)if(listOfMIs.contains(testingMI))listOfMIs.remove(listOfMIs.indexOf(testingMI));
		for(String testingMI:tmpSet)listOfMIs.add(testingMI);		
				
		numOfSlices = listOfPRs.size();
		numOfRows = listOfMDs.size();
		numOfCols = listOfMIs.size();						
		
//		System.out.println("Size: " + numOfSlices + " x " + numOfRows + " x " + numOfCols);
		
		byte UserItemContextMatrix[][][] = new byte[this.numOfSlices][this.numOfRows][this.numOfCols];
		Set<String> myMIs = new HashSet<String>();		
		String currentMD = "", currentMI = "";
				
		
		Map<String,Set<String>> myMDs = new HashMap<String,Set<String>>();
						
		/*populate all cells in the user-item-context ratings matrix using 1 and 0*/		
		for(int i=0;i<numOfSlices-1;i++) {			
			String currentPro = listOfPRs.get(i);
			myMDs = allProjects.get(currentPro);
			for(int j=0;j<numOfRows;j++) {
				currentMD = listOfMDs.get(j);
				if(myMDs.containsKey(currentMD)) {
					myMIs = myMDs.get(currentMD); 
					for(int k=0;k<numOfCols;k++) {
						currentMI = listOfMIs.get(k);
						if(myMIs.contains(currentMI)) {
							UserItemContextMatrix[i][j][k]=(int) 1.0;
						}
						else UserItemContextMatrix[i][j][k]=(int) 0.0;
					}				
				} else	{
					for(int k=0;k<numOfCols;k++) UserItemContextMatrix[i][j][k]=(int) 0.0;					
				}
			}
		}
				
		/*this is the testing project, it is the last slice of the 3-D matrix*/
		myMDs = allProjects.get(testingPro);
		
		for(int j=0;j<numOfRows-1;j++) {						
			currentMD = listOfMDs.get(j);	
			if(myMDs.containsKey(currentMD)) {
				myMIs = myMDs.get(currentMD);				
				for(int k=0;k<numOfCols;k++) {
					currentMI = listOfMIs.get(k);
					if(myMIs.contains(currentMI)) {
						UserItemContextMatrix[numOfSlices-1][j][k]=(int) 1.0;
					}
					else UserItemContextMatrix[numOfSlices-1][j][k]=(int) 0.0;
				}			
			} else	{
				for(int k=0;k<numOfCols;k++) UserItemContextMatrix[numOfSlices-1][j][k]=(int) 0.0;					
			}		
		}
			
		
		
		currentMD = listOfMDs.get(numOfRows-1);
		
//		System.out.println(currentMD);
		
		myMIs = myMDs.get(currentMD);
		
		
		
		
		int c = 0;
		
//		System.out.println(myMIs.size() + "==========" );
				
//		for(String s:myMIs) {
//			System.out.println(myMIs.size() + "==========" + s);
//			c++;
//		}
		
		
		
		
		
		
		for(int k=0;k<numOfCols;k++) {
			currentMI = listOfMIs.get(k);
			
//			System.out.println(k + "\t" + currentMI);
			
			if(myMIs.contains(currentMI)) {
				UserItemContextMatrix[numOfSlices-1][numOfRows-1][k]=(int) 1.0;
			}
			else UserItemContextMatrix[numOfSlices-1][numOfRows-1][k]=(int) -1.0;			
		}
				
		for(String l:listOfPRs)listOfProjects.add(l);
		for(String l:listOfMIs)listOfMethodInvocations.add(l);
		
		return UserItemContextMatrix;		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	/*Recommends invocations to test projects using the collaborative-filtering technique*/
	
	
	/*Recommends invocations to test projects using the collaborative-filtering technique*/
	
	public void Recommendation(){
			
		DataReader reader = new DataReader();			
		Map<Integer,String> testingProjects = new HashMap<Integer,String>();
		testingProjects = reader.readProjectList(this.srcDir + "List.csv",this.testingStartPos,this.testingEndPos);						
		Set<Integer> keyTestingProjects = testingProjects.keySet();						
		String testingPro = "", filename="";
		
		int numOfSimilarMethods = 6;
														
		for(Integer keyTesting:keyTestingProjects){		
		
			Map<String, Float> recommendations = new HashMap<String, Float>();
			List<String> listOfPRs = new ArrayList<String>();
			List<String> listOfMIs = new ArrayList<String>();
								
			testingPro = testingProjects.get(keyTesting);		
			filename = testingPro;		
			String tmp = this.simDir + filename;		
			Map<String, Double> simScores =  reader.getSimilarScores(tmp, numOfNeighbours);		
						
			byte UserItemContextMatrix[][][] = BuildUserItemContextMatrix(testingPro, listOfPRs, listOfMIs);
			
			double avgMdRating = 0;
//			double val = 0;
						
			/*Select most similar method declaration to the testing method declaration: those that contain the testing method invocation (only one)*/
									
			System.out.println("Project: " + testingPro);
						
			int vector1[] = new int[numOfCols];
			int vector2[] = new int[numOfCols];
						
			/*This is the testing method declaration*/
			
			
			int tmp2 = 0;
			for(int k=0;k<numOfCols;k++) {								
				vector1[k]=UserItemContextMatrix[numOfSlices-1][numOfRows-1][k];
				if(vector1[1]!=1)tmp2++;
//				System.out.println("abc : " + vector1[1]);
			}
			
//			System.out.println("count : " + tmp2 + " " + numOfCols);
			
			
			
			float sim = 0;
			Map<String,Float> mdSimScores = new HashMap<String,Float>();
									
			for(int i=0;i<numOfSlices-1;i++) {				
				for(int j=0;j<numOfRows;j++) {		
					vector2 = new int[numOfCols];
					for(int k=0;k<numOfCols;k++) {
						vector2[k]=UserItemContextMatrix[i][j][k];
//						if(vector2[k]!=0)System.out.println("vector2 " + vector2[k]);
					}										
					SimilarityCalculator simCalculator = new SimilarityCalculator(this.srcDir);
					
//					System.out.println("vector1 length :"  +vector1.length);
					
					sim = simCalculator.JaccardSimilarity(vector1,vector2);
					
//					System.out.println("methodSim " + sim);
					
					String key = Integer.toString(i) + "#" + Integer.toString(j);
					mdSimScores.put(key, sim);
//					if(sim!=0)System.out.println("Method similarity " + sim);
				}				
			}
			
			
			ValueComparator bvc =  new ValueComparator(mdSimScores);        
			TreeMap<String,Float> sorted_map = new TreeMap<String,Float>(bvc);
			sorted_map.putAll(mdSimScores);
			Set<String> keySet2 = sorted_map.keySet();			
			
			Map<String,Float> newSimScores = new HashMap<String,Float>();
			int count=0;
			for(String key:keySet2) {
				newSimScores.put(key, mdSimScores.get(key));
				count++;
				//Get a fixed number of similar method declarations
				if(count==numOfSimilarMethods)break;
			}
						
			keySet2 = newSimScores.keySet();	
			
			float ratings[] = new float[numOfCols];
//			float ratings[] = new float[numOfCols-1];
			
			
			
			
			
			
			
			
			
			for(int k=0;k<numOfCols;k++) {
								
				if(UserItemContextMatrix[numOfSlices-1][numOfRows-1][k]==(int)-1.0) {
					int slice=0,row=0;			
					avgMdRating = 0;
													
					double totalMethodSim=0;
					double totalProjectSim=0;
								
					
					
					
					/*go through all similar projects (context)*/					
					for(String key:keySet2) {					
														
						String line = key.trim();
												
						String parts[] = line.split("#");					
						slice = Integer.parseInt(parts[0]);						
						row = Integer.parseInt(parts[1]);	
						
						avgMdRating = 0;					
						/*go through all columns of a single row*/										
						for(int m=0;m<numOfCols;m++)avgMdRating+=UserItemContextMatrix[slice][row][m];			
						/*get the average rating of a user (method declaration)*/
						avgMdRating/=numOfCols;
	
						String project = listOfPRs.get(slice);
												
						
						double projectSim = simScores.get(project);
						totalProjectSim+=projectSim;
						
						double val = projectSim * UserItemContextMatrix[slice][row][k];
						
						
						
//						sim2 = simScores.get(project);						
//						val += sim2*UserItemContextMatrix[slice][row][k];
						
												
						double methodSim = newSimScores.get(key);
//						System.out.println("methodSim " + methodSim);
						totalMethodSim+=methodSim;						
						ratings[k] += (val-avgMdRating)*methodSim;						
					}
									
	
					if(totalMethodSim!=0)ratings[k]/=totalMethodSim;
									
					double activeMDrating = 0.8;	
					
					ratings[k]+=activeMDrating;					
					String methodInvocation=listOfMIs.get(k);
					recommendations.put(methodInvocation, ratings[k]);
				}
				
			
				
				
				
				
				
				
				
			}
					
			
			bvc =  new ValueComparator(recommendations);        
			sorted_map = new TreeMap<String,Float>(bvc);
			sorted_map.putAll(recommendations);				
			keySet2 = sorted_map.keySet();				
		
			filename = testingPro;
			
			try {
				tmp = this.recDir + filename;
				BufferedWriter writer = new BufferedWriter(new FileWriter(tmp));
				int numLine = 0;
				for(String key:keySet2){					
					String content = key + "\t" + recommendations.get(key);				
					writer.append(content);							
					writer.newLine();
					writer.flush();
					numLine++;
					if(numLine>50)break;
				}
				writer.close();							
			} catch (IOException e) {
				e.printStackTrace();
			}			
		}		
		return;
	}
	
	
		
//	public void Recommendation(){
//			
//		DataReader reader = new DataReader();			
//		Map<Integer,String> testingProjects = new HashMap<Integer,String>();
//		testingProjects = reader.readProjectList(this.srcDir + "List.csv",this.testingStartPos,this.testingEndPos);						
//		Set<Integer> keyTestingProjects = testingProjects.keySet();						
//		String testingPro = "", filename="";		
//														
//		for(Integer keyTesting:keyTestingProjects){		
//		
//			Map<String, Float> recommendations = new HashMap<String, Float>();
//			List<String> listOfPRs = new ArrayList<String>();
//			List<String> listOfMIs = new ArrayList<String>();
//								
//			testingPro = testingProjects.get(keyTesting);		
//			filename = testingPro;		
//			String tmp = this.simDir + filename;		
//			Map<String, Double> simScores =  reader.getSimilarScores(tmp, numOfNeighbours);		
//						
//			byte UserItemContextMatrix[][][] = BuildUserItemContextMatrix(testingPro, listOfPRs, listOfMIs);
//			
//			double avgMdRating = 0;
//			double val = 0;
//						
//			/*Select most similar method declaration to the testing method declaration: those that contain the testing method invocation (only one)*/
//									
//			System.out.println("Project: " + testingPro);
//						
//			int vector1[] = new int[numOfCols];
//			int vector2[] = new int[numOfCols];
//						
//			/*This is the testing method declaration*/
//			
//			
//			for(int k=0;k<numOfCols;k++) {								
//				vector1[k]=UserItemContextMatrix[numOfSlices-1][numOfRows-1][k];				
//			}
//			
//			float sim = 0;
//			Map<String,Float> mdSimScores = new HashMap<String,Float>();
//									
//			for(int i=0;i<numOfSlices-1;i++) {				
//				for(int j=0;j<numOfRows;j++) {		
//					vector2 = new int[numOfCols];
//					for(int k=0;k<numOfCols;k++) {
//						vector2[k]=UserItemContextMatrix[i][j][k];
//					}										
//					SimilarityCalculator simCalculator = new SimilarityCalculator(this.srcDir);					
//					sim = simCalculator.JaccardSimilarity(vector1,vector2);					
//					String key = Integer.toString(i) + "#" + Integer.toString(j);
//					mdSimScores.put(key, sim);
////					if(sim!=0)System.out.println("Method similarity " + sim);
//				}				
//			}
//			
//			
//			ValueComparator bvc =  new ValueComparator(mdSimScores);        
//			TreeMap<String,Float> sorted_map = new TreeMap<String,Float>(bvc);
//			sorted_map.putAll(mdSimScores);
//			Set<String> keySet2 = sorted_map.keySet();			
//			
//			Map<String,Float> newSimScores = new HashMap<String,Float>();
//			int count=0;
//			for(String key:keySet2) {
//				newSimScores.put(key, mdSimScores.get(key));
//				count++;
//				if(count>3)break;
//			}
//						
//			keySet2 = newSimScores.keySet();		
//			float vals[] = new float[numOfCols-1];
//			
//			
//			for(int k=0;k<numOfCols;k++) {
//								
//				if(UserItemContextMatrix[numOfSlices-1][numOfRows-1][k]==(int)-1.0) {
//					int slice=0,row=0;			
//					avgMdRating = 0;
//													
//					double sim2=0,sim3=0,totalSim=0;
//					/*go through all similar projects (context)*/
//					
//					for(String key:keySet2) {						
//						val = 0;									
//						String line = key.trim();
//												
//						String parts[] = line.split("#");					
//						slice = Integer.parseInt(parts[0]);						
//						row = Integer.parseInt(parts[1]);	
//						
//						avgMdRating = 0;					
//						/*go through all columns of a single row*/										
//						for(int m=0;m<numOfCols;m++)avgMdRating+=UserItemContextMatrix[slice][row][m];			
//						/*get the average rating of a user (method declaration)*/
//						avgMdRating/=numOfCols;
//	
//						String project = listOfPRs.get(slice);
//						sim2 = simScores.get(project);				
//						val = sim2*UserItemContextMatrix[slice][row][k];
//						sim3 = newSimScores.get(key);
//						totalSim+=sim3;						
//						vals[k] += (val-avgMdRating)*sim3;						
//					}
//	
//					if(totalSim!=0)vals[k]/=totalSim;
//									
//					double activeMDrating = 0.8;	
//					vals[k]+=activeMDrating;					
//					String methodInvocation=listOfMIs.get(k);
//					recommendations.put(methodInvocation, vals[k]);
//				}
//			}
//					
//			
//			bvc =  new ValueComparator(recommendations);        
//			sorted_map = new TreeMap<String,Float>(bvc);
//			sorted_map.putAll(recommendations);				
//			keySet2 = sorted_map.keySet();				
//		
//			filename = testingPro;
//			
//			try {
//				tmp = this.recDir + filename;
//				BufferedWriter writer = new BufferedWriter(new FileWriter(tmp));				
//				for(String key:keySet2){					
//					String content = key + "\t" + recommendations.get(key);				
//					writer.append(content);							
//					writer.newLine();
//					writer.flush();					
//				}
//				writer.close();							
//			} catch (IOException e) {
//				e.printStackTrace();
//			}			
//		}		
//		return;
//	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public double CosineSimilarity(double[] vector1, double[] vector2) {        
        double sclar = 0, norm1 = 0, norm2 = 0;
        int length = vector1.length;       
        for(int i=0;i<length;i++) sclar+=vector1[i]*vector2[i];
        for(int i=0;i<length;i++) norm1+=vector1[i]*vector1[i];
        for(int i=0;i<length;i++) norm2+=vector2[i]*vector2[i];
        double ret = 0;
        double norm = norm1*norm2;                
        if(norm>0 && sclar>0) ret = (double)sclar / Math.sqrt(norm);        
        else ret =0;
        return ret;
    }

}
