package com.oyp.crypt.signature

import com.oyp.crypt.encode.Base64Util
import com.oyp.crypt.rsa.RSACryptUtil
import java.security.PrivateKey
import java.security.PublicKey
import java.security.Signature

/**
 * 算法： SHA256withRSA   消息摘要+非对称加密
 * 特点：
 *     私钥签名
 *     公钥校验
 *
 *  应用场景： 校验数据完整性
 */
object SignatureUtil {
    /**
     * 签名
     * @param privateKey 数字签名要使用的RSA私钥
     * @param input 要使用数字签名加密的内容
     */
    fun sign(input: String, privateKey: PrivateKey): String {
        // 获取数字签名实例对象
        val signature = Signature.getInstance("SHA256withRSA")
        // 初始化签名，如果不调用会报错：java.security.SignatureException: object not initialized for signing
        // 签名要用私钥加密
        signature.initSign(privateKey)
        // 设置数据源
        signature.update(input.toByteArray())
        //签名
        val sign = signature.sign()
        return Base64Util.encode(sign)
    }

    /**
     * 校验签名
     * @param input 要使用数字签名加密的内容
     * @param publicKey 数字签名校验要使用的RSA公钥
     * @param sign 要校验的签名
     */
    fun verify(input: String, publicKey: PublicKey, sign: String): Boolean {
        // 获取数字签名实例对象
        val signature = Signature.getInstance("SHA256withRSA")
        // 初始化签名  签名要用公钥校验
        signature.initVerify(publicKey)
        signature.update(input.toByteArray())
        // 校验签名信息
        return signature.verify(Base64Util.decode(sign))
    }
}

fun main(args: Array<String>) {
    println("===========================签名===================================")
    val input = "欧阳鹏的博客：http://blog.csdn.net/ouyang_peng 欢迎大家一起来浏览"
    // 获取RSA私钥
    val privateKey = RSACryptUtil.getPrivateKey()
    val sign = SignatureUtil.sign(input, privateKey)
    println("数字签名内容为：${sign}")

    println("===========================校验===================================")
    // 获取RSA公钥
    val publicKey = RSACryptUtil.getPublicKey()
    val verify = SignatureUtil.verify(input, publicKey, sign)
    println("数字签名 校验内容结果为：${verify}")

    val verify2 = SignatureUtil.verify(input + "瞎写的篡改的内容", publicKey, sign)
    println("数字签名 校验内容结果为：${verify2}")
}



