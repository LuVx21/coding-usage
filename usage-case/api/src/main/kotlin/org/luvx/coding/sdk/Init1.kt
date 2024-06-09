package org.luvx.coding.sdk

import io.github.oshai.kotlinlogging.KotlinLogging

private val log = KotlinLogging.logger {}

class Init1 {
    open class Foo {
        var i = 1

        init {
            log.info { "1:$i" }
            // 调用子类的方法, 子类未初始化, 因此返回0
            val x = getValue()
            log.info { "2:$x" }
        }

        init {
            i = 2
        }

        open fun getValue(): Int {
            log.info("info1:{}", javaClass.simpleName)
            return i
        }
    }

    class Bar : Foo() {
        var j = 1

        init {
            j = 4
        }

        init {
            j = 3
        }

        override fun getValue(): Int {
            log.info("info2:{}", javaClass.simpleName)
            return j
        }
    }
}

fun main() {
    val bar = Init1.Bar()
    // 执行结果: 1 0 3
    log.info("3:{}", bar.getValue())
}