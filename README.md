# grouperserve
Microservice / WebAPI for the SwissDRG grouper. Provided as a REST JSON API over HTTP/HTTPS using the Java framework Spark.

## Getting started

Build jar:
```
gradle build
```

Run grouper server:
```
java -cp build/libs/grouperserve-0.1.1.jar ch.eonum.grouperserve.GrouperServe
```


Test this URL in your browser:
`http://localhost:4567/systems`
You should obtain a list of all provided SwissDRG systems in a JSON array. 
In order for the service to be functional you have to provide the JSON specifications (as provided by SwissDRG AG) for each system in the folder /grouperspecs with the folder names matching the field 'version' as obtained by the above call.

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
Group a patient case and obtain a grouper result and an effective cost weight.

URL:
`POST /group`

Parameters:
* pc: patient case in the URL patient case format
* version: version identifier as provided by /systems
* pretty: pretty print JSON (default is false)
	

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
* version: version identifier as provided by /systems
* pretty: pretty print JSON (default is false)
	

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
docker remove grouperserve
docker start grouperserve
```
Follow the logs:
```
docker logs -ft grouperserve
```




