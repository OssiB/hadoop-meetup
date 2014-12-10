hadoop-meetup
=============

### Installation

Download [VirtualBox]  and [single-Node Hadoop Cluster] and open the downloaded Cloudera virtualbox file.



### Download data for Hadoop
If you have R installed on your machine you can run the script top.R inside ```/src/main/scripts``` . As a result you will have 20 cvs file  CA.csv, AZ,... Join files 
```sh
$ awk 'FNR>1' *.csv > states.csv
```
Or you can use *curl*
```sh
$ curl  http://data.stackexchange.com/stackoverflow/csv/325342?state={AZ,TX,IN,CA....} -o "states.csv" -s -S -f
```
### Move data to HDFS 
```sh
$ hadoop fs -mkdir user/cloudera/input
$ hadoop fs -copyFromLocal states.csv /user/cloudera/input/
```
[single-node Hadoop Cluster]:http://www.cloudera.com/content/cloudera/en/downloads/quickstart_vms/cdh-5-2-x.html
[Virtualbox]:https://www.virtualbox.org/wiki/Downloads



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
If you run the tests, they should fail. Try to implement ```StateReputationMapper``` method
```java
protected void map(LongWritable key, Text value, Context context)
```
so that ```StatepReputationMapperTest``` class tests will pass. 

| Input line  | Output key |Output value|
| ------------- | ------------- |-------|
| "251665","Chicago, IL","1464","50153","0.0337142626664605"  | CA  |1464|


#### MapReduceDriver 


### Oozie workflow






