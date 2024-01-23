package com.github.the_gigi.llm_playground;

public class TextUtil {
  public static String breakStringIntoLines(String input, int maxLineLength) {
    StringBuilder result = new StringBuilder();
    String[] paragraphs = input.split("\n");

    for (String paragraph : paragraphs) {
      String[] words = paragraph.split(" ");
      var line = new StringBuilder();

      for (String word : words) {
        // Check if adding the next word exceeds the max line length
        if (line.length() + word.length() + 1 > maxLineLength) {
          // Add the current line to the result and start a new line
          result.append(line).append("\n");
          line = new StringBuilder();
        }

        if (!line.isEmpty()) {
          line.append(" ");
        }
        line.append(word);
      }

      // Add the last line of the paragraph
      if (!line.isEmpty()) {
        result.append(line);
      }

      // Add a newline character after each paragraph except the last one
      if (!paragraph.equals(paragraphs[paragraphs.length - 1])) {
        result.append("\n");
      }
    }

    return result.toString();
  }
}
