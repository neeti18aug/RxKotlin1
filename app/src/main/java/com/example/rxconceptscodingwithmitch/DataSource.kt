package com.example.rxconceptscodingwithmitch

object DataSource {

    fun getTaskList(): List<Task> {
        val task1 = Task("Take out the Trash", true, 3)
        val task2 = Task("Walk the Dog", false, 9)
        val task3 = Task("Make Dinner", true, 3)
        val task4 = Task("Make my Bed", true, 4)
        return listOf(task1, task2, task3, task4)
    }

    fun myTask(): Array<MyTask> {
        val task1 = MyTask("Good morning", true, 3)
        val task2 = MyTask("Good noon", false, 9)
        val task3 = MyTask("Good Evening", true, 3)
        val task4 = MyTask("Good night", true, 4)
        return arrayOf(task1, task2, task3, task4)
    }

}
