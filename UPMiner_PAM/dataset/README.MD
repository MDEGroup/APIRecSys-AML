# The dataset structure

This folder contains the datasets employed to perform the experiments using the parameter settings. As discussed in the paper, we used three main paramenters to build the datasets:

 - alpha: is the ratio of projects injected with fake APIs (%);
 - omega: is the number of fake APIs injected to each declaration;
 - beta: is the ratio of methods in a project getting fake APIs (%)

Thus, we resemble this structure in the definition of the different dataset. We report just the structure of the *AML_alpha_5* folder as explanatory example.

```
ARFF_fake_datasets
    .
    |--- AML_alpha_5         
	    |--- omega_1
		    |---beta_40
			    |--- AML_dataset_root_5
			    |---results_PAM_5
			    |---results_UPMiner_5
			|---beta_50
			    |--- AML_dataset_root_5
			    |---results_PAM_5
			    |---results_UPMiner_5
			|---beta_60
				|--- AML_dataset_root_5
			    |---results_PAM_5
			    |---results_UPMiner_5
		|--- omega_2
		    |---beta_40
			    |--- AML_dataset_root_5
			    |---results_PAM_5
			    |---results_UPMiner_5
			|---beta_50
			    |--- AML_dataset_root_5
			    |---results_PAM_5
			    |---results_UPMiner_5
			|---beta_60
				|--- AML_dataset_root_5
			    |---results_PAM_5
			    |---results_UPMiner_5
      
```
Note that the *AML_dataset_root_5* contained in each subfolder is computed by setting corresponding parameters i.e., alpha, omega, and beta. In such a way, you can use them directly to replicate the results by exploiting the [injector](https://github.com/ASE2021-AML/APIRecSys/tree/master/UPMiner_PAM/injector) tool. 
