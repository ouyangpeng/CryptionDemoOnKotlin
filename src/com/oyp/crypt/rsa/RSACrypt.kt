package com.oyp.crypt.rsa

import com.oyp.crypt.encode.Base64Util
import java.io.ByteArrayOutputStream
import java.security.Key
import java.security.KeyPairGenerator
import java.security.PrivateKey
import java.security.PublicKey
import javax.crypto.Cipher

object RSACrypt {
    /**
     * 算法
     */
    val CRYPT_ALGORITHM = "RSA"
    /**
     * Cipher对象
     */
    val cipher = Cipher.getInstance(CRYPT_ALGORITHM)

    /**
     * 不能大于117个字节，否则会报异常：javax.crypto.IllegalBlockSizeException: Data must not be longer than 117 bytes
     */
    val ENCRYPT_MAX_SIZE = 117;

    /**
     * 使用私钥加密
     */
    fun encryptByPrivateKey(originContent: String, privateKey: PrivateKey): String {
        return segmentedEncrypt(originContent, privateKey)
    }

    /**
     * 使用公钥加密
     */
    fun encryptByPublicKey(originContent: String, publicKey: PublicKey): String {
        return segmentedEncrypt(originContent, publicKey)
    }

    /**
     * 分段加密
     */
    private fun segmentedEncrypt(originContent: String, key: Key): String {

        // 初始化cipher
        cipher.init(Cipher.ENCRYPT_MODE, key)
        // 分段加密
        val byteArray = originContent.toByteArray()
        var temp: ByteArray? = null
        // 当前位置偏移量
        var offset = 0
        // 输出流
        val byteArrayOutputStream = ByteArrayOutputStream()
        // 如果没有加密完，则一直加密
        while (byteArray.size - offset > 0) {
            // 每次加密不能大于117个字节，否则会报异常：javax.crypto.IllegalBlockSizeException: Data must not be longer than 117 bytes
            if (byteArray.size - offset > ENCRYPT_MAX_SIZE) {
                // 剩余部分大于117个字节
                // 加密完整的117个字节
                temp = cipher.doFinal(byteArray, offset, ENCRYPT_MAX_SIZE)
                // 重新计算偏移的位置
                offset += ENCRYPT_MAX_SIZE
            } else {
                // 计算最后一块不足117个字节的那块
                temp = cipher.doFinal(byteArray, offset, byteArray.size - offset)
                // 重新计算偏移的位置
                offset = byteArray.size
            }
            // 存储到临时缓冲区
            byteArrayOutputStream.write(temp)
        }
        // 关闭输出流
        byteArrayOutputStream.close()
        // 3、Base64编码 加密后的内容
        return Base64Util.encode(byteArrayOutputStream.toByteArray())
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
    val originContent = """欧阳鹏的博客：http://blog.csdn.net/ouyang_peng 欢迎大家一起来浏览，
欧阳鹏的博客：http://blog.csdn.net/ouyang_peng欢迎大家一起来浏览，
欧阳鹏的博客：http://blog.csdn.net/ouyang_peng 欢迎大家一起来浏览,
欧阳鹏的博客：http://blog.csdn.net/ouyang_peng 欢迎大家一起来浏览"""

    // 秘文
    val rsaPrivateKeyEncryptContent = RSACrypt.encryptByPrivateKey(originContent, privateKey)
    println("原文为：【${originContent}】 \nRSA私钥加密后的内容为：【${rsaPrivateKeyEncryptContent}】")

    // 秘文
    val rsaPublicKeyEncryptContent = RSACrypt.encryptByPublicKey(originContent, publicKey)
    println("原文为：【${originContent}】 \nRSA公钥加密后的内容为：【${rsaPublicKeyEncryptContent}】")
}