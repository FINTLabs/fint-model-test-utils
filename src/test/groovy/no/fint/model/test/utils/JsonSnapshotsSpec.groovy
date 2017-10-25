package no.fint.model.test.utils

import spock.lang.Specification

class JsonSnapshotsSpec extends Specification {
    private JsonSnapshots jsonSnapshots

    void setup() {
        jsonSnapshots = new JsonSnapshots('no.fint.model.test.utils.testmodel')
    }

    void cleanup() {
        jsonSnapshots.cleanSnapshotFolder()
    }

    def "Create json snapshots with string base package"() {
        when:
        def snapshots = new JsonSnapshots(this)

        then:
        snapshots.jsonSnapshotList.size() > 0
    }

    def "Create json snapshots"() {
        when:
        def created = jsonSnapshots.create()

        then:
        created
        jsonSnapshots.jsonSnapshotList.each {
            assert !it.modelClass.isEnum()
            assert it.snapshotFolderExists()
            assert it.exists()
        }
    }

    def "Match json snapshot"() {
        given:
        jsonSnapshots.create()

        when:
        def matchesSnapshot = jsonSnapshots.matchesSnapshots()
        def matchesRelationNames = jsonSnapshots.matchesRelationNames()

        then:
        matchesSnapshot
        matchesRelationNames
    }

    def "Do not match json snapshot when file contains updated property"() {
        given:
        jsonSnapshots.create()
        jsonSnapshots.jsonSnapshotList.each {
            def snapshotFile = it.getSnapshotFile()
            def json = snapshotFile.text
            json = json.replaceAll('test', 'test123')
            snapshotFile.write(json)

            def relationNamesFile = it.getRelationNamesFile()
            json = relationNamesFile.text
            json = json.replaceAll('TESTREL123', 'TESTREL234')
            relationNamesFile.write(json)
        }

        when:
        def matchesSnapshot = jsonSnapshots.matchesSnapshots()
        def matchesRelationNames = jsonSnapshots.matchesRelationNames()

        then:
        !matchesSnapshot
        !matchesRelationNames
    }
}
