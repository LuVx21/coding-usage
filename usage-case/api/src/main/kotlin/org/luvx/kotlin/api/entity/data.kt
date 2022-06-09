package org.luvx.kotlin.api.entity

import org.luvx.enhancer.out

data class User1(
    var id: Long = 0L,
    var userName: String = "",
    var password: String = "",
    var age: Int = 0
) {
    constructor(name: String, pwd: String, age: Int) :
            this(0L, name, pwd, age)

    constructor(id: Long, age: Int) :
            this(id, "", "", age)
}

fun main() {
    val u = User1()
    u.apply { id = 1 }.apply { userName = "foo" }.apply { password = "bar" }.apply { age = 18 }
    u.out()
}