package org.luvx.pattern;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.length;
import static org.apache.commons.lang3.StringUtils.substring;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    private static final Pattern SUPER_UBB_AT_PATTERN = Pattern
            .compile("\\[at uid=(?<userId>\\d+)\\]@(?<username>.*?)\\[/at\\]");
    private static final Pattern SUPER_UBB_TAG_PATTERN = Pattern
            .compile("\\[tag id=(?<tagId>\\d+)\\]#(?<tagName>.*?)\\[/tag\\]");
    private static final Pattern SUPER_UBB_AT_TAG_PATTERN = Pattern
            .compile(SUPER_UBB_AT_PATTERN.pattern() + "|" + SUPER_UBB_TAG_PATTERN.pattern());

    public static void main1() {
        // String s = "abcdefg [tag id=123]#test[/tag]";
        // String s1 = ResourceUtils.cutLiteStringWithUbb(s, 10);
        // System.out.println(s1);
        //
        // s1 = ResourceUtils.cutLiteMomentContextWithUbb("abcdefg [at uid=123]@test[/at]", 10);
        // System.out.println(s1);
    }

    public static void main(String[] args) {
        String at = "abcdefg [at uid=123]@test[/at] hijklmn [at uid=123]@test1[/at] opq";
        String tag = "abcdefg [tag id=123]#test[/tag] hijklmn [tag id=123]#test1[/tag] opq";
        // at(at);
        // tag(tag);
        at_tag(at + " " + tag);
        System.out.println(cutLiteStringWithUbb(at + " " + tag, 60));
    }

    public static void at_tag(String s) {
        StringBuffer sb = new StringBuffer();
        Matcher matcher = SUPER_UBB_AT_TAG_PATTERN.matcher(s);
        while (matcher.find()) {
            String username1 = matcher.group("username");
            if (username1 != null) {
                String username = "@" + username1;
                matcher.appendReplacement(sb, Matcher.quoteReplacement(username));
            }

            String tagName1 = matcher.group("tagName");
            if (tagName1 != null) {
                String tagName = "#" + tagName1;
                matcher.appendReplacement(sb, Matcher.quoteReplacement(tagName));
            }
        }
        matcher.appendTail(sb);

        System.out.println(s + "->" + s.length());
        System.out.println(sb + "->" + sb.length());
        System.out.println("---------");
    }

    public static void at(String s) {
        StringBuffer sb = new StringBuffer();
        Matcher matcher = SUPER_UBB_AT_PATTERN.matcher(s);
        while (matcher.find()) {
            String username = matcher.group("username");
            String ubbText1 = "@" + username;
            matcher.appendReplacement(sb, Matcher.quoteReplacement(ubbText1));
        }
        matcher.appendTail(sb);

        System.out.println(s);
        System.out.println(sb);
    }

    public static void tag(String s) {
        StringBuffer sb = new StringBuffer();
        Matcher matcher = SUPER_UBB_TAG_PATTERN.matcher(s);
        while (matcher.find()) {
            String tagName = matcher.group("tagName");
            String ubbText1 = "#" + tagName;
            matcher.appendReplacement(sb, Matcher.quoteReplacement(ubbText1));
        }
        matcher.appendTail(sb);

        System.out.println(s);
        System.out.println(sb);
    }


    public static String cutLiteStringWithUbb(String origContent, int maxLength) {
        if (isBlank(origContent)) {
            return origContent;
        }
        StringBuffer sb = new StringBuffer();
        Matcher matcher = SUPER_UBB_AT_TAG_PATTERN.matcher(origContent);
        int preEnd = 0, preLen = 0;
        while (matcher.find()) {
            String username = matcher.group("username");
            if (username != null) {
                String ubbText = "@" + username;
                matcher.appendReplacement(sb, Matcher.quoteReplacement(ubbText));
            }
            String tagName = matcher.group("tagName");
            if (tagName != null) {
                String ubbText = "#" + tagName;
                matcher.appendReplacement(sb, Matcher.quoteReplacement(ubbText));
            }
            if (length(sb) > maxLength) {
                return substring(origContent, 0, preEnd) +
                        substring(origContent, preEnd, Math.min(preEnd + maxLength - preLen, matcher.start()));
            }
            preEnd = matcher.end();
            preLen = length(sb);
        }
        return substring(origContent, 0, Math.min(preEnd + maxLength - preLen, origContent.length()));
    }
}
