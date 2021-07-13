---
author:
- Phuong T. Nguyen, Claudio Di Sipio, Juri Di Rocco, Davide Di Ruscio
- Massimiliano Di Penta
bibliography:
- references.bib
**README: How to experiment the artifacts**
---

ABSTRACT

This document provides exhaustive instructions to get the datasets, to
obtain, install, and execute the tools for replicating the experiments
described in the paper entitled *"A Reproduction of Adversarial Attacks
to API Recommender Systems: Time to Wake Up and Smell the Coffee?"*
accepted in the Technical Track of ASE 2021.
:::

# Introduction

API recommender systems have gained momentum in recent years as they
became more successful at suggesting API calls or code snippets. While
these systems have proven to be effective in terms of prediction
accuracy, there has been less attention for what concerns such
recommenders' resilience against adversarial attempts. In the accepted
paper [@ASE2021], we present an empirical investigation of adversarial
machine learning techniques and their possible influence on recommender
systems.

The evaluation performed on three state-of-the-art API recommender
systems, i.e., UP-Miner [@Wang2013Mining],
PAM [@Fowkes:2016:PPA:2950290.2950319], and FOCUS [@FOCUS2019] reveals a
worrying outcome: all of them are not immune to malicious data. To
evaluate the resilience of UP-Miner, PAM, and FOCUS, we use a dataset
containing Android apps' source code. We focused on Android apps because
they entail a typical scenario in which an infection can cause unwanted
consequences such as data leaks. The artifacts include the adversarial
API injectors, i.e., FOCUS and UP-Miner/PAM injectors and two datasets.
The former contains the API metadata extracted from 2,850 android
applications (it has been considered to evaluate the resilience of
FOCUS). Because UP- Miner and PAM suffer from scalability issues,the
latter considers a subset of 500 apps randomly extracted from the first
one.

# System Requirements

Table [\[tab:Requirements\]](#tab:Requirements){reference-type="ref"
reference="tab:Requirements"} specifies the hardware and software
requirements that a testing system needs to meet in order to be eligible
for the execution of the artifacts.

::: {.table*}
  **Name**           **Requirements**
  ------------------ --------------------------------------------------------------------------------------------------------------
  RAM                $\geq$ 8GB
  Operating System   A modern Linux system
  Java JRE           $\geq$ Java 8
  Apache Maven       $\geq$ Maven 3.0[ $\blacktriangleright$*Check if maven is needed*$\blacktriangleleft$ ]{style="color: gray"}
  Python             $\geq$ 3.7
  Rascal             $\geq$ 10.0
  Git                $\geq$ 2.0

