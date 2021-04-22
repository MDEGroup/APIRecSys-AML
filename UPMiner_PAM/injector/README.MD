##  How to generate infected datasets

This subpackage containts the needed functions to replicate the study conducted in the paper. To insert fake API invocations in the ARFF file format, you have to install **liac-arff** library in your Python environment available [here](https://github.com/renatopp/liac-arff). 
# Code structure
The **injector.py** file contains the following utility functions: 

 - *generate_fake_content(declaration, api_fake)*: It injects a fake API in a given declaration;
 - *inject_system(arff_obj, half_decl,api, api2)*: It makes used of the previous function to modify the specified ratio of ARFF file
 - *hit_ratio(file, num_rec, fake_1, fake_2)*: It computes the hit ratio value for a certain number of recommendation	

# Running example
	
To replicate the results obtained, you to run the following main functions on the prepared dataset described [here](https://github.com/ASE2021-AML/APIRecSys/tree/master/UPMiner_PAM/dataset).	
The two main functions are the following:

     main_arff(root, dest, beta, fake_api, fake_api2 )
    
where *root* folder contains the orginal ARFF files and *beta* is the ratio of the projects injected with fake APIs as described in the paper. *fake_api* and *fake_api2* are the two fake invocations that have to be specified in a specifici formato e.g., *org.my.api*. *dest* folder stores the obtained ARFF files. Given these, you can compute the corresponding hit_ratio by using the following function:

    main_hit(path_results, fake_1, fake_2)
in  which *path_results* contains the injected ARFF files obtained by running the previous function.

For instance, you can inject 40% of ARFF files as follows:

    main_arff(root='/path_dataset',dest='/your_dest_path', beta=40,fake_api='one.fake.api',fake_api2='another.fake.api')
