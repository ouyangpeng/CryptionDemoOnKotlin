package com.oyp.crypt.rsa

import com.oyp.crypt.encode.Base64Util
import java.security.KeyPairGenerator
import java.security.PrivateKey
import java.security.PublicKey
import javax.crypto.Cipher

object RSACrypt {
    private val TRANSFORMATION = "RSA"
    val CRYPT_ALGORITHM = "RSA"

    private val cipher = Cipher.getInstance(TRANSFORMATION)

    fun encryptByPrivateKey(originContent: String, privateKey: PrivateKey): String {
        // 初始化cipher
        cipher.init(Cipher.ENCRYPT_MODE, privateKey)
        // 3、Base64编码 加密后的内容
        return Base64Util.encode(cipher.doFinal(originContent.toByteArray()))
    }

    fun encryptByPublicKey(originContent: String, publicKey: PublicKey): String {
        // 初始化cipher
        cipher.init(Cipher.ENCRYPT_MODE, publicKey)
        // 3、Base64编码 加密后的内容
        return Base64Util.encode(cipher.doFinal(originContent.toByteArray()))
    }
}

fun main(args: Array<String>) {
    //秘钥对生成器
    val pairGenerator = KeyPairGenerator.getInstance(RSACrypt.CRYPT_ALGORITHM)
    // 秘钥对
    val keyPair = pairGenerator.genKeyPair();
    val publicKey = keyPair.public
    val privateKey = keyPair.private

    println("publicKey = ${Base64Util.encode(publicKey.encoded)} ")
    println("privateKey = ${Base64Util.encode(privateKey.encoded)}")


    // 原文
    val originContent = "欧阳鹏的博客：http://blog.csdn.net/ouyang_peng"

    // 秘文
    val rsaPrivateKeyEncryptContent = RSACrypt.encryptByPrivateKey(originContent, privateKey)
    println("原文为：【${originContent}】 \nRSA私钥加密后的内容为：【${rsaPrivateKeyEncryptContent}】")

    // 秘文
    val rsaPublicKeyEncryptContent = RSACrypt.encryptByPublicKey(originContent, publicKey)
    println("原文为：【${originContent}】 \nRSA公钥加密后的内容为：【${rsaPublicKeyEncryptContent}】")
}