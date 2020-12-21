package com.oyp.crypt.des

import java.security.Key
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.DESKeySpec

object DesCrypt {
    private val CRYPT_METHOD = "DES"

    private val cipher = Cipher.getInstance(CRYPT_METHOD)
    private val desSecretKeyFactory = SecretKeyFactory.getInstance(CRYPT_METHOD)

    fun encrypt(originContent: String, desKey: String): ByteArray {
        // 1、初始化DES相关的操作
        val key: Key? = initKey(desKey)
        // 2、初始化cipher对象(参数一：加密模式)
        cipher.init(Cipher.ENCRYPT_MODE, key)
        // 3、加密
        return cipher.doFinal(originContent.toByteArray());
    }

    fun decrypt(encryptArray: ByteArray, desKey: String): ByteArray {
        // 1、初始化DES相关的操作
        val key: Key? = initKey(desKey)
        // 2、初始化cipher对象(参数一：解密模式)
        cipher.init(Cipher.DECRYPT_MODE, key)
        // 3、解密
        return cipher.doFinal(encryptArray);
    }

    private fun initKey(desKey: String): Key? {
        val desKeySpec = DESKeySpec(desKey.toByteArray())
        return desSecretKeyFactory.generateSecret(desKeySpec)
    }
}

fun main(args: Array<String>) {
    // 原文
    val originContent = "欧阳鹏的博客：http://blog.csdn.net/ouyang_peng"
    // 自定义的des的秘钥
    val desKey = "12345678"
    val desEncryptContent = DesCrypt.encrypt(originContent, desKey)
    println("原文为：【${originContent}】 进行Des加密后的内容为：【${desEncryptContent}】")

    val desDecryptContent = DesCrypt.decrypt(desEncryptContent, desKey)
    println("秘文为：【${desEncryptContent}】 进行Des加密后的内容为：【${String(desDecryptContent)}】")
}