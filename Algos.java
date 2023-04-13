import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.BufferedReader;
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
        BufferedReader br = new BufferedReader(new FileReader("Abbreviations_and_Slang.txt"));
        String line1 =  br.readLine();
        HashMap<String,String> abbs = new HashMap<String, String>();

        while(line1 != null){
            String str[] = line1.split(",");
            abbs.put(str[0], str[1]);
        }
        br.close();

        Algos a = new Algos();
        String pattern = "\\b[A-Z]{2,}\\b";
        Pattern regex = Pattern.compile(pattern);

        try {
            BufferedReader reader = new BufferedReader(new FileReader("covidvaccine 2.csv"));
            String line = reader.readLine();
            while (line != null) {
                String[] tweet = line.split(","); 
                Matcher matcher = regex.matcher(tweet[9]);
                if (matcher.find()) {
                    int iBM = a.BoyerMooreAlgo(tweet[9], matcher.group());
                    if (iBM != -1) {
                        System.out.println("Pattern found at index " + iBM);
                        char[] chars = tweet[9].toCharArray();
                        chars[iBM] = abbs.get(matcher.group());
                        tweet[9] = String.valueOf(chars);
                        System.out.println("Pattern changed at index " + iBM);
                    } else {
                        System.out.println("Pattern not found");
                    }
        
                    int iKMP = a.KMPAlgo(tweet[9], matcher.group());
                    if (iKMP != -1) {
                        System.out.println("Pattern found at index " + iKMP);
                        char[] chars = tweet[9].toCharArray();
                        chars[iKMP] = abbs.get(matcher.group());
                        tweet[9] = String.valueOf(chars);
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
