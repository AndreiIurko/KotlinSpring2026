package org.jetbrains.kotlin.public.course.generics.practice.answer.projections.task1

/* Projections #1

Implement a system of news agencies with the following rules:
 - There exists only regular and elite agencies
 - Regular can accept any scoop, elite - only elite scoops
 - Elite reporter can deliver only elite scoops, but to any agency
 - Regular reporter can deliver any scoop, but only to regular agencies
**/

interface NewsAccepter<in T: Scoop> {
    fun deliverScoop(item: T)
}

class NewsAgency<in T: Scoop>: NewsAccepter<T> {
    private var scoops: MutableList<Any> = mutableListOf()

    override fun deliverScoop(item: T) {
        scoops.add(item)
    }
}

class Reporter<T: Scoop>(private val agencies: List<NewsAccepter<T>>): NewsAccepter<T> {
    override fun deliverScoop(item: T) {
        agencies.forEach { it.deliverScoop(item) }
    }
}

interface Scoop

open class Normal(open val info: String) : Scoop

data class Sensational(val reward: Int, override val info: String) : Normal(info)

fun main() {
    val sensational = Sensational(150, "Students from NUP won ICPC")
    val normal = Normal("Students from NUP has Kotlin lecture")
    // accepts any scoop
    val regularAgency = NewsAgency<Normal>()
    regularAgency.deliverScoop(normal)
    regularAgency.deliverScoop(sensational)
    // accepts only "elite" scoops
    val eliteAgency = NewsAgency<Sensational>()
    eliteAgency.deliverScoop(sensational)
    // eliteAgency.deliverScoop(normal) // should rise a compilation error
    

    // works by submitting elite scoops to everyone and making a lot of many
    val eliteReporter = Reporter(listOf(regularAgency, eliteAgency))
    eliteReporter.deliverScoop(sensational)
    // eliteReporter.deliverScoop(normal) // should return a compilation error

    // works by submitting normal scoops mostly and sometimes elite ones (but only regular agencies are ready to work with them)
    val regularReporter = Reporter(listOf(regularAgency))
    regularReporter.deliverScoop(sensational)
    regularReporter.deliverScoop(normal)
}