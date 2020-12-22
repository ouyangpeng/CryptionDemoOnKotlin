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
     * 加密的时候  不能大于117个字节，否则会报异常：javax.crypto.IllegalBlockSizeException: Data must not be longer than 117 bytes
     */
    val ENCRYPT_MAX_SIZE = 117;

    /**
     * 解密的时候  不能大于128个字节，否则会报异常：javax.crypto.IllegalBlockSizeException: Data must not be longer than 128 bytes
     */
    val DECRYPT_MAX_SIZE = 128;

    /**
     * 使用私钥解密
     * @param encryptContent 要解密的内容
     * @param key privateKey：RSA私钥
     */
    fun decryptByPrivateKey(encryptContent: String, privateKey: PrivateKey): String {
        return segmentedDecrypt(encryptContent, privateKey)
    }

    /**
     * 使用公钥解密
     * @param encryptContent 要解密的内容
     * @param key publicKey：RSA公钥
     */
    fun decryptByPublicKey(encryptContent: String, publicKey: PublicKey): String {
        return segmentedDecrypt(encryptContent, publicKey)
    }

    /**
     * 使用私钥加密
     * @param originContent 要加密的内容
     * @param publicKey 秘钥：RSA私钥
     */
    fun encryptByPrivateKey(originContent: String, privateKey: PrivateKey): String {
        return segmentedEncrypt(originContent, privateKey)
    }

    /**
     * 使用公钥加密
     * @param originContent 要加密的内容
     * @param publicKey 秘钥：RSA公钥
     */
    fun encryptByPublicKey(originContent: String, publicKey: PublicKey): String {
        return segmentedEncrypt(originContent, publicKey)
    }

    /**
     * 分段加密
     * @param originContent 要加密的内容
     * @param key 秘钥：RSA公钥或者RSA私钥
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

    /**
     * 分段解密
     * @param encryptContent 要解密的内容
     * @param key 秘钥：RSA公钥或者RSA私钥
     */
    private fun segmentedDecrypt(encryptContent: String, key: Key): String {
        // 初始化cipher
        cipher.init(Cipher.DECRYPT_MODE, key)
        // 使用Base64解编码
        val byteArray = Base64Util.decode(encryptContent)

        //分段解密
        var temp: ByteArray? = null
        // 当前位置偏移量
        var offset = 0
        // 输出流
        val byteArrayOutputStream = ByteArrayOutputStream()
        // 如果没有解密完，则一直解密
        while (byteArray.size - offset > 0) {
            // 每次解密的时候  不能大于128个字节，否则会报异常：javax.crypto.IllegalBlockSizeException: Data must not be longer than 128 bytes
            if (byteArray.size - offset > DECRYPT_MAX_SIZE) {
                // 剩余部分大于128个字节
                // 解密完整的128个字节
                temp = cipher.doFinal(byteArray, offset, DECRYPT_MAX_SIZE)
                // 重新计算偏移的位置
                offset += DECRYPT_MAX_SIZE
            } else {
                // 计算最后一块不足128个字节的那块
                temp = cipher.doFinal(byteArray, offset, byteArray.size - offset)
                // 重新计算偏移的位置
                offset = byteArray.size
            }
            // 存储到临时缓冲区
            byteArrayOutputStream.write(temp)
        }
        //关闭输出流
        byteArrayOutputStream.close()
        // 3、解密后的内容
        return String(byteArrayOutputStream.toByteArray())
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

    println("\n==============================RSA私钥加密======================================================")
    // 秘文
    val rsaPrivateKeyEncryptContent = RSACrypt.encryptByPrivateKey(originContent, privateKey)
    println("\n原文为：【${originContent}】 \nRSA私钥加密后的内容为：【${rsaPrivateKeyEncryptContent}】")


    println("\n==============================RSA公钥加密======================================================")

    // 解密  私钥加密后的秘文
    val rsaPublicKeyDecryptContent = RSACrypt.decryptByPublicKey(rsaPrivateKeyEncryptContent, publicKey)
    println("\nRSA私钥加密后的内容为：【${rsaPrivateKeyEncryptContent}】\n RSA公钥解密后的内容为 ${rsaPublicKeyDecryptContent}")

    println("\n*************************************************************************************************")
    println("*************************************************************************************************")
    println("*************************************************************************************************")
    println("*************************************************************************************************")

    println("\n==============================RSA公钥加密======================================================")

    // 秘文
    val rsaPublicKeyEncryptContent = RSACrypt.encryptByPublicKey(originContent, publicKey)
    println("\n原文为：【${originContent}】 \nRSA公钥加密后的内容为：【${rsaPublicKeyEncryptContent}】")

    println("\n==============================RSA私钥加密======================================================")

    // 解密  公钥加密后的秘文
    val rsaPrivateKeyDecryptContent = RSACrypt.decryptByPrivateKey(rsaPublicKeyEncryptContent, privateKey)
    println("\nRSA公钥加密后的内容为：【${rsaPublicKeyEncryptContent}】\n RSA私钥解密后的内容为 ${rsaPrivateKeyDecryptContent}")


}