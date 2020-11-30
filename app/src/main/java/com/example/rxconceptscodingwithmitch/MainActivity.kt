package com.example.rxconceptscodingwithmitch

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.rxconceptscodingwithmitch.DataSource.getTaskList
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers


class MainActivity : AppCompatActivity() {

    private lateinit var disposable: CompositeDisposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        disposable = CompositeDisposable()
        createOperator()
        createOperatorList()
        fromIterable()
        justOperator()
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

            override fun onNext(t: String?) {

            }

            override fun onError(e: Throwable?) {

            }

            override fun onComplete() {

            }

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
            override fun onSubscribe(d: Disposable?) {

            }

            override fun onNext(t: Task?) {

            }

            override fun onError(e: Throwable?) {

            }

            override fun onComplete() {

            }

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

    private fun fromIterable() {

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