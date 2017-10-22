package no.fint.model.test.utils;

import lombok.Getter;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class JsonSnapshots {
    @Getter
    private List<JsonSnapshot> jsonSnapshotList;

    public JsonSnapshots(String basePackage) {
        jsonSnapshotList = new ArrayList<>();

        Set<Class<?>> classes = new Reflections(basePackage, new SubTypesScanner(false)).getSubTypesOf(Object.class);
        classes.forEach(clazz -> jsonSnapshotList.add(new JsonSnapshot(clazz)));
    }

    public void create() {
        jsonSnapshotList.forEach(JsonSnapshot::create);
    }

    public void cleanSnapshotFolder() {
        if (jsonSnapshotList.size() > 0) {
            jsonSnapshotList.get(0).cleanSnapshotFolder();
        }
    }

    public boolean matchesSnapshots() {
        Optional<JsonSnapshot> notMatches = jsonSnapshotList.stream().filter(jsonSnapshot -> !jsonSnapshot.matchesSnapshot()).findAny();
        return !notMatches.isPresent();
    }

    public boolean matchesRelationNames() {
        Optional<JsonSnapshot> notMatches = jsonSnapshotList.stream().filter(jsonSnapshot -> !jsonSnapshot.matchesRelationNames()).findAny();
        return !notMatches.isPresent();
    }

}
