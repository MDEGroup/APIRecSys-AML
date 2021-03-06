package ase2021.aml.apirecsys;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class Runner {

	private int numOfProjects;
	private int numOfFolds;

	public Runner(String src) {

		DataReader reader = new DataReader();

		String inputFile = "List.csv";
		Path listPaths = Paths.get(src, inputFile);
		
		numOfProjects = reader.getNumberOfProjects(listPaths.toString());
		numOfFolds = 10;
	}

	private void createListFile(String src) {
		File dir = new File(src);
		File[] directoryListing = dir.listFiles();
		if (directoryListing != null) {
			try(BufferedWriter writer = new BufferedWriter(new FileWriter(Paths.get(src, "List.csv").toFile()))) {
				for (File child : directoryListing) {
					if(!child.getName().equals("List.csv") && !child.getName().startsWith("Round"))
						writer.write(child.getName() + "\n");
				}
			}
			catch(Exception e) {
				
			}
		} else {
			// Handle the case where dir is not really a directory.
			// Checking dir.isDirectory() above would not be sufficient
			// to avoid race conditions with another process that deletes
			// directories.
		}

	}

	public void populateFakeProjects(double alpha, double gamma, int num, String src, String output)
			throws IOException {
		numOfFolds = 10;
		geretateFolders(output, numOfFolds);
		System.out.println("Number of projects: " + Integer.toString(numOfProjects));

		int step = (int) numOfProjects / numOfFolds;

		for (int i = 0; i < numOfFolds; i++) {
			int testingStartPos = 1 + i * step;
			int testingEndPos = (i + 1) * step;
			FakeProjectCreator creator = new FakeProjectCreator(src, testingStartPos, testingEndPos);
			creator.insertFakeAPIs(alpha, gamma, num, output);
		}
		createListFile(output);
	}

	private void geretateFolders(String output, int numOfFold) {
		for (int i = 0; i < numOfFold; i++) {
			Path roundFolder = Paths.get(output, "Round" + (i+1));
			try {
				if (!Files.exists(roundFolder))
					Files.createDirectories(roundFolder);
				Path tmpTruth = Paths.get(roundFolder.toString(), "GroundTruth");
				if (!Files.exists(tmpTruth))
					Files.createDirectories(tmpTruth);

				tmpTruth = Paths.get(roundFolder.toString(), "PrecisionRecall");
				if (!Files.exists(tmpTruth))
					Files.createDirectories(tmpTruth);

				tmpTruth = Paths.get(roundFolder.toString(), "Recommendations");
				if (!Files.exists(tmpTruth))
					Files.createDirectories(tmpTruth);

				tmpTruth = Paths.get(roundFolder.toString(), "Similarities");
				if (!Files.exists(tmpTruth))
					Files.createDirectories(tmpTruth);

				tmpTruth = Paths.get(roundFolder.toString(), "TestingInvocations");
				if (!Files.exists(tmpTruth))
					Files.createDirectories(tmpTruth);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public static void main(String[] args) {
		Option alpha_option = new Option("alpha", true, "The alpha value. It is an double value.");
		Option beta_option = new Option("beta", true, "The beta value. It is an double value.");
		Option omega_option = new Option("omega", true, "The omega value. It is an integer value.");
		Option src_option = new Option("src", true, "The soruce folder. It is an exisiting path.");
		final CommandLineParser parser = new GnuParser();
		HelpFormatter formatter = new HelpFormatter();
		Options options = new Options();
		options.addOption(alpha_option);
		options.addOption(beta_option);
		options.addOption(omega_option);
		options.addOption(src_option);
		try {
			final CommandLine line = parser.parse(options, args);
			double alfa = 0;
			double beta = 0;
			int omega = 0;
			String out;
			String src = "";
			if (line.hasOption(alpha_option.getOpt())) {
				alfa = Double.parseDouble(line.getOptionValue(alpha_option.getOpt()));
			} else {
				formatter.printHelp("Runner", options);
				throw new Exception();
			}

			if (line.hasOption(beta_option.getOpt())) {
				beta = Double.parseDouble(line.getOptionValue(beta_option.getOpt()));
			} else {
				formatter.printHelp("Runner", options);
				throw new Exception();
			}

			if (line.hasOption(omega_option.getOpt())) {
				omega = Integer.parseInt(line.getOptionValue(omega_option.getOpt()));
			} else {
				formatter.printHelp("Runner", options);
				throw new Exception();
			}
			if (line.hasOption(src_option.getOpt())) {
				src = line.getOptionValue(src_option.getOpt());
				out = src;
			} else {
				formatter.printHelp("Runner", options);
				throw new Exception();
			}



			Runner runner = new Runner(src);
			runner.populateFakeProjects(alfa, beta, omega, src, out);

		} catch (Exception e) {
			formatter.printHelp("Runner", options);
		}

	}

}
