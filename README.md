#lint-graph
A tool to emphasize relations between lint issues and files.

**:construction: This tool is still under development :construction:**

##Setup
1. [Install neo4j server](http://neo4j.com/docs/stable/server-installation.html)
2. Start neo4j (on OSX & Linux: open a terminal, cd to the extracted folder, execute `bin/neo4j start`)
3. Open [http://localhost:7474](http://localhost:7474)


##Usage
1. Run `./gradlew lint` in your Android application project
2. Build *lint-graph* with `./gradlew build`
3. Run lint-graph 

```shell
$ lint-graph <app_project>/build/outputs/lint-results.xml http://localhost:7474/db/data neo4j neo4j
```
