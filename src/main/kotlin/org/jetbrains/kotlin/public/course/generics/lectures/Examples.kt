package org.jetbrains.kotlin.public.course.generics.lectures

import java.util.Stack

fun main() {
    //example1()
    //example2()
    //example3()
    example4()
    //example5()
}

fun <T: Comparable<T>>compare(a: T, b: T): Boolean {
    return a > b
}

fun compareStrange(a: Any, b: Any): Boolean {
    // during compilation can't rise exception because it's perfectly fine to execute compare(Unit, Unit)
    if (a !is Comparable<*> || b !is Comparable<*>) {
        return false
    }
    // And think what this check should look like in case someone implements T: Comparable<V>
    if (a.javaClass != b.javaClass) return false
    return a as Comparable<Any> > b as Comparable<Any>
}

fun example1() {
    println(compare(2, 1))
    println(compareStrange(2, 1))
}

class Holder<T> {
    private val storage = mutableListOf<T>()

    fun add(el: T) = storage.add(el)
    operator fun get(index: Int) = storage[index]
}

class StrangeHolder {
    private val storage = mutableListOf<Any>()
    var type: Class<Any>? = null
        private set

    fun add(el: Any) {
        if (type == null) {
            type = el.javaClass
        } else {
            // again - no compilation check!
            if (type != el.javaClass) throw IllegalArgumentException("Cannot add element of different type to StrangeHolder")
        }
        storage.add(el)
    }

    operator fun get(index: Int) = storage[index]
}

fun example2() {
    val holder = Holder<String>()
    holder.add("HELLO")
    println("Here is my string value: ${holder[0].lowercase()}")

    val strangeHolder = StrangeHolder()
    strangeHolder.add("HELLO")
    val strangeValue = strangeHolder[0]
    // We need to actually cast value to get string back
    if (strangeHolder.type == String::class.java) {
        val castedValue = strangeValue as String
        println("Here is my strange value: ${castedValue.lowercase()}")
    }
}

fun example3() {
    val intList = mutableListOf<Int>()
    val intList2 = mutableListOf<Any?>()
    intList.add(1)
    intList2.add(2)
    // error: we can't use MutableList<Int> instead of MutableList<Any?>
    // reason: intList.add(Any) should work in case MutableList<Any?>, but not in case MutableList<Int>
    // opposite also incorrect: MutableList<Any?> can't be used instead of MutableList<Int> because get() would return Any? instead of Int
    // differenceBetweenStarAndAny(intList, intList2)
    
    // but we can use intList in <*>
    differenceBetweenStarAndAny(intList2, intList)
}

// Any? - programmer explicitly defined this class can work with any object
// * - programmer explicitly defined class, BUT WE DON'T KNOW IT
fun differenceBetweenStarAndAny(anyList: MutableList<Any?>, starList: MutableList<*>) {
    anyList.add("Haha")
    // Error: 
    //starList.add("Haha")
    
    // Both Any?
    val val1 = anyList.first()
    val val2 = starList.first()
    
    println("Are these ints? ${val1 as? Int},  ${val2 as? Int}")
}

interface Producer<out T> {
    fun produce(): T
}

interface Consumer<in T> {
    fun consume(el: T)
}

// It can do both!
class ProducerConsumerExample<T>: Producer<T>, Consumer<T> {
    private val holder = Stack<T>()
    
    override fun produce(): T = holder.pop()
    
    override fun consume(el: T) {
        holder.add(el)
    }
}

open class A {
    override fun toString(): String {
        return "A"
    }
}

open class B: A() {
    override fun toString(): String {
        return "${super.toString()}B"
    }
}

open class C: B() {
    override fun toString(): String {
        return "${super.toString()}C"
    }
}

fun example4() {
    val holderA = ProducerConsumerExample<A>()
    holderA.consume(A())
    val holderB = ProducerConsumerExample<B>()
    holderB.consume(B())
    val holderC = ProducerConsumerExample<C>()
    holderC.consume(C())
    // all three can consume type C
    val consumerList: List<Consumer<C>> = listOf(holderA, holderB, holderC)
    consumerList.forEach { it.consume(C()) }
    
    // all three can produce type A
    val producerList: List<Producer<A>> = listOf(holderA, holderB, holderC)
    producerList.forEach { println(it.produce()) }

    println("Second time")
    producerList.forEach { println(it.produce()) }
}

// expecting any list
fun <T: List<*>> example3(list: T) {
    // this check will fail
    // list is List<String>
}

// Error!
//fun <T>Any?.isClass(): Boolean = this is T

inline fun <reified T>Any?.isClass(): Boolean = this is T

fun example5() {
    val myValue = 1
    println(myValue.isClass<Long>())
    println(myValue.isClass<Int>())
}
