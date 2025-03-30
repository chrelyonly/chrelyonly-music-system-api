package cn.chrelyonly.chrelyonlymusicsystemapi.aop;


import cn.hutool.core.util.StrUtil;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.Objects;

/**
 * @author chrelyonly
 * @version 3.1
 * @since 2022/11/3 16:53
 * remark: 加密解密注解的实现
 */
public class MyAesUtil {


    /**
     * aes加密解密密钥
     */
    private static final String AES_KEY = "arg1uhIRGHq6jYSFFkCFgoCicAvLKxqB";


    /**
     * aes加密
     *
     * @param content 加密的文本
     * @return 密文
     */
    public static String myAes(String content) {
        byte[] res = encrypt(content.getBytes(StandardCharsets.UTF_8), AES_KEY);
        if (res.length == 0) {
            return "";
        }
        return new String(encodeRes(res), StandardCharsets.UTF_8);
    }


    /**
     * aes解密
     *
     * @param content 解密文本
     * @return 明文
     */
    public static String myDec(String content) {

        byte[] hexBytes = decryptFormBase64(content, AES_KEY);
        return hexBytes == null ? null : new String(hexBytes, StandardCharsets.UTF_8);
    }

    //    *******************************************************************************加解密依赖的方法*******************************************************************************
    @Nullable
    private static byte[] decryptFormBase64(@Nullable String content, String aesTextKey) {
        return StrUtil.isEmpty(content) ? null : decryptFormBase64(content.getBytes(StandardCharsets.UTF_8), aesTextKey);
    }

    private static byte[] decryptFormBase64(byte[] content, String aesTextKey) {
        return decrypt(decode(content), aesTextKey);
    }

    private static byte[] decrypt(byte[] content, String aesTextKey) {
        return decrypt(content, ((String) Objects.requireNonNull(aesTextKey)).getBytes(StandardCharsets.UTF_8));
    }

    private static byte[] decrypt(byte[] encrypted, byte[] aesKey) {
        return decodeRes(aes(encrypted, aesKey, 2));
    }

    private static byte[] decodeRes(byte[] decrypted) {
        int pad = decrypted[decrypted.length - 1];
        if (pad < 1 || pad > 32) {
            pad = 0;
        }

        return pad > 0 ? Arrays.copyOfRange(decrypted, 0, decrypted.length - pad) : decrypted;
    }

    private static byte[] decode(byte[] src) {
        if (src.length == 0) {
            return src;
        }
        return Base64.getDecoder().decode(src);
    }

    private static byte[] encodeRes(byte[] src) {
        if (src.length == 0) {
            return src;
        }
        return Base64.getEncoder().encode(src);
    }

    private static byte[] encrypt(byte[] content, String aesTextKey) {
        return encrypt(content, ((String) Objects.requireNonNull(aesTextKey)).getBytes(StandardCharsets.UTF_8));
    }

    private static byte[] encrypt(byte[] content, byte[] aesKey) {
        return aes(encode(content), aesKey, 1);
    }

    private static byte[] encode(byte[] src) {
        int count = src.length;
        int amountToPad = 32 - count % 32;
        byte pad = (byte) (amountToPad & 255);
        byte[] pads = new byte[amountToPad];

        int length;
        for (length = 0; length < amountToPad; ++length) {
            pads[length] = pad;
        }

        length = count + amountToPad;
        byte[] dest = new byte[length];
        System.arraycopy(src, 0, dest, 0, count);
        System.arraycopy(pads, 0, dest, count, amountToPad);
        return dest;
    }

    private static byte[] aes(byte[] encrypted, byte[] aesKey, int mode) {
        Assert.isTrue(aesKey.length == 32, "IllegalAesKey, aesKey's length must be 32");
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            SecretKeySpec keySpec = new SecretKeySpec(aesKey, "AES");
            IvParameterSpec iv = new IvParameterSpec(Arrays.copyOfRange(aesKey, 0, 16));
            cipher.init(mode, keySpec, iv);
            return cipher.doFinal(encrypted);
        } catch (Exception var6) {
            var6.printStackTrace();
            return new byte[0];
        }
    }

}
