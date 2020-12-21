package com.oyp.crypt.caesar

import java.lang.StringBuilder

fun main(args: Array<String>) {
    // 获取字符ASCII编码
    val c: Char = 'a'
    // 字符转换成十进制
    val value: Int = c.toInt()

    println(value) // 97

    val str = "I Love You"
    val array = str.toCharArray()

//    val stringBuilder = StringBuilder()
//    for (c1 in array) {
//        val result: Int = c1.toInt()
//        stringBuilder.append(result.toString() + " ")
//    }
//    println(stringBuilder.toString())

    // 使用with操作
    val result = with(StringBuilder()) {
        for (c1 in array) {
            val result: Int = c1.toInt()
            append(result.toString() + " ")
        }
        // 返回结果
        toString()
    }
    println(result)

}