package org.luvx.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: org.luvx.utils
 * @Description:
 * @Author: Ren, Xie
 * @Date: 2019/6/21 11:55
 */
public class RestTemplateUtils {

    /**
     * 创建cookie
     *
     * @param cookie
     * @return
     */
    public static List<String> getCookie(Map<String, String> cookie) {
        List<String> cookies = new ArrayList<>();
        cookies.add("0013351188__project=ZQEEBCCN");
        cookies.add("0013351188__region=0000000001");
        cookies.add("0013351188__tenant=ZQEEBCCN");
        cookies.add("0013351188__workspace=sit");
        cookies.add("ALIPAYJSESSIONID=cLSXnDptNfzP2Wy1AoDMTdLJLcQ1Ze42authweb");
        cookies.add("ANTSESSIONID=Hrq1CBym37z7s3dHV9n0JYf0PHcap");
        cookies.add("JSESSIONID=Hrq1CBym37z7s3dHV9n0JYf0PHcap");
        cookies.add("LOCALE=zh_CN");
        cookies.add("acLoginFrom=antcloud_login");
        cookies.add("aliyungf_tc=AQAAAJ3e/HUNmw0As2gmav6HzWSTxzwe");
        cookies.add("authorization=hmac%200013351188-2%3AcFVxcWdadmQ2Z3Yyamw1bzhPdnFvWEhDMlpxc2ZtSUw%3D~0");
        cookies.add("bs_n_lang=zh_CN");
        cookies.add("ctoken=w2_js9Gqn3zPUSlY");
        cookies.add("isEnableLocale=disabled");
        cookies.add("nav_original_path=user.cloud.alipay.com");
        cookies.add("rtk=HGS1iaTYmxWJODmXzGjxFj3XJ/RXhNMwll3arNJgBg6FhHqxBf6");
        // cookies.add("tree=a636%010eac7e78-f91c-475d-8045-f86a1a4bd283%012");
        return cookies;
    }

    public static void printResponse(ResponseEntity<?> response) {
        HttpStatus status = response.getStatusCode();
        if (!status.is2xxSuccessful()) {
            System.out.println("非200");
        }
        System.out.println("-------------------------");
        System.out.println(response.getStatusCodeValue());
        System.out.println(response.getHeaders());
        System.out.println(response.getBody());
        System.out.println("-------------------------");
    }
}
