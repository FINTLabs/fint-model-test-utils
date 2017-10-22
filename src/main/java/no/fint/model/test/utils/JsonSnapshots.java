package no.fint.model.test.utils;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
public class JsonSnapshots {
    @Getter
    private List<JsonSnapshot> jsonSnapshotList;

    public JsonSnapshots(String basePackage) {
        jsonSnapshotList = new ArrayList<>();

        Set<String> allTypes = new Reflections(basePackage, new SubTypesScanner(false)).getAllTypes();
        allTypes.forEach(type -> {
            try {
                Class<?> clazz = Class.forName(type);
                log.info("Adding class: {}", type);
                jsonSnapshotList.add(new JsonSnapshot(clazz));
            } catch (ClassNotFoundException e) {
                throw new IllegalStateException(e);
            }
        });
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
