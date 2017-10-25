package no.fint.model.test.utils;

import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.ClassPath;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import spock.lang.Specification;

import java.io.IOException;
import java.lang.reflect.Modifier;
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
            classInfos.stream()
                    .map(ClassPath.ClassInfo::load)
                    .filter(this::isNotTestClass)
                    .filter(this::isNotEnum)
                    .filter(this::isNotAbstract)
                    .forEach(clazz -> jsonSnapshotList.add(new JsonSnapshot(clazz)));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private boolean isNotTestClass(Class<?> clazz) {
        return clazz.getSuperclass() != Specification.class;
    }

    private boolean isNotEnum(Class<?> clazz) {
        return !clazz.isEnum();
    }

    private boolean isNotAbstract(Class<?> clazz) {
        return !Modifier.isAbstract(clazz.getModifiers());
    }

    public boolean create() {
        for (JsonSnapshot jsonSnapshot : jsonSnapshotList) {
            boolean snapshotCreated = jsonSnapshot.create();
            if (!snapshotCreated) {
                return false;
            }
        }
        return true;
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
