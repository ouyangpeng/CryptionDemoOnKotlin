package com.oyp.messagediget.md5

import java.lang.StringBuilder
import java.security.MessageDigest

/**
 * 消息摘要后的结果是固定长度，无论你的数据有多大，即使一个G的文件，摘要结果都是固定长度
 */
object Md5Util {
    fun md5(input: String): String {
        val messageDigest = MessageDigest.getInstance("MD5")
        // 信息摘要后16个字节
        val result = messageDigest.digest(input.toByteArray())
        println("md5信息摘要后的后长度： ${result.size}")
        val stringBuilder = StringBuilder()
        // 转换成16进制
        result.forEach {
            val hex = it.toInt() and (0xFF)
            val hexString = Integer.toHexString(hex)
            if (hexString.length == 1) {
                stringBuilder.append("0").append(hexString)
            } else {
                stringBuilder.append(hexString)
            }
        }
        // 信息摘要后的转成16进制则是32个字节
        return stringBuilder.toString()
    }
}

fun main(args: Array<String>) {
    val input = "欧阳鹏的博客：http://blog.csdn.net/ouyang_peng 欢迎大家一起来浏览"
    val md5 = Md5Util.md5(input)
    println("md5信息摘要后的值为:${md5}")
    println("md5加密后转成16进制的长度为为:${md5.toByteArray().size}")
}