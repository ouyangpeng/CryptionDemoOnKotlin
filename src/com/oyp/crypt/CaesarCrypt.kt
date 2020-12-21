package com.oyp.crypt

/**
 * 凯撒加密算法
 */
fun main(args: Array<String>) {
    // 移到字符

    // 原文
    val originContent = "I Love You"
    // 秘钥
    val key = 1
    // 加密算法
    val encryptAlgorithm = CaesarCrypt()
    // 加密后的内容
    val encryptContent = encryptAlgorithm.encrypt(originContent, key)
    println("${originContent} 凯撒加密之后的内容为：${encryptContent}")
    // 解密后的内容
    val deEncryptContent = encryptAlgorithm.deEncrypt(encryptContent, key)
    println("${encryptContent} 凯撒解密之后的内容为：${deEncryptContent}")
}


class CaesarCrypt {
    /**
     * @param originContent 待加密的原文
     * @param key 秘钥
     *
     * @return 加密后的内容
     */
    fun encrypt(originContent: String, key: Int): String {
        // 获取字符ASCII编码
        val charArray = originContent.toCharArray()
        return with(StringBuilder()) {
            charArray.forEach {
                // 遍历每一个字符，对ASCII码进行偏移操作
                val c: Char = it
                // 字符转换成十进制,获取ASCII的值
                var ascii: Int = c.toInt()
                // 移动
                ascii += key
                // 转成字符
                val result = ascii.toChar()
                append(result)
            }
            // 返回结果
            toString()
        }
    }

    /**
     * @param encryptContent 要解密的秘文
     * @param key 秘钥
     *
     * @return 解密后的内容
     */
    fun deEncrypt(encryptContent: String, key: Int): String {
        // 获取字符ASCII编码
        val charArray = encryptContent.toCharArray()
        return with(StringBuilder()) {
            charArray.forEach {
                // 遍历每一个字符，对ASCII码进行偏移操作
                val c: Char = it
                // 字符转换成十进制,获取ASCII的值
                var ascii: Int = c.toInt()
                // 和加密的方向相反移动
                ascii -= key
                // 转成字符
                val result = ascii.toChar()
                append(result)
            }
            // 返回结果
            toString()
        }
    }
}