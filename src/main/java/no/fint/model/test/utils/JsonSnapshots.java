package no.fint.model.test.utils;

import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.ClassPath;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class JsonSnapshots {
    @Getter
    private List<JsonSnapshot> jsonSnapshotList;

    public JsonSnapshots(String basePackage) {
        jsonSnapshotList = new ArrayList<>();

        try {
            ImmutableSet<ClassPath.ClassInfo> classInfos = ClassPath.from(this.getClass().getClassLoader()).getTopLevelClassesRecursive(basePackage);
            classInfos.stream().filter(classInfo -> isNotTestClass(classInfo.getName())).forEach(classInfo -> jsonSnapshotList.add(new JsonSnapshot(classInfo.load())));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private boolean isNotTestClass(String name) {
        String className = name.toLowerCase();
        return !className.endsWith("test") && !className.endsWith("spec");
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
