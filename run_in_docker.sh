docker run -v $PWD:/opt/grouperserve -p 4567:4567 --workdir /opt/grouperserve --name=grouperserve --detach=true java:8 java -cp build/libs/grouperserve-0.1.1.jar ch.eonum.grouperserve.GrouperServe
