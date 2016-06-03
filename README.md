# SCORM-2004-4ed-SampleRTE

<a name='review-pr'/>
## ADL Ownership

Advanced Distributed Learning (ADL) is a government organization allowing use of software and web services in the spirit of 
Open Source collaboration.  It makes no guarantee of any version of the software found in this repository.  Those using 
this software should be aware of multiple non-ADL contributors to the project.  ADL, as a government initiative, remains 
unbiased towards any particular vendor.  Any language or reference to specific vendors will be removed from the end product.

ADL reserves the right to update this repo as needed.  Pull requests and issues are very much appreciated, but could be
reverted or deleted at any time.  If you wish to maintain your own repo from ADL, please fork it and maintain it there. 

## Installation

This project was originally released on the ADL website as a windows application

If you just want to install and use the Sample RTE, consider using the original windows appplication linked [here](https://www.google.com/url?q=https%3A%2F%2Fadlnet.gov%2Fwp-content%2Fuploads%2F2011%2F07%2FSCORM.2004.4ED.SRTE_.v1.1.1.zip&sa=D&sntz=1&usg=AFQjCNG81dbJtfuAsig1E4vzJbbG19R3-g)

#### For Windows

+ Install ant-1.7.1
+ Install java jdk6, 7, or 8
+ Set JAVA_HOME env var to jdk path, ANT_HOME to ant path, SCORM4ED_SRTE111_HOME to /path/to/SCORM_2004_4TH_Ed_sample_RTE_V1.1.1, CATALINA_HOME to /path/to/SCORM_2004_4TH_Ed_sample_RTE_V1.1.1/Sample_RTE/apache-tomcat
+ Add JAVA_HOME\bin and ANT_HOME\bin to Path
+ Run the installer (make sure the install path matches the paths you provided in the env var SCORM4ED_SRTE111_HOME)
+ Replace the SCORM4ED_SRTE111_HOME/Sample_RTE/source directory with this repo and rename the folder, source

#### For Linux

If you are attempting to download on a unix based machine clone the project at https://github.com/adlnet/SCORM-2004-4ed-SampleRTE/tree/osmerge

If the above options don't work, or you are planning on contributing to the project,

Run: `git clone https://github.com/adlnet/SCORM-2004-4ed-SampleRTE`

or [Download directly](https://github.com/adlnet/SCORM-2004-4ed-SampleRTE/archive/master.zip)

## Configuration

For additional configuration, installation and use, see the original [readme](https://github.com/pauliejes/SCORM-2004-4ed-SampleRTE/tree/master/RTE_Readme)

## Contributing to the project
We welcome contributions to this project. Fork this repository, make changes, and submit pull requests. If you're not comfortable with editing the code, please [submit an issue](https://github.com/adlnet/SCORM-2004-4ed-SampleRTE/issues) and we'll be happy to address it. 

## License
   Copyright &copy;2016 Advanced Distributed Learning

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
