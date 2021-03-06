package com.example.rxconceptscodingwithmitch

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.rxconceptscodingwithmitch.DataSource.getTaskList
import com.example.rxconceptscodingwithmitch.DataSource.myTask
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.annotations.NonNull
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.functions.Function
import io.reactivex.rxjava3.functions.Predicate
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit


@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    private lateinit var disposable: CompositeDisposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        disposable = CompositeDisposable()
        createOperator()
        createOperatorList()
        fromIterableOperator()
        justOperator()
        rangeAndRepeatOperator()
        timerOperator()
        intervalTimerOperator()
        fromArrayOperator()
        fromCallableOperator()
        filterOperator()
        distinctOperator()
        takeOperator()
        takeWhileOperator()
        mapOperator()
        bufferOperator()
    }

    private fun bufferOperator() {
        val bufferObservable: @NonNull Observable<MutableList<Task>>? =
            Observable.fromIterable(getTaskList())
                .buffer(3)
        bufferObservable?.subscribe(object : Observer<MutableList<Task>> {
            override fun onSubscribe(d: Disposable?) {}
            override fun onNext(t: MutableList<Task>?) {
                println("check buffer")
                if (t != null) {
                    for (task in t) {
                        println("buffer:$task")
                    }
                }
            }

            override fun onError(e: Throwable?) {}
            override fun onComplete() {

            }
        })

    }

    private fun mapOperator() {
        val mapObservable: @NonNull Observable<String>? = Observable.fromIterable(getTaskList())
            .subscribeOn(Schedulers.io())
            .map { t -> t.description }
        mapObservable?.subscribe(object : Observer<String> {
            override fun onSubscribe(d: Disposable?) {}
            override fun onNext(t: String?) {
                println("onNext: $t")
            }

            override fun onError(e: Throwable?) {}
            override fun onComplete() {}
        })
    }

    private fun takeWhileOperator() {
        val takeWhileObservable: Observable<Task> = Observable.fromIterable(getTaskList())
            .takeWhile(object : Predicate<Task> {
                override fun test(t: Task?): Boolean {
                    if (t != null) {
                        return t.isComplete
                    }
                    return false
                }
            })
        takeWhileObservable.subscribe(object : Observer<Task> {
            override fun onSubscribe(d: Disposable?) {}
            override fun onNext(t: Task?) {
                println(t?.isComplete)
            }

            override fun onError(e: Throwable?) {}
            override fun onComplete() {}
        })
    }

    private fun takeOperator() {
        val takeObservable: Observable<Task> = Observable.fromIterable(getTaskList())
            .take(2)
        takeObservable.subscribe(object : Observer<Task> {
            override fun onSubscribe(d: Disposable?) {}
            override fun onNext(t: Task?) {
                println(t?.description)
            }

            override fun onError(e: Throwable?) {}
            override fun onComplete() {
            }
        })
    }

    private fun distinctOperator() {
        val distinctObservable: Observable<Task> = Observable.fromIterable(getTaskList())
            .distinct(object : Function<Task, Boolean> {
                override fun apply(t: Task?): Boolean {
                    if (t != null) {
                        return t.isComplete
                    }
                    return false
                }
            })
        distinctObservable.subscribe(object : Observer<Task> {
            override fun onSubscribe(d: Disposable?) {}
            override fun onNext(t: Task?) {
                println(t?.isComplete)
            }

            override fun onError(e: Throwable?) {}
            override fun onComplete() {}
        })
    }

    private fun filterOperator() {
        val observableFilter: Observable<Task> = Observable.fromIterable(getTaskList())
            .filter(object : Predicate<Task> {
                override fun test(t: Task?): Boolean {
                    if (t?.description == "Walk the Dog") {
                        return true
                    }
                    return false
                }
            })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
        observableFilter.subscribe(object : Observer<Task> {
            override fun onSubscribe(d: Disposable?) {}
            override fun onNext(t: Task?) {
                if (t != null) {
                    println(t.description)
                }
            }

            override fun onError(e: Throwable?) {}
            override fun onComplete() {}
        })
    }

    private fun fromCallableOperator() {
        val callable: Observable<Array<MyTask>> =
            Observable.fromCallable { myTask() }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        callable.subscribe(object : Observer<Array<MyTask>> {
            override fun onSubscribe(d: Disposable?) {}
            override fun onNext(t: Array<MyTask>?) {
                println("callable" + (t?.get(2) ?: "hello"))
            }

            override fun onError(e: Throwable?) {}
            override fun onComplete() {}
        })
    }

    private fun fromArrayOperator() {

        val myList = arrayOf("Take out the Trash", "true", "3")
        val observableArray: Observable<Any> = Observable.fromArray(myList)
        observableArray.subscribe(object : Observer<Any> {
            override fun onSubscribe(d: Disposable?) {}
            override fun onNext(t: Any?) {
                println("fromArrayOperator${t.toString()}")
            }

            override fun onError(e: Throwable?) {}
            override fun onComplete() {}
        })
    }

    private fun intervalTimerOperator() {
        val observableInterval: Observable<Long> = Observable
            //.interval(5000, TimeUnit.MILLISECONDS)
            .timer(3, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
        observableInterval.subscribe(object : Observer<Long> {
            override fun onSubscribe(d: Disposable?) {}
            override fun onNext(t: Long?) {
                println("intervalOperator onNext: $t")
            }

            override fun onError(e: Throwable?) {}
            override fun onComplete() {}
        })
    }

    private fun timerOperator() {
        val updateHandler = Handler()
        val runnable = Runnable {
            println("execute this task after time set up")
        }
        updateHandler.postDelayed(runnable, 5000)
    }

    private fun rangeAndRepeatOperator() {
        val observableRange: Observable<Int> = Observable
            .range(1, 5)
            .repeat(2)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
        observableRange.subscribe(object : Observer<Int> {
            override fun onSubscribe(d: Disposable?) {}
            override fun onNext(t: Int?) {
                println("onNext: $t")
            }

            override fun onError(e: Throwable?) {}
            override fun onComplete() {}
        })
    }

    private fun justOperator() {
        val observableJust: Observable<String> = Observable.just(
            "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "10"
        )
        observableJust.subscribe(object : Observer<String> {
            override fun onSubscribe(d: Disposable?) {
                disposable.add(d)
            }

            override fun onNext(t: String?) {}
            override fun onError(e: Throwable?) {}
            override fun onComplete() {}
        })
    }

    private fun createOperatorList() {
        val observableListCreate: Observable<Task> =
            Observable.create {
                for (task in getTaskList()) {
                    println("observable created:$task")
                }
            }
        observableListCreate.subscribe(object : Observer<Task> {
            override fun onSubscribe(d: Disposable?) {}
            override fun onNext(t: Task?) {}
            override fun onError(e: Throwable?) {}
            override fun onComplete() {}
        })
    }

    private fun createOperator() {
        val observableCreate: Observable<Employee> = Observable.create { emitter ->
            if (!emitter.isDisposed) {
                emitter?.onNext(
                    Employee("Neeti")
                )
                emitter.onComplete()
            }
        }
        observableCreate.subscribe(object : Observer<Employee> {
            override fun onSubscribe(d: Disposable?) {
                disposable.add(d)
            }

            override fun onNext(t: Employee?) {
                println(t?.name)
            }

            override fun onError(e: Throwable?) {
                if (e != null) {
                    println(e.printStackTrace())
                }
            }

            override fun onComplete() {
                println("onCompleted")
            }
        })
    }

    private fun fromIterableOperator() {

        val observable: Observable<Task> = Observable
            .fromIterable(getTaskList())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

        observable.subscribe(object : Observer<Task> {
            override fun onComplete() {
                println("onCompleted")
            }

            override fun onSubscribe(d: Disposable) {
                disposable.add(d)
            }

            override fun onNext(t: Task) {
                println("onCompleted" + t.description)

            }

            override fun onError(e: Throwable) {
                println("onCompleted" + e.message)
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.clear()
    }
}

data class Employee(val name: String)