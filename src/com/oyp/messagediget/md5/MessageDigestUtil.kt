package com.oyp.messagediget.md5

import java.lang.StringBuilder
import java.security.MessageDigest

/**
 * 消息摘要后的结果是固定长度，无论你的数据有多大，即使一个G的文件，摘要结果都是固定长度
 */
object MessageDigestUtil {
    /**
     * MD5信息摘要
     * @param input 要进行MD5信息摘要的内容
     */
    fun md5(input: String): String {
        val messageDigest = MessageDigest.getInstance("MD5")
        // 信息摘要后16个字节
        val result = messageDigest.digest(input.toByteArray())
        println("md5信息摘要后的后长度： ${result.size}")
        // 信息摘要后的转成16进制则是32个字节
        return covertToHex(result)
    }
    /**
     * SHA1信息摘要
     * @param input 要进行SHA1信息摘要的内容
     */
    fun sha1(input: String): String {
        val messageDigest = MessageDigest.getInstance("SHA-1")
        // 信息摘要后20个字节
        val result = messageDigest.digest(input.toByteArray())
        println("sha1信息摘要后的后长度： ${result.size}")
        return covertToHex(result)
    }

    /**
     * 转换成16进制
     */
    private fun covertToHex(result: ByteArray): String {
        return with(StringBuilder()) {
            // 转换成16进制
            result.forEach {
                val hex = it.toInt() and (0xFF)
                val hexString = Integer.toHexString(hex)
                if (hexString.length == 1) {
                    this.append("0").append(hexString)
                } else {
                    this.append(hexString)
                }
            }
            this.toString()
        }
    }
}

fun main(args: Array<String>) {
    val input = "欧阳鹏的博客：http://blog.csdn.net/ouyang_peng 欢迎大家一起来浏览"
    val md5 = MessageDigestUtil.md5(input)
    println("md5信息摘要后的值为:${md5}")
    println("md5加密后转成16进制的长度为为:${md5.toByteArray().size}")

    println("=================================================================")

    val sha1 = MessageDigestUtil.sha1((input))
    println("sha1信息摘要后的值为:${sha1}")
    println("sha1信息摘要后转成16进制的长度为为:${sha1.toByteArray().size}")
}