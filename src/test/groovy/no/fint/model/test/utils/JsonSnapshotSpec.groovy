package no.fint.model.test.utils

import no.fint.model.test.utils.testmodel.TestModel
import spock.lang.Specification

class JsonSnapshotSpec extends Specification {
    private JsonSnapshot jsonSnapshot

    void setup() {
        jsonSnapshot = new JsonSnapshot(TestModel)
    }

    void cleanup() {
        jsonSnapshot.cleanSnapshotFolder()
    }

    def "Create json snapshot"() {
        when:
        jsonSnapshot.create()

        then:
        jsonSnapshot.snapshotFolderExists()
        jsonSnapshot.exists()
    }

    def "Match json snapshot"() {
        given:
        jsonSnapshot.create()

        when:
        def matchesSnapshot = jsonSnapshot.matchesSnapshot()
        def matchesRelationNames = jsonSnapshot.matchesRelationNames()

        then:
        matchesSnapshot
        matchesRelationNames
    }

    def "Do not match json snapshot when file contains updated property"() {
        given:
        jsonSnapshot.create()
        def snapshotFile = jsonSnapshot.getSnapshotFile()
        def json = snapshotFile.text
        json = json.replaceAll('test', 'test123')
        snapshotFile.write(json)

        def relationNamesFile = jsonSnapshot.getRelationNamesFile()
        json = relationNamesFile.text
        json = json.replaceAll('TESTREL123', 'TESTREL234')
        relationNamesFile.write(json)

        when:
        def matchesSnapshot = jsonSnapshot.matchesSnapshot()
        def matchesRelationNames = jsonSnapshot.matchesRelationNames()

        then:
        !matchesSnapshot
        !matchesRelationNames
    }
}
