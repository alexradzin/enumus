package org.enumus.samples;

import org.enumus.Hierarchy;

public enum OsType {
    OS(null),
        Windows(OS),
            WindowsNT(Windows),
                WindowsNTWorkstation(WindowsNT),
                WindowsNTServer(WindowsNT),
            Windows2000(Windows),
                Windows2000Server(Windows2000),
                Windows2000Workstation(Windows2000),
            WindowsXp(Windows),
            WindowsVista(Windows),
            Windows7(Windows),
            Windows95(Windows),
            Windows98(Windows),
    Unix(OS) {
        @Override
        public boolean supportsXWindowSystem() {
            return true;
        }
    },
    Linux(Unix),
        IOS(Linux),
        Android(Linux),
    AIX(Unix),
    HpUx(Unix),
    SunOs(Unix),
    ;

    private OsType parent;
    private static Hierarchy<OsType> hierarchy = new Hierarchy<>(OsType.class, e -> e.parent);


    OsType(OsType parent) {
        this.parent = parent;
    }

    public OsType parent() {
        return parent;
    }

    public OsType[] children() {
        return hierarchy.getChildren(this);
    }

    public boolean supportsXWindowSystem() {
        return hierarchy.invoke(this, args -> false);
    }

    public boolean isA(OsType other) {
        return hierarchy.relate(other, this);
    }
}