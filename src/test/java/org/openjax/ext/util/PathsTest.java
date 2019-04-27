/* Copyright (c) 2008 OpenJAX
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * You should have received a copy of The MIT License (MIT) along with this
 * program. If not, see <http://opensource.org/licenses/MIT/>.
 */

package org.openjax.ext.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class PathsTest {
  @Test
  public void testIsAbsoluteLocalURL() {
    assertFalse(Paths.isAbsoluteLocalURL(""));
    assertFalse(Paths.isAbsoluteLocalURL("file"));
    assertFalse(Paths.isAbsoluteLocalURL("file:"));
    assertFalse(Paths.isAbsoluteLocalURL("file:/"));

    assertFalse(Paths.isAbsoluteLocalURL("jar:file"));
    assertFalse(Paths.isAbsoluteLocalURL("jar:file:"));
    assertFalse(Paths.isAbsoluteLocalURL("jar:file:/"));

    // Here, "foo" is considered as the host
    assertFalse(Paths.isAbsoluteLocalURL("file://foo"));

    assertTrue(Paths.isAbsoluteLocalURL("file:/foo"));
    assertFalse(Paths.isAbsoluteLocalURL("jar:file:/foo"));
    assertTrue(Paths.isAbsoluteLocalURL("jar:file:/foo!/bar"));

    // Here, "" is considered as the host, and "foo" is considered as the path
    assertTrue(Paths.isAbsoluteLocalURL("file:///foo"));
    assertTrue(Paths.isAbsoluteLocalURL("file:///foo/"));
    assertTrue(Paths.isAbsoluteLocalURL("file:///foo/bar"));

    assertTrue(Paths.isAbsoluteLocalURL("file://localhost/foo/bar"));
    assertFalse(Paths.isAbsoluteLocalURL("file://google.com/foo/bar"));
  }

  @Test
  public void testIsAbsoluteLocalUnix() {
    assertFalse(Paths.isAbsoluteLocalUnix(""));
    assertTrue(Paths.isAbsoluteLocalUnix("/"));
    assertTrue(Paths.isAbsoluteLocalUnix("//"));
    assertTrue(Paths.isAbsoluteLocalUnix("/./."));
    assertTrue(Paths.isAbsoluteLocalUnix("//foo/bar"));
    assertTrue(Paths.isAbsoluteLocalUnix("/foo"));
    assertTrue(Paths.isAbsoluteLocalUnix("/foo/.//bar//.././."));
    assertTrue(Paths.isAbsoluteLocalUnix("//foo/../bar/"));
    assertFalse(Paths.isAbsoluteLocalUnix("/\0"));
  }

  @Test
  public void testIsAbsoluteLocalWindows() {
    assertTrue(Paths.isAbsoluteLocalWindows("\\\\test\\test$\\TEST.xls"));
    assertTrue(Paths.isAbsoluteLocalWindows("\\\\server\\share\\folder\\\\myfile.txt"));
    assertTrue(Paths.isAbsoluteLocalWindows("\\\\server\\share\\myfile.txt"));
    assertTrue(Paths.isAbsoluteLocalWindows("\\\\123.123.123.123\\.\\share\\..\\folder\\\\myfile.txt"));
    assertTrue(Paths.isAbsoluteLocalWindows("c:\\folder\\myfile.txt"));
    assertTrue(Paths.isAbsoluteLocalWindows("c:\\folder\\.\\myfileWithoutExtension"));
  }

  @Test
  public void testIsAbsoluteLocal() {
    // Local UNIX
    assertTrue(Paths.isAbsoluteLocal("/etc/profile"));
    assertTrue(Paths.isAbsoluteLocal("//etc/./profile"));
    assertTrue(Paths.isAbsoluteLocal("file:/etc/profile"));
    assertTrue(Paths.isAbsoluteLocal("file:///etc/profile"));
    assertTrue(Paths.isAbsoluteLocal("file://localhost/etc/profile"));
    assertFalse(Paths.isAbsoluteLocal("file://localhostx/etc/profile"));

    // Local Windows
    assertTrue(Paths.isAbsoluteLocal("/C:/Windows"));
    assertTrue(Paths.isAbsoluteLocal("file:///C:/Windows"));
    assertTrue(Paths.isAbsoluteLocal("file://localhost/C:/Windows"));
    assertFalse(Paths.isAbsoluteLocal("file://localhostx/C:/Windows"));
    assertTrue(Paths.isAbsoluteLocal("file:/C:/Windows"));

    // Remote
    assertFalse(Paths.isAbsoluteLocal("http://www.google.com"));
    assertFalse(Paths.isAbsoluteLocal("ftp://ftp.google.com"));
  }

  @Test
  public void testNewPath() {
    // UNIX
    assertEquals("/etc/profile", Paths.newPath("/etc/", "profile"));
    assertEquals("/etc/profile", Paths.newPath("/etc", "profile"));
    assertEquals("/etc/profile", Paths.newPath("/etc", "/profile"));
    assertEquals("/etc/profile", Paths.newPath("/etc/", "/profile"));

    // Windows
    assertEquals("C:\\Windows\\System32", Paths.newPath("C:\\Windows", "System32"));
    assertEquals("C:\\Windows\\System32", Paths.newPath("C:\\Windows", "\\System32"));
    assertEquals("C:\\Windows\\System32", Paths.newPath("C:\\Windows\\", "System32"));
    assertEquals("C:\\Windows\\System32", Paths.newPath("C:\\Windows\\", "\\System32"));

    // Web
    assertEquals("http://www.google.com/images", Paths.newPath("http://www.google.com", "images"));
    assertEquals("http://www.google.com/images", Paths.newPath("http://www.google.com/", "images"));
    assertEquals("http://www.google.com/images", Paths.newPath("http://www.google.com", "/images"));
    assertEquals("http://www.google.com/images", Paths.newPath("http://www.google.com/", "/images"));
    assertEquals("ftp://www.google.com/files", Paths.newPath("ftp://www.google.com", "files"));
  }

  @Test
  public void testGetName() {
    try {
      Paths.getName(null);
      fail("Expected NullPointerException");
    }
    catch (final NullPointerException e) {
    }

    try {
      Paths.getName("");
      fail("Expected IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    assertEquals("share.txt", Paths.getName("file:///usr/share/../share.txt"));
    assertEquals("lib", Paths.getName("file:///usr/share/../share/../lib"));
    assertEquals("var.old", Paths.getName("/usr/share/../share/../lib/../../var.old"));
    assertEquals("var", Paths.getName("/usr/share/../share/../lib/../../var/"));
    assertEquals("resolv.conf", Paths.getName("/etc/resolv.conf"));
    assertEquals("name", Paths.getName("name"));
  }

  @Test
  public void testGetShortName() {
    try {
      Paths.getShortName(null);
      fail("Expected NullPointerException");
    }
    catch (final NullPointerException e) {
    }

    try {
      Paths.getShortName("");
      fail("Expected IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    assertEquals("share", Paths.getShortName("file:///usr/share/../share.txt"));
    assertEquals("lib", Paths.getShortName("file:///usr/share/../share/../lib"));
    assertEquals("var", Paths.getShortName("/usr/share/../share/../lib/../../var.tar.old"));
    assertEquals("var", Paths.getShortName("/usr/share/../share/../lib/../../var/"));
    assertEquals("resolv", Paths.getShortName("/etc/resolv.conf"));
    assertEquals("name", Paths.getShortName("name"));
  }

  @Test
  public void testGetParent() {
    assertEquals("file:///usr/share/../", Paths.getParent("file:///usr/share/../share"));
    assertEquals("/usr/share/../share/", Paths.getParent("/usr/share/../share/.."));
    assertEquals("arp/../", Paths.getParent("arp/../pom.xml"));
    assertEquals("/usr/share/../share/../", Paths.getParent("/usr/share/../share/../../"));
    assertEquals("file:///usr/local/bin/../lib/../", Paths.getParent("file:///usr/local/bin/../lib/../bin"));
  }

  @Test
  public void testCanonicalize() {
    // Windows
    assertEquals("c:\\Windows\\System32", Paths.canonicalize("C:\\Windows\\\\System32"));
    assertEquals("c:\\Windows\\System32", Paths.canonicalize("C:\\Windows\\.\\System32"));
    assertEquals("c:\\Windows\\System32", Paths.canonicalize("C:\\Windows\\.\\System32\\..\\System32"));
    assertEquals("c:\\Windows\\System32\\", Paths.canonicalize("C:\\Windows\\.\\System32\\..\\System32\\"));
    assertEquals("\\\\test\\test$\\TEST.xls", Paths.canonicalize("\\\\test\\\\test$\\\\TEST.xls"));
    assertEquals("\\\\server\\share\\folder\\myfile.txt", Paths.canonicalize("\\\\server\\.\\share\\folder\\.\\\\myfile.txt"));
    assertEquals("\\\\server\\share.\\myfile.txt", Paths.canonicalize("\\\\server\\share.\\myfile.txt"));
    assertEquals("\\\\123.123.123.123\\folder\\myfile.txt", Paths.canonicalize("\\\\123.123.123.123\\.\\share\\..\\folder\\\\myfile.txt"));
    assertEquals("c:\\folder\\myfile.txt", Paths.canonicalize("c:\\folder\\myfile.txt"));
    assertEquals("c:\\folder\\myfileWithoutExtension", Paths.canonicalize("c:\\folder\\.\\myfileWithoutExtension"));

    // UNIX
    assertEquals("/usr/share/", Paths.canonicalize("/usr/share////////////"));
    assertEquals("/usr/share/", Paths.canonicalize("/usr/././/./share/./"));
    assertEquals("/usr/share", Paths.canonicalize("/usr/share//."));
    assertEquals("usr/share.", Paths.canonicalize("./usr//./share./."));
    assertEquals("/usr", Paths.canonicalize("/usr/share/../share/.."));
    assertEquals("pom.xml", Paths.canonicalize("arp/../pom.xml"));
    assertEquals("pom.xml", Paths.canonicalize("a/../pom.xml"));
    assertEquals("/../pom.xml", Paths.canonicalize("/../pom.xml"));
    assertEquals("/", Paths.canonicalize("/usr/share/../.share/../../"));
    assertEquals("", Paths.canonicalize("usr/share/../sh.are/../../"));
    assertEquals("/var.old", Paths.canonicalize("/usr/share/../share/../lib/../../var.old"));
    assertEquals("/.var/", Paths.canonicalize("/usr/share/../share/../lib/../../.var/"));

    // File URL
    assertEquals("file:///usr/share", Paths.canonicalize("file:///usr/share/../share"));
    assertEquals("file:///usr/local/bin.", Paths.canonicalize("file:///usr//local/././bin/../lib/../bin."));
  }

  @Test
  public void testGetCanonicalParent() {
    assertEquals("file:///usr", Paths.getCanonicalParent("file:///usr/./share/../share"));
    assertEquals("", Paths.getCanonicalParent("/usr/share/..//./share/.."));
    assertNull(Paths.getCanonicalParent("arp/../././pom.xml"));
    assertEquals("", Paths.getCanonicalParent("/usr//./././share/../share/../../"));
    assertEquals("file:///usr/local", Paths.getCanonicalParent("file://////usr/local/bin/../lib/../bin"));
  }
}