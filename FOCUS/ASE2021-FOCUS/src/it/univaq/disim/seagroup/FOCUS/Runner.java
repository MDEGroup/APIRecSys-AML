package it.univaq.disim.seagroup.FOCUS;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;


class ValueComparator implements Comparator<String> {

    Map<String, Float> base;
    public ValueComparator(Map<String, Float> base) {
        this.base = base;
    }

    // Note: this comparator imposes orderings that are inconsistent with equals.    
    public int compare(String a, String b) {
        if (base.get(a) >= base.get(b)) {
            return -1;
        } else {
            return 1;
        } // returning 0 would merge keys
    }
}



public class Runner {
	
	private String srcDir;	
	private String subFolder;
	private int numOfProjects;
	private int numOfNeighbours;
	private int numOfFolds;
	private int conf;
	
	public Runner(String srcDir){
		this.srcDir = srcDir;
	}
	

			
	
	public void run(){		
		System.out.println("FOCUS: A Context-Aware Recommender System!");
		
		
		
		tenFoldCrossValidation();
		
		
		
		
//		leaveOneOutValidation();
//		System.out.println(System.currentTimeMillis());		
//		testLinkedHashMap();
		
	}
		
	/*Ten-fold cross validation*/
		
	
	
	
	public void tenFoldCrossValidation() {
		
//		numOfProjects = 610;		
//		numOfNeighbours = 2;
		
		numOfProjects = 2600;		
		numOfNeighbours = 3;
		
		numOfFolds = 10;
		
		conf = 2; /*Configuration:  1 - C1.1, 
									2 - C1.2, 
									3 - C2.1, 
									4 - C2.2*/
					
				
		int step = (int)numOfProjects/numOfFolds;								
								
		for(int i=0;i<numOfFolds;i++) {
			
			int trainingStartPos1 = 1;			
			int trainingEndPos1 = i*step;			
			int trainingStartPos2 = (i+1)*step+1;
			int trainingEndPos2 = numOfProjects;
			int testingStartPos = 1+i*step;
			int testingEndPos =   (i+1)*step;
			
			int k=i+1;
			
			subFolder = "Round" + Integer.toString(k);
			
//			System.out.println("Configuration here: " + conf);
			
			SimilarityCalculator calculator = new SimilarityCalculator(srcDir,this.subFolder, conf,
					trainingStartPos1,
					trainingEndPos1,
					trainingStartPos2,
					trainingEndPos2,
					testingStartPos,
					testingEndPos);			
			calculator.ComputeProjectSimilarity();
			
			ContextAwareRecommendation engine = new ContextAwareRecommendation(this.srcDir,this.subFolder,numOfNeighbours,testingStartPos,testingEndPos);
			engine.Recommendation();
			
			
//			APIUsagePatternMatcher matcher = new APIUsagePatternMatcher(this.srcDir,this.subFolder,
//					trainingStartPos1,
//					trainingEndPos1,
//					trainingStartPos2,
//					trainingEndPos2,
//					testingStartPos,
//					testingEndPos);
			
//			matcher.searchAPIUsagePatterns();
		
			
//			APIUsagePatternEvaluation eval = new APIUsagePatternEvaluation(this.srcDir, this.subFolder, testingStartPos, testingEndPos);
//			eval.ComputeSimilarityScore();
			
		}
		
	}
	
	
	
	
	
	
	
	
	public void leaveOneOutValidation() {		
		numOfProjects = 200;		
		numOfNeighbours = 2;
		numOfFolds = numOfProjects;		
		conf = 0;
		int step = 1;
		
		
		long start = System.currentTimeMillis();
		
		for(int i=0;i<numOfFolds;i++) {			
			int trainingStartPos1 = 1;			
			int trainingEndPos1 = i*step;			
			int trainingStartPos2 = (i+1)*step+1;
			int trainingEndPos2 = numOfProjects;
			int testingStartPos = 1+i*step;
			int testingEndPos =  (i+1)*step;
			
			int k=i+1;
			subFolder = "Round1";		
			
			
			
			SimilarityCalculator calculator = new SimilarityCalculator(this.srcDir,this.subFolder, conf,
					trainingStartPos1,
					trainingEndPos1,
					trainingStartPos2,
					trainingEndPos2,
					testingStartPos,
					testingEndPos);
			calculator.ComputeProjectSimilarity();	
				
			
			ContextAwareRecommendation engine = new ContextAwareRecommendation(this.srcDir,this.subFolder,numOfNeighbours,testingStartPos,testingEndPos);
			engine.Recommendation();		
			
		}		
		
		long time = System.currentTimeMillis() - start;
		
		
		System.out.println("The total time is: " + time);
	}
	
	
	
	public static void main(String[] args) {	
		Option src_option = new Option("src", true, "The soruce folder. It is an exisiting path.");
		Options options = new Options();
		options.addOption(src_option);
		HelpFormatter formatter = new HelpFormatter();
		final CommandLineParser parser = new GnuParser();

		try {
			final CommandLine line = parser.parse(options, args);
			String src = "";
			if (line.hasOption(src_option.getOpt())) {
				src = line.getOptionValue(src_option.getOpt());
				Runner runner = new Runner(src);		
				runner.run();		
				return;
			} else {
				formatter.printHelp("Runner", options);
				throw new Exception();
			}
		} catch (Exception e) {
			formatter.printHelp("Runner", options);
		}
		
	}	
	
}
