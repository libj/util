/* Copyright (c) 2008 LibJ
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

package org.libj.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class StringPathsTest {
  @Test
  public void testIsAbsoluteLocalURL() {
    assertFalse(StringPaths.isAbsoluteLocalURL(""));
    assertFalse(StringPaths.isAbsoluteLocalURL("file"));
    assertFalse(StringPaths.isAbsoluteLocalURL("file:"));
    assertFalse(StringPaths.isAbsoluteLocalURL("file:/"));

    assertFalse(StringPaths.isAbsoluteLocalURL("jar:file"));
    assertFalse(StringPaths.isAbsoluteLocalURL("jar:file:"));
    assertFalse(StringPaths.isAbsoluteLocalURL("jar:file:/"));

    // Here, "foo" is considered as the host
    assertFalse(StringPaths.isAbsoluteLocalURL("file://foo"));

    assertTrue(StringPaths.isAbsoluteLocalURL("file:/foo"));
    assertFalse(StringPaths.isAbsoluteLocalURL("jar:file:/foo"));
    assertTrue(StringPaths.isAbsoluteLocalURL("jar:file:/foo!/bar"));

    // Here, "" is considered as the host, and "foo" is considered as the path
    assertTrue(StringPaths.isAbsoluteLocalURL("file:///foo"));
    assertTrue(StringPaths.isAbsoluteLocalURL("file:///foo/"));
    assertTrue(StringPaths.isAbsoluteLocalURL("file:///foo/bar"));

    assertTrue(StringPaths.isAbsoluteLocalURL("file://localhost/foo/bar"));
    assertFalse(StringPaths.isAbsoluteLocalURL("file://google.com/foo/bar"));
  }

  @Test
  public void testIsAbsoluteLocalUnix() {
    assertFalse(StringPaths.isAbsoluteLocalUnix(""));
    assertTrue(StringPaths.isAbsoluteLocalUnix("/"));
    assertTrue(StringPaths.isAbsoluteLocalUnix("//"));
    assertTrue(StringPaths.isAbsoluteLocalUnix("/./."));
    assertTrue(StringPaths.isAbsoluteLocalUnix("//foo/bar"));
    assertTrue(StringPaths.isAbsoluteLocalUnix("/foo"));
    assertTrue(StringPaths.isAbsoluteLocalUnix("/foo/.//bar//.././."));
    assertTrue(StringPaths.isAbsoluteLocalUnix("//foo/../bar/"));
    assertFalse(StringPaths.isAbsoluteLocalUnix("/\0"));
  }

  @Test
  public void testIsAbsoluteLocalWindows() {
    assertTrue(StringPaths.isAbsoluteLocalWindows("\\\\test\\test$\\TEST.xls"));
    assertTrue(StringPaths.isAbsoluteLocalWindows("\\\\server\\share\\folder\\\\myfile.txt"));
    assertTrue(StringPaths.isAbsoluteLocalWindows("\\\\server\\share\\myfile.txt"));
    assertTrue(StringPaths.isAbsoluteLocalWindows("\\\\123.123.123.123\\.\\share\\..\\folder\\\\myfile.txt"));
    assertTrue(StringPaths.isAbsoluteLocalWindows("c:\\folder\\myfile.txt"));
    assertTrue(StringPaths.isAbsoluteLocalWindows("c:\\folder\\.\\myfileWithoutExtension"));
  }

  @Test
  public void testIsAbsoluteLocal() {
    // Local UNIX
    assertTrue(StringPaths.isAbsoluteLocal("/etc/profile"));
    assertTrue(StringPaths.isAbsoluteLocal("//etc/./profile"));
    assertTrue(StringPaths.isAbsoluteLocal("file:/etc/profile"));
    assertTrue(StringPaths.isAbsoluteLocal("file:///etc/profile"));
    assertTrue(StringPaths.isAbsoluteLocal("file://localhost/etc/profile"));
    assertFalse(StringPaths.isAbsoluteLocal("file://localhostx/etc/profile"));

    // Local Windows
    assertTrue(StringPaths.isAbsoluteLocal("/C:/Windows"));
    assertTrue(StringPaths.isAbsoluteLocal("file:///C:/Windows"));
    assertTrue(StringPaths.isAbsoluteLocal("file://localhost/C:/Windows"));
    assertFalse(StringPaths.isAbsoluteLocal("file://localhostx/C:/Windows"));
    assertTrue(StringPaths.isAbsoluteLocal("file:/C:/Windows"));

    // Remote
    assertFalse(StringPaths.isAbsoluteLocal("http://www.google.com"));
    assertFalse(StringPaths.isAbsoluteLocal("ftp://ftp.google.com"));
  }

  @Test
  public void testNewPath() {
    // UNIX
    assertEquals("/etc/profile", StringPaths.newPath("/etc/", "profile"));
    assertEquals("/profile", StringPaths.newPath("/etc", "profile"));
    assertEquals("/profile", StringPaths.newPath("/etc", "/profile"));
    assertEquals("/etc/profile", StringPaths.newPath("/etc/", "/profile"));

    // Windows
    assertEquals("C:\\System32", StringPaths.newPath("C:\\Windows", "System32"));
    assertEquals("C:\\System32", StringPaths.newPath("C:\\Windows", "\\System32"));
    assertEquals("C:\\Windows\\System32", StringPaths.newPath("C:\\Windows\\", "System32"));
    assertEquals("C:\\Windows\\System32", StringPaths.newPath("C:\\Windows\\", "\\System32"));

    // Web
    assertEquals("http://images", StringPaths.newPath("http://www.google.com", "images"));
    assertEquals("http://images", StringPaths.newPath("http://www.google.com", "/images"));
    assertEquals("http://www.google.com/images", StringPaths.newPath("http://www.google.com/", "images"));
    assertEquals("http://www.google.com/images", StringPaths.newPath("http://www.google.com/", "/images"));
    assertEquals("ftp://www.google.com/files", StringPaths.newPath("ftp://www.google.com/", "files"));
  }

  @Test
  public void testGetName() {
    try {
      StringPaths.getName(null);
      fail("Expected IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      StringPaths.getName("");
      fail("Expected IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    assertEquals("share.txt", StringPaths.getName("file:///usr/share/../share.txt"));
    assertEquals("lib", StringPaths.getName("file:///usr/share/../share/../lib"));
    assertEquals("var.old", StringPaths.getName("/usr/share/../share/../lib/../../var.old"));
    assertEquals("var", StringPaths.getName("/usr/share/../share/../lib/../../var/"));
    assertEquals("resolv.conf", StringPaths.getName("/etc/resolv.conf"));
    assertEquals("name", StringPaths.getName("name"));
  }

  @Test
  public void testGetSimpleName() {
    try {
      StringPaths.getSimpleName(null);
      fail("Expected IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      StringPaths.getSimpleName("");
      fail("Expected IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    assertEquals("share", StringPaths.getSimpleName("file:///usr/share/../share.txt"));
    assertEquals("lib", StringPaths.getSimpleName("file:///usr/share/../share/../lib"));
    assertEquals("var", StringPaths.getSimpleName("/usr/share/../share/../lib/../../var.tar.old"));
    assertEquals("var", StringPaths.getSimpleName("/usr/share/../share/../lib/../../var/"));
    assertEquals("resolv", StringPaths.getSimpleName("/etc/resolv.conf"));
    assertEquals("name", StringPaths.getSimpleName("name"));
  }

  @Test
  public void testGetParent() {
    assertEquals("file:///usr/share/../", StringPaths.getParent("file:///usr/share/../share"));
    assertEquals("/usr/share/../share/", StringPaths.getParent("/usr/share/../share/.."));
    assertEquals("arp/../", StringPaths.getParent("arp/../pom.xml"));
    assertEquals("/usr/share/../share/../", StringPaths.getParent("/usr/share/../share/../../"));
    assertEquals("file:///usr/local/bin/../lib/../", StringPaths.getParent("file:///usr/local/bin/../lib/../bin"));
    assertNull(StringPaths.getParent("resource"));
    assertNull(StringPaths.getParent("classpath://resource"));
  }

  @Test
  public void testCanonicalize() {
    assertEquals("", StringPaths.canonicalize(""));
    assertEquals("../../../../config/src/main/resources/foo-bar.txt", StringPaths.canonicalize("../../../../config/src/main/resources/foo-bar.txt"));

    // Windows
    assertEquals("c:\\Windows\\System32", StringPaths.canonicalize("C:\\Windows\\\\System32"));
    assertEquals("c:\\Windows\\System32", StringPaths.canonicalize("C:\\Windows\\.\\System32"));
    assertEquals("c:\\Windows\\System32", StringPaths.canonicalize("C:\\Windows\\.\\System32\\..\\System32"));
    assertEquals("c:\\Windows\\System32\\", StringPaths.canonicalize("C:\\Windows\\.\\System32\\..\\System32\\"));
    assertEquals("\\\\test\\test$\\TEST.xls", StringPaths.canonicalize("\\\\test\\\\test$\\\\TEST.xls"));
    assertEquals("\\\\server\\share\\folder\\myfile.txt", StringPaths.canonicalize("\\\\server\\.\\share\\folder\\.\\\\myfile.txt"));
    assertEquals("\\\\server\\share.\\myfile.txt", StringPaths.canonicalize("\\\\server\\share.\\myfile.txt"));
    assertEquals("\\\\123.123.123.123\\folder\\myfile.txt", StringPaths.canonicalize("\\\\123.123.123.123\\.\\share\\..\\folder\\\\myfile.txt"));
    assertEquals("c:\\folder\\myfile.txt", StringPaths.canonicalize("c:\\folder\\myfile.txt"));
    assertEquals("c:\\folder\\myfileWithoutExtension", StringPaths.canonicalize("c:\\folder\\.\\myfileWithoutExtension"));

    // UNIX
    assertEquals("etc/resolv.conf", StringPaths.canonicalize("etc//resolv.conf"));
    assertEquals("/usr/share/", StringPaths.canonicalize("/usr/share////////////"));
    assertEquals("/usr/share/", StringPaths.canonicalize("/usr/././/./share/./"));
    assertEquals("/usr/share", StringPaths.canonicalize("/usr/share//."));
    assertEquals("usr/share.", StringPaths.canonicalize("./usr//./share./."));
    assertEquals("/usr", StringPaths.canonicalize("/usr/share/../share/.."));
    assertEquals("pom.xml", StringPaths.canonicalize("arp/../pom.xml"));
    assertEquals("pom.xml", StringPaths.canonicalize("a/../pom.xml"));
    assertEquals("/../pom.xml", StringPaths.canonicalize("/../pom.xml"));
    assertEquals("/", StringPaths.canonicalize("/usr/share/../.share/../../"));
    assertEquals("", StringPaths.canonicalize("usr/share/../sh.are/../../"));
    assertEquals("/var.old", StringPaths.canonicalize("/usr/share/../share/../lib/../../var.old"));
    assertEquals("/.var/", StringPaths.canonicalize("/usr/share/../share/../lib/../../.var/"));

    // File URL
    assertEquals("file:///usr/share", StringPaths.canonicalize("file:///usr/share/../share"));
    assertEquals("file:///usr/local/bin.", StringPaths.canonicalize("file:///usr//local/././bin/../lib/../bin."));
  }

  @Test
  public void testGetCanonicalParent() {
    assertEquals("file:///usr/", StringPaths.getCanonicalParent("file:///usr/./share/../share"));
    assertEquals("/", StringPaths.getCanonicalParent("/usr/share/..//./share/.."));
    assertNull(StringPaths.getCanonicalParent("arp/../././pom.xml"));
    assertEquals("/", StringPaths.getCanonicalParent("/usr//./././share/../share/../../"));
    assertEquals("file:///usr/local/", StringPaths.getCanonicalParent("file://////usr/local/bin/../lib/../bin"));
  }
}