[\[tab:Requirements\]]{#tab:Requirements label="tab:Requirements"}
:::

We developed and tested the tools involved in the experiments on
different Linux systems, although it should work without any issue on
any operating system. In the instructions, we assume a Linux system and
basic command line skills. The use of Rascal is optional.\
In our case, the testing platform is a PC with Linux $4.20.3$, Intel
Core i7-6700HQ CPU @ 2.60GHz and 16GB of RAM.

# The validation process[ $\blacktriangleright$*check the section title*$\blacktriangleleft$ ]{style="color: orange"}

This section gives a summary on the experiments applied in the paper. In
Section [3.1](#sec:dataset){reference-type="ref"
reference="sec:dataset"}, we briefly introduce the process we followed
to extract the datasets involved in the experiments. Afterwards, in
Section [3.2](#sec:Confs){reference-type="ref" reference="sec:Confs"} we
recall the experimental configurations.

## Data extraction {#sec:dataset}

The tool and the process described in the following have been already
developed and used in our recent work [@9359479]. Since all three
approaches accepts as input data extracted by Rascal, which in turn
requires a specific format, we devised our own method to acquire an
Android dataset eligible for the evaluation. The extraction process
needs to comply with some certain requirements, and it is illustrated in
Fig. [1](#fig:DataExtraction){reference-type="ref"
reference="fig:DataExtraction"}. First, we exploited the
*AndroidTimeMachine* platform [@8595172] to crawl open source projects.
The platform fetches apps from the Google Play store[^1] and associates
them with the open source counterparts hosted in GitHub. The crawling
process resulted in a set of 7,968 open source Android apps.

[ $\blacktriangleright$*check this*$\blacktriangleleft$
]{style="color: orange"}Most of the apps (82%) in the dataset are
written in Java; 4% in Kotlin; 4% in JavaScript, 2% in C++, and 1% in
C\#. The remaining 7% belong to other languages. As Rascal can parse
certain programming languages, from the initial dataset we filtered out
irrelevant projects to select only the Java and Kotlin ones, which
account for the majority of the apps.

Afterwards, we retrieved the corresponding compiled APK files by
querying the Apkpure platform[^2] using some tailored Python
scripts [@8543433]. The process culminated in the final corpus
consisting of 2,600 APK binary files (mined from Apkpure) together with
additional metadata (mined from Google Play), including authors,
categories, star rating, price, and the number of downloads.

By carefully inspecting the data, we realized that most of the apps are
highly rated and they have a high number of downloads. We decompiled the
APKs into the JAR format by means of the **dex2jar** tool [@dex2jar].
The JAR files were then fed as input for Rascal (see
Section [5.1](#sec:arffExtractor){reference-type="ref"
reference="sec:arffExtractor"}) to convert them into the M$^3$ format
and ARFF, which can eventually be consumed by FOCUS, UP-Miner and PAM
(see Section ). Because UP-Miner and PAM suffer from scalability issues,
we considered a subset of 500 apps randomly extracted from the first one
to experiment UP-Miner and PAM.

![The data extraction
process.](figs/DataExtraction.pdf){#fig:DataExtraction
width="0.88\\columnwidth"}

[\[fig:DataExtraction\]]{#fig:DataExtraction label="fig:DataExtraction"}

## Configurations {#sec:Confs}

::: {.table*}
  **Conf.**   **$\Omega$**   **$\alpha$**   **$\beta$**   **Description**
  ----------- -------------- -------------- ------------- ---------------------------------------------------------------------
  C1.1.1      1              0.5            0.4           We inject one fake API into 40% of methods belonging to 5% project
  C1.2.1      1              10.0           0.4           We inject one fake API into 40% of methods belonging to 10% project
  C1.3.1      1              15.0           0.4           We inject one fake API into 40% of methods belonging to 15% project
  C1.4.1      1              20.0           0.4           We inject one fake API into 40% of methods belonging to 20% project
  C1.1.2      1              0.5            0.5           We inject one fake API into 50% of methods belonging to 5% project
  C1.2.2      1              10.0           0.5           We inject one fake API into 50% of methods belonging to 10% project
  C1.3.2      1              15.0           0.5           We inject one fake API into 50% of methods belonging to 15% project
  C1.4.2      1              20.0           0.5           We inject one fake API into 50% of methods belonging to 20% project
  C1.1.3      1              0.5            0.6           We inject one fake API into 60% of methods belonging to 5% project
  C1.2.3      1              10.0           0.6           We inject one fake API into 60% of methods belonging to 10% project
  C1.3.3      1              15.0           0.6           We inject one fake API into 60% of methods belonging to 15% project
  C1.4.1      1              20.0           0.6           We inject one fake API into 60% of methods belonging to 20% project
  C2.1.1      2              0.5            0.4           We inject two fake API into 40% of methods belonging to 5% project
  C2.2.1      2              10.0           0.4           We inject two fake API into 40% of methods belonging to 10% project
  C2.3.1      2              15.0           0.4           We inject two fake API into 40% of methods belonging to 15% project
  C2.4.1      2              20.0           0.4           We inject two fake API into 40% of methods belonging to 20% project
  C2.1.2      2              0.5            0.5           We inject two fake API into 50% of methods belonging to 5% project
  C2.2.2      2              10.0           0.5           We inject two fake API into 50% of methods belonging to 10% project
  C2.3.2      2              15.0           0.5           We inject two fake API into 50% of methods belonging to 15% project
  C2.4.2      2              20.0           0.5           We inject two fake API into 50% of methods belonging to 20% project
  C2.1.3      2              0.5            0.6           We inject two fake API into 60% of methods belonging to 5% project
  C2.2.3      2              10.0           0.6           We inject two fake API into 60% of methods belonging to 10% project
  C2.3.3      2              15.0           0.6           We inject two fake API into 60% of methods belonging to 15% project
  C2.3.3      2              20.0           0.6           We inject two fake API into 60% of methods belonging to 20% project
:::

The three parameters $\Omega$, $\alpha$, and $\beta$ are used to
populate artificial projects:

-   $\alpha$ is the ratio of projects injected with fake APIs.

-   $\beta$ is the ratio of methods in a project getting fake APIs.

-   $\Omega$ is the number of fake APIs injected to each declaration.

In particular, we consider the configurations as given in
Table [\[tab:Confs\]](#tab:Confs){reference-type="ref"
reference="tab:Confs"}.

# Download the artifacts

On a Linux system, open a terminal and execute the following command to
download all artifacts from a single GitHub repository:

    [backgroundcolor = \color{lightgray},language=,captionpos=t]
     $ git clone https://github.com/MDEGroup/APIRecSys-AML.git

This will create a new directory and we call it in the scope of this
presentation. An example of such a folder in Linux is . The root
directory has the following structure:

-   The directory contains the implementation of the injector and the
    dataset consisting of 2,600 artifacts:

    -   : the Java implementation of the FOCUS injector.

    -   : a set of 2,600 M$_3$ models extracted by Rascal and ready to
        be used with FOCUS;

-   The directory contains the ARFF injector and the sub-datasets of 500
    android apps that we used to evaluate PAM and FOCUS:

    -   : Because both PAM and UP-Miner uses ARFF files as input, the
        injector works for both UP-Miner and PAM

    -   contains the datasets generated by varying the inject parameter
        on the initial dataset of 500 android apps.

-   The FOCUS metadata parsed for a dataset consisting of 2,600 Android
    apps is stored in the folder. We acknowledge the original data
    collected from the AndroidTimeMachine platform [@8595172].

-   The UP-Miner and PAM metadata parsed for a dataset consisting of 500
    Android apps is stored in the folder. We acknowledge the original
    data collected from the AndroidTimeMachine platform [@8595172].

More information about the artifacts can be found in .

# Execute the experiments

This section describes how to run FOCUS,PAM, and UP-Miner on the
datasets using the configurations to reproduce the results presented in
the paper.

## Running Rascal to extract the metadata {#sec:arffExtractor}

The tool and the process described in the following have been already
developed and used in our recent work [@9359479].

This section describes how to run the `Parsing` phase as illustrated in
Figure [1](#fig:DataExtraction){reference-type="ref"
reference="fig:DataExtraction"} using Rascal to generate metadata.

::: {.tcolorbox}
**NOTE**: For the sake of reproducibility, we report all the related
activities to generate metadata using Rascal. However, we completely
parsed the metadata needed as input for both FOCUS, UPMiner and PAM at
your disposal. The tasks described in this section require a long
computation time as well as knowledge about Rascal, and we strongly
suggest using the metadata as it is. Therefore, it is advisable not to
perform the executions presented in
Section [5.1](#sec:arffExtractor){reference-type="ref"
reference="sec:arffExtractor"}.
:::

The JAR libraries involved in this experiment can be found at . A
snapshot of the related GitHub repositories is available online.[^3]

Follow the instructions available at to install and configure the Rascal
environment. Afterwards, go to the Rascal Console and import the main
module:

``` {#lst:importRascal .java backgroundcolor="\\color{lightgray}" label="lst:importRascal" language="Java" captionpos="t" escapechar="|"}
import focus::corpus::ExtractMetadata;
```

Since the $MV_L$ and $MV_S$ datasets have been extracted from byte code,
whereas the $SH_L$ and $SH_S$ datasets are mined directly from source
code, the module provides two different ways to parse the datasets. To
extract metadata from source code, is invoked using the following Rascal
command. By default, it will read the GitHub repositories metadata
stored in and output the results in the
`FocusRascal/data/m3/ java-projects/` directory.[^4]

``` {#lst:unzip .java backgroundcolor="\\color{lightgray}" label="lst:unzip" language="Java" captionpos="t"}
processGithubRepos();
```

The other function, `processJARs(loc directory)` is used to create the
M3 models from the JAR files contained in the specified directory. By
default, it outputs the results in `FocusRascal/data/m3/jar-projects/`.
The following command, for instance, parses all the JARs joined in this
artifact submission (replacing with ):

``` {#lst:sourceM3Rascal .java backgroundcolor="\\color{lightgray}" label="lst:sourceM3Rascal" language="Java" captionpos="t" escapechar="|"}
processJARs(|file:///path/to/FOCUS/dataset/jars|);
```

For your convenience, we provide a Python script named to convert the
input data used by FOCUS to be fed to PAM/UP-Miner. Run the following
commands:[^5]

``` {backgroundcolor="\\color{lightgray}" captionpos="t"}
$ cd <experiment_root>/tools/PAM
    $ python3 parsingARFF.py <experiment_root>/dataset/SH_S/ <experiment_root>/dataset/PAM/SH_S/
```

[ $\blacktriangleright$*Here, How to run the python script to extract
the PAM/UP-Miner metadata.*$\blacktriangleleft$ ]{style="color: cyan"}

## Inject fake APIs on Focus datasets

[ $\blacktriangleright$*Fill it*$\blacktriangleleft$
]{style="color: gray"}

## Inject fake APIs on PAM/UP-Miner datasets

Since both PAM and UP-Miner rely on ARFF file format to perform the
recommendations, we develop a Python script that injects APIs function
call to the initial project file using *liac-arff*, a tailored library
to open and manipulate ARFF[^6]. The `main_arff` function opens the
project file and injects one or two fake APIs for a certain number of
method declarations according to the configurations shown in Table
[\[tab:Confs\]](#tab:Confs){reference-type="ref" reference="tab:Confs"}.
For instance, you can inject two fake APIs into 40% of ARFF project
files as follows:

``` {backgroundcolor="\\color{lightgray}" captionpos="t"}
$ cd <experiment_root>/UPMiner_PAM/injector/
    $ pip install liac-arff
    $ pipi install click
    $ python3 injector.py --root=../../ASE2021-Appendix/initial_dataset --dest=./dest/ --beta=40 --fake_api=org.fake1 --fake_api2=org.fake2
```

Even though such kind of attacks is simple, we shown in the paper that
can compromise the quality of the recommendations. By looking at the
parameters defined in Section [3.2](#sec:Confs){reference-type="ref"
reference="sec:Confs"} we used `–fake_api` and `–fake_api2` as $\Omega$
values. The $\beta$ value is specified as command line parameter.
Finally $\alpha$ \...

## Running FOCUS {#sec:run_focus}

Three parameters must be fed to FOCUS: the dataset (as shown
Table [\[tab:Confs\]](#tab:Confs){reference-type="ref"
reference="tab:Confs"}), the configuration (as shown in
Table [\[tab:Confs\]](#tab:Confs){reference-type="ref"
reference="tab:Confs"}), and the cross-validation method (10-fold or
leave-one-out). These parameters can be specified in the file . The
default file runs 10-fold cross-validation on the $SH_S$ dataset using
configuration `C1.1`, and it is written as follows:

    [backgroundcolor = \color{lightgray},language=,captionpos=t]
     # Dataset directory (SH_L, SH_S, MV_L, MV_S)
     sourceDirectory=../../dataset/SH_S/
        
     # Configuration (C1.1, C1.2, C2.1, C2.2)
     configuration=C1.1
        
     # Validation type (ten-fold, leave-one-out)
     validation=ten-fold

To run FOCUS using the file, navigate to the directory containing the
FOCUS's implementation and execute it through Maven as given below:

``` {backgroundcolor="\\color{lightgray}" captionpos="t"}
$ cd <experiment_root>/tools/Focus
 $ mvn compile exec:java -Dexec.mainClass=org.focus.Runner
```

You need to replace with your actual root directory.

The final results of the evaluation including success rate, precision,
and recall for different cut-off values $N=\{1,5,10,15,20\}$ are printed
directly in the console. Intermediate results are stored in the
evaluation folder of the corresponding dataset's directory. For
instance, the results for the $SH_L$ dataset will be saved in . In this
folder, there are ten sub-folders, i.e.,, ,\..., . Each of them stores
the evaluation results of one fold using the following folders:

-   : the folder stores the recommended real code snippets.

-   : it contains all the invocations extracted as ground-truth data.

-   : the list of recommended invocations for each project is stored in
    this folder.

-   : the folder contains the similarity scores for each project.

-   : the folder stores the invocations used as query.

## Running PAM and UP-Miner

To evaluate the impact of adversarial attacks, we runned PAM and
UP-Miner by following the guide provided at
<https://github.com/mast-group/api-mining>.

Then execute PAM on the given input data as follows:

``` {backgroundcolor="\\color{lightgray}" captionpos="t" escapechar="|"}
$ git clone git@github.com:mast-group/api-mining.git <repo_root>
    $ cd <PAM_root>
    $ mvn package
    $ for f in <experiment_root>/<injected_dataset> *; do java -jar api-mining/target/api-mining-1.0.jar apimining.pam.main.PAM -f $f; done
    $ for f in <experiment_root>/<injected_dataset> *; do java -jar api-mining/target/api-mining-1.0.jar pimining.upminer.UPMiner -f $f; done
```

where is the directory where you have cloned the repository and is the
path of the dataset injected by fake APIs. In our case, we set they to .
and respectively.

# Troubleshooting {#troubleshooting .unnumbered}

If you encounter any problem during the evaluation, please do not
hesitate to contact us through one of the email addresses given above.

[^1]: <https://play.google.com/>

[^2]: <https://apkpure.com/>

[^3]: <https://drive.google.com/open?id=1hQkh85XY4c0sh788G9g8B0yi5stJxSwc>

[^4]: More information can be found at

[^5]: The execution of the commands necessitates python3 which is
    available at <https://www.python.org/download/releases/3.0/>

[^6]: <https://pythonhosted.org/liac-arff/>
