/* Copyright (c) 2008 FastJAX
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

package org.fastjax.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class PathsTest {
  @Test
  public void testIsLocal() {
    // Local UNIX
    assertTrue(Paths.isLocal("/etc/profile"));
    assertTrue(Paths.isLocal("//etc/profile"));
    assertTrue(Paths.isLocal("file:/etc/profile"));
    assertTrue(Paths.isLocal("file:///etc/profile"));

    // Local Windows
    assertTrue(Paths.isLocal("/C:/Windows"));
    assertTrue(Paths.isLocal("file:///C:/Windows"));
    assertTrue(Paths.isLocal("file:/C:/Windows"));

    // Remote
    assertFalse(Paths.isLocal("http://www.google.com"));
    assertFalse(Paths.isLocal("ftp://ftp.google.com"));
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
    assertEquals(null, Paths.getName(null));
    assertEquals("", Paths.getName(""));
    assertEquals("share.txt", Paths.getName("file:///usr/share/../share.txt"));
    assertEquals("lib", Paths.getName("file:///usr/share/../share/../lib"));
    assertEquals("var.old", Paths.getName("/usr/share/../share/../lib/../../var.old"));
    assertEquals("var", Paths.getName("/usr/share/../share/../lib/../../var/"));
    assertEquals("resolv.conf", Paths.getName("/etc/resolv.conf"));
    assertEquals("name", Paths.getName("name"));
  }

  @Test
  public void testGetShortName() {
    assertEquals(null, Paths.getShortName(null));
    assertEquals("", Paths.getShortName(""));
    assertEquals("share", Paths.getShortName("file:///usr/share/../share.txt"));
    assertEquals("lib", Paths.getShortName("file:///usr/share/../share/../lib"));
    assertEquals("var", Paths.getShortName("/usr/share/../share/../lib/../../var.old"));
    assertEquals("var", Paths.getShortName("/usr/share/../share/../lib/../../var/"));
    assertEquals("resolv", Paths.getShortName("/etc/resolv.conf"));
    assertEquals("name", Paths.getShortName("name"));
  }

  @Test
  public void testGetParent() {
    assertEquals("file:///usr/share/..", Paths.getParent("file:///usr/share/../share"));
    assertEquals("/usr/share/../share", Paths.getParent("/usr/share/../share/.."));
    assertEquals("arp/..", Paths.getParent("arp/../pom.xml"));
    assertEquals("/usr/share/../share/..", Paths.getParent("/usr/share/../share/../../"));
    assertEquals("file:///usr/local/bin/../lib/..", Paths.getParent("file:///usr/local/bin/../lib/../bin"));
  }

  @Test
  public void testGetCanonicalParent() {
    assertEquals("file:///usr", Paths.getCanonicalParent("file:///usr/share/../share"));
    assertEquals("/usr", Paths.getCanonicalParent("/usr/share/../share/.."));
    assertEquals(null, Paths.getCanonicalParent("arp/../pom.xml"));
    assertEquals("", Paths.getCanonicalParent("/usr/share/../share/../../"));
    assertEquals("file:///usr/local", Paths.getCanonicalParent("file:///usr/local/bin/../lib/../bin"));
  }
}