package com.oyp.crypt.aes

import com.oyp.crypt.encode.Base64Util
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

object AESCrypt {
    //AES/CBC/NoPadding (128)
    //AES 秘钥长度是16位，每位8个字节，所以是  16*8=128

    //算法/工作模式/填充模式
    private val TRANSFORMATION = "AES"
    private val CRYPT_ALGORITHM = "AES"

    private val cipher = Cipher.getInstance(TRANSFORMATION)

    fun encrypt(originContent: String, aesKey: String): String {
        // 1、初始化AES相关的操作
        val key: SecretKeySpec = initKey(aesKey)
        // 2、初始化cipher对象(参数一：加密模式)
        cipher.init(Cipher.ENCRYPT_MODE, key)
        // 3、Base64编码 加密后的内容
        return Base64Util.encode(cipher.doFinal(originContent.toByteArray()))
    }

    fun decrypt(encryptContent: String, aesKey: String): String {
        // 1、初始化AES相关的操作
        val key: SecretKeySpec = initKey(aesKey)
        // 2、初始化cipher对象(参数一：解密模式)
        cipher.init(Cipher.DECRYPT_MODE, key)
        // 3、Base64解码 解密后的内容
        return String(cipher.doFinal(Base64Util.decode(encryptContent)))
    }

    private fun initKey(aesKey: String): SecretKeySpec {
        return SecretKeySpec(aesKey.toByteArray(), CRYPT_ALGORITHM)
    }
}

fun main(args: Array<String>) {
    // 原文
    val originContent = "欧阳鹏的博客：http://blog.csdn.net/ouyang_peng"
    // 自定义的des的秘钥
    val desKey = "1234567812345678"
    val desEncryptContent = AESCrypt.encrypt(originContent, desKey)
    println("原文为：【${originContent}】 进行Des加密后的内容为：【${desEncryptContent}】")

    val desDecryptContent = AESCrypt.decrypt(desEncryptContent, desKey)
    println("秘文为：【${desEncryptContent}】 进行Des解密后的内容为：【${desDecryptContent}】")
}