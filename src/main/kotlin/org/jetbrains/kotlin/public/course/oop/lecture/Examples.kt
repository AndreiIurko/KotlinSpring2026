package org.jetbrains.kotlin.public.course.oop.lecture

interface OperationCountObject {
    fun getCount(): Int
    
    val operationCountGetNumber: Int
}

// store the last value that satisfies check, drop the ones not satisfy check
// inheritance: I am using AbstractMutableList instead of implementing everything on my own
// abstraction: depending on context, we can view this as just "object with some operations count" or just "mutable list"
class CountedMutableList<T> : MutableList<T>, AbstractMutableList<T>(), OperationCountObject {
    
    // invariant: I want to control who can set this value
    // encapsulation: hide the internal collection from direct access
    private var _collection = mutableListOf<T>()
    private var _operationCount = 0
        get() {
            operationCountGetNumber++
            println("Count requested")
            return field
        }
        set(value) {
            println("Count set to $value")
            field = value
        }
    
    override var operationCountGetNumber: Int = 0
        private set
    
    override fun getCount(): Int {
        return _operationCount
    }
    
    override val size: Int
        get() = _collection.size
    
    override fun get(index: Int): T {
        _operationCount++
        return _collection[index]
    }

    override fun add(index: Int, element: T) {
        _operationCount++
        _collection.add(element)
    }

    override fun removeAt(index: Int): T {
       return _collection.removeAt(index)
    }

    override fun set(index: Int, element: T): T {
        _operationCount++
        return _collection.set(index, element)
    }
}

fun printMyListElements() {
    val elements = CountedMutableList<Int>()
    elements.addAll(listOf(1, 2, 3))
    // polymorphism - we can use our collection as MutableList interface
    printAllElements(elements)
}

fun <T>printAllElements(mutableList: MutableList<T>) {
    mutableList.forEach { println(it) }
}

abstract class StrangeExample(internalValue: Int) {
    protected abstract val myValue: Int
    open val myValue2: Int = 1
    private val myValue3: Int = 1
    // final by default
    val myValue4: Int = 1
    
    init {
        println("StrangeExample initialized with myValue: $myValue")
        println("StrangeExample initialized with myValue2: $myValue2")
        println("StrangeExample initialized with internalValue: $internalValue")
    }
}

class StrangeExampleChild : StrangeExample(10) {
    override val myValue: Int = 42
    override val myValue2: Int = 23
    // error 'myValue4' in 'StrangeExample' is final and cannot be overridden.
    //override val myValue4: Int = 23
    
    fun someFunction() {
        // error Cannot access 'val myValue3: Int': it is private in 'org.jetbrains.kotlin.public.course.oop.lecture.StrangeExample'
        //println("Here is myValue3: $myValue3")
        println("Here is myValue4: $myValue4")
    }
}

fun main() {
    val strangeExample = StrangeExampleChild()
    strangeExample.myValue2
    // error Cannot access 'val myValue: Int': it is protected in 'org.jetbrains.kotlin.public.course.oop.lecture.StrangeExampleChild'.
    //strangeExample.myValue
}