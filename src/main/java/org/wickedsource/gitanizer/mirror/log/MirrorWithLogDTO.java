package org.wickedsource.gitanizer.mirror.log;

import org.wickedsource.gitanizer.mirror.list.MirrorDTO;

public class MirrorWithLogDTO extends MirrorDTO {

    private String name;

    private String log;

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }
}
