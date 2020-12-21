package com.oyp.crypt.caesar

import java.io.IOException
import java.io.File
import java.io.FileReader
import java.util.*
import kotlin.collections.HashMap
import java.io.FileWriter

/**
 * 频度分析法破解凯撒加密算法
 */
class FrequencyAnalysis {
    /**一般来说英文里出现次数最多的字符*/
    private val MAGIC_CHAR = 'e'
    /** 破解生成的最大文件数 */
    private val DE_MAX_FILE = 5

    /**
     * 打印字符出现的次数
     * @param path 文件路径
     */
    fun printCharCount(path: String) {
        val data = fileToString(path)
        val mapList = getMaxCountChar(data)
        for (entry in mapList) {
            //输出前几位的统计信息
            println("字符【" + entry.key + "】出现" + entry.value + "次")
        }
    }

    /**
     * 读取文件内容，转换成String
     * @param path 文件路径
     */
    fun fileToString(path: String): String {
        val reader = FileReader(File(path))
        val buffer = CharArray(1024)
        var len = reader.read(buffer)
        val sb = StringBuffer()
        while (len != -1) {
            sb.append(buffer, 0, len)
            len = reader.read(buffer)
        }
        return sb.toString()
    }


    /**
     * 统计String里出现最多的字符
     * @param data 要统计的字符串
     */
    private fun getMaxCountChar(data: String): SortedMap<Char, Int> {
        // 定义一个map 来存储 每个字符 -> 该字符出现的次数
        val map = HashMap<Char, Int>()
        // 将String转换成Char数组
        val array = data.toCharArray()
        // 循环遍历 读取每个字符出现的次数
        for (c in array) {
            if (!map.containsKey(c)) {
                map.put(c, 1)
            } else {
                val count = map[c]
                map.put(c, count!! + 1)
            }
        }
        // 对map进行降序排序
        return map.toSortedMap(compareByDescending {
            map[it]
        })
    }


    /**
     * 使用凯撒加密算法加密文件
     * @param srcFile 要被加密的文件路径
     * @param destFile 加密后保存的文件路径
     * @param caesarKey 凯撒加密的秘文
     */
    fun encryptFile(srcFile: String, destFile: String, caesarKey: Int) {
        //读取文件内容，转换成String
        val article = fileToString(srcFile)
        //加密文件
        val encryptData = CaesarCrypt().encrypt(article, caesarKey)
        //保存加密后的文件
        stringToFile(encryptData, destFile)
    }

    /**
     * 将字符串保存为文件
     * @param data  要保存的字符串
     * @param path  保存的文件路径
     */
    private fun stringToFile(data: String, path: String) {
        var writer: FileWriter? = null
        try {
            writer = FileWriter(File(path))
            writer.write(data)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (writer != null) {
                try {
                    writer.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    /**
     * 破解凯撒密码
     * @param input 数据源
     * @param destPath 生成的备选文件路径
     * @return 返回解密后的数据
     */
    fun decryptCaesarCode(input: String, destPath: String) {
        //当前解密生成的备选文件数
        var deCount = 0
        //获取出现频率最高的字符信息（出现次数越多越靠前）
        val mapList = getMaxCountChar(input)
        // 从出现次数最高的字符串开始循环遍历
        for ((encryptChar, countNumber) in mapList) {
            //限制解密文件备选数
            if (deCount >= DE_MAX_FILE) {
                break
            }
            //输出前几位的统计信息
            println("字符【" + encryptChar + "】出现" + countNumber + "次")

            ++deCount

            // 我们假设原文中出现次数最高的字符为是 MAGIC_CHAR 变量，即字母e
            // 那么我们要解密的秘文中出现次数最高的字符为 encryptChar
            // 则 解密的秘文中出现次数最高的字符encryptChar跟我们假设原文中出现次数最高的字符为是 MAGIC_CHAR的偏移量即为秘钥
            val caesarKey = encryptChar - MAGIC_CHAR
            println("猜测出来的秘钥caesarKey = " + caesarKey + "， 解密生成第" + deCount + "个备选文件" + "\n")

            // 使用上面我们猜测出来的秘钥caesarKey来解密内容
            val decrypt = CaesarCrypt().deEncrypt(input, caesarKey)
            // 然后将解密后的内容写入到文件中
            val fileName = "de_" + deCount + destPath
            stringToFile(decrypt, fileName)
        }
    }


}

fun main(args: Array<String>) {
    println("====================打印出现次数最多的字符============================")
    //打印出现次数最多的字符
    FrequencyAnalysis().printCharCount("article.txt")

    println("====================加密============================")

    // 秘钥
    val caesarKey = 3
    // 加密文件
    FrequencyAnalysis().encryptFile("article.txt", "article_en.txt", caesarKey)
    // 统计密文出现次数最多的字符
    FrequencyAnalysis().printCharCount("article_en.txt")

    println("====================解密============================")

    //读取加密后的文件
    val artile = FrequencyAnalysis().fileToString("article_en.txt")
    //解密（会生成多个备选文件）   然后我们查看几个备选文件，看哪个是解密对的，那么凯撒加密的秘钥我们就破解了
    FrequencyAnalysis().decryptCaesarCode(artile, "article_de.txt");

}

