# grouperserve
Microservice for the SwissDRG grouper. Provided as a REST JSON API over HTTP/HTTPS using the Java framework Spark.

## Getting started

Copy grouper jar to the libs directory:
```
mkdir libs
cp grouper_0.4.0.jar libs 
```

Make sure gradle -v returns something similar to this:

```
------------------------------------------------------------
Gradle 3.0
------------------------------------------------------------

Build time:   2016-08-15 13:15:01 UTC
Revision:     ad76ba00f59ecb287bd3c037bd25fc3df13ca558

Groovy:       2.4.7
Ant:          Apache Ant(TM) version 1.9.6 compiled on June 29 2015
JVM:          1.8.0_101 (Oracle Corporation 25.101-b13)
OS:           Linux 3.19.0-59-generic amd64
```

Build jar:
```
./gradlew build
```
In order for the service to be functional you have to provide the JSON specifications (as provided by SwissDRG AG) for each system in the folder /grouperspecs with the folder names matching the field 'version' as obtained by the above call and stored in grouperspecs/systems.json.

Run grouper server:
```
java -cp build/libs/grouperserve-0.1.1.jar ch.eonum.grouperserve.GrouperServe
```


Test this URL in your browser:
`http://localhost:4567/systems`
You should obtain a list of all provided SwissDRG systems in a JSON array. 


## The patient case URL format
![the URL patient case format](PatientCase_URL_format.png "The patient case URL format")

## API calls

If you test the API in your shell e.g. using curl:
`export ROOT_URL=http://localhost:4567`

### systems
Obtain a list of available SwissDRG systems.

URL:
`GET /systems`

sample call using curl:
```
curl "$ROOT_URL/systems"
```

### group
Group a patient case and obtain a grouper result and an effective cost weight. You can find a documentation of the grouper result and the effective cost weight in the documentation for the SwissDRG grouper.

URL:
`POST /group`

Parameters:
* pc: patient case in the URL patient case format
* version: version identifier as provided by /systems
* pretty: (true|false) pretty print JSON (default is false)
* annotate: (true|false) return the annotated patient case with CCL values, validations and used flags (default is false)

sample call using curl: 
```
curl --header "Accept: application/json" --data "version=V5_A&pc=11_65_0_0_M_01_00_1_0_I481_Z921_F051_-_8954_&pretty=true" "$ROOT_URL/group"
```

### group_many
Group more than one patient case in one request.

URL:
`POST /group_many`

Parameters:
* pcs: patient cases JSON array with strings in the URL patient case format
* version, pretty, annotate: see /group
	

sample call using curl: 
```
curl --header "Accept: application/json" --data "version=V5_A&pcs=[\"11_65_0_0_M_01_00_1_0_I481_Z921_F051_-_8954_\", \"12_65_0_0_M_01_00_1_0_I481\"]&pretty=true" "http://localhost:4567/group_many"
```


## Run as Docker container
Run the server:
```
docker run -it -v $PWD:/opt/grouperserve -p 4567:4567 --workdir /opt/grouperserve --rm java:8 java -cp build/libs/grouperserve-0.1.1.jar ch.eonum.grouperserve.GrouperServe
```

Run in detached mode:
```
docker run -v $PWD:/opt/grouperserve -p 4567:4567 --workdir /opt/grouperserve --name=grouperserve --detach=true java:8 java -cp build/libs/grouperserve-0.1.1.jar ch.eonum.grouperserve.GrouperServe
```
In detached mode you can stop, kill, remove or start the server as follows:
```
docker stop grouperserve
docker kill grouperserve
docker rm grouperserve
docker start grouperserve
```
Follow the logs:
```
docker logs -ft grouperserve
```




