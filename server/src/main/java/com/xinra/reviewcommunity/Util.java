package com.xinra.reviewcommunity;

public class Util {
  /**
   * Normalizes user input. Leading and trailing whitespace is trimmed. If string is empty (or only
   * whitespace), {@code null} is returned.
   */
  public static String normalize(String str) {
    if (str == null) {
      return null;
    }
    str = str.trim();
    return str.isEmpty() ? null : str;
  }
}
