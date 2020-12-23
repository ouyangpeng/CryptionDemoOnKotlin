package com.oyp.crypt.aes

import com.oyp.crypt.encode.Base64Util
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

/**
 * 对称加密：AES
 * 特点：可逆，加密速度快，可以加密大文件
 */
object AESCryptUtil {
    //AES/CBC/NoPadding (128)
    //AES 秘钥长度是16位，每位8个字节，所以是  16*8=128

    //   算法/工作模式/填充模式
    //   AES/CBC/NoPadding (128)
    //   AES/CBC/PKCS5Padding(128)
    //   AES/ECB/NoPadding(128)
    //   AES/ECB/PKCS5Padding(128)
    private val TRANSFORMATION = "AES/CBC/PKCS5Padding"
    private val CRYPT_ALGORITHM = "AES"

    private val cipher = Cipher.getInstance(TRANSFORMATION)

    fun encrypt(originContent: String, aesKey: String): String {
        initCipher(aesKey, Cipher.ENCRYPT_MODE)
        // 3、Base64编码 加密后的内容
        return Base64Util.encode(cipher.doFinal(originContent.toByteArray()))
    }

    fun decrypt(encryptContent: String, aesKey: String): String {
        initCipher(aesKey, Cipher.DECRYPT_MODE)
        // 3、Base64解码 解密后的内容
        return String(cipher.doFinal(Base64Util.decode(encryptContent)))
    }

    private fun initCipher(aesKey: String, mode: Int) {
        // 1、初始化AES相关的操作
        val key: SecretKeySpec = initKey(aesKey)
        // 2、初始化cipher对象(参数一：解密模式)
        if (TRANSFORMATION.contains("CBC")) {
            // CBC工作模式需要额外添加参数,否则报错：java.security.InvalidKeyException: Parameters missing
            val iv = IvParameterSpec(aesKey.toByteArray())
            cipher.init(mode, key, iv)
        } else {
            cipher.init(mode, key)
        }
    }

    private fun initKey(aesKey: String): SecretKeySpec {
        return SecretKeySpec(aesKey.toByteArray(), CRYPT_ALGORITHM)
    }
}

fun main(args: Array<String>) {
    // 原文
    val originContent = "欧阳鹏的博客：http://blog.csdn.net/ouyang_peng"
    // 自定义的aes的秘钥
    val aesKey = "1234567812345678"
    val aesEncryptContent = AESCryptUtil.encrypt(originContent, aesKey)
    println("原文为：【${originContent}】 进行aes加密后的内容为：【${aesEncryptContent}】")

    val aesDecryptContent = AESCryptUtil.decrypt(aesEncryptContent, aesKey)
    println("秘文为：【${aesEncryptContent}】 进行aes解密后的内容为：【${aesDecryptContent}】")
}