# Polymorphism

Welcome to exercise 6 of programming II!

The exercise set is intended to help you practice lists and polymorphism.

Exercises marked with ⭐️ are the most important ones. Exercises marked with 🔥 are the most challenging ones. You do not need to complete all exercises to succeed in this class, and you do not need to do all exercises in order.

**Your are allowed to copy/clone/fork this repository, but not to share solutions of the exercise in any public repository or web page.**

## Warm-up: Polymorphic Lists ⭐️

In previous exercises and labs, we used IntList for lists which elements are integers. This week, we’ll move to polymorphic lists.

<details><summary> Reminder: Algebraic Data Types </summary>

In week 3, we learned that algebraic data types can be created with the `enum` construct. Check the previous lectures or [this](https://docs.scala-lang.org/scala3/book/types-adts-gadts.html) for more details.

</details>

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

## Functions on Polymorphic Lists ⭐️

### Part 1: Higher-order functions ⭐️

In last week, we have implemented higher-order functions on IntLists. For example, `map` was defined as:

```scala
def map(f: Int => Int)(l: IntList): IntList =
  l match
    case IntNil         => IntNil
    case IntCons(x, xs) => IntCons(f(x), map(f)(xs))
```

In contrast, the function `map`, generalized to generic lists, has the following signature:

```scala
def map[A, B](f: A => B)(l: MyList[A]): MyList[B] =
  l match
    case Nil         => Nil
    case Cons(x, xs) => Cons(f(x), map(f)(xs))
```

1. Based on the example above, write a generic signature for `filter`, `foldRight`, `reduceRight`, `forall`, `exists`, `zip`, and `zipWith`.
    
<details>
<summary> Solution </summary>

```scala
    def map[A, B](l: MyList[A])(f: A => B): MyList[B] =
      ???

    def filter[A](l: MyList[A])(p: A => Boolean): MyList[A] =
      ???

    def foldRight[A, B](l: MyList[A])(f: (A, B) => B, base: B): B =
      ???

    def reduceRight[A](l: MyList[A])(f: (A, A) => A): A =
      ???

    def forall[A](l: MyList[A])(p: A => Boolean): Boolean =
      ???

    def exists[A](l: MyList[A])(p: A => Boolean): Boolean =
      ???

    def zip[A, B](l1: MyList[A], l2: MyList[B]): MyList[(A, B)] =
      ???

    def zipWith[A, B, C](l1: MyList[A], l2: MyList[B])(op: (A, B) => C): MyList[C] =
      ???
```

</details>

2. In previous exercises, we had separate implementations for `foldRight` and `foldRightList` (we had to handle the cases of returning an integer and returning an `IntList` separately).

Do we need to define a similar `foldRightList` on polymorphic lists?

<details> 
<summary> Solution </summary>

    No, type variable B can be instantized to MyList[Int].

</details>

3. Implement these eight higher-order functions (`map` plus all other ones above) on MyList using pattern matching.

Then, using these APIs of lists:

1. Implement function `elementsAsStrings` which converts every element of a list to a string (you may need the `.toString` function):

```scala
    def elementsAsStrings[A](l: MyList[A]): MyList[String] =
      ???
```

2. Reimplement functions from previous exercises on polymorphic lists:

```scala
    def length[A](l: MyList[A]): Int =
      ???

    def takeWhilePositive(l: MyList[Int]): MyList[Int] =
      ???

    def last[A](l: MyList[A]): A =
      ???
```

3. Adapt the string functions capitalizeString and wordCount to operate on lists of characters (you will need `toUpper`):

```scala
    def capitalizeString(l: MyList[Char]): MyList[Char] =
        ???

    def wordCount(l: MyList[Char]): Int =
        ???
```
    
**Beware:** the solution from exercise session 2 for wordCount doesn’t express itself naturally as a fold… 🔥 try to look for a different one!

**Strings and Lists**

Both `String` and `List[Char]` or `MyList[Char]` represent sequences of characters. However, it’s usually more efficient and convenient to use `String` for text processing and manipulation in Scala because `String` has optimized storage for texts and rich APIs tailored for text operations.

### Part 2: More functions: flatMap and cross-product

*flatMap*

You may have come across `flatMap`, a powerful higher-order function that can be used to transform and flatten container datatypes, such as lists.

```scala
def flatMap[A, B](l: MyList[A])(f: A => MyList[B]): MyList[B] =
  ???
```

The idea of `flatMap(f)(l)` is:

- Map: apply a function `f` to each element of the list `l`, where `f` returns a list;
- Flatten: concatenate all the resulting lists into a single list.

For example,

```scala
object FlatMapExamples:
  val numbers: MyList[Int] = Cons(2, Cons(3, Nil))

  val mapped = map(numbers)((n: Int) =>
    Cons(n, Cons(n * 2, Nil))
  )
  // For simplicity, we write Cons as `::` in the results.
  // Result: (2 :: 4 :: Nil) :: (3 :: 6 :: Nil)

  val flatMapped = flatMap(numbers)((n: Int) =>
    Cons(n, Cons(n * 2, Nil))
  )
  // Result: 2 :: 4 :: 3 :: 6 :: Nil
```

1. Implement `flatMap`. You may use the `append` function that we included in the starting code.

2. Implement `flatten` using `flatMap`. `flatten` takes a list of lists, and returns the concatenation of all the lists list:

```scala
    def flatten[A](l: MyList[MyList[A]]): MyList[A] =
      ???
```

*cross-product*

The cross-product function, often referred to as the Cartesian product, produces all possible pairs (combinations) of elements from two lists.

```scala
def crossProduct[A, B](l1: MyList[A], l2: MyList[B]): MyList[(A, B)] =
  ???
```

For example, given a list of main dishes and a list of side dishes, we can use `crossProduct` to generate all possible meal combinations:

```scala
object CrossProductExamples:
  val mains = Cons("burger", Cons("Pizza", Cons("Pasta", Nil)))
  val sides = Cons("Salad", Cons("Soup", Nil))

  val meals = crossProduct(mains, sides)
  // Result:
  // ("burger", "Salad") :: ("burger", "Soup") :: ("Pizza", "Salad") ::
  // ("Pizza", "Soup") :: ("Pasta", "Salad") :: ("Pasta", "Soup") :: Nil
```

- Implement `crossProduct` using `flatMap`.

*Triangles in DAG*

Consider a directed graph (DAG) given by its set of (directed) edges stored as a list of pairs of nodes:

```scala
type NodeId = Int
type DirectedEdge = (NodeId, NodeId)
type DirectedGraph = MyList[DirectedEdge]
type Triangle = (NodeId, NodeId, NodeId)
```

Define the triangles function that finds all cycles of length 3, with three distinct nodes, in the given graph.

```scala
def triangles(edges: DirectedGraph): MyList[Triangle] =
  ???
```

<details> <summary> Hint </summary>

You can make use of flatMap, map and filter.

</details>

Each cycle should appear only once. For instance, given the edges:

```
Cons((1, 2), Cons((2, 3), Cons((3, 1), Nil)))
```

You should return exactly one of the three following possibilities:

```
(1, 2, 3), (2, 3, 1), (3, 1, 2)
```

You are free to decide which of the three you return.

## Option Type

In last week’s exercises, we use a custom type `LookupResult` for the result of looking up in a context:

```scala
enum LookupResult:
  case Ok(v: Int)
  case NotFound
```

It’s always good to explore the [Scala standard library](https://scala-lang.org/api/3.x/). After all, why use a custom type when there is something suitable in the standard library?

Can you find a suitable type for LookupResult?

One suitable choice is already given by the title: the [Option](https://scala-lang.org/api/3.x/scala/Option.html#) type!
<details><summary> Is there any other suitable container in the standard library? </summary>

[Tuple](https://scala-lang.org/api/3.x/scala/Tuple$.html#), [Either](https://scala-lang.org/api/3.x/scala/util/Either.html#).

</details>

### Part 1. Basic Usage

The basic usage of `Option` type is as the return type of functions that might not always return a valid value.

Implement `findFirstEvenNumber` to return the first even number in the list, or `None` if there isn’t one.

```scala
def findFirstEvenNumber(l: List[Int]): Option[Int] =
  ???
```

### Part 2. Drawing Parallels with List in Standard Library

Notice that `Option` also has `map`, `flatMap`, `filter` just like `List`. Do you know why?
<details><summary> Hint </summary>

An option is like a list with only one element.
</details>

In this part, we use the `List` (`scala.collection.immutable.List`) from the standard library.

You can compare the definition of `map`, `flatMap` and `filter` in [standard library `List` methods](https://scala-lang.org/api/3.x/scala/collection/immutable/List.html#) with [`Option`](https://scala-lang.org/api/3.x/scala/Option.html#)’s. Do the definitions line up? What’s the difference between the definitions on `scala.collection.immutable.List` and our custom polymorphic lists `poly.List`?

1. Implement `parseStringToInt` and `findSquareRoot`. Then, define `findSquartRootFromString` to chain these two functions to parse a string and find its square root.

```scala
    def parseStringToInt(s: String): Option[Int] =
      ???

    def findSquareRoot(n: Int): Option[Double] =
      ???

    def findSquareRootFromString(s: String): Option[Double] =
      ???
```

2.  🔜 Given a list of strings representing integers:

```scala

    val numberStrings: List[String] = List("1", "2", "star", "4")

```

Try to use `map` to convert them in integers. What issues do you face?

Now, use the member method `flatMap` of `scala.collection.immutable.List` and the `parseStringToInt` function to safely convert them.

```scala
    val numbers =
      TODO
```
**Check Yourself** 🔥

Can you do the same trick using our custom lists `poly.List` and definition of `flatMap` instead? Why?
<details><summary> Solution </summary>

No.

The fact that we can line up `List` and `Option` easily is because in the standard library, both `List` and `Option` are subtypes of `IterableOnce`, and signatures of useful methods make use of the supertype `InterableOnce`. For example, the signature of `flatMap` in `List` is `def flatMap[B](f: A => IterableOnce[B]): List[B]`.

We will cover this more advanced API at two points later in the course: first to introduce *comprehensions*, and then more generally *monads*.

</details>

## FoldLeft and Tail Recursion ⭐️

In Exercise 2, we learned about tail recursion.
Tail recursion is a special form of recursion where the recursive call is the last operation in the function, which allows the compiler to optimize the recursion by reusing the current function's stack frame for the next function call, effectively transforming the recursion into a loop, and therefore more stack memory efficient. 

### Sum

For example, `sum0` is not a tail recursive function:

```scala
def sum0(l: MyList[Int]): Int = l match
  case Nil         => 0
  case Cons(x, xs) => x + sum0(xs)
```

If you uncomment the test which tests `sum0` on a list with 50000 elements, it is very likely to fail on your machine due to stack overflow!

```scala
// test("sum0: large list"):
//   assertEquals(sum0(manyNumbers1), N)
```

Can you implement the sum algorithm using tail recursion?

```scala
def sum1(l: MyList[Int]): Int =
    // @tailrec // Uncomment this line.
    def sum(l: MyList[Int], acc: Int): Int =
        ???
    sum(l, 0)
```

In Scala, the `@tailrec` annotation is a directive for the compiler, indicating that the annotated method should be tail-recursive. If the method is not tail-recursive, the compiler will raise a compile-time error. 

### FoldLeft

Similar to `foldRight`, `foldLeft` processes the list from the leftmost (head) element to the rightmost element.

The main difference between foldLeft and foldRight is that foldLeft is typically implemented using tail recursion, while foldRight is the opposite.

1. Define `foldLeft`:

```scala
    // @tailrec // Uncomment this line.
    def foldLeft[A, B](base: B, f: (B, A) => B)(l: MyList[A]): B =
      ???
```

2. Define `sum0Fold` using `foldRight`, define `sum1Fold` using `foldLeft`:

```scala
    def sum0Fold(l: MyList[Int]): Int =
      ???
    def sum1Fold(l: MyList[Int]): Int =
      ???
```

3. Reimplement `reverseAppend` using `foldLeft`:

```scala
    def reverseAppend[A](l1: MyList[A], l2: MyList[A]): MyList[A] =
      ???
```
4. Implement `countEven` and `totalLength` using `foldLeft`. `CountEven` takes a list of integers and returns the number of even integers in the list; `totalLength` takes a list of strings and return the sum of each string’s length.

```scala
    val countEven: MyList[Int] => Int =
      TODO

    val totalLength: MyList[String] => Int =
      TODO
```

## Currying and Composition

<details><summary> Reminder </summary>

You can check the previous exercises for currying and composition in Exercise 5: Higher-order Functions.
</details>

### CurriedZipWith

Use `map` and `zip` to implement the curried version `curriedZipWith` of `zipWith`.
<details><summary> Defining polymorphic function values </summary>

Reference: [Polymorphic Function Types](https://docs.scala-lang.org/scala3/reference/new-types/polymorphic-function-types.html).


```scala
// A polymorphic method:
def foo[A](xs: List[A]): List[A] = ???

// A polymorphic function value:
val bar = [A] => (xs: List[A]) => foo[A](xs)
```

`bar` has type `[A] => List[A] => List[A]`. This type describes function values which take a type `A` as a parameter, then take a list of type `List[A]`, and return a list of the same type `List[A]`.
</details>

```scala
val curriedZipWith =
  TODO
```

### Polymorphic Composition 🔥

1. In previous exercises we defined a function `compose` to compose functions `f: Int => Double` and `g: Double => String`. Generalize this function to arbitrary pairs of types, using polymorphic argument types.

2. What is the neutral element for the generalized `compose`?

3. In previous exercises, we defined `andLifter` and `notLifter` for functions on `Int`. To make it more general, we can define `andLifter` for functions of arbitrary input types:

```scala
def andLifter[A](f: A => Boolean, g: A => Boolean): A => Boolean =
  a => f(a) && g(a)
```

… and we can generalize further! Look at the following four functions; do they have anything in common?

```scala
def orLifter[A](f: A => Boolean, g: A => Boolean): A => Boolean =
  a => f(a) || g(a)
def sumLifter[A](f: A => Int, g: A => Int): A => Int =
  a => f(a) + g(a)
def listConcatLifter[A, B](f: A => MyList[B], g: A => MyList[B]): A => MyList[B] =
  a => f(a) ++ g(a)
```

Write a `binaryLifter` higher-order function to capture the common pattern above, and use it to rewrite all four lifters that we’ve seen up to this point.

```scala
def binaryLifter[A, B, C](op: (B, B) => C)(f: A => B, g: A => B): A => C =
  ???
```

```scala
def andLifter1[A](f: A => Boolean, g: A => Boolean) =
  ???

def orLifter1[A](f: A => Boolean, g: A => Boolean) =
  ???

def sumLifter1[A](f: A => Int, g: A => Int) =
  ???

def listConcatLifter1[A, B](f: A => MyList[B], g: A => MyList[B]) =
  ???
```

4. Similarly, we can implement a `unaryLifter` to generate lifters like `notLifter`. Can you tell which function `unaryLifter` essentially is?

## Proofs on lists

### Composition of maps ⭐️

Prove that the following equivalence holds by using inductive reasoning:

```scala 
∀ (l: List[T]), l.map(f).map(g) === l.map(f `andThen` g)
```

(∀ is short for “for all”, so the statement above says: “forall list `l` of type `List[T]`, `l.map(f).map(g)` equals `l.map(f `andThen` g)`”.)

Here are the relevant axioms for this proof:

1. `Nil.map(f) === Nil`
2. `(x :: xs).map(f) === f(x) :: (xs.map(f))`
3. ``(f `andThen` g)(x) === g(f(x))``

Be very precise in your proof:

-   Make sure to state what you want to prove, and what your induction hypothesis is, if any.
-   Clearly state which axiom you use at each step, and when/if you use the induction hypothesis.
-   Use only one axiom / hypothesis at each step: applying two axioms requires two steps.
-   Underline the part of each expression on which you apply the axiom or hypothesis at each step.

### A more complicated proof (FP midterm 2016)

We want to implement a function `sum(list: List[Int]): Int`, which returns the sum of the elements of a list of `Int`-s. We can easily specify that function as follows:

```
(1)  sum(Nil) === 0
(2)  sum(x :: xs) === x + sum(xs)
```

If we naively translate this specification into a Scala implementation, we end up with a non-tail-recursive function. Instead, we implement it using `foldLeft`:

```scala
def betterSum(list: List[Int]): Int =
  list.foldLeft(0)(add)

def add(a: Int, b: Int): Int = a + b
```

However, that implementation is not obviously correct anymore. We would like to *prove* that it is correct for all lists of integers. In other words, we want to prove that

```
list.foldLeft(0)(add) === sum(list)
```

for all lists of integers.

In addition to the specification of `sum` (axioms `1`-`2`), you may use the following axioms:

```
(3)  Nil.foldLeft(z)(f) === z
(4)  (x :: xs).foldLeft(z)(f) === xs.foldLeft(f(z, x))(f)
(5)  add(a, b) === a + b
(6)  a + b === b + a
(7)  (a + b) + c === a + (b + c)
(8)  a + 0 === a
```

Axioms 3-5 follow from the implementations of `foldLeft` and `add`. Axioms 6-8 encode well-known properties of `Int.+`: commutativity, associativity, and neutral element.

Your task: Prove the following lemma by structural induction:

```
∀ (l: List[Int]) (z: Int), l.foldLeft(z)(add) === z + sum(l)
```

From that lemma, we can (with the help of axioms 6 and 8) derive that the implementation of `betterSum` is correct by substituting `0` for `z` in the lemma. You are not asked to do that last bit.

### A hard proof on `foldLeft` and `foldRight` 🔥

We have now seen two list-traversal functions. One, from left to right, called `foldLeft`; and another, from right-to-left, called `foldRight`. In this exercise, we’ll see how to relate them to each other.

Let’s look back at the definitions of `foldLeft` and `foldRight`:

```scala
def foldLeft[A, B](base: B, f: (B, A) => B)(l: List[A]): B = l match
  case Nil         => base
  case Cons(x, xs) => foldLeft(f(base, x), f)(xs)

def foldRight[A, B](f: (A, B) => B, base: B)(l: List[A]): B = l match
  case Nil         => base
  case Cons(x, xs) => f(x, foldRight(f, base)(xs))
```

Let’s see what they have in common on a concrete example:

- Using the substitution method, reduce the expression `foldLeft(base, f)(a :: b :: c :: d :: Nil)` for an arbitrary function `f` and arbitrary values `a, b, c, d`. Similarly, reduce the expression `foldRight(g, base)(a :: b :: c :: d :: Nil)` for an arbitrary function `g` and arbitrary values `a, b, c, d`.
<details><summary> Solution </summary>

```
      foldLeft(base, f)(a :: b :: c :: d :: Nil)
    = foldLeft(f(base, a), f)(b :: c :: d :: Nil)
    = foldLeft(f(f(base, a), b), f)(c :: d :: Nil)
    = foldLeft(f(f(f(base, a), b), c), f)(d :: Nil)
    = foldLeft(f(f(f(f(base, a), b), c), d), f)(Nil)
    = f(f(f(f(base, a), b), c), d)
```

```
      foldRight(g, base)(a :: b :: c :: d :: Nil)
    = g(a, foldRight(g, base)(b :: c :: d :: Nil))
    = g(a, g(b, foldRight(g, base)(c :: d :: Nil)))
    = g(a, g(b, g(c, foldRight(g, base)(d :: Nil))))
    = g(a, g(b, g(c, g(d, foldRight(g, base)(Nil)))))
    = g(a, g(b, g(c, g(d, base))))
```
</details>

- Based on the example above, conjecture a relation between `foldLeft` and `foldRight`.
<details><summary> Solution </summary>

Given type `A` and `B`, for any list `l: List[A]` , base value `base: B` and function `f: (A, B) => B`, the result of `foldRight(f, base)(l)` is the same as `foldLeft(base, (x, y) => f(y, x))(reverse(l))`.

</details>

Now, let’s prove this property!

Similar to the above exercises, we can specify `foldLeft`, `foldRight` and `reverse` as follows:

```scala
(1) Nil.foldLeft(z)(f) === z
(2) (x :: xs).foldLeft(z)(f) === xs.foldLeft(f(z, x))(f)
(3) Nil.foldRight(f)(z) === z 
(4) (x :: xs).foldRight(f)(z) === f(x, xs.foldRight(f)(z))
(5) Nil.reverse === Nil 
(6) (x :: xs).reverse === xs.reverse ++ (x :: Nil)
(7) Nil ++ l === l
(8) (x :: xs) ++ l === x :: (xs ++ l)
```

**Your task**: Prove the following lemma (called `fold_left_rev_right`) by structural induction:

```scala
fold_left_rev_right: ∀ [A, B] (l: List[A]) (f: (A, B) => B) (z: B), 
  l.reverse.foldRight(f)(z) === l.foldLeft(z)((a: A, b: B) => f(b, a))
```

## Simple Stack Machine Interpreter 🔥

A stack machine uses a last-in, first-out (LIFO) stack to hold short-lived temporary values. Assume the values are integers, then a stack can be viewed as a list of integers:

```scala
type Stack = MyList[Int]
```

Most of a stack machine’s instructions assume that operands will be from the stack, and results placed in the stack. We consider a simple stack machine that only supports the following instructions:

- `Push(v)`: Push an integer `v` into the top of the stack;
- `Pop`: Pop out the top of the stack;
- `Add`, `Sub`, `Mul`, `Div`: Pop out the top two elements of the stack, use them as operands of the operation, then push the result back to the stack.

Exceptions:

- For `Div`, a `DividedByZeroException` exception should be thrown when the divisor is zero.
- A `NotEnoughOperandsInStackException` exception should be thrown when the stack doesn’t have enough values for any instruction.

A program is a list of instructions:

```scala
type Program = MyList[Instruction]
```

For example, consider the expression `A * (B - C) + (D + E)`, written in reverse Polish notation as `A B C - * D E + +`. Compiling and running this on a simple stack machine would take the form:

```
 Program         # stack contents (leftmost = top = most recent):
 Push(A)         #           A
 Push(B)         #     B     A
 Push(C)         # C   B     A
 Sub             #     B-C   A
 Mul             #           A*(B-C)
 Push(D)         #     D     A*(B-C)
 Push(E)         # E   D     A*(B-C)
 Add             #     D+E   A*(B-C)
 Add             #           A*(B-C)+(D+E)
```

(The example is taken from [Wikipedia](https://en.wikipedia.org/wiki/Stack_machine).)

In this section, your task is to implement a simple stack machine interpreter. That is, implement a `interpreteProg` method which takes an initial stack and a program, then returns the stack after execution of the program. You must use `foldLeft` to implement it.

```scala
def interpreteProg(stack: Stack, program: Program): Stack =
  foldLeft(stack, interpreteInst)(program)
```

To do so, you need to first implement the `interpreteInst` function which interprets only one instruction each time:

```scala
  def interpreteInst(stack: Stack, inst: Instruction): Stack =
    ???
```

## Specifications: from English to Code (SE exercise)

<details><summary> Writing (Simple) Pre-/Post-conditions in Scala </summary>

In Scala, it’s common to use [`require`](https://scala-lang.org/api/3.x/scala/Predef$.html#require-8ac) and [`ensuring`](https://scala-lang.org/api/3.x/scala/Predef$.html#require-8ac) to specify pre- and post-conditions for functions, respectively. The `require` method checks a given condition (usually an input validation) and throws an `IllegalArgumentException` if the condition is not met. On the other hand, `ensuring` is used to validate the result of a function. It takes a predicate that the result must satisfy, and if not, an assertion error is thrown.

For example:

```scala
val eps = 0.00001f

def sqrt(x: Double): Double = {
  require(x >= 0)
  Math.sqrt(x)
} ensuring (res =>
    (x - res * res) <= eps && (x - res * res) >= -eps
)
```

</details>

In this section, you will practice translating specification from English to code.

1. Translate “The list of integers `l` is sorted in ascending order” into code. (Note that it means every element in list `l` is less than or equal to the next element.)

2. Translate “The output of function `norm` is not-negative” into code, where function `norm` is:

```scala
    def norm(v: Vector2): Double =
      Math.sqrt(v.x * v.x + v.y * v.y).toFloat
```

3. Translate the grading policy of this course (can be found in [Overall Grade](https://cs-214.epfl.ch/info/policies-schedule/#overall-grade)) into a function `OverallGrade`:

```scala
    def OverallGrade(labScore: Double, midtermScore: Double, finalScore: Double): Double =
      ???
```

## Finding bugs by writing specifications (SE exercise)

Writing preconditions and post-conditions for each function can help identify bugs.

The following code defines a class `BankAccount` and associated operations `deposit`, `withdraw` and `transfer`:

```scala
protected class BankAccount(private var _balance: Double):
  import AccOpResult.*

  def balance = _balance

  private def updateBalance(amount: Double): AccOpResult =
    val oldBalance = balance
    _balance = amount
    Ok(oldBalance)

  /** Deposits the specified amount into the bank account.
    *
    * @param amount
    *   The amount to be deposited. Must be non-negative.
    */
  def deposit(amount: Double): AccOpResult = {
    updateBalance(balance + amount)
  }

  /** Withdraws the specified amount from the bank account.
    *
    * @param amount
    *   The amount to be withdrawn. Must be non-negative.
    */
  def withdraw(amount: Double): AccOpResult = {
    if balance >= amount then
      updateBalance(balance - amount)
    else
      InsufficientFund(balance)
  }

  /** Transfers the specified amount from this bank account to `that` bank
    * account.
    *
    * @param amount
    *   The amount to be transferred. Must be non-negative.
    */
  def transfer(that: BankAccount, amount: Double): (AccOpResult, AccOpResult) = {
    if this.balance >= amount then
      (this.withdraw(amount), that.deposit(amount))
    else
      (InsufficientFund(this.balance), Ok(that.balance))
  }
```

Type `AccOpResult` is used to memorize a “screenshot” of the balance before the operation and whether the operation is successful:

```scala
enum AccOpResult:
  case Ok(oldBalance: Double)
  case InsufficientFund(oldBalance: Double)
```

1. Write the preconditions and postconditions for function `deposit`, `withdraw` and `transfer` using `require` and `ensuring`.

2. Can you identify the bugs in the code that violate these conditions?