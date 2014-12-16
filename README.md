hadoop-meetup
=============

### Installation

Download [VirtualBox]  and [single-Node Hadoop Cluster] and open the downloaded Cloudera virtualbox file.



### Download data for Hadoop
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
### Move data to HDFS using java program



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


### Oozie workflow






