# SCORM-2004-4ed-SampleRTE

This document outlines how to contribute to this project.

* [ADL Ownership] (#adl-owner)
* [Reviewing Open Pull Requests](#review-pr)
* [Suggesting Solutions to Issues](#suggest-solution)
* [Creating an Issue] (#create-issue)
* [Making a Pull Request to Address an Issue](#make-pr)

<a name='review-pr'/>
## ADL Ownership

Advanced Distributed Learning (ADL) is a government organization allowing use of software and web services in the spirit of 
Open Source collaboration.  It makes no guarantee of any version of the software found in this repository.  Those using 
this software should be aware of multiple non-ADL contributors to the project.  ADL, as a government initiative, remains 
unbiased towards any particular vendor.  Any language or reference to specific vendors will be removed from the end product.

ADL reserves the right to update this repo as needed.  Pull requests and issues are very much appreciated, but could be
reverted or deleted at any time.  If you wish to maintain your own repo from ADL, please fork it and maintain it there. 

For more information on usage see our license here. (link needed)

# Contributing 

<a name='review-pr'/>
## Review a Pull Request (PR)
The most helpful **and** easiest way to contribute to the specification is to review an existing PR. The easiest way
to see "open" (needing review) PRs is to go to the project home page and click on the "Pull Requests" link on the right 
(it is just below issues) To review a PR you should:

* Read any issues linked to from the PR description and make sure you understand the issue the PR is desigend to address. 
* Read any comments on both the issues and the PR to understand if the goals of the original issue have changed and if
any solutions have been agreed. 
* Check the 'Files changed' tab of the PR and look at what has changed. Use the 'rich diff' option if that helps you to see
the changes better. Check to see if what's been changed matches the solution you expected from reading the issue and comments. 
* Look for problems such as typos, unintended changes to behavior and any text that's unclear. 
* Check that anything that's been removed has either been replaced or the removal was intentional (based on reading the issue 
and comments).
* If you spot any issues with the PR, add a comment describing these problems as best you can and suggesting how they can
be resolved. 
* If the PR looks good to you, add a comment saying "+1".
* If you are unsure about the PR or have questions, ask questions in the PR comments. 
* If you feel that the PR should never be merged, even with changes, add a comment starting with "-1" and explaining your
reasons. If your comments run contrary to what's already been agreed in the issues, there might be some resistance, but 
that's ok if you have a strong arguement! 

When reviewing a PR please don't:

* Review and leave no comment; always let us know you've had a look!
* Suggest additional changes outside of what the PR was intended to achieve. That's what Issues are for!
* Make vague critisisms without suggesting the changes to the PR that would meet those critisisms. 

Most ADL Project Pull Requests will remain open for a period of at least 7 days to allow comments or +1s.  Some projects 
will have a policy where a certain number of +1s will cause the project to merged.  An ADL Project owner has full discretion 
in such matters.

<a name='suggest-solution'/>
## Suggest a Solution
The second most helpful and second easiest way to contribute to the specification is to suggest a solution to an issue that 
has been raised. Ideally the person who raises the issue will propose a solution, but this does not always happen and you 
may be able to improve a suggestion. 

To suggest a solution to an issue you should:

* Read the issue and ensure you understand the problem being described. Ask questions if you need to. 
* Add a comment stating:
    * Which files/document sections you propose to change.
    * A description of the proposed change. 
    * Any reasons for and against the change you're proposing. 

Once your suggestion has been discussed and agreed, add another comment summarising your understanding of the outcome of 
the discussion and including proposed wording for the changes you've suggested. 

Good and detailed suggested solutions for issues make it much easier to write PRa and helps to ensure those PRs are 
merged faster. 

<a name='create-issue'/>
## Create an Issue

A helpful way to start a discussion around a problem, desired feature, best practice, etc. is to create an issue.  Issues 
are typically left open until closed by the person who raised it, awaiting their satisfaction.  Most issues will eventually
transition into a Pull Request (PR).  Unresolved issues or those not generating discussion will typically have the ADL 
project leader comment to the person creating it to add more information, reframe the issue, or close it.  ADL Staff have the 
final authority in closing issues.

When creating an issue, be sure to:

    * Check to make sure the issue isn't already raised (make sure to check the "closed" issues in the search as well)
    * Specify which files/document sections you propose to change.
    * Provide a description of the proposed change. 
    * Describe any reasons for and against the change you're proposing. 

To create an issue, go to the project home page and click the "Issue" link on the right.  Some projects may have tags to go 
with certain types of issues.  Be sure to use these tags as you become familiar within the particular project.  Tags will 
vary widely across projects.

<a name='make-pr'/>
## Make a Pull Request (PR)
The strongest way to contribute to a project is to create a PR.  ADL recommends only getting involved in this way if you 
are already used to Github and markdown and/or have previously contributed to reviewing PRs and suggesting solutions to issues. 
If you wish to learn the ropes, ADL recommends forking this repository and submitting PRs to your own branch to practice.

Before making a PR you should:
* Read the issue you are planning to address carefully including any comments.
* Read the relevant files/document sections you desire to change.
* Read any suggested solutions to the issue and the discussion around those issues. 

If you would like to raise a PR that directly addresses your own concern, there is no need to raise a separate issue. 

To make a PR you will need to edit the files either using the GitHub.com interface, or use git on your local computer. 
The GitHub.com interface is simpler for new users and is ideal for smaller changes like typos. These two methods are 
described below.

When making a PR, you should include a description that explains:
* What issue numbers of any issues you are addressing (normally just one issue)?
* How does your PR relate to any proposed solutions to the issue?
* If you have moved text between files or sections, have you made any changes to content within those moved sections?
* What decisions did you make in writing the change? Why did you make those decisions? 

## Edit on GitHub.com
To edit the specification on GitHub.com, first open the file you wish to have changed and [follow the instructions here](https://help.github.com/articles/editing-files-in-another-user-s-repository/). 

## Edit Locally

### Set up
If you are not currently working with GitHub and git, follow these set up steps 
first. GitHub provides excellent help at [https://help.github.com/articles/set-up-git](https://help.github.com/articles/set-up-git)

#### Fork the ADL repository
Go to the project repository. Fork the repository to your own account using 
the "Fork" button on the top right of ADL project repository page. This makes a 
copy of the ADL project repository. This fork gives you the ability to edit your 
version without impacting the master copy.

#### Install Git (use cmd line) or Install Windows/Mac GitHub client
You need to install Git to work with a GitHub repository. If you are on a Windows machine, 
you can download the GitHub client app. If you use a Mac you can download the GitHub client app 
but will also have to download git to add a remote to the master repository. Otherwise install git from the 
git site.

__Git__  
This provides a command line client app for working with a git repository (like 
GitHub)  
Download and run [git install](http://git-scm.com/downloads)

__GitHub Client__  
GitHub Client provides a GUI interface to simplify working with a repository on 
GitHub. This does not currently support synchronizing with a master repository so 
some commands will still need to be completed using the command line.

__Mac:__ http://mac.github.com/  
__Windows:__ http://windows.github.com/


#### Clone your GitHub fork to your machine
To make edits and work on the files in the repository, clone your repository to 
your local machine using Git. The url is provided on the home page of your 
repository (ex. ```https://github.com/<your username>/xAPI-Spec/```)  

__Git__  
```git clone https://github.com/<your username>/<project>/>```  

__GitHub Client__  
On the home screen of the client app, select your account under 'github' and 
choose the repository you want to clone. Selecting the repository from the list 
gives you an option to clone it. 

### Add ADL repository as upstream remote
Add a remote repository to git to reference the master repository. This will make 
synchronizing with the master respository a bit easier.  

__Git__  
```git remote add upstream https://github.com/adlnet/<projectname>```  

__GitHub Client__  
Currently the GitHub clients don't have a way to synchronize with the master 
repository. In order to do this, open your repository on the GitHub client 
app home screen. On the repository screen select 'tools' and 'open a shell 
here'. Alternatively use the 'Git Shell' shortcut if it was created during 
installation. **NOTE:** If you're using a Mac there is no shell shortcut so 
navigate to ```/your/repo/path/<projectname>``` then follow the shell instructions.
  
In the shell, enter..  
```git remote add upstream https://github.com/adlnet/<projectname>```  


### Workflow

### Sync up with ADL Repository development
Pull down changes from the development repository. This automatically does a 
fetch of the repository and a merge into your local repository.

__Git and GitHub Client__  
```git pull upstream <branchname>```

### Make Changes Locally
Edit the local copy of the file, save and commit. Rule of thumb: Use commits 
like save points. Commit to indicate logical groups of edits, and places 
where the edits could be safely rolled back.  

__Git__  
```git commit -a -m "<commit message>"```  

__GitHub Client__  
The GitHub client will detect saved changes to the documents in your 
local repository and present a button to commit your edits at the top 
right of the repository screen.  

### Push Changes to Your Repository (Origin)
Pushing your changes to your remote GitHub repository stages the files 
so that you can then make requests to the master repository to merge in 
your changes.

__Git__  
```git push origin```

__GitHub Client__  
The GitHub client has a 'sync' button at the top of the repository screen. 
This will synchronize your local and remote (origin) repository.  

### Submit a Pull Request to Master ADL Repository (Upstream)
When you forked from the ADL Project repository, a link back to the master 
repository is remembered. To send your changes back the the master repository, 
click the "Pull Request" button at the top of your repository page. This will 
direct you to a page that gives you the ability to submit a request to the 
master repository to merge in the changes you committed.

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
