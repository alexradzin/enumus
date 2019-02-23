package org.enumus;

import org.enumus.samples.OsType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HierarchyTest {
    @Test
    void justValues() {
        assertTrue(OsType.values().length > 0);
    }

    @Test
    void parentOfRoot() {
        assertNull(OsType.OS.parent());
    }

    @Test
    void parentOfFirstLevel() {
        assertEquals(OsType.OS, OsType.Windows.parent());
        assertEquals(OsType.OS, OsType.Unix.parent());
    }

    @Test
    void parentOfSecondLevel() {
        assertEquals(OsType.Windows, OsType.WindowsNT.parent());
    }

    @Test
    void parentOfThirdLevel() {
        assertEquals(OsType.WindowsNT, OsType.WindowsNTServer.parent());
    }

    @Test
    void childrenOfLeaf() {
        assertArrayEquals(new OsType[0], OsType.WindowsNTServer.children());
    }

    @Test
    void childrenPreLeafLevel() {
        assertArrayEquals(new OsType[] {OsType.Linux, OsType.AIX, OsType.HpUx, OsType.SunOs}, OsType.Unix.children());
    }

    @Test
    void childrenIntermediateLevel() {
        assertArrayEquals(
                new OsType[] {OsType.WindowsNT, OsType.Windows2000, OsType.WindowsXp, OsType.WindowsVista, OsType.Windows7, OsType.Windows95, OsType.Windows98,},
                OsType.Windows.children());
    }

    @Test
    void childrenTopLeafLevel() {
        assertArrayEquals(new OsType[] {OsType.Windows, OsType.Unix}, OsType.OS.children());
    }

    @Test
    void callDirect() {
        assertTrue(OsType.Unix.supportsXWindowSystem());
    }

    @Test
    void callFromChild() {
        assertTrue(OsType.Linux.supportsXWindowSystem());
    }

    @Test
    void callNotImplemented() {
        assertFalse(OsType.Windows.supportsXWindowSystem());
    }

    @Test
    void callRoot() {
        assertFalse(OsType.OS.supportsXWindowSystem());
    }


    @Test
    void relation1() {
        assertTrue(OsType.Windows2000.isA(OsType.Windows));
        assertTrue(OsType.Linux.isA(OsType.Unix));
        assertFalse(OsType.Linux.isA(OsType.Windows));
    }

    @Test
    void relation2() {
        assertTrue(OsType.Windows2000Server.isA(OsType.Windows));
        assertTrue(OsType.Windows98.isA(OsType.Windows));
        assertFalse(OsType.Windows98.isA(OsType.Unix));
        assertFalse(OsType.Windows98.isA(OsType.Linux));
    }

    @Test
    void relationSiblings() {
        assertFalse(OsType.Linux.isA(OsType.AIX));
        assertFalse(OsType.SunOs.isA(OsType.HpUx));
        assertFalse(OsType.Windows2000Server.isA(OsType.Windows2000Workstation));
    }
}