package org.wickedsource.gitanizer.mirror.list;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class MirrorDTOPairTest {

    @Test
    public void createsPairsFromList() {
        List<MirrorDTO> mirrors = new ArrayList<>();
        mirrors.add(mirror("mirror1"));
        mirrors.add(mirror("mirror2"));
        mirrors.add(mirror("mirror3"));
        mirrors.add(mirror("mirror4"));
        mirrors.add(mirror("mirror5"));

        List<MirrorDTOPair> pairs = MirrorDTOPair.fromList(mirrors);
        assertThat(pairs).hasSize(3);
        assertThat(pairs.get(0).getMirrors().get(0).getName()).isEqualTo("mirror1");
        assertThat(pairs.get(0).getMirrors().get(1).getName()).isEqualTo("mirror2");
        assertThat(pairs.get(1).getMirrors().get(0).getName()).isEqualTo("mirror3");
        assertThat(pairs.get(1).getMirrors().get(1).getName()).isEqualTo("mirror4");
        assertThat(pairs.get(2).getMirrors().get(0).getName()).isEqualTo("mirror5");
    }

    private MirrorDTO mirror(String name) {
        MirrorDTO mirror = new MirrorDTO();
        mirror.setName(name);
        return mirror;
    }

}