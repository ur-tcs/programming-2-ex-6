# Polymorphism

Welcome to exercise 6 of programming II!

The exercise set is intended to help you practice lists and polymorphism.

Exercises marked with ⭐️ are the most important ones. Exercises marked with 🔥 are the most challenging ones. You do not need to complete all exercises to succeed in this class, and you do not need to do all exercises in order.

## Warm-up: Polymorphic Lists ⭐️

In previous exercises and labs, we used IntList for lists which elements are integers. This week, we’ll move to polymorphic lists.

Reminder: Algebraic Data Types

In week 3, we learned that algebraic data types can be created with the `enum` construct. Check the previous lectures or [this](https://docs.scala-lang.org/scala3/book/types-adts-gadts.html) for more details.

Polymorphic lists can be defined as an algebraic data type in the following way:
```scala
enum MyList[+A]:
  case Nil
  case Cons(x: A, xs: MyList[A])
```

**Covariance**

The `+` before `A` indicates that `List` is covariant in `A`. Check [this](https://docs.scala-lang.org/tour/variances.html) for more details, or ignore it for now — we will cover it later!

We’ll use the above `MyList` type in this exercise.

**Check Yourself**

How would you define the `isEmpty`, `head`, and `tail` methods on such polymorphic lists?

<details>
<summary> Solution</summary>

```scala
def isEmpty: Boolean = this match
  case Nil => true
  case _   => false

def head: A = this match
  case Nil        => throw EmptyListException()
  case Cons(x, _) => x

def tail: MyList[A] = this match
  case Nil         => throw EmptyListException()
  case Cons(_, xs) => xs
```
</details>