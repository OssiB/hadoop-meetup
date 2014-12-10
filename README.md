hadoop-meetup
=============

### Installation

Download [VirtualBox]  and [single-Node Hadoop Cluster] and open the downloaded Cloudera virtualbox file.
Open a terminal
```sh
$ git clone https://github.com/OssiB/hadoop-meetup.git
$ cd hadoop-meetup
$ mvn eclipse:eclipse
```
Import generated eclipse project into your Eclipse workspace.

### Download data for Hadoop
If you have R installed on your machine you can run the script top.R. As a result you will have 20 cvs file  CA.csv, AZ,... Join files 
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
#### MapReduce testing with MRUnit
Inside <code>/src/test/hadoop/meetup/first</code> there are three test classes :
```java
StateReputationMapperTest
StateReputationReducerTest
StateReputationDriverTest
```
Run the tests and all the tests should fail.

#### MapReduceDriver 


### Oozie workflow






