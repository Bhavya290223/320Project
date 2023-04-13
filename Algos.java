import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.lang.Math;

public class Algos {

    private int[] lastOccurenceFunct(String pattern) {
        int[] last = new int[256];
        for (int i = 0; i < 256; i++) {
            last[i] = -1;
        }
        for (int i = 0; i < pattern.length(); i++) {
            last[pattern.charAt(i)] = i;
        }
        return last;
    }

    public int BoyerMooreAlgo(String text, String pattern) {
        int[] lof = lastOccurenceFunct(pattern);
        int n = text.length();
        int m = pattern.length();
        int i = m - 1;
        int j = m - 1;
        while (i < n) {
            char[] txt = text.toCharArray();
            char[] p = pattern.toCharArray();
            if (txt[i] == p[j]) {
                if (j == 0) {
                    return i;
                } else {
                    i--;
                    j--;
                }
            } else {
                int l = lof[txt[i]];
                int helper = m - Math.min(j, 1 + l);
                i += helper;
                j = m - 1;
            }
        }
        return -1;
    }

    private int[] failureFunct(String pattern) {
        int m = pattern.length();
        int[] f = new int[m];

        int i = 1;
        int j = 0;

        while (i < m) {
            if (pattern.charAt(i) == pattern.charAt(j)) {
                f[i] = j + 1;
                i++;
                j++;
            } else if (j > 0) {
                j = f[j - 1];
            } else {
                f[i] = 0;
                i++;
            }
        }
        return f;
    }

    public int KMPAlgo(String text, String pattern) {
        int[] f = failureFunct(pattern);
        int n = text.length();
        int m = pattern.length();
        int i = 0;
        int j = 0;
        while (i < n) {
            char[] txt = text.toCharArray();
            char[] p = pattern.toCharArray();
            if (txt[i] == p[j]) {
                if (j == m-1) {
                    return i - j;
                } else {
                    i++;
                    j++;
                }
            } else {
                if (j > 0) {
                    j = f[j-1];
                } else {
                    i++;
                }
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        HashMap<String, String> abbs = new HashMap<String, String>();
        abbs.put("int", "Integer");
        abbs.put("str", "String");
        abbs.put("bool", "Boolean");
        abbs.put("flt", "Float");
        abbs.put("dbl", "Double");
        abbs.put("chr", "Character");

        Algos a = new Algos();
        String pattern = "\\b[A-Z]{2,}\\b";
        Pattern regex = Pattern.compile(pattern);

        try {
            BufferedReader reader = new BufferedReader(new FileReader("filename.txt"));
            String line = reader.readLine();
            while (line != null) {
                Matcher matcher = regex.matcher(line);
                if (matcher.find()) {
                    int iBM = a.BoyerMooreAlgo(line, matcher.group());
                    if (iBM != -1) {
                        System.out.println("Pattern found at index " + iBM);
                        char[] chars = line.toCharArray();
                        chars[iBM] = abbs.get(matcher.group());
                        line = String.valueOf(chars);
                        System.out.println("Pattern changed at index " + iBM);
                    } else {
                        System.out.println("Pattern not found");
                    }
        
                    int iKMP = a.KMPAlgo(line, matcher.group());
                    if (iKMP != -1) {
                        System.out.println("Pattern found at index " + iKMP);
                        char[] chars = line.toCharArray();
                        chars[iKMP] = abbs.get(matcher.group());
                        line = String.valueOf(chars);
                        System.out.println("Pattern changed at index " + iKMP);
                    } else {
                        System.out.println("Pattern not found");
                    }
                }
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("ERROR: reading file");;
        }  
    }
}
