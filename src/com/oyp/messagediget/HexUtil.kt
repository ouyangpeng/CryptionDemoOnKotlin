package com.oyp.messagediget

import java.lang.StringBuilder

object HexUtil {
    /**
     * 转换成16进制
     */
    fun covertToHex(result: ByteArray): String {
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
            // 信息摘要后的转成16进制则是32个字节
            this.toString()
        }
    }
}
