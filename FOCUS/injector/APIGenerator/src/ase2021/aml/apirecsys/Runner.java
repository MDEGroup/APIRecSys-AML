package ase2021.aml.apirecsys;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;


public class Runner {
	
	private String srcDir;	
	private String subFolder;
	private int numOfProjects;
	private int numOfNeighbours;
	private int numOfFolds;
	private int conf;	
	private int numOfItems;
	
	public Runner(){
		loadConfigurations();
		DataReader reader = new DataReader();
		String inputFile = "List.txt";		
		numOfProjects = reader.getNumberOfProjects(this.srcDir + inputFile);
		numOfFolds = 10;
	}
	
	
	public void loadConfigurations(){		
		Properties prop = new Properties();				
		try {
			prop.load(new FileInputStream("evaluation.properties"));		
			this.srcDir=prop.getProperty("sourceDirectory");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}	
		return;
	}
			
	
	public void run(){		
		System.out.println("Generating fake projects!");	
		
		double alpha = 0.05;
		double gamma = 0.6;
		
		int num = 1;				
		populateFakeProjects(alpha,gamma,num);
		
	}
		
		
	public void populateFakeProjects(double alpha, double gamma, int num) {	
		
		System.out.println("Number of projects: " + Integer.toString(numOfProjects));
		
		numOfNeighbours = 4;
		numOfFolds = 10;		
		
		int step = (int)numOfProjects/numOfFolds;			
								
		for(int i=0;i<numOfFolds;i++) {		
			int testingStartPos = 1+i*step;
			int testingEndPos =   (i+1)*step;
			
			int k=i+1;	
						
			FakeProjectCreator creator = new FakeProjectCreator(this.srcDir,testingStartPos,testingEndPos);
			creator.insertFakeAPIs(alpha,gamma,num);			
		}		
	}
		

		
	public static void main(String[] args) {	
		Runner runner = new Runner();			
		runner.run();				    		    
		return;
	}	
	
}
