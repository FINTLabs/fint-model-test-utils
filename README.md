# FINT Model Test Utils

## Installation

```groovy
repositories {
    maven {
        url  "http://dl.bintray.com/fint/maven" 
    }
}

testCompile('no.fint:fint-model-test-utils:1.0.2')
```

## Usage

The `fint-model-test-utils` is created to be used with the [Spock framework](http://spockframework.org). 
The library will generate json files for the model and the relation names. 
When new model classes are generated these can be matched against the persisted json files.

### Test setup

1. The setup method initializes the JsonSnapshots. It will scan the `no.fint.model` package and find all model classes.
2. The `create FINT model snapshots` test generates the json files.
When calling create it will first remove all files in the `src/test/resources/snapshots` folder before generating new.
The test is setup to only run when the system property `UPDATE_SNAPSHOT` is set.
The return value from create is a boolean indicating if the snapshot file generation has succeeded.
3. The `matches snapshots` test will match the snapshots and relation names for all model classes.


```groovy
class ModelSpec extends Specification {
    private JsonSnapshots jsonSnapshots

    void setup() {
        jsonSnapshots = new JsonSnapshots('no.fint.model')
    }

    @Requires({ Boolean.valueOf(sys['UPDATE_SNAPSHOT']) })
    def "Create FINT model snapshots"() {
        expect:
        jsonSnapshots.create()
    }

    def "Matches snapshots"() {
        expect:
        jsonSnapshots.matchesSnapshots()
        jsonSnapshots.matchesRelationNames()
    }
}
```

