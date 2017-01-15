package org.wickedsource.gitanizer.mirror.list;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MirrorDTOPair {

    private MirrorDTO mirror1;

    private MirrorDTO mirror2;

    public void addMirror(MirrorDTO mirror) {
        if (mirror1 == null) {
            mirror1 = mirror;
        } else if (mirror2 == null) {
            mirror2 = mirror;
        } else {
            throw new IllegalStateException("Both mirrors are already filled!");
        }
    }

    public List<MirrorDTO> getMirrors() {
        List<MirrorDTO> mirrors = new ArrayList<>();
        if (mirror1 != null) {
            mirrors.add(mirror1);
        }
        if (mirror2 != null) {
            mirrors.add(mirror2);
        }
        return mirrors;
    }

    public boolean isFull() {
        return mirror1 != null && mirror2 != null;
    }

    public boolean isEmpty() {
        return mirror1 == null && mirror2 == null;
    }

    public static List<MirrorDTOPair> fromList(List<MirrorDTO> mirrors) {
        if (mirrors.isEmpty()) {
            return Collections.emptyList();
        }

        List<MirrorDTOPair> pairs = new ArrayList<>();
        MirrorDTOPair currentPair = new MirrorDTOPair();
        pairs.add(currentPair);
        for (MirrorDTO mirror : mirrors) {
            if (currentPair.isFull()) {
                currentPair = new MirrorDTOPair();
                pairs.add(currentPair);
            }
            currentPair.addMirror(mirror);
        }
        return pairs;
    }
}
