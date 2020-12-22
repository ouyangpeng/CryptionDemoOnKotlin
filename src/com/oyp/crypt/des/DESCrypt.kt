package com.oyp.crypt.des

import com.oyp.crypt.encode.Base64Util
import java.security.Key
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.DESKeySpec
import javax.crypto.spec.IvParameterSpec

object DESCrypt {
    //DES/CBC/NoPadding (56)
    //DES 秘钥长度是8位，但是前7位参与加密计算，最后一位作为校验码。每位8个字节 所以是  7*8=56

    //    算法/工作模式/填充模式
    //   DES/CBC/NoPadding (56)
    //   DES/CBC/PKCS5Padding(56)
    //   DES/ECB/NoPadding(56)
    //   DES/ECB/PKCS5Padding(56)
    private val TRANSFORMATION = "DES/CBC/PKCS5Padding"
    private val CRYPT_ALGORITHM = "DES"

    private val cipher = Cipher.getInstance(TRANSFORMATION)
    private val desSecretKeyFactory = SecretKeyFactory.getInstance(CRYPT_ALGORITHM)

    fun encrypt(originContent: String, desKey: String): String {
        initCipher(desKey, Cipher.ENCRYPT_MODE)
        // 3、Base64编码 加密后的内容
        return Base64Util.encode(cipher.doFinal(originContent.toByteArray()))
    }

    fun decrypt(encryptContent: String, desKey: String): String {
        initCipher(desKey, Cipher.DECRYPT_MODE)
        // 3、Base64解码 解密后的内容
        return String(cipher.doFinal(Base64Util.decode(encryptContent)))
    }

    private fun initCipher(desKey: String, mode: Int) {
        // 1、初始化DES相关的操作
        val key: Key? = initKey(desKey)
        // 2、初始化cipher对象(参数一：解密模式)
        if (TRANSFORMATION.contains("CBC")) {
            // CBC工作模式需要额外添加参数,否则报错：java.security.InvalidKeyException: Parameters missing
            val iv = IvParameterSpec(desKey.toByteArray())
            cipher.init(mode, key, iv)
        } else {
            cipher.init(mode, key)
        }
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
    val desEncryptContent = DESCrypt.encrypt(originContent, desKey)
    println("原文为：【${originContent}】 进行Des加密后的内容为：【${desEncryptContent}】")

    val desDecryptContent = DESCrypt.decrypt(desEncryptContent, desKey)
    println("秘文为：【${desEncryptContent}】 进行Des解密后的内容为：【${desDecryptContent}】")
}