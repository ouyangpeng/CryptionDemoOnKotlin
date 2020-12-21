package com.oyp.crypt.caesar

/**
 * 凯撒加密算法
 */
fun main(args: Array<String>) {
    // 原文
    val originContent = "I Love You"
    // 秘钥
    val key = 1
    // 加密后的内容
    val encryptContent = CaesarCrypt.encrypt(originContent, key)
    println("${originContent} 凯撒加密之后的内容为：${encryptContent}")
    // 解密后的内容
    val deEncryptContent = CaesarCrypt.decrypt(encryptContent, key)
    println("${encryptContent} 凯撒解密之后的内容为：${deEncryptContent}")
}


object CaesarCrypt {
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
    fun decrypt(encryptContent: String, key: Int): String {
        // 只需要将秘钥变成对应的负数即可
        return encrypt(encryptContent, -key)
    }
}