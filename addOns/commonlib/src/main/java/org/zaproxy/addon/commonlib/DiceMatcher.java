/*
 * Zed Attack Proxy (ZAP) and its related class files.
 *
 * ZAP is an HTTP/HTTPS proxy for assessing web application security.
 *
 * Copyright 2021 The ZAP Development Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.zaproxy.addon.commonlib;

import java.util.Arrays;

/**
 * A {@code DiceMatcher} that implements the Dice algorithm to measure the similarity between two
 * strings
 *
 * @since 1.3.0
 */
public final class DiceMatcher {

    private DiceMatcher() {}

    /**
     * @param a The first string to be compared
     * @param b The second string to be compared
     * @return The match percentage of the two strings, rounded off to the nearest integer
     */

    /*
     * Source : https://en.wikibooks.org/wiki/Algorithm_Implementation/Strings/Dice%27s_coefficient
     * License :  https://creativecommons.org/licenses/by-sa/3.0/
     * Author : Jelle Fresen
     * Changes : Fixed indexing to prevent out of array access
     * Released under CC-BY-SA.
     */

    public static int getMatchPercentage(String a, String b) {

        String s = a.replaceAll("\\s+", " ");
        String t = b.replaceAll("\\s+", " ");

        // Verifying the input:
        if (s == null || t == null) return 0;
        // Quick check to catch identical objects:
        if (s == t) return 1;
        // avoid exception for single character searches
        if (s.length() < 2 || t.length() < 2) return 0;

        // Create the bigrams for string s:
        final int n = s.length() - 1;
        final int[] sPairs = new int[n];
        for (int i = 0; i < n; i++)
            if (i == 0) sPairs[i] = s.charAt(i) << 16;
            else if (i == n - 1) sPairs[i - 1] |= s.charAt(i);
            else sPairs[i] = (sPairs[i - 1] |= s.charAt(i)) << 16;

        // Create the bigrams for string t:
        final int m = t.length() - 1;
        final int[] tPairs = new int[m];
        for (int i = 0; i < m; i++)
            if (i == 0) tPairs[i] = t.charAt(i) << 16;
            else if (i == m - 1) tPairs[i - 1] |= t.charAt(i);
            else tPairs[i] = (tPairs[i - 1] |= t.charAt(i)) << 16;

        // Sort the bigram lists:
        Arrays.sort(sPairs);
        Arrays.sort(tPairs);

        // Count the matches:
        int matches = 0, i = 0, j = 0;
        while (i < n && j < m) {
            if (sPairs[i] == tPairs[j]) {
                matches += 2;
                i++;
                j++;
            } else if (sPairs[i] < tPairs[j]) i++;
            else j++;
        }
        return (int) Math.floor((double) matches * 100 / (n + m));
    }
}
