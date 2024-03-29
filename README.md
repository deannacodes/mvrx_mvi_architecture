# MVI Architecture Implementation with MvRx
An implementation of an Mvi architecture design on an Android app using Kotlin, MvRx, Epoxy, RxJava, Dagger 2 and AssistedInject. 

It works by using the MvRx view to send intents to the ViewModel via an RxJava Relay stream. Then the ViewModel processes the intent, which will update the state. In MvRx, this triggers the `invalidate()` function in the view, which redraws it whenever the state is updated.

With MvRx, we can eliminate a lot of boilerplate code and can have a very easy-to-follow, unidirectional flow of data.

# Read More
You can read an in depth explanation of this project [here](http://deanna.codes/2019/08/27/implementing-the-mvi-design-pattern-with-mvrx/).
