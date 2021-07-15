#  How to generate infected datasets

This subpackage contains the needed functions to replicate the study conducted in the paper. To insert fake API invocations in the ARFF file format, you have to install **liac-arff** library in your Python environment available [here](https://github.com/renatopp/liac-arff). Futhermore, we provide a CLI interface to interact with the tool. Thus you have to install also [**click**](https://click.palletsprojects.com/en/8.0.x/) library.

## Requirements
You can use the following commands to set the environment:

     pip install click
     pip install liac-arff
    
    

## Code structure
The **injector.py** is used to inject the fake api in the original dataset: 

 - *generate_fake_content(declaration, api_fake)*: It injects a fake API in a given declaration;
 - *inject_system(arff_obj, half_decl,api, api2)*: It makes used of the previous function to modify the specified ratio of ARFF file

To compute the hit ratio, we can use the **computeHitRatio.py** file and its function: 
 - *hit_ratio(file, num_rec, fake_1, fake_2)*: It computes the hit ratio value for a certain number of recommendation	

## Running example
	
To replicate the results obtained, you to run the following main functions on the prepared dataset described [here](https://github.com/ASE2021-AML/APIRecSys/tree/master/UPMiner_PAM/dataset).	
To inject the original dataset you can use the following command:

     $ python3 injector.py --root=../../initial_upminer_pam_dataset/ --dest=./dest --beta=40 --alpha=5 --fake_api=org.fake1 --fake_api2=org.fake2
    
where *root* folder contains the original ARFF files, *dest* is the destination folder, *beta* is the ratio of the projects injected, and *alpha* is the ratio of infected projects with fake APIs as described in the paper. *fake_api* and *fake_api2* are the two fake invocations that have to be specified in a specific format e.g., *org.my.api*. *dest* folder stores the obtained ARFF files. Given these, you can compute the corresponding hit_ratio by running the following command:

    $ python3 computeHitRatio.py --path_rec=../../recommendation_path/ --fake_api=org.fake1 --fake_api2=org.fake2)
in which *path_rec* contains the injected ARFF files obtained by running the previous function.

