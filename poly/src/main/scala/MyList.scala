package poly

final class EmptyListException extends Exception(f"Empty list")

enum MyList[+A]:
  case Nil
  case Cons(x: A, xs: MyList[A])

    def isEmpty: Boolean = this match
        case Nil => true
        case _   => false

    def head: A = this match
        case Nil        => throw EmptyListException()
        case Cons(x, _) => x

    def tail: MyList[A] = this match
        case Nil         => throw EmptyListException()
        case Cons(_, xs) => xs