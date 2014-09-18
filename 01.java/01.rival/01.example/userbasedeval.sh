#!/bin/bash
#


mvn assembly:assembly -DdescriptorId=jar-with-dependencies
java -jar target/crowdrec-rival-test-0.1-SNAPSHOT-jar-with-dependencies.jar $1 $2

exit 0

