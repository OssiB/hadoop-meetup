hadoop-meetup
=============

### Installation

Download [VirtualBox]  and [single-Node Hadoop Cluster] and open the downloaded Cloudera virtualbox file.



### Download data for Hadoop
Note. You can also download the data running ```GetData``` program.
The data we will use is a query result from the [StackExchange Data Explorer]. If you run the [query] with parameter CA
you will get about 18 000 lines as result set. Inside the result page there is a link  [Download CSV], which we will use in fetching actual data.

If you have R installed on your machine you can run the script top.R inside ```/src/main/scripts``` . As a result you will have 20 cvs file  CA.csv, AZ,... Join files 
```sh
$ awk 'FNR>1' *.csv > states.csv
```
Or you can use ```curl```
```sh
$ curl  http://data.stackexchange.com/stackoverflow/csv/329607?state={AZ,TX,IN,CA....} -o "states.csv" -s -S -f
```

### Move data to HDFS using CLI
```sh
$ hadoop fs -mkdir user/cloudera/input
$ hadoop fs -copyFromLocal states.csv /user/cloudera/input/
```
[single-node Hadoop Cluster]:http://www.cloudera.com/content/cloudera/en/downloads/quickstart_vms/cdh-5-2-x.html
[Virtualbox]:https://www.virtualbox.org/wiki/Downloads
[StackExchange Data Explorer]:http://data.stackexchange.com/
[Download CSV]:http://data.stackexchange.com/stackoverflow/csv/329607?state=CA
[query]:http://data.stackexchange.com/stackoverflow/query/249571/state-query


### How to run MapReduce program in Eclipse

Open a terminal
```sh
$ git clone https://github.com/OssiB/hadoop-meetup.git
$ cd hadoop-meetup
$ mvn eclipse:eclipse
```
Import generated eclipse project into your Eclipse workspace.
#### MapReduce testing with MRUnit
Inside <code>/src/test/hadoop/meetup/first</code> directory there are test classes :
```java
StateReputationMapperTest
StateReputationReducerTest
```
If you run the tests, they should fail. Try to implement   method 
```java
protected void map(LongWritable key, Text value, Context context)
```
for the class ```StateReputationMapper``` so that ```StatepReputationMapperTest``` class tests will pass. 

| Input line  | Output key |Output value|Header counter|Not in states counter|
| ------------- | ------------- |-------|-----------|---------------------|
| "251665","Chicago, IL","1464","50153","0.0337142626664605"  | IL  |1464|-|
|Id,Location,Reputation,Ranking,Percentile|-|-|+1|-|
|"2189688","POLLACHI,COIMBATORE,TAMIL NADU,INDIA","101","323293","0.217"|-|-|-|+1| 


#### MapReduceDriver 

Inside Eclipse run the program ```GetData```with parameters 
```
input http://data.stackexchange.com/stackoverflow/csv/329607?state=  TX,CA
```
Press F5 and you should  see files TX.csv,CA.csv  under ```input``` directory.
Running  program ```MinimalMapReduceDriver``` shows what happens when you run MapReduce without
setting mapper or reduce. Try to run programm with parameters
```
input output
```
Open file ```part-r-00000```. It should start with lines:
```
0	Id,Location,Reputation,Ranking,Percentile
0	Id,Location,Reputation,Ranking,Percentile
43	"120163","Austin, TX","52616","673","0.0004471680574501"
43	"76337","Phoenix, AZ, USA","122527","175","0.0001162769837351"
101	"4926","Austin, TX","38394","1097","0.0007288905780427"
```
Modify  class ```MinimalMapReduceDrive``` by inserting line 
```java
job.setOutputKeyClass(NullWritable.class);
```
Run program again and notice that keys are removed.Add line
```java
job.setNumReduceTasks(0);
```
and run program. Output directory should contain files 
```
part-m-0001,part-m-0002
```
If you have succesfully run the tests you may try to run 
```

```
### Oozie workflow
It is possible to run dependent jobs using Oozie workflow. Workflow definitions are written in XML using 
Hadoop Process Definition Language. Workflow file ```src\main\resources\state-rep-workflow\workflow.xml```
has excatly same functionality as ```StateReputationDriver``. An Oozie workflow application is made up
* workflow definition file
* jar files
* Pig scripts

It should have the following diretory structure
```dir
state-rep-workflow/
|---lib/
|   |----hadoop-meetup-1.0.jar
|---workflow.xml
```


This directory structure should be deployed to HDFS.
```sh
% hadoop fs -put state-rep-workflow state-rep-workflow
```
Run from the terminal
```sh
export OOZIE_URL="http://localhost:11000/oozie"
```
and start Oozie job
```sh
oozie job  -config state-rep-workflow/state_workflow.properties -run
```
Open the browser and choose Oozie application. You should see Oozie job running.
If  job shows SUCCESS status check job output with command
```sh
$ hadoop fs -cat wfoutput/part_*
```
second meetup
=============





