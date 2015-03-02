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
[asuntojen hintatiedot]:http://asuntojen.hintatiedot.fi
[Download CSV]:http://data.stackexchange.com/stackoverflow/csv/329607?state=CA
[query]:http://data.stackexchange.com/stackoverflow/query/249571/state-query
[Palauta alkuperäinen järjestys]:[asuntojen.hintatiedot.fi/haku/?c=Helsinki&cr=1&search=1]
[jsoup]:[jsoup.org]
[jquery]:[jquery.org]
[Kite SDK]:[http://kitesdk.org/docs/current/]
[Avro]:[http://avro.apache.org/docs/1.7.7/gettingstartedjava.html]


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
### Open data 
At our first meetup we downloaded the data using command line tools  and  R. It was quite easy  because the response
for the  request was a well formatted csv file. At our next meeting we  will download  data from    [asuntojen hintatiedot]. 
The web interface for the house price information is provided by the Ministry of Enviroment which was one of the first organisations making it's data available for every one. Users can make queries related to, for example, city, postal code or room size. For example, if one runs a query with the parameter "Helsinki", the results will be shown arranged by the time of sale. The results can also be sorted by price, building year etc. After clicking Rv header, the results are sorted by the building year. After this we copy the link [Palauta alkuperäinen järjestys] and we have a starting point for our data scraping.
```
http://asuntojen.hintatiedot.fi/haku/?c=Helsinki&cr=1&search=1
```
If we want only apartments with 2 rooms inside Helsinki the url would be
```
http://asuntojen.hintatiedot.fi/haku/?c=Helsinki&cr=1&search=1&r=2
```
But there are still too many results for a single page, so we have to add a page index to url
```
http://asuntojen.hintatiedot.fi/haku/?c=Helsinki&cr=1&search=1&r=2&z=1
```
 
#### Parsing data
##### Pseudocode 
```java
   for each city 
      for room_size in [1..4]
         while(!hasMore) 
            moveToNextResultPage
                parseData
 ```
We will collect data from the following cities: Helsinki,Espoo,Vantaa,Tampere. It would anyhow be possible to collect data 
from all cities using the request ```http://asuntojen.hintatiedot.fi/haku/searchForm/fetchCities?lang=fi_FI```.
The respond will be in json format
```json
{
cities: "[Akaa, Alajärvi, Alavieska, Alavus, Asikkala, Askola, Aura, Enonkoski, Espoo, Eura, Eurajoki, Forssa, Haapajärvi, Haapavesi, Hailuoto, Hamina, Hankasalmi, Hanko, Harjavalta, Hattula, Hausjärvi, Heinola, Heinävesi, Helsinki, Hirvensalmi,....
```

#### Selecting data            
Next we will use [jsoup] library to parse data. [jsoup] has a similar syntax to [jquery so we can scrape all the  ```tr``` elements using code
```java
Elements rows = doc.select("tr");
```
We will now select rows which have at least ten child elements. Also we want to exclude data header row ..
```java
for (Element row : rows) {
	if (row.children().size() > 10) {
		Elements tableData = row.children();
		String kaupunginosa = tableData.get(0).text();
		if (!kaupunginosa.startsWith("Kaupunginosa")) {....
```
If you run the program ```HousePrice```, it will produce file ```houseprice.csv```.
```
"Alppila";"1h+kk";"kt";"29,00";"167000";"5759";"1960";"4/8";"on";"tyyd.";"Helsinki";"0"
"Kannelmäki";"1 h, kk";"kt";"34,50";"121000";"3507";"1977";"4/4";"ei";"tyyd.";"Helsinki";"1"
"Kallio";"1H+KK";"kt";"22,00";"160000";"7273";"1938";"4/6";"on";"hyvä";"Helsinki";"2"
```
The last two columns ```city,order``are inserted during parsing. We included ``òrder``field because
it gives us some kind of approximation of the sale  time. If we have for example 400 hundred sale events
we know that the row with order 126, has a sale time of about Now- (365-(126/400)*365).
#### Move data to the Hadoop ecosystem
At our previous meeting we used MapReduce program and Hadoop command line utilities in storing data. Now we are going
to use [Kite SDK] tool. Kite is a high-level data layer for Hadoop. You can download [Kite SDK] using commands
```sh
curl http://central.maven.org/maven2/org/kitesdk/kite-tools/1.0.0/kite-tools-1.0.0-binary.jar -o kite-dataset
chmod +x kite-dataset
```
Copy few lines from ```houseprices.csv``` and make  a new file ```houseprices_schema.csv``` . Modify the file 
inserting header row. File content should be 
```txt
"location";"rooms";"type";"squares";"price";"square_price";"build_year";"flat";"elevator";"condition";"city";"order"
"Alppila";"1h+kk";"kt";"29,00";"167000";"5759";"1960";"4/8";"on";"tyyd.";"Helsinki";"0"
"Kannelmäki";"1 h, kk";"kt";"34,50";"121000";"3507";"1977";"4/4";"ei";"tyyd.";"Helsinki";"1"
"Kallio";"1H+KK";"kt";"22,00";"160000";"7273";"1938";"4/6";"on";"hyvä";"Helsinki";"2"
```
Run command
```sh
kite-dataset csv-schema  house_price_schema.csv  --class HousePrice -o houseprice.avsc --delimiter ";"
```
Now you have a schema,to be precise an [Avro] schema. Verify previous argument
```sh
cat houseprice.avsc
```
.. with schema we can create dataset
```sh
kite-dataset create houseprices -s houseprice.avsc
```
```sh
kite-dataset schema houseprices
```
Now we have created table inside ```hive```. You can verify it running the command
```sh
$hive
hive>show tables;
 OK
 houseprices
```
If you run query
```sql
hive>select count(*) from houseprises;
```
the result will be 0 so we have to insert data 
```
kite-dataset csv-import datasets/houseprice.csv houseprices  --delimiter ";"
```
Kite SDK will show first ten rows 
```sh
$kite-dataset show houseprices
```